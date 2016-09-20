package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.wm.lock.LockConstants;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.Inspection;
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
    protected com.wm.lock.entity.BluetoothDevice fix(BluetoothDevice device) {
        final List<com.wm.lock.entity.BluetoothDevice> list = list();
        final com.wm.lock.entity.BluetoothDevice result = findExist(list, device);
        if (result != null) {
            return convert(result, device);
        }
        return null;
    }

    private List<com.wm.lock.entity.BluetoothDevice> list() {
        final List<com.wm.lock.entity.BluetoothDevice> result = new ArrayList<>();
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);

        // 巡检任务对应的大门的蓝牙设备
        final Inspection inspection = bizService.findInspection(mInspectionId);
        if (!TextUtils.isEmpty(inspection.getLock_mac())) {
            final com.wm.lock.entity.BluetoothDevice device = new com.wm.lock.entity.BluetoothDevice();
            device.setMacAddress(inspection.getLock_mac());
            result.add(device);
        }

        // 指定分类的所有机柜的蓝牙设备
        final List<com.wm.lock.entity.BluetoothDevice> cabinetList = bizService.listInspectionItemCategoryBluetooth(mInspectionId, mCategory);
        if (!CollectionUtils.isEmpty(cabinetList)) {
            result.addAll(cabinetList);
        }

        return result;
    }

}
