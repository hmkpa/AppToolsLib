package android.box.tools.demo;

import android.box.tools.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-16
 * Description:  四种简单动画
 */
public class BaseSimplerAnim extends LinearLayout{

	
	public BaseSimplerAnim(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initViews();
	}

	private void initViews() {
		setGravity(Gravity.CENTER_HORIZONTAL);
		setOrientation(LinearLayout.VERTICAL);
		Button btn = new Button(getContext());
		btn.setText("startAnim");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startAnim();
			}
		});
		addView(btn);

		// 载入XML文件成Animation对象
		Animation translate = AnimationUtils.loadAnimation(getContext(), R.anim.base_simpler_anim_translate);
		Animation scale = AnimationUtils.loadAnimation(getContext(), R.anim.base_simpler_anim_scale);
		Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.base_simpler_anim_rotate);
		Animation alpha = AnimationUtils.loadAnimation(getContext(), R.anim.base_simpler_anim_alpha);
		
		char a = 'a';
		createTextView(String.valueOf(a), translate);
		createTextView(String.valueOf(a+1), scale);
		createTextView(String.valueOf(a+2), rotate);
		createTextView(String.valueOf(a+3), alpha);
	}
	
	private void createTextView(String text,Animation animation) {
		TextView tv = new TextView(getContext());
		tv.setText(text);
		tv.setLines(2);
		tv.setMinLines(2);
		tv.setGravity(Gravity.CENTER);
		tv.setAnimation(animation);
		addView(tv);
	}
	
	private void startAnim(){
		for(int i=0;i<getChildCount();i++){
			if(getChildAt(i) instanceof TextView){
				getChildAt(i).startAnimation(getChildAt(i).getAnimation());
			}
		}
	}

}
