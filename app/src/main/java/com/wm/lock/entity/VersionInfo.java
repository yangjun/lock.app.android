package com.wm.lock.entity;

/**
 * Created by Administrator on 2016/3/31.
 */
public class VersionInfo {

    private String version;
    private String info;
    private String[] fixlist;
    private String address;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String[] getFixlist() {
        return fixlist;
    }

    public void setFixlist(String[] fixlist) {
        this.fixlist = fixlist;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
