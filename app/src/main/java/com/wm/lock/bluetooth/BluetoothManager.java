package com.wm.lock.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.wm.lock.R;

public class BluetoothManager {

    private static final long SCAN_PERIOD = 60000;

    private boolean mScanning;
    private Handler mHandler;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private ScanCallback mScanCallback;
    private Runnable mScanTimeoutRunnable;

    private BluetoothService mBluetoothService;
    private ServiceConnection mServiceConnection;

    private BluetoothManager() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    private static class InstanceHolder {
        static final BluetoothManager instance = new BluetoothManager();
    }

    public static BluetoothManager getInstance() {
        return InstanceHolder.instance;
    }

    public String checkHardware(Context ctx) {
        if (getAdapter(ctx) == null) {
            return "设备不支持蓝牙";
        }

        if (!ctx.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return "设备不支持BLE";
        }

        return null;
    }

    public boolean isEnabled(Context ctx) {
        return getAdapter(ctx).isEnabled();
    }

    public Intent requestEnableIntent() {
        return new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    }

    public void startScan(Context ctx, ScanCallback callback) {
        mScanCallback = callback;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mScanCallback != null) {
                    mScanCallback.onPreExecute();
                }
            }
        });
        scan(ctx, true);
    }

    public void stopScan(Context ctx) {
        scan(ctx, false);
    }

    public void bind(Context ctx, final String address) {
        final Intent serviceIntent = new Intent(ctx, BluetoothService.class);
        mServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mBluetoothService = ((BluetoothService.LocalBinder) service).getService();
                mBluetoothService.initialize();
                mBluetoothService.connect(address);
            }

            public void onServiceDisconnected(ComponentName componentName) {
                mBluetoothService = null;
            }
        };
        ctx.bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind(Context ctx) {
        if (mServiceConnection != null) {
            ctx.unbindService(mServiceConnection);
            mServiceConnection = null;
            mBluetoothService = null;
        }
    }

    public BluetoothService getBluetoothService() {
        return mBluetoothService;
    }






    private synchronized void scan(Context ctx, boolean enable) {
        final BluetoothAdapter adapter = getAdapter(ctx);
        if (enable) {
            mScanTimeoutRunnable = new Runnable() {
                public void run() {
                    mScanning = false;
                    adapter.stopLeScan(getScanCallback());
                    if (mScanCallback != null) {
                        mScanCallback.onComplete();
                    }
                }
            };
            mHandler.postDelayed(mScanTimeoutRunnable, SCAN_PERIOD);
            mScanning = true;
            adapter.startLeScan(getScanCallback());
        }
        else {
            mScanning = false;
            if (mScanTimeoutRunnable != null) {
                mHandler.removeCallbacks(mScanTimeoutRunnable);
            }
            if (mLeScanCallback != null) {
                adapter.stopLeScan(mLeScanCallback);
            }
//            if (mScanCallback != null) {
//                mScanCallback.onComplete();
//            }
        }
    }

    private BluetoothAdapter getAdapter(Context ctx) {
        final android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return null;
        }
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter;
    }

    private BluetoothAdapter.LeScanCallback getScanCallback() {
        if (mLeScanCallback == null) {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if (mScanCallback != null) {
                        mScanCallback.onScan(device, rssi, scanRecord);
                    }
                }
            };
        }
        return mLeScanCallback;
    }

    public static interface ScanCallback {
        public void onPreExecute();
        public void onScan(BluetoothDevice device, int rssi, byte[] scanRecord);
        public void onComplete();
    }

}
