package android.hmm.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-1-14
 * Description:  
 */
public class HomeKeyReceiver extends BroadcastReceiver {
	static final String SYSTEM_REASON = "reason";
	static final String SYSTEM_HOME_KEY = "homekey";// home key
	static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra(SYSTEM_REASON);
			if (reason != null) {
				if (reason.equals(SYSTEM_HOME_KEY)) {
					// home key处理点
				} else if (reason.equals(SYSTEM_RECENT_APPS)) {
					// long home key处理点
				}
			}
		}
	}
}