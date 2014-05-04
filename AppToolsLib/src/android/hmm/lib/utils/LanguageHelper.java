package android.hmm.lib.utils;

import java.lang.reflect.Method;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-12
 * Description:  
 */
public class LanguageHelper {
	/**
	 *  根据标识 设置选择的语言环境
	 */
	public static void settingLanguage(Activity indexActivity, Locale locale) {
//		if (tempNumber == 0) {
//			// 设置本地语言为中文
//			locale = Locale.SIMPLIFIED_CHINESE;
//		} else if (tempNumber == 1) {
//			// 设置本地语言为英文
//			locale = Locale.ENGLISH;
//		} else {
//			// 设置本地语言为日文
//			locale = Locale.JAPAN;
//		}
		if (null == locale || Locale.getDefault().equals(locale)) {
			return;
		}

		Locale.setDefault(locale);// 设置选定的语言
		Configuration config = new Configuration();
		// 更新设置
		config.locale = locale;
		indexActivity.getBaseContext().getResources().updateConfiguration(config, indexActivity.getBaseContext().getResources().getDisplayMetrics());
		indexActivity.finish();
		// 重启当前界面
		indexActivity.startActivity(indexActivity.getIntent());
		// @toto 为了确保下次进入程序记住当前选择的语言，可以使用SharedPreferences
		// 来记住设置。values-en-rUS values-ja-rJP values-zh-rCN
//		SharedPreferences sharedPreferences = indexActivity.getSharedPreferences("phantom", Context.MODE_PRIVATE);
//		sharedPreferences.edit().putString("locale", locale.toString()).commit();
//		// 设置成功后的提示信息
//		if (locale == Locale.SIMPLIFIED_CHINESE) {
//			Toast.makeText(indexActivity, R.string.ab_msg_chinese_setted, 1000).show();
//		} else if (locale == Locale.ENGLISH) {
//			Toast.makeText(indexActivity, R.string.ab_msg_english_setted, 1000).show();
//		} else {
//			Toast.makeText(indexActivity, R.string.ab_msg_japanese_setted, 1000).show();
//		}
	}

	public static void setLocaleLanguage(Context context, Locale locale) {
		Resources resources = context.getResources();// 获得res资源对象
		Configuration config = resources.getConfiguration();// 获得设置对象
		DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
//		config.locale = Locale.SIMPLIFIED_CHINESE; // 简体中文
		config.locale = locale; // 简体中文
		resources.updateConfiguration(config, dm);

//		LocalePicker.updateLocale(locale);
	}

	public static Locale getLocale() {
		Locale locale = null;
		try {
			Object objIActMag, objActMagNative;
			Class<?> clzIActMag = Class.forName("android.app.IActivityManager");
			Class<?> clzActMagNative = Class.forName("android.app.ActivityManagerNative");
			Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
			// IActivityManager iActMag = ActivityManagerNative.getDefault();
			objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
			// Configuration config = iActMag.getConfiguration();
			Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
			Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
			locale = config.locale;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locale;
	}

	public static boolean updateLanguage(Locale locale) {
		try {
			Object objIActMag, objActMagNative;
			Class<?> clzIActMag = Class.forName("android.app.IActivityManager");
			Class<?> clzActMagNative = Class.forName("android.app.ActivityManagerNative");
			Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
			// IActivityManager iActMag = ActivityManagerNative.getDefault();
			objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
			// Configuration config = iActMag.getConfiguration();
			Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
			Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
			config.locale = locale;
			// iActMag.updateConfiguration(config);
			// 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
			// 会重新调用 onCreate();
			Class<?>[] clzParams = { Configuration.class };
			Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod("updateConfiguration", clzParams);
			mtdIActMag$updateConfiguration.invoke(objIActMag, config);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
