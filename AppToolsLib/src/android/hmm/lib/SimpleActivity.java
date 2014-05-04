package android.hmm.lib;

import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author:heming
 * @since :JDK 17
 * @version：1.0 Create at:2013-7-28 
 * Description:調出菜單
 */
public class SimpleActivity extends ConfigActivity {

//	protected MenuDialog menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) { return true; }
		return super.onKeyDown(keyCode, event);
	}

}
