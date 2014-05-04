package android.hmm.lib.menu;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuBodyAdapter extends BaseAdapter {

	private Context context;
	private List<MenuBodyItem> list;

	public MenuBodyAdapter(Context context, List<MenuBodyItem> list) {
		this.context = context;

		this.list = list;
	}

	@Override
	public int getCount() {
		return (list != null) ? list.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {

			holder = new ViewHolder();
			holder.layout = new LinearLayout(context);
			holder.setLinearLayoutAttrs();

			holder.imageView = new ImageView(context);
			holder.setImageViewAttrs();

			holder.textView = new TextView(context);
			holder.setTextViewAttrs();

			arg1 = holder.layout;
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		holder.textView.setText(list.get(arg0).getName());
		holder.imageView.setImageResource(list.get(arg0).getIconId());
		return arg1;
	}

	static class ViewHolder {
		private TextView textView;
		private ImageView imageView;
		private LinearLayout layout;

		private void setLinearLayoutAttrs() {
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);
		}

		private void setTextViewAttrs() {
			textView.setGravity(Gravity.CENTER);
			textView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			textView.setTextSize(18);
			layout.addView(textView);
		}

		private void setImageViewAttrs() {
			imageView.setLayoutParams(new LayoutParams(50, 50));
			layout.addView(imageView);
		}

	}

}
