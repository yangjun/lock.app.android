package com.wm.lock.ui.fragments;

import android.os.Bundle;

import com.wm.lock.LockConstants;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.ui.activities.InspectionConstructActivity_;

import org.androidannotations.annotations.EFragment;

@EFragment
public class InspectionListInProcessFragment extends InspectionListFragment {

    @Override
    protected InspectionQueryParam getQueryParam() {
        final InspectionQueryParam result = super.getQueryParam();
        result.setState(InspectionState.IN_PROCESS);
        return result;
    }

    @Override
    protected void onItemClick(Inspection item) {
        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, item.getId_());
        bundle.putString(LockConstants.TITLE, item.getPlan_name());
        bundle.putBoolean(LockConstants.BOOLEAN, true);
        RedirectUtils.goActivity(mActivity, InspectionConstructActivity_.class, bundle);
    }

    public void receive(Inspection inspection) {
        reload();
    }

}
