package android.hmm.lib.tree;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class TreeElementHelper {

	private static final int itemSpaceNumber = 0x1000;

	public static TreeElement addTreeElement(int id, String title,
			List<TreeElement> nodes) {
		TreeElement element = new TreeElement(getId(id), title);
		nodes.add(element);
		return element;
	}

	public static TreeElement addTreeElement(Context context, int id,
			String title, Class<? extends Activity> cls, List<TreeElement> nodes) {
		TreeElement element = new TreeElement(getId(id), title, getIntent(
				context, cls));
		nodes.add(element);
		return element;
	}

	public static TreeElement addTreeElement(int id, String title,
			Intent intent, List<TreeElement> nodes) {
		TreeElement element = new TreeElement(getId(id), title, intent);
		nodes.add(element);
		return element;
	}

	public static TreeElement addTreeElement(int id, String title,
			android.view.View.OnClickListener onClick) {
		return addTreeElement(id, title, onClick, null);
	}

	public static TreeElement addTreeElement(int id, String title,
			android.view.View.OnClickListener onClick, List<TreeElement> nodes) {
		TreeElement element = new TreeElement(getId(id), title, onClick);
		if (null != nodes) {
			nodes.add(element);
		}
		return element;
	}

	public static String getId(int id) {
		String flagId = String.valueOf(id);
		id += itemSpaceNumber;
		return flagId;
	}

	public static android.view.View.OnClickListener getDemoDialogClick(
			final Handler handler, final int eventid, final View view,
			final String title) {
		android.view.View.OnClickListener click = new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendToTarget(handler, eventid, view, title);
			}
		};
		return click;
	}

	private static void sendToTarget(Handler handler, int eventid, View view,
			String title) {
		Message msg = handler.obtainMessage();
		msg.what = eventid;
		msg.obj = view;
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		msg.setData(bundle);
		msg.sendToTarget();
	}

	public static Intent getIntent(Context context,
			Class<? extends Activity> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		return intent;
	}

}
