package com.wm.lock.core.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.List;

public class HardwareUtils {
    
    public static int getWidth(Context ctx) {
        return ((Activity)ctx).getWindowManager().getDefaultDisplay().getWidth();
    }
    
    public static int getHeight(Context ctx) {
        return ((Activity)ctx).getWindowManager().getDefaultDisplay().getHeight();
    }
    
    /**
	 * 根据手机的分辨率从 dp的单位转成为 px(像素)
	 * @param context 上下文
	 * @param dpValue dp
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素)的单位转成为 dp
	 * @param context 上下文
	 * @param pxValue px
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
    /**
     * 获取状态栏(通知栏)高度
     * @return
     */
    public static int getStatusBarHeight(Context ctx){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = ctx.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
    
    /**
     * @return MSISDN （mobile subscriber ISDN 用户手机号码）手机号 可能为空
     */
    public static String getTelephoneNumber(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        // （getLine1Number 取GSM没问题，CDMA可能为空，将来可能会有getLine2Number）

        String sim = telephonyManager.getLine1Number();
        if(!TextUtils.isEmpty(sim) && sim.length() > 11){
        	sim = sim.trim().substring(3);
    	}
        return sim;
    }
    
    /**
     * @return SIM卡号，也就是ICCID（ICC identity集成电路卡标识）号
     */
    public static String getSimNumber(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber().trim();
    }
    
    /**
     * @return IMSI(International Mobile Subscriber Identification Number
     *         国际移动用户号码标识)号
     */
    public static String getImsiNumber(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }
    
    /**
     * @return IMEI��international mobile Equipment identity手机唯一标识码）号
     */
    public static String getImeiNumber(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    
    public static boolean isTelephoneServiceAvailable(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return (null != telephonyManager);
    }
    
    public static boolean isGpsEnabled(Context ctx) {
    	LocationManager locationManager  = (LocationManager) ctx.
    			getSystemService(Context.LOCATION_SERVICE);  
    	return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
    }
    
    public static boolean isGpsAvailable(Context ctx) {
    	LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    	if (null == locationManager) {
    		return false;
    	}
    	
    	List<String> providers = locationManager.getAllProviders();
    	return !providers.isEmpty();
    }
    
    /**
     * @deprecated
     * 打开GPS
     */
    public static void openGps(Context ctx) {
    	ContentResolver mResolver = ctx.getContentResolver();
    	String provider = LocationManager.GPS_PROVIDER;
        if(!Settings.Secure.isLocationProviderEnabled(mResolver, provider)) {
        	Settings.Secure.setLocationProviderEnabled(mResolver, provider, true);
        }
    }
    
    /**
     * @deprecated
     * 关闭GPS
     */
    public static void turnOfffGps(Context ctx) {
    	ContentResolver mResolver = ctx.getContentResolver();
    	String provider = LocationManager.GPS_PROVIDER;
    	if(Settings.Secure.isLocationProviderEnabled(mResolver, provider)) {
        	Settings.Secure.setLocationProviderEnabled(mResolver, provider, false);
        }
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    
    public static boolean isWifiConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();
    }
    
    public static boolean isCameraAvailable(Context ctx) {
    	return ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
//        return Camera.getNumberOfCameras() > 0;

    }
    
    public static boolean isSDCardAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
    
    /**
	 * 获取应用程序版本号
	 */
	public static int getVerCode(Context context) {
		PackageInfo pi = getPackage(context);
		return (pi == null) ? 0 : pi.versionCode;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getVerName(Context context) {
		PackageInfo pi = getPackage(context);
		return (pi == null) ? "" : pi.versionName;
	}

	private static PackageInfo getPackage(Context context) {
		String packageName = context.getPackageName();
		try {
			return context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	};

	public static boolean isRunning(Context ctx, Class cls) {
		Intent intent = new Intent(ctx, cls);
		ComponentName cmpName = intent.resolveActivity(ctx.getPackageManager());
		if (cmpName != null) {
			ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
			for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
				if (taskInfo.baseActivity.equals(cmpName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断app是否在运行状态
	 */
	public static boolean isAppRunning(Context ctx) {
		return getRunningTask(ctx) != null;
	}

	/**
	 * 判断程序是否后台运行
	 */
	public static boolean isBackgroundRunning(Context ctx) {
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		KeyguardManager keyguardManager = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);

		if (activityManager == null) {
			return false;
		}

		List<RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo process : processList) {
			if (process.processName.startsWith(ctx.getPackageName())) {
				boolean isBackground = process.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE;
				boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
				return isBackground || isLockedState || !isScreenOn(ctx);
			}
		}

		return false;
	}

	/**
	 * 判断app正在运行的activity数量
	 */
	public static ActivityManager.RunningTaskInfo getRunningTask(Context ctx) {
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1000);
		String myPkgName = ctx.getPackageName();
		for (ActivityManager.RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(myPkgName) || info.baseActivity.getPackageName().equals(myPkgName)) {
				return info;
			}
		}
		return null;
	}

	public static int getRunningActivityCount(Context ctx) {
		ActivityManager.RunningTaskInfo taskInfo = getRunningTask(ctx);
		return taskInfo == null ? 0 : taskInfo.numActivities;
	}

	public static boolean isScreenOn(Context ctx) {
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	/**
	 * 获取手机厂商
	 */
	public static String getFirm() {
		return Build.MANUFACTURER;
	}
	
	/**
	 * 获取手机型号
	 */
	public static String getModel() {
		return Build.MODEL;
	}
	
	/**
	 * 获取操作系统版本
	 */
	public static String getOsVersion() {
		return Build.VERSION.RELEASE;
	}
	
	public static void deleteSmsRecordByBody(Context ctx, String body) {
		Uri uri = Uri.parse("content://sms/");
		ctx.getContentResolver().delete(uri, "body = ?", new String[]{body});
	}
	
	/**
	 * 显示软键盘
	 */
	public static void showKeyboard(Context ctx, EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);
	}
	
	/**
	 * 隐藏软键盘
	 */
	public static void hideKeyboard(Context ctx, EditText editText) {
		((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).
			hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
	}
	
	/**
	 * 设置光标位置
	 * 
	 * @param editText
	 */
	public static void setCursorPos(EditText editText) {
		String result = editText.getText().toString();
		editText.setSelection(TextUtils.isEmpty(result) ? 0 : result.length());
	}

	/**
	 * 回到桌面
	 * @param ctx
	 */
	public static void goHome(Activity ctx) {
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_HOME);
		ctx.startActivity(home);
	}

	/**
	 * 进入系统设置gps页面
	 */
	public static void goGps(Context ctx) {
		ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}

	/**
	 * 调用系统拨号器
	 * 
	 * @param ctx
	 *            上下文
	 * @param mobile
	 *            电话号码
	 */
	public static void dial(Context ctx, String mobile) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobile));
        ctx.startActivity(intent);
	}
	
	/**
	 * 调用系统发送短信界面
	 * 
	 * @param ctx
	 *            上下文
	 * @param mobile
	 *            要发送联系人的电话号码
	 * @param smsBody
	 *            短信内容
	 */
	public static void sendSMS(Context ctx, String mobile, String smsBody)
	{
		Uri smsToUri = Uri.parse("smsto:"+mobile);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		ctx.startActivity(intent);
	}
	
	/**
	 * 调用系统的添加联系人界面
	 * 
	 * @param ctx
	 *            上下文
	 * @param name
	 *            姓名
	 * @param mobile
	 *            电话号码
	 */
	public static void addContact(Context ctx, String name, String mobile) {
		Intent it = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts"));
		it.setType("vnd.android.cursor.dir/person");
		// it.setType("vnd.android.cursor.dir/contact");
		// it.setType("vnd.android.cursor.dir/raw_contact");
		// 联系人姓名
		it.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, name);
		// 公司
		// it.putExtra(android.provider.ContactsContract.Intents.Insert.COMPANY,
		// "北京XXXXXX公司");
		// email
		// it.putExtra(android.provider.ContactsContract.Intents.Insert.EMAIL,
		// "123456@qq.com");
		// 手机号码
		it.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, mobile);
		// 单位电话
		// it.putExtra(
		// android.provider.ContactsContract.Intents.Insert.SECONDARY_PHONE,
		// "18600001111");
		// 住宅电话
		// it.putExtra(
		// android.provider.ContactsContract.Intents.Insert.TERTIARY_PHONE,
		// "010-7654321");
		// 备注信息
		// it.putExtra(android.provider.ContactsContract.Intents.Insert.JOB_TITLE,
		// "名片");

