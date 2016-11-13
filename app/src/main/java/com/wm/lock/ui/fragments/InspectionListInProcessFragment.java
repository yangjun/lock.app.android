package com.wm.lock.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wm.lock.LockConstants;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.ui.activities.HomeActivity;
import com.wm.lock.ui.activities.InspectionConstructActivity_;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;

@EFragment
public class InspectionListInProcessFragment extends InspectionListFragment {

    private static final int REQUEST_CONSTRUCT = 11;

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
        bundle.putString(LockConstants.FLAG, item.getPlan_id());
        bundle.putString(LockConstants.NAME, item.getRoom_name());
        bundle.putString(LockConstants.TITLE, item.getPlan_name());
        bundle.putBoolean(LockConstants.BOOLEAN, true);
        RedirectUtils.goActivityForResult(this, InspectionConstructActivity_.class, bundle, REQUEST_CONSTRUCT);
    }

    public void receive(Inspection inspection) {
        reload();
    }

    @OnActivityResult(REQUEST_CONSTRUCT)
    void onConstructResult(int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
            case Activity.RESULT_FIRST_USER:
                ((HomeActivity) mActivity).reloadSubmitFail();
                reloadDelay();
                break;
        }
    }

}
