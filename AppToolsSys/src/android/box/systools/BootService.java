package android.box.systools;

import android.box.systools.control.ActionName;
import android.hmm.lib.intent.IntentUtils;
import android.hmm.lib.service.BaseBootService;
import android.os.Message;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-17
 * Description:  
 */
public class BootService extends BaseBootService {

	private static final int EVENT_OPEN_USB = EVENT + 100;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler.sendEmptyMessageDelayed(EVENT_OPEN_USB, 2 * 1000);
	}

	@Override
	protected void handlerMessageReceive(Message msg) {
		super.handlerMessageReceive(msg);
		switch (msg.what) {
		case EVENT_OPEN_USB:
			IntentUtils.sendBroadcast(getApplicationContext(), ActionName.action_lib_usb);
			break;
		default:
			break;
		}
	}

}
