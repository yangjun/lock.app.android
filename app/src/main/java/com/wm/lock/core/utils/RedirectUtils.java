package com.wm.lock.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RedirectUtils {

	private static final int INVALIDATE_REQUEST = -1;

	public static void goActivity(Context ctx, Class<?> clazz) {
		goActivity(ctx, clazz, null);
	}

	public static void goActivity(Context ctx, Class<?> clazz, Bundle bundle) {
		goActivity(ctx, clazz, bundle, INVALIDATE_REQUEST);
	}

	public static void goActivityForResult(Context ctx, Class<?> clazz, int requestCode) {
		goActivityForResult(ctx, clazz, null, requestCode);
	}

	public static void goActivityForResult(Context ctx, Class<?> clazz, Bundle bundle, int requestCode) {
		goActivity(ctx, clazz, bundle, requestCode);
	}

	/**
	 * 公用跳转
	 * @param fragment 当前fragment
	 * @param clazz 跳转页面
	 */
	public static void goActivityForResult(Fragment fragment, Class<?> clazz, int requestCode) {
		goActivityForResult(fragment, clazz, null, requestCode);
	}

	/**
	 * 公用跳转
	 * @param fragment 当前fragment
	 * @param clazz 跳转页面
	 * @param bundle 封装传输的数据
	 */
	public static void goActivityForResult(Fragment fragment, Class<?> clazz, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		intent.setClass(fragment.getActivity(), clazz);
		if (requestCode == INVALIDATE_REQUEST) {
			fragment.startActivity(intent);
		}
		else {
			fragment.startActivityForResult(intent, requestCode);
		}
	}

	public static void goActivity(Context ctx, Class<?> clazz, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		intent.setClass(ctx, clazz);
		if (requestCode == INVALIDATE_REQUEST) {
			ctx.startActivity(intent);
		}
		else {
			((Activity) ctx).startActivityForResult(intent, requestCode);
		}
	}

	/**
	 * 清理指定Activity堆栈上的所有activity对象，并重新创建该Activity
	 */
	public static void goActivityByClearTop(Context ctx, Class<?> destClass) {
		goActivityByClearTop(ctx, destClass, null);
	}

	/**
	 * 清理指定Activity堆栈上的所有activity对象，并重新创建该Activity
	 */
	public static void goActivityByClearTop(Context ctx, Class<?> destClass, Intent it) {
		if (null == it) {
			it = new Intent();
		}
		it.setClass(ctx, destClass);
		it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ctx.startActivity(it);
	}

	/**
	 * 清理指定Activity堆栈上的所有ctivity对象，不重新创建该Activity
	 */
	public static void goActivityByClearTopForUnReCrate(Context ctx, Class<?> destClass) {
		goActivityByClearTopForUnReCrate(ctx, destClass, new Intent());
	}

	/**
	 * 清理指定Activity堆栈上的所有ctivity对象，不重新创建该Activity
	 */
	public static void goActivityByClearTopForUnReCrate(Context ctx, Class<?> destClass, Intent it) {
		if (null == it) {
			it = new Intent();
		}
		it.setClass(ctx, destClass);
		it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ctx.startActivity(it);
	}

}
