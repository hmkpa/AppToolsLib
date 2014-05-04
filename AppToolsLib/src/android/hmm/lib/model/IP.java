package android.hmm.lib.model;

public class IP {
	private String ip;
	private String provider;
	private String city;
	
	private String ip4;
	private String ip6;
	private String outsideIp;//外网IP
	private String pcName;//本机名
	private String domainName;//域名
	private String connName;//当前连接的网络名字
	private String gateway;
	private String netmask;
	private String dns1;
	private String dns2;
	private String mac;

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProvider() {
		return provider;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setIp4(String ip4) {
		this.ip4 = ip4;
	}

	public String getIp4() {
		return ip4;
	}

	public void setIp6(String ip6) {
		this.ip6 = ip6;
	}

	public String getIp6() {
		return ip6;
	}

	public void setOutsideIp(String outsideIp) {
		this.outsideIp = outsideIp;
	}

	public String getOutsideIp() {
		return outsideIp;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getConnName() {
		return connName;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getGateway() {
		return gateway;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}

	public String getDns1() {
		return dns1;
	}

	public void setDns2(String dns2) {
		this.dns2 = dns2;
	}

	public String getDns2() {
		return dns2;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMac() {
		return mac;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	public String getPcName() {
		return pcName;
	}
	
	
}
