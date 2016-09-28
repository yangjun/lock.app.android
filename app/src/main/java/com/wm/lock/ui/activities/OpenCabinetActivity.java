package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothDevice;

import com.wm.lock.LockConstants;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.LockDevice;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class OpenCabinetActivity extends OpenLockActivity {

    private long mInspectionId;
    private String mCategory;

    @Override
    protected void init() {
        super.init();
        mInspectionId = mSaveBundle.getLong(LockConstants.ID);
        mCategory = mSaveBundle.getString(LockConstants.DATA);
    }

    @Override
    protected LockDevice fix(BluetoothDevice device) {
        final List<LockDevice> list = list();
        return findExist(list, device);
    }

    private List<LockDevice> list() {
        final List<LockDevice> result = new ArrayList<>();
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);

//        // 巡检任务对应的大门的蓝牙设备
//        final Inspection inspection = bizService.findInspection(mInspectionId);
//        if (!TextUtils.isEmpty(inspection.getLock_mac())) {
//            final LockDevice device = new LockDevice();
//            device.setMacAddress(inspection.getLock_mac());
//            result.add(device);
//        }

        // 指定分类的所有机柜的蓝牙设备
        final List<LockDevice> cabinetList = bizService.listInspectionItemCategoryBluetooth(mInspectionId, mCategory);
        if (!CollectionUtils.isEmpty(cabinetList)) {
            result.addAll(cabinetList);
        }

        return result;
    }

}
