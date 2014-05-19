package android.hmm.lib;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hmm.lib.model.CPUInfo;
import android.hmm.lib.receiver.BatteryReceiverControl;
import android.hmm.lib.tclass.Foo;
import android.hmm.lib.utils.CPUHelper;
import android.hmm.lib.utils.LocationHelper;
import android.hmm.lib.utils.MobileHelper;
import android.hmm.lib.utils.SIMHelper;
import android.hmm.lib.utils.TimerHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-9
 * Description:  
 */
public class AboutSysActivity extends ConfigActivity {

	private List<AboutSysItem> list;
	private TAdapter mTAdapter;
	private ProgressBar progressBar;

	private int EVENT_LOAD_START = 1;
	private int EVENT_LOAD_END = EVENT_LOAD_START + 1;
	private int EVENT_BATTERY_CHANGE = EVENT_LOAD_START + 2;
	private int EVENT_OPENTIME_CHANGE = EVENT_LOAD_START + 3;
	private int EVENT_LOCATION_CHANGE = EVENT_LOAD_START + 4;

	private BatteryReceiverControl mBatteryReceiverControl;
	private android.widget.ListView listView;

	private boolean bRecordRunTime = false;
	private boolean isLoadingData = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViews();
		initDatas();
	}

	private void initDatas() {
		mBatteryReceiverControl = new BatteryReceiverControl(this, EVENT_BATTERY_CHANGE, handler);
		mBatteryReceiverControl.registerReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();
		bRecordRunTime = true;
		handler.sendEmptyMessage(EVENT_LOAD_START);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBatteryReceiverControl.unregisterReceiver();
		bRecordRunTime = false;
		mTAdapter = null;
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == EVENT_LOAD_START) {
				setProgressBarVisible(true);
				loadData();
			} else if (msg.what == EVENT_LOAD_END) {// ? extends Action<ClientInterface>
				refreshViews((List<AboutSysActivity.AboutSysItem>) msg.obj);
				handler.sendEmptyMessage(EVENT_BATTERY_CHANGE);
				handler.sendEmptyMessageDelayed(EVENT_OPENTIME_CHANGE, 1000);
			} else if (msg.what == EVENT_BATTERY_CHANGE) {
				if (isLoadingData) {
					Message tmsg = handler.obtainMessage();
					tmsg.what = msg.what;
					tmsg.obj = msg.obj;
					handler.sendMessageDelayed(tmsg, 1000);
					return true;
				}
				updateView(R.string.aboutsys_battery, msg.obj);
			} else if (msg.what == EVENT_OPENTIME_CHANGE) {
				if (!bRecordRunTime) { return true; }
				if (isLoadingData) {
					handler.sendEmptyMessageDelayed(EVENT_OPENTIME_CHANGE, 1000);
					return true;
				}
				updateView(R.string.aboutsys_opentime, TimerHelper.getSysRunTimeFormat());
				updateView(R.string.aboutsys_runtime, MobileHelper.getRunTime(getApplicationContext(), getPackageName()) + "");
				updateView(R.string.aboutsys_runcount, MobileHelper.getRunCount(getApplicationContext(), getPackageName()) + "");
				handler.sendEmptyMessageDelayed(EVENT_OPENTIME_CHANGE, 1000);
			} else if (msg.what == EVENT_LOCATION_CHANGE) {
				if (!bRecordRunTime) { return true; }
				if (isLoadingData) {
					Message tmsg = handler.obtainMessage();
					tmsg.what = msg.what;
					tmsg.obj = msg.obj;
					handler.sendMessageDelayed(tmsg, 1000);
					return true;
				}
				updateView(R.string.aboutsys_location, msg.obj);
			}
			return false;
		}
	});
	
	private void updateView(int id, Object obj) {
		String itemName = getResources().getString(id);
		int position = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTitle().equals(itemName)) {
				position = i;
				break;
			}
		}
		updateView(position, id, obj);
	}

	private void updateView(int position, int id, Object obj) {
		if (null == obj) { return; }
		if (null != list && list.size() > position) {
			list.remove(position);
			list.add(position, initItem(id, obj.toString()));
			mTAdapter.updateView(position, listView);
		}
	}

	private void initViews() {
		RelativeLayout parent = new RelativeLayout(getApplicationContext());
		parent.setGravity(Gravity.CENTER);
		listView = new android.widget.ListView(this);
		mTAdapter = new TAdapter(getApplicationContext(), null);
		listView.setAdapter(mTAdapter);
		parent.addView(listView);

		progressBar = new ProgressBar(getApplicationContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		progressBar.setScrollBarStyle(style)
//		params.addRule(Gravity.CENTER);
		parent.addView(progressBar, params);
		setContentView(parent);
	}

	private void setProgressBarVisible(boolean b) {
		int v = View.VISIBLE;
		if (!b) {
			v = View.GONE;
		}
		progressBar.setVisibility(v);
	}

	private void refreshViews(List<AboutSysItem> list) {
		mTAdapter.refreshData(list);
		setProgressBarVisible(false);
	}

	private void loadData() {
		new Thread() {
			@Override
			public void run() {
				isLoadingData = true;
				if (null == list) {
					list = new ArrayList<AboutSysActivity.AboutSysItem>();
				}
				list.clear();
				list.add(initItem(R.string.aboutsys_opentime, TimerHelper.getSysRunTimeFormat()));
				list.add(initItem(R.string.aboutsys_runtime, MobileHelper.getRunCount(getApplicationContext(), getPackageName()) + ""));
				list.add(initItem(R.string.aboutsys_runcount, MobileHelper.getRunCount(getApplicationContext(), getPackageName()) + ""));
				list.add(initItem(R.string.aboutsys_battery, "-"));
				list.add(initItem(R.string.aboutsys_location, "-"));

				String text = "-";
				if (null != LocationHelper.getLocation(AboutSysActivity.this)) {
					text = (LocationHelper.getLocation(AboutSysActivity.this))[0] + "-" 
						+ (LocationHelper.getLocation(AboutSysActivity.this))[1];
				}
				list.add(initItem(R.string.aboutsys_location, text));

				list.add(initItem(R.string.aboutsys_other, MobileHelper.getOther(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_modle, MobileHelper.getModel()));
				list.add(initItem(R.string.aboutsys_brand, MobileHelper.getBrand()));
				list.add(initItem(R.string.aboutsys_device, MobileHelper.getDevice()));
				list.add(initItem(R.string.aboutsys_serial, MobileHelper.getSERIAL()));
				list.add(initItem(R.string.aboutsys_sdk, String.valueOf(MobileHelper.getSDKVersion())));
				list.add(initItem(R.string.aboutsys_release, MobileHelper.getKernelVersion()));
				list.add(initItem(R.string.aboutsys_cpu, parseCpuInfo(CPUHelper.getCPUInfo())));
				
				list.add(initItem(R.string.aboutsys_sim_deviceid, 
						SIMHelper.getDeviceId(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_devicesoftwareversion,
						SIMHelper.getDeviceSoftwareVersion(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_hasicccard, SIMHelper.hasIccCard(getApplicationContext())?
							getString(android.R.string.yes):getString(android.R.string.no)));
				
				list.add(initItem(R.string.aboutsys_sim_line1number, 
						SIMHelper.getLine1Number(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_networkcountryiso,
						SIMHelper.getNetworkCountryIso(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_networkoperator, 
						SIMHelper.getNetworkOperator(getApplicationContext())));
				
				list.add(initItem(R.string.aboutsys_sim_networkoperatorname,
						SIMHelper.getNetworkOperatorName(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_networkroaming,
						SIMHelper.isNetworkRoaming(getApplicationContext())?
								getString(android.R.string.yes):getString(android.R.string.no)));
				list.add(initItem(R.string.aboutsys_sim_networktype, 
						SIMHelper.getNetworkType(getApplicationContext())+""));
				
				list.add(initItem(R.string.aboutsys_sim_phonetype, 
						SIMHelper.getPhoneType(getApplicationContext())+""));
				list.add(initItem(R.string.aboutsys_sim_simcountryiso,
						SIMHelper.getSimCountryIso(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_simoperator, 
						SIMHelper.getSimOperator(getApplicationContext())));
				
				list.add(initItem(R.string.aboutsys_sim_simoperatorname, 
						SIMHelper.getSimOperatorName(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_simserialnumber,
						SIMHelper.getSimSerialNumber(getApplicationContext())));
				String simState = "";
				switch (SIMHelper.getSimState(getApplicationContext())) {
				case TelephonyManager.SIM_STATE_UNKNOWN:
					simState = "未知状态";
					break;
				case TelephonyManager.SIM_STATE_ABSENT:
					simState = "没插卡";
					break;
				case TelephonyManager.SIM_STATE_PIN_REQUIRED:
					simState = "锁定状态，需要用户的PIN码解锁";
					break;
				case TelephonyManager.SIM_STATE_PUK_REQUIRED:
					simState = "锁定状态，需要用户的PUK码解锁";
					break;
				case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
					simState = "锁定状态，需要网络的PIN码解锁";
					break;
				case TelephonyManager.SIM_STATE_READY:
					simState = "就绪状态";
					break;
				default:
					break;
				}
				list.add(initItem(R.string.aboutsys_sim_simstate,  simState));
				
				list.add(initItem(R.string.aboutsys_sim_subscriberid, 
						SIMHelper.getSubscriberId(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_voicemailalphatag,
						SIMHelper.getVoiceMailAlphaTag(getApplicationContext())));
				list.add(initItem(R.string.aboutsys_sim_voicemailnumber, 
						SIMHelper.getVoiceMailNumber(getApplicationContext())));

				Foo.sendMessage(EVENT_LOAD_END, list, handler);
				isLoadingData = false;
				super.run();
			}
		}.start();
	}

	private String parseCpuInfo(CPUInfo info) {
		String cpuStr = "name=" + info.getName();
		cpuStr += "\nHardware=" + info.getHardware();
		cpuStr += "\nCores=" + info.getNumCores();
		cpuStr += "\nSerial=" + info.getSerial();
		cpuStr += "\nmaxFreq=" + info.getMaxCpuFreq();
		cpuStr += "\nminFreq=" + info.getMinCpuFreq();
		cpuStr += "\ncurFreq" + info.getCurCpuFreq();

		if (null != info.getListProcessor()) {
			for (int i = 0; i < info.getListProcessor().size(); i++) {
				cpuStr += "\n Processor" + i + ":" + info.getListProcessor().get(i);
			}
		}
		return cpuStr;
	}

	private AboutSysActivity.AboutSysItem initItem(int id, String info) {
		String title = getResources().getString(id);
		AboutSysActivity.AboutSysItem itemt = new AboutSysItem();
		itemt.setTitle(title);
		itemt.setInfo(info);
		return itemt;
	}

	private class TAdapter extends BaseAdapter {
		private Context context;
		private List<AboutSysItem> list;

		public TAdapter(Context context, List<AboutSysItem> list) {
			this.context = context;
			this.list = list;
		}

		public void refreshData(List<AboutSysItem> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void updateView(int itemIndex, ViewGroup arg2) {
			if (null == arg2) return;
			int visiblePosition = ((android.widget.ListView) arg2).getFirstVisiblePosition();
			int endPosition = ((android.widget.ListView) arg2).getLastVisiblePosition();
			// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
			if (itemIndex - visiblePosition >= 0 && itemIndex <= endPosition) {
				// 得到要更新的item的view
				View view = arg2.getChildAt(itemIndex - visiblePosition);
//				//从view中取得holder
//				ViewHolder holder = (ViewHolder) view.getTag();
//				HashMap<String, Object> item = data.get(itemIndex);
				getView(itemIndex, view, arg2);
			}
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return createViews(arg0, arg1);
		}

		private View createViews(int position, View view) {
			ViewHolder holder = null;
			if (null == view) {
				holder = new ViewHolder();
				LinearLayout linear = new LinearLayout(context);
				linear.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.weight = 1;
				holder.tv_title = createTextView();
				holder.tv_info = createTextView();
				linear.addView(holder.tv_title, params);
				linear.addView(holder.tv_info, params);

				view = linear;
				linear.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (null != list && list.size() > position && null != list.get(position)) {
				holder.tv_title.setText(list.get(position).getTitle());
				holder.tv_info.setText(list.get(position).getInfo());
			}
			return view;
		}

		private TextView createTextView() {
			TextView tv = new TextView(context);
//			tv.setText(text);
			tv.setGravity(Gravity.CENTER);
			return tv;
		}

		private class ViewHolder {
			private TextView tv_title;
			private TextView tv_info;
		}
	}

	private class AboutSysItem {
		private String mTitle;
		private String mInfo;

		public void setTitle(String title) {
			this.mTitle = title;
		}

		public void setInfo(String message) {
			this.mInfo = message;
		}

		public String getTitle() {
			return mTitle;
		}

		public String getInfo() {
			return mInfo;
		}
	}
}
