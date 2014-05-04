package android.hmm.lib.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.util.Log;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-20
 * Description:  
 */
public class ClearCacheHelper {
	/* * 文 件 名: DataCleanManager.java * 描 述: 主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录 */

	/** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
	}

	/**
	 * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
	 * context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
	}

	/** * 按名字清除本应用数据库 * * @param context * @param dbName */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
	 * context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/** * 清除本应用所有的数据 * * @param context * @param filepath */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/****************************************************************************************
	 * 
	 * ****************************************************************************************/
	/**
	 * 获得手机中所有程序的缓存
	 */
	private void queryToatalCache(PackageManager pm) {
		List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
		String pkgName = "";
		for (ApplicationInfo info : apps) {
			pkgName = info.packageName;
//			try {
////			queryPkgCacheSize(pkgName);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}

	public static void clearApplicationUserData(Context context, String pkg) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		pkg = "com.android.providers.contacts";
		try {
			Class<?> c1 = IPackageDataObserver.class;
			Method method = pm.getClass().getMethod("clearApplicationUserData", String.class, c1);
			method.invoke(am, pkg, new IPackageDataObserver.Stub() {
				public void onRemoveCompleted(final String packageName, final boolean succeeded) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Sandy", "", e);
		}
	}

	/**
	 * 一种带root权限的：
	 * <uses-permission android:name="android.permission.DELETE_CACHE_FILES" />
	 */
	public static void deleteApplicationCacheFiles(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();

		try {
			Class<?> c1 = IPackageDataObserver.class;
			Method method = pm.getClass().getMethod("deleteApplicationCacheFiles", String.class, c1);
			method.invoke(pm, packageName, new IPackageDataObserver.Stub() {
				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 一种不带root权限的：
	 * <uses-permission android:name="android.permission.DELETE_CACHE_FILES" />
	 */
	public static void clearCache(Context context) throws Exception {
		PackageManager pm = context.getPackageManager();
		Class<?>[] arrayOfClass = new Class[2];
		Class<?> localClass2 = Long.TYPE;
		arrayOfClass[0] = localClass2;
		arrayOfClass[1] = IPackageDataObserver.class;
		Method localMethod = pm.getClass().getMethod("freeStorageAndNotify", arrayOfClass);
		Long localLong = Long.valueOf(getEnvironmentSize() - 1L);
		Object[] arrayOfObject = new Object[2];
		arrayOfObject[0] = localLong;
		localMethod.invoke(pm, localLong, new IPackageDataObserver.Stub() {
			public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

			}
		});
	}

	private static long getEnvironmentSize() {
		File localFile = Environment.getDataDirectory();
		long l1;
		if (localFile == null) l1 = 0L;
		while (true) {

			String str = localFile.getPath();
			StatFs localStatFs = new StatFs(str);
			long l2 = localStatFs.getBlockSize();
			l1 = localStatFs.getBlockCount() * l2;
			Log.e("清理缓存", str + "：" + l1);
			return l1;
		}
	}

}
