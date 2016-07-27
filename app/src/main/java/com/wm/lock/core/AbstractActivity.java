package com.wm.lock.core;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


import com.wm.lock.R;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.core.utils.ToastUtils;

import java.lang.reflect.Method;

public abstract class AbstractActivity extends ActionBarActivity implements OnMenuItemClickListener {

    protected Bundle mSaveBundle;

    protected Toolbar mToolbar;
    protected TextView mTvTitle;

    protected boolean isDestroyed;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        int contentViewId = getContentViewId();
        if (contentViewId > 0) {
            setContentView(contentViewId);
        }

        if (arg0 != null) {
            mSaveBundle = arg0;
            onMemoryRecycled();
        } else {
            mSaveBundle = getIntent().getExtras();
            init();
        }
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        HardwareUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSaveBundle != null) {
            outState.putAll(mSaveBundle);
        }
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

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        setTitle(title);
    }

    public void setTitle(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        boolean isActionBarExist = (actionBar != null);
        if (mTvTitle == null) {
            if (isActionBarExist) {
                actionBar.setTitle(title);
            }
        } else {
            mTvTitle.setText(title);
            if (isActionBarExist) {
                actionBar.setTitle("");
            }
        }
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

    public void showTip(CharSequence message) {
        ToastUtils.showShortToast(getApplicationContext(), message);
    }

    public void showTip(int messageId) {
        ToastUtils.showShortToast(getApplicationContext(), messageId);
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

    public boolean isDestroy() {
        return isDestroyed;
    }

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mTvTitle = (TextView) mToolbar.findViewById(R.id.tv_title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setLogo(null);
            mToolbar.setOnMenuItemClickListener(this);
        } else {
            mTvTitle = (TextView) findViewById(R.id.tv_title);
        }
        HardwareUtils.setStatusBarColor(this, getStatusBarColor());
    }

    protected int getStatusBarColor() {
        return R.color.bg_title;
    }

    protected abstract int getContentViewId();

    protected abstract void init();

    protected void onMemoryRecycled() {
        HardwareUtils.restartApp(getApplicationContext());
    }

}
