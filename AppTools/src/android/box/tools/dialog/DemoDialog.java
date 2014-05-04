package android.box.tools.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.box.tools.demo.ListViewItemFlyIn;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author HEMING
 * Time:2014-3-24
 */
public class DemoDialog extends Dialog implements android.view.View.OnClickListener {

//	private View tagview = null;
	protected Activity activity;

	public DemoDialog(Activity context) {
		super(context);
		activity = context;
	}

	public void showDialog(View view, String title) {
		if(null != view.getParent()){
			((ViewGroup)view.getParent()).removeAllViews();
		}
		
		setContentView(view);
		setTitle(title);
		super.show();
		if(view instanceof ListViewItemFlyIn){
			((ListViewItemFlyIn)view).setLoadingViewContext(this);
			((ListViewItemFlyIn)view).setStart(true);
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
//		tagview = null;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View arg0) {
		dismiss();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
		}
		return super.onKeyDown(keyCode, event);
	}

}
