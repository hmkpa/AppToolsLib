package android.hmm.lib.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

/**
 * @author HMM
 * TIME:2013-07-31
 */
public class CMDHelper {

	/**********************************************************************************
	 * 一 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	 * @param command 命令：String apkRoot="chmod 777 " + getPackageCodePath();
	 *                调用: RootCommand(apkRoot);
	 * @return 应用程序是/否获取Root权限
	 ********************************************************************************/
	public static synchronized boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (null != process) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/******************************************************************
	 * 二
	 * @return
	 *********************************************************************/
	public static synchronized boolean getRootAhth() {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("exit\n");
			os.flush();
			int exitValue = process.waitFor();
			if (exitValue == 0) { return true; }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (null != process) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/***************************************************************************
	 * 执行linux命令但不关注结果输出
	 * @param paramString
	 * @return
	 ***************************************************************************/
	public static boolean haveRoot() {
		int i = execRootCmd("echo test"); // 通过执行测试命令来检测
		if (i == 0) { return true; }
		return false;
	}

	private static int execRootCmd(String paramString) {
		Process process = null;
		DataOutputStream dataOutputStream = null;
		try {
			process = Runtime.getRuntime().exec("su");
			Object localObject = process.getOutputStream();
			dataOutputStream = new DataOutputStream((OutputStream) localObject);
			String str = String.valueOf(paramString);
			localObject = str + "\n";
			dataOutputStream.writeBytes((String) localObject);
			dataOutputStream.flush();
			dataOutputStream.writeBytes("exit\n");
			dataOutputStream.flush();
			process.waitFor();
			int result = process.exitValue();
			return (Integer) result;
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (null != process) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/*****************************************************************************************
	 * /system/bin/su 该文件是否存在,以此判断是否root
	 * 还有是检查default.prop里的ro.secure是否等于0
	 * 存有疑问：没有root权限，如何判断该文件是否存在
	 * @return 
	 *****************************************************************************************/
	protected static boolean existsRootFile() {
		File su = new File("/system/bin/su");
		if (su.exists()) {
			Log.i("RootUtils", "check the root file is eixst");
			return true;
		} else {
			Log.i("RootUtils", "check the root file is not eixst");
			return false;
		}
	}

	/************************************************************************************************
	 * 执行一个shell命令，并返回字符串值
	 * @param cmd 命令名称&参数组成的数组（例如：{"/system/bin/cat", "/proc/version"}）
	 * @param workdirectory  命令执行路径（例如："system/bin/"）
	 * @return 执行结果组成的字符串
	 * @throws IOException
	 ************************************************************************************************/
	public static synchronized String run(String[] cmd, String workdirectory) throws IOException {
		StringBuffer result = null;
		try {
			// 创建操作系统进程（也可以由Runtime.exec()启动）
			// Runtime runtime = Runtime.getRuntime();
			// Process proc = runtime.exec(cmd);
			// InputStream inputstream = proc.getInputStream();
			ProcessBuilder builder = new ProcessBuilder(cmd);

			InputStream in = null;
			if (workdirectory != null) {
				builder.directory(new File(workdirectory));// 设置工作目录（同上）
				builder.redirectErrorStream(true);// 合并标准错误和标准输出
				Process process = builder.start();// 启动一个新进程

				in = process.getInputStream();// 读取进程标准输出流
				byte[] re = new byte[1024];
				result = new StringBuffer();
				while (in.read(re) != -1) {
					result = result.append(new String(re));
				}
			}
			// 关闭输入流
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result == null ? null : result.toString();
	}

	/******************************************************************************
	 * 
	 * 
	 *******************************************************************************/
	public static String execCmd(Context context, String paramString) {
		Process process = null;
		DataOutputStream dataOutputStream = null;
		try {
			process = Runtime.getRuntime().exec("su");
			Object localObject = process.getOutputStream();
			InputStream stream = process.getInputStream();
			dataOutputStream = new DataOutputStream((OutputStream) localObject);
			String str = String.valueOf(paramString);
			localObject = str + "\n";
			dataOutputStream.writeBytes((String) localObject);
			dataOutputStream.flush();
			dataOutputStream.writeBytes("exit\n");
			dataOutputStream.flush();
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuffer result = new StringBuffer();
			while ((str = reader.readLine()) != null) {
				Log.d("reader.readLine=", str);
				result = result.append(str);
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			LogHelper.println(e.toString());
			ToastHelper.showCustomToast(context, e.toString());
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (null != process) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/***********************************************************
	 * Wi-Fi adb
	 ***********************************************************/
	public static String execCmdWiFiADB(Context context) {
		String str = "setprop service.adb.tcp.port 5555";
		str += "\n" + "stop adbd";
		str += "\n" + "start adbd";
		return execCmd(context,str);
	}
}
