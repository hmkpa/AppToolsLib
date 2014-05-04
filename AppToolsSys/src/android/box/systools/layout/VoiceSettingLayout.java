package android.box.systools.layout;

import android.content.Context;
import android.hmm.lib.utils.VoiceHelper;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-26
 * Description:  
 */
public class VoiceSettingLayout extends LinearLayout implements OnSeekBarChangeListener{
	
	private VoiceHelper voiceHelper;
	
	public VoiceSettingLayout(Context context) {
		super(context);
		voiceHelper = new VoiceHelper(context);
		setOrientation(LinearLayout.VERTICAL);
//		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		
		createItemView("alr",VoiceHelper.type_alr);
		createItemView("cal",VoiceHelper.type_cal);
		createItemView("mic",VoiceHelper.type_mic);
		createItemView("rig",VoiceHelper.type_rig);
		createItemView("sys",VoiceHelper.type_sys);
	}

	private void createItemView(String title, int type) {
		LinearLayout linear = new LinearLayout(getContext());
		linear.setOrientation(LinearLayout.HORIZONTAL);
		linear.setPadding(0, 5, 0, 5);
		
		TextView tv = new TextView(getContext());
		tv.setText(title);
		tv.setPadding(10, 0, 10, 0);
		linear.addView(tv);

		SeekBar seekBar = new SeekBar(getContext());
		seekBar.setLayoutParams(new LinearLayout.LayoutParams(150, LayoutParams.WRAP_CONTENT));
		seekBar.setTag(type);
		seekBar.setMax(voiceHelper.getMaxAudio(type));
		seekBar.setProgress(voiceHelper.getCurrentAudio(type));
		seekBar.setOnSeekBarChangeListener(this);
		linear.addView(seekBar);
		
		tv = new TextView(getContext());
		tv.setPadding(10, 0, 10, 0);
		tv.setText(voiceHelper.getCurrentAudio(type) + "/" + voiceHelper.getMaxAudio(type));
		linear.addView(tv);
		
//		ViewGroup vg = (ViewGroup) findViewById(android.R.id.content);
//		LinearLayout layout = (LinearLayout) vg.getChildAt(0);
//		layout.addView(linear);
		addView(linear);
	}
	
	private void updateItemView() {
//		ViewGroup vg = (ViewGroup) findViewById(android.R.id.content);
//		LinearLayout layout = (LinearLayout) vg.getChildAt(0);
		LinearLayout layout = this;
		for (int i = 0; i < layout.getChildCount(); i++) {
			LinearLayout linear = (LinearLayout) layout.getChildAt(i);
			updateItemView(linear, (Integer) linear.getChildAt(1).getTag());
		}
	}
	
	private void updateItemView(LinearLayout linear,int type) {
		SeekBar seekBar = (SeekBar) linear.getChildAt(1);
		seekBar.setProgress(voiceHelper.getCurrentAudio(type));
		TextView tv = (TextView) linear.getChildAt(2);
		tv.setText(voiceHelper.getCurrentAudio(type) + "/" + voiceHelper.getMaxAudio(type));
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		if(fromUser){
			int type = (Integer) seekBar.getTag();
			voiceHelper.setAudio(type, progress);
//			updateItemView((LinearLayout) seekBar.getParent(), type);
			updateItemView();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	

}
