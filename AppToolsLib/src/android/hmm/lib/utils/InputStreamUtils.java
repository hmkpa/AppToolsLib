package android.hmm.lib.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpException;

/**
 * @author HEMING
 * Time:2013-8-2
 */
public class InputStreamUtils {

	public static final int BUFFER_SIZE = 8192;
	public static final String CHARSET = "utf-8";

	/**
	 * Save InputStream to disk
	 * @throws FileNotFoundException
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void saveInputStreamToFile(InputStream in, String filename) throws FileNotFoundException, HttpException, IOException {
		BufferedOutputStream out = null;
		byte[] bt = new byte[BUFFER_SIZE];
		try {
			File file = new File(filename);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			out = new BufferedOutputStream(new FileOutputStream(file));
			int readLength = in.read(bt);
			while (readLength != -1) {
				out.write(bt, 0, readLength);
				readLength = in.read(bt);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Convert InputStream to String
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream is) throws IOException {
		if (is != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(is, CHARSET));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			boolean flag = false;
			while ((line = in.readLine()) != null) {
				if (flag) {
					buffer.append('\n');
				} else {
					flag = true;
				}
				buffer.append(line);
			}
			try {
				is.close();
			} catch (Exception e) {
			}
			try {
				in.close();
			} catch (Exception e) {
			}
			return buffer.toString();
		}
		return null;
	}

}
