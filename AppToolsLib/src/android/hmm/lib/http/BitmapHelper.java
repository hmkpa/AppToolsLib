package android.hmm.lib.http;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-10-21
 * Description:  
 */
public class BitmapHelper {

	public static Map<String, SoftReference<Bitmap>> imageRef = new HashMap<String, SoftReference<Bitmap>>();

	public static Bitmap getHttpBitmap(String url) {
		return getHttpBitmap(url, 10);
	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url, int timeout) {
		URL myFileURL;
		Bitmap bitmap = null;
		if (imageRef.containsKey(url)) {
			SoftReference<Bitmap> softBitmap = imageRef.get(url);
			bitmap = softBitmap.get();
			if (bitmap != null) return bitmap;
		}

		try {
			myFileURL = new URL(url);

			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
			// 设置超时时间为10秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(timeout * 1000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);

			SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
			imageRef.put(url, softBitmap);
			is.close();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			bitmap = null;
		} catch (Exception e) {
			e.printStackTrace();
			bitmap = null;
		}
		return bitmap;

	}

	// 加载本地图片
	public static Bitmap getLoacalBitmap(String pathName) {
		Bitmap bitmap = null;
		try {
			// 降低图片的像素
			// BitmapFactory.Options options=new BitmapFactory.Options();
			// options.inSampleSize = 8;
			// bitmap = BitmapFactory.decodeFile(pathName,options);
			bitmap = BitmapFactory.decodeFile(pathName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
