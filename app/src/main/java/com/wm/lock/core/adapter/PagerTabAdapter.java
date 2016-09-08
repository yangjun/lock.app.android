package com.wm.lock.core.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.wm.lock.core.AbstractActivity;

/**
 * Created by wangmin on 16/9/6.
 */
public class PagerTabAdapter extends FragmentStatePagerAdapter {

    protected TabItem[] mTabs;
    private AbstractActivity mAct;

    public PagerTabAdapter(AbstractActivity activity, TabItem[] tabItems) {
        super(activity.getSupportFragmentManager());
        this.mAct = activity;
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

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container,  position);
        mAct.getSupportFragmentManager().beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        final Fragment fragment = mTabs[position].fragment;
        mAct.getSupportFragmentManager().beginTransaction().hide(fragment).commit();
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
