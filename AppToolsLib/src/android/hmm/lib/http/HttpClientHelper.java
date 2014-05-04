package android.hmm.lib.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.hmm.lib.utils.InputStreamUtils;


public class HttpClientHelper implements HttpClient {

	private static final int defaultTimeout = 30;
	private static final int defaultSoTimeout = 10;
	private int mTimeout;
	private int mSoTimeout;
	private String mUrl;
	private HttpClient delegate;
	private long mContentLength;

	public HttpClientHelper() {
		this("");
	}

	public HttpClientHelper(String url) {
		this(url, defaultTimeout);
	}

	public HttpClientHelper(String url, int timeout) {
		this(url, timeout, defaultSoTimeout);
	}

	public HttpClientHelper(String url, int timeout, int soTimeout) {
		this.mUrl = url;
		this.mTimeout = timeout;
		this.mSoTimeout = soTimeout;
		this.delegate = new DefaultHttpClient() {
			protected HttpParams createHttpParams() {
				HttpParams params = super.createHttpParams();
				HttpConnectionParams.setConnectionTimeout(params, mTimeout * 1000);
				HttpConnectionParams.setSoTimeout(params, mSoTimeout * 1000);
				return params;
			}
		};
	}

	public HttpParams getParams() {
		return delegate.getParams();
	}

	public ClientConnectionManager getConnectionManager() {
		return delegate.getConnectionManager();
	}

	public String downloadString() throws IOException {
		return downloadString(this.mUrl, "UTF-8");
	}

	public String downloadString(String url) throws IOException {
		return downloadString(url, "UTF-8");
	}

	public String downloadString(String url, String charset) throws IOException {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toString(response.getEntity(), charset);
		}
		return "";
	}

	public void downloadFile(String path) throws IOException, HttpException {
		downloadFile(this.mUrl, path);
	}

	public void downloadFile(String url, String path) throws IOException, HttpException {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream is = response.getEntity().getContent();
			InputStreamUtils.saveInputStreamToFile(is, path);
		}
	}

	public byte[] downloadByte(String url) throws IOException {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toByteArray(response.getEntity());
		}
		return null;
	}

	public long getContentLength() {
		return mContentLength;
	}

	public InputStream getContent() throws Exception {
		return getContent(this.mUrl);
	}

	public InputStream getContent(String url) throws Exception {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			mContentLength = response.getEntity().getContentLength();
			return response.getEntity().getContent();
		}
		return null;
	}

	public HttpResponse execute() throws IOException {
		HttpUriRequest request = new HttpGet(this.mUrl);
		return execute(request);
	}

	public HttpResponse post() throws IOException {
		HttpUriRequest request = new HttpPost(this.mUrl);
		return execute(request);
	}

	// 
	public HttpResponse execute(HttpUriRequest request) throws IOException {
		return delegate.execute(request);
	}

	public void close() {
		getConnectionManager().shutdown();
	}

	public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
		return delegate.execute(request, context);
	}

	public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
		return delegate.execute(target, request);
	}

	public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
		return delegate.execute(target, request, context);
	}

	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
		return delegate.execute(request, responseHandler);
	}

	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
		return delegate.execute(request, responseHandler, context);
	}

	public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
		return delegate.execute(target, request, responseHandler);
	}

	public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
		return delegate.execute(target, request, responseHandler, context);
	}
}