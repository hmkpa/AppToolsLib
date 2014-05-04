package android.hmm.lib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hmm.lib.tclass.Foo;
import android.hmm.lib.utils.LogHelper;
import android.os.BatteryManager;
import android.os.Handler;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-9
 * Description:  
 */
public class BatteryReceiverControl {
	private Context context;
	private Handler handler;
	private int event_id;
	public final static String flag_barttery = "barttery";
	private BatteryBroadcastReceiver batteryReceiver;

	public BatteryReceiverControl(Context context, int event_id, Handler handler) {
		this.context = context;
		this.handler = handler;
		this.event_id = event_id;
	}

	public void registerReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		if (null == batteryReceiver) {
			batteryReceiver = new BatteryBroadcastReceiver();
			context.registerReceiver(batteryReceiver, filter);
		}
	}

	public void unregisterReceiver() {
		if (null != batteryReceiver) {
			context.unregisterReceiver(batteryReceiver);
			batteryReceiver = null;
		}
	}

	/**********************************************************************
	 * 电池电量
	 * <uses-permission android:name="android.permission.BATTERY_STATS" /> 
	 * ACTION_BATTERY_CHANGED
	 * 
	 ***********************************************************************/
	public class BatteryBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (!Intent.ACTION_BATTERY_CHANGED.equals(action)) { return; }

			String message1 = "当前电量=";
			int level = intent.getIntExtra("level", 0);// level加%就是当前电量了
//			int scale = intent.getIntExtra("scale", 100);
//			int curPower = (level * 100 / scale) / 25;
			LogHelper.println(message1 + level);

			String message2 = "\n当前电压为：";
			String message3 = "\n当前温度为：";
			int BatteryV = intent.getIntExtra("voltage", 0);
			int BatteryT = intent.getIntExtra("temperature", 0);
			LogHelper.println(message2 + BatteryV);
			LogHelper.println(message3 + BatteryT);

			String BatteryStatus1 = "";
			String BatteryStatus2 = "";
			String BatteryStatus3 = "";

			switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
			case BatteryManager.BATTERY_STATUS_CHARGING:
				BatteryStatus1 = "充电状态";
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				BatteryStatus1 = "放电状态";
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				BatteryStatus1 = "未充电";
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				BatteryStatus1 = "充满电";
				break;
			case BatteryManager.BATTERY_STATUS_UNKNOWN:

			default:
				BatteryStatus1 = "未知道状态";
				break;
			}

			switch (intent.getIntExtra("plugged", BatteryManager.BATTERY_PLUGGED_AC)) {
			case BatteryManager.BATTERY_PLUGGED_AC:
				BatteryStatus2 = "AC充电";
				break;
			case BatteryManager.BATTERY_PLUGGED_USB:
				BatteryStatus2 = "USB充电";
				break;
			}

			switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
			case BatteryManager.BATTERY_HEALTH_UNKNOWN:

			case BatteryManager.BATTERY_HEALTH_GOOD:
				BatteryStatus3 = "状态良好";
				break;
			case BatteryManager.BATTERY_HEALTH_DEAD:
				BatteryStatus3 = "电池没有电";
				break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				BatteryStatus3 = "电池电压过高";
				break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				BatteryStatus3 = "电池过热";
				break;
			default:
				BatteryStatus3 = "未知错误";
				break;
			}

			String message = message1 + level;
			message += message2 + BatteryV;
			message += message3 + BatteryT;
			message += "\n" + BatteryStatus1;
			message += "\n" + BatteryStatus2;
			message += "\n" + BatteryStatus3;

			LogHelper.println("\n" + BatteryStatus1);
			LogHelper.println("\n" + BatteryStatus2);
			LogHelper.println("\n" + BatteryStatus3);
			Foo.sendMessage(event_id, message, handler);
		}
	};

}
