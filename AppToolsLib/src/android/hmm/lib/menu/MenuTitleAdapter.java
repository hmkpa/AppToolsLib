package android.hmm.lib.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MenuTitleAdapter extends BaseAdapter {

	private Context mContext;
	protected int mFocusIndex = 1;;
	private List<View> mTVMenuTitle = new ArrayList<View>();
	private List<Integer> positionSet = new ArrayList<Integer>();
	private List<MenuTitleItem> title = new ArrayList<MenuTitleItem>();

	public MenuTitleAdapter(Context context, List<MenuTitleItem> title) {
		mContext = context;
		this.title = title;
	}

	@Override
	public int getCount() {
		return (title != null) ? title.size() : 0;
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
		if (positionSet == null) {
			arg1 = createTextView(arg0);
		} else {
			if (positionSet.contains(arg0)) {
				arg1 = mTVMenuTitle.get(positionSet.get(arg0));
			} else {
				if (positionSet.size() > 10) {
					positionSet.remove(0);
					mTVMenuTitle.remove(0);
				}
				arg1 = createTextView(arg0);
				positionSet.add(arg0);
				mTVMenuTitle.add(arg1);
			}
		}
		return arg1;
	}

	public TextView createTextView(int arg0) {
		TextView textView = new TextView(mContext);
		textView.setGravity(Gravity.CENTER);
		textView.setText(title.get(arg0).getName());
		textView.setTextSize(18);
		textView.setTextColor(Color.BLACK);
		textView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return textView;
	}
}
