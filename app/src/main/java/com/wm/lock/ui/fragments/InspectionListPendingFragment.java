package com.wm.lock.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wm.lock.R;
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.dto.InspectionNewDto;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.ui.activities.HomeActivity;
import com.wm.lock.websocket.WebSocketWriter;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

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
    protected void onItemClick(final Inspection item) {
        final CharSequence[] items = new CharSequence[]{
                "1. " + mActivity.getString(R.string.label_receive_task),
                "2. " + mActivity.getString(R.string.label_refuse_task)
        };
        DialogManager.showSingleChoiceDialog(mActivity, R.string.label_dialog_options, items, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                switch (i) {
                    case 0:
                        receive(item);
                        break;

                    case 1:
                        refuse(item);
                        break;
                }
                return false;
            }
        }, false);
    }

    private void refuse(final Inspection item) {
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.inflate_dialog_input, null);
        final EditText editText = (EditText) view;
        editText.setHint(R.string.hint_refuse_task);
        final Dialog dialog = DialogManager.getBuilder(mActivity, getString(R.string.label_refuse_task), false)
                .customView(view, true)
                .negativeText(R.string.cancel)
                .positiveText(R.string.concern)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        final String reason = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(reason)) {
                            mActivity.showTip(R.string.empty_refuse_task);
                            showDialogDelay(dialog);
                        } else {
                            doRefuse(item, reason);
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                    }
                })
                .build();
        HardwareUtils.showKeyboard(dialog);
        dialog.show();
    }

    @UiThread(delay = 200)
    void showDialogDelay(Dialog dialog) {
        dialog.show();
    }

    private void doRefuse(final Inspection item, final String reason) {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
        new AsyncExecutor().execute(new AsyncWork<Void>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onSuccess(Void result) {
                mListFragment.remove(item);
                updateTabTitleCount(--mItemCount);
                bizService.deleteInspection(item.getId_());
                mActivity.showTip(R.string.message_refuse_task_success);
            }

            @Override
            public void onFail(Exception e) {
                Logger.p("fail to refuse inspection", e);
                mActivity.showTip(R.string.message_refuse_task_fail);
            }

            @Override
            public Void onExecute() throws Exception {
                bizService.refuseInspection(item, reason);
                WebSocketWriter.refuseInspection(item, reason);
                return null;
            }
        });
    }

    private void receive(final Inspection item) {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
        new AsyncExecutor().execute(new AsyncWork<Void>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onSuccess(Void result) {
                mListFragment.remove(item);
                updateTabTitleCount(--mItemCount);
                mActivity.showTip(R.string.message_receive_task_success);
                ((HomeActivity) mActivity).receive(item);
            }

            @Override
            public void onFail(Exception e) {
                Logger.p("fail to refuse inspection", e);
                mActivity.showTip(R.string.message_receive_task_fail);
            }

            @Override
            public Void onExecute() throws Exception {
                bizService.receiveInspection(item);
                WebSocketWriter.receiveInspection(item);
                return null;
            }
        });
    }

}
