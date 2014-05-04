package android.hmm.lib.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;


import android.content.Context;
import android.hmm.lib.net.wifi.CheckNetHelper;
import android.hmm.lib.utils.StorageHelper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpDownloadThread extends Thread {
	private static final int BUFFER_SIZE = 4096;
	// 空间不足
	private static final int STORE_FAILED = 0;
	// 连接不上网络
	private static final int NETWORK_FAILED = 1;
	// 下载中
	private static final int DOWNLOAD_WORKING = 1;
	// 暂停
	private static final int DOWNLOAD_SUSPEND = 2;
	// 重新连接
	private static final int DOWNLOAD_RESTART = 3;
	// 停止
	private static final int DOWNLOAD_STOP = 4;
	// 空间不够是否已经提示
	protected static boolean noSpaceAlert = false;
	// 连不上网上是否已经提示
	protected static boolean noNetworkAlert = false;
	//
	private Object mControl = new Object();
	// 状态
	private int mStatus;
	// 出错次数
	private int mError = 0;
	private Context mContext;
	private String mPath;
	private String mUrl;
	private RandomAccessFile mFile;
	private InputStream mStream;
	private long mCompelete = 0;
	private long mSize;

	public long getCompelete() {
		return mCompelete;
	}

	public HttpDownloadThread(Context context, String url, String path) {
		this.mContext = context;
		this.mUrl = url;
		this.mPath = path;
		noSpaceAlert = false;
		noNetworkAlert = false;
	}

	// 暂停
	public void suspendEx() {
		this.mStatus = DOWNLOAD_SUSPEND;
	}

	// 继续
	public void resumeEx() {
		this.mStatus = DOWNLOAD_WORKING;
		synchronized (mControl) {
			mControl.notifyAll();
		}
	}

	// 停止
	public void stopEx() {
		if (mStatus == DOWNLOAD_SUSPEND) {
			resumeEx();
		}
		this.mStatus = DOWNLOAD_STOP;
	}

	public void run() {
		this.onPrepare();
		try {
			mFile = new RandomAccessFile(mPath, "rwd");
			if (mCompelete > 0) {
				mFile.seek(mCompelete);
			}
			mStream = getContent(mCompelete);
			if (mSize <= 0) {
				this.onDownloadFailed(mCompelete, false);
				return;
			}
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true) {
				// 停止
				if (this.mStatus == DOWNLOAD_STOP) {
					break;
				}
				long filesize = mSize - mCompelete;
				// 空间不够
				if (filesize > StorageHelper.getSDCardAvailaSize()) {
					mHandler.sendEmptyMessage(STORE_FAILED);
					continue;
				}
				noSpaceAlert = false;
				// 网络断开
				if (CheckNetHelper.checkNetwork(mContext) == false) {
					this.mStatus = DOWNLOAD_RESTART;
					mHandler.sendEmptyMessage(NETWORK_FAILED);
					continue;
				}
				noNetworkAlert = false;
				// 暂停
				synchronized (mControl) {
					if (this.mStatus == DOWNLOAD_SUSPEND) {
						try {
							mControl.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
//				// 重新连接
//				if (this.mStatus == DOWNLOAD_RESTART) {
//					this.mStatus = DOWNLOAD_WORKING;
//					if (mDialog != null && mDialog.isShowing()) {
//						mDialog.dismiss();
//					}
//					this.onDownloadFailed(mCompelete, true);
//					break;
//				}
				try {
					int len = -1;
					len = mStream.read(buffer);
					if (mCompelete >= mSize) {
						break;
					}
					mFile.write(buffer, 0, len);
					mCompelete += len;
					onDownloading(len, mCompelete, mSize);
					if (len == -1) {
						mError++;
						if (mError >= 3) {
							this.onDownloadFailed(mCompelete, false);
							break;
						}
					} else {
						mError = 0;
					}
				} catch (Exception e) {
					mError++;
					if (mError >= 3) {
						this.onDownloadFailed(mCompelete, false);
						break;
					}
					e.printStackTrace();
				}
			}
			// 下载完成
			if (mCompelete >= mSize) {
				this.onDownloadSuccess(mPath);
			}
		} catch (Exception e) {
			this.onDownloadFailed(mCompelete, false);
			e.printStackTrace();
		} finally {
			try {
				mFile.close();
				if (mStream != null) {
					mStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected InputStream getContent(long start) throws IOException {
		HttpUriRequest request = new HttpGet(this.mUrl);
		if (start > 0) {
			request.setHeader("Range", "bytes=" + start + "-");
		}
		HttpClientHelper client = new HttpClientHelper();
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			mSize = response.getEntity().getContentLength();
			return response.getEntity().getContent();
		}
		return null;
	}

	protected void setUrl(String url) {
		this.mUrl = url;
	}

	protected void onPrepare() {
		File file = new File(mPath);
		if (!file.getParentFile().exists()) {
			boolean b1 = file.getParentFile().mkdirs();
			Log.v("t", b1 + "");
		}
		if (file.exists()) {
			mCompelete = file.length();
		}
	}

	protected void onDownloading(int length, long compelete, long size) {

	}

	protected void onDownloadSuccess(String filepath) {

	}

	protected void onDownloadFailed(long compelete, boolean restart) {

	}

	protected void handleMessage(Message msg) {
//		if (msg.what == NETWORK_FAILED) {
//		if (noNetworkAlert) {
//			return;
//		}
//		noNetworkAlert = true;
//		mDialog = Utils.confirmSetWifi(mContext);
//		return;
//	}
//	if (noSpaceAlert) {
//		return;
//	}
//	noSpaceAlert = true;
//	String info = "SD卡空间只剩下" + SDcardUtils.FormatFileSize(SDcardUtils.getAvailableStore(mPath)) + "，下载需要" + SDcardUtils.FormatFileSize(mSize - mCompelete);
//	DialogUtils.alert(mContext, info);
	}

//	private WoboDialogInterface mDialog;
	// 消息
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			handleMessage(msg);
		}
	};
}