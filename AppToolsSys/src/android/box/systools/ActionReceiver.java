package android.box.systools;

import android.box.systools.control.ActionName;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hmm.lib.utils.LogHelper;
import android.hmm.lib.utils.UsbHelper;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-13
 * Description:  
 */
public class ActionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		handlerReceiver(context, intent);
	}

	private void handlerReceiver(Context context, Intent intent) {
		String action = intent.getAction();
		LogHelper.log(context, "------>ActionReceiver=" + action);

		if (null == action || action.length() == 0) { return; }

		if (ActionName.action_lib_adb_wifi.equals(action)) {

		} else if (ActionName.action_lib_adb_wifi.equals(action)) {

		} else if (ActionName.action_lib_usb.equals(action)) {
			UsbHelper.setUsbMode(context, true);// 打开usb
		}
	}

}
