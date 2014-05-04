package android.hmm.lib.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.Window;
import android.view.WindowManager;

public class BrightUtils {
	/**
	 * 
	 */
	public static final int ACTIVITY_BRIGHTNESS_AUTOMATIC = -1;
	/**
	 * 自动调节模式
	 */
	public static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC 
					= Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
	/**
	 * 手动调节模式
	 */
	public static final int SCREEN_BRIGHTNESS_MODE_MANUAL
				    = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
	/**
	 * 默认亮度
	 */
	public static final int SCREEN_BRIGHTNESS_DEFAULT = 75;
	/**
	 * 最大亮度
	 */
	public static final int MAX_BRIGHTNESS = 100;
	/**
	 * 最小亮度
	 */
	public static final int MIN_BRIGHTNESS = 0;

	public static final int MaxBrighrness = 255;
	public static final int MinBrighrness = 120;

	/**
	 * 获取当前系统亮度值
	 * @param context
	 * @return
	 */
	public static int getBrightness(Context context) {
		try {
			return Settings.System.getInt(context.getContentResolver(), 
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取当前系统亮度模式
	 * @param context
	 * @return:true自动，false：手动
	 */
	public static boolean getBrightMode(Context context) {
		try {
			return Settings.System.getInt(context.getContentResolver(), 
					Settings.System.SCREEN_BRIGHTNESS_MODE) == SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Description: 设置亮度的模式
	 * @param context
	 * @param mode调节模式
	 * @return boolean
	 * @throws
	 */
	public static boolean setMode(Context context, int mode) {
		if (mode == SCREEN_BRIGHTNESS_MODE_AUTOMATIC || mode == SCREEN_BRIGHTNESS_MODE_MANUAL) {
			return Settings.System.putInt(context.getContentResolver(), 
					Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
		}
		return false;
	}

	/**
	 * 亮度预览
	 * @param activity 
	 * brightness：亮度值（0~1）
	 */
	public static void brightnessPreview(Activity activity, float brightness) {
		Window window = activity.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = brightness;
		window.setAttributes(lp);
	}

	/**
	 * 亮度预览
	 * @param activity
	 * @param percent百分比（0.0~1.00）
	 */
	public static void brightnessPreviewFromPercent(Activity activity, float percent) {
		float brightness = percent + (1.0f - percent) * (((float) MinBrighrness) / MaxBrighrness);
		brightnessPreview(activity, brightness);
	}

	/**
	 * 设置屏幕亮度
	 * @param brightness ：亮度值,值为0至100
	 */
	public static void setBrightness(Context context, int brightness) {
		int mid = MaxBrighrness - MinBrighrness;
		int bri = (int) (MinBrighrness + mid * ((float) brightness) / MAX_BRIGHTNESS);
		ContentResolver resolver = context.getContentResolver();
		Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, bri);
	}

	/**
	 * 保存亮度
	 * @param resolver
	 * @param brightness:1-100
	 */
	public static void saveBrightnessD(Context context, int brightness) {
		float num = (float) (brightness / 255.0);
		brightnessPreviewFromPercent((Activity) context, num);
		ContentResolver resolver = context.getContentResolver();
		Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
		android.provider.Settings.System.putInt(resolver, "screen_brightness", brightness);
		// resolver.registerContentObserver(uri, true, myContentObserver);
		resolver.notifyChange(uri, null);
	}

	/**
	 * 保存亮度 小米手机测试通过 
	 * sdk：15 
	 * 无签名
	 * brightness:5-255
	 */
	public static void saveBrightnessF(Context context, int brightness) {
		float num = (float) (brightness / 255.0);
		brightnessPreview((Activity) context, num);
		ContentResolver resolver = context.getContentResolver();
		Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
		android.provider.Settings.System.putInt(resolver, "screen_brightness", brightness);
		// resolver.registerContentObserver(uri, true, myContentObserver);
		resolver.notifyChange(uri, null);
	}

}
