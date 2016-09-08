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
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.http.Rest;
import com.wm.lock.ui.fragments.InspectionListFragment;
import com.wm.lock.ui.fragments.InspectionListInProcessFragment;
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

    @ViewById(R.id.pager)
    ViewPager mViewPager;

    @ViewById(R.id.indicator)
    TabPageIndicator mTabPagerIndicator;

    private PagerTabAdapter.TabItem[] mTabItems;

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

    @Override
    public void onBackPressed() {
        HardwareUtils.goHome(this);
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
        mTabItems = new PagerTabAdapter.TabItem[] {
                new PagerTabAdapter.TabItem(getTabTitle(R.string.label_pending), InspectionListPendingFragment_.builder().build()),
                new PagerTabAdapter.TabItem(getTabTitle(R.string.label_in_process), InspectionListInProcessFragment_.builder().build()),
                new PagerTabAdapter.TabItem(getTabTitle(R.string.label_submit_fail), InspectionListSubmitFailFragment_.builder().build()),
        };

        PagerTabAdapter adapter = new PagerTabAdapter(this, mTabItems);
        mViewPager.setAdapter(adapter);

        mTabPagerIndicator.setViewPager(mViewPager);

        updateTabTitle();
    }

    private void updateIndicator() {
        final boolean hasNew = BuglyManager.hasUpgradeInfo(getApplicationContext());
        mVIndicator.setVisibility(hasNew ? View.VISIBLE : View.GONE);
    }

    public void receive(Inspection inspection) {
        final int index = 1;
        mViewPager.setCurrentItem(index);
        ((InspectionListInProcessFragment) mTabItems[index].fragment).receive(inspection);
    }

    public void updateCount(long count, InspectionListFragment fragment) {
        for (PagerTabAdapter.TabItem tabItem : mTabItems) {
            if (tabItem.fragment == fragment) {
                updateTabTitle(tabItem, count);
                break;
            }
        }
    }

    private String getTabTitle(int resId) {
        return getTabTitle(resId, "*");
    }

    private String getTabTitle(int resId, String count) {
        return String.format(getString(resId), count);
    }

    private synchronized void updateTabTitle(PagerTabAdapter.TabItem tabItem, long count) {
        String title = tabItem.title;
        final int index = title.lastIndexOf("(");
        title = title.substring(0, index);
        title = title + "(" + count + ")";

        tabItem.title = title;
        mTabPagerIndicator.notifyDataSetChanged();
    }

    private void updateTabTitle() {
        for (final PagerTabAdapter.TabItem tabItem : mTabItems) {
            new AsyncExecutor().execute(new AsyncWork<Long>() {
                @Override
                public void onPreExecute() {

                }

                @Override
                public void onSuccess(Long result) {
                    updateTabTitle(tabItem, result);
                }

                @Override
                public void onFail(Exception e) {
                    Logger.p("fail to load inspection list count for tab:" + tabItem.title, e);
                }

                @Override
                public Long onExecute() throws Exception {
                    return ((InspectionListFragment) tabItem.fragment).getItemCount();
                }
            });
        }
    }

}
