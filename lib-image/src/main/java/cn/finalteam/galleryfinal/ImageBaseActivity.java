/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.galleryfinal;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Method;

import cn.finalteam.galleryfinal.utils.SystemBarTintManager;
import cn.finalteam.toolsfinal.ActivityManager;
import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午5:46
 */
public abstract class ImageBaseActivity extends ActionBarActivity implements OnMenuItemClickListener {

    protected Toolbar mToolbar;
    protected TextView mTvTitle;

    protected int mScreenWidth = 720;
    protected int mScreenHeight = 1280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager.getActivityManager().addActivity(this);
        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
        ActivityManager.getActivityManager().finishActivity(this);
    }

    public void toast(String msg) {
        ImageHandler.getInstance().getOperationProvider().notify(msg);
    }


    @Override
    public final boolean onMenuItemClick(MenuItem arg0) {
        onOptionsItemSelected(arg0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 菜单创建事件回调
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 通过这种方式，来显示下拉框中的图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void setTitle(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(null);
        }
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    /**
     * 设置标题栏的可见性
     */
    public void setActionBarVisible(boolean visible) {
        if (getSupportActionBar() == null) {
            return;
        }
        if (visible) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
        if (mTvTitle != null) {
            mTvTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置返回按钮的可见性
     */
    public void setBackBtnVisible(boolean visible) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(visible);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setupActionBar();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupActionBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setupActionBar();
    }

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mTvTitle = (TextView) mToolbar.findViewById(R.id.tv_title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_return);
            mToolbar.setLogo(null);
            mToolbar.setOnMenuItemClickListener(this);
        } else {
            mTvTitle = (TextView) findViewById(R.id.tv_title);
        }
        setStatusBarColor(R.color.colorPrimary);
    }

    protected void setStatusBarColor(int colorRes) {
        final Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (colorRes == android.R.color.transparent) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            else {
                window.setStatusBarColor(getResources().getColor(colorRes));
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorRes);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
