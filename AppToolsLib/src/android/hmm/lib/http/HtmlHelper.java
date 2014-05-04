package android.hmm.lib.http;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.protocol.HTTP;


/**
 * @author HEMING
 * Time:2013-8-2
 *
 */
public class HtmlHelper {
	//
	public static String getHtml(String url, String charset) throws IOException, Exception {
		return getHtml(url, charset, 20);
	}

	//
	public static String getHtml(String url) throws IOException, Exception {
		return getHtml(url, 20);
	}

	//
	public static String getHtml(String url, int timeout) throws IOException, Exception {
		return getHtml(url, HTTP.UTF_8, timeout);
	}

	// 获取网络文本资源
	public static String getHtml(String url, String charset, int timeout) throws Exception {
		HttpClientHelper client = new HttpClientHelper(url, timeout);
		return client.downloadString(url, charset);
	}

	public static byte[] getBytes(String url, int timeout) throws Exception {
		HttpClientHelper client = new HttpClientHelper(url, timeout);
		return client.downloadByte(url);
	}

	// 取网络stream
	private static InputStream getHttpStream(String url, int timeout) throws Exception {
		HttpClientHelper client = new HttpClientHelper(url, timeout);
		return client.getContent();
	}

	// http下载文件，返回文件大小,-1代表下载失败
	public static boolean downloadFile(String url, String path) {
		try {
			InputStream is = getHttpStream(url, 30);
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			file.mkdirs();

			file.createNewFile();
			byte[] buffer = new byte[1024];

			BufferedOutputStream out = null;

			out = new BufferedOutputStream(new FileOutputStream(file));
			int readLength = is.read(buffer);
			while (readLength != -1) {
				out.write(buffer, 0, readLength);
				readLength = is.read(buffer);
			}

			out.close();
			is.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
