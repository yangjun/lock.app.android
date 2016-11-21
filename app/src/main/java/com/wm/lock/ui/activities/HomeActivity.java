package com.wm.lock.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

import com.viewpagerindicator.TabPageIndicator;
import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.bugly.BuglyManager;
import com.wm.lock.core.adapter.PagerTabAdapter;
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.dialog.VerticalPopMenu;
import com.wm.lock.dto.InspectionNewDto;
import com.wm.lock.dto.InspectionResultDto;
import com.wm.lock.entity.Inspection;
import com.wm.lock.helper.NotificationHelper;
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

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.tencent.bugly.crashreport.inner.InnerApi.context;

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
    private List<VerticalPopMenu.VerticalPopMenuItem> moreItems;

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_home;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
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
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        final Object data = bundle.getSerializable(LockConstants.DATA);
        if (data == null) {
            return;
        }

        if (data instanceof InspectionNewDto && mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
        }
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

    @OptionsItem(R.id.menu_more)
    void onMoreClick() {
        DialogManager.showBottomVerticalPop(this, mToolbar, getMoreItems());
    }

    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContainerHeader.getLayoutParams();
            params.topMargin = HardwareUtils.getStatusBarHeight(getApplicationContext());
            mContainerHeader.setLayoutParams(params);
        }

        setBackBtnVisible(false);
        mToolbar.setBackgroundResource(Color.TRANSPARENT);
    }

    private void setupTab() {
        mTabItems = new PagerTabAdapter.TabItem[] {
                new PagerTabAdapter.TabItem(getTabTitle(R.string.label_pending), InspectionListPendingFragment_.builder().build()),
                new PagerTabAdapter.TabItem(getTabTitle(R.string.label_in_process), InspectionListInProcessFragment_.builder().build()),
                new PagerTabAdapter.TabItem(getTabTitle(R.string.label_submit_fail), InspectionListSubmitFailFragment_.builder().build()),
        };

        final PagerTabAdapter adapter = new PagerTabAdapter(this, mTabItems);
        mViewPager.setAdapter(adapter);

        mTabPagerIndicator.setViewPager(mViewPager);

        updateTabTitles();
        mViewPager.setCurrentItem(1); //选中处理中
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dismissNewInspectionNotificationIfCondition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateIndicator() {
//        final boolean hasNew = BuglyManager.hasUpgradeInfo(getApplicationContext());
//        mVIndicator.setVisibility(hasNew ? View.VISIBLE : View.GONE);
        // TODO 更新红点的时候,需要在下拉功能菜单的"设置"项旁边加上红点,这样才完整
    }

    public void dismissNewInspectionNotificationIfCondition() {
        if (mViewPager.getCurrentItem() == 0) {
            NotificationHelper.dismissNewInspection();
        }
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

    public void reloadSubmitFail() {
        ((InspectionListFragment) mTabItems[2].fragment).reloadDelay();
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

        if (tabItem == mTabItems[0]) {
            updateDesktopCount((int) count);
        }
    }

    private void updateTabTitles() {
        for (int i = 0, len = mTabItems.length; i < len; i++) {
            updateTabTitleItem(i);
        }
    }

    private void updateTabTitleItem(final int index) {
        new AsyncExecutor().execute(new AsyncWork<Long>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onSuccess(Long result) {
                updateTabTitle(mTabItems[index], result);
            }

            @Override
            public void onFail(Exception e) {
                Logger.p("fail to load inspection list count for tab:" + mTabItems[index].title, e);
            }

            @Override
            public Long onExecute() throws Exception {
                return ((InspectionListFragment) mTabItems[index].fragment).getItemCount();
            }
        });
    }

    private void updateDesktopCount(int count) {
        ShortcutBadger.applyCount(context, count);
    }

    private List<VerticalPopMenu.VerticalPopMenuItem> getMoreItems() {
        if (moreItems == null) {
            moreItems = new ArrayList<>();
            moreItems.add(new VerticalPopMenu.VerticalPopMenuItem(
                    R.mipmap.ic_temperature_humility,
                    getString(R.string.label_temperature_humility_list),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RedirectUtils.goActivity(HomeActivity.this, TemperatureHumidityListActivity_.class);
                        }
                    }
            ));
            moreItems.add(new VerticalPopMenu.VerticalPopMenuItem(
                    R.mipmap.ic_setting,
                    getString(R.string.label_setting),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RedirectUtils.goActivity(HomeActivity.this, SettingActivity_.class);
                        }
                    }
            ));
        }
        return moreItems;
    }


    public void onEventMainThread(InspectionNewDto dto) {
        updateTabTitleItem(0);
    }

    public void onEventMainThread(InspectionResultDto dto) {
        if (dto.isSuccess()) {
            updateTabTitleItem(1);
            updateTabTitleItem(2);
        }
    }

}
