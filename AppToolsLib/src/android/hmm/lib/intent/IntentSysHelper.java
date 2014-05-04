package android.hmm.lib.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;

public class IntentSysHelper {

	/**
	 * shutdown
	 */
	public static void shutDown(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.ACTION_SHUTDOWN");
		context.sendBroadcast(intent);
	}

	/*
	 * 需要加上相关的权限： android:sharedUserId="android.uid.system" <uses-permission android:name="android.permission.SHUTDOWN"/>
	 */
	public static void shutDownNow(Context mContext) {
		//		Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
		////	intent.putExtra(Intent.EXTRA_KEY_CONFIRM,false);//是否需要用户点击确认
		//		intent.putExtra("android.intent.extra.KEY_CONFIRM",true);
		//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
		//		intent.putExtra(Intent.EXTRA_KEY_CONFIRM,false);//是否需要用户点击确认
		intent.putExtra("android.intent.extra.KEY_CONFIRM", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	public static void shutDown() {
		try {
			//			Runtime.getRuntime().exec("su -c \"/system/bin/shutdown\"");
			Runtime.getRuntime().exec("su -c \"/system/bin/poweroff -f\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void reboot() {
		try {
			/*
			 * one way
			 */
			//			 Process proc =Runtime.getRuntime().exec(newString[]{"su","-c","reboot -p"});
			//			 proc.waitFor();

			/*
			 * two way
			 */
			Runtime.getRuntime().exec("su -c \"/system/bin/reboot\"");

			/*
			 * three way
			 */
			//			String cmd = "su -c reboot";
			//			try {
			//				Runtime.getRuntime().exec(cmd);
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void rebootNow(Context context) {
		/*
		 * one way need add android.uid.system
		 */
		//		Intent i = new Intent(Intent.ACTION_REBOOT);
		//		i.putExtra("nowait", 1);
		//		i.putExtra("interval", 1);
		//		i.putExtra("window", 0);
		//		context.sendBroadcast(i);

		/*
		 * two way 需要加签名
		 */
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		pm.reboot(null);

	}

	/**
	 * use the default method open the web
	 */
	public static void startBrowsers(Context context, String addr) {
		if (addr != null) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.parse(addr);
			intent.setData(uri);
			intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			context.startActivity(intent);
		}
	}

	/**
	 * open the web find all ways can do
	 */
	public static void startWeb(Context context, String addr) {
		Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(addr));
		context.startActivity(viewIntent);
	}

	public static void startToHome(Context context) {
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		home.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(home);
	}

	/**
	 * 使用下面字段可以在软件中直接打开相应的系统界面 　　
	 * com.android.settings.AccessibilitySettings 辅助功能设置 　　
	 * com.android.settings.ActivityPicker 选择活动 　　
	 * com.android.settings.ApnSettings APN设置 　　
	 * com.android.settings.ApplicationSettings 应用程序设置
	 * com.android.settings.BandMode 设置GSM/UMTS波段 　　
	 * com.android.settings.BatteryInfo 电池信息
	 * com.android.settings.DateTimeSettings 日期和坝上旅游网时间设置 　　
	 * com.android.settings.DateTimeSettingsSetupWizard 日期和时间设置
	 * com.android.settings.DevelopmentSettings 应用程序设置=》开发设置 　　
	 * com.android.settings.DeviceAdminSettings 设备管理器
	 * com.android.settings.DeviceInfoSettings 关于手机 　　
	 * com.android.settings.Display 显示——设置显示字体大小及预览
	 * com.android.settings.DisplaySettings 显示设置 　　
	 * com.android.settings.DockSettings 底座设置
	 * com.android.settings.IccLockSettings SIM卡锁定设置 　　
	 * com.android.settings.InstalledAppDetails 语言和键盘设置
	 * com.android.settings.LanguageSettings 语言和键盘设置 　　
	 * com.android.settings.LocalePicker 选择手机语言
	 * com.android.settings.LocalePickerInSetupWizard 选择手机语言 　　
	 * com.android.settings.ManageApplications 已下载（安装）软件列表
	 * com.android.settings.MasterClear 恢复出厂设置 　　
	 * com.android.settings.MediaFormat 格式化手机闪存
	 * com.android.settings.PhysicalKeyboardSettings 设置键盘 　　
	 * com.android.settings.PrivacySettings 隐私设置
	 * com.android.settings.ProxySelector 代理设置 　　
	 * com.android.settings.RadioInfo 手机信息
	 * com.android.settings.RunningServices 正在运行的程序（服务） 　　
	 * com.android.settings.SecuritySettings 位置和安全设置
	 * com.android.settings.Settings 系统设置 　　
	 * com.android.settings.SettingsSafetyLegalActivity 安全信息
	 * com.android.settings.SoundSettings 声音设置 　
	 * com.android.settings.TestingSettings 测试——显示手机信息、电池信息、使用情况统计、Wifi information、服务信息 　　
	 * com.android.settings.TetherSettings 绑定与便携式热点 　　
	 * com.android.settings.TextToSpeechSettings 文字转语音设置 　
	 * com.android.settings.UsageStats 使用情况统计 　　
	 * com.android.settings.UserDictionarySettings 用户词典
	 * com.android.settings.VoiceInputOutputSettings 语音输入与输出设置 　　
	 * com.android.settings.WirelessSettings 无线和网络设置
	 */
	public static void startSetting(Context context) {
		Intent intent = new Intent();
		intent.setAction(android.provider.Settings.ACTION_SETTINGS);
		context.startActivity(intent);
	}

}
