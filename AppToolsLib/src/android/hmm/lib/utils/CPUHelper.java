package android.hmm.lib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.hmm.lib.model.CPUInfo;
import android.util.Log;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-11-8
 * Description:  
 */
public class CPUHelper {

	/**
	  * 获取CPU核心数
	  * @return
	  */
	public static int getNumCores() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]", pathname.getName())) { return true; }
				return false;
			}
		}

		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 获取CPU最大频率（单位KHZ）
	// "/system/bin/cat" 命令行
	// "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
	public static String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// 获取CPU最小频率（单位KHZ）
	public static String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// 实时获取CPU当前频率（单位KHZ）
	public static String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static CPUInfo getCPU() {
		String str1 = "/proc/cpuinfo";
		CPUInfo cpuInfo = new CPUInfo();
		try {
//			String cpuMode = "";
//			FileReader fr = new FileReader(str1);
//			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
//			str2 = localBufferedReader.readLine();
//			arrayOfString = str2.split("\\s+");
//			for (int i = 2; i < arrayOfString.length; i++) {
//				cpuMode = cpuMode + arrayOfString[i] + " ";
//			}
//			SystemItem se1 = new SystemItem("CPU型号：", cpuMode);
//			listData.add(se1);
//			str2 = localBufferedReader.readLine();
//			arrayOfString = str2.split("\\s+");
//			localBufferedReader.close();
//			fr.close();
//			SystemItem se2 = new SystemItem("CPU的频率：", arrayOfString[2]);
////			SystemItemElement se2 = new SystemItemElement("CPU的频率：", cpuRate);
//			listData.add(se2);

			FileReader frtemp = new FileReader(str1);
			BufferedReader local = new BufferedReader(frtemp, 8192);
			String temp = "";
			String line = local.readLine();
			while (null != line) {
				temp += line + "\n";
				line = local.readLine();
			}
			local.close();
			frtemp.close();
			Log.i("AboutSystem", "cpu:" + temp);
			temp = temp.replace("\n\n", "\n");
			int index = temp.indexOf("\n");
			if (-1 != index) {
				temp = temp.substring(0, index);
				temp = temp.substring(index + 1);
				String msg[] = temp.split(":");
				if (msg != null && msg.length > 1) {
					cpuInfo.setName(msg[1]);
				}
			}
			List<String> listProcessor = new ArrayList<String>();
			while (temp.indexOf("processor") != -1) {
				index = temp.indexOf("BogoMIPS");
				if (index != -1) {
					temp = temp.substring(index);
					index = temp.indexOf("\n");
					if (index != -1) {
						String tBogoMIPS = temp.substring(0, index);
						temp = temp.substring(index + 1);
						String msg[] = tBogoMIPS.split(":");
						if (msg != null && msg.length > 1) {
							listProcessor.add(msg[1]);
						}
					}
				}
			}
			cpuInfo.setListProcessor(listProcessor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cpuInfo;
	}

	public static CPUInfo getCPUInfo() {
		CPUInfo cpuInfo = new CPUInfo();
		try {
			FileReader frtemp = new FileReader("/proc/cpuinfo");
			BufferedReader local = new BufferedReader(frtemp, 8192);
			String line = local.readLine();
			List<String> listProcessor = new ArrayList<String>();
			while (null != line) {
				if (line.indexOf("Processor") != -1) {
					cpuInfo.setName(getValueByName(line, "Processor"));
				} else if (line.indexOf("Serial") != -1) {
					cpuInfo.setSerial(getValueByName(line, "Serial"));
				} else if (line.indexOf("Hardware") != -1) {
					cpuInfo.setHardware(getValueByName(line, "Hardware"));
				} else if (line.indexOf("BogoMIPS") != -1) {
					listProcessor.add(getValueByName(line, "BogoMIPS"));
				}
				line = local.readLine();
			}
			local.close();
			frtemp.close();
			cpuInfo.setListProcessor(listProcessor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cpuInfo.setNumCores(getNumCores());
		cpuInfo.setMaxCpuFreq(getMaxCpuFreq());
		cpuInfo.setMinCpuFreq(getMinCpuFreq());
		cpuInfo.setCurCpuFreq(getCurCpuFreq());
		return cpuInfo;
	}

	// 获取CPU名字
	public static String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			for (int i = 0; i < array.length; i++) {
			}
			return array[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	* 获取CPU序列号
	* @return CPU序列号(16位) 
	* 读取失败为"0000000000000000" 
	*/
	public static String getCPUSerial() {
		String str = "", strCPU = "", cpuAddress = "0000000000000000";
		try {
			// 读取CPU信息
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			// 查找CPU序列号
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					// 查找到序列号所在行
					if (str.indexOf("Serial") > -1) {
						// 提取序列号
						strCPU = str.substring(str.indexOf(":") + 1, str.length());
						// 去空格
						cpuAddress = strCPU.trim();
						break;
					}
				} else {
					// 文件结尾
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return cpuAddress;
	}

	private static String getValueByName(String str, String name) {
		String value = "不详";
		try {
//			str = str.substring(str.indexOf(":") + 1, str.length());
			if (str.contains(":")) {
				String msg[] = str.split(":");
				if (msg != null && msg.length > 1) {
					value = msg[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
