package com.wm.lock.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class BluetoothService extends Service {

    public static final String ACTION_DATA_AVAILABLE = "cn.znv.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "cn.znv.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "cn.znv.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "cn.znv.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String EXTRA_DATA = "cn.znv.bluetooth.le.EXTRA_DATA";
    public static final UUID READ_BACK_DATA;
    private static final String TAG;
    private final IBinder mBinder;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private final BluetoothGattCallback mGattCallback;

    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    public BluetoothService() {
        this.mGattCallback = new BluetoothGattCallback() {
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == 2) {
                    BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_GATT_CONNECTED);
                    Log.i(BluetoothService.TAG, "Connected to GATT server.");
                    final boolean discoverResult = BluetoothService.this.mBluetoothGatt.discoverServices();
                    Log.i(BluetoothService.TAG, "Attempting to start service discovery:" + discoverResult);
                } else if (newState == 0) {
                    String intentAction = BluetoothService.ACTION_GATT_DISCONNECTED;
                    Log.i(BluetoothService.TAG, "Disconnected from GATT server.");
                    BluetoothService.this.broadcastUpdate(intentAction);
                }
            }

            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == 0) {
                    BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
                } else {
                    Log.w(BluetoothService.TAG, "onServicesDiscovered received: " + status);
                }
            }

            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (status == 0) {
                    BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_DATA_AVAILABLE, characteristic);
                }
            }

            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                Log.v(BluetoothService.TAG, "onCharacteristicChanged");
                BluetoothService.this.broadcastUpdate(BluetoothService.ACTION_DATA_AVAILABLE, characteristic);
            }
        };
        this.mBinder = new LocalBinder();
    }

    static {
        TAG = BluetoothService.class.getSimpleName();
        READ_BACK_DATA = UUID.fromString(BluetoothConfig.READBACKDATA);
    }

    private void broadcastUpdate(String action) {
        sendBroadcast(new Intent(action));
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        if (READ_BACK_DATA.equals(characteristic.getUuid())) {
            byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                intent.putExtra(EXTRA_DATA, data);
            }
        }
        sendBroadcast(intent);
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (this.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return false;
    }

    public boolean connect(String address) {
        if (this.mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        } else if (this.mBluetoothDeviceAddress == null || !address.equals(this.mBluetoothDeviceAddress) || this.mBluetoothGatt == null) {
            BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
            if (device == null) {
                Log.w(TAG, "Device not found.  Unable to connect.");
                return false;
            }
            this.mBluetoothGatt = device.connectGatt(this, false, this.mGattCallback);
            Log.d(TAG, "Trying to create a new connection.");
            this.mBluetoothDeviceAddress = address;
            return true;
        } else {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (this.mBluetoothGatt.connect()) {
                return true;
            }
            return false;
        }
    }

    public void disconnect() {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.disconnect();
        }
    }

    public void close() {
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    public boolean writeData(BluetoothGattCharacteristic gattCharacteristic, byte[] param) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        gattCharacteristic.setValue(param);
        return this.mBluetoothGatt.writeCharacteristic(gattCharacteristic);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        final boolean writeReuslt= this.mBluetoothGatt.writeCharacteristic(characteristic);
        Log.i("DeviceControlActivity", "数据发送：" + writeReuslt);
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.readCharacteristic(characteristic);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        this.mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        for (BluetoothGattDescriptor dp : characteristic.getDescriptors()) {
            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            this.mBluetoothGatt.writeDescriptor(dp);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.mBluetoothGatt == null) {
            return null;
        }
        return this.mBluetoothGatt.getServices();
    }

    public BluetoothGattService getService(String uuid) {
        if (this.mBluetoothGatt == null) {
            return null;
        }
        return this.mBluetoothGatt.getService(UUID.fromString(uuid));
    }

}
