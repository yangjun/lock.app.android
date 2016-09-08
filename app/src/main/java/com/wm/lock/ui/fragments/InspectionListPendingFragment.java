package com.wm.lock.ui.fragments;

import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wm.lock.R;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.dto.InspectionNewDto;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;

import org.androidannotations.annotations.EFragment;

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
        final CharSequence[] items = new CharSequence[]{
                "1. " + mActivity.getString(R.string.label_receive_task),
                "2. " + mActivity.getString(R.string.label_refuse_task)
        };
        DialogManager.showSingleChoiceDialog(mActivity, R.string.label_dialog_options, items, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                switch (i) {
                    case 0:
                        receive();
                        break;

                    case 1:
                        refuse();
                        break;
                }
                return false;
            }
        }, false);
    }

    private void refuse() {
        // TODO
    }

    private void receive() {
        // TODO
    }

}
