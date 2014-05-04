package android.box.tools;


import android.box.tools.menu.MenuControl;
import android.hmm.lib.ConfigActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;

public class Launcher extends ConfigActivity {

	private MenuControl mMenuControl = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);

		mMenuControl = new MenuControl(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mMenuControl.showMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
