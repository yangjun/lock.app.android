package com.wm.lock.ui.fragments;

import com.wm.lock.R;
import com.wm.lock.entity.Inspection;

public abstract class InspectionListFragment extends BaseFragment {

    @Override
    protected int getContentViewId() {
        return R.layout.frag_inspection_list;
    }

    @Override
    protected void init() {

    }

    public abstract int getItemCount();

    public abstract void onItemClick(Inspection item);

}
