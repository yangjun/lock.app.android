package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wm.lock.R;
import com.wm.lock.adapter.BlueDeviceLockListAdapter;
import com.wm.lock.bluetooth.BluetoothManager;
import com.wm.lock.core.callback.Injector;
import com.wm.lock.dialog.DialogManager;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity
public abstract class OpenLockActivity extends BaseActivity implements BluetoothManager.ScanCallback {

    private static final int REQUEST_ENABLE_BT = 1;

    @ViewById(R.id.ll_progress)
    View mVLoading;

    @ViewById(R.id.lst)
    ListView mListView;

    private BlueDeviceLockListAdapter mListAdapter;
    private boolean mScanning = false;
    private List<com.wm.lock.entity.BluetoothDevice> mDeviceList;
    private boolean mIsRequestEnable = false;

    @Override
    protected int getContentViewId() {
        return R.layout.act_open_lock;
    }

    @Override
    protected void init() {
        mDeviceList = new ArrayList<>();
        mListAdapter = new BlueDeviceLockListAdapter(this, mDeviceList);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果没有扫描到设备，立即重新扫描
        if (!mIsRequestEnable && mDeviceList.isEmpty()) {
            onReScanClick();
        }
    }

    @Override
    protected void onPause() {
        stopScan();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_open_lock, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_rescan).setVisible(!mScanning);
        return super.onPrepareOptionsMenu(menu);
    }

    @OptionsItem(R.id.menu_rescan)
    void onReScanClick() {
        mDeviceList.clear();
        mListAdapter.notifyDataSetChanged();

        if (BluetoothManager.getInstance().isEnabled(getApplicationContext())) {
            startScan();
        }
        else {
            mIsRequestEnable = true;
            startActivityForResult(BluetoothManager.getInstance().requestEnableIntent(), REQUEST_ENABLE_BT);
        }
    }

    @OnActivityResult(REQUEST_ENABLE_BT)
    void onRequestBluetoothResult(int resultCode, Intent data) {
        mIsRequestEnable = false;
        switch (resultCode) {
            case RESULT_OK:
                startScan();
                break;

            default:
                finish();
                break;
        }
    }

    @Override
    public void onPreExecute() {
        mScanning = true;
        mVLoading.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    @Override
    public void onScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        final com.wm.lock.entity.BluetoothDevice bluetoothDevice = filter(device);
        if (bluetoothDevice != null) {
            mDeviceList.add(0, bluetoothDevice);
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onComplete() {
        mScanning = false;
        mVLoading.setVisibility(View.GONE);
        invalidateOptionsMenu();
        showReScanDialogIfEmpty();
    }

    private void startScan() {
        BluetoothManager.getInstance().startScan(getApplicationContext(), this);
    }

    private void stopScan() {
        BluetoothManager.getInstance().stopScan(getApplicationContext());
    }

    private void showReScanDialogIfEmpty() {
        if (mDeviceList.isEmpty()) {
            showReScanDialog();
        }
    }

    private void showReScanDialog() {
        DialogManager.showConfirmDialog(this,
                R.string.label_scan_null,
                getString(R.string.label_rescan_notify),
                false,
                new Injector() {
                    @Override
                    public void execute() {
                        finish();
                    }
                },
                new Injector() {
                    @Override
                    public void execute() {
                        onReScanClick();
                    }
                }
        );
    }

    private com.wm.lock.entity.BluetoothDevice filter(BluetoothDevice device) {
        if (findExist(mDeviceList, device) != null) {
            return null;
        }
        return fix(device);
    }

    protected com.wm.lock.entity.BluetoothDevice findExist(List<com.wm.lock.entity.BluetoothDevice> list, BluetoothDevice device) {
        for (com.wm.lock.entity.BluetoothDevice item : list) {
            if (item.getMacAddress().equals(device.getAddress())) {
                return item;
            }
        }
        return null;
    }

    protected com.wm.lock.entity.BluetoothDevice convert(com.wm.lock.entity.BluetoothDevice bluetoothDevice, BluetoothDevice device) {
        if (bluetoothDevice == null) {
            return convert(device);
        }
        if (TextUtils.isEmpty(bluetoothDevice.getName())) {
            bluetoothDevice.setName(device.getName());
        }
        return bluetoothDevice;
    }

    protected com.wm.lock.entity.BluetoothDevice convert(BluetoothDevice device) {
        final com.wm.lock.entity.BluetoothDevice result = new com.wm.lock.entity.BluetoothDevice();
        result.setMacAddress(device.getAddress());
        result.setName(device.getName());
        return result;
    }

    protected abstract com.wm.lock.entity.BluetoothDevice fix(BluetoothDevice device);

}
