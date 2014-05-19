package android.hmm.lib.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.hmm.lib.model.AppInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

public class AppHelper {

	private static String TAG = AppHelper.class.getName();
	public static final int TYPE_APP = 0;
	public static final int TYPE_ALLAPP = 1;
	public static final int TYPE_SYSTEMAPP = 2;
	public static final int TYPE_UERERAPP = 3;
	public static final int TYPE_SDCARDAPP = 4;
	public static final int TYPE_RUNNINGAPP = 5;

	/**
	 * 返回所有已安装的应用程序
	 */
	public static List<PackageInfo> getAllPackageInfoList(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageInfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		return packageInfos;
	}

	/**
	 * 返回所有已安装的应用程序
	 */
	public static List<ApplicationInfo> getAllApplicationInfoList(
			Context context) {
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packageInfos = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(packageInfos,
				new ApplicationInfo.DisplayNameComparator(pm));
		return packageInfos;
	}

	/**
	 * 返回所有已安装的应用程序
	 */
	public static List<ResolveInfo> getAllResolveInfoList(Context context) {
		PackageManager pm = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.MATCH_DEFAULT_ONLY);
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		return resolveInfos;
	}

	/**
	 * 判断某个应用程序是 不是三方的应用程序
	 * 
	 * @param applicationInfo
	 * @return 如果是第三方应用程序则返回true，如果是系统程序则返回false
	 */
	public static boolean isUserApp(ApplicationInfo applicationInfo) {
		if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据查询条件，查询特定的ApplicationInfo
	 * 
	 * @param context
	 * @param appType
	 * @return
	 */
	public static List<AppInfo> getCategoryApp(Context context, int appType) {
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> applicationInfos = getAllApplicationInfoList(context);
		List<AppInfo> appInfos = new ArrayList<AppInfo>(); // 保存过滤查到的AppInfo
		// 根据条件来过滤
		switch (appType) {
		case TYPE_ALLAPP: // 所有应用程序
			for (ApplicationInfo app : applicationInfos) {
				appInfos.add(initAppInfo(context, app));
			}
			break;
		case TYPE_SYSTEMAPP: // 系统程序
			for (ApplicationInfo app : applicationInfos) {
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					appInfos.add(initAppInfo(context, app));
				}
			}
			break;
		case TYPE_UERERAPP: // 第三方应用程序
			for (ApplicationInfo app : applicationInfos) {
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
					appInfos.add(initAppInfo(context, app));
				}
			}
			break;
		case TYPE_SDCARDAPP: // 安装在SDCard的应用程序
			for (ApplicationInfo app : applicationInfos) {
				if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
					appInfos.add(initAppInfo(context, app));
				}
			}
			break;
		case TYPE_RUNNINGAPP: // 正在运行的应用程序
			appInfos = GetRunningProcess(context, applicationInfos);
			appInfos.addAll(GetRunningService(context, applicationInfos));
			break;
		case TYPE_APP:
			List<ResolveInfo> list = getAllResolveInfoList(context);
			for (ResolveInfo app : list) {
				appInfos.add(initAppInfo(context,
						app.activityInfo.applicationInfo));
			}
			break;
		default:
			break;
		}
		return appInfos;
	}

	public class AppClose extends Application {
		private List<Activity> mainActivity = new ArrayList<Activity>();

		public List<Activity> MainActivity() {
			return mainActivity;
		}

		public void addActivity(Activity act) {
			mainActivity.add(act);
		}

		public List<Activity> getActivityList() {
			return mainActivity;
		}

		public void finishAll() {
			for (Activity act : mainActivity) {
				if (!act.isFinishing()) {
					act.finish();
				}
			}
			mainActivity = null;
		}
	}

	private static AppInfo initAppInfo(Context context, ApplicationInfo app) {
		AppInfo appInfo = null;
		if (null == app) {
			return appInfo;
		}
		PackageManager pm = context.getPackageManager();
		appInfo = new AppInfo();
		appInfo.setAppName(app.loadLabel(pm).toString());
		appInfo.setIcon(app.loadIcon(pm));
		appInfo.setPackageName(app.packageName);
		appInfo.setClassName(app.className);

//		appInfo.setPermisson(getPermisson(pm, app.packageName));
		appInfo.sePosition(getAppInstallPosition(context, app.packageName));
		appInfo.setTypeGroup(isUserApp(app) ? AppInfo.type_group_user
				: AppInfo.type_group_system);
		try {
			PackageInfo packinfo = pm.getPackageInfo(app.packageName,
					PackageManager.GET_SIGNATURES);
			appInfo.setSignatures((packinfo.signatures[0].toCharsString()));
			appInfo.setLastUpdateTime(TimerHelper.getFormatTime(
					TimerHelper.FormatDetail, packinfo.lastUpdateTime));
			appInfo.setFirstInstallTime(TimerHelper.getFormatTime(
					TimerHelper.FormatDetail, packinfo.firstInstallTime));
			
			String appFile = app.sourceDir;
			long installed = new File(appFile).lastModified();
//			UsageStat usageStat = getUsageStat(app);//apk需要在system目录下才行
			
			// 更新显示当前包得大小信息
			queryPacakgeSize(pm, appInfo);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return appInfo;
	}

	private static AppInfo initAppInfo(Context context,
			ActivityManager.RunningAppProcessInfo runningAppProcessInfo,
			GetApplicationInfo pInfo) {
		// 占用内存信息
		int[] pids = { runningAppProcessInfo.pid };
		// 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
		android.os.Debug.MemoryInfo[] memoryInfos = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE))
				.getProcessMemoryInfo(pids);
		// 获取进程占内存用信息 kb单位
		int memorysize = memoryInfos[0].getTotalPrivateDirty();

		AppInfo mAppInfo = initAppInfo(context, pInfo.getInfo(runningAppProcessInfo.processName));
		mAppInfo.setMemorysize(memorysize);
		mAppInfo.setProcessName(runningAppProcessInfo.processName);
		mAppInfo.setPid(runningAppProcessInfo.pid);
		mAppInfo.setUid(runningAppProcessInfo.uid);

		// 获得每个进程里运行的应用程序(包),即每个应用程序的包名
		String[] packageList = runningAppProcessInfo.pkgList;
