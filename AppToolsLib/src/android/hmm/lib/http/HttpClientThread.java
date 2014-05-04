package android.hmm.lib.http;

import java.io.IOException; 
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;  


//import com.google.gson.Gson;

public class HttpClientThread extends Thread { 
	
	public static final int SUCCESS = 1;
	public static final int FAILED = 2;
	public static final int TIMEOUT = 3;
	
	public String mUrl; 
	private Type mType; 
	private boolean mStart ;
	public HttpClientThread() {
		this("",null,true);
	}
	public HttpClientThread(String url) {
		this(url,null,true);
	}
	public HttpClientThread(String url,Type type) {
		this(url,type,true);
	}
	public HttpClientThread(String url,Type type,boolean start) {
		this.mType = type;
		this.mUrl = url;
		this.mStart = start;
		if(this.mStart == true) {
			this.start();
		}
	} 
	public void run () {   
		int result = FAILED;
		String responseText = "";
		try {
			HttpClientHelper client = new HttpClientHelper(mUrl);
			HttpResponse response = client.execute();
			if(response.getStatusLine().getStatusCode() == 200) { 
				responseText = EntityUtils.toString(response.getEntity(), "UTF-8"); 
				responseText = tryRun(responseText);
				result = SUCCESS; 
				handleResult(responseText);
			} 
		} catch (IOException e) {
			e.printStackTrace();
			if (e instanceof ConnectTimeoutException) { 
				result = TIMEOUT;
			} 
		} 
		if(result == SUCCESS) {
			return;
		}
		handleError(result);
	}
	
	//遇到wep的加密方式尝试再运行一次
	private String tryRun(String responseText) throws IOException {
		if (responseText != null && responseText.indexOf("<script>") > 0
				&& responseText.indexOf("TPSecNotice") > 0) {
			String sep = mUrl.indexOf("?") == -1 ? "?" : "&";
			String newUrl = mUrl + sep + "TPSecNotice";

			HttpClientHelper client = new HttpClientHelper(newUrl);
			HttpResponse response = client.execute();
			if (response.getStatusLine().getStatusCode() == 200) {
				responseText = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		}
		return responseText;
	}
	
	private void handleResult(String responseText) {
		handleJson(responseText);
		handleObject(responseText);
		if(mType != null) {
			//Object obj = new Gson().fromJson(json, mType);
			//handleObject(obj);
		}
	} 
	public void handleJson(String json) {
		
	}
	public void handleObject(Object object) {
		
	}
	public void handleError(int result) {
		
	}
}