package com.wm.lock.core.load;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.wm.lock.R;
import com.wm.lock.core.AbstractActivity;
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;

import java.lang.ref.WeakReference;


/**
 * Created by wm on 15/8/28.
 */
public class LoadApi {

    public static final int LOADING_ID = 10000;
    public static final int FAIL_ID = 10001;

    private int currId = -1;

    private ViewGroup mContainer;
    private WeakReference<AbstractActivity> mActivityReference;
    private LoadCallBack mCallBack;
    private LoadConfig mConfig = new LoadConfig();

    private Handler mHandler = new Handler();
    private AsyncExecutor mAsyncExecutor;
//    private boolean isExcuting = false;

    public LoadApi(AbstractActivity act) {
        mActivityReference = new WeakReference<AbstractActivity>(act);
    }

    public <T> void execute(LoadCallBack<T> callBack) {
        mCallBack = callBack;
        if (mActivityReference.get() != null) {
            fixConfig();
            mContainer = callBack.getContainer();
            if (mContainer == null) {
                mContainer = (ViewGroup) mActivityReference.get().findViewById(callBack.getContainerId());
            }
            cancel();
            mAsyncExecutor = new AsyncExecutor();
            execute();
        }
    }

    public void reExecute() {
        execute();
    }

    public void setLoadConfig(LoadConfig config) {
        if (config != null) {
            mConfig = config;
            fixConfig();
        }
    }

    public void cancel() {
        if (mAsyncExecutor != null) {
            mAsyncExecutor.cancel();
            mAsyncExecutor = null;
        }
    }

    protected void toFailState(Exception e) {
        if (!mConfig.isShowResultWidget()) {
            return;
        }
        View failView = mConfig.getFailView(e);
        addView(failView, FAIL_ID);
        failView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                execute();
            }
        });
    }

    private void fixConfig() {
        mConfig.setActivity(mActivityReference.get());
    }

    private <T> void execute() {
        if (mActivityReference.get() == null) {
            return;
        }
        mAsyncExecutor.execute(new AsyncWork<T>() {

            @Override
            public void onPreExecute() {
                showLoadingDialog();
                mCallBack.onPreExecute();
            }

            @Override
            public void onSuccess(T result) {
                dismissLoadingDialog();
                executeSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                dismissLoadingDialog();
                executeFail(e);
            }

            @Override
            public T onExecute() {
                return (T) mCallBack.onExecute();
            }
        });
    }

    private <T> void executeSuccess(T result) {
        mCallBack.onSuccess(result);
    }

    private void executeFail(Exception e) {
        toFailState(e);
        mCallBack.onFail(e);
    }

    private void showLoadingDialog() {
        if (mConfig.isShowProgressWidget()) {
            View loadView = mConfig.getLoadingView();
            loadView.setOnClickListener(null); // 屏蔽其他事件
            addView(loadView, LOADING_ID);
        } else {
            removeView(currId);
        }
    }

    private void dismissLoadingDialog() {
        if (mConfig.isShowProgressWidget()) {
            removeView(LOADING_ID);
        }
    }

    protected void addView(View view, int id) {
        if (currId == id) {
            return;
        }
        view.setId(id);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContainer.addView(view, this.getAddedPos());
        removeView(currId);
        currId = id;
    }

    protected final void removeView(int id) {
        View view = mContainer.findViewById(id);
        removeView(view);
    }

    protected void removeView(View view) {
        if (null != view) {
            mContainer.removeView(view);
        }
        currId = -1;
    }

    private int getAddedPos() {
        if (mContainer instanceof LinearLayout || mContainer instanceof ViewAnimator) {
            return 0;
        }
        return mContainer.getChildCount();
    }

    public static class LoadConfig {

        private AbstractActivity mActivity;

        private View loadingView;
        private View failView;
        private boolean isShowProgressWidget = true;
        private boolean isShowResultWidget = true;

        public LoadConfig() {
        }

        public boolean isShowResultWidget() {
            return isShowResultWidget;
        }

        public LoadConfig setIsShowResultWidget(boolean isShowResultWidget) {
            this.isShowResultWidget = isShowResultWidget;
            return this;
        }

        protected View getLoadingView() {
            if (loadingView == null) {
                loadingView = this.getInflateView(R.layout.load_inflate_loading);
            }
            return loadingView;
        }

        public LoadConfig setLoadingView(View loadingView) {
            this.loadingView = loadingView;
            return this;
        }

        protected View getFailView(Exception e) {
            if (failView == null) {
                failView = this.getInflateView(R.layout.load_inflate_fail);
                ((TextView) failView.findViewById(R.id.tview_load_fail)).setText(getFailTxt());
            }
            return failView;
        }

        public LoadConfig setFailView(View failView) {
            this.failView = failView;
            return this;
        }

        protected CharSequence getFailTxt() {
            return mActivity == null ? null : mActivity.getString(R.string.label_load_fail);
        }

        public boolean isShowProgressWidget() {
            return isShowProgressWidget;
        }

        public LoadConfig setIsShowProgressWidget(boolean isShowProgressWidget) {
            this.isShowProgressWidget = isShowProgressWidget;
            return this;
        }

        private View getInflateView(int viewId) {
            if (mActivity != null) {
                return LayoutInflater.from(mActivity).inflate(viewId, null);
            }
            return null;
        }

        protected void setActivity(AbstractActivity act) {
            mActivity = act;
        }
    }

    public static abstract class LoadCallBack<T> {

        public void onPreExecute() {

        }

        public abstract T onExecute();

        public abstract void onSuccess(T result);

        public void onFail(Exception e) {
            e.printStackTrace();
        }

        public abstract int getContainerId();

        public ViewGroup getContainer() {
            return null;
        }

    }


}
