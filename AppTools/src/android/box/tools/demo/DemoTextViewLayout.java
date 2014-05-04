package android.box.tools.demo;

import android.box.tools.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hmm.lib.utils.LogHelper;
import android.hmm.lib.widget.BaseViewPager;
import android.hmm.lib.widget.ScrollTextView_1;
import android.hmm.lib.widget.ScrollTextView_2;
import android.hmm.lib.widget.TextViewVertical;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-3-31
 * Description:
 */
public class DemoTextViewLayout extends BaseViewPager {

	private String text = "我打江南走过\n" + "那等在季节里的容颜如莲花的开落\n" + "东风不来\n" + "三月的柳絮不飞\n" + "你的心如小小的寂寞的城\n" + "恰若青石的街道向晚\n" + "跫音不响\n" + "三月的春帷不揭\n" + "你的心是小小的窗扉紧掩\n" + "我达达的马蹄声是美丽的错误\n" + "我不是归人\n"
			+ "是个过客...";

	public DemoTextViewLayout(Context context) {
		super(context);
		addTestViews();
		super.initViews();
	}

	private void addTestViews() {
		initText_1();
		initText_2();
		initText_3();

		initText_4();
		initText_5();
		initText_6();
	}

	/**
	 * SpannableString的使用
	 */
	private void initText_3() {
		TextView tv_3 = new TextView(getContext());
		tv_3.setLineSpacing(4.2f, 1.2f);
		tv_3.setPadding(0, 20, 0, 0);
		SpannableString ss = new SpannableString("红色打电话粗体删除线绿色下划线图片:.");
		ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new URLSpan("tel:4155551212"), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new StyleSpan(Typeface.BOLD), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new StrikethroughSpan(), 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new UnderlineSpan(), 10, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		// 不添加这一句，拨号，http，发短信的超链接不能执行.
		tv_3.setMovementMethod(LinkMovementMethod.getInstance());
		tv_3.setText(ss);
		addObjectView(tv_3);
	}

	/**
	 * SpannableString的使用
	 */
	private void initText_2() {
		TextView tv = new TextView(getContext());
		tv.setLineSpacing(4.2f, 1.2f);
		tv.setPadding(0, 20, 0, 0);
		Drawable d1 = getResources().getDrawable(R.drawable.ic_launcher);
		d1.setBounds(0, 0, d1.getIntrinsicWidth(), d1.getIntrinsicHeight()); // 设置图片大小

		Drawable d2 = getResources().getDrawable(R.drawable.ic_launcher);
		d2.setBounds(0, 0, d2.getIntrinsicWidth(), d2.getIntrinsicHeight()); // 设置图片大小

		int start;
		boolean type = true;
		String value1 = "100";
		String value2 = "10";
		String tFlag = "[XX]";
		SpannableString ss;
		String text = this.text.substring(0, 20);
		String temp = String.format("\n" + tFlag + "赞：%1$s个\n" + tFlag + "踩：%2$s个", value1, value2);
		if (type) {
			start = text.length();
			ss = new SpannableString(text + temp);
		} else {
			start = text.length();
			ss = new SpannableString(text + temp);
		}
		ss.setSpan(new ImageSpan(d1, ImageSpan.ALIGN_BASELINE), start, start + tFlag.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // 插入图片的位置
		start = start + tFlag.length() + 5 + value1.length();
		ss.setSpan(new ImageSpan(d2, ImageSpan.ALIGN_BASELINE), start, start + tFlag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // 插入图片的位置

		tv.setText(ss);
		addObjectView(tv);
	}

	/**
	 * 原生样例
	 */
	private void initText_1() {
		TextView tv_1 = new TextView(getContext());
		tv_1.setLineSpacing(4.2f, 1.2f);
		tv_1.setPadding(0, 20, 0, 0);
		tv_1.setSingleLine(true);
		tv_1.setEllipsize(TruncateAt.MARQUEE);
		tv_1.setText(text);
		tv_1.setFocusable(true);
		addObjectView(tv_1);
	}

	private void initText_6() {
		ScrollTextView_1 tv = new ScrollTextView_1(getContext());
		tv.setLineSpacing(4.2f, 1.2f);
		tv.setPadding(0, 20, 0, 0);
		tv.setEllipsize(TruncateAt.MARQUEE);
		tv.setSingleLine(true);
		tv.setText(text);
		tv.startScroll();
		tv.setSpeed(-1.6f);
		addObjectView(tv);
	}

	private void initText_5() {
		ScrollTextView_2 tv_0 = new ScrollTextView_2(getContext());
		tv_0.setLineSpacing(4.2f, 1.2f);
		tv_0.setPadding(0, 20, 0, 0);
		tv_0.setSingleLine(true);
		tv_0.setEllipsize(TruncateAt.MARQUEE);
		tv_0.setText(text);
//		tv_0.setFocusable(true);
		tv_0.init(126);
		tv_0.setSpeed(1.2f);
		tv_0.startScroll();
		addObjectView(tv_0);
	}

	/**
	 * 竖排滚动的文字
	 */
	private void initText_4() {
		HorizontalScrollView sv = new HorizontalScrollView(getContext());
		TextViewVertical textViewVertical = new TextViewVertical(getContext());
		sv.addView(textViewVertical);
//		textViewVertical.setHandler(handler);// 将Handler绑定到TextViewVertical
		// 创建并设置字体（这里只是为了效果好看一些，但为了让网友们更容易下载，字体库并没有一同打包
		// 如果需要体验下效果的朋友可以自行在网络上搜索stxingkai.ttf并放入assets/fonts/中）
		Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/stxingka.ttf");
		textViewVertical.setTypeface(face);
		// 设置文字内容
		textViewVertical.setText(text+text+text+text+text);
		textViewVertical.setHandler(handler);
		addObjectView(sv);
	}
	
	private void updateVerticalTextView() {
		if (getCurrentItemView() instanceof HorizontalScrollView) {
			HorizontalScrollView hs = (HorizontalScrollView)getCurrentItemView();
			TextViewVertical textViewVertical = (TextViewVertical) hs.getChildAt(0);
			textViewVertical.scrollBy(textViewVertical.getTextWidth(), 0);// 滚动到最右边
		}
		LogHelper.println("updateVerticalTextView");
	}
	
	public void handleRecieveMessage(Message msg){
		if(msg.what == TextViewVertical.LAYOUT_CHANGED){
			updateVerticalTextView();
		}
	}


}
