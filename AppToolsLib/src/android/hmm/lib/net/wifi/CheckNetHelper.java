package android.hmm.lib.net.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-18
 * Description:  
 */
public class CheckNetHelper {

	/**
	 * 检测网络状态
	 * @param context
	 * permission ACCESS_NETWORK_STATE
	 * @return
	 */
	public static boolean checkWiFiConn(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) { return false; }
			NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) { return true; }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 检测网络状态
	 * permission ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static boolean checkNetwork(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) { return false; }
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) { return true; }
		} catch (Exception e) {
		}
		return false;
	}

}
