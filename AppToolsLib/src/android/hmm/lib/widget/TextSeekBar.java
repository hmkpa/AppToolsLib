package android.hmm.lib.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author:heming
 * @since :JDK 17  
 * @version：1.0
 * Create at:2013-7-29
 * Description:  
 *
 */
public class TextSeekBar extends LinearLayout implements OnSeekBarChangeListener {

	private SeekBar seekBar;
	private TextView tv;
	private int MAX = 100;
	private int curValue = 0;
	private int tv_size = 20;
	private String suffix = "";

	private int tv_width = 60;
	private int width = LayoutParams.WRAP_CONTENT;
	private int height = LayoutParams.WRAP_CONTENT;
	private OnSeekBarChangeListener onChangeListen;
	private int preWidth = 0;
	private int preProgress = 0;
	private int endWidth = -1;
	private int endProgress = 100;

	/**
	 * @param context
	 */
	public TextSeekBar(Context context) {
		this(context, -1, -1, null);
	}

	public TextSeekBar(Context context, int width, int height) {
		this(context, width, height, null);
	}

	public TextSeekBar(Context context, int width, int height, OnSeekBarChangeListener onChangeListen) {
		super(context);
		if (null != onChangeListen) {
			this.onChangeListen = onChangeListen;
		}
		this.width = width;
		this.height = height;
		createView();
	}

	private void createView() {
		setOrientation(LinearLayout.VERTICAL);
		setLayoutParams(new LayoutParams(width, height));
		setTextViewAttr();
		setSeekBarAttr();
		addView(tv);
		addView(seekBar);
	}

	private void getConfig() {
		int i = 1;
		float left = 0;
		int swidth = seekBar.getWidth();
		while (i <= MAX) {
			if (tv_width / 2 > swidth * i / MAX) {
				left = tv_width / 2;
				preProgress = i;
			} else if ((swidth - tv_width - 25) < swidth * i / MAX) {
				endWidth = swidth - tv_width - 25;
				endProgress = i;
				break;
			}
			i++;
		}
		preWidth = (int) left;
	}

	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		if (null != seekBar) {
			seekBar.setTag(tag);
		}
	}

	public void setSuffix(String suffix) {
		if (null != suffix) {
			this.suffix = suffix;
		}
	}

	private void setTextViewAttr() {
		tv = new TextView(getContext());
//		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(tv_size);
		tv.setSingleLine(true);
		tv.setEllipsize(TruncateAt.MARQUEE);
		tv.setFocusable(true);
	}

	private void setSeekBarAttr() {
		seekBar = new SeekBar(getContext());
		seekBar.setMax(MAX);
		seekBar.setPadding(10, 0, 10, 0);
		seekBar.setOnSeekBarChangeListener(this);
	}

	public void setMax(int max) {
		this.MAX = max;
		if (null != seekBar) {
			seekBar.setMax(MAX);
		}
	}

	private void setTextLayoutMax(int value) {
		int left = 0;
		if (-1 != endWidth) {
			if (value <= preProgress) {
				left = preWidth;
			} else if (value > endProgress) {
				left = endWidth;
			} else {
				left = (value - preProgress) * (endWidth - preWidth) / (endProgress - preProgress);
			}
//			Layout layout = tv.getLayout();
//			tv.layout(l, t, r, b)
			LayoutParams lp = (LayoutParams) tv.getLayoutParams();
			lp.leftMargin = left;
			tv.setLayoutParams(lp);
		}
	}

	private void setTextLayout(int value) {

	}

	private void showProgress(int value) {
		if (null != tv) {
			curValue = value;
			tv.setText(curValue + suffix);
			if (100 == MAX) {
				setTextLayoutMax(value);
			} else {
				setTextLayout(value);
			}
		}
	}

	public void setProgress(int value) {
		if (null != seekBar) {
			seekBar.setProgress(value);
		}
		showProgress(value);
	}

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListen) {
		this.onChangeListen = onChangeListen;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (-1 == endWidth && MAX == 100) {
			getConfig();
		}
		showProgress(progress);
		if (null != onChangeListen) {
			onChangeListen.onProgressChanged(seekBar, progress, fromUser);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (null != onChangeListen) {
			onChangeListen.onStartTrackingTouch(seekBar);
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (null != onChangeListen) {
			onChangeListen.onStopTrackingTouch(seekBar);
		}
	}

}
