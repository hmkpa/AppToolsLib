package android.hmm.lib.widget;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hmm.lib.R;
import android.hmm.lib.tclass.IThreadTask;
import android.hmm.lib.threadpool.QueueTask;
import android.hmm.lib.utils.ToastHelper;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

/********************************
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-15
 * Description:  
 *******************************/
public class LoadingView {

	private static String imageViewtag = "LoadingViewTag";
	
	public static final int type_order = 1;//队列中只有一个任务,执行完此任务,才能执行下一个
	public static final int type_fast = type_order + 1;//移除队列前面的任务,立即执行最后添加的任务
	
	private int mTaskType = type_order;
	private Context context;
	private Handler handler;
	
	private FrameLayout layout = null;
	private ImageView mLoadingView = null;
	
	private QueueTask mQueueTask;
	private ExecutorService singleThreadExecutor;
	
	public LoadingView(Context context, Handler handler, FrameLayout layout) {
		this.handler = handler;
		this.layout = layout;
		this.context = context;
	}
	
	public void setTaskType(int taskType){
		this.mTaskType = taskType;
	}
	
	public void setParentLayout(FrameLayout layout){
		this.layout = layout;
	}

	/**
	 * 开始任务
	 * @param activity
	 * @param handler
	 * @param task
	 * exampler:
	  
	   submitTask(new Task() { 
			public void process() {
				loadVideo();
			} 
			public void handleResult() {
				initView();
			} 
			public void handleError() {
				showMessage("加载数据失败");
				finish();
			}
		});
		
	 */
	public boolean submitTask(final IThreadTask task) {
		switch (mTaskType) {
		case type_order:
			if(singleThreadExecutor == null){
				singleThreadExecutor = Executors.newSingleThreadExecutor();
			}
			if (!singleThreadExecutor.isTerminated()) {
				ToastHelper.showCustomToast(context, "wait a moment, is busy now");
				return false;
			}
			showLoadingView();
			task.prepare();
			singleThreadExecutor.submit(new Runnable() {
				
				@Override
				public void run() {
					singleThreadExecutor.shutdown();
					// TODO Auto-generated method stub
					task.process();
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							task.handleResult();
							closeLoadingView();
						}
					});
				}
			});
			return true;
		case type_fast:
			if(null == mQueueTask){
				mQueueTask = new QueueTask(context, handler);
//				mQueueTask.start();
			}
			mQueueTask.addTask(task);
		default:
			task.handleError();
			break;
		}
		return false;
	}

	/**
	 * 显示加载中控件
	 * @param activity
	 */
	public void showLoadingView() {
		if (null == getLoadingView(layout)) return;
		getLoadingView(layout).bringToFront();
		getLoadingView(layout).requestLayout();
		getLoadingView(layout).setVisibility(View.VISIBLE);
	}

	/**
	 * 关闭加载中控件
	 * @param activity
	 */
	public void closeLoadingView() {
		if (null == getLoadingView(layout)) return;
		getLoadingView(layout).setVisibility(View.GONE);
	}

	/**
	 * 获取加载中控件的显示状态
	 * @param activity
	 */
	public synchronized boolean isLoadingViewShow() {
		if (null == getLoadingView(layout)) return false;
		return (getLoadingView(layout).getVisibility() == View.VISIBLE) ? true : false;
	}

	/**
	 * 取得加载中控件
	 * @param object
	 */
	private View getLoadingView(Object object) {
		if(null != mLoadingView){
			return mLoadingView;
		}
		
		if (null == object) { return null; }

//		FrameLayout layout = null;
//		Context context = null;
//		if (object instanceof Activity) {
//			context = ((Activity) object).getApplicationContext();
//			layout = (FrameLayout) ((Activity) object).findViewById(android.R.id.content);
//		} else if (object instanceof Dialog) {
//			context = ((Dialog) object).getContext();
//			layout = (FrameLayout) ((Dialog) object).findViewById(android.R.id.content);
//		} else if (object instanceof View) {
//			context = ((View) object).getContext();
//			layout = (FrameLayout) ((View) object).findViewById(android.R.id.content);
//			
//			layout = (FrameLayout) ((View) object).getParent();
//		}
//
//		if(null == layout){
//			return layout;
//		}

		for (int i = 0; i < layout.getChildCount(); i++) {
			View view = layout.getChildAt(i);
			if (view instanceof ImageView) {
				if (view.getTag() != null && imageViewtag.equals(view.getTag())) { 
					mLoadingView = (ImageView) view; 
					return view;
				}
			}
		}
		
		mLoadingView = new ImageView(context);
		mLoadingView.setTag(imageViewtag);
		mLoadingView.setImageResource(R.anim.loading_progress);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		layout.addView(mLoadingView, lp);
		try {
			Drawable drawable = mLoadingView.getDrawable();
			Method method = drawable.getClass().getMethod("start");
			method.invoke(drawable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mLoadingView.setVisibility(View.GONE);
		return mLoadingView;
	}
	
	public void destoryData(){
		if(singleThreadExecutor != null){
			singleThreadExecutor.shutdownNow();
		}
		if(mQueueTask != null){
			mQueueTask.destoryData();
		}
	}

}
