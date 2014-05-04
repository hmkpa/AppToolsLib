package android.hmm.lib.net.wifi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

@SuppressWarnings("deprecation")
public class WiFiManager {

	private final static String TAG = "WifiManage";
	static Context mContext;
	// 声明管理对象
	private static WifiManager wifiManager;
	// Wifi信息
	private WifiInfo wifiInfo;
	// 扫描出来的网络连接列表
	private List<ScanResult> scanResultList;
	// 网络配置列表
	private List<WifiConfiguration> wifiConfigList;
	// Wifi锁
	private WifiLock wifiLock;

	static ConnectivityManager conMan;
	NetworkInfo[] mNetworkInfoList;

	private DhcpInfo mDHCPInfo;

	@SuppressWarnings("static-access")
	public WiFiManager(Context context) {
		mContext = context;
		// 获取Wifi服务
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 得到Wifi信息
		conMan = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
		netWorkStata();
		wifiInfo = wifiManager.getConnectionInfo();
		startScan();

	}

	/**
	 * 检查当前Wifi网卡状态
	 */
	public int checkNetCardStatus() {
		if (wifiManager.getWifiState() == 0) {
			Log.i(TAG, "网卡正在关闭");
			return 0;
		} else if (wifiManager.getWifiState() == 1) {
			Log.i(TAG, "网卡已经关闭");
			return 1;
		} else if (wifiManager.getWifiState() == 2) {
			Log.i(TAG, "网卡正在打开");
			return 2;
		} else if (wifiManager.getWifiState() == 3) {
			Log.i(TAG, "网卡已经打开");
			return 3;
		} else {
			Log.i(TAG, "---_---晕......没有获取到状态---_---");
			return 4;
		}
	}

	/**
	 * 检查Wifi网卡是否可用
	 */
	public boolean getWifiStatus() {
		return wifiManager.isWifiEnabled();
	}

	/**
	 * 打开Wifi网卡
	 */
	public void openNetCard() {
		// if (!wifiManager.isWifiEnabled()) {
		wifiManager.setWifiEnabled(true);
		// }
	}

