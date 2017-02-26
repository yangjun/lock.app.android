package com.wm.lock.core.load;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.wm.lock.R;
import com.wm.lock.core.AbstractActivity;
import com.wm.lock.core.AbstractFragment;
import com.wm.lock.core.widget.GoogleRefreshLayout;

/**
 * Created by wm on 15/8/28.
 */
abstract class LoadBaseFragment<T> extends AbstractFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int DISTANCE_PULLREFRESH = 64;

    public static final int MODE_NORMAL = 1;
    public static final int MODE_PULLREFRESH = 2;

    protected int mCurrMode = MODE_NORMAL;
    protected AbstractActivity mActivity;

    private GoogleRefreshLayout mRefreshLayout;
    private OnSyncRefreshListener mSyncRefreshListener;
    protected LoadApi mLoadApi;
    private LoadFrgConfig mLoadConfig = new LoadFrgConfig();
    private LoadFrgCallBack<T> mLoadFrgCallBack;
    private LoadApiCallBack<T> mLoadCallBack;

    private Handler mHandler = new Handler();

    public LoadBaseFragment() { }

    public LoadBaseFragment(LoadFrgCallBack<T> loadCallBack) {
        mLoadFrgCallBack = loadCallBack;
    }

    protected void setLoadApiCallBack(LoadApiCallBack<T> callBack) {
        mLoadCallBack = callBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AbstractActivity) getActivity();
        mLoadConfig.mAct = mActivity;
        mLoadApi = new LoadApi(mActivity);
    }

    @Override
    public void onDetach() {
        mLoadApi.cancel();
        super.onDetach();
    }

    @Override
    protected void init() {
        mRefreshLayout = (GoogleRefreshLayout) getView().findViewById(R.id.srl);
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(R.color.txt_title, R.color.txt_title, R.color.txt_title, R.color.txt_title);
//            mRefreshLayout.setDistanceToTriggerSync(400); //设置手指在屏幕下拉多少距离会触发下拉刷新
            mRefreshLayout.setProgressBackgroundColor(R.color.bg_title);
//            mRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
            mRefreshLayout.setOnRefreshListener(this);
            setPullRefreshEnable(mLoadConfig.isPullRefreshEnable());
        }
        reload();
    }

    @Override
    protected void onMemoryRecycled() {

    }

    @Override
    public void onRefresh() {
        mCurrMode = MODE_PULLREFRESH;
        performLoad(false, true);
        if (mSyncRefreshListener != null) {
            mSyncRefreshListener.onSyncRefresh();
        }
    }

    protected void setLoadConfig(LoadFrgConfig config) {
        if (config != null) {
            mLoadConfig = config;
            if (mActivity != null) {
                mLoadConfig.mAct = mActivity;
            }
        }
    }

    /**
     * 重新加载
     */
    public void reload() {
        mCurrMode = MODE_NORMAL;
        performLoad(mLoadConfig.isShowProgressWidget(), true);
    }

    protected void performLoad(boolean isShowProgressWidget, boolean isShowResultWidget) {
        mLoadCallBack.mCallBack = mLoadFrgCallBack;
        mLoadCallBack.isShowResultWidget = isShowResultWidget;
        mLoadCallBack.mode = mCurrMode;
        mLoadCallBack.mfrg = this;
        mLoadCallBack.config = mLoadConfig;
        mLoadCallBack.refreshLayout = mRefreshLayout;
        mLoadApi.setLoadConfig(toLoadConfig(isShowProgressWidget, isShowResultWidget));
        mLoadApi.execute(mLoadCallBack);
    }

    public void setPullRefreshEnable(boolean isEnable) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setSwipeRefreshEnable(isEnable);
        }
    }

    public void showRefreshLayout() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setProgressViewOffset(false, 0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DISTANCE_PULLREFRESH,
                            getResources().getDisplayMetrics()));
            mRefreshLayout.setRefreshing(true);
        }
    }

    public void hideRefreshLayout() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    public GoogleRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    protected void stopPullRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    protected LoadApi.LoadConfig toLoadConfig(final boolean isShowProgressWidget, boolean isShowResultWidget) {
        LoadApi.LoadConfig config = new LoadApi.LoadConfig() {
            @Override
            protected View getFailView(Exception e) {
                View view = mLoadConfig.getFailView(e);
                return view == null ? super.getFailView(e) : view;
            }

            @Override
            public boolean isShowProgressWidget() {
                return isShowProgressWidget;
            }
        };
        config.setIsShowResultWidget(isShowResultWidget);
        config.setLoadingView(mLoadConfig.getLoadingView());
        return config;
    }

    public static interface OnSyncRefreshListener {

        public void onSyncRefresh();

    }

    public static class LoadFrgConfig {

        protected AbstractActivity mAct;

        private boolean isPullRefreshEnable = true;

        private View loadingView;
        private View failView;
        private boolean isShowProgressWidget = true;

        public LoadFrgConfig() {
            super();
        }

        protected boolean isPullRefreshEnable() {
            return isPullRefreshEnable;
        }

        public LoadFrgConfig setIsPullRefreshEnable(boolean isPullRefreshEnable) {
            this.isPullRefreshEnable = isPullRefreshEnable;
            return this;
        }

        protected View getLoadingView() {
            return loadingView;
        }

        public LoadFrgConfig setLoadingView(View loadingView) {
            this.loadingView = loadingView;
            return this;
        }

        protected View getFailView(Exception e) {
            return failView;
        }

        public LoadFrgConfig setFailView(View failView) {
            this.failView = failView;
            return this;
        }

        protected boolean isShowProgressWidget() {
            return isShowProgressWidget;
        }

        public LoadFrgConfig setIsShowProgressWidget(boolean isShowProgressWidget) {
            this.isShowProgressWidget = isShowProgressWidget;
            return this;
        }

    }

    public static abstract class LoadFrgCallBack<T> {

        public void onPreExecute() {

        }

        public abstract T onExecute();

        public void onSuccess(T result) {

        }

        public void onFail(Exception e) {
            e.printStackTrace();
        }

    }

    protected static class LoadApiCallBack<T> extends LoadApi.LoadCallBack<T> {

        LoadBaseFragment mfrg;
        LoadFrgCallBack<T> mCallBack;
        boolean isShowResultWidget;
        int mode;
        LoadFrgConfig config;
        SwipeRefreshLayout refreshLayout;

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            showProgress();
            mCallBack.onPreExecute();
        }

        @Override
        public T onExecute() {
            return mCallBack.onExecute();
        }

        @Override
        public void onSuccess(T result) {
            dismissProgress();
            mCallBack.onSuccess(result);
        }

        @Override
        public void onFail(Exception e) {
            super.onFail(e);
            dismissProgress();
            mCallBack.onFail(e);
            if (!isShowResultWidget) {
                mfrg.mActivity.showTip(R.string.message_fail_load);
            }
        }

        @Override
        public ViewGroup getContainer() {
            return (ViewGroup) mfrg.getView().findViewById(R.id.fl_load);
        }

        @Override
        public int getContainerId() {
            return R.id.fl_load;
        }

        private void showProgress() {
            if (mode == MODE_NORMAL && !config.isShowProgressWidget() && refreshLayout != null) {
                refreshLayout.setProgressViewOffset(false, 0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DISTANCE_PULLREFRESH,
                                mfrg.getResources().getDisplayMetrics()));
                refreshLayout.setRefreshing(true);
            }
        }

        private void dismissProgress() {
            if (mode == MODE_NORMAL && !config.isShowProgressWidget() && refreshLayout != null) {
                refreshLayout.setRefreshing(false);
            }
        }
    }

}
