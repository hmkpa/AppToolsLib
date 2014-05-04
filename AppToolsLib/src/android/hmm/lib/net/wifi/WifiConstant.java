package android.hmm.lib.net.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;

public class WifiConstant {
	public static final int SECURITY_HIDE = -1;
	public static final int SECURITY_NONE = 0;
	public static final int SECURITY_WEP = 1;
	public static final int SECURITY_PSK = 2;
	public static final int SECURITY_EAP = 3;
	public static final int SECURITY_UNKNOWN = -2;
    
	
	public static final int PSKTYPE_UNKNOWN = 0;
	public static final int PSKTYPE_WPA = 1;
	public static final int PSKTYPE_WPA2 = 2;
	public static final int PSKTYPE_WPA_WPA2 = 3;
	
    public static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) ||
                config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    public static int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (result.capabilities.contains("PSK")) {
            return SECURITY_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_EAP;
        }
        return SECURITY_NONE;
    }
    
    public static String getSecurityString(Context context,int security,int pskType) {
        switch(security) {
            case SECURITY_EAP:
                return "802.1x EAP";
            case SECURITY_PSK:
                switch (pskType) {
                    case PSKTYPE_WPA:
                        return "WPA PSK";
                    case PSKTYPE_WPA2:
                        return "WPA2 PSK";
                    case PSKTYPE_WPA_WPA2:
                        return "WPA/WPA2 PSK";
                    case PSKTYPE_UNKNOWN:
                    default:
                        return "WPA/WPA2 PSK";
                }
            case SECURITY_WEP:
                return "WEP";
            case SECURITY_NONE:
            	return "无";
            default:
                return "";
        }
    }
    
    public static int getPskType(ScanResult result) {
        boolean wpa = result.capabilities.contains("WPA-PSK");
        boolean wpa2 = result.capabilities.contains("WPA2-PSK");
        if (wpa2 && wpa) {
            return PSKTYPE_WPA_WPA2;
        } else if (wpa2) {
            return PSKTYPE_WPA2;
        } else if (wpa) {
            return PSKTYPE_WPA;
        } else {
            return PSKTYPE_UNKNOWN;
        }
    }
}
