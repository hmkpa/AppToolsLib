package android.hmm.lib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hmm.lib.BuildConfig;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-12-23
 * Description:  
 * 
 * 当然对于测试人员还是应该检查该属性的，比如使用aapt工具：
	 aapt list -v -a myfile.apk
	
	 这个命令将会打印和apk相关的所有详细信息
	 这个命令将会打印和apk相关的所有详细信息，找到“android:debuggable"，它的值分为：
	
	 0x0: debuggable false
	 0xffffffff: debugabble true
	 
	 例如，在我的测试中，这一行的信息是：
	 A: android ebuggable(0x0101000f)=(type 0x12)0x0
	
	 这说明我的Release Build已经关闭了debuggable！
	
	还有一种测试方法：
	 使用android cts测试，http://source.android.com/compatibility/cts-intro.html
	 这是一种单元测试的方法，具体用到的类是android.permission.cts.DebuggableTest。
	testNoDebuggable：如果是true，说明debuggable false;
	testNoDebuggable：如果是false，说明debuggable true。

 */
public class DebugMode {

	protected static boolean isApkDebugable(Context context) {
		PackageManager mgr = context.getPackageManager();
		try {
			ApplicationInfo info = mgr.getApplicationInfo(context.getPackageName(), 0);
//			android.util.Log.d(TAG, "ApplicationInfo.FLAG_DEBUGGABLE: " + ApplicationInfo.FLAG_DEBUGGABLE);
//			android.util.Log.d(TAG, "applicationInfo.flags: " + info.flags);
//			android.util.Log.d(TAG, "info.flags &amp; ApplicationInfo.FLAG_DEBUGGABLE: " + (info.flags & ApplicationInfo.FLAG_DEBUGGABLE));
//			android.util.Log.d(TAG, "debug: " + ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE));
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
		} catch (NameNotFoundException e) {
		}
		return false;
	}

	protected static boolean isApkDebugable(Context context, String packageName) {
		try {
			android.content.pm.PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName, 1);
			if (pkginfo != null) {
				ApplicationInfo info = pkginfo.applicationInfo;
				return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * the version of adt should Equal or Bigger than 17
	 * @return
	 */
	public static boolean isApkDebugable() {
		return BuildConfig.DEBUG;
	}

}
