package android.hmm.lib.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-21
 * Description:  
 */
public class LogHelper {

	private String TAG = "TAG_NULL";
	private final int type_i = 0;
	private final int type_e = 1;
	private final int type_w = 2;
	private final int type_d = 3;
	private final int type_v = 4;

	public LogHelper(String tag) {
		if (null == tag || 0 == tag.length()) {
			e("the parem of tag == null");
			return;
		}
		this.TAG = tag;
	}

	public void i(String message) {
		log(TAG, message, type_i);
	}

	public void e(String message) {
		log(TAG, message, type_e);
	}

	public void v(String message) {
		log(TAG, message, type_v);
	}

	public void d(String message) {
		log(TAG, message, type_d);
	}

	public void w(String message) {
		log(TAG, message, type_w);
	}

	public void log(String tag, String message, int type) {
		log(tag, message, type, false);
	}

	public void log(String tag, String message, int type, boolean forceShow) {
		if (!forceShow && !DebugMode.isApkDebugable()) return;
		switch (type) {
		case type_i:
			Log.i(tag, message);
			break;
		case type_e:
			Log.e(tag, message);
			break;
		case type_w:
			Log.w(tag, message);
			break;
		case type_d:
			Log.d(tag, message);
			break;
		case type_v:
			Log.v(tag, message);
			break;
		default:
			break;
		}
	}

	/*************************************************************************************
	 * 
	 *************************************************************************************/
	public static void println(String str) {
		System.out.println(str);
	}

	public static void log(Class<?> cls, String message) {
		if (null != cls) {
			log(cls.getName(), message);
		}
	}

	public static void log(Context context, String message) {
		if (null != context) {
			log(context.getApplicationInfo().className, message);
		}
	}

	public static void log(String className, String message) {
		log(className, message, false);
	}

	public static void log(String className, String message, boolean forceShow) {
		if (DebugMode.isApkDebugable() || forceShow) {
			Log.i(className, message);
		}
	}

	/******************************************************************************************
	 * <uses-permission android:name="android.permission.READ_LOGS"/>
	 * @author heming
	 * @since :JDK ?  
	 * @version：1.0
	 * Create at:2013-8-24
	 * Description:
	 ******************************************************************************************/
	protected class CatchLogThread extends Thread {
		private boolean isTesting = true;

		@Override
		public void run() {
			Process mLogcatProc = null;
			BufferedReader reader = null;
			String line;
			while (isTesting) {
				try {
					// 获取logcat日志信息
					mLogcatProc = Runtime.getRuntime().exec(new String[] { "logcat", "ActivityManager:I *:S" });
					reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));
					while ((line = reader.readLine()) != null) {
						if (line.indexOf("android.intent.category.HOME") > 0) {
							isTesting = false;
							handler.sendMessage(handler.obtainMessage());
							Runtime.getRuntime().exec("logcat -c");// 删除日志
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			Log.i("CatchLogThread", "Home key press");
			return false;
		}
	});

}
