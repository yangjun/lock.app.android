package com.wm.lock.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;

import com.wm.lock.dto.InspectionNewDto;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;

import org.androidannotations.annotations.EFragment;
import org.apache.http.util.VersionInfo;

import de.greenrobot.event.EventBus;

@EFragment
public class InspectionListPendingFragment extends InspectionListFragment {

    private boolean needReload = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needReload) {
            reload();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && needReload) {
            reload();
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    public void onEventMainThread(InspectionNewDto dto) {
        if (isPaused || isHidden()) {
            needReload = true;
        }
        else {
            reload();
        }
    }

    @Override
    protected InspectionQueryParam getQueryParam() {
        final InspectionQueryParam result = super.getQueryParam();
        result.setState(InspectionState.PENDING);
        return result;
    }

    @Override
    protected void onItemClick(Inspection item) {
        // TODO
    }

}
