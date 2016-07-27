package com.wm.lock.core.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by wm on 15/9/7.
 */
public class GoogleRefreshLayout extends SwipeRefreshLayout {

    private boolean isPullRefreshEnable = true;
    private int mTouchSlop;
    private float mPrevX;

    private static Class<View>[] mScrollClasses = new Class[] {
            ListView.class, ScrollView.class, WebView.class, GridView.class };

    public GoogleRefreshLayout(Context context) {
        super(context);
        init();
    }

    public GoogleRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSwipeRefreshEnable(boolean isEnable) {
        isPullRefreshEnable = isEnable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isPullRefreshEnable) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return isPullRefreshEnable ? super.onInterceptTouchEvent(ev) : false;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isPullRefreshEnable ? super.onTouchEvent(ev) : false;
    }

    @Override
    public boolean canChildScrollUp() {
//        return super.canChildScrollUp();
        View targetV = getChildAt(0);
        View finalV = getScrollerV(targetV);
        if (finalV == null) {
            finalV = targetV;
        }
        return finalV.canScrollVertically(-1);
    }

    private View getScrollerV(View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup group = (ViewGroup) view;
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child.getVisibility() != View.VISIBLE) {
                continue;
            }
            if (isSrcollClass(child)) {
                return child;
            }
            View v = getScrollerV(child);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    private boolean isSrcollClass(View child) {
        for (int j = 0, len = mScrollClasses.length; j < len; j++) {
            if (child.getClass().isAssignableFrom(mScrollClasses[j])) {
                return true;
            }
            if (child.getParent() != null && child.getParent() instanceof View && isSrcollClass((View) child.getParent())) {
                return true;
            }
        }
        return false;
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

}
