package android.hmm.lib;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.hmm.lib.utils.LogHelper;
import android.hmm.lib.utils.SharedPreferencesHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-21
 * Description:  
 */
public class AppConfigControl {
	private LogHelper log;
	private android.content.Context context;

	public AppConfigControl(android.content.Context context) {
		this.context = context;
		log = new LogHelper(getClass().toString());
	}

	public void initScreenData() {
		setStyle(android.R.style.Theme_NoTitleBar_Fullscreen);
		// 读取配置文件中上次保存的屏幕状态(最好保存在sd上)
		getScreenStatus();
	}

	private void setStyle(int id) {
//		int id = context.getResources().get
		((android.app.Application) context).setTheme(id);
	}

	/**
	 * 截面布局不受字体大小的影响
	 */
	public void keepFontSize() {
		Resources res = context.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		log.i("keep keepFontSize!");
	}

	public void clearConfiguration(Configuration newConfig) {
		if (newConfig.fontScale > 1.0f) {
			Resources resource = context.getResources();
			Configuration c = resource.getConfiguration();
			c.fontScale = 1.0f;
			resource.updateConfiguration(c, resource.getDisplayMetrics());
			log.i("keep fontScale do not changed!");

//			 config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
		}
	}

	public void setScreenStatus(boolean status) {
		SharedPreferencesHelper.save(context, Config.FileName_AppConfig, Config.Key_ScreenStatus, status);
		log.i("status:" + status);
	}

	public boolean getScreenStatus() {
		return SharedPreferencesHelper.getValueBoolean(context, Config.FileName_AppConfig, Config.Key_ScreenStatus);
	}

	// 控制整个界面字体变化
	public static void changeFonts(ViewGroup root, Activity act) {
		Typeface tf = Typeface.createFromAsset(act.getAssets(), "fonts/stxingka.ttf");
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, act);
			}
		}
	}
}
