package android.hmm.lib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hmm.lib.receiver.SdcardHelper;

/**
 * @author heming
 * @since :JDK ?
 * @version：1.0
 *              Create at:2014-4-12
 *              Description:
 */

/**
 * http://blog.csdn.net/ztp800201/article/details/7322110
 * 
 * 1、apk中有两种资源文件，使用两种不同的方式进行打开使用。
		raw使用InputStream in = getResources().openRawResource(R.raw.test);
		asset使用InputStream in = getResources().getAssets().open(fileName);
		这些数据只能读取，不能写入。更重要的是该目录下的文件大小不能超过1M。
	
		同时，需要注意的是，在使用InputStream的时候需要在函数名称后加上throws IOException。
	2、SD卡中的文件使用FileInputStream和FileOutputStream进行文件的操作。
	3、存放在数据区(/data/data/..)的文件只能使用openFileOutput和openFileInput进行操作。
		注意不能使用FileInputStream和FileOutputStream进行文件的操作。
	4、RandomAccessFile类仅限于文件的操作，不能访问其他IO设备。它可以跳转到文件的任意位置，从当前位置开始读写。
	5、InputStream和FileInputStream都可以使用skip和read(buffre,offset,length)函数来实现文件的随机读取。
 */
public class FileHelper {

	public final static int TYPE_DIR = 0;
	public final static int TYPE_FILE = 1;

	/******************************************************
	 * 向SharedPreferences写数据
	 * @param context
	 * @param file
	 * @param key
	 * @param object
	 *****************************************************/
	public static void putSharedPreferences(Context context, String file, String key, Object object) {
		if (null == object) {
			LogHelper.log(context, "the value of object is null");
			return;
		}

		SharedPreferences pref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		if (object instanceof String) {
			editor.putString(key, String.valueOf(object));
		} else if (object instanceof Boolean) {
//			editor.putBoolean(key, Boolean.parseBoolean(object.toString()));
			editor.putBoolean(key, Boolean.valueOf(object.toString()));
		} else if (object instanceof Integer) {
			editor.putInt(key, Integer.valueOf(object.toString()));
		} else if (object instanceof Float) {
			editor.putFloat(key, Float.valueOf(object.toString()));
		} else if (object instanceof Long) {
			editor.putLong(key, Long.valueOf(object.toString()));
		}
		editor.commit();
	}

	/********************************************************
	 * 从SharedPreferences读数据
	 * @param context
	 * @param file
	 * @param key
	 * @param type
	 * @param defaultValue
	 * @return
	 *******************************************************/
	public static Object getSharedPreferences(Context context, String file, String key, Class<?> type, Object defaultValue) {
		Object object = null;

		SharedPreferences pref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		if (String.class.equals(type)) {
			object = pref.getString(key, String.valueOf(object));
		} else if (Boolean.class.equals(type)) {
//			editor.putBoolean(key, Boolean.parseBoolean(object.toString()));
			object = pref.getBoolean(key, Boolean.valueOf(defaultValue.toString()));
		} else if (Integer.class.equals(type)) {
			object = pref.getInt(key, Integer.valueOf(defaultValue.toString()));
		} else if (Float.class.equals(type)) {
			object = pref.getFloat(key, Float.valueOf(defaultValue.toString()));
		} else if (Long.class.equals(type)) {
			object = pref.getLong(key, Long.valueOf(defaultValue.toString()));
		}
		return object;
	}

