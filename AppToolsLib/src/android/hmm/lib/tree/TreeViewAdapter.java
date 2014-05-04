package android.hmm.lib.tree;

import java.util.List;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hmm.lib.R;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;


/**
 * @author HEMING
 * Time:2013-7-31
 *
 */
public class TreeViewAdapter extends ArrayAdapter<TreeElement> {

	private List<TreeElement> listData;
	private Bitmap mIconCollapse;
	private Bitmap mIconExpand;

	public TreeViewAdapter(Context context, int textViewResourceId, List<TreeElement> objects) {
		super(context, textViewResourceId, objects);
		listData = objects;
		mIconCollapse = BitmapFactory.decodeResource(context.getResources(), R.drawable.framework_tree_plus);
		mIconExpand = BitmapFactory.decodeResource(context.getResources(), R.drawable.framework_tree_cut);
	}

	@Override
	public int getCount() {
		return null == listData ? 0 : listData.size();
	}

	@Override
	public TreeElement getItem(int position) {
		if (listData != null && listData.size() > position) {
			listData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		TreeElement obj = listData.get(position);
//		if (null == convertView) {
		holder = new ViewHolder();
		holder.layout = holder.createItemUI(getContext());
		holder.text = holder.createItemTextView(getContext());
		holder.icon = holder.createItemImageView(getContext());
		holder.layout.addView(holder.icon);
		holder.layout.addView(holder.text);
		convertView = holder.layout;
		convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}

		int level = obj.getLevel();
		holder.icon.setPadding(25 * (level + 1), holder.icon.getPaddingTop(), 0, holder.icon.getPaddingBottom());
		holder.text.setText(obj.getTitle());
		if (obj.isHasChild() && (obj.isExpanded() == false)) {
			holder.icon.setImageBitmap(mIconCollapse);
		} else if (obj.isHasChild() && (obj.isExpanded() == true)) {
			holder.icon.setImageBitmap(mIconExpand);
		} else if (!obj.isHasChild()) {
			holder.icon.setImageBitmap(mIconCollapse);
			holder.icon.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView text;
		private ImageView icon;
		private LinearLayout layout;

		private LinearLayout createItemUI(Context context) {
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setGravity(Gravity.CENTER_VERTICAL);
			return layout;
		}

		private ImageView createItemImageView(Context context) {
			ImageView icon = new ImageView(context);
			icon.setScaleType(ScaleType.FIT_XY);
			return icon;
		}

		private TextView createItemTextView(Context context) {
			TextView tv = new TextView(context);
			tv.setSingleLine(true);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextColor(Color.WHITE);
			tv.setPadding(6, 5, 0, 5);
			tv.setTextSize(24);
			return tv;
		}
	}

}
