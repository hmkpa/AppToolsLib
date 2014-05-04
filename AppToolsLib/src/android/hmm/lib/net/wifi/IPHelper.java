package android.hmm.lib.net.wifi;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.hmm.lib.http.HtmlHelper;
import android.hmm.lib.model.IP;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-18
 * Description:  
 */
public class IPHelper {

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof java.net.Inet4Address)) { 
						return inetAddress.getHostAddress(); 
					}
				}
				
				/**
				 * 
				 * 
				 * getCanonicalHostName方法获得主机名
				 * address.getHostName()主机别名
				 * ip地址构建的InetAddress，两个方法效果就一样
				 * 
				 * 
				 * 	InetAddress address = InetAddress.getLocalHost();
					System.out.println(address.getHostName());  // 输出本机名,time=0
					
					
					用域名作为getByName和getAllByName方法的参数调用这两个方法后，系统会自动记住这个域名。
					当调用getHostName方法时，就无需再访问DNS服务器，而是直接将这个域名返回。如下
					InetAddress address = InetAddress.getByName("www.oracle.com");
					System.out.println(address.getHostName());  //无需访问DNS服务器，直接返回域名,time=0
					
					使用IP地址创建InetAddress对象时(getByName、getAllByName和getByAddress方法
					都可以通过IP地址创建InetAddress对象)，
					并不需要访问DNS服务器。因此，通过DNS服务器查找域名的工作就由getHostName方法来完成。
					如果这个IP地址不存在或DNS服务器不允许进行IP地址和域名的映射，
					getHostName方法就直接返回这个IP地址。如下面的代码所示：
					InetAddress address = InetAddress.getByName("141.146.8.66");
					System.out.println(address.getHostName());  // 需要访问DNS服务器才能得到域名
					InetAddress address = InetAddress.getByName("1.2.3.4");  // IP地址不存在
					System.out.println(address.getHostName());  //直接返回IP地址
				 */
				
				
				
				/**
				 * 	
				  	InetAddress address=InetAddress.getByName("www.baidu.com");
         			System.out.println(address);
         			System.out.println("-----");
         			InetAddress[] addresses=InetAddress.getAllByName("www.baidu.com");
        			for(InetAddress addr:addresses) {
             			System.out.println(addr);
         			}
         			
         			运行结果如下：
					www.baidu.com/119.75.213.61
					-----
					www.baidu.com/119.75.213.61
					www.baidu.com/119.75.216.30
					在这个例子中，我们使用到了getByName()以及getAllByName()两个方法，
					前者通过"www.baidu.com"来获取InetAddress的对象，并且输出到控制台。
					System.out.println(address); 默认调用了InetAddress.toString()方法，
					在结果中可以看到"www.baidu.com/119.75.213.61"的输出结果，
					其中119.75.213.61为百度网站的IP地址。
					getAllByName()方法是根据主机名返回其可能的所有InetAddress对象，保存在一个数组中。
					在这个例子中，输出的结果中，www.baidu.com有两个ip地址
					分别为119.75.213.61以及119.75.216.30。
					
				 */
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查ip输入的格式是否正确
	 * @param value
	 * @return
	 */
	public static boolean isIpAddress(String value) {
		if (value == null) { return false; }
		int start = 0;
		int end = value.indexOf('.');
		int numBlocks = 0;

		// IP ends with '.' is invalid
		if (value.startsWith(".") || value.endsWith(".")) { return false; }

		while (start < value.length()) {
			if (end == -1) {
				end = value.length();
			}

			try {
				int block = Integer.parseInt(value.substring(start, end));
				if ((block > 255) || (block < 0)) { return false; }
			} catch (NumberFormatException e) {
				return false;
			}

			numBlocks++;
			start = end + 1;
			end = value.indexOf('.', start);
		}

		return numBlocks == 4;
	}

	/************************************************************************************
	 * 
	 ************************************************************************************/
	private static final String IP138_URL = "http://iframe.ip138.com/ic.asp";
	private static final String IPQQ_URL = "http://ip.qq.com/cgi-bin/index";

	// 获取IP和宽带运行商
	public static IP getIPInfo() {
		IP ip = new IP();
		String html = "";
		try {
			html = HtmlHelper.getHtml(IPQQ_URL, "gb2312", 10);
			ip = getIP4QQ(html);

			if (ip.getIp() == null || ip.getProvider() == null) { throw new Exception(); }
		} catch (Exception e) {
			try {
				html = HtmlHelper.getHtml(IP138_URL, "gb2312", 10);
				ip = getIP4IP138(html);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		return ip;
	}

	private static IP getIP4IP138(String html) {
		IP ip = new IP();

		String reg = "<center>您的IP是：(.*?) 来自：(.*?)</center>";
		Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html);
		if (m.find()) {
			ip.setIp(m.group(1).replace("[", "").replace("]", ""));
			format(m.group(2), ip);
		}
		return ip;
	}

	private static IP getIP4QQ(String html) {
		IP ip = new IP();

		String reg = "您当前的IP为：<span class=.red.>(.*?)</span>";
		Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html);
		if (m.find()) {
			ip.setIp(m.group(1));
		}

		reg = "该IP所在地为：<span>(.*?)</span>";
		p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		m = p.matcher(html);
		if (m.find()) {
			format(m.group(1), ip);
		}
		return ip;
	}

	private static void format(String address, IP ip) {
		address = address.replace("&nbsp;", " ");
		String city = address.substring(0, address.lastIndexOf(" ")).trim();
		String provider = address.substring(address.lastIndexOf(" ") + 1).trim();
		ip.setProvider(provider);
		ip.setCity(city);
	}

	// ip地址转换
	public static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}
	
	// 获取IP地址
	public static String StringizeIp(int ip) {
		int ip4 = (ip >> 24) & 0x000000FF;
		int ip3 = (ip >> 16) & 0x000000FF;
		int ip2 = (ip >>  8) & 0x000000FF;
		int ip1 = ip & 0x000000FF;
		return Integer.toString(ip1) + "." + ip2 + "." + ip3 + "." + ip4;
	}
}
