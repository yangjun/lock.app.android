package com.wm.lock.core.load;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wm.lock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 15/8/28.
 */
public class LoadListFragment<T, T2 extends ListView> extends LoadBaseFragment {

    public static final int MODE_LOADMORE = 3;
    public static final int AUTO_LOADMORE_POS = 3;

    protected T2 mListView;
    protected List<T> mList = new ArrayList<T>();
    protected BaseAdapter mListAdapter;
    protected LoadListConfig mLoadListConfig;
    protected LoadListCallBack<T> mLoadListCallBack;
    protected FooterView mFooterView;
    protected boolean isLoadMoreLocked = false;
    protected int mPageNow = 0;

    /** 当前列表的最后一项 */
    protected int mLastItem = -1;
    protected int mContentViewId;

    protected ViewGroup mEmptyGroup;

    public LoadListFragment() {
        super();
    }

    public LoadListFragment(LoadListCallBack<T> loadListCallBack) {
        super(loadListCallBack);
        setLoadConfig(new LoadListConfig());
        mLoadListCallBack = loadListCallBack;
        setLoadApiCallBack(new LoadApiCallBack<List<T>>() {
            @Override
            public void onSuccess(List<T> result) {
                performSuccess(result);
                isLoadMoreLocked = false;
                super.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                performFail(e);
                isLoadMoreLocked = false;
                super.onFail(e);
            }

            @Override
            public void onPreExecute() {
                performPre();
                super.onPreExecute();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        if (mContentViewId > 0) {
            return mContentViewId;
        }
        return R.layout.load_frag_list;
    }

    @Override
    protected void init() {
        View emptyeView = mLoadListConfig.getEmptyView();
        emptyeView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mEmptyGroup = (ViewGroup) getView().findViewById(R.id.fl_empty);
        mEmptyGroup.addView(emptyeView);

        mListView = (T2) getView().findViewById(R.id.olv);
        mListView.setOnItemClickListener(mLoadListConfig.getOnItemClickListener());
        mListView.setOnScrollListener(mLoadListConfig.getOnScrollListener());
        if (mLoadListConfig.getDividerDrawable() != null) {
            mListView.setDivider(mLoadListConfig.getDividerDrawable());
        }
        mListView.setDividerHeight(mLoadListConfig.getDividerHeight());

        mListView.setPadding(mLoadListConfig.getPaddingLeft(), mLoadListConfig.getPaddingTop(), mLoadListConfig.getPaddingRight(), mLoadListConfig.getPaddingBottom());

//        mListView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
//            @Override
//            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//                setPullRefreshEnable(scrollY <= 0);
//                if (mLoadListConfig.getObservableScrollViewCallbacks() != null) {
//                    mLoadListConfig.getObservableScrollViewCallbacks().onScrollChanged(scrollY, firstScroll, dragging);
//                }
//            }
//
//            @Override
//            public void onDownMotionEvent() {
//                if (mLoadListConfig.getObservableScrollViewCallbacks() != null) {
//                    mLoadListConfig.getObservableScrollViewCallbacks().onDownMotionEvent();
//                }
//            }
//
//            @Override
//            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//                if (mLoadListConfig.getObservableScrollViewCallbacks() != null) {
//                    mLoadListConfig.getObservableScrollViewCallbacks().onUpOrCancelMotionEvent(scrollState);
//                }
//            }
//        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (mLoadListConfig.isAutoLoadMore()
                        && mListAdapter.getCount() - AUTO_LOADMORE_POS < mLastItem
                        && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && mFooterView != null) {
                    loadMore();
                }
                if (mLoadListConfig.getOnScrollListener() != null) {
                    mLoadListConfig.getOnScrollListener().onScrollStateChanged(absListView, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastItem = firstVisibleItem + visibleItemCount - 1;
                if (mLoadListConfig.getOnScrollListener() != null) {
                    mLoadListConfig.getOnScrollListener().onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });
        super.init();
    }

    @Override
    public void onRefresh() {
        mPageNow = mLoadListConfig.getPageStart();
        super.onRefresh();
    }

    @Override
    public void reload() {
        mPageNow = mLoadListConfig.getPageStart();
        super.reload();
    }

    public void setLoadConfig(LoadListConfig config) {
        super.setLoadConfig(config);
        mLoadListConfig = config;
    }

    public void setContentViewId(int id) {
        mContentViewId = id;
    }

    /**
     * 获取ListView对象
     */
    public T2 getListView() {
        return mListView;
    }

    public List<T> getList() {
        return mList;
    }

    /**
     * 获取当前页码
     */
    public int getPageNow() {
        return mPageNow;
    }

    /**
     * 在列表末尾新增一条数据
     */
    public void add(T item) {
        if (null != item) {
            this.mList.add(item);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 在列表的指定位置新增一条数据
     */
    public void add(T item, int position) {
        if (null != item) {
            this.mList.add(position, item);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 在列表的末尾新增多条数据
     */
    public void addAll(List<T> list) {
        if (null != list && !list.isEmpty()) {
            this.mList.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 在列表的指定位置新增多条数据
     */
    public void addAll(List<T> list, int position) {
        if (null != list && !list.isEmpty()) {
            this.mList.addAll(position, list);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除某条记录
     */
    public void remove(T item) {
        if (null != item && this.mList.contains(item)) {
            this.mList.remove(item);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除多条记录
     */
    public void remove(List<T> list) {
        if (null != list && !list.isEmpty()) {
            boolean isNeedRefresh = false;
            for (T item : list) {
                if (null != item && this.mList.contains(item)) {
                    this.mList.remove(item);
                    isNeedRefresh = true;
                }
            }
            if (isNeedRefresh) {
                this.notifyDataSetChanged();
            }
        }
    }

    /**
     * 删除某个位置的记录
     */
    public void remove(int position) {
        if (position >= 0 && position < this.mList.size()) {
            this.mList.remove(position);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 更新某条记录
     */
    public void update(T item) {
        int position = this.getPosition(item);
        if (position >= 0) {
            this.update(item, position);
        }
    }

    /**
     * 更新指定位置的某条记录
     */
    public void update(T item, int position) {
        this.mList.set(position, item);
        this.notifyDataSetChanged();
    }

    /**
     * 更新多条记录
     */
    public void update(List<T> list) {
        if (null != list & !list.isEmpty()) {
            boolean isNeedRefresh = false;
            for (T item : list) {
                int position = this.getPosition(item);
                if (position >= 0) {
                    this.mList.set(position, item);
                    isNeedRefresh = true;
                }
            }
            if (isNeedRefresh) {
                this.notifyDataSetChanged();
            }
        }
    }

    /**
     * 刷新列表
     */
    public void notifyDataSetChanged() {
        if (null != this.mListAdapter) {
            this.mListAdapter.notifyDataSetChanged();
            checkEmpty(mList);
            checkFooter(mList);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////
    private void loadMore() {
        if (isLoadMoreLocked) {
            return;
        }
        isLoadMoreLocked = true;
        mCurrMode = MODE_LOADMORE;
        mPageNow++;
        mFooterView.toProgressState();
        performLoad(false, false);
    }

    private void stopLoadMore() {
        if (mFooterView != null) {
            mFooterView.toNormalState();
        }
    }

    protected void performPre() {

    }

    protected void performSuccess(List<T> result) {
        if (mCurrMode == MODE_LOADMORE) {
            stopLoadMore();
            if (result == null || result.isEmpty()) {
                mPageNow--;
                checkFooter(result);
            } else {
                mList.addAll(result);
                notifyDataSetChanged();
            }
        } else {
            if (mCurrMode == MODE_PULLREFRESH) {
                stopPullRefresh();
            }
            mList = result;
            setAdapter();
        }
    }

    protected void performFail(Exception e) {
        if (mCurrMode == MODE_PULLREFRESH) {
            stopPullRefresh();
        } else if (mCurrMode == MODE_LOADMORE) {
            mPageNow--;
            stopLoadMore();
        }
    }

    public void setList(List<T> list) {
        mList = list;
        setAdapter();
    }

    public void setAdapter() {
        if (!checkEmpty(mList)) {
            mListAdapter = mLoadListCallBack.getAdapter(mList);
            mListView.setAdapter(mListAdapter);
        }
        checkFooter(mList);
    }

    private void checkFooter(List result) {
        if (result != null && !result.isEmpty() && result.size() >= mLoadListConfig.getPageLimit()) {
            if (mFooterView == null) {
                mFooterView = new FooterView(mActivity);
                mListView.addFooterView(mFooterView);
            }
        } else if (mFooterView != null){
            mListView.removeFooterView(mFooterView);
            mFooterView = null;
        }
    }

    protected boolean checkEmpty(List list) {
        if (list == null || list.isEmpty()) {
            mEmptyGroup.setVisibility(View.VISIBLE);
            return true;
        } else {
            mEmptyGroup.setVisibility(View.GONE);
            return false;
        }
    }

    private int getPosition(T item) {
        if (null == item || this.mList.isEmpty() || !this.mList.contains(item)) {
            return -1;
        }
        for (int i = 0, len = this.mList.size(); i < len; i++) {
            if (this.mList.get(i) == item) {
                return i;
            }
        }
        return -1;
    }

    private static int dp2px(Context context, float dpValue) {
        if (dpValue <= 0) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static class LoadListConfig extends LoadFrgConfig {

        private int pageLimit = Integer.MAX_VALUE;
        private int pageStart = 0;
        private boolean isAutoLoadMore = true;
        private View emptyView;
        private int dividerHeight = 1;
        private Drawable dividerDrawable;

        private int paddingLeft = 0;
        private int paddingTop = 0;
        private int paddingRight = 0;
        private int paddingBottom = 0;

        private AdapterView.OnItemClickListener onItemClickListener;
        private AbsListView.OnScrollListener onScrollListener;
//        private ObservableScrollViewCallbacks observableScrollViewCallbacks;

        public LoadListConfig() {
            super();
        }

        protected AdapterView.OnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }

        public LoadListConfig setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        protected AbsListView.OnScrollListener getOnScrollListener() {
            return onScrollListener;
        }

        public LoadListConfig setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
            this.onScrollListener = onScrollListener;
            return this;
        }

        protected View getEmptyView() {
            if (emptyView == null) {
                emptyView = LayoutInflater.from(mAct).inflate(R.layout.load_inflate_empty, null);
//                ((TextView) emptyView.findViewById(R.id.tview_load_empty)).setText(getEmptyText());
            }
            return emptyView;
        }

        public void setEmptyView(View emptyView) {
            this.emptyView = emptyView;
        }

        protected String getEmptyText() {
            return mAct.getString(R.string.label_load_empty);
        }

//        public ObservableScrollViewCallbacks getObservableScrollViewCallbacks() {
//            return observableScrollViewCallbacks;
//        }
//
//        public LoadListConfig setObservableScrollViewCallbacks(ObservableScrollViewCallbacks observableScrollViewCallbacks) {
//            this.observableScrollViewCallbacks = observableScrollViewCallbacks;
//            return this;
//        }

        protected boolean isAutoLoadMore() {
            return isAutoLoadMore;
        }

        public LoadListConfig setIsAutoLoadMore(boolean isAutoLoadMore) {
            this.isAutoLoadMore = isAutoLoadMore;
            return this;
        }

        protected int getPageLimit() {
            return pageLimit;
        }

        public LoadListConfig setPageLimit(int pageLimit) {
            this.pageLimit = pageLimit;
            return this;
        }

        public int getPageStart() {
            return pageStart;
        }

        public int getDividerHeight() {
            return dividerHeight;
        }

        public Drawable getDividerDrawable() {
            if (dividerDrawable == null) {
                dividerDrawable = new ColorDrawable(mAct.getResources().getColor(R.color.bg_item_light));
            }
            return dividerDrawable;
        }

        public LoadListConfig setPageStart(int pageStart) {
            this.pageStart = pageStart;
            return this;
        }

        public LoadListConfig setDividerHeight(int dividerHeight) {
            this.dividerHeight = dividerHeight;
            return this;
        }

        public LoadListConfig setDividerDrawable(Drawable dividerDrawable) {
            this.dividerDrawable = dividerDrawable;
            return this;
        }

        public int getPaddingLeft() {
            return paddingLeft;
        }

        public LoadListConfig setPaddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        public int getPaddingTop() {
            return paddingTop;
        }

        public LoadListConfig setPaddingTop(int paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }

        public int getPaddingRight() {
            return paddingRight;
        }

        public LoadListConfig setPaddingRight(int paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }

        public int getPaddingBottom() {
            return paddingBottom;
        }

        public LoadListConfig setPaddingBottom(int paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }

        @Override
        public LoadListConfig setIsPullRefreshEnable(boolean isPullRefreshEnable) {
            super.setIsPullRefreshEnable(isPullRefreshEnable);
            return this;
        }

        public LoadListConfig setLoadingView(View loadingView) {
            super.setLoadingView(loadingView);
            return this;
        }

        public LoadListConfig setFailView(View failView) {
            super.setFailView(failView);
            return this;
        }

        public LoadListConfig setIsShowProgressWidget(boolean isShowProgressWidget) {
            super.setIsShowProgressWidget(isShowProgressWidget);
            return this;
        }

    }

    public static abstract class LoadListCallBack<T> extends LoadFrgCallBack<List<T>> {

        public abstract BaseAdapter getAdapter(List<T> list);

    }

    public class FooterView extends FrameLayout implements View.OnClickListener {

        private ProgressBar mPb;
        private TextView mTvHint;

        public FooterView(Context context) {
            super(context);
            setup();
        }

        public FooterView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setup();
        }

        private void setup() {
            LayoutInflater.from(getContext()).inflate(R.layout.load_inflate_footer, this);
            mPb = (ProgressBar) findViewById(R.id.pb_footer);
            mTvHint = (TextView) findViewById(R.id.tv_hint_footer);
            toNormalState();
        }

        public void toProgressState() {
            setOnClickListener(null);
            mPb.setVisibility(View.VISIBLE);
            mTvHint.setText(getString(R.string.header_hint_loading));
        }

        public void toNormalState() {
            setOnClickListener(this);
            mPb.setVisibility(View.GONE);
            mTvHint.setText(getString(R.string.footer_hint_normal));
        }

        @Override
        public void onClick(View view) {
            loadMore();
        }
    }

}
