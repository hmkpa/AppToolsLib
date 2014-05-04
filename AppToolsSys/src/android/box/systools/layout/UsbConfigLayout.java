package android.box.systools.layout;

import android.box.systools.R;
import android.content.Context;
import android.hmm.lib.utils.UsbHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-12
 * Description:  
 */
public class UsbConfigLayout extends LinearLayout {

	public UsbConfigLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initViews();
	}

	private void initViews() {
		setGravity(Gravity.CENTER);
		CheckBox cb = new CheckBox(getContext());
		addView(cb);
		cb.setText(R.string.action_usb);
		updateCheckBox(cb);

		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UsbHelper.setUsbMode(getContext(), !UsbHelper.isWorking(getContext()));
				updateCheckBox((CheckBox) v);
			}
		});
//		cb.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				UsbHelper.setUsbMode(getContext(), isChecked);
//			}
//		});
	}

	private void updateCheckBox(CheckBox cb) {
		if (!UsbHelper.isSurportUsbFunc()) {
			cb.setEnabled(false);
			return;
		}
		boolean isChecked = false;
		if (UsbHelper.isWorking(getContext())) {
			isChecked = true;
		}
		cb.setChecked(isChecked);
	}

}
