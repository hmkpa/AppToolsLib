package android.box.tools;

import android.hmm.lib.ConfigActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-24
 * Description: 临时测试
 */
public class TestActivity extends ConfigActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		ScrollView mScrollView = new ScrollView(getApplicationContext());
		setContentView(mScrollView);
	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {

	}

}
