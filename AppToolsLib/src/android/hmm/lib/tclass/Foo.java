package android.hmm.lib.tclass;

import android.os.Handler;
import android.os.Message;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-9
 * Description:  
 */
public class Foo {

	public static void sendMessage(int what, Object obj, Handler handler) {
		if (null == handler) { return; }
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
//		handler.sendMessage(msg);
		msg.sendToTarget();
	}



}
