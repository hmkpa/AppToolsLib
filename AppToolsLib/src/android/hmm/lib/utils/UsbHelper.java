package android.hmm.lib.utils;

import java.io.File;
import android.content.Context;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-8-13
 * Description:  
 */
public class UsbHelper {

	private static String USB_MODE_PATH = "/sys/bus/platform/drivers/usb20_otg/force_usb_mode";
	public static final String status_open = "2";
	public static final String status_close = "1";
	public static final String status_unknow = "0";

	public static boolean isSurportUsbFunc() {
		File file = new File(USB_MODE_PATH);
		if (file.exists() && file.isFile() && file.canRead()) { return true; }
		return false;
	}

	public static boolean isWorking(Context context) {
		if (!isSurportUsbFunc()) { return false; }
		if (getUsbMode(context).startsWith(status_open)) { return true; }
		return false;
	}

	private static String getUsbMode(Context context) {
		if (!isSurportUsbFunc()) { return null; }
		return FileHelper.readFile( USB_MODE_PATH);
	}

	public static void setUsbMode(Context context, boolean isEnable) {
		if (!isSurportUsbFunc()) { return; }

		String mode = isEnable ? status_open : status_close;
		String data = FileHelper.readFile(USB_MODE_PATH);
		String str = data.subSequence(0, 1).toString();
		data = data.replaceFirst(str, mode);
		FileHelper.writeFile(USB_MODE_PATH, mode);
	}

}
