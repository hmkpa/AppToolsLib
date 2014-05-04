package android.hmm.lib.menu;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * 构造方法
 * @param context 调用方的上下文
 * @param titleClick 菜单点击事件
 */
public class MenuView extends Dialog implements OnKeyListener, android.view.View.OnClickListener {
	private Context context;
	private LinkedHashMap<String, Integer> hashMap_data = new LinkedHashMap<String, Integer>();
	private int screenWidth = 1280;
	private int MAXNUM = 6;
	private int height = 110;
	private int width = 200;
	private int spaceLenght = 50;//起始位置预留的空隔宽度
	private View.OnClickListener onClick;

	public MenuView(Context context) {
		this(context, null, null);
	}

	public MenuView(Context context, LinkedHashMap<String, Integer> hashMap_data, OnClickListener onClick) {
		super(context, android.hmm.lib.R.style.MenuStyle);
		this.context = context;
		if (null == hashMap_data || hashMap_data.size() == 0) {
			initData();
		} else {
			this.hashMap_data = hashMap_data;
		}
		initData();
		setConfig();
		createUI();
	}

	protected void initData() {
		hashMap_data.put("aaaa", android.hmm.lib.R.drawable.ic_launcher);
		hashMap_data.put("bbbb", android.hmm.lib.R.drawable.ic_launcher);
		hashMap_data.put("cccc", android.hmm.lib.R.drawable.ic_launcher);
		hashMap_data.put("dddd", android.hmm.lib.R.drawable.ic_launcher);
	}

	public void setData(LinkedHashMap<String, Integer> hashMap_data) {
		if (null != hashMap_data && hashMap_data.size() > 0) {
			hashMap_data.clear();
		}
		this.hashMap_data = hashMap_data;
	}

	public void setOnClick(View.OnClickListener onClick) {
		this.onClick = onClick;
	}

	private void setConfig() {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = ((Activity) context).getWindow().getWindowManager();
		wm.getDefaultDisplay().getMetrics(dm);
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0; // 新位置X坐标
		lp.y = dm.heightPixels - height; // 新位置Y坐标
		lp.width = dm.widthPixels; // 宽度
		lp.height = height; // 高度
		lp.dimAmount = 0.01f; // 去背景
		lp.gravity = Gravity.LEFT;
		dialogWindow.setAttributes(lp);
		setCanceledOnTouchOutside(true);
		width = (dm.widthPixels - spaceLenght) / MAXNUM;
		screenWidth = dm.widthPixels;
	}

	private void createUI() {
		LinearLayout mainLayout = new LinearLayout(context);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
//		mainLayout.setBackgroundResource(R.drawable.menu_bg);
		mainLayout.setBackgroundColor(Color.GRAY);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		/**
		 * 下面两种加载布局方式,可以任选一种
		 */
//		createChildViews(mainLayout);
		createTabLayout(mainLayout);
		setContentView(mainLayout);
	}

	protected void createChildViews(ViewGroup parante) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(new LayoutParams(screenWidth, LayoutParams.WRAP_CONTENT));
		layout.setPadding(spaceLenght, 0, 0, 0);
		if (null != hashMap_data && hashMap_data.size() > 0) {
			Iterator<Entry<String, Integer>> it = hashMap_data.entrySet().iterator();
			int tag = 1;
			while (it.hasNext()) {
				Entry<String, Integer> entry = it.next();
				String key = (String) entry.getKey();
				Integer val = (Integer) entry.getValue();
				layout.addView(createItemView(key, val, tag));
				tag++;
			}
		}
		parante.addView(layout);
	}

	protected void createTabLayout(ViewGroup parante) {
		TableLayout tabLayout = new TableLayout(context);
		tabLayout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, LayoutParams.WRAP_CONTENT));
		tabLayout.setShrinkAllColumns(true);
		tabLayout.setPadding(spaceLenght, 0, 0, 0);
		fillTabLayout(tabLayout);
		parante.addView(tabLayout);
	}

	private void fillTabLayout(TableLayout tabLayout) {
		if (null != hashMap_data && hashMap_data.size() > 0) {
			Iterator<Entry<String, Integer>> it = hashMap_data.entrySet().iterator();
			int tag = 1;
			while (it.hasNext()) {
				Entry<String, Integer> entry = it.next();
				String key = (String) entry.getKey();
				Integer val = (Integer) entry.getValue();
				fillTableRow(key, val, tag, tabLayout);
				tag++;
			}
		}
	}

	protected void fillTableRow(String text, int res, int tag, TableLayout tabLayout) {
		View view = createItemView(text, res, tag);
		TableRow layout = null;
		if (0 == (tag - 1) % MAXNUM) {
			layout = new TableRow(context);
			layout.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			layout.setPadding(0, 0, 0, 0);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.addView(view);
			tabLayout.addView(layout);
		} else {
			layout = (TableRow) tabLayout.getChildAt(tabLayout.getChildCount() - 1);
			layout.addView(view);
		}
	}

	private View createItemView(String text, int res, int tag) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setLayoutParams(new TableRow.LayoutParams(width, height));

		ImageView iv = new ImageView(context);
//		iv.setImageResource(R.drawable.menu_split_line);

		linearLayout.addView(iv);
		linearLayout.addView(createImageText(text, res, tag));
		return linearLayout;
	}

	private View createImageText(String text, int res, int tag) {
		LinearLayout layout = new LinearLayout(context);
		layout.setTag(tag);
		layout.setGravity(Gravity.CENTER);
		layout.setOrientation(LinearLayout.VERTICAL);
//		layout.setBackgroundResource(R.drawable.menu_item_style);
		layout.setFocusable(true);
		layout.setFocusableInTouchMode(true);
		// 屏蔽了所有子控件获取Focus的权限
		layout.setDescendantFocusability(android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		layout.setOnClickListener(this);
		layout.setOnKeyListener(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
		lp.weight = 1;
		createImageView(res, layout, lp);
		createText(text, layout, lp);
		return layout;
	}

	private void createImageView(int res, ViewGroup parent, LayoutParams lp) {
		ImageView iv = new ImageView(context);
		iv.setImageResource(res);
		iv.setFocusable(false);
		parent.addView(iv, lp);
	}

	private void createText(String text, ViewGroup parent, LayoutParams lp) {
		TextView tv = new TextView(context);
		tv.setText(text);
		tv.setTextSize(28);
		tv.setTextColor(Color.WHITE);
		tv.setSingleLine(true);
		tv.setGravity(Gravity.CENTER);
		tv.setEllipsize(TruncateAt.MARQUEE);
		parent.addView(tv, lp);
	}

	public void showAtLocation(View v, int position, int x, int y) {
		show();
	}

	@Override
	public void show() {
		if (isShowing()) {
			dismiss();
			return;
		}
		super.show();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
			if (isShowing() && KeyEvent.ACTION_DOWN == event.getAction()) {
				dismiss();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (null == v.getTag()) {
			return;
		}
		Log.i(getClass().getName(), "click position of menu is:" + v.getTag().toString());
		if (null == onClick) {
			return;
		}
		onClick.onClick(v);
	}

}
