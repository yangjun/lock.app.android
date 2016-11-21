package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothDevice;

import com.wm.lock.entity.LockDevice;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class OpenDoorActivity extends OpenLockActivity {

    private List<LockDevice> mAllList;

    @Override
    void onReScanClick() {
        mAllList = null;
        super.onReScanClick();
    }

    @Override
    protected LockDevice fix(BluetoothDevice device) {
        final List<LockDevice> list = list();
        return findExist(list, device);
//        final LockDevice result = new LockDevice();
//        result.setLock_mac(device.getAddress());
//        result.setLock_name(device.getName());
//        return result;
    }

    private List<LockDevice> list() {
        if (mAllList == null) {
//            mAllList = new ArrayList<>();
//            final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
//
//            // 巡检项对应的门锁列表
//            final List<LockDevice> inspectionLockList = bizService.listInspectionBluetooth(constructParam());
//            if (!CollectionUtils.isEmpty(inspectionLockList)) {
//                mAllList.addAll(inspectionLockList);
//            }
//
//            // 授权的锁列表
//            final List<LockDevice> authLockList = bizService.listLockDevice(loginUser().getJobNumber());
//            final List<LockDevice> diffList = new ArrayList<>();
//            CollectionUtils.diff(mAllList, authLockList, diffList, new Comparator<LockDevice>() {
//                @Override
//                public int compare(LockDevice lhs, LockDevice rhs) {
//                    return lhs.getLock_mac().equals(rhs.getLock_mac()) ? 0 : 1;
//                }
//            });
//            if (!diffList.isEmpty()) {
//                mAllList.addAll(diffList);
//            }

            final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
            mAllList = bizService.listLockDevice(loginUser().getJobNumber());
        }
        return mAllList;
    }

//    private InspectionQueryParam constructParam() {
//        final InspectionQueryParam param = new InspectionQueryParam();
//        param.setUser_job_number(loginUser().getJobNumber());
//        return param;
//    }

}
