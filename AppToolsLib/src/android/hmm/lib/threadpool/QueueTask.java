package android.hmm.lib.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.hmm.lib.tclass.IThreadTask;
import android.hmm.lib.utils.LogHelper;
import android.os.Handler;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014年5月10日
 * Description:  
 */
public class QueueTask extends Thread {

	private QueueTask mQueueTask;
	private boolean isInterrupted;
	private boolean isRunning = true;
	private List<DateTask> listDateTask;
	private int checkFrequency = 100;// 毫秒
	private ScheduledFuture<?> mSFInterrupt;
	private ScheduledFuture<?> mSFCheck;
	private LogHelper mLogHelper;
	private boolean bWaiting = true;
	private Handler handler;
	protected Context context;
	private boolean isFirstStart = true;
	private static int taskOrder = 0;//任务的序列号，递增的

	public QueueTask(Context context, Handler handler) {
		mQueueTask = this;
		this.context = context;
		this.handler = handler;
		mLogHelper = new LogHelper(this.getName());
	}

	public void addTask(final IThreadTask task) {
		mLogHelper.i("------> addTask");
		task.prepare();

		if (mSFCheck != null && !mSFCheck.isCancelled()) {
			isInterrupted = true;
			cancleScheduledFuture(mSFCheck);
		}
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		mSFCheck = ses.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mLogHelper.i("------> checking mSFCheck");
				if (bWaiting) {
					if (null == listDateTask) {
						listDateTask = new ArrayList<DateTask>();
					}
					listDateTask.add(new DateTask(task));
					cancleScheduledFuture(mSFCheck);
					mLogHelper.i("------> cancleScheduledFuture mSFCheck");

					synchronized (mQueueTask) {
						if (isFirstStart) {
							mQueueTask.start();
							isFirstStart = false;
						} else {
							throwException(mQueueTask);
						}
						mQueueTask.notify();
					}
					return;
				} else {
					throwException(mQueueTask);
					
					isInterrupted = true;
				}
			}
		}, 0, checkFrequency, TimeUnit.MILLISECONDS);
	}

	private void cancleScheduledFuture(ScheduledFuture<?> sf) {
		if (null != sf && !sf.isCancelled()) {
			sf.cancel(true);
			sf = null;
		}
	}

	public void destoryData() {
		mLogHelper.i("------> destoryData");
		if (null != listDateTask) {
			listDateTask = null;
		}
		if (null != mLogHelper) {
			mLogHelper = null;
		}

		cancleScheduledFuture(mSFCheck);
		cancleScheduledFuture(mSFInterrupt);
		mSFCheck = null;
		mSFInterrupt = null;

		isRunning = false;
		isInterrupted = false;
		bWaiting = true;
		
		throwException(mQueueTask);
		mQueueTask = null;
	}

	private void progressWork() throws Exception {
		mLogHelper.i("------> progressWork");
		bWaiting = false;
		isInterrupted = false;

		startInterruptScheduled();
		
		while (listDateTask.size() > 0) {
			final DateTask mDateTask = listDateTask.get(listDateTask.size() - 1);
			listDateTask.clear();
			mDateTask.getIThreadTask().process();//会执行很长时间
			if (listDateTask.size() == 0) {
				cancleScheduledFuture(mSFInterrupt);
				mLogHelper.i("------> run over cancleScheduledFuture mSFInterrupt");
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mLogHelper.i("------> taskOrder is =" + taskOrder+",DateTask.getOrder"+ mDateTask.getOrder());
						if(taskOrder != mDateTask.getOrder()){
							return;
						}
						mDateTask.getIThreadTask().handleResult();
					}
				});
			}
		}
	}
	
	protected void startInterruptScheduled(){
//		cancleScheduledFuture(mSFInterrupt);
//		mLogHelper.i("------> cancleScheduledFuture mSFInterrupt");
		
		ScheduledExecutorService mScheduledExecutorService = null;
		if (mScheduledExecutorService == null) {
			mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		}

//		ScheduledFuture<?> mScheduledFuture = mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//			}
//		}, 10, 30, TimeUnit.SECONDS);

		mSFInterrupt = mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isInterrupted) {
					isInterrupted = false;
					cancleScheduledFuture(mSFInterrupt);
					mLogHelper.i("------> throw cancleScheduledFuture mSFInterrupt");
					throwException();
					return;
				}
				if (bWaiting) {
					cancleScheduledFuture(mSFInterrupt);
					mLogHelper.i("------> bwaiting cancleScheduledFuture mSFInterrupt");
					return;
				}
//				mLogHelper.i("------> SFInterrupt checking isInterrupted=" + isInterrupted);
			}
		}, 0, checkFrequency, TimeUnit.MILLISECONDS);
	}

	private int throwException() {
		mLogHelper.i("------> throwException,taskOrder is =" + taskOrder);
//		throw new Exception();
		String str = null;
		return str.length();
	}

	private void throwException(QueueTask mQueueTask) {
		synchronized (this.mQueueTask) {
			try {
				bWaiting = true;
				this.mQueueTask.sleep(5);
				this.mQueueTask.interrupt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		destoryData();
		super.destroy();
	}

	@Override
	public void run() {
		while (isRunning) {
			mLogHelper.i("------> run");
			try {
				progressWork();
			} catch (Exception e1) {
//				e1.printStackTrace();
			}

			synchronized (mQueueTask) {
				try {
					mLogHelper.i("------> wait");
					bWaiting = true;
					isInterrupted = false;
					mQueueTask.wait();
				} catch (InterruptedException e) {
//					e.printStackTrace();
					cancleScheduledFuture(mSFInterrupt);
				}
			}
		}
		mLogHelper.i("------> run over");
	}

	private class DateTask {
		private  int order;
		private long recordTime;
		private IThreadTask iThreadTask;

		public DateTask(IThreadTask iThreadTask) {
			synchronized (mQueueTask) {
				taskOrder = taskOrder+1;
				if(taskOrder > Integer.MAX_VALUE){
					taskOrder = 0;
				}
				setOrder(taskOrder);
				mLogHelper.i("------> create DateTask, the taskOrder="+getOrder());
			}
			setRecordTime(System.currentTimeMillis());
			this.setIThreadTask(iThreadTask);
		}

		public long getRecordTime() {
			return recordTime;
		}

		public void setRecordTime(long recordTime) {
			this.recordTime = recordTime;
		}

		protected IThreadTask getIThreadTask() {
			return iThreadTask;
		}

		public void setIThreadTask(IThreadTask iThreadTask) {
			this.iThreadTask = iThreadTask;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}
	}

}
