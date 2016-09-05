package com.wm.lock.ui.activities;

import com.wm.lock.R;
import com.wm.lock.core.widget.GesturePrintView;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.EActivity;

@EActivity
public class GesturePrintVerifyActivity extends GesturePrintActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.act_gestureprint_verify;
    }

    @Override
    protected void init() {
        super.init();
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        final String pin = userService.getLoginedInfo().getGesturePwd();

        mTvIndicator.setText(R.string.gestureprint_verify_hint);
        mGpv.setValidPassword(pin);
    }

    @Override
    public void onRepeat() {
    }

    @Override
    protected String getFailMsg(GesturePrintView.Error error) {
        if (error == GesturePrintView.Error.NOT_MATCH) {
            return getString(R.string.gestureprint_verify_fail);
        }
        else {
            return super.getFailMsg(error);
        }
    }

}
