package android.hmm.lib.tree;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * @author heming
 * @since :JDK ?
 * @version：1.0 Create at:2014-2-24 Description:
 */
public class DemoTreeActivity extends ListActivity implements OnClickListener,
		OnItemClickListener {

	protected TreeViewAdapter adapter = null;
	private TreeDataProvider mDataProvider = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		ListView view = getListView();
		view.setPadding(0, 20, 0, 20);
		view.setDivider(new ColorDrawable(Color.parseColor("#FF343A45")));
		view.setDividerHeight(1);

		adapter = new TreeViewAdapter(getApplicationContext(),
				android.R.layout.activity_list_item, getNodes());
		view.setAdapter(adapter);
	}

	protected List<TreeElement> getNodes() {
		if (null == mDataProvider) {
			mDataProvider = new TreeDataProvider(getApplicationContext());
		}
		return mDataProvider.getDataSource();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		handlerOnListItemClick(l, adapter, v, position, getNodes());
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	public static void handlerOnListItemClick(ListView listview,
			BaseAdapter adapter, View v, int position, List<TreeElement> nodes) {
		Log.i("TreeView", "position:" + position);
		// List<TreeElement> nodes = getNodes();
		if (!nodes.get(position).isHasChild()) {
			if (null != nodes.get(position).getIntent()) {
				listview.getContext().startActivity(nodes.get(position).getIntent());
			} else {
				TreeElement obj = nodes.get(position);
				// obj.forward(getApplicationContext());
				if (null != obj.getOnClick()) {
					obj.getOnClick().onClick(v);
				} else if (obj.getIntent() != null) {
					listview.getContext().startActivity(obj.getIntent());
				}
			}
			return;
		}

		if (nodes.get(position).isExpanded()) {
			nodes.get(position).setExpanded(false);
			TreeElement element = nodes.get(position);
			ArrayList<TreeElement> temp = new ArrayList<TreeElement>();

			for (int i = position + 1; i < nodes.size(); i++) {
				if (element.getLevel() >= nodes.get(i).getLevel()) {
					break;
				}
				temp.add(nodes.get(i));
			}

			nodes.removeAll(temp);
			adapter.notifyDataSetChanged();
		} else {
			TreeElement obj = nodes.get(position);
			obj.setExpanded(true);
			int level = obj.getLevel();
			int nextLevel = level + 1;
			for (TreeElement element : obj.getChilds()) {
				element.setLevel(nextLevel);
				element.setExpanded(false);
				nodes.add(position + 1, element);// 在当前位置的后面添加item
				position++;
			}
			adapter.notifyDataSetChanged();
		}
	}

}
