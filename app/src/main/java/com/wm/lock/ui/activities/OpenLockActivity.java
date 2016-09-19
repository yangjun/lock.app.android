package com.wm.lock.ui.activities;

import android.view.Menu;

import com.wm.lock.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

@EActivity
public class OpenLockActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.act_open_lock;
    }

    @Override
    protected void init() {
        if (mSaveBundle != null) {

        }
        onReScanClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_open_lock, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(R.id.menu_rescan)
    void onReScanClick() {
        // TODO
    }

}
