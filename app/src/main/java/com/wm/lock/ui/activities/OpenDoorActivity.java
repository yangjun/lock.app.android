package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothDevice;

import com.wm.lock.entity.LockDevice;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class OpenDoorActivity extends OpenLockActivity {

    @Override
    protected LockDevice fix(BluetoothDevice device) {
        final List<LockDevice> list =  list();
        return findExist(list, device);
    }

    private List<LockDevice> list() {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
        final List<LockDevice> result =  bizService.listInspectionBluetooth(constructParam());
        return result;
    }

    private InspectionQueryParam constructParam() {
        final InspectionQueryParam param = new InspectionQueryParam();
        param.setUser_job_number(loginUser().getJobNumber());
        return param;
    }

}
