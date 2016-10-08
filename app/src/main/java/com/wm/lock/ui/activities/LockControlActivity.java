package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.bluetooth.BluetoothConfig;
import com.wm.lock.bluetooth.BluetoothManager;
import com.wm.lock.bluetooth.BluetoothService;
import com.wm.lock.bluetooth.DoorException;
import com.wm.lock.bluetooth.DoorManager;
import com.wm.lock.bluetooth.DoorPackageResult;
import com.wm.lock.entity.UserInfo;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.List;
import java.util.UUID;

@EActivity
public class LockControlActivity extends BaseActivity {

    public static final UUID RECEIVE_UUID = UUID.fromString(BluetoothConfig.READBACKDATA);
    public static final UUID SEND_UUID = UUID.fromString(BluetoothConfig.WRITEDATA);

    private DoorManager doorManager;
    private boolean mConnected = false;
    private String mDeviceAddress;
    private BluetoothGattCharacteristic mWriteGattCharacteristic;
    private boolean mFirstDisplayService = true;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
            }
            else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                fail(R.string.message_open_lock_try_connect_fail);
            }
            else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(BluetoothManager.getInstance().getBluetoothService().getSupportedGattServices());
                if (mFirstDisplayService) {
                    mFirstDisplayService = false;
                    openDelay();
                }
            }
            else if (BluetoothService.ACTION_DATA_AVAILABLE.equals(action)) {
                final byte[] receives = intent.getByteArrayExtra(BluetoothService.EXTRA_DATA);
                showResult(receives);
            }
            else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                finish();
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return -1;
    }

    @Override
    protected void init() {
        mDeviceAddress = mSaveBundle.getString(LockConstants.DATA);
        BluetoothManager.getInstance().bind(getApplicationContext(), mDeviceAddress);

        doorManager = new DoorManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, intentFilter);

        connect();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        BluetoothManager.getInstance().unbind(getApplicationContext());
        super.onDestroy();
    }

    private void showResult(byte[] data) {
        try {
            final DoorPackageResult result = doorManager.receive(data);
            if (result != null) {
                switch (result.code) {
                    case DoorPackageResult.SUCCESS:
                        success();
                        return;

                    default:
                        if (!TextUtils.isEmpty(result.description)) {
                            final String message = String.format(getString(R.string.message_open_lock_fail_reason), result.description);
                            fail(message);
                            return;
                        }
                        break;
                }
            }
            fail(R.string.message_open_lock_fail);
        } catch (DoorException e) {
            fail(e);
        }
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices != null) {
            for (BluetoothGattService gattService : gattServices) {
                for (BluetoothGattCharacteristic characteristic : gattService.getCharacteristics()) {
                    if (SEND_UUID.equals(characteristic.getUuid())) {
                        if (characteristic.getProperties() == 4) {
                            characteristic.getUuid();
                        }
                        mWriteGattCharacteristic = characteristic;
                    }
                    if (RECEIVE_UUID.equals(characteristic.getUuid())) {
                        BluetoothManager.getInstance().getBluetoothService().setCharacteristicNotification(characteristic, true);
                        if (characteristic.getProperties() == 16) {
                            characteristic.getUuid();
                        }
                    }
                }
            }
        }
    }

    private void connect() {
        showWaittingDialog(R.string.message_open_lock_try_connect);
        if (BluetoothManager.getInstance().getBluetoothService() != null) {
            mConnected = BluetoothManager.getInstance().getBluetoothService().connect(mDeviceAddress);
        }
    }

    @UiThread(delay = 300)
    void openDelay() {
        if (mWriteGattCharacteristic == null) {
            fail(R.string.message_open_lock_fail);
        }
        else {
            open();
        }
    }

    private void open() {
        dismissDialog();
        showWaittingDialog(R.string.message_open_lock_try_open);
        try {
            final UserInfo user = loginUser();
            final String pin = user.getJobNumber() + user.getLockPwd();
            final byte[] send = doorManager.open(pin);
            if (send != null) {
                BluetoothManager.getInstance().getBluetoothService().writeData(mWriteGattCharacteristic, send);
            }
        } catch (DoorException e) {
            fail(e);
        }
    }

    private void fail(DoorException e) {
        fail(e.getMessage());
    }

    private void fail(int messageId) {
        fail(getString(messageId));
    }

    private void fail(String reason) {
        showTip(reason);
        finish();
    }

    private void success() {
        dismissDialog();
        showTip(R.string.message_open_lock_success);
        setResult(RESULT_OK);
        finish();
    }

}
