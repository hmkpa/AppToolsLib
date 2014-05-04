package android.box.systools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * @author HEMING
 * Time:2014-3-24
 */
public class DemoDialog extends Dialog implements android.view.View.OnClickListener {

	private View layout = null;
	protected Activity activityReference;

	public DemoDialog(Context context) {
		super(context);
		activityReference = (Activity) context;
	}

	public void showDialog(String title, View view) {
		this.layout = view;
		setContentView(layout);
		setTitle(title);
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void onClick(View arg0) {
		dismiss();
	}

}
