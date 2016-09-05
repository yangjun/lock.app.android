package com.wm.lock.ui.activities;

import android.view.Menu;

import com.wm.lock.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

/**
 * Created by wangmin on 16/9/5.
 */
@EActivity
public class OpenDoorActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.act_open_door;
    }

    @Override
    protected void init() {
        onReScanClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_open_door, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(R.id.menu_rescan)
    void onReScanClick() {
        // TODO
    }

}
