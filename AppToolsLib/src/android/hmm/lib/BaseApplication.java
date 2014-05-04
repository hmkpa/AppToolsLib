package android.hmm.lib;


import android.app.Application;
import android.content.res.Configuration;
import android.hmm.lib.utils.LogHelper;

/**
 * @author:heming
 * @since :JDK 17
 * @version：1.0 Create at:2013-7-27
 * Description:初始化数据，应用配置，控制字体不受影响等
 */
public class BaseApplication extends Application {

	private String TAG = "BaseApplication";
	private LogHelper log;
	private AppConfigControl mAppConfigControl;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		log = new LogHelper(TAG);
		log.v("start to create application!");
		mAppConfigControl = new AppConfigControl(getApplicationContext());
		mAppConfigControl.initScreenData();
		mAppConfigControl.keepFontSize();
	}
	
	/**
	 * 低内存时调用
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	/**
	 * 这个函数是模拟一个过程环境，在真机中永远也不会被调用
	 */
	@Override
	public void onTerminate() {
		// super.onTerminate();
		log.e("onLowMemory");
		System.gc();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		mAppConfigControl.clearConfiguration(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	public boolean getScreenStatus() {
		return mAppConfigControl.getScreenStatus();
	}

}
