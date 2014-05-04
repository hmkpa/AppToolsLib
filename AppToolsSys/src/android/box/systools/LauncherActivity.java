package android.box.systools;

import java.util.ArrayList;
import java.util.List;

import android.box.systools.control.IntentControl;
import android.hmm.lib.AboutSysActivity;
import android.hmm.lib.ConfigActivity;
import android.hmm.lib.tree.DemoTreeActivity;
import android.hmm.lib.tree.TreeElement;
import android.hmm.lib.tree.TreeElementHelper;
import android.hmm.lib.tree.TreeViewAdapter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LauncherActivity extends ConfigActivity implements
		OnItemClickListener {

	private TreeViewAdapter adapter = null;
	private List<TreeElement> nodes = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adapter = new TreeViewAdapter(this,
				android.R.layout.activity_list_item, getNodes());
		android.widget.ListView listview = (android.widget.ListView) findViewById(R.id.listview_act_main);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		IntentControl.start2DemoDialog(this, item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// showToast("onPrepareOptionsMenu");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		// showToast("onOptionsMenuClosed");
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		DemoTreeActivity.handlerOnListItemClick((ListView) arg0, adapter, arg1,
				arg2, getNodes());
	}

	public List<TreeElement> getNodes() {
		if (null == nodes) {
			nodes = new ArrayList<TreeElement>();
			int id = 0;
			TreeElementHelper.addTreeElement(this, id, "关于系统",
					AboutSysActivity.class, nodes);
			TreeElementHelper.addTreeElement(id, "test3", nodes);
			TreeElementHelper.addTreeElement(id, "test4", nodes);
			TreeElementHelper.addTreeElement(id, "test5", nodes);
			TreeElementHelper.addTreeElement(id, "test6", nodes);
			TreeElementHelper.addTreeElement(id, "test7", nodes);
			TreeElementHelper.addTreeElement(id, "test8", nodes);
			TreeElementHelper.addTreeElement(id, "test9", nodes);
			TreeElementHelper.addTreeElement(id, "test10", nodes);
			TreeElementHelper.addTreeElement(id, "test11", nodes);
			TreeElementHelper.addTreeElement(id, "test12", nodes);
		}
		return nodes;
	}

}
