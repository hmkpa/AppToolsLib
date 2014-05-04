package android.hmm.lib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-8-13
 * Description:  
 */
public class StorageHelper {

	/**
	 * 获取Android手机中SD卡存储信息 获取剩余空间
	 * 在manifest.xml文件中要添加  
	 * android:name="android.permission.WRITE_EXTERNAL_STORAGE">
	 */
	public void getSDCardInfo() {
		// 需要判断手机上面SD卡是否插好，如果有SD卡的情况下，我们才可以访问得到并获取到它的相关信息，当然以下这个语句需要用if做判断
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 取得sdcard文件路径
			File path = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(path.getPath());
			// 获取block的SIZE
			long blocSize = statfs.getBlockSize();
			// 获取BLOCK数量
			long totalBlocks = statfs.getBlockCount();
			// 空闲的Block的数量
			long availaBlock = statfs.getAvailableBlocks();
			// 计算总空间大小和空闲的空间大小
			// 存储空间大小跟空闲的存储空间大小就被计算出来了。
			long availableSize = blocSize * availaBlock;
			// (availableBlocks * blockSize)/1024 KIB 单位
			// (availableBlocks * blockSize)/1024 /1024 MIB单位
			long allSize = blocSize * totalBlocks;
		}
	}
	
	public static long getSDCardAvailaSize() {
		// 需要判断手机上面SD卡是否插好，如果有SD卡的情况下，我们才可以访问得到并获取到它的相关信息，当然以下这个语句需要用if做判断
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 取得sdcard文件路径
			File path = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(path.getPath());
			// 获取block的SIZE
			long blocSize = statfs.getBlockSize();
			// 获取BLOCK数量
			long totalBlocks = statfs.getBlockCount();
			// 空闲的Block的数量
			long availaBlock = statfs.getAvailableBlocks();
			// 计算总空间大小和空闲的空间大小
			// 存储空间大小跟空闲的存储空间大小就被计算出来了。
			return blocSize * availaBlock;
		}
		return -1;
	}
	
	public static long getSDCardTotalSize() {
		// 需要判断手机上面SD卡是否插好，如果有SD卡的情况下，我们才可以访问得到并获取到它的相关信息，当然以下这个语句需要用if做判断
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 取得sdcard文件路径
			File path = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(path.getPath());
			// 获取block的SIZE
			long blocSize = statfs.getBlockSize();
			// 获取BLOCK数量
			long totalBlocks = statfs.getBlockCount();
			// 空闲的Block的数量
			long availaBlock = statfs.getAvailableBlocks();
			// 计算总空间大小和空闲的空间大小
			// 存储空间大小跟空闲的存储空间大小就被计算出来了。
			long availableSize = blocSize * availaBlock;
			// (availableBlocks * blockSize)/1024 KIB 单位
			// (availableBlocks * blockSize)/1024 /1024 MIB单位
			return blocSize * totalBlocks;
		}
		return -1;
	}
	
	

	// 格式化大小
	public static String FormatFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("0.00");
		if (fileSize == 0) { return "0K"; }
		String result = "";
		if (fileSize < 1024) {
			result = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			result = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			result = df.format((double) fileSize / 1048576) + "M";
		} else {
			result = df.format((double) fileSize / 1073741824) + "G";
		}
		return result.replace(".00B", "B").replace(".00K", "K").replace(".00M", "M").replace(".00G", "G");
	}

	// 获取手机可用内存和总内存
	public String getSystemMemory(Context context) {
		/*
		 * 在android开发中，有时候我们想获取手机的一些硬件信息，比如android手机的总内存和可用内存大小。这个该如何实现呢？
		 * 通过读取文件"/proc/meminfo" 的信息能够获取手机Memory的总量，而通过ActivityManager.getMemoryInfo
		 * (ActivityManager.MemoryInfo)方法可以获取当前的可用Memory量。
		 * "/proc/meminfo"文件记录了android手机的一些内存信息
		 * ，在命令行窗口里输入"adb shell"，进入shell环境，输入
		 * "cat /proc/meminfo"即可在命令行里显示meminfo文件的内容，具体如下所示。
		 * 
		 * C:\Users\Figo>adb shell # cat /proc/meminfo cat /proc/meminfo
		 * MemTotal: 94096 kB
		 * MemFree: 1684 kB Buffers: 16 kB Cached: 27160 kB
		 * SwapCached: 0 kB Active: 35392 kB Inactive: 44180 kB
		 * Active(anon): 26540 kB Inactive(anon): 28244 kB Active(file): 8852 kB
		 * Inactive(file): 15936 kB Unevictable: 280 kB Mlocked: 0 kB * SwapTotal: 0 kB SwapFree: 0 kB Dirty: 0 kB Writeback: 0 kB AnonPages: 52688 kB
		 * Mapped: 17960 kB Slab: 3816 kB SReclaimable: 936 kB SUnreclaim: 2880
		 * kB PageTables: 5260 kB NFS_Unstable: 0 kB Bounce: 0 kB
		 * WritebackTmp: 0 kB 290. * CommitLimit: 47048 kB
		 * Committed_AS: 1483784 kB 292. * VmallocTotal: 876544 kB
		 * VmallocUsed: 15456 kB 294. * VmallocChunk: 829444 kB #
		 * 
		 * 下面先对"/proc/meminfo"文件里列出的字段进行粗略解释： MemTotal: 所有可用RAM大小。 MemFree:
		 * LowFree与HighFree的总和，被系统留着未使用的内存。 Buffers: 用来给文件做缓冲大小。 Cached:
		 * 被高速缓冲存储器（cache memory）用的内存的大小（等于diskcache minus SwapCache）。
		 * SwapCached:被高速缓冲存储器（cache
		 * memory）用的交换空间的大小。已经被交换出来的内存，仍然被存放在swapfile中，
		 * 用来在需要的时候很快的被替换而不需要再次打开I/O端口。 Active:
		 * 在活跃使用中的缓冲或高速缓冲存储器页面文件的大小，除非非常必要，否则不会被移作他用。 Inactive:
		 * 在不经常使用中的缓冲或高速缓冲存储器页面文件的大小，可能被用于其他途径。 SwapTotal: 交换空间的总大小。 SwapFree:
		 * 未被使用交换空间的大小。 Dirty: 等待被写回到磁盘的内存大小。 Writeback: 正在被写回到磁盘的内存大小。
		 * AnonPages：未映射页的内存大小。 Mapped: 设备和文件等映射的大小。 Slab:
		 * 内核数据结构缓存的大小，可以减少申请和释放内存带来的消耗。 SReclaimable:可收回Slab的大小。
		 * SUnreclaim：不可收回Slab的大小（SUnreclaim+SReclaimable＝Slab）。
		 * PageTables：管理内存分页页面的索引表的大小。 NFS_Unstable:不稳定页表的大小。
		 * 要获取android手机总内存大小，只需读取"/proc/meminfo"文件的第1行，并进行简单的字符串处理即可。
		 */
		// 手机的内存信息主要在/proc/meminfo文件中，其中第一行是总内存，而剩余内存可通过ActivityManager.MemoryInfo得到。
		String availMemory = getAvailMemory(context);
		String totalMemory = getTotalMemory(context);
		return "可用内存=" + availMemory + "\n" + "总内存=" + totalMemory;
	}

	private String getAvailMemory(Context context) {
		// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	private String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

}
