package android.hmm.lib.widget;

import java.lang.reflect.Method;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-11-7
 * Description:光标移到的时候实现滑动效果
 */
public class ScrollGridView extends GridView {
	private String tag = "FocusScrollListView";

	private int itemWidth;
	private int itemHeight;
	private int verticalSpacing = 0;
	private int horizontalSpacing = 0;

	// 背景图片，也可以不用任何图片而直接用一个颜色画一个和item等高等宽的矩形
	private Bitmap mBitmap;

	// 是否被调用了setSelection，如果调用了就必须强制刷新焦点图片的位置
	private boolean isSetSelection;

	// Scroller类当前返回的数字，本项目下焦点的Y坐标
	private int cordinatesY;
	private int cordinatesX = 0;

	// 是否是屏幕滑动了，用于按键翻页的，强制刷新焦点图片位置
	private boolean isPageScroll;

	// 是否已经拿到了item的高度
	private boolean hadHeight;

	// 用于滑动的封装了加速减速器的计数类
	private Scroller mScroller;

	private Matrix matrix;

	// scale X和Y，用于将任意图片拉伸到刚好填充item的空间
	private float scaley;
	private float scalex;

	// 焦点滑动的时间
	private int sDuration = 700;

	// 翻页的API
	private Method method_pageScroll;

	// item批量上下刷新的API
	private Method method_arrowScrollImpl;

	// 焦点是否正在滑动的过程中，焦点从一个item滑向相邻的另外一个item且滑动过程结束时该变量即为false
	private boolean isScroll;

	// 记录离开当前ListView时所在的焦点位置，用于从ListView切换到另外的控件上然后再切换回来还能保持上次焦点所在的位置
	protected int tmpSelection;
	protected Context context;
	private boolean firstMove = true;

	public ScrollGridView(Context context, AttributeSet attrs) {
		this(context, null, -1);
	}

	public ScrollGridView(Context context) {
		this(context, null);
	}

	public ScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		this.context = context;
		// 禁用ListView上面的渐进边缘，也可以不禁用
//		setVerticalFadingEdgeEnabled(false);
		matrix = new Matrix();
		// 读取焦点背景图片
		mBitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.arrow_down_float);
