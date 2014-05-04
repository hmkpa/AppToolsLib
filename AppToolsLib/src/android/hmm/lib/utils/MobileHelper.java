package android.hmm.lib.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.content.ComponentName;
import android.content.Context;
import android.hmm.lib.intent.IntentHelper;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-12
 * Description:  
 */
public class MobileHelper {

	/**********************************************************************
	 * 制造商
	 ***********************************************************************/
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	/**********************************************************************
	 * 设备
	 ***********************************************************************/
	public static String getDevice() {
		return android.os.Build.DEVICE;
	}

	/**********************************************************************
	 * 系统型号
	 ***********************************************************************/
	public static String getDisplay() {
		return android.os.Build.DISPLAY;
	}

	/**********************************************************************
	 * 品牌
	 ***********************************************************************/
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	/**********************************************************************
	 * SERIAL
	 ***********************************************************************/
	public static String getSERIAL() {
		return android.os.Build.SERIAL;
	}

	/**********************************************************************
	 * 系统型号
	 ***********************************************************************/
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**********************************************************************
	 * SDK_INT
	 ***********************************************************************/
	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**********************************************************************
	 * RELEASE
	 ***********************************************************************/
	public static String getAndroidVersion() {
		return android.os.Build.VERSION.RELEASE.toString();
//		android.os.Build.VERSION.INCREMENTAL.toString();
	}

	/**********************************************************************
	 * 获取当前系统的内核版本号
	 ***********************************************************************/
	public static String getKernelVersion() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("cat /proc/version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == process) { return null; }
		// get the output line
		InputStream outs = process.getInputStream();
		InputStreamReader isrout = new InputStreamReader(outs);
		BufferedReader brout = new BufferedReader(isrout, 8 * 1024);
		String result = "";
		String line;
		try {// get the whole standard output string
			while ((line = brout.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cutOutKernelString(result);
	}

	// 截取内核信息中有用的部分
	public static String cutOutKernelString(String text) {
		int idx = text.indexOf("gcc");
		String tempText = text.substring(0, idx - 1);
		idx = text.indexOf("#");
		tempText = tempText + "\n" + text.substring(idx);
		return tempText;
	}

	/***************************************************************************************************
	 * 比较两个应用程序的启动次数和运行时间
	 * @param a
	 * @param b
	 * @return
	 * 编译好的apk要安装到system/app目录下才可以得到PACKAGE_USAGE_STATS权限。
		具体使用方法，首先准备一部root过的手机。然手插上usb
		然后打开cmd命令编辑器，建议下载PowerCmd方便操作。
		输入adb shell回车
		输入su回车
		输入mount -o rw,remount /system回车
		
		再打开另外一个cmd命令编辑器
		输入adb push "你的apk绝对路径" system/app回车
		当出现
		XXXXkb/s     0.XXXs时表示已经放到系统应用下
		输入adb reboot回车
		
		这时会重启手机，手机重启后再打开应用就可以获得其他应用的启动次数了。
		可以进入手机打电话播*#*#4636#*#*对比一下。

	 ***************************************************************************************************/
	public final int compare(ComponentName aName, ComponentName bName) {
//		ComponentName aName = a.intent.getComponent();
//		ComponentName bName = b.intent.getComponent();
		int aLaunchCount, bLaunchCount;
		long aUseTime, bUseTime;
		int result = 0;

		try {

			// 获得ServiceManager类
			Class<?> ServiceManager = Class.forName("android.os.ServiceManager");

			// 获得ServiceManager的getService方法
			Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

			// 调用getService获取RemoteService
			Object oRemoteService = getService.invoke(null, "usagestats");

			// 获得IUsageStats.Stub类
			Class<?> cStub = Class.forName("com.android.internal.app.IUsageStats$Stub");
			// 获得asInterface方法
			Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
			// 调用asInterface方法获取IUsageStats对象
			Object oIUsageStats = asInterface.invoke(null, oRemoteService);
			// 获得getPkgUsageStats(ComponentName)方法
			Method getPkgUsageStats = oIUsageStats.getClass().getMethod("getPkgUsageStats", ComponentName.class);
			// 调用getPkgUsageStats 获取PkgUsageStats对象
			Object aStats = getPkgUsageStats.invoke(oIUsageStats, aName);
			Object bStats = getPkgUsageStats.invoke(oIUsageStats, bName);

			// 获得PkgUsageStats类
			Class<?> PkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");

			aLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(aStats);
			bLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(bStats);
			aUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(aStats);
			bUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(bStats);

			if ((aLaunchCount > bLaunchCount) || ((aLaunchCount == bLaunchCount) && (aUseTime > bUseTime))) {
				result = 1;
			} else if ((aLaunchCount < bLaunchCount) || ((aLaunchCount == bLaunchCount) && (aUseTime < bUseTime))) {
				result = -1;
			} else {
				result = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public final static long getRunTime(Context context, String packageName) {
		long useTime = 0;
		ComponentName cname = IntentHelper.getComponentName(context, packageName);
		if (cname == null) { return useTime; }

		try {
			// 获得ServiceManager类
			Class<?> ServiceManager = Class.forName("android.os.ServiceManager");

			// 获得ServiceManager的getService方法
			Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

			// 调用getService获取RemoteService
			Object oRemoteService = getService.invoke(null, "usagestats");

			// 获得IUsageStats.Stub类
			Class<?> cStub = Class.forName("com.android.internal.app.IUsageStats$Stub");
			// 获得asInterface方法
			Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
			// 调用asInterface方法获取IUsageStats对象
			Object oIUsageStats = asInterface.invoke(null, oRemoteService);
			// 获得getPkgUsageStats(ComponentName)方法
			Method getPkgUsageStats = oIUsageStats.getClass().getMethod("getPkgUsageStats", ComponentName.class);
			// 调用getPkgUsageStats 获取PkgUsageStats对象
			Object aStats = getPkgUsageStats.invoke(oIUsageStats, cname);

			// 获得PkgUsageStats类
			Class<?> PkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");
			useTime = PkgUsageStats.getDeclaredField("usageTime").getLong(aStats);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return useTime;
	}

	public final static int getRunCount(Context context, String packageName) {
		int aLaunchCount = 0;
		ComponentName name = IntentHelper.getComponentName(context, packageName);
		if (name == null) { return aLaunchCount; }
		try {

			// 获得ServiceManager类
			Class<?> ServiceManager = Class.forName("android.os.ServiceManager");

			// 获得ServiceManager的getService方法
			Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

			// 调用getService获取RemoteService
			Object oRemoteService = getService.invoke(null, "usagestats");

			// 获得IUsageStats.Stub类
			Class<?> cStub = Class.forName("com.android.internal.app.IUsageStats$Stub");
			// 获得asInterface方法
			Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
			// 调用asInterface方法获取IUsageStats对象
			Object oIUsageStats = asInterface.invoke(null, oRemoteService);
			// 获得getPkgUsageStats(ComponentName)方法
			Method getPkgUsageStats = oIUsageStats.getClass().getMethod("getPkgUsageStats", ComponentName.class);
			// 调用getPkgUsageStats 获取PkgUsageStats对象
			Object aStats = getPkgUsageStats.invoke(oIUsageStats, name);

			// 获得PkgUsageStats类
			Class<?> PkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");
			aLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(aStats);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return aLaunchCount;
	}

}
