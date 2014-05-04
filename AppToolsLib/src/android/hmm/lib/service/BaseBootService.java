package android.hmm.lib.service;

import android.app.Service;
import android.content.Intent;
import android.hmm.lib.utils.LogHelper;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-13
 * Description:  
 */
public class BaseBootService extends Service {

	private LogHelper log = new LogHelper("android.hmm.lib.service.BootService");
	protected static final int EVENT = 0x3100;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		log.i("BootService onCreate");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		log.i("BootService onStart");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		log.i("BootService onDestroy");
	}

	protected Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			handlerMessageReceive(msg);
			return false;
		}
	});

	protected void handlerMessageReceive(Message msg) {

	}

}
