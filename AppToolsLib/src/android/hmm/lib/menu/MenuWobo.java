package android.hmm.lib.menu;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-24
 * Description:
 */
public class MenuWobo extends PopupWindow implements OnItemSelectedListener, OnKeyListener {

	private GridView gvTitle; // 标题
	private LinearLayout mLayout; // PopupWindow的布局
	private WoboMenuAdapter woboMenuAdapter; // 自定义菜单的适配器
	private Context context;

	/**
	 * 构造方法
	 * 
	 * @param context 调用方的上下文
	 * @param titleClick菜单点击事件
	 */
	public MenuWobo(Context context, OnItemClickListener titleClick) {
		super(context);

		this.context = context;
		mLayout = new LinearLayout(context);
		mLayout.setOrientation(LinearLayout.VERTICAL);

		// 菜单选项栏
		gvTitle = new GridView(context);
		gvTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// 设置宽度自适应
		gvTitle.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gvTitle.setVerticalSpacing(1);
		gvTitle.setHorizontalSpacing(1);
		gvTitle.setGravity(Gravity.CENTER);
		gvTitle.setOnItemClickListener(titleClick);
		gvTitle.setOnItemSelectedListener(this);
		gvTitle.setOnKeyListener(this);

		this.mLayout.addView(gvTitle);

		// 设置菜单的特征
		setContentView(this.mLayout);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setAnimationStyle(android.hmm.lib.R.style.MenuWoboPopupAnimation);
		setFocusable(true);
	}

	public void setItems(HashMap<String, Integer> items) {
		// 创建适配器
		woboMenuAdapter = new WoboMenuAdapter(context, items, 30); // 选中背景颜色

		// 设置列数
		gvTitle.setNumColumns(items.size());
		gvTitle.setAdapter(woboMenuAdapter);
	}

	public void setVisiable(int index) {
		View v = woboMenuAdapter.getView(index, null, null);
		if (v != null) {
			v.setVisibility(View.GONE);
		}
	}

	public void setTitleSelect(int index) {
		if (gvTitle != null && gvTitle.getAdapter() != null) {
			gvTitle.setSelection(index);
			this.woboMenuAdapter.setFocus(index);
		}
	}

	/**
	 * 适配器
	 * 
	 * @author haozi
	 * 
	 */
	class WoboMenuAdapter extends BaseAdapter {
		private LinearLayout[] itemViews;

		/**
		 * 设置 title
		 * 
		 * @param context
		 *            调用方的上下文
		 * @param titles
		 *            数据
		 * @param fontSize
		 *            字体大小
		 * @param fontUnSelColor
		 *            未选中字体颜色
		 * @param fontSelColor
		 *            选中字体颜色
		 * @param bgUnSelColor
		 *            未选中背景颜色
		 * @param bgSelColor
		 *            选中背景颜色
		 */
		@SuppressWarnings("rawtypes")
		public WoboMenuAdapter(Context context, HashMap<String, Integer> titles, int fontSize) {

			// 根据传递进来的titles创建menu上的textView。
			itemViews = new LinearLayout[titles.size()];

			Iterator iter = titles.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				HashMap.Entry entry = (HashMap.Entry) iter.next();
				TextView tvTitles = new TextView(context);
				tvTitles.setText((String) entry.getKey());
				tvTitles.setTextSize(fontSize);
				tvTitles.setGravity(Gravity.CENTER);
				tvTitles.setPadding(10, 30, 10, 30);
				tvTitles.setTextColor(Color.WHITE);

				ImageView icons = new ImageView(context);
				icons.setImageResource((Integer) entry.getValue());

				itemViews[i] = new LinearLayout(context);
				itemViews[i].setLayoutParams(new android.widget.AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 110));
				itemViews[i].setOrientation(LinearLayout.HORIZONTAL);
				itemViews[i].setGravity(Gravity.CENTER);
				itemViews[i].addView(icons);
				itemViews[i].addView(tvTitles);
				itemViews[i].setTag(entry.getKey());

				i++;
			}
		}

		@Override
		public int getCount() {
			return itemViews.length;
		}

		@Override
		public Object getItem(int position) {
			return 0;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		// 设置选中效果
		public void setFocus(int index) {

			for (int i = 0; i < itemViews.length; i++) {

				if (i != index) {// 如果未选中
					//	this.itemViews[i].setBackgroundResource(R.drawable.menu_bg);
				} else {// 如果选中
					//	this.itemViews[i].setBackgroundResource(R.drawable.menu_press);
				}
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = null;
			if (convertView == null) {
				v = itemViews[position];
			} else {
				v = convertView;
			}
			return v;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		setTitleSelect(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) && isShowing()) {
			dismiss();
			return true;
		}
		return false;
	}
}