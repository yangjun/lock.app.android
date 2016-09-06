package com.wm.lock.entity.params;

public class PageParam {

    /** 页码，从0开始 */
    private int index;

    /** 每页显示的数量 */
    private int limit;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
