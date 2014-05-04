package android.hmm.lib.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class ScreenHelper {

	/**
	 * 屏幕状态
	 */
	public static void getWindowStata(Context context, Activity activityReference) {
		int v = activityReference.getWindow().getAttributes().flags;
		// 全屏 66816 - 非全屏 65792
		if (v != 66816) {
			activityReference.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			activityReference.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	/**
	 * 获取屏幕状态
	 * @param context  :Activity
	 * @return：true表示全屏，false表示非全屏
	 */
	public static boolean getWindowStata(Activity context) {
		WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
		if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 
			WindowManager.LayoutParams.FLAG_FULLSCREEN) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 显示屏幕信息
	 * @param context  :Activity
	 */
	public static DisplayMetrics getDisplayMetrics(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = context.getWindow().getWindowManager();
		wm.getDefaultDisplay().getMetrics(dm);
		// Log.i("message", "xdpi:"+dm.xdpi);
		// Log.i("message", "ydpi:"+dm.ydpi);
		// Log.i("message", "density:"+dm.density);
		// Log.i("message", "densityDpi:"+dm.densityDpi);
		// Log.i("message", "heightPixels:"+dm.heightPixels);
		// Log.i("message", "widthPixels:"+dm.widthPixels);
		// Log.i("message", "scaledDensity:"+dm.scaledDensity);
		return dm;
	}

	/**
	 * 
	 * @param context :Activity
	 * @param ScreenSize :保存屏幕大小的变量
	 * @return
	 */
	public static int[] getScreenSize(Activity context) {
		int[] tScreenSize = new int[2];
		return getScreenSize(context, tScreenSize);
	}
	
	/**
	 * 
	 * @param context :Activity
	 * @param size :保存屏幕大小的变量
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int[] getScreenSize(Activity context, int[] size) {
		WindowManager windowManager = context.getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		size[0] = display.getWidth();
		size[1] = display.getHeight();
		return size;
	}

	/**
	 * 退出全屏
	 * @param context :Activity
	 */
	public static void quitFullScreen(Activity context) {
		final WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		((Activity) context).getWindow().setAttributes(attrs);
		((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 设置成无标题栏
	 * @param context : Activity
	 */
	public static void setMatchScreen(Activity context) {
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 全屏 注意：有些方法是要看底层硬件处理
	 * @param context :Activity
	 */
	public static void setFullScreen(Activity activityReference) {
		activityReference.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

//		/**
//		 * 第二种方法： 在调用requestWindowFeature之后，才能设置setContentView,否则报错
//		 */
//		 try {
//			 activityReference.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			 int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | 0x80000000;
//			 activityReference.getWindow().setFlags(flags, flags);
//		 } catch (Exception e) {
//			 e.printStackTrace();
//		 }

		/**
		 * 第三种方法： Manifest.xml配置文件中
		 * android:theme="@android:style/Theme.NoTitleBar.Fullscreen" >
		 * android:theme="@android:style/Theme.NoTitleBar" >
		 */
	}

	protected void keepScreenOn(Activity activityReference) {
		// 为窗口添加flag:
		activityReference.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 或者
//		activityReference.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/**
	 * 使用电源管理： 需要权限 <uses-permission
	 * android:name="android.permission.WAKE_LOCK"/> 释放： if (mWakeLock.isHeld())
	 * mWakeLock.release();
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("Wakelock")
	protected void keepScreenOn(Context context, String className) {
		PowerManager.WakeLock mWakeLock;
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, className);
		mWakeLock.acquire();
	}

	/**
	 * 旋转屏幕 需要导入的包为：import android.provider.Settings;
	 * 在还得在manifest里面设置android:configChanges="orientation|keyboardHidden"
	 * android:screenOrientation="user" 并且要加上权限：
	 * android.permission.WRITE_SETTINGS
	 */
	public static void revolveScreen(Context context) {
		int flag = Settings.System.getInt(context.getContentResolver(), 
				Settings.System.ACCELEROMETER_ROTATION, 0);
		if (!Settings.System.putInt(context.getContentResolver(), 
				Settings.System.ACCELEROMETER_ROTATION, flag == 1 ? 0 : 1)) {
			return;
		}
	}

}
