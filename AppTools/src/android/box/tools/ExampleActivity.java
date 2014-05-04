package android.box.tools;

import java.util.ArrayList;
import java.util.List;

import android.box.tools.demo.BaseSimplerAnim;
import android.box.tools.demo.ListViewItemFlyIn;
import android.box.tools.demo.DemoTextViewLayout;
import android.box.tools.dialog.DemoDialog;
import android.content.DialogInterface;
import android.hmm.lib.tree.DemoTreeActivity;
import android.hmm.lib.tree.TreeElement;
import android.hmm.lib.tree.TreeElementHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author heming
 * @since :JDK ?
 * @version：1.0 Create at:2014-2-24 Description:
 */
public class ExampleActivity extends DemoTreeActivity {

	private List<TreeElement> nodes = new ArrayList<TreeElement>();
	private static final int EVENT_OPEN_DIALOG = 0x0123;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public java.util.List<android.hmm.lib.tree.TreeElement> getNodes() {
		// List<TreeElement> nodes = getDataSource();
		if (null == nodes || nodes.size() == 0) {
			nodes = getDataSource();
		}
		return nodes;
	};

	private List<TreeElement> getDataSource() {
		int id = 0;
		List<TreeElement> nodes = new ArrayList<TreeElement>();

		TreeElement element = null;
		element = TreeElementHelper.addTreeElement(id, "Anim", nodes);

		String title = "四种简单动画";
		element.addChild(TreeElementHelper.addTreeElement(id, title,
				TreeElementHelper.getDemoDialogClick(handler,
						EVENT_OPEN_DIALOG, new BaseSimplerAnim(
								ExampleActivity.this), title)));

		title = "ListView动画";
		element.addChild(TreeElementHelper.addTreeElement(id, title,
				TreeElementHelper.getDemoDialogClick(handler,
						EVENT_OPEN_DIALOG, new ListViewItemFlyIn(
								ExampleActivity.this), title)));

		element = TreeElementHelper.addTreeElement(id, "TextView", nodes);
		title = "TextView效果";
		element.addChild(TreeElementHelper.addTreeElement(id, title,
				TreeElementHelper.getDemoDialogClick(handler,
						EVENT_OPEN_DIALOG, new DemoTextViewLayout(
								ExampleActivity.this), title)));

		TreeElementHelper.addTreeElement(id, "test4", nodes);
		TreeElementHelper.addTreeElement(id, "test5", nodes);
		TreeElementHelper.addTreeElement(id, "test6", nodes);
		TreeElementHelper.addTreeElement(id, "test7", nodes);
		TreeElementHelper.addTreeElement(id, "test8", nodes);
		return nodes;
	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == EVENT_OPEN_DIALOG) {
				openDialog(msg);
			}

			return false;
		}
	});

	private void openDialog(Message msg) {
		Bundle bundle = msg.getData();
		View view = (View) msg.obj;
		String title = bundle.getString("title");
		DemoDialog dialog = new DemoDialog(this);
		dialog.showDialog(view, title);
		dialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
	}

}
