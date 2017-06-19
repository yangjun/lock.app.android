package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.adapter.BlueDeviceLockListAdapter;
import com.wm.lock.bluetooth.BluetoothManager;
import com.wm.lock.core.callback.Injector;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.entity.LockDevice;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EActivity
public abstract class OpenLockActivity extends BaseActivity implements BluetoothManager.ScanCallback {

    private static final long DELAY = 500;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_OPEN = 2;

    @ViewById(R.id.ll_progress)
    View mVLoading;

    @ViewById(R.id.lst)
    ListView mListView;

    private BlueDeviceLockListAdapter mListAdapter;
    private boolean mScanning = false;
    protected List<LockDevice> mDeviceList;
    private boolean mIsRequestEnable = false;

    private Handler mHandler = new Handler();
    private Runnable mRefreshRunnable;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        showTip(R.string.message_bluetooth_disconnect);
                        finish();
                        break;
                }
            }
        }
    };

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
                final LockDevice device = (LockDevice) parent.getItemAtPosition(position);
                final Bundle bundle = new Bundle();
                bundle.putString(LockConstants.DATA, device.getLock_mac());
                bundle.putString(LockConstants.NAME, device.getLock_name());
                RedirectUtils.goActivityForResult(OpenLockActivity.this, LockControlActivity_.class, bundle, REQUEST_OPEN);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        // 如果没有扫描到设备，立即重新扫描
        if (!mIsRequestEnable && mDeviceList.isEmpty()) {
            onReScanClick();
        }
    }

    @Override
    protected void onPause() {
        stopScan();
        mScanning = false;
        mVLoading.setVisibility(View.GONE);
        invalidateOptionsMenu();
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        stopRefresh();
        super.onDestroy();
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
    void onRequestEnableResult(int resultCode, Intent data) {
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

    @OnActivityResult(REQUEST_OPEN)
    void onOpenResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onPreExecute() {
        mScanning = true;
        mVLoading.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    @Override
    public void onScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final LockDevice bluetoothDevice = filter(device);
                if (bluetoothDevice != null) {
                    mDeviceList.add(bluetoothDevice);
                    mListAdapter.notifyDataSetChanged();
//                    startRefresh();
                }
            }
        });
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

    private LockDevice filter(BluetoothDevice device) {
        if (findExist(mDeviceList, device) != null) {
            return null;
        }
        return fix(device);
    }

//    protected void startRefresh() {
//        stopRefresh();
//        mRefreshRunnable = new Runnable() {
//            @Override
//            public void run() {
//                Collections.sort(mDeviceList, new LockDeviceComparator());
//                mListAdapter.notifyDataSetChanged();
//            }
//        };
//        mHandler.postDelayed(mRefreshRunnable, DELAY);
//    }
//
//    private void stopRefresh() {
//        if (mRefreshRunnable != null) {
//            mHandler.removeCallbacks(mRefreshRunnable);
//            mRefreshRunnable = null;
//        }
//    }

    protected LockDevice findExist(List<LockDevice> list, BluetoothDevice device) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (LockDevice item : list) {
            if (item.getLock_mac().equals(device.getAddress())) {
                return item;
            }
        }
        return null;
    }

    protected LockDevice convert(BluetoothDevice device) {
        final LockDevice result = new LockDevice();
        result.setLock_mac(device.getAddress());
        result.setLock_name(device.getName());
        return result;
    }

    protected abstract LockDevice fix(BluetoothDevice device);

    private static class LockDeviceComparator implements Comparator<LockDevice> {

        @Override
        public int compare(LockDevice s, LockDevice t1) {
            final String firstLetterS = s.getFirst_letter();
            final String firstLetterT = t1.getFirst_letter();
            if (TextUtils.isEmpty(firstLetterS) || TextUtils.isEmpty(firstLetterT)) {
                return -1;
            }

            final int val1 = (int) firstLetterS.charAt(0);
            final int val2 = (int) firstLetterT.charAt(0);
            if (val1 > val2) {
                return 1;
            }
            if (val2 > val1) {
                return -1;
            }
            return 0;
        }
    }

}
