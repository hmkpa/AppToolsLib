package android.hmm.lib.utils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-11
 * Description:  
 */
public class TimerHelper {
	
	private static int AUTO_TIME_ON = 1;
	private static int AUTO_TIME_OFF = 0;
	public static final String FormatData = "yyyy-MM-dd";
	public static final String FormatTime = "hh:mm:ss";
	public static final String FormatDetail = "yyyy年MM月dd日 HH:mm:ss";
	
	/**
	 * 	SystemClock
	  	currentTimeMillis()  系统时间，也就是日期时间，可以被系统设置修改，然后值就会发生跳变。
		uptimeMillis 自开机后，经过的时间，不包括深度睡眠的时间
		elapsedRealtime自开机后，经过的时间，包括深度睡眠的时间
	 *  @return
	 */
	public static long getSysRunTime(){
		long runTime = SystemClock.elapsedRealtime();
//		runTime = SystemClock.elapsedRealtimeNanos();
		return runTime;
	}

	public static String getDateString() {
		String date = "";
		return date;
	}

	public static String getTimeString() {
		String time = "";
		return time;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getFormatTime(String format, long time) {
		String str = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			str = formatter.format(new Date(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**********************************************************************
	 * 
	 ***********************************************************************/
	public static String getSysRunTimeFormat() {
		long ut = SystemClock.elapsedRealtime() / 1000;
		if (ut == 0) {
			ut = 1;
		}
		int s = (int) (ut % 60);
		int m = (int) ((ut / 60) % 60);
		int h = (int) ((ut / 3600));

		return h + " : " + m + " : " + s;
	}
	
	/**********************************************************************
	 * 同步时间
	 * 需要把ap放到system/app目录下面才可以
	 ***********************************************************************/
	public static void handlerSyncTime(Context context) {
		if (compareIsRightTime(context)) {
			if (!getAutoStatus(context)) {
				setAutoStatus(context, false);
			}
			return;
		}
		setAutoStatus(context, getAutoStatus(context));
	}

	private static boolean compareIsRightTime(Context context) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = df.parse("2004-03-25");
			Date d2 = new Date();// 获取当前时间
			return d1.before(d2) ? true : false;
//			return (d1.getTime() - d2.getTime()) > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
		}
		return version;
	}

	private static boolean getAutoStatus(Context context) {
		int nAutoTimeStatus = AUTO_TIME_ON;
		if (getAndroidSDKVersion() > 16) {
			try {
//				android.provider.Settings.Global.getInt( context.getContentResolver(), Settings.Global.AUTO_TIME, OFF);
				ClassLoader cl = context.getClassLoader();
				Class<?> secureClass = cl.loadClass("android.provider.Settings$Global");
				Method isMethod = secureClass.getMethod("getInt", ContentResolver.class, String.class, int.class);
				nAutoTimeStatus = (Integer) isMethod.invoke(secureClass, context.getContentResolver(), "auto_time", AUTO_TIME_OFF);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			nAutoTimeStatus = Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME, AUTO_TIME_OFF);
		}
		return nAutoTimeStatus == AUTO_TIME_ON ? true : false;
	}

	private static void setAutoStatus(Context context, boolean reset) {
		if (getAndroidSDKVersion() > 16) {
			try {
				ClassLoader cl = context.getClassLoader();
				Class<?> secureClass = cl.loadClass("android.provider.Settings$Global");
				Method isMethod = secureClass.getMethod("putInt", ContentResolver.class, String.class, int.class);
				if (reset) {
					isMethod.invoke(secureClass, context.getContentResolver(), "auto_time", AUTO_TIME_OFF);
				}
				isMethod.invoke(secureClass, context.getContentResolver(), "auto_time", AUTO_TIME_ON);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (reset) {
				Settings.System.putInt(context.getContentResolver(), Settings.System.AUTO_TIME, AUTO_TIME_OFF);
			}
			Settings.System.putInt(context.getContentResolver(), Settings.System.AUTO_TIME, AUTO_TIME_ON);
		}
	}

}
