package android.box.tools.demo;

import java.util.ArrayList;
import java.util.List;

import android.box.tools.R;
import android.content.Context;
import android.hmm.lib.tclass.Task;
import android.hmm.lib.utils.LoadingControl;
import android.hmm.lib.widget.BaseViewPager;
import android.hmm.lib.widget.ScrollGridView;
import android.hmm.lib.widget.ScrollListView;
import android.os.AsyncTask;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-28
 * Description:  
 */
public class ListViewItemFlyIn extends BaseViewPager {

	private List<String> mAppList = new ArrayList<String>();
	private int EVENT_SHOW_PROGRESSBAR = 1;
	private int EVENT_CLOSE_PROGRESSBAR = EVENT_SHOW_PROGRESSBAR + 1;
	private boolean isStart = false;
	private LoadingControl mLoadingControl;

	public ListViewItemFlyIn(Context context) {
		super(context);
		addViews();
		super.initViews();
		mLoadingControl = new LoadingControl(handler, getContext());
	}

	private void addViews() {
		ScrollListView sListView = new ScrollListView(getContext());
		sListView.setSelector(android.R.color.transparent);
		addObjectView(sListView);

		ScrollGridView sGridView = new ScrollGridView(getContext());
		sGridView.setSelector(android.R.color.transparent);
		addObjectView(sGridView);
	}

	@SuppressWarnings("unchecked")
	private void updateListView(AbsListView view) {
		setAnimationOfView(view);
		ArrayAdapter<String> mAdapter = (ArrayAdapter<String>) view.getAdapter();
		if (null == mAdapter) {
			mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mAppList);
			view.setAdapter(mAdapter);
			view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					// TODO Auto-generated method stub
					mAppList.remove(position);
					((ArrayAdapter<String>) parent.getAdapter()).notifyDataSetChanged();
				}
			});
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private void setAnimationOfView(AbsListView view) {
		/**
		 * 每次刷新数据时，都需要设置，不然只在第一次才会出现动画
		 */
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.list_item_animation_1);
		// 得到一个LayoutAnimationController对象；
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		// 设置控件显示的顺序；
		lac.setOrder(LayoutAnimationController.ORDER_REVERSE);
		// 设置控件显示间隔时间；
		lac.setDelay(0.2f);
		// 为ListView设置LayoutAnimationController属性；
		view.setLayoutAnimation(lac);
	}

	public void handleRecieveMessage(Message msg) {
		if (msg.what == EVENT_CLOSE_PROGRESSBAR) {
			mLoadingControl.closeLoadingView();
		} else if (msg.what == EVENT_SHOW_PROGRESSBAR) {
			mLoadingControl.showLoadingView();
		} else if (msg.what == EVENT_PAGE_CHANGE) {
			if (!isStart) { return; }
			start2Loading();
		}
	}
	
	public void start2Loading() {
		mLoadingControl.submitTask(new Task() {

			@Override
			public void process() {
				// TODO Auto-generated method stub
				initData();
			}

			@Override
			public void handleResult() {
				// TODO Auto-generated method stub
				updateListView((AbsListView) getCurrentItemView());
				mLoadingControl.closeLoadingView();
			}

			@Override
			public void handleError() {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void initData(){
		mAppList.clear();
		int i = 1;
		while (i < 11) {
			mAppList.add("item" + i);
			SystemClock.sleep(200);
			i++;
		}
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
		start2Loading();
	}

	public boolean isStart() {
		return isStart;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		isStart = false;
		mLoadingControl.closeLoadingView();
		super.onDetachedFromWindow();
	}

	public void setLoadingViewContext(Object loadingViewContext) {
		mLoadingControl.setLoadingViewContext(loadingViewContext);
	}

	protected class ChapTask extends AsyncTask<Integer, Integer, Long> {
		protected void onPreExecute() {
			handler.sendEmptyMessage(EVENT_SHOW_PROGRESSBAR);
		}

		protected Long doInBackground(Integer... params) {
			initData();
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
//			mAdapter.notifyDataSetChanged();
//			updateListView();
		}

		protected void onPostExecute(Long result) {
			handler.sendEmptyMessage(EVENT_CLOSE_PROGRESSBAR);
		}
	}

}