	/**
	 * 关闭Wifi网卡
	 */
	public void closeNetCard() {
		if (wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}

	public void closeNetCardForce() {
		wifiManager.setWifiEnabled(false);
	}

	// 锁定/解锁wifi

	// 其实锁定WiFI就是判断wifi是否建立成功，在这里使用的是held，握手的意思acquire 得到！
	public void lockWifi() {
		wifiLock.acquire();
	}

	public void unLockWifi() {
		if (!wifiLock.isHeld()) {
			wifiLock.release();
			// 释放资源
		}
	}

	// 我本来是写在构造函数中了，但是考虑到不是每次都会使用Wifi锁
	// 所以干脆自己建立一个方法！需要时调用，建立就OK
	public void createWifiLock() {
		wifiLock = wifiManager.createWifiLock("flyfly");
		// 创建一个锁的标志
	}

	/**
	 * 扫描周边网络
	 */
	public void startScan() {
		wifiManager.startScan();
		wifiInfo = wifiManager.getConnectionInfo();
		// 扫描返回结果列表
		scanResultList = wifiManager.getScanResults();
		// 扫描配置列表
		wifiConfigList = wifiManager.getConfiguredNetworks();
		mDHCPInfo = wifiManager.getDhcpInfo();
	}

	/**
	 * 得到扫描列表结果
	 */
	public List<ScanResult> getWifiList() {
		return scanResultList;
	}

	/**
	 * 得到配置列表结果
	 */
	public List<WifiConfiguration> getWifiConfigList() {
		return wifiConfigList;
	}

	/**
	 * 判断网络是否可用
	 */
	public static final boolean networkIsAvailable(final Context context) {
		NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		return (null != info && info.isAvailable());
	}

	/*
	 * 获取DHCP信息
	 */
	public DhcpInfo getDHCPInfo() {
		return mDHCPInfo;
	}

	// 获取指定信号的强度/所发现的WIFI网络信号强度
	public int getLevel(int NetId) {
		return scanResultList.get(NetId).level;
	}

	// 获取指定信号的强度/获取802.11n 收集的旌旗灯号
	public int getRssi() {
		return (Integer) ((wifiInfo == null) ? null : wifiInfo.getRssi());
	}

	// 获取本机Mac地址
	public String getMac() {
		return (wifiInfo == null) ? "" : wifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (wifiInfo == null) ? null : wifiInfo.getBSSID();
	}

	public String getSSID() {
		String ssid = null;
		if (wifiInfo != null) {
			ssid = wifiInfo.getSSID();
			if (ssid != null && ssid.length() > 1 && ssid.startsWith("\"") && ssid.endsWith("\"")) {// 有双引号去掉双引号
				ssid = ssid.substring(1, ssid.length() - 1);
			}
		}
		return ssid;
	}

	/**
	 * 得到连接的ID
	 */
	public int getCurrentNetId() {
		return (wifiInfo == null) ? null : wifiInfo.getNetworkId();
	}

	// 返回所有信息
	public String getStringWifiInfo() {
		return (wifiInfo == null) ? null : wifiInfo.toString();
	}

	/*
	 * 获取连接信息
	 */
	public void getWifiInfo() {
		wifiInfo = wifiManager.getConnectionInfo();
	}

	// 获取IP地址
	public int getIP() {
		return (wifiInfo == null) ? null : wifiInfo.getIpAddress();
	}

	// 获取IP地址
	public String StringizeIp() {
		int ip = getIP();
		int ip4 = (ip >> 24) & 0x000000FF;
		int ip3 = (ip >> 16) & 0x000000FF;
		int ip2 = (ip >> 8) & 0x000000FF;
		int ip1 = ip & 0x000000FF;
		return Integer.toString(ip1) + "." + ip2 + "." + ip3 + "." + ip4;
	}

	// 返回当前已连接的网络名
	public String getConnectedWifiName() {
		String text = "";
		getWifiInfo();
		if (null != wifiInfo) {
			text = wifiInfo.getSSID();
		}
		return text;
	}

	// 判断当前网络是否已经连接
	@SuppressWarnings("static-access")
	public boolean isConnect() {
		conMan = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
		boolean wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		if (wifi) {
			Log.v(TAG, "网络已连接");
		} else {
			Log.v(TAG, "网络未连接");
		}
		return wifi;
	}

	// 判断当前网络是否已经连接
	@SuppressWarnings("static-access")
	public boolean isWifiConnect() {
		conMan = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
		startScan();
		boolean wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

		String wifiName = getSSID();
		if (wifi && wifiName != null && !wifiName.isEmpty() && getWifiStatus()) {
			return true;
		} else {
			return false;
		}
	}

	// 不显示SSID
	public void hiddenSSID(int NetId) {
		wifiConfigList.get(NetId).hiddenSSID = true;
	}

	// 显示SSID
	public void displaySSID(int NetId) {
		wifiConfigList.get(NetId).hiddenSSID = false;
	}

	public String checkNetworkInfo() {
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		return wifi.toString();
	}

	public void netWorkStata() {
		List<NetworkInfo> mm = new ArrayList<NetworkInfo>();
		mNetworkInfoList = conMan.getAllNetworkInfo();
		for (int i = 0; i < mNetworkInfoList.length; i++) {
			if (mNetworkInfoList[i].getTypeName().equals("WIFI")) mm.add(mNetworkInfoList[i]);
		}
	}

	// 添加一个网络
	public boolean addNetWordLink(WifiConfiguration config) {
		int NetId = wifiManager.addNetwork(config);
		return wifiManager.enableNetwork(NetId, true);
	}

	// 添加一个网络
	public int addNetwork(WifiConfiguration wcg) {
		int wcgID = wifiManager.addNetwork(wcg);
		wifiManager.enableNetwork(wcgID, true);
		wifiManager.saveConfiguration();
		return wcgID;
	}

	// 禁用一个网络
	public boolean disableNetWordLick(int NetId) {
		wifiManager.disableNetwork(NetId);
		return wifiManager.disconnect();
	}

	/**
	 * 断开当前连接的网络
	 */
	public void disconnectWifi() {
		int netId = getCurrentNetId();
		wifiManager.disableNetwork(netId);
		wifiManager.disconnect();
		wifiManager.saveConfiguration();
		wifiInfo = null;
	}

	// 移除一个网络
	public boolean removeNetworkLink(int NetId) {
		return wifiManager.removeNetwork(NetId);
	}

	// 移除一个网络
	public boolean removeNetworkLink(WifiConfiguration config) {
		int NetId = config.networkId;
		Log.i(TAG, "移除一个网络，这个网络的ID" + NetId);
		wifiManager.disableNetwork(NetId);
		boolean status = wifiManager.removeNetwork(NetId);
		wifiManager.saveConfiguration();
		return status;
	}

	public boolean removeNetworkLink(String name) {
		startScan();
		if (null == name || name.isEmpty()) {
			return true;
		} else if (null == wifiConfigList || wifiConfigList.isEmpty()) {
			return false;
		} else {
			name = "\"" + name + "\"";
			for (int val = 0; val < wifiConfigList.size(); val++) {
				WifiConfiguration config = wifiConfigList.get(val);
				if (config.SSID.equals(name)) {
					int NetId = config.networkId;
					Log.i(TAG, "移除一个网络 -->" + name);
					wifiManager.disableNetwork(NetId);
					boolean status = wifiManager.removeNetwork(NetId);
					wifiManager.saveConfiguration();
					return status;
				}
			}
			return false;
		}
	}
	
	public static android.hmm.lib.model.IP getWiFiInfo(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled() && CheckNetHelper.checkNetwork(context)) {
			DhcpInfo d = wifiManager.getDhcpInfo();
			android.hmm.lib.model.IP ip = new android.hmm.lib.model.IP();
			ip.setConnName(wifiManager.getConnectionInfo().getSSID());
			ip.setDns1(IPHelper.StringizeIp(d.dns1));
			ip.setDns2(IPHelper.StringizeIp(d.dns2));
			ip.setGateway( IPHelper.StringizeIp(d.gateway));
			ip.setMac(wifiManager.getConnectionInfo().getMacAddress());
			ip.setNetmask(IPHelper.StringizeIp(d.netmask));
			ip.setIp4(IPHelper.StringizeIp(d.ipAddress));
			
//			String s_serverAddress = "Server IP: " + WiFiProvider.StringizeIp(d.serverAddress);
//			String s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
			
			return ip;
		} 
		return null;
	}

