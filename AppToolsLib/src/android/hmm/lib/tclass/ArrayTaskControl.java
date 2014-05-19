package android.hmm.lib.tclass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.os.Handler;
import android.util.Log;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014年5月5日
 * Description:  
 */
public class ArrayTaskControl {

	private long IntervalTime = 1500;
	private Handler mHandler;
	private boolean isRunning = false;
	private List<TaskCell> tasks = new ArrayList<TaskCell>();
	private Process mProcess;
	public static int MSG_RESPOND_COMMAND = 0x1010;

	public ArrayTaskControl(Handler handler) {
		isRunning = true;
		mHandler = handler;
		mProcess = new Process();
		mProcess.start();
	}

	public void addTask(int keyType) {
		TaskCell t = new TaskCell();

		if (tasks.size() > 0) {
			tasks.clear();
		}
		tasks.add(t);
	}

	public void starProcess() {
		synchronized (mProcess) {
			mProcess.notify();
		}
	}
	
	public void destory(){
		isRunning = false;
		mHandler.removeMessages(MSG_RESPOND_COMMAND);
		mProcess.stop();
		mProcess.destroy();
	}

	private class Process extends Thread {
		@Override
		public void run() {
			while (isRunning) {
				while (tasks.size() > 0) {
					Log.i("SearchInputHandle", "tasks.size() =" + tasks.size());
					if (needShow(tasks.get(0).getAddDate())) {
						mHandler.sendEmptyMessage(MSG_RESPOND_COMMAND);
						tasks.clear();
					}
				}

				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	private boolean needShow(Date date) {
		long between = (new Date().getTime() - date.getTime());
		Log.i("SearchInputHandle", "between time = " + between);
		if (between >= IntervalTime) {
			return true;
		}
		return false;
	}

	private class TaskCell {
		private Date addDate;

		public TaskCell() {
			this.addDate = new Date();
		}

		public Date getAddDate() {
			return addDate;
		}
	}
	
}