	public static void deleteSharedPreferences(Context context, String file, String key) {
		SharedPreferences preferences = context.getSharedPreferences(file, 0);
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/********************************************************************
	 * 向已创建的文件中写入数据
	 * @param activity
	 * @param data
	 * @param filepath
	 *******************************************************************/
	public static void writeFile(String filepath, String data) {
		if (filepath == null || filepath.length() < 1) { return; }

		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			File file = new File(filepath);
			if (!file.isFile()) { return; }
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(filepath, true);// 创建FileWriter对象，用来写入字符流
			bw = new BufferedWriter(fw); // 将缓冲对文件的输出
			bw.write(data + "\n"); // 写入文件
			bw.newLine();
			bw.flush(); // 刷新该流的缓冲
			bw.close();
			fw.close();

			/**
				FileOutputStream fout = new FileOutputStream(file);
			//	FileOutputStream fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				byte[] bytes = data.getBytes();
				fout.write(bytes);
				fout.close();
			 */
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bw) {
					bw.close();
				}
				if (null != fw) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*************************************************************************
	 * 根据文件前三个字符的编码自动读取文件
	 * @param filepath
	 * @return
	 *************************************************************************/
	public static String readFile(String filepath) {
		File file = new File(filepath);

		BufferedReader reader;
		String text = "";
		try {
			if (!file.exists() || !file.isFile()) { return null; }

			// FileReader f_reader = new FileReader(file);
			// BufferedReader reader = new BufferedReader(f_reader);
			FileInputStream fis = new FileInputStream(file);
			final int tLength = 3;
			if (fis.available() > tLength) {
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.mark(tLength + 1);
				byte[] first3bytes = new byte[tLength];
				bis.read(first3bytes);// 找到文档的前三个字节并自动判断文档类型。
				bis.reset();
				if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {// utf-8
					reader = new BufferedReader(new InputStreamReader(bis, HTTP.UTF_8));
				} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
					reader = new BufferedReader(new InputStreamReader(bis, "unicode"));
				} else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {
					reader = new BufferedReader(new InputStreamReader(bis, "utf-16be"));
				} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
					reader = new BufferedReader(new InputStreamReader(bis, "utf-16le"));
				} else {
					reader = new BufferedReader(new InputStreamReader(bis, "GBK"));
				}
				String str = reader.readLine();
				while (str != null) {
					text = text + str + "\n";
					str = reader.readLine();
				}
				reader.close();
				bis.close();
			} else {
				// FileInputStream fin = context.openFileInput(fileName);
				text = readDataFromInputStream(getInputStream(filepath));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	/********************************************************************
	 * 从resource的raw中读取文件数据
	 * 几百行数据可通过
	 ********************************************************************/
	public static String readRawResource(Context context, int id) {
		try {
			InputStream in = context.getResources().openRawResource(id);
			return readDataFromInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readDataFromInputStream(InputStream is) {
		try {
			int length = is.available();
			byte[] buffer = new byte[length];
			while (-1 != is.read(buffer))
				;
			is.close();
			return EncodingUtils.getString(buffer, HTTP.UTF_8);

			/*
			 * int len;
			 * byte[] bBuf = new byte[1024 * 50];
			 * int readCount = 0;
			 * while ((len = fi.read(bBuf, readCount, 1024 * 50 - readCount)) != -1) {
			 * readCount += len;
			 * }
			 * data = new String(bBuf, 0, readCount, "utf-8");
			 */

			/*
			 * int length = in.available();
			 * byte[] buffer = new byte[length];
			 * in.read(buffer);
			 * String result = EncodingUtils.getString(buffer, HTTP.UTF_8);
			 * in.close();
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/********************************************************************
	 * 如何从FileInputStream中得到InputStream？
	 ********************************************************************/
	public static InputStream getInputStream(String fileName) throws IOException {
		InputStream in = null;
		try {
			FileInputStream fin = new FileInputStream(fileName);
			in = new BufferedInputStream(fin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	/**
	 * String Name = File.getName(); //获得文件或文件夹的名称：
	 * String parentPath = File.getParent(); //获得文件或文件夹的父目录
	 * String path = File.getAbsoultePath();//绝对路经
	 * String path = File.getPath();//相对路经
	 * File.createNewFile();//建立文件
	 * File.mkDir(); //建立文件夹
	 * File.isDirectory(); //判断是文件或文件夹
	 * File[] files = File.listFiles(); //列出文件夹下的所有文件和文件夹名
	 * File.renameTo(dest); //修改文件夹和文件名
	 * File.delete(); //删除文件夹或文件
	 */

	/*
	 * 程序功能：演示了RandomAccessFile类的操作，同时实现了一个文件复制操作。
	 * 使用RandomAccessFile进行文件的读写：
	 * RandomAccessFile的使用方法比较灵活，功能也比较多，可以使用类似seek的方式可以跳转到文件的任意位置，
	 * 从文件指示器当前位置开始读写,它有两种构造方法
	 * new RandomAccessFile(f,"rw");//读写方式
	 * new RandomAccessFile(f,"r");//只读方式
	 */
	public static void RandomAccessFileDemo() throws IOException {
		RandomAccessFile file = new RandomAccessFile("file", "rw");
		// 以下向file文件中写数据
		file.writeInt(20);// 占4个字节
		file.writeDouble(8.236598);// 占8个字节
		file.writeUTF("这是一个UTF字符串");// 这个长度写在当前文件指针的前两个字节处，可用readShort()读取
		file.writeBoolean(true);// 占1个字节
		file.writeShort(395);// 占2个字节
		file.writeLong(2325451l);// 占8个字节
		file.writeUTF("又是一个UTF字符串");
		file.writeFloat(35.5f);// 占4个字节
		file.writeChar('a');// 占2个字节

		file.seek(0);// 把文件指针位置设置到文件起始处

		// 以下从file文件中读数据，要注意文件指针的位置
		System.out.println("——————从file文件指定位置读数据——————");
		System.out.println(file.readInt());
		System.out.println(file.readDouble());
		System.out.println(file.readUTF());

		file.skipBytes(3);// 将文件指针跳过3个字节，本例中即跳过了一个boolean值和short值。
		System.out.println(file.readLong());

		file.skipBytes(file.readShort()); // 跳过文件中“又是一个UTF字符串”所占字节，注意readShort()方法会移动文件指针，所以不用加2。
		System.out.println(file.readFloat());

		// 以下演示文件复制操作
		System.out.println("——————文件复制（从file到fileCopy）——————");
		file.seek(0);
		RandomAccessFile fileCopy = new RandomAccessFile("fileCopy", "rw");
		int len = (int) file.length();// 取得文件长度（字节数）
		byte[] b = new byte[len];
		file.readFully(b);
		fileCopy.write(b);
		System.out.println("复制完成！");
	}

	/**
	 * 读取资源文件时能否实现类似于seek的方式可以跳转到文件的任意位置，从指定的位置开始读取指定的字节数呢？
	 * 答案是可以的。
	 * 在FileInputStream和InputStream中都有下面的函数：
	 * public long skip (long byteCount); //从数据流中跳过n个字节
	 * public int read (byte[] buffer, int offset, int length); //从数据流中读取length数据存在buffer的offset开始的位置。
	 * offset是相对于buffer的开始位置的，不是数据流。
	 * 
	 * 可以使用这两个函数来实现类似于seek的操作，请看下面的测试代码：
	 * 其中read_raw是一个txt文件，存放在raw目录下。
	 * read_raw.txt文件的内容是："ABCDEFGHIJKLMNOPQRST"
	 */
	public String getRawString(Context context, int id) throws IOException {
		String str = null;
		InputStream in = context.getResources().openRawResource(id);
		int length = in.available();
		byte[] buffer = new byte[length];
		in.skip(2); // 跳过两个字节
		in.read(buffer, 0, 3); // 读三个字节
		in.skip(3); // 跳过三个字节
		in.read(buffer, 0, 3); // 读三个字节
		str = EncodingUtils.getString(buffer, "BIG5");
		in.close();
		return str;
	}

	/*
	 * APK资源文件的大小不能超过1M，如果超过了怎么办？
	 * 我们可以将这个数据再复制到data目录下，然后再使用。复制数据的代码如下
	 */
	public boolean assetsCopyData(Context context, String strAssetsFilePath, String desFilePath) {
		boolean bIsSuc = true;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		File file = new File(desFilePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				Runtime.getRuntime().exec("chmod 766 " + file);
			} catch (IOException e) {
				bIsSuc = false;
			}
		} else {// 存在
			return true;
		}

		try {
			inputStream = context.getAssets().open(strAssetsFilePath);
			outputStream = new FileOutputStream(file);

			int nLen = 0;
			byte[] buff = new byte[1024 * 1];
			while ((nLen = inputStream.read(buff)) > 0) {
				outputStream.write(buff, 0, nLen);
			}
		} catch (IOException e) {
			bIsSuc = false;
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				bIsSuc = false;
			}
		}
		return bIsSuc;
	}

	/*************************************************************
	 * 内置sd卡目录下创建文件夹或文件(文件多时，建议开线程)
	 *************************************************************/
	public static boolean createSdcardFile(Context context, String fileName, int fileType) {
		try {
			String path = SdcardHelper.getSdcardAbsPath();
			if (null != path && path.length() > 0) {
				File file = new File(fileName);
				switch (fileType) {
				case TYPE_DIR:
					return file.mkdirs();
				case TYPE_FILE:
					if (fileName.contains(File.separator)) {
						String preDir = fileName.substring(0, fileName.lastIndexOf(File.separator));
						createSdcardFile(context, preDir, TYPE_DIR);
					}
					return file.createNewFile();
				default:
					ToastHelper.showCustomToast(context, fileName + "传入的文件参数不正确");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/************************************************************
	 * 复制文件夹或文件夹
	 * @param url1源文件夹
	 * @param url2目标文件夹
	 * @throws IOException
	 * 注意：大文件的复制可能会有问题
	 ************************************************************/
	public static void copy(String srcFileName, String targePath) throws IOException {
//		srcfile = "f:/photos";
//		desfile = "d:/tempPhotos";
		try {
			if (new File(targePath).isFile()) { return; }
			
			(new File(targePath)).mkdirs();
			File srcFile = new File(srcFileName);
			if (!srcFile.exists()) { return; }
			
			if (srcFile.isFile()) {
				copyFile(srcFile, new File(targePath + File.separator + srcFile.getName()));
				return;
			}

			copyDirectiory(srcFileName, targePath);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	/**************************************************************************
	 * 复制文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 **************************************************************************/
	private static void copyFile(File sourceFile, File targetFile) {
		try {
			// 新建文件输入流并对它进行缓冲
			FileInputStream input = new FileInputStream(sourceFile);
			BufferedInputStream inBuff = new BufferedInputStream(input);

			// 新建文件输出流并对它进行缓冲
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();

			// 关闭流
			inBuff.close();
			outBuff.close();
			output.close();
			input.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/****************************************************************************
	 * 复制文件夹
	 * @param sourceDir
	 * @param targetPath
	 ****************************************************************************/
	private static void copyDirectiory(String sourceDir, String targetPath) {
		try {
			(new File(targetPath)).mkdirs();// 新建目标目录
			// 获取源文件夹当前下的文件或目录
			File[] file = (new File(sourceDir)).listFiles();
			if (null == file) { return; }

			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					String tname = targetPath + File.separator + file[i].getName();// 目标文件
					copyFile(file[i], new File(tname));
				} else if (file[i].isDirectory()) {
					String dir1 = sourceDir + File.separator + file[i].getName();// 源文件夹
					String dir2 = targetPath + File.separator + file[i].getName();// 目标文件夹
					copyDirectiory(dir1, dir2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	/******************************************************************************
	 * 删除文件夹里面的所有文件
	 * @param filePath 
	 ******************************************************************************/
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) { return; }
		if (!file.isDirectory()) {
			file.delete();// 删除文件
			return;
		}
		File[] fileList = file.listFiles();
		if (null != fileList) { 
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					deleteFile(filePath + File.separator + fileList[i]);// 先删除文件夹里面的文件
				}
				fileList[i].delete();// 再删除空文件夹
			}
		}
		file.delete();// 删除文件夹
	}

	// return the all of files this directory
	public static List<String> getFileList(String filePath) {
		File filedir = new File(filePath);
		File[] files = filedir.listFiles();
		if (files == null || files.length == 0) { return null; }
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			File tfile = files[i];
			items.add(tfile.getName());
		}
		return items;
	}

	// Judgment is a pic
	public static boolean JudgmentPic(File file) {
		if (file.isFile()) {
			try {
				int idx = file.getPath().lastIndexOf(".");
				if (idx <= 0) { return false; }
				String suffix = file.getPath().substring(idx);
				if (suffix.toLowerCase().equals(".jpg") || suffix.toLowerCase().equals(".jpeg") || suffix.toLowerCase().equals(".bmp") || suffix.toLowerCase().equals(".png")
						|| suffix.toLowerCase().equals(".gif")) { return true; }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (file.isDirectory()) {
			if ((file.getName()).equals("LOST.DIR") || file.getName().equals("DCIM")) { return false; }

			String temp = (file.getName()).substring(0, 1);
			if (temp.equals(".")) { return false; }
			return true;
		}
		return false;
	}

	/******************************************************************
	 * 前进
	 * @param path
	 * @return
	 ******************************************************************/
	public static String forwardPath(String path) {
		String temp = null;
		if (path == null || path.length() < 1) { return temp; }

		if (path.endsWith(File.separator)) { return path; }

		return path + File.separator;
	}

	/********************************************************
	 * 后退
	 * @param rootPath
	 * @param path
	 * @return
	 ********************************************************/
	public static String backwardPath(String rootPath, String path) {
		if (rootPath == null || path == null) { return rootPath; }
		if (!path.contains(rootPath) || rootPath.equals(path)) { return rootPath; }

		int index = path.lastIndexOf(File.separator);
		if (-1 != index) { return path.substring(0, index); }
		return rootPath;
	}

	/***************************************************************
	 * 文件排序
	 * @param list
	 * @return
	 ***************************************************************/
	public static List<String> sortFiles(List<String> list) {
		if (null == list || list.size() == 0) { return list; }

		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 1; j < list.size() - i; j++) {
				if (compairByName(list.get(j - 1), list.get(j)) > 0) {
					list.set((j - 1), list.get(j));
					list.set(j, list.get(j - 1));
				}
			}
		}
		return list;
	}

	private static int compairByName(String file1, String file2) {
		Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
		return com.compare(file1, file2);
	}

	/**********************************************************
	 * 更改权限
	 **********************************************************/
	public static int chmod(Context context, File path, int mode) {
		try {
			Runtime.getRuntime().exec("chmod " + mode + " " + path);
			return mode;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mode;
//		return android.os.FileUtils.setPermissions(path.getAbsolutePath(), mode, context.getApplicationInfo().uid, -1);
	}

}
