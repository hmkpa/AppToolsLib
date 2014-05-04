package android.hmm.lib.utils;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-12-23
 * Description:  
 */
public class SDKLevelHelper {

	/**
	  Platform Version	 API Level	 VERSION_CODE	 	Notes
	  	Android 4.2.x		17			
	 	Android 4.1.x		16			
	 	Android 4.0.3		15			
	 	Android 4.0.1		14		ICE_CREAM_SANDWICH	Platform Highlights
		Android 3.2			13		HONEYCOMB_MR2	  
		Android 3.1.x		12		HONEYCOMB_MR1		Platform Highlights
		Android 3.0.x		11		HONEYCOMB			Platform Highlights
		Android 2.3.4
		Android 2.3.3		10		GINGERBREAD_MR1		Platform Highlights
		Android 2.3.2
		Android 2.3.1
		Android 2.3			9		GINGERBREAD
		Android 2.2.x		8		FROYO				Platform Highlights
		Android 2.1.x		7		ECLAIR_MR1			Platform Highlights
		Android 2.0.1		6		ECLAIR_0_1
		Android 2.0			5		ECLAIR
		Android 1.6			4		DONUT				Platform Highlights
		Android 1.5			3		CUPCAKE				Platform Highlights
		Android 1.1	 		2		BASE_1_1	  
		Android 1.0	 		1		BASE
	 */

	public static int getAndroidSDKLevel() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
		}
		return version;
	}

	public static boolean compareSDKLevel(int sdkTag) {
//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
////		     // only for gingerbread and newer versions
//		}
		return android.os.Build.VERSION.SDK_INT >= sdkTag ? true : false;
	}
}
