package android.box.systools.layout;

import android.content.Context;
import android.hmm.lib.net.wifi.CheckNetHelper;
import android.hmm.lib.net.wifi.IPHelper;
import android.hmm.lib.tclass.Foo;
import android.hmm.lib.utils.CMDHelper;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-18
 * Description:  
 */
public class WiFiADBLayout extends LinearLayout implements android.view.View.OnClickListener {

	private int ENVENT_SHOW_RESULT = 1;
	private TextView tv;

	public WiFiADBLayout(Context context) {
		super(context);
		
		initViews();
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == ENVENT_SHOW_RESULT) {
				refreshMessage(msg.obj);
			}
			return false;
		}
	});

	@Override
	public void onClick(View v) {
		
	}

	private void initViews() {
		setOrientation(LinearLayout.VERTICAL);
		tv = new TextView(getContext());
		addView(tv);
		ProgressBar bar = new ProgressBar(getContext());
		addView(bar);
	}

	private void refreshMessage(Object obj) {
		if (null != tv) {
			if (null == obj) obj = "null";

			tv.setText((CharSequence) obj);
			setChildViewEnable(false);
		}
	}

	private void setChildViewEnable(boolean enable) {
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i) instanceof ProgressBar) {
				int vib = enable ? View.VISIBLE : View.GONE;
				getChildAt(i).setVisibility(vib);
			} else if (getChildAt(i) instanceof Button) {
//				parent.getChildAt(i).setVisibility(View.GONE);
				getChildAt(i).setEnabled(!enable);
			}
		}
	}

	public void startCmd() {
		setChildViewEnable(true);

		new Thread() {
			public void run() {
				String result = "";
				result = CMDHelper.execCmdWiFiADB(getContext());
//				result = "over!";
				if (CheckNetHelper.checkWiFiConn(getContext())) {
					android.hmm.lib.model.IP ip = android.hmm.lib.net.wifi.WiFiManager.getWiFiInfo(getContext());
					result += "\nWiFi 已连接";
					result += "\nWiFi name:" + ip.getConnName();
					result += "\nIP Addr=" + IPHelper.getLocalIpAddress();
					result += "\n在电脑请输入 ";
					result += "\nadb connect " + ip.getIp4();
				} else {
					result += "\nWiFi 未连接";
					result += "\n不能进行adb conn";
				}
				result += "\n";
				Foo.sendMessage(ENVENT_SHOW_RESULT, result, handler);
			};
		}.start();
	}


}
