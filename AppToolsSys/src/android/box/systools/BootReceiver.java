package android.box.systools;

import android.content.Context;
import android.content.Intent;
import android.hmm.lib.intent.IntentUtils;
import android.hmm.lib.receiver.BaseBootReceiver;
import android.hmm.lib.utils.LogHelper;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-17
 * Description:  
 */
public class BootReceiver extends BaseBootReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		
		String action = intent.getAction();
		LogHelper.log(context, "------>BootReceiver=" + action);
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			IntentUtils.start2BootService(context, BootService.class);
		}
	}
	
}