		ctx.startActivity(it);
	}

	public static void setStatusBarColor(Activity act, int colorRes) {
		Window window = act.getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (colorRes == android.R.color.transparent) {
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
						| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(Color.TRANSPARENT);
			}
			else {
				window.setStatusBarColor(act.getResources().getColor(colorRes));
			}
		}
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(act);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(colorRes);
		}
	}

	public static void restartApp(Context ctx) {
		Intent launch = ctx.getPackageManager().getLaunchIntentForPackage(ctx.getPackageName());
		launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(launch);
	}

	public static void fixInputMethodManagerLeak(Context destContext) {
		if (destContext == null) {
			return;
		}

		InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) {
			return;
		}

		String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
		Field f = null;
		Object obj_get = null;
		for (int i = 0;i < arr.length;i ++) {
			String param = arr[i];
			try{
				f = imm.getClass().getDeclaredField(param);
				if (f.isAccessible() == false) {
					f.setAccessible(true);
				} // author: sodino mail:sodino@qq.com
				obj_get = f.get(imm);
				if (obj_get != null && obj_get instanceof View) {
					View v_get = (View) obj_get;
					if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
						f.set(imm, null); // 置空，破坏掉path to gc节点
					} else {
						// 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
//
						break;
					}
				}
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}

	public static boolean isPackageInstalled(Context ctx, String packageName) {
		if (ctx == null) {
			throw new IllegalArgumentException("the ctx args must not be null");
		}
		if (TextUtils.isEmpty(packageName)) {
			throw new IllegalArgumentException("the packageName args must not be empty");
		}
		try {
			return ctx.getPackageManager().getPackageInfo(packageName, 0) != null;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static void switchFullScreen(Activity ctx, boolean fullScreen) {
        if (fullScreen) {
            WindowManager.LayoutParams lp = ctx.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			ctx.getWindow().setAttributes(lp);
        }
		else {
            WindowManager.LayoutParams attr = ctx.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			ctx.getWindow().setAttributes(attr);
        }
	}

	public static void showKeyboard(Dialog dialog) {
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

}
