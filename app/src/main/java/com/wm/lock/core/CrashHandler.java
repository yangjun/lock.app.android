package com.wm.lock.core;

import android.content.Context;

import com.wm.lock.core.logger.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {

	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/**
	 * 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能
	 * */
	public static final boolean DEBUG = false;
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;

	private CrashCallBack mCallBack;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx, CrashCallBack callBack) {
		mContext = ctx;
		mCallBack = callBack;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.d(ex);
		boolean isHandled = handleException(ex);
		try {
//			Thread.sleep(1000);
//			if (isHandled && mDefaultHandler != null) {
				if (mCallBack != null) {
					mCallBack.onExist();
				}
				mDefaultHandler.uncaughtException(thread, ex);
//			} else {
//				android.os.Process.killProcess(android.os.Process.myPid());
//				System.exit(10);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
//		if (ex == null) {
//			Log.w(TAG, "handleException --- ex==null");
//			return true;
//		}
//		final String msg = ex.getLocalizedMessage();
//		if (msg == null) {
//			return false;
//		}

//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				try {
//					mCallBack.showException(mContext.getString(R.string.message_crash));
//				} catch (Exception e) {
//					Logger.log(e);
//				}
//				Looper.loop();
//			}
//		}.start();
		return true;
	}

	public static interface CrashCallBack {

		public void onExist();

		public void showException(String msg);
	}

}
