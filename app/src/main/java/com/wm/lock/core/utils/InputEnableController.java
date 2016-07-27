package com.wm.lock.core.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by wm on 16/2/3.
 */
public final class InputEnableController implements TextWatcher {

    private Context mCtx;
    private EditText[] mEts;
    private CharSequence[] mInitSequences;
    private OnTargetEnableChangedListener mListener;
    private boolean mCanEmpty;

    public InputEnableController(Context ctx, EditText[] ets, OnTargetEnableChangedListener listener, boolean canEmpty) {
        this.mCtx = ctx;
        this.mEts = ets;
        this.mListener = listener;
        this.mCanEmpty = canEmpty;
        init();
    }

    public void release() {
        mCtx = null;
        mEts = null;
        mInitSequences = null;
        mListener = null;
    }

    private void init() {
        final int len = mEts.length;
        mInitSequences = new CharSequence[len];
        for (int i = 0; i < len; i++) {
            EditText et = mEts[i];
            mInitSequences[i] = et.getText().toString();
            et.addTextChangedListener(this);
        }
        setEnable(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setEnable(isEnable());
    }

    private void setEnable(boolean isEnable) {
        if (mListener != null) {
            mListener.onTargetEnableChanged(isEnable);
        }
    }

    private boolean isEnable() {
        if (!mCanEmpty) {
            for (EditText et : mEts) {
                if (TextUtils.isEmpty(et.getText().toString().trim())) {
                    return false;
                }
            }
        }

        for (int i = 0, len = mEts.length; i < len; i++) {
            EditText et = mEts[i];
            if (!TextUtils.equals(et.getText(), mInitSequences[i])) {
                return true;
            }
        }
        return false;
    }

    public static interface OnTargetEnableChangedListener {
        public void onTargetEnableChanged(boolean isEnable);
    }

}
