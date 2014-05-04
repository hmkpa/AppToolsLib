package android.box.systools.layout;

import android.app.Activity;
import android.content.Context;
import android.hmm.lib.utils.BrightUtils;
import android.hmm.lib.utils.ScreenHelper;
import android.hmm.lib.utils.ToastHelper;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-26
 * Description:  
 */
public class ScreenInfoLayout extends LinearLayout{

	private Activity activity;
	
	public ScreenInfoLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		activity = (Activity) context;
		init((Activity) context);
	}
	
	private void init(Activity activity) {
		setOrientation(LinearLayout.VERTICAL);
		DisplayMetrics dm = ScreenHelper.getDisplayMetrics(activity);
		createDisplayMetrics("xdpi:", dm.xdpi);
		createDisplayMetrics("ydpi:", dm.ydpi);
		createDisplayMetrics("density:", dm.density);
		createDisplayMetrics("densityDpi:", dm.densityDpi);
		createDisplayMetrics("heightPixels:", dm.heightPixels);
		createDisplayMetrics("widthPixels:", dm.widthPixels);
		createDisplayMetrics("scaledDensity:", dm.scaledDensity);
		
		createBrightView();
		createStatusView();
	}

	private void createDisplayMetrics(String title, Object value) {
		LinearLayout linear = new LinearLayout(getContext());
		linear.setOrientation(LinearLayout.HORIZONTAL);
		linear.setPadding(0, 5, 0, 5);

		TextView tv = new TextView(getContext());
		tv.setText(title);
		tv.setPadding(10, 0, 10, 0);
		linear.addView(tv);
		
		tv = new TextView(getContext());
		tv.setPadding(10, 0, 10, 0);
		tv.setText(String.valueOf(value));
		linear.addView(tv);
		
		addView(linear);
	}
	
	private void createBrightView(){
		LinearLayout linear = new LinearLayout(getContext());
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setPadding(0, 5, 0, 5);
		
		RadioGroup rg = new RadioGroup(getContext()); 
		rg.setOrientation(LinearLayout.HORIZONTAL);
//		rg.setGravity(Gravity.LEFT);
		rg.setPadding(0, 10, 0, 0);
		linear.addView(rg);
		
		RadioButton rb = new RadioButton(getContext());
		rb.setText("auto");
		rg.addView(rb);
		
		rb = new RadioButton(getContext());
		rb.setText("manual");
		rg.addView(rb);
		
		if(!BrightUtils.getBrightMode(getContext())){
			rb.setChecked(true);
		}
		
		rg.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				boolean b = false;
				if (checkedId == group.getChildAt(0).getId()) {
					b = BrightUtils.setMode(getContext(), BrightUtils.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
				} else if (checkedId == group.getChildAt(1).getId()) {
					b = BrightUtils.setMode(getContext(), BrightUtils.SCREEN_BRIGHTNESS_MODE_MANUAL);
				}
				if(!b){
					ToastHelper.showCustomToast(getContext(), "设置失败");
				}
				
				LinearLayout line = (LinearLayout) group.getParent();
				line.getChildAt(1).setEnabled(!BrightUtils.getBrightMode(getContext()));
			}
		});
		
		SeekBar seekBar = new SeekBar(getContext());
		seekBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT));
		seekBar.setMax(BrightUtils.MaxBrighrness);
		seekBar.setProgress(BrightUtils.getBrightness(getContext()));
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				BrightUtils.saveBrightnessF(getContext(), progress);
			}
		});
		linear.addView(seekBar);
		addView(linear);
	}
	
	private void createStatusView(){
		LinearLayout linear = new LinearLayout(getContext());
		linear.setOrientation(LinearLayout.HORIZONTAL);
		linear.setPadding(0, 5, 0, 5);
		
		RadioGroup rg = new RadioGroup(getContext()); 
		rg.setOrientation(LinearLayout.HORIZONTAL);
		rg.setPadding(0, 10, 0, 0);
		linear.addView(rg);
		
		RadioButton rb = new RadioButton(getContext());
		rb.setText("full");
		rg.addView(rb);
		
		rb = new RadioButton(getContext());
		rb.setText("no full");
		rg.addView(rb);
		
		if(!BrightUtils.getBrightMode(getContext())){
			rb.setChecked(true);
		}
		
		rg.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == group.getChildAt(0).getId()) {
					ScreenHelper.setFullScreen(activity);
				}else if (checkedId == group.getChildAt(1).getId()) {
					ScreenHelper.quitFullScreen(activity);
				}
//				((BaseApplication) activity.getApplication()).setFullScreenStatus(
//						!((BaseApplication) activity.getApplication()).getScreenStatus());
			}
		});
		addView(linear);
	}

}
