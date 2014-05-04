package android.box.systools.control;

import android.app.Activity;
import android.box.systools.DemoDialog;
import android.box.systools.R;
import android.box.systools.layout.ScreenInfoLayout;
import android.box.systools.layout.UsbConfigLayout;
import android.box.systools.layout.VoiceSettingLayout;
import android.box.systools.layout.WiFiADBLayout;
import android.hmm.lib.utils.ScreenHelper;
import android.hmm.lib.utils.SysConfigUtils;
import android.view.MenuItem;
import android.view.View;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-13
 * Description: 
 */
public class IntentControl {

	public static void start2DemoDialog(Activity activity, MenuItem item) {
		start2DemoDialog(activity, item.getTitle().toString(), item.getItemId());
	}
	
	public static void start2DemoDialog(Activity activity, String title, int id) {
		View view = null;
		switch (id) {
		case R.id.action_voice:
			view = new VoiceSettingLayout(activity);
			break;
		case R.id.action_bluetooth:
			SysConfigUtils.configSwitch(activity, 4);
			return;
		case R.id.action_bright:
			SysConfigUtils.configSwitch(activity, 1);
			return;
		case R.id.action_gprs:
			SysConfigUtils.setGPRS(activity);
			return;
		case R.id.action_screen_info:
			view = new ScreenInfoLayout(activity);
			break;
		case R.id.action_screen_rotate:
			ScreenHelper.revolveScreen(activity);
			return;
		case R.id.action_sync:
			SysConfigUtils.configSwitch(activity, 2);
			return;
		case R.id.action_usb:
			view = new UsbConfigLayout(activity);
			break;
		case R.id.action_adb_wifi:
			view = new WiFiADBLayout(activity);
			((WiFiADBLayout) view).startCmd();
			break;
		default:
			break;
		}

		start2DemoDialog(activity, title, view);
	}

	public static void start2DemoDialog(Activity activity, String title, View view) {
		DemoDialog dialog = new DemoDialog(activity);
		dialog.showDialog(title, view);
	}

}
