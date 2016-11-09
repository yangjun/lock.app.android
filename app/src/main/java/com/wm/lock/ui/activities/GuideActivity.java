package com.wm.lock.ui.activities;

import android.view.View;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.core.widget.GuideGroup;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.sys.ISysService;

import org.androidannotations.annotations.EActivity;

/**
 * Created by wm on 15/10/15.
 */
@EActivity
public class GuideActivity extends BaseActivity {

    private GuideGroup mGuideGroup;
    private ISysService mSysService;

    /** 是否为启动时调用 */
    private boolean isByStart;

    @Override
    protected int getContentViewId() {
        return R.layout.act_guide;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        HardwareUtils.setStatusBarColor(this, R.color.txt_title);
    }

    @Override
    protected void init() {
        mSysService = ModuleFactory.getInstance().getModuleInstance(ISysService.class);
        isByStart = (mSaveBundle != null && mSaveBundle.getBoolean(LockConstants.FLAG, false));

//        if (isByStart && !mSysService.isNewInstall()) {
            next();
//        }
//        else {
//            setupGroup();
//        }
    }

    private void setupGroup() {
        final int[] icons = new int[] {R.mipmap.guide_0, R.mipmap.guide_1};
        mGuideGroup = (GuideGroup) findViewById(R.id.gg);
        mGuideGroup.setButtonVisible(isByStart);
        mGuideGroup.setText(getString(R.string.label_begin_app));
        mGuideGroup.setAutoNextInterval(5000);
        mGuideGroup.setGuideIcons(icons);
        mGuideGroup.setOnStartAppClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    private void next() {
        if (isByStart) {
            mSaveBundle.remove(LockConstants.FLAG);
//            RedirectUtils.goActivity(this, LoginActivity_bak_.class, mSaveBundle);
            RedirectUtils.goActivity(this, LoginActivity_.class, mSaveBundle);
        }
        finish();
        mSysService.setCacheAppVersion();
    }

}
