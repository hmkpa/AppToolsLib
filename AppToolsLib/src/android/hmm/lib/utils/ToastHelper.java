package android.hmm.lib.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ToastHelper {

	public static void showToast(Context context, String text) {
		android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT).show();
	}

	public static void showCustomToast(Context context, String text) {
		showCustomToast(context, text, android.widget.Toast.LENGTH_SHORT);
	}

	/***********************************************************************
	 * 自定义提示
	 ***********************************************************************/
	public static void showCustomToast(Context context, String text, int duration) {
		if (null == text || text.length() < 1) return;
		LinearLayout toastRoot = new LinearLayout(context);
		toastRoot.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		toastRoot.setBackgroundResource(android.R.drawable.toast_frame);

		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		toastRoot.addView(createTextView(context, text));
		android.widget.Toast toastStart = new android.widget.Toast(context);
		toastStart.setGravity(Gravity.BOTTOM, 0, 10);
		toastStart.setDuration(duration);// 测试设置时间是无效的
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	private static android.widget.TextView createTextView(Context context, String text) {
		android.widget.TextView message = new android.widget.TextView(context);
		/**
		 * 第一个参数为模糊半径，越大越模糊。 
		 * 第二个参数是阴影离开文字的x横向距离。 
		 * 第三个参数是阴影离开文字的Y横向距离。 
		 * 第四个参数是阴影颜色。
		 */
		message.setShadowLayer(2.75f, 0, 0, Color.parseColor("#BB000000"));
//		   <style name="CustomToast">
//        		<item name="android:textSize">21sp</item>
//        		<item name="android:textStyle">normal</item>
//        		<item name="android:textColor">@android:color/secondary_text_light</item>
//    		</style>
		message.setTextAppearance(context, android.hmm.lib.R.style.CustomToast);
		message.setTextSize(21);
		message.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
		message.setTextColor(Color.WHITE);
//		//设置字体样式正常，粗体，斜体，粗斜体
		android.text.SpannableString msp = new android.text.SpannableString(text);
		msp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.NORMAL), 1, text.length(), 
				android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 正常
		msp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 1, text.length(), 
				android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗体
		msp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), 1, text.length(), 
				android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 斜体
		msp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 1, text.length(), 
				android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗斜体
		message.setText(msp);
		return message;
	}

	protected void ExampleToast(Context context) {
		android.widget.Toast toast = android.widget.Toast.makeText(context, "自定义位置Toast", android.widget.Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

		toast = android.widget.Toast.makeText(context, "带图片的Toast", android.widget.Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		android.widget.ImageView imageCodeProject = new android.widget.ImageView(context);
		imageCodeProject.setImageResource(android.hmm.lib.R.drawable.ic_launcher);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

}
