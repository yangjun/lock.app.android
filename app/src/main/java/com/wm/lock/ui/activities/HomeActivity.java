package com.wm.lock.ui.activities;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

import com.viewpagerindicator.TabPageIndicator;
import com.wm.lock.R;
import com.wm.lock.bugly.BuglyManager;
import com.wm.lock.core.adapter.PagerTabAdapter;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.http.Rest;
import com.wm.lock.ui.fragments.InspectionListFragment;
import com.wm.lock.ui.fragments.InspectionListInProcessFragment_;
import com.wm.lock.ui.fragments.InspectionListPendingFragment_;
import com.wm.lock.ui.fragments.InspectionListSubmitFailFragment_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wangmin on 16/7/27.
 */

@EActivity
public class HomeActivity extends BaseActivity {

    @Bean
    Rest mRest;

    @ViewById(R.id.fl_header)
    View mContainerHeader;

    @ViewById(R.id.v_indicator)
    View mVIndicator;

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_home;
    }

    @Override
    protected void init() {
        setupActionBar();
        setupTab();
        BuglyManager.checkUpgradeSilent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateIndicator();
    }

    @Click(R.id.tv_open_door)
    void onOpenDoorClick() {
        RedirectUtils.goActivity(this, OpenDoorActivity_.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(R.id.menu_setting)
    void onSettingClick() {
        RedirectUtils.goActivity(this, SettingActivity_.class);
    }

    private void setupActionBar() {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContainerHeader.getLayoutParams();
        params.topMargin = HardwareUtils.getStatusBarHeight(getApplicationContext());
        mContainerHeader.setLayoutParams(params);

        setBackBtnVisible(false);
        mToolbar.setBackgroundResource(Color.TRANSPARENT);
    }

    private void setupTab() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        PagerTabAdapter.TabItem[] tabItems = new PagerTabAdapter.TabItem[] {
                new PagerTabAdapter.TabItem("待处理", InspectionListPendingFragment_.builder().build()),
                new PagerTabAdapter.TabItem("处理中", InspectionListInProcessFragment_.builder().build()),
                new PagerTabAdapter.TabItem("未提交", InspectionListSubmitFailFragment_.builder().build()),
        };

        PagerTabAdapter adapter = new PagerTabAdapter(this, tabItems);
        pager.setAdapter(adapter);

        final TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        // FIXME 通过这种方式来刷新tab的标题
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                indicator.notifyDataSetChanged();
            }
        }, 3000);
    }

    private void updateIndicator() {
        final boolean hasNew = BuglyManager.hasUpgradeInfo(getApplicationContext());
        mVIndicator.setVisibility(hasNew ? View.VISIBLE : View.GONE);
    }

    public void updateCount(long count, InspectionListFragment fragment) {
        // TODO
    }

}
