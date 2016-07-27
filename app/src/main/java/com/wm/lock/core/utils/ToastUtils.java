package com.wm.lock.core.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast显示的工具类
 * @author kevin
 *
 */
public class ToastUtils {

	/**
	 * 短时间显示普通的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 */
	public static void showShortToast(Context context, CharSequence string){
		Toast.makeText(context, string ,Toast.LENGTH_SHORT).show();
	}
	
	public static void showShortToast(Context context, int resId){
		Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 长时间显示普通的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 */
	public static void showLongToast(Context context, CharSequence string){
		Toast.makeText(context, string ,Toast.LENGTH_LONG).show();
	}
	
	public static void showLongToast(Context context, int resId){
		Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_LONG).show();
	}
	
	
	/**
	 * 短时间显示出现在底部的的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showShortButtomToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, x, y);
		toast.show();
	}
	
	public static void showShortButtomToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, x, y);
		toast.show();
	}
	
	/**
	 * 长时间显示出现在底部的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showLongButtomToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, x, y);
		toast.show();
	}
	
	public static void showLongButtomToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, x, y);
		toast.show();
	}
	
	/**
	 * 短时间显示在顶部的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showShortTopToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, x, y);
		toast.show();
	}
	
	public static void showShortTopToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, x, y);
		toast.show();
	}
	
	/**
	 * 长时间显示可以设置位置的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showLongTopToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, x, y);
		toast.show();
	}
	
	public static void showLongTopToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, x, y);
		toast.show();
	}
	
	/**
	 * 短时间显示可以偏左的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showShortLeftToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.LEFT, x, y);
		toast.show();
	}
	
	public static void showShortLeftToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.LEFT, x, y);
		toast.show();
	}
	
	/**
	 * 长时间显示可以偏左的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showLongLeftToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.LEFT, x, y);
		toast.show();
	}
	
	public static void showLongLeftToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.LEFT, x, y);
		toast.show();
	}
	
	/**
	 * 短时间显示可以偏右的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showShortRightToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.RIGHT, x, y);
		toast.show();
	}
	
	public static void showShortRightToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.RIGHT, x, y);
		toast.show();
	}
	
	/**
	 * 长时间显示可以偏右的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showLongRightToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.RIGHT, x, y);
		toast.show();
	}
	
	public static void showLongRightToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.RIGHT, x, y);
		toast.show();
	}
	
	/**
	 * 短时间显示中心的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showShortCenterToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, x, y);
		toast.show();
	}
	
	public static void showShortCenterToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, x, y);
		toast.show();
	}
	
	/**
	 * 长时间显示中心的Toast
	 * @param context 上下文
	 * @param string 显示的文字/ resId 显示文字的id
	 * @param x 左右偏移量
	 * @param y 上下偏移量
	 */
	public static void showLongCenterToast(Context context, CharSequence string, int x, int y){
		Toast toast = Toast.makeText(context, string ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, x, y);
		toast.show();
	}
	
	public static void showLongCenterToast(Context context, int resId, int x, int y){
		Toast toast = Toast.makeText(context, context.getResources().getString(resId) ,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, x, y);
		toast.show();
	}
}
