package com.wm.lock.ui.activities;

import android.bluetooth.BluetoothDevice;

import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class OpenDoorActivity extends OpenLockActivity {

    @Override
    protected com.wm.lock.entity.BluetoothDevice fix(BluetoothDevice device) {
        final List<com.wm.lock.entity.BluetoothDevice> list =  list();
        final com.wm.lock.entity.BluetoothDevice result = findExist(list, device);
        return convert(result, device);
    }

    private List<com.wm.lock.entity.BluetoothDevice> list() {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
        final List<com.wm.lock.entity.BluetoothDevice> result =  bizService.listInspectionBluetooth(constructParam());
        return result;
    }

    private InspectionQueryParam constructParam() {
        final InspectionQueryParam param = new InspectionQueryParam();
        param.setUser_job_number(loginUser().getJobNumber());
        return param;
    }

}