//		for (String pkg : packageList) {
//			Log.i(TAG, "packageName " + pkg + " in process id is -->"
//					+ runningAppProcessInfo.pid);
//		}

		return mAppInfo;
	}

	/**
	 * 通过queryIntentActivities()方法，查询Android系统的所有具备ACTION_MAIN和CATEGORY_LAUNCHER
	 * 的Intent的应用程序，点击后，能启动该应用，说白了就是做一个类似Home程序的简易Launcher 。
	 */
	public List<AppInfo> queryAppInfo(Context context) {
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfos = getAllResolveInfoList(context);
		List<AppInfo> mSimpleLauncheritems = new ArrayList<AppInfo>();
	
		for (ResolveInfo reInfo : resolveInfos) {
			// 创建一个AppInfo对象，并赋值
			AppInfo appInfo = new AppInfo();
			appInfo.setAppName(reInfo.loadLabel(pm).toString());
			appInfo.setPackageName(reInfo.activityInfo.packageName);
			appInfo.setClassName(reInfo.activityInfo.name);
			appInfo.setIcon(reInfo.loadIcon(pm));
			mSimpleLauncheritems.add(appInfo); // 添加至列表中
		}
		
		return mSimpleLauncheritems;
	}

	public static void queryPacakgeSize(PackageManager mPackageManager,
			AppInfo appInfo) {
		if (appInfo == null) {
			return;
		}
		// 使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
		try {
			PkgSizeObserver mPkgSizeObserver = new PkgSizeObserver(appInfo);
			// 通过反射机制获得该隐藏函数
			/**
			 * android2.3有效 Method getPackageSizeInfo = mPackageManager.getClass
			 * ().getDeclaredMethod("getPackageSizeInfo", String.class,
			 * IPackageStatsObserver.class); // 调用该函数，并且给其分配参数
			 * ，待调用流程完成后会回调PkgSizeObserver类的函数
			 * getPackageSizeInfo.invoke(mPackageManager,
			 * appInfo.getPackageName(), mPkgSizeObserver);
			 */

			Method getPackageSizeInfo = mPackageManager.getClass()
					.getDeclaredMethod("getPackageSizeInfo", String.class,
							int.class, IPackageStatsObserver.class);
			/**
			 * after invoking, PkgSizeObserver.onGetStatsCompleted() will be
			 * called as callback function. <br>
			 * About the third parameter ‘Process.myUid() / 100000’，please
			 * check: <android_source>/frameworks/base/core/java/android/content
			 * /pm/PackageManager.java: getPackageSizeInfo(packageName,
			 * UserHandle.myUserId(), observer);
			 */
			getPackageSizeInfo.invoke(mPackageManager,
					appInfo.getPackageName(),
					android.os.Process.myUid() / 100000, mPkgSizeObserver);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// aidl文件形成的Bindler机制服务类
	public static class PkgSizeObserver extends IPackageStatsObserver.Stub {
		/***
		 * 回调函数，
		 * 
		 * @param pStatus
		 *            ,返回数据封装在PackageStats对象中
		 * @param succeeded
		 *            代表回调成功
		 */

		// 全局变量，保存当前查询包得信息
		private AppInfo app;

		public PkgSizeObserver(AppInfo app) {
			this.app = app;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			app.setCacheSize(pStats.cacheSize);
			app.setDatasize(pStats.dataSize);
			app.setCodeSize(pStats.codeSize);
		}
	}

	/**
	 * 查看权限
	 * @param pm
	 * @param pkgName
	 * @return
	 */
	public static String getPermisson(PackageManager pm, String pkgName) {
		String permisson = null;
		try {
			PackageInfo pkgInfo = pm.getPackageInfo(pkgName,
					PackageManager.GET_PERMISSIONS);// 通过包名，返回包信息
			String sharedPkgList[] = pkgInfo.requestedPermissions;// 得到权限列表

			for (int i = 0; null != sharedPkgList && i < sharedPkgList.length; i++) {
				String permName = sharedPkgList[i];
				// 通过permName得到该权限的详细信息
				PermissionInfo tmpPermInfo = pm.getPermissionInfo(permName, 0);
				// 权限分为不同的群组，通过权限名，我们得到该权限属于什么类型的权限。
				PermissionGroupInfo pgi = pm.getPermissionGroupInfo(
						tmpPermInfo.group, 0);

				permisson += i + "-" + permName + "\n";
				permisson += i + "-" + pgi.loadLabel(pm).toString() + "\n";
				permisson += i + "-" + tmpPermInfo.loadLabel(pm).toString()
						+ "\n";
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return permisson;
	}


	// Experimentally determined
	protected static final int auto = 0;
	protected static final int internalOnly = 1;
	protected static final int preferExternal = 2;

	/**
	 * 查看安装位置
	 * @param mContext
	 * @param pkg
	 * @return
	 */
	public static String getAppInstallPosition(Context mContext, String pkg) {
		AssetManager am = null;
		String result = null;
		XmlResourceParser xml = null;
		int eventType = 0;
		try {
			am = mContext.createPackageContext(pkg, 0).getAssets();
			xml = am.openXmlResourceParser("AndroidManifest.xml");
			eventType = xml.getEventType();

			xmlloop: while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (!xml.getName().matches("manifest")) {
						break xmlloop;
					} else {
						attrloop: for (int j = 0; j < xml.getAttributeCount(); j++) {
							if (xml.getAttributeName(j).matches(
									"installLocation")) {
								// switch
								// (Integer.parseInt(xml.getAttributeValue(j))
								if (xml.getAttributeValue(j).equals("0")) {
									result = "0";
								} else if (xml.getAttributeValue(j).equals("1")) {
									result = "1";
								} else if (xml.getAttributeValue(j).equals("2")) {
									result = "2";
								} else if (xml.getAttributeValue(j).equals(
										"internalOnly")) {
									result = "internalOnly";
								}
								break attrloop;
							}
						}
					}
					break;
				}
				eventType = xml.nextToken();
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void reMoveApp(Context mContext, String packageName) {
		// 对于2.2以上的版本可以用sdk的接口
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		String SCHEME = "package";
		Uri uri = Uri.fromParts(SCHEME, packageName, null);
		intent.setData(uri);

		// 对于Android2.2及以下的版本，采用非公开的借口
		// final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 :
		// APP_PKG_NAME_21);
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.setClassName(APP_DETAILS_PACKAGE_NAME,
		// APP_DETAILS_CLASS_NAME);
		// intent.putExtra(appPkgName, packageName);

		// 最后，跳转Activity
		mContext.startActivity(intent);
	}

	// 类PackageInfo主要包括ApplicationInfo getInfo(String name)方法
	public static class GetApplicationInfo {
		private List<ApplicationInfo> applicationInfos;

		public GetApplicationInfo(Context context) {
			this(getAllApplicationInfoList(context));
		}

		public GetApplicationInfo(List<ApplicationInfo> applicationInfos) {
			this.applicationInfos = applicationInfos;
		}

		public ApplicationInfo getInfo(String name) {
			if (name == null) {
				return null;
			}
			for (ApplicationInfo appInfo : applicationInfos) {
				if (name.equals(appInfo.processName)) {
					return appInfo;
				}
			}
			return null;
		}
	}

	/**
	 * 获取所有进程及用户 的进程
	 */
	@SuppressWarnings("static-access")
	public static List<AppInfo> GetRunningProcess(Context context,
			List<ApplicationInfo> applicationInfos) {
		List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE))
				.getRunningAppProcesses(); // 获取所有的进程
		// List<ActivityManager.RunningAppProcessInfo>
		// userRunningAppProcessInfos =
		// new ArrayList<ActivityManager.RunningAppProcessInfo>();// 获取用户的进程

		List<AppInfo> appList = new ArrayList<AppInfo>();

		for (int i = 0; i < runningAppProcessInfos.size(); i++) {
			// if ("system".equals(runningAppProcessInfos.get(i).processName)
			// ||
			// "Android.process.media".equals(runningAppProcessInfos.get(i).processName)
			// ||
			// "android.process.acore".equals(runningAppProcessInfos.get(i).processName))
			// {
			// continue;
			// }
			// userRunningAppProcessInfos.add(runningAppProcessInfos.get(i));

			GetApplicationInfo pInfo = new GetApplicationInfo(applicationInfos);
			if (null != pInfo
					.getInfo(runningAppProcessInfos.get(i).processName)
					&& !runningAppProcessInfos.get(i).processName
							.equals(context.getApplicationInfo().packageName)
					&& ((pInfo
							.getInfo(runningAppProcessInfos.get(i).processName).flags & pInfo
							.getInfo(runningAppProcessInfos.get(i).processName).FLAG_SYSTEM) == 0
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.contacts")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.email")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.settings")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.music")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.calendar")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.calculator2")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.browser")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.camera")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.cooliris.media")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.bluetooth")
							&& !(runningAppProcessInfos.get(i).processName)
									.equals("com.android.mms") && !(runningAppProcessInfos
								.get(i).processName)
							.equals("com.sohu.inputmethod"))) {

				AppInfo mAppInfo = initAppInfo(context,
						runningAppProcessInfos.get(i), pInfo);
				appList.add(mAppInfo);
			}
		}
		return appList;
	}

	@SuppressWarnings("static-access")
	public static List<AppInfo> GetRunningService(Context context,
			List<ApplicationInfo> applicationInfos) {
		List<AppInfo> appList = new ArrayList<AppInfo>();
		GetApplicationInfo pInfo = new GetApplicationInfo(applicationInfos);
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> appList2 = mActivityManager
				.getRunningServices(100);
		for (ActivityManager.RunningServiceInfo running : appList2) {
			// 获得该Service的组件信息 可能是pkgname/servicename
			ComponentName serviceCMP = running.service;
			String serviceName = serviceCMP.getShortClassName(); // service 的类名
			String pkgName = serviceCMP.getPackageName(); // 包名

			if ((running.flags & running.FLAG_SYSTEM_PROCESS) == 0) {
				AppInfo mAppInfo = initAppInfo(context,
						pInfo.getInfo(running.process));
				if (mAppInfo != null) {
					mAppInfo.setServiceFlag(1);
					appList.add(mAppInfo);
				}
			}
		}

		return appList;
	}

	/**
	 * ActivityManager.RunningAppProcessInfo { public int importance //
	 * 进程在系统中的重要级别 public int importanceReasonCode // 进程的重要原因代码 public
	 * ComponentName importanceReasonComponent // 进程中组件的描述信息 public int
	 * importanceReasonPid // 当前进程的子进程Id public int lru // 在同一个重要级别内的附加排序值
	 * public int pid // 当前进程Id public String[] pkgList // 被载入当前进程的所有包名 public
	 * String processName // 当前进程的名称 public int uid // 当前进程的用户Id }
	 */

	/**
	 * 获取系统的任务信息，需要用户权限：android.permission.GET_TASKS
	 * 
	 * ActivityManager.RunningTaskInfo { public ComponentName baseActivity //
	 * 任务做为第一个活动的组件信息 public CharSequence description // 任务当前状态的描述 public int id
	 * // 任务的ID public int numActivities // 任务中所包含的活动的数目 public int numRunning
	 * // 任务中处于运行状态的活动数目 public Bitmap thumbnail // 任务当前状态的位图表示，目前为null public
	 * ComponentName topActivity // 处于任务栈的栈顶的活动组件 }
	 */

	/**
	 * ActivityManager.RunningServiceInfo { public long activeSince //
	 * 服务第一次被激活的时间 (启动和绑定方式) public int clientCount // 连接到该服务的客户端数目 public int
	 * clientLabel // 【系统服务】为客户端程序提供用于访问标签 public String clientPackage //
	 * 【系统服务】绑定到该服务的包名 public int crashCount // 服务运行期间，出现crash的次数 public int
	 * flags // 服务运行的状态标志 public boolean foreground // 服务是否被做为前台进程执行 public long
	 * lastActivityTime // 该服务的最后一个活动的时间 public int pid // 非0值，表示服务所在的进程Id
	 * public String process // 服务所在的进程名称 public long restarting //
	 * 如果非0，表示服务没有执行，将在参数给定的时间点重启服务 public ComponentName service // 服务组件信息
	 * public boolean started // 标识服务是否被显示的启动 public int uid // 拥有该服务的用户Id }
	 */
	protected void getAppMessage(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获得系统运行的进程
		List<ActivityManager.RunningAppProcessInfo> appList1 = mActivityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo running : appList1) {
			System.out.println(running.processName);
		}
		System.out.println("================");
		// 获得当前正在运行的service
		List<ActivityManager.RunningServiceInfo> appList2 = mActivityManager
				.getRunningServices(100);
		for (ActivityManager.RunningServiceInfo running : appList2) {
			System.out.println(running.service.getClassName());
		}
		System.out.println("================"); // 获得当前正在运行的activity
		List<ActivityManager.RunningTaskInfo> appList3 = mActivityManager
				.getRunningTasks(1000);
		for (ActivityManager.RunningTaskInfo running : appList3) {
			System.out.println(running.baseActivity.getClassName());
		}
		System.out.println("================"); // 获得最近运行的应用
		List<ActivityManager.RecentTaskInfo> appList4 = mActivityManager
				.getRecentTasks(100, 1);
		for (ActivityManager.RecentTaskInfo running : appList4) {
			System.out.println(running.origActivity.getClassName());
		}
	}

	/**
	 *  杀死该进程，释放进程占用的空间
	 *  <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"></uses-permission>
	 * @param context
	 * @param pkg
	 */
	public static void killProcess(Context context, String pkg) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		mActivityManager.killBackgroundProcesses(pkg);
	}

	// 查看运行在该进程的应用程序
	public static void showAPPInProcess() {
	}

	/**
	 *  某一特定进程里所有正在运行的应用程序
	 * @param context
	 * @param info
	 * @return
	 */
	public static List<AppInfo> querySpecailPIDRunningAppInfo(Context context,
			AppInfo info) {
		ActivityManager.RunningAppProcessInfo appProcessInfo = null;
		String[] pkgNameList = appProcessInfo.pkgList;
		String processName = info.getProcessName();

		PackageManager pm = context.getPackageManager();
		// 保存所有正在运行的应用程序信息
		List<AppInfo> runningAppInfos = new ArrayList<AppInfo>(); // 保存过滤查到的AppInfo

		for (int i = 0; i < pkgNameList.length; i++) {
			// 根据包名查询特定的ApplicationInfo对象
			ApplicationInfo appInfo;
			try {
				appInfo = pm.getApplicationInfo(pkgNameList[i], 0);
				// getAppInfo()//初始化appinfo
				// runningAppInfos.add(getAppInfo(appInfo, info.getPid(),
				// processName));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} // 0代表没有任何标记;
		}
		return runningAppInfos;
	}

	/**
	 *  查询所有正在运行的应用程序信息： 包括他们所在的进程id和进程名
	 *  这儿我直接获取了系统里安装的所有应用程序，然后根据报名pkgname过滤获取所有真正运行的应用程序
	 * @param context
	 * @return
	 */
	public static List<AppInfo> queryAllRunningAppInfo(Context context) {
		// 查询所有已经安装的应用程序
		List<ApplicationInfo> listAppcations = getAllApplicationInfoList(context);

		// 保存所有正在运行的包名 以及它所在的进程信息
		Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = am
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
			int pid = appProcess.pid; // pid
			String processName = appProcess.processName; // 进程名
			Log.i(TAG, "processName: " + processName + "  pid: " + pid);

			String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包

			// 输出所有应用程序的包名
			for (int i = 0; i < pkgNameList.length; i++) {
				String pkgName = pkgNameList[i];
				Log.i(TAG, "packageName " + pkgName + " at index " + i
						+ " in process " + pid);
				// 加入至map对象里
				pgkProcessAppMap.put(pkgName, appProcess);
			}
		}
		// 保存所有正在运行的应用程序信息
		List<AppInfo> runningAppInfos = new ArrayList<AppInfo>(); // 保存过滤查到的AppInfo

		for (ApplicationInfo app : listAppcations) {
			// 如果该包名存在 则构造一个RunningAppInfo对象
			if (pgkProcessAppMap.containsKey(app.packageName)) {
				// 获得该packageName的 pid 和 processName
				int pid = pgkProcessAppMap.get(app.packageName).pid;
				String processName = pgkProcessAppMap.get(app.packageName).processName;
				runningAppInfos.add(initAppInfo(context, app));
			}
		}
		return runningAppInfos;
	}

	/**
	 *  判断包名是否存在
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean existPackageName(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 得到当前系统的包名
	 */
	public static String getLauncherPackageName(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		ResolveInfo res = context.getPackageManager()
				.resolveActivity(intent, 0);
		if (res.activityInfo == null) {
			return "";
		}
		// 如果是不同桌面主题，可能会出现某些问题，这部分暂未处理
		if (res.activityInfo.packageName.equals("android")) {
			return "";
		} else {
			return res.activityInfo.packageName;
		}
	}

	/**
	 * 调用系统安装功能
	 */
	public static void startSysInstall(Context context, String filePath) {
		Intent intent = new Intent();
		intent.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 调用系统卸载功能
	 */
	public static void startSysUninstall(Context context, String pkg) {
		try {
			Uri packageURI = Uri.parse("package:" + pkg);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
					packageURI);
			uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(uninstallIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取启动次数和运行时间
	 * 需要是放在system目录下面,有系统权限
	 * @param a
	 * @return
	 */
	private static UsageStat getUsageStat(ApplicationInfo a) {
		ComponentName aName = new ComponentName(a.packageName, a.className);
		int aLaunchCount = 0;
		long aUseTime = 0;
		UsageStat result = new UsageStat();

		try {
			// 获得ServiceManager类
			Class<?> ServiceManager = Class
					.forName("android.os.ServiceManager");

			// 获得ServiceManager的getService方法
			Method getService = ServiceManager.getMethod("getService",
					java.lang.String.class);

			// 调用getService获取RemoteService
			Object oRemoteService = getService.invoke(null, "usagestats");

			// 获得IUsageStats.Stub类
			Class<?> cStub = Class
					.forName("com.android.internal.app.IUsageStats$Stub");
			// 获得asInterface方法
			Method asInterface = cStub.getMethod("asInterface",
					android.os.IBinder.class);
			// 调用asInterface方法获取IUsageStats对象
			Object oIUsageStats = asInterface.invoke(null, oRemoteService);
			// 获得getPkgUsageStats(ComponentName)方法
			Method getPkgUsageStats = oIUsageStats.getClass().getMethod(
					"getPkgUsageStats", ComponentName.class);
			// 调用getPkgUsageStats 获取PkgUsageStats对象
			Object aStats = getPkgUsageStats.invoke(oIUsageStats, aName);

			// 获得PkgUsageStats类
			Class<?> PkgUsageStats = Class
					.forName("com.android.internal.os.PkgUsageStats");

			aLaunchCount = PkgUsageStats.getDeclaredField("launchCount")
					.getInt(aStats);
			// bLaunchCount =
			// PkgUsageStats.getDeclaredField("launchCount").getInt(bStats);
			aUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(
					aStats);
			// bUseTime =
			// PkgUsageStats.getDeclaredField("usageTime").getLong(bStats);
		} catch (Exception e) {
			Log.e("###", e.toString(), e);
		}
		result.setCount(aLaunchCount);
		result.setUsageTime(TimerHelper.getFormatTime(TimerHelper.FormatDetail, aUseTime));
		return result;
	}

	private static class UsageStat {
		private int count = 0;
		private String usageTime;

		public UsageStat() {
		}

		public void setUsageTime(String usageTime) {
			this.usageTime = usageTime;
		}

		public String getUsageTime() {
			return usageTime;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}
	}


}
