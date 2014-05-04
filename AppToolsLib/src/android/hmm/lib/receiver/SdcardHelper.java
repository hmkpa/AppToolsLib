package android.hmm.lib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hmm.lib.utils.LogHelper;
import android.os.Environment;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-14
 * Description:  
 */
public class SdcardHelper {

	// Here is an example of typical code to monitor the state of external storage:
	public BroadcastReceiver mExternalStorageReceiver;
	public boolean mExternalStorageAvailable = false;
	public boolean mExternalStorageWriteable = false;

	public void updateExternalStorageState() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
//	    handleExternalStorageState(mExternalStorageAvailable,mExternalStorageWriteable);
	}

	public void startWatchingExternalStorage(Context context) {
		if (null != mExternalStorageReceiver) return;
		mExternalStorageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				new LogHelper(SdcardHelper.class.getName()).i("Storage: " + intent.getData());
				updateExternalStorageState();
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		context.registerReceiver(mExternalStorageReceiver, filter);
		updateExternalStorageState();
	}

	public void stopWatchingExternalStorage(Context context) {
		if (null == mExternalStorageReceiver) return;
		context.unregisterReceiver(mExternalStorageReceiver);
	}

	public static boolean isSdcardExist() {
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取sd卡路径
	 * @return
	 */
	public static String getSdcardAbsPath() {
		String sdDir = null;
		String state = Environment.getExternalStorageState();
		LogHelper.println(state);
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			sdDir = Environment.getExternalStorageDirectory() + "";
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		} else if (Environment.MEDIA_REMOVED.equals(state)) {
		} else {
		}
		return sdDir;
	}

}
