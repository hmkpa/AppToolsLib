package android.hmm.lib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hmm.lib.utils.LogHelper;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-13
 * Description:  
 */
public class BaseBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogHelper.log(context, "------>BootReceiver=" + action);
	}

}
