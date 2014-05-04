package android.hmm.lib.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-8
 * Description:  
 */
public class BaseViewPager extends FrameLayout implements android.view.View.OnClickListener, android.view.View.OnTouchListener {

	private List<View> mViews = new ArrayList<View>();
	private ViewPager mViewPager;
	private RadioGroup mBottomBar;
	private int firstDisplayPosition = 0;
	private int mKeyCode = 0;
	public Activity context;
	public int EVENT_PAGE_CHANGE = 0x5000;
	
	public BaseViewPager(Context context) {
		super(context);

		this.context = (Activity) context;
	}
	
	public void initViews() {
//		LayoutInflater.from(getContext()).inflate(R.layout.layout_base_viewpager, this, true);
//		mViewPager = (ViewPager) findViewById(R.id.viewpager);
//		mBottomBar = (RadioGroup) findViewById(R.id.bottombar);
//		for (int i = 0; i < mBottomBar.getChildCount(); i++) {
//			mBottomBar.getChildAt(i).setVisibility(View.GONE);
//		}
//		mBottomBar.removeAllViews();
		
		
		mViewPager = new ViewPager(getContext());
		mBottomBar = new RadioGroup(getContext());
		mBottomBar.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
		mBottomBar.setOrientation(LinearLayout.HORIZONTAL);
		mBottomBar.setPadding(0, 0, 0, 10);
		addView(mViewPager);
		addView(mBottomBar);
	
		for (int i = 0; i < mViews.size(); i++) {
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
		ViewPager.OnPageChangeListener pcl= new ViewPager.OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) {

			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			public void onPageSelected(int position) {
				RadioButton radioButton = (RadioButton) mBottomBar.getChildAt(position);
				if (context.getCurrentFocus() == null || mBottomBar.getFocusedChild() != null) {
					onClick(radioButton);
				}
				radioButton.setChecked(true);
			}
		};
		mViewPager.setOnPageChangeListener(pcl);

		OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) { return; }
				if (mKeyCode == KeyEvent.KEYCODE_DPAD_UP) {
					mBottomBar.getChildAt(mViewPager.getCurrentItem()).requestFocus();
					return;
				}
				mViewPager.setCurrentItem((Integer) view.getTag());
				((RadioButton) view).setChecked(true);
			}
		};
		PagerAdapter adapter= new PagerAdapter() {
			public int getCount() {
				return mViews.size();
			}

			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			public Object instantiateItem(ViewGroup container, int position) {
				View view = (View) mViews.get(position);
				container.addView(view);
				return view;
			}

			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(mViews.get(position));
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
			if(firstDisplayPosition == l){
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
	
	public void handleRecieveMessage(Message msg){

	}
	
	public void addObjectView(View view){
		mViews.add(view);
	}
	
	public View getCurrentItemView() {
		return mViews.get(mViewPager.getCurrentItem());
	}
	public int getCurrentItemPosition() {
		return mViewPager.getCurrentItem();
	}
	public List<View> getAllViews(){
		return mViews;
	}
}
