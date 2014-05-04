package android.hmm.lib.http;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-10-21
 * Description:  
 */
public class APKHelper {
	//
	public static int getVersionCode(Context context) {
		return getVersionCode(context, context.getPackageName());
	}

	// 取版本编号
	public static int getVersionCode(Context context, String packageName) {
		try {
			PackageInfo pi = getPackageInfo(context, packageName);
			if (pi == null) { return -1; }
			return pi.versionCode;
		} catch (Exception e) {
		}
		return -1;
	}

	//
	public static String getVersionName(Context context) {
		return getVersionName(context, context.getPackageName());
	}

	// 取版本名
	public static String getVersionName(Context context, String packageName) {
		try {
			PackageInfo pi = getPackageInfo(context, packageName);
			if (pi == null || pi.versionName == null || pi.versionName.length() <= 0) { return ""; }
			return pi.versionName;
		} catch (Exception e) {
		}
		return "";
	}

	//
	public static PackageInfo getPackageInfo(Context context, String packageName) throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		return pm.getPackageInfo(packageName, 0);
	}
}
