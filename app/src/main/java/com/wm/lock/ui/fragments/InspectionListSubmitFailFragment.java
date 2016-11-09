package com.wm.lock.ui.fragments;

import android.os.Bundle;

import com.wm.lock.LockConstants;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.dto.InspectionResultDto;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.ui.activities.InspectionConstructActivity_;

import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment
public class InspectionListSubmitFailFragment extends InspectionListFragment {

    private boolean needReload = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needReload && getUserVisibleHint()) {
            reload();
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isPaused && needReload) {
                reload();
            }
        }
    }

    public void onEventMainThread(InspectionResultDto dto) {
        if (!dto.isSuccess()) {
            return;
        }

        if (isPaused || !getUserVisibleHint()) {
            needReload = true;
        }
        else {
            reload();
        }
    }

    @Override
    public void reload() {
        needReload = false;
        super.reload();
    }

    @Override
    protected InspectionQueryParam getQueryParam() {
        final InspectionQueryParam result = super.getQueryParam();
        result.setState(InspectionState.SUBMIT_FAIL);
        return result;
    }

    @Override
    protected void onItemClick(Inspection item) {
        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, item.getId_());
        bundle.putString(LockConstants.NAME, item.getRoom_name());
        bundle.putString(LockConstants.TITLE, item.getPlan_name());
        bundle.putBoolean(LockConstants.BOOLEAN, false);
        RedirectUtils.goActivity(mActivity, InspectionConstructActivity_.class, bundle);
    }

}
