package android.hmm.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.hmm.lib.tclass.Foo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-9
 * Description:  
 */
public class LocationHelper {

	/**********************************************************************
	 * 获取手机经纬度
	 *  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
	    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
	    <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION"/>
	 ***********************************************************************/
	public static void getLocation(Context context, final Handler handler, final int EVENET_ID) {

		// 1. 创建一个 LocationManager对象。
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 2. 创建一个 LocationListener对象。
		LocationListener myGPSListener = new LocationListener() {
			// 一旦Location发生改变就会调用这个方法
			public void onLocationChanged(Location location) {
				double latitude = location.getLatitude();// 经度
				double longitude = location.getLongitude();// 维度
				LogHelper.println(latitude + "/" + longitude);
				Foo.sendMessage(EVENET_ID, latitude + "/" + longitude, handler);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			public void onProviderEnabled(String provider) {

			}

			public void onProviderDisabled(String provider) {

			}
		};
		// 3.向LocationManager 注册一个LocationListener。
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 获取精准位置
		criteria.setCostAllowed(true);// 允许产生开销
//		criteria.setPowerRequirement(Criteria.POWER_HIGH);// 消耗大的话，获取的频率高
		criteria.setPowerRequirement(Criteria.ACCURACY_LOW);// 消耗大的话，获取的频率高
		criteria.setSpeedRequired(true);// 手机位置移动
		criteria.setAltitudeRequired(true);// 海拔

		String provider = locationManager.getBestProvider(criteria, true);
		// 根据Criteria的设置获取一个最佳的Provider
		// parameter: 1. provider 2. 每隔多少时间获取一次 3.每隔多少米 4.监听器触发回调函数
		locationManager.requestLocationUpdates(provider, 1 * 1000, 0, myGPSListener);
		// 4.移除LocationManager 注册的 LocationListener。
		locationManager.removeUpdates(myGPSListener);
	}

	public static double[] getLocation(Activity activity) {
		String serviceString = Context.LOCATION_SERVICE;
		LocationManager locationManager = (LocationManager) activity.getSystemService(serviceString);
		String provider = LocationManager.NETWORK_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);
		if (null == location) { return null; }
		double[] position = new double[2];
		position[0] = location.getLatitude();
		position[1] = location.getLongitude();
		return position;
	}
}
