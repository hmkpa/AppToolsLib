package android.hmm.lib.utils;

import java.lang.reflect.Method;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hmm.lib.R;
import android.hmm.lib.tclass.Task;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-15
 * Description:  
 */
public class LoadingControl {

	private static String imageViewtag = "IVLoading";
	private Handler handler;
	private Object loadingViewContext;

	/**
	 * 
	 * @param handler
	 * @param loadingContext:加载中的控件的跟 ,值：Activity,Dialog,View
	 */
	public LoadingControl(Handler handler, Object loadingViewContext) {
		this.handler = handler;
		this.loadingViewContext = loadingViewContext;
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
	public void submitTask(final Task task) {
		showLoadingView();
		new Thread() {
			public void run() {
				process(task);
			}
		}.start();
	}

	/**
	 * 转入主线程
	 * @param handler
	 * @param r
	 */
	private void post(Runnable r) {
		handler.post(r);
	}

	/**
	 * 执行任务
	 * @param activity
	 * @param handler
	 * @param task
	 */
	private synchronized void process(Task task) {
		try {
			task.process();
			handleResult(task);
		} catch (Exception ex) {
			ex.printStackTrace();
			handleError(task);
		}
	}

	/**
	 * 正常结果
	 * @param activity
	 * @param handler
	 * @param task
	 */
	private synchronized void handleResult(final Task task) {
		post(new Runnable() {
			public void run() {
				task.handleResult();
				closeLoadingView();
			}
		});
	}

	/**
	 * 出错结果
	 * @param activity
	 * @param handler
	 * @param task
	 */
	private synchronized void handleError(final Task task) {
		post(new Runnable() {
			public void run() {
				task.handleError();
				closeLoadingView();
			}
		});
	}
	
	public void setLoadingViewContext(Object loadingViewContext) {
		this.loadingViewContext = loadingViewContext;
	}

	/**
	 * 显示加载中控件
	 * @param context
	 */
	public void showLoadingView() {
		if (null == loadingViewContext) return;
		getLoadingView(loadingViewContext).bringToFront();
		getLoadingView(loadingViewContext).requestLayout();
		getLoadingView(loadingViewContext).setVisibility(View.VISIBLE);
	}

	/**
	 * 关闭加载中控件
	 * @param context
	 */
	public void closeLoadingView() {
		if (null == loadingViewContext) return;
		getLoadingView(loadingViewContext).setVisibility(View.GONE);
	}

	/**
	 * 获取加载中控件的显示状态
	 * @param context
	 */
	public synchronized boolean isLoadingViewShow() {
		if (null == loadingViewContext) return false;
		return (getLoadingView(loadingViewContext).getVisibility() == View.VISIBLE) ? true : false;
	}

	/**
	 * 取得加载中控件
	 * @param object
	 */
	private View getLoadingView(Object object) {
		if (null == object) { return null; }

		FrameLayout layout = null;
		Context context = null;
		if (object instanceof Activity) {
			context = ((Activity) object).getApplicationContext();
			layout = (FrameLayout) ((Activity) object).findViewById(android.R.id.content);
		} else if (object instanceof Dialog) {
			context = ((Dialog) object).getContext();
			layout = (FrameLayout) ((Dialog) object).findViewById(android.R.id.content);
		} else if (object instanceof View) {
			context = ((View) object).getContext();
			layout = (FrameLayout) ((View) object).findViewById(android.R.id.content);
		} else {
			return null;
		}

		for (int i = 0; i < layout.getChildCount(); i++) {
			View view = layout.getChildAt(i);
			if (view instanceof ImageView) {
				if (view.getTag() != null && imageViewtag.equals(view.getTag())) { return view; }
			}
		}
		

		ImageView mLoadingView = new ImageView(context);
		mLoadingView.setTag(imageViewtag);
		mLoadingView.setImageResource(R.anim.loading_progress);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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

}