	// 保存配置
	@SuppressWarnings("unused")
	public void saveConfiguration(WifiConfiguration wcg) {
		int NetId = wifiManager.addNetwork(wcg);
		boolean b = wifiManager.enableNetwork(NetId, true);
		wifiManager.saveConfiguration();
	}

	// 指定配置好的网络进行连接
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回
		if (index >= wifiConfigList.size()) { return; }
		// 连接配置好的指定ID的网络
		wifiManager.enableNetwork(wifiConfigList.get(index).networkId, true);
	}

	/**
	 * 连接指定网络
	 */
	public boolean connectWiFi(WifiConfiguration wcg) {
		int res = wifiManager.addNetwork(wcg);
		boolean b = wifiManager.enableNetwork(res, true);
		wifiInfo = wifiManager.getConnectionInfo();
		wifiManager.saveConfiguration();
		return b;
	}

	/*
	 * 连接指定网络
	 */
	public boolean connectWiFi(String wiFiName, String passWord, int securityMode, boolean hiddenSSID) {
		if (hiddenSSID) {
//			wifiManager.startWifi();//要反射
		}

		WifiConfiguration wcg = getWifiConfig(wiFiName, passWord, securityMode, hiddenSSID);
		int NetId = wifiManager.addNetwork(wcg);
		boolean b = wifiManager.enableNetwork(NetId, true);
		wifiManager.saveConfiguration();
		wifiInfo = wifiManager.getConnectionInfo();
		return b;
	}

	/*
	 * 连接指定网络
	 */
	public boolean connectNetwork(String wiFiName, String passWord, int securityMode) {
		WifiConfiguration wcg = getWifiConfig(wiFiName, passWord, securityMode, false);
		int NetId = wifiManager.addNetwork(wcg);
		boolean b = wifiManager.enableNetwork(NetId, true);
		wifiManager.saveConfiguration();
		wifiInfo = wifiManager.getConnectionInfo();
		return b;
	}

	/*
	 * 连接指定网络
	 */
	public boolean connectNetwork(String wiFiName, String passWord, int securityMode, String text1, String text2, String text3, int prefixLength) {
		WifiConfiguration wcg = getWifiConfig(wiFiName, passWord, securityMode, false);

		wcg = setIP(text1, text2, text3, prefixLength, wcg);
		int NetId = wifiManager.addNetwork(wcg);
		boolean b = wifiManager.enableNetwork(NetId, true);

		wifiManager.saveConfiguration();
		wifiInfo = wifiManager.getConnectionInfo();
		return b;
	}

	/*
	 * 连接指定网络
	 */
	public boolean connectNetwork(String wiFiName, String passWord, int securityMode, String text1, String text2, String text3, String text4, int prefixLength) {
		WifiConfiguration wcg = getWifiConfig(wiFiName, passWord, securityMode, false);

		wcg = setIP(text1, text2, text3, text4, prefixLength, wcg);
		int NetId = wifiManager.addNetwork(wcg);
		boolean b = wifiManager.enableNetwork(NetId, true);
		wifiManager.saveConfiguration();
		wifiInfo = wifiManager.getConnectionInfo();
		return b;
	}

	// 自动获取IP
	public WifiConfiguration AutoSetIP(WifiConfiguration config) {
		try {
			setIpAssignment("DHCP", config); // or "DHCP" for dynamic setting
			wifiManager.updateNetwork(config); // apply the setting
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	/*
	 * 设置静态IP 只有DNS1
	 */
	public WifiConfiguration setIP(String text1, String text2, String text3, int prefixLength, WifiConfiguration wcg) {
		try {
			setIpAssignment("STATIC", wcg); // or "DHCP" for dynamic setting
			setIpAddress(InetAddress.getByName(text1), prefixLength, wcg);
			setGateway(InetAddress.getByName(text2), wcg);
			setDNS(InetAddress.getByName(text3), wcg);
			wifiManager.updateNetwork(wcg); // apply the setting
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wcg;
	}

	/*
	 * 设置静态IP 有DNS1、DNS2
	 */
	public WifiConfiguration setIP(String text1, String text2, String text3, String text4, int prefixLength, WifiConfiguration wcg) {
		try {
			setIpAssignment("STATIC", wcg); // or "DHCP" for dynamic setting
			setIpAddress(InetAddress.getByName(text1), prefixLength, wcg);
			setGateway(InetAddress.getByName(text2), wcg);
			setDNS(InetAddress.getByName(text3), InetAddress.getByName(text4), wcg);
			wifiManager.updateNetwork(wcg); // apply the setting
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wcg;
	}

	/**
	 * 返回指定网络配置列表中网络名 即去掉引号
	 */
	public String getConfigWifiName(WifiConfiguration wcg) {
		String text = "";
		if (null != wcg) {
			text = wcg.SSID;
			text = text.substring(1, text.length() - 1);
		}
		return text;
	}

	// 代码只能判断是否有可用的连接，而不能判断是否能连网
	public static boolean checkNet(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) { return true; }
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public WifiConfiguration getWifiConfig(String wiFiName, String passWord, int security, boolean hiddenSSID) {

		WifiConfiguration config = new WifiConfiguration();
		config.SSID = "\"" + wiFiName + "\"";
		config.hiddenSSID = hiddenSSID;

		switch (security) {
		case WifiConstant.SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;

		case WifiConstant.SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			if (passWord.length() != 0) {
				int length = passWord.length();
				String password = passWord;
				// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
				if ((length == 10 || length == 26 || length == 58) && password.matches("[0-9A-Fa-f]*")) {
					config.wepKeys[0] = password;
				} else {
					config.wepKeys[0] = '"' + password + '"';
				}
			}
			break;

		case WifiConstant.SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			if (passWord.length() != 0) {
				String password = passWord;
				if (password.matches("[0-9A-Fa-f]{64}")) {
					config.preSharedKey = password;
				} else {
					config.preSharedKey = '"' + password + '"';
				}
			}
			break;

		case WifiConstant.SECURITY_EAP:
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
			if (passWord.length() != 0) {
//				config.password.setValue(passWord.toString());//要反射
			}
			break;

		default:
			return null;
		}

		return config;
	}

	/*
	 * 网上找的代码，稍作了修改
	 */
	public static void setIpAssignment(String assign, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		setEnumField(wifiConf, assign, "ipAssignment");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException,
			NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException {
		try {
			Object linkProperties = getField(wifiConf, "linkProperties");
			if (linkProperties == null) return;
			Class laClass = Class.forName("android.net.LinkAddress");
			Constructor laConstructor = laClass.getConstructor(new Class[] { InetAddress.class, int.class });
			Object linkAddress = laConstructor.newInstance(addr, prefixLength);

			ArrayList mLinkAddresses = (ArrayList) getDeclaredField(linkProperties, "mLinkAddresses");
			mLinkAddresses.clear();
			mLinkAddresses.add(linkAddress);
		} catch (Exception e) {
//			DialogUtils.alert(mContext, mContext.getResources().getString(R.string.wifimanager_setting_ip_failure));
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setGateway(InetAddress gateway, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
		try {
			Object linkProperties = getField(wifiConf, "linkProperties");
			if (linkProperties == null) return;
			Class routeInfoClass = Class.forName("android.net.RouteInfo");
			Constructor routeInfoConstructor = routeInfoClass.getConstructor(new Class[] { InetAddress.class });
			Object routeInfo = routeInfoConstructor.newInstance(gateway);
			ArrayList mRoutes = (ArrayList) getDeclaredField(linkProperties, "mRoutes");
			mRoutes.clear();
			mRoutes.add(routeInfo);
		} catch (Exception e) {
//			DialogUtils.alert(mContext, mContext.getResources().getString(R.string.wifimanager_setting_gateway_failure));
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void setDNS(InetAddress dns, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		try {
			Object linkProperties = getField(wifiConf, "linkProperties");
			if (linkProperties == null) return;
			ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(linkProperties, "mDnses");
			mDnses.clear(); // or add a new dns address , here I just want to
							// replace DNS1
			mDnses.add(dns);
		} catch (Exception e) {
//			DialogUtils.alert(mContext, mContext.getResources().getString(R.string.wifimanager_setting_dns1_failure));
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void setDNS(InetAddress dns1, InetAddress dns2, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		try {
			Object linkProperties = getField(wifiConf, "linkProperties");
			if (linkProperties == null) return;
			ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(linkProperties, "mDnses");
			mDnses.clear();
			mDnses.add(dns1);
			mDnses.add(dns2);
		} catch (Exception e) {
//			DialogUtils.alert(mContext, mContext.getResources().getString(R.string.wifimanager_setting_dns_failure));
			e.printStackTrace();
		}
	}

	public static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		Object out = f.get(obj);
		return out;
	}

	public static Object getDeclaredField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		Object out = f.get(obj);
		return out;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setEnumField(Object obj, String value, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
	}
}
