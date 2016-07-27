package com.wm.lock.core.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * 
 * @author minWang
 *
 * @description 处理Fragment的工具类
 */
public class FragmentUtils {

	/**
	 * 添加Fragment
	 *
	 * @param manager
	 * @param containerRes
	 * @param fragment
	 */
	public static void addFragment(FragmentManager manager, int containerRes, Fragment fragment) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(containerRes, fragment);
		transaction.commit();
	}

	/**
	 * 替换Fragment
	 *
	 * @param manager
	 * @param containerRes
	 * @param fragment
	 */
	public static void replaceFragment(FragmentManager manager, int containerRes, Fragment fragment) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(containerRes, fragment);
		transaction.commit();
	}

	/**
	 * 替换Fragment并加入到后退栈
	 *
	 * @param manager
	 * @param containerRes
	 * @param fragment
	 */
	public static void replaceFragmentToBackStack(FragmentManager manager, int containerRes, Fragment fragment) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(containerRes, fragment);
		// 调用addToBackStack()，此时被代替的fragment就被放入后退栈中，于是当用户按下返回键时，事务发生回溯，原先的fragment又回来了
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * 隐藏Fragment
	 *
	 * @param manager
	 * @param fragment
	 */
	public static void hideFragment(FragmentManager manager, Fragment fragment) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.hide(fragment);
		transaction.commit();
	}

	/**
	 * 显示Fragment
	 *
	 * @param manager
	 * @param fragment
	 */
	public static void showFragment(FragmentManager manager, Fragment fragment) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.show(fragment);
		transaction.commit();
	}

	/**
	 * 立即清理Fragment
	 *
	 * @param manager
	 * @param fragment
	 */
	public static void removeFragmentRecently(FragmentManager manager, Fragment fragment) {
		removeFragment(manager, fragment);
		manager.executePendingTransactions();
	}

	/**
	 * 立即清理Fragment列表
	 *
	 * @param manager
	 */
	public static void removeFragmentRecently(FragmentManager manager, List<Fragment> list) {
		removeFragmentList(manager, list);
		manager.executePendingTransactions();
	}

	/**
	 * 清理Fragment
	 *
	 * @param manager
	 * @param fragment
	 */
	public static void removeFragment(FragmentManager manager, Fragment fragment) {
		FragmentTransaction ft = manager.beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

	/**
	 * 清理Fragment列表
	 *
	 * @param manager
	 */
	public static void removeFragmentList(FragmentManager manager, List<Fragment> list) {
		FragmentTransaction ft = manager.beginTransaction();
		if (null != list && !list.isEmpty()) {
			for(Fragment fragment : list) {
				ft.remove(fragment);
			}
		}
		ft.commit();
	}
	
}
