package com.wm.lock.entity;

/**
 * 蓝牙设备信息
 */
public class BluetoothDevice {

    /** 名称 */
    private String name;

    /** mac地址 */
    private String macAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

}
