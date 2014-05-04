package android.hmm.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class ScrollTextView_1 extends TextView implements Runnable {
	private int currentScrollX; // 当前滚动的位置
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;
	private float speed = -3.5f;
	private boolean isReverse = true;

	public ScrollTextView_1(Context context) {
		super(context, null);
	}

	public ScrollTextView_1(Context context, AttributeSet attrs) {
		super(context, attrs, -1);
	}

	public ScrollTextView_1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFirstScrollX();
		setGravity(Gravity.RIGHT);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isMeasure) {// 文字宽度只需获取一次就可以了
			getTextWidth();
			isMeasure = true;
		}
	}

	public void setText(String text) {
		if (isReverse) {
//			text = StringUtils.reverseString(text);
		}
		super.setText(text);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * 获取文字宽度
	 */
	private void getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}

	@Override
	public void run() {
		currentScrollX += speed;// 滚动速度
		scrollTo(currentScrollX, 0);
		if (isStop) { return; }
		if (textWidth != 0) {
			if (speed > 0) {
				if (getScrollX() > textWidth) {
					setCurScrollX(-getWidth());
				}
			} else {
				if (getScrollX() <= -(this.getWidth())) {
					if (isReverse) {
						setCurScrollX(textWidth - getWidth());
					} else {
						setCurScrollX(textWidth);
					}
				}
			}
			postDelayed(this, 5);
		} else {
			postDelayed(this, 500);
		}
	}

	private void setCurScrollX(int position) {
		currentScrollX = position;
		scrollTo(currentScrollX, 0);
	}

	private void initFirstScrollX() {
		if (speed > 0) {
			currentScrollX = getWidth();
		} else {
			currentScrollX = 0;
		}
	}

	// 开始滚动
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	// 停止滚动
	public void stopScroll() {
		isStop = true;
	}

	// 从头开始滚动
	public void startFor0() {
		initFirstScrollX();
		startScroll();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

}
