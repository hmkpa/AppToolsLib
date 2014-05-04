package android.hmm.lib.tree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hmm.lib.tree.ITreeDataProvider;
import android.hmm.lib.tree.TreeElement;

/**
 * @author HMM
 * 树形控件数据提供类
 */
public class TreeDataProvider implements ITreeDataProvider {
	private Context context = null;
	private List<TreeElement> nodes = new ArrayList<TreeElement>();

	public TreeDataProvider(Context context) {
		this.context = context;
	}

	private void initDataSource() {
		int id = 0;
		addTreeElement(id, "view");
		addTreeElement(id, "test2");
		addTreeElement(id, "test3");
		addTreeElement(id, "test4");
		addTreeElement(id, "test5");
		addTreeElement(id, "test6");
		addTreeElement(id, "test7");
		addTreeElement(id, "test8");
		addTreeElement(id, "test9");
		addTreeElement(id, "test10");
		addTreeElement(id, "test11");
		addTreeElement(id, "test12");
		addTreeElement(id, "test13");
		addTreeElement(id, "test14");
		addTreeElement(id, "test15");

//		TreeElement secondlement1 = new TreeElement(flagId, "TextView");
//		element1.addChild(secondlement1);
//		TreeElement fisrtelement6 = new TreeElement(flagId, "多媒体");
//		nodes.add(fisrtelement6);
	}

	private void addTreeElement(int id, String title) {
		String flagId = String.valueOf(id);
		id += 0x1000;
		TreeElement element = new TreeElement(flagId, title);
		nodes.add(element);
	}

	protected Intent getIntent(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		return intent;
	}

	@Override
	public void foward(String caption) throws Exception {

	}

	@Override
	public List<TreeElement> getDataSource() {
		if (null == nodes || nodes.size() == 0) initDataSource();
		return nodes;
	}

}
