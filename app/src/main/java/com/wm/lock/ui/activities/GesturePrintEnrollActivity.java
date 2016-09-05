package com.wm.lock.ui.activities;

import com.wm.lock.R;
import com.wm.lock.core.widget.GesturePrintView;

import org.androidannotations.annotations.EActivity;

@EActivity
public class GesturePrintEnrollActivity extends GesturePrintActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.act_gestureprint_enroll;
    }

    @Override
    protected void init() {
        super.init();
        mTvIndicator.setText(R.string.gestureprint_enroll_hint);
    }

    @Override
    public void onRepeat() {
        updateIndicator(R.string.gestureprint_enroll_repeat, R.color.txt_content);
    }

    @Override
    protected String getFailMsg(GesturePrintView.Error error) {
        if (error == GesturePrintView.Error.NOT_MATCH) {
            return getString(R.string.gestureprint_enroll_repeat_fail);
        }
        else {
            return super.getFailMsg(error);
        }
    }

}
