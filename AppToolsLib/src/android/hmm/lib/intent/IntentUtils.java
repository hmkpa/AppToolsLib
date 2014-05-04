package android.hmm.lib.intent;

import android.content.Context;
import android.content.Intent;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-13
 * Description:  内部功能模块间跳转管理类
 */
public class IntentUtils {

	/*************************************************************************
	 * 开机时启动的服务
	 * @param context
	 * @param cls
	 *************************************************************************/
	public static void start2BootService(Context context, Class<?> cls) {
		Intent service = new Intent();
		service.setClass(context, cls);
		context.startService(service);
	}

	public static void sendBroadcast(Context context, String action) {
		Intent intent = new Intent(action);
		context.sendBroadcast(intent);
	}

}
