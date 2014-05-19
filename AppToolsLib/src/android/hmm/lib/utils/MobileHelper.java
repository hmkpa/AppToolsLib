package android.hmm.lib.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.ComponentName;
import android.content.Context;
import android.hmm.lib.intent.IntentHelper;
import android.hmm.lib.reflect.ReflectHelper;

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
	 * Other
	 *      
		Build.BOARD // 主板  
		Build.BRAND // android系统定制商  
		Build.CPU_ABI // cpu指令集  
		Build.DEVICE // 设备参数  
		Build.DISPLAY // 显示屏参数  
		Build.FINGERPRINT // 硬件名称  
		Build.HOST  
		Build.ID // 修订版本列表  
		Build.MANUFACTURER // 硬件制造商  
		Build.MODEL // 版本  
		Build.PRODUCT // 手机制造商  
		Build.TAGS // 描述build的标签  
		Build.TIME  
		Build.TYPE // builder类型  
		Build.USER 
		
		// 当前开发代号  
		Build.VERSION.CODENAME  
		// 源码控制版本号  
		Build.VERSION.INCREMENTAL  
		// 版本字符串  
		Build.VERSION.RELEASE  
		// 版本号  
		Build.VERSION.SDK  
		// 版本号  
		Build.VERSION.SDK_INT
		
		
		Build.VERSION.SDK_INT可与switch搭配用  
		switch (Build.VERSION.SDK_INT) {  
		case Build.VERSION_CODES.BASE: // 1.0  
		    break;  
		      
		case Build.VERSION_CODES.BASE_1_1: // 1.1  
		    break;  
		      
		case Build.VERSION_CODES.CUPCAKE: // 1.5  
		    break;  
		      
		case Build.VERSION_CODES.CUR_DEVELOPMENT: // current dev version  
		    break;  
		      
		case Build.VERSION_CODES.DONUT: // 1.6  
		    break;  
		      
		case Build.VERSION_CODES.ECLAIR: // 2.0  
		    break;  
		      
		case Build.VERSION_CODES.ECLAIR_0_1: // 2.0.1  
		    break;  
		      
		case Build.VERSION_CODES.ECLAIR_MR1: // 2.1  
		    break;  
		}  
	 ***********************************************************************/
	public static String getOther(Context context) {
		String result = "BOARD=" +android.os.Build.BOARD;
		result += "\nBOOTLOADER=" +android.os.Build.BOOTLOADER;
		result += "\nFINGERPRINT=" +android.os.Build.FINGERPRINT;
		result += "\nHARDWARE=" +android.os.Build.HARDWARE;
		result += "\nHOST=" +android.os.Build.HOST;
		result += "\nID=" +android.os.Build.ID;
		result += "\nPRODUCT=" +android.os.Build.PRODUCT;
		result += "\nRADIO=" +android.os.Build.RADIO;
		result += "\nTAGS=" +android.os.Build.TAGS;
		result += "\nTIME=" +android.os.Build.TIME;
		result += "\nTYPE=" +android.os.Build.TYPE;
		result += "\nUSER=" +android.os.Build.USER;
		result += "\ngetRadioVersion=" +android.os.Build.getRadioVersion();
		result += "\ngetFirmWare=" + getFirmWare(context);
		
		result += "\nVERSION.INCREMENTAL=" + android.os.Build.VERSION.INCREMENTAL;
		result += "\nVERSION.CODENAME=" + android.os.Build.VERSION.CODENAME;
		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION.RELEASE;
		
		
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.BASE;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.BASE_1_1;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.CUPCAKE;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.CUR_DEVELOPMENT;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.DONUT;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.ECLAIR;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.ECLAIR_0_1;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.ECLAIR_MR1;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.FROYO;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.GINGERBREAD;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.GINGERBREAD_MR1;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.HONEYCOMB;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.HONEYCOMB_MR1;
//		result += "\nVERSION.RELEASE=" + android.os.Build.VERSION_CODES.ECLAIR_0_1;
		
		return result;
	}
	
	public static String getFirmWare(Context context){
		String result = null;
		try {
			Object object =	ReflectHelper.getConstructor(context, "android.os.Build");
			result = ReflectHelper.getDeclaredField(object.getClass(), "FIRMWARE").toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
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
