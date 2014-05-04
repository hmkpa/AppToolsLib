package android.hmm.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScrollTextView_2 extends TextView {

	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 0f; // 文字的横坐标
	private float y = 0f; // 文字的纵坐标
	private float temp_view_plus_text_length = 0.0f; // 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	public boolean isStarting = false; // 是否开始滚动
	private Paint paint = null; // 绘图样式
	private CharSequence text = ""; // 文本内容
	private float speed = 2.5f;
	private int defaultTextColor; // 默认字体颜色
	private int textSelectColor; // 选择的字体颜色
	private boolean isCenter = false;
	private boolean isFirst = true;

	public int getTextColor() {
		return defaultTextColor;
	}

	public void setTextColor(int color) {
		this.defaultTextColor = color;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setCenter(boolean isCenter) {
		this.isCenter = isCenter;
	}

	public ScrollTextView_2(Context context) {
		super(context);
		setDefaultColor(context);
	}

	public ScrollTextView_2(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDefaultColor(context);
	}

	public ScrollTextView_2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setDefaultColor(context);
	}

	private void setDefaultColor(Context context) {
		defaultTextColor = 0xFFFFFFFF;
		textSelectColor = 0xFFFFF100;
	}

	public void init(float width) {
		paint = super.getPaint();
		text = super.getText().toString();

		textLength = paint.measureText(text.toString());
		viewWidth = width;
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
		y = getTextSize() + getPaddingTop();
		paint.setColor(defaultTextColor);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.step = step;
		ss.isStarting = isStarting;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;
	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		@SuppressWarnings("unused")
		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if (b != null && b.length > 0) isStarting = b[0];
			step = in.readFloat();
		}
	}

	public void startScroll() {
		paint.setColor(textSelectColor);
		if (textLength < viewWidth) { return; }

		isStarting = true;
		invalidate();
	}

	public void stopScroll() {
		paint.setColor(defaultTextColor);
		isStarting = false;
		isFirst = true;
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		if (!isStarting) {
			float x = 0;
			if (textLength < viewWidth && isCenter) {
				x = (viewWidth - textLength) / 2;
			}
			canvas.drawText(text, 0, text.length(), x, y, paint);
			return;
		}
		if (isFirst) {
			canvas.drawText(text, 0, text.length(), temp_view_plus_text_length - step - viewWidth, y, paint);
		} else {
			canvas.drawText(text, 0, text.length(), temp_view_plus_text_length - step, y, paint);
		}

		step += speed;
		if (step > temp_view_plus_two_text_length) {
			step = textLength;
			isFirst = false;
		}
		invalidate();
	}
}