//		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_btn_select);
		// 通过反射初始化私有方法
		initPrivateMethods();
	}

	@Override
	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
		super.setHorizontalSpacing(horizontalSpacing);
	};

	@Override
	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
		super.setVerticalSpacing(verticalSpacing);
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		itemWidth = getWidth() / getNumColumnsCompat();
		// ListView刚创建的时候没有child，因为还没有setAdapter，但是创建的时候会调用layout，所以要判断一下，要不然会报错
		if (getChildCount() > 0) {
			// 如果获取ListView的高度了就不要再调用这个方法了，要不然某些情况高度会变成0，同时对性能也有好处
			if (!hadHeight) {
				itemHeight = getChildAt(0).getHeight();
				hadHeight = true;
			}
			scalex = (float) itemWidth / mBitmap.getWidth();
			scaley = (float) itemHeight / mBitmap.getHeight();
			matrix.setScale(scalex, scaley);
			// 根据item宽高调整好拉伸背景图片
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
		}
	}

	// 所有的效果都是通过这个回调方法完成，这个方法很重要
	@Override
	protected void onDraw(Canvas canvas) {
//		Log.i(tag, "onDraw isSetSelection:" + isSetSelection + ",isScroll:" + isScroll + ",isPageScroll:" + isPageScroll + ",cordinatesY:" + cordinatesY + "getSelectedView:" + getSelectedView());
		// 如果外面调用了ListView的setSelection方法就会刷新并且返回，不执行下面的代码
		if (isSetSelection) {
			if (null != getSelectedView()) {
				canvas.drawBitmap(mBitmap, 0, getSelectedView().getTop(), null);
				setScroller(getSelectedView().getTop());
				isSetSelection = false;
				return;
			}
		}

		// 焦点一边滑动一边刷新，知道Scroller滑动结束，将isScroll置false
		if (mScroller.computeScrollOffset()) {
			// 不断的回调onDraw
			invalidate();
		} else {
			if (isScroll) {
				isScroll = false;
			}
		}

		// 如果是翻页就刷新
		if (isPageScroll) {
			if (null != getSelectedView()) {
				isPageScroll = false;
				if (firstMove) {
					firstMove = false;
					cordinatesY = 0;
				} else {
					cordinatesY = getSelectedView().getTop();
					cordinatesX = getSelectedView().getLeft();
					setScroller(getSelectedView().getTop());
				}
			}
		} else {
			cordinatesY = mScroller.getCurrY();
			cordinatesX = mScroller.getCurrX();
		}

		canvas.drawBitmap(mBitmap, cordinatesX, cordinatesY, null);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		// 离开ListView时候记录焦点位置
		if (getChildCount() > 0) {
			if (!gainFocus) {
				tmpSelection = getSelectedItemPosition();
			} else {
				if (null != getSelectedView()) {
//					setSelectionFromTop(tmpSelection, getSelectedView().getTop());
				}
			}
		}
	}

	@Override
	public void setSelection(int position) {
		super.setSelection(position);
		updateFocus();
	}

	/**
	 * The method of setMSelection() instead of setSelection(), so please call
	 * setMSelection to set position of item
	 * 
	 * @param position
	 */
	public void setMSelection(int position) {
		setTmpSelection(position);
		setSelection(position);
		isPageScroll = true;
	}

	private void updateFocus() {
		isSetSelection = true;
	}

	private void setTmpSelection(int position) {
		tmpSelection = position;
	}

	/**
	 * return the number of items at present
	 * 
	 * @return the number of items at present
	 */
	public int getItemNum() {
		return getChildCount();
	}

	private void setScroller(int newY) {
		mScroller.setFinalY(newY);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setMSelection(0);
	}

	// ListView的item数量实际上是动态改变的，会在一个数值x和x+1甚至x+2之间徘徊，所以利用item的数量来计算焦点的移动是不行的，所以增加的实现此功能的复杂度
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 获得当前选中的item
		View view = getSelectedView();
		int position = getSelectedItemPosition();
		Log.i(tag, String.valueOf("position: " + position));
		int[] location = new int[2];
		if (null != view) {
			view.getLocationInWindow(location);
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if ((position + getNumColumnsCompat()) < getAdapter().getCount()) {
					if (location[1] >= (720 - itemHeight)) {
						setSelection(position + getNumColumnsCompat());
						return false;
					}
					mScroller.startScroll(view.getLeft(), view.getTop(), 0, itemHeight + verticalSpacing, sDuration);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				if ((position - getNumColumnsCompat()) >= 0) {
					if (location[1] <= 0) {
						setSelection(position - getNumColumnsCompat());
						return false;
					}
					mScroller.startScroll(view.getLeft(), view.getTop(), 0, -itemHeight - verticalSpacing, sDuration);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (position == 0) {

				} else if (position % getNumColumnsCompat() != 0) {
					mScroller.startScroll(view.getLeft(), view.getTop(), -itemWidth - horizontalSpacing, 0, sDuration);
					isScroll = true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (position == 0 || (position + 1) % getNumColumnsCompat() != 0) {
					isScroll = true;
					mScroller.startScroll(view.getLeft(), view.getTop(), itemWidth + horizontalSpacing, 0, sDuration);
				} else {

				}
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private int getNumColumnsCompat() {
	    if (Build.VERSION.SDK_INT >= 11) {
	        return getNumColumnsCompat11();
	    } else {
	        int columns = 0;
	        int children = getChildCount();
	        if (children > 0) {
	            int width = getChildAt(0).getMeasuredWidth();
	            if (width > 0) {
	                columns = getWidth() / width;
	            }
	        }
	        return columns > 0 ? columns : AUTO_FIT;
	    }
	}

	private int getNumColumnsCompat11() {
//		return getNumColumnsCompat();
		return getNumColumns();
	}

	// 初始化私有方法
	private void initPrivateMethods() {
		try {
			method_pageScroll = ListView.class.getDeclaredMethod("pageScroll", int.class);
			method_arrowScrollImpl = ListView.class.getDeclaredMethod("arrowScrollImpl", int.class);
			method_pageScroll.setAccessible(true);
			method_arrowScrollImpl.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 通过此方法设置焦点背景图片
	public void setFocusBitmap(int resourceId) {
		mBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
	}

	protected void animationEffect(View view, Boolean animationFlage, float mScaleXY) {
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnimation = null;
		if (animationFlage) {
			scaleAnimation = new ScaleAnimation(1f, mScaleXY, 1f, mScaleXY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			scaleAnimation = new ScaleAnimation(1f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		}
		scaleAnimation.setDuration(100);
		animationSet.addAnimation(scaleAnimation);
		animationSet.setFillAfter(true);
		view.startAnimation(animationSet);
		bringToFront();
	}
}
