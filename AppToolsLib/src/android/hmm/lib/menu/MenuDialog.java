package android.hmm.lib.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author:heming
 * @since :JDK 17  
 * @version：1.0
 * Create at:2013-7-28
 * Description:  以上是自定义Dialgog可以使用自定义样式：
 * 关于<item name="android:windowAnimationStyle">@android:style/Animation.Translucent</item> 
 * 重要:Dialog进出的显示动画要特别注意，可以自定义Dialog进出的动画，但是item的名字必须和下面的一样，
 * 以确定Android系统能够认识出来。
 * <style name="Animation.Translucent">
 * <item name="windowEnterAnimation">@anim/translucent_enter</item>
 * <item name="windowExitAnimation">@anim/translucent_exit</item>
 * </style>
 *
 */
public class MenuDialog extends Dialog implements android.view.View.OnKeyListener, OnItemSelectedListener, OnItemClickListener {

	private Context context;
	private LinearLayout mLayout;
	private GridView mGVTitle;
	private GridView mGVBody;
	private MenuBodyAdapter[] mMenuBodyAdapter;
	private MenuTitleAdapter mMenuTitleAdapter;

	private List<MenuTitleItem> mMenuTitleList = new ArrayList<MenuTitleItem>();
	private int mMenuIndex = 0;
	private int NumColumns = 4;
	public boolean currentState;

	public MenuDialog(Context context) {
		this(context, -1);
	}

	public MenuDialog(Context context, int theme) {
		super(context, theme);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | 0x80000000;
		getWindow().setFlags(flags, flags);
		
		
		initData(context);
	}

	private void initData(Context context) {
		this.context = context;
		currentState = true;
	}

	/**
	 * 初始化整个界面
	 */
	public void initViews() {
		if (null == mMenuTitleList || 0 == mMenuTitleList.size()) { return; }
		if (mGVTitle == null) {
			LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			mGVTitle = createGridView(mMenuTitleList.size(), Color.WHITE, lp);

			mMenuTitleAdapter = new MenuTitleAdapter(context, mMenuTitleList);
			mGVTitle.setAdapter(mMenuTitleAdapter);
		}

		if (mGVBody == null) {
			mGVBody = createGridView(NumColumns, Color.TRANSPARENT, null);
		}
		mMenuBodyAdapter = new MenuBodyAdapter[mMenuTitleList.size()];
		for (int i = 0; i < mMenuTitleList.size(); i++) {
			mMenuBodyAdapter[i] = new MenuBodyAdapter(context, mMenuTitleList.get(i).getBodyList());
		}
		mGVBody.setAdapter(mMenuBodyAdapter[0]);

		if (null == mGVTitle && null == mGVBody) { return; }
		if (null == mLayout) {
			mLayout = new LinearLayout(context);
			mLayout.setOrientation(LinearLayout.VERTICAL);
			mLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		mLayout.removeAllViews();
		mLayout.addView(mGVTitle);
		mLayout.addView(mGVBody);

		this.setContentView(mLayout);
	}

	private GridView createGridView(int numColumns, int backgroundColor, LayoutParams params) {
		GridView gridview = new GridView(context);
		gridview.setNumColumns(numColumns);
		gridview.setBackgroundColor(backgroundColor);
		gridview.setOnKeyListener(this);
		gridview.setOnItemClickListener(this);
		gridview.setOnItemSelectedListener(this);
		if (null != params) gridview.setLayoutParams(params);
		return gridview;
	}

	public void setWindowConfig() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = ((Activity) context).getWindow().getWindowManager();
		wm.getDefaultDisplay().getMetrics(dm);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.x = 0; // 新位置X坐标
		lp.y = dm.heightPixels; // 新位置Y坐标
		lp.width = dm.widthPixels; // 宽度
		lp.height = 130; // 高度
		lp.alpha = 1.0f; // 透明度
		lp.dimAmount = 1.0f; // 去背景
		dialogWindow.setAttributes(lp);
		setCanceledOnTouchOutside(true);
	}

	public void setBackEvent(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)) {
			dismiss();
		}
	}

	public void setMenuEvnet(int keyCode, KeyEvent event) {

	}

	public void setMenuTitle(List<MenuTitleItem> list) {
		mMenuTitleList = list;
	}

	public String getBodyItem(int pageIndex, int position) {
		String result = null;
		return result;
	}

	public GridView getTitleGridView() {
		return mGVTitle;
	}

	public GridView getBodyGridView() {
		return mGVBody;
	}

	public int getMenuIndex() {
		return mMenuIndex;
	}

	public void titleItemSelected(int position) {
		if (null == mMenuBodyAdapter) return;
		mMenuIndex = position;
		mGVBody.setAdapter(mMenuBodyAdapter[position]);
	}

	@Override
	public void dismiss() {
		currentState = false; // 标记状态，已消失
		super.dismiss();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (v == mGVTitle) {
			setBackEvent(keyCode, event);
		}
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == mGVTitle) {
			titleItemSelected(arg2);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0.equals(mGVBody)) {
			mMenuTitleList.get(mMenuIndex).getBodyList().get(arg2).getClick().onClick(arg1);
		} else if (arg0.equals(mGVTitle)) {

		}
	}

}
