package android.hmm.lib.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

public class SysConfigUtils {

	/**
	 * 获取GPS状态 
	 * <uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />
	 * @return:true表示关闭，false表示打开
	 */
	public static boolean getGPSStatus(Context context) {
		ClassLoader cl = context.getClassLoader();
		Class<?> secureClass;
		try {
			secureClass = cl.loadClass("android.provider.Settings$Secure");
			Method isMethod = secureClass.getMethod("isLocationProviderEnabled", ContentResolver.class, String.class);
			return (Boolean) isMethod.invoke(secureClass, context.getContentResolver(), "gps");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		// second method
		String str = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (str != null) { return str.contains("gps"); }

		return false;
	}

	/**
	 * 这是个纯开关，如果当前是开启的，那么就会关闭它，反之亦然。
	 * 测试通过,不用签名
	 * sdk：15(有人说4.0后会失效)
	 */
	public static void setGPRS(Context context) {
		String message = "关闭GPRS成功";
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
			if (!getGPSStatus(context)) {
				message = "打开GPRS成功";
			}
		} catch (CanceledException e) {
			e.printStackTrace();
			message = "打开GPRS出现异常";
		}
		ToastHelper.showCustomToast(context, message);
	}

	/**
	 * 测试通过(有说4.0以后失效了)
	 * 只要修改代码中的：in.setData(Uri.parse("custom:3"))，即能够实现对应的功能。
	 * 其中WIFI：0；背光高度：1；同步数据：2；GSP：3；蓝牙：4
	 * 背光调节时，会在自动->最低->中等->最高->自动间调节
	 * android version 2.3
	 */
	public static void configSwitch(Context context, int custom) {
		Intent in = new Intent();
		// 利用java反射功能，发送广播：
		in.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		in.addCategory("android.intent.category.ALTERNATIVE");
		in.setData(Uri.parse("custom:" + custom));
		try {
			PendingIntent.getBroadcast(context, 0, in, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试未通过，出现异常
	 * @param context
	 * @param enable
	 */
	public static void setGPRSStatus(Context context) {
		String message = "关闭GPRS成功";
		try {
			boolean enable = false;
			if (!getGPSStatus(context)) {
				message = "打开GPRS成功";
				enable = true;
			}
			Settings.Secure.putString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED, "network,gps");
			Settings.Secure.setLocationProviderEnabled(context.getContentResolver(), "gps", enable);
		} catch (Exception e) {
			e.printStackTrace();
			message = "打开GPRS出现异常";
		}
		ToastHelper.showCustomToast(context, message);
	}

	@SuppressWarnings("deprecation")
	public static void screenShor(Context context) {
		View v = ((Activity) context).getWindow().getDecorView();
		View view = v.getRootView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap1 = view.getDrawingCache(); // 取得全屏，包括状态栏，标题栏

		// 获取状态栏高度
		Rect frame = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 去除状态栏
		Bitmap bitmap = Bitmap.createBitmap(bitmap1, 0, statusBarHeight, width, height - statusBarHeight);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
			String fname = "/sdcard/" + dateFormat.format(new Date()) + ".png";// 把获得的图片保存到sd卡下
			FileOutputStream out = new FileOutputStream(fname);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			Toast.makeText(context, "截屏成功！", Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 截屏方法
	 * 
	 * @param view
	 * @param path
	 * @param fileName
	 *             
	 */
	public static boolean takeScreenShot(View view, String path) {
		boolean isSucc = false;
		/**
		 * 我们要获取它的cache先要通过setDrawingCacheEnable方法把cache开启，                  
		 * 然后再调用getDrawingCache方法就可以获得view的cache图片了。                  
		 * buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，          
		 * 若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。    
		 * 若果要更新cache,必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
		 */
		view.setDrawingCacheEnabled(true);// 开启获取缓存
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();// 得到View的cache
		Canvas canvas = new Canvas(bitmap);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
		String time = simple.format(new Date());

		// canvas.drawText(time, w-w/2, h-h/10, paint);
		canvas.save();
		canvas.restore();
		FileOutputStream fos = null;
		try {
			File sddir = new File(path);
			if (!sddir.exists()) {
				sddir.mkdir();
			}
			File file = new File(path + time + ".jpg");
			fos = new FileOutputStream(file);
			if (fos != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
				isSucc = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSucc;
	}

	/**
	 * 打开调试
	 * 系统签名+ <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
	 * <uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />
	 * 小米手机上测试不通过
	 */
	public static void openDebug(Context context, boolean enable) {
		String textshow = "";
		try {
			boolean enableAdb = (Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
			if (enable) {
				if (!enableAdb) {
					Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 1);
					textshow = "成功打开";
				} else {
					textshow = "已经打开";
				}
			} else {
				if (enableAdb) {
					Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
					textshow = "成功关闭";
				} else {
					textshow = "已经关闭";
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			textshow = "权限不够,须系统签名";
		}
		Toast.makeText(context, textshow, Toast.LENGTH_LONG).show();
	}

	/**
	 * 换壁纸
	 * 系统权限+<uses-permission android:name="android.permission.SET_WALLPAPER" />
	 * 测试通过,不用签名
	 * sdk：15
	 */
	public static void changeWall(Context context) {
		try {
			context.clearWallpaper();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, "清空桌面背景失败，请稍后再试", Toast.LENGTH_LONG).show();
		}
		InputStream is = DrawHelper.Drawable2InputStream(
				context.getResources().getDrawable(android.R.drawable.editbox_background));
		try {
			context.setWallpaper(is);
			Toast.makeText(context, "恭喜你设置成功！！", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(context, "设置桌面背景失败，请稍后再试", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

}
