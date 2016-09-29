package com.wm.lock.entity;

import java.util.List;

public class LockDeviceGroup {

    private List<LockDevice> locks;

    public List<LockDevice> getLocks() {
        return locks;
    }

    public void setLocks(List<LockDevice> locks) {
        this.locks = locks;
    }
}
