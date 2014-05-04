package android.hmm.lib;


import android.app.Activity;
import android.hmm.lib.utils.ScreenHelper;
import android.os.Bundle;

/**
 * 基本属性的设置,控制整个的工程
 * 如：全屏
 */
public class ConfigActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setScreenStatus();
		ScreenHelper.setFullScreen(this);
	}

	public void setScreenStatus() {
		if (((BaseApplication) getApplication()).getScreenStatus()) {
			ScreenHelper.setFullScreen(this);
		} else {
			ScreenHelper.quitFullScreen(this);
		}
	}

}
