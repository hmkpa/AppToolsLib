package android.hmm.lib.intent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hmm.lib.utils.ToastHelper;
import android.net.Uri;
import android.os.Bundle;

public class IntentHelper {

	/*******************************************************************************************
	 *  退出程序
	 *******************************************************************************************/
	public static void exitProgram() {
		android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(0);
		// killBackgroundProcesses(String packageName);
		/**
		 * 2.2以前可以 <!-- 关闭应用程序的权限 --><uses-permission
		 * android:name="android.permission.RESTART_PACKAGES" />
		 */
		// ActivityManager activityMgr= (ActivityManager)
		// this.getSystemService(ACTIVITY_SERVICE );
		// activityMgr.restartPackage(getPackageName());

		/**
		 * 
		 * 	话说killbackgroudprocesses结束其他进程时是没问题的，但是当其他进程里面包含service的时候，可能在service的销毁里面又包含了启动service的代码的话，就死的不完全了。
			里面activity是死了，但是service重生了又。
			说用反射调用forceStopPackage可以有效杀死
		 	
		 	Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);  
			forceStopPackage.setAccessible(true);  
			forceStopPackage.invoke(am, yourpkgname);
			加上权限
			<uses-permission android:name="android.permission.FORCE_STOP_PACKAGES"></uses-permission>
		 */
	}

	/**
	 * 隐藏程序 回到桌面
	 * @param context
	 */
	public static void hideProgram(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}

	/*******************************************************************************************
	 * 
	 *******************************************************************************************/
	public static void startEvent(Context context, Class<?> cls) {
		startEvent(context, cls, null);
	}

	public static void startEvent(Context context, String pkg) {
		startEvent(context, pkg, null, null, null, (String) null);
	}

	public static void startEvent(Context context, String pkg, String cls) {
		startEvent(context, pkg, cls, null, null, (String) null);
	}

	public static void startEvent(Context context, Class<?> cls, Bundle bundle) {
		startEvent(context, context.getPackageName(), cls.getName(), null, null, bundle);
	}

	public static void startEvent(Context context, String pkg, String cls, String action, String uriString, String extra) {
		try {
			Uri uri = null;
			if (uriString != null) {
				uri = Uri.parse(uriString);
			}
			Bundle bundle = null;
			if (extra != null) {
				bundle = new Bundle();
				String[] strArray = extra.split(",");
				for (String item : strArray) {
					String[] keyValue = item.split(":");
					bundle.putString(keyValue[0], keyValue[1]);
				}
			}
			startEvent(context, pkg, cls, action, uri, bundle);
		} catch (Exception ex) {
			ex.printStackTrace();
			ToastHelper.showCustomToast(context, ex.toString());
		}
	}

	public static void startEvent(Context context, String pkg, String cls, String action, Uri uri, Bundle bundle) {
		try {
			Intent itent = new Intent();
			if (action != null) itent.setAction(action);
			if (pkg != null) {
				itent.setPackage(pkg);
				if (cls != null) itent.setClassName(pkg, cls);
			}
			
			if (pkg != null && cls == null) {
				final PackageManager pm = context.getPackageManager();
				itent = pm.getLaunchIntentForPackage(pkg);
			}
			if (uri != null) {
				itent.setData(uri);
			}
			if (bundle != null) {
				itent.putExtras(bundle);
			}
			context.startActivity(itent);
		} catch (Exception ex) {
			ex.printStackTrace();
			ToastHelper.showCustomToast(context, ex.toString());
		}
	}

	public static ComponentName getComponentName(Context context, String packageName) {
		ComponentName cn = null;
		PackageManager pm = context.getPackageManager();
		Intent i = pm.getLaunchIntentForPackage(packageName);
		cn = i.getComponent();

//		Intent intent = new Intent(Intent.ACTION_MAIN, null);
//		intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		intent.setPackage(packageName);
//		List<ResolveInfo> infoList = context.getPackageManager().queryIntentActivities(intent, 0);
//		if (infoList != null && infoList.size() > 0) {
//			ResolveInfo info = infoList.get(0);
//			if (info != null) {
//				String pkgName = info.activityInfo.packageName;
//				String clsName = info.activityInfo.name;
//				cn = new ComponentName(pkgName, clsName);
//			}
//		}
		return cn;
	}

}
