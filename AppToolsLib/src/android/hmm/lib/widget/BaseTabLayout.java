package android.hmm.lib.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/*********************************************************
 * @Title: BaseTabLayout.java
 * @Package android.hmm.lib.widget
 * @Description: TODO(用一句话描述该文件做什么)
 * @author heming
 * @date May 4, 2014 -- 8:20:08 PM
 * @version V1.0
 *********************************************************/
public class BaseTabLayout extends FrameLayout implements
		android.view.View.OnClickListener, android.view.View.OnTouchListener {

	private List<BodyViewCell> mListBodyView = null;
	private ViewPager mViewPager;
	private RadioGroup mBottomBar;
	private int firstDisplayPosition = 0;
	private int mKeyCode = 0;
	public Activity activity;
	public Dialog dContext;
	public int EVENT_PAGE_CHANGE = 0x5010;

	/*******************************************************
	 * @param context
	 ******************************************************/
	public BaseTabLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity = (Activity) context;
	}

	public void setDialogContext(Dialog context){
		dContext = context;
	}
	
	public void initBaseViews() {
		mViewPager = new ViewPager(getContext());
		
		/* 主要代码段 */
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			// 设置加速度 ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
			FixedSpeedScroller mScroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
			mScroller.setmDuration(1000);
			mField.set(mViewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mBottomBar = new RadioGroup(getContext());
		mBottomBar.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		mBottomBar.setOrientation(LinearLayout.HORIZONTAL);
		mBottomBar.setPadding(0, 0, 0, 10);
		addView(mViewPager);
		addView(mBottomBar);

		for (int i = 0; i < mListBodyView.size(); i++) {
			RadioButton rb = new RadioButton(getContext());
			rb.setFocusable(true);
			rb.setFocusableInTouchMode(true);
			rb.setGravity(Gravity.CENTER);
			mBottomBar.addView(rb);
		}
		mBottomBar.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						handler.sendEmptyMessage(EVENT_PAGE_CHANGE);
					}
				});
		ViewPager.OnPageChangeListener pcl = new ViewPager.OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) {

			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			public void onPageSelected(int position) {
				RadioButton radioButton = (RadioButton) mBottomBar
						.getChildAt(position);
				if (activity.getCurrentFocus() == null
						|| mBottomBar.getFocusedChild() != null) {
					onClick(radioButton);
				}
				radioButton.setChecked(true);
			}
		};
		mViewPager.setOnPageChangeListener(pcl);

		OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					return;
				}
				if (mKeyCode == KeyEvent.KEYCODE_DPAD_UP) {
					mBottomBar.getChildAt(mViewPager.getCurrentItem())
							.requestFocus();
					return;
				}
				mViewPager.setCurrentItem((Integer) view.getTag());
				((RadioButton) view).setChecked(true);
			}
		};
		PagerAdapter adapter = new PagerAdapter() {
			public int getCount() {
				return mListBodyView.size();
			}

			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			public Object instantiateItem(ViewGroup container, int position) {
//				if(mBodyView.getParent() != null){
//					((ViewGroup)mBodyView.getParent()).removeView(mBodyView);
//				}
//				container.addView(mBodyView);
//				return mListBodyView.get(position).getBodyView();
				View view = mListBodyView.get(position).getBodyView();
				container.addView(view);
				return view;
			}

			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mListBodyView.get(position).getBodyView());
			}

		};
		mViewPager.setAdapter(adapter);

		OnKeyListener onKeyListener = new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				mKeyCode = keyCode;
				return false;
			}
		};

		for (int l = 0; l < mBottomBar.getChildCount(); l++) {
			RadioButton radioButton = ((RadioButton) mBottomBar.getChildAt(l));
			radioButton.setTag(l);
			radioButton.setOnFocusChangeListener(onFocusChangeListener);
			radioButton.setOnKeyListener(onKeyListener);
			radioButton.setOnClickListener(this);
			radioButton.setOnTouchListener(this);
			if (firstDisplayPosition == l) {
				//				radioButton.setChecked(true);
			}
		}
		pcl.onPageSelected(firstDisplayPosition);
		//		mViewPager.setCurrentItem(firstDisplayPosition);
	}

	@Override
	protected void onDetachedFromWindow() {
		handler.removeMessages(TextViewVertical.LAYOUT_CHANGED);
		super.onDetachedFromWindow();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		v.requestFocus();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			handleRecieveMessage(msg);
			return false;
		}
	});

	public void handleRecieveMessage(Message msg) {

	}

	public void setBodyView(String title,View view){
		if(null == mListBodyView){
			mListBodyView = new ArrayList<BaseTabLayout.BodyViewCell>();
		}
		BodyViewCell bvc= new BodyViewCell();
		bvc.setBodyView(view);
		bvc.setTitle(title);
		mListBodyView.add(bvc);
//		mListBodyView = listBodyView;
	}
	
	public List<BodyViewCell> getListBodyView(){
		return mListBodyView;
	}
	
	public View getBodyView(){
		return mListBodyView.get(getCurrentItemPosition()).getBodyView();
	}
	
	public String getBodyTitle(){
		return mListBodyView.get(getCurrentItemPosition()).getTitle();
	}

	public int getCurrentItemPosition() {
		return mViewPager.getCurrentItem();
	}
	
	private class BodyViewCell  {
		private String title;
		private View bodyView;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public View getBodyView() {
			return bodyView;
		}
		public void setBodyView(View bodyView) {
			this.bodyView = bodyView;
		}
	}

}
