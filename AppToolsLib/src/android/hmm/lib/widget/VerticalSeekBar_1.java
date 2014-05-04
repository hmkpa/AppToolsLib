package android.hmm.lib.widget;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsSeekBar;

/**
 * @author:heming
 * @since :JDK 17  
 * @version：1.0
 * Create at:2013-7-29
 * Description:  
 *
 */
public class VerticalSeekBar_1 extends AbsSeekBar {

	private int height = -1;
	private int width = -1;
	private int mKeyProgressIncrement = 1;
	private boolean mIsVertical = true;

	public interface OnSeekBarChangeListener {
		public void onProgressChanged(VerticalSeekBar_1 vBar, int progress, boolean fromUser);

		public void onStartTrackingTouch(VerticalSeekBar_1 vBar);

		public void onStopTrackingTouch(VerticalSeekBar_1 vBar);
	}

	private OnSeekBarChangeListener mOnSeekBarChangeListener;

	public VerticalSeekBar_1(Context context) {
		this(context, null);
	}

	public VerticalSeekBar_1(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.seekBarStyle);
	}

	public VerticalSeekBar_1(Context context, AttributeSet attrs, int defstyle) {
		super(context, attrs, defstyle);
	}

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		mOnSeekBarChangeListener = l;
	}

	void onStartTrackingTouch() {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStartTrackingTouch(this);
		}
	}

	void onStopTrackingTouch() {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStopTrackingTouch(this);
		}
	}

	void onProgressRefresh(float scale, boolean fromUser) {
		Drawable thumb = null;
		try {
			Field mThumb_f = this.getClass().getSuperclass().getDeclaredField("mThumb");
			mThumb_f.setAccessible(true);
			thumb = (Drawable) mThumb_f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);

		invalidate();

		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), fromUser);
		}
	}

	private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
		int available = 0;
		try {

			int up = getPaddingTop();
			int bottom = getPaddingBottom();

			available = getHeight() - up - bottom;
			int thumbWidth = thumb.getIntrinsicWidth();
			int thumbHeight = thumb.getIntrinsicHeight();
			available -= thumbWidth;

			// The extra space for the thumb to move on the track
			available += getThumbOffset() * 2;

			int thumbPos = (int) (scale * available);

			int topBound, bottomBound;
			if (gap == Integer.MIN_VALUE) {
				Rect oldBounds = thumb.getBounds();
				topBound = oldBounds.top;
				bottomBound = oldBounds.bottom;
			} else {
				topBound = gap;
				bottomBound = gap + thumbHeight;
			}
			// Canvas will be translated, so 0,0 is where we start drawing
			thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = 30;
		height = View.MeasureSpec.getSize(heightMeasureSpec);

		this.setMeasuredDimension(width, height);
	}

	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-height, 0);
		super.onDraw(c);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldw, oldh);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		boolean mIsUserSeekable = true;
		try {
			Field mIsUserSeekable_f = this.getClass().getSuperclass().getDeclaredField("mIsUserSeekable");
			mIsUserSeekable_f.setAccessible(true);

			mIsUserSeekable = mIsUserSeekable_f.getBoolean(this);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (!mIsUserSeekable || !isEnabled()) { return false; }

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setPressed(true);
			onStartTrackingTouch();
			trackTouchEvent(event);
			break;

		case MotionEvent.ACTION_MOVE:
			trackTouchEvent(event);
			Method attemptClaimDrag;
			try {
				attemptClaimDrag = this.getClass().getSuperclass().getDeclaredMethod("attemptClaimDrag");
				attemptClaimDrag.setAccessible(true);
				attemptClaimDrag.invoke(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case MotionEvent.ACTION_UP:
			trackTouchEvent(event);
			onStopTrackingTouch();
			setPressed(false);
			// ProgressBar doesn't know to repaint the thumb drawable
			// in its inactive state when the touch stops (because the
			// value has not apparently changed)
			invalidate();
			break;

		case MotionEvent.ACTION_CANCEL:
			onStopTrackingTouch();
			setPressed(false);
			invalidate(); // see above explanation
			break;
		}
		return true;
	}

	protected void trackTouchEvent(MotionEvent event) {

		final int height = getHeight();
		final int available = height - getPaddingLeft() - getPaddingRight();
		int y = (int) (height - event.getY());
		float scale;
		float progress = 0;
		if (y < getPaddingLeft()) {
			scale = 0.0f;
		} else if (y > height - getPaddingRight()) {
			scale = 1.0f;
		} else {
			scale = (float) (y - getPaddingLeft()) / (float) available;
			float mTouchProgressOffset = 0.0f;
			try {
				Field mTouchProgressOffset_f = this.getClass().getSuperclass().getDeclaredField("mTouchProgressOffset");
				mTouchProgressOffset_f.setAccessible(true);
				mTouchProgressOffset = mTouchProgressOffset_f.getFloat(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			progress = mTouchProgressOffset;
		}

		final int max = getMax();
		progress += scale * max;

		try {
			Method setProgress = this.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setProgress", int.class, boolean.class);
			setProgress.setAccessible(true);
			setProgress.invoke(this, (int) progress, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void onKeyChange() {
	}

	public void setKeyProgressIncrement(int increment) {
		mKeyProgressIncrement = increment < 0 ? -increment : increment;
	}

	public int getKeyProgressIncrement() {
		return mKeyProgressIncrement;
	}

	@Override
	public synchronized void setMax(int max) {
		super.setMax(max);

		if ((mKeyProgressIncrement == 0) || (getMax() / mKeyProgressIncrement > 20)) {
			setKeyProgressIncrement(Math.max(1, Math.round((float) getMax() / 20)));
		}
	}

	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		super.setProgress(progress);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isEnabled()) {
			int progress = getProgress();
			if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT && !mIsVertical) || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && mIsVertical)) {
				if (progress > 0) {
//					setMyProgress(getContext(), progress - mKeyProgressIncrement, true);
					setProgress(progress - mKeyProgressIncrement);
					onKeyChange();
					return true;
				}
			} else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && !mIsVertical) || (keyCode == KeyEvent.KEYCODE_DPAD_UP && mIsVertical)) {
				if (progress < getMax()) {
//					setMyProgress(getContext(), progress + mKeyProgressIncrement, true);
					setProgress(progress + mKeyProgressIncrement);
					onKeyChange();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

//	public boolean setMyProgress(Context context, int apConfig, boolean enabled) {
//		try {
//			Method method = getClass().getMethod("setProgress", Integer.class, Boolean.TYPE);
//			method.invoke(this, apConfig, enabled);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
}
