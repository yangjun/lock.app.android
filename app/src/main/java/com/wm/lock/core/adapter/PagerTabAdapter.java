package com.wm.lock.core.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wm.lock.core.AbstractActivity;

/**
 * Created by wangmin on 16/9/6.
 */
public class PagerTabAdapter extends FragmentStatePagerAdapter {

    protected TabItem[] mTabs;

    public PagerTabAdapter(AbstractActivity activity, TabItem[] tabItems) {
        super(activity.getSupportFragmentManager());
        this.mTabs = tabItems;
    }

    public PagerTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mTabs[position].fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position].title;
    }

    @Override
    public int getCount() {
        return mTabs.length;
    }

    public static class TabItem {

        public String title;
        public Fragment fragment;

        public TabItem(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }

    }

}
