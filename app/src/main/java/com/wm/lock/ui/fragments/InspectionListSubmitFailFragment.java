package com.wm.lock.ui.fragments;

import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;

import org.androidannotations.annotations.EFragment;

@EFragment
public class InspectionListSubmitFailFragment extends InspectionListFragment {

    @Override
    protected InspectionQueryParam getQueryParam() {
        final InspectionQueryParam result = super.getQueryParam();
        result.setState(InspectionState.SUBMIT_FAIL);
        return result;
    }

    @Override
    protected void onItemClick(Inspection item) {
        // TODO
    }

}
