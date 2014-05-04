package android.hmm.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class SimpleProgressBar extends ProgressBar{
	private Paint mPaint;
	private String msg = "加载中...";
	
	public SimpleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SimpleProgressBar(Context context) {
		super(context,null,android.R.attr.progressBarStyleLarge); 
		
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);  
		this.mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		this.mPaint.setTextSize(25); 
	}
	
	public SimpleProgressBar(Context context,String msg) {
		super(context,null,android.R.attr.progressBarStyleLarge); 
		
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);  
		this.mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		this.mPaint.setTextSize(25); 
		
		this.msg = msg;
	}

	protected synchronized void onDraw(Canvas canvas) { 
		super.onDraw(canvas);
		Rect rect = new Rect(); 
		int x = (getWidth() / 2) - 45;  
		int y = (getHeight() / 2) + 10;  
		this.mPaint.getTextBounds(msg, 0,msg.length(), rect);
		canvas.drawText(msg, x, y, this.mPaint);
	}  
}
