package com.wm.lock.dto;

public class DataWriteFailDto {

    public static final int ERROR_NETWORK = 1;
    public static final int ERROR_OTHER = -1;

    private int error;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
