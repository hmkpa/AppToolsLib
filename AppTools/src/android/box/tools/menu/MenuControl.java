package android.box.tools.menu;

import java.util.ArrayList;
import java.util.List;

import android.hmm.lib.AboutSysActivity;
import android.hmm.lib.intent.IntentHelper;
import android.hmm.lib.intent.IntentSysHelper;
import android.hmm.lib.menu.MenuBodyItem;
import android.hmm.lib.menu.MenuDialog;
import android.hmm.lib.menu.MenuTitleItem;
import android.hmm.lib.utils.LogHelper;
import android.hmm.lib.utils.ToastHelper;

import android.app.Activity;
import android.box.tools.ExampleActivity;
import android.box.tools.R;
import android.box.tools.TestActivity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;


/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-24
 * Description:  
 */
public class MenuControl {

	private MenuDialog menu;
	private static Activity context;

	private static String body_str_101 = "demo";
	private static String body_str_102 = "test";

	private static String body_str_201 = "计算器";
	
	private static String body_str_401 = "设置";
	private static String body_str_402 = "原生设置";
	private static String body_str_403 = "关于";
	private static String body_str_404 = "退出";

	private static String title_str_001 = "菜单一";
	private static String title_str_002 = "菜单二";
	private static String title_str_003 = "菜单三";
	private static String title_str_004 = "菜单四";

	public MenuControl(Activity context) {
		MenuControl.context = context;
	}

	private void createMenu() {
		menu = new MenuDialog(context);
		menu.setMenuTitle(createMenuTitle());
		menu.initViews();
		menu.setWindowConfig();
	}

	public void titleItemSelected(int arg2) {
		menu.titleItemSelected(arg2);
	}

	public void showMenu() {
		if (null == menu) {
			createMenu();
		}
		menu.show();
	}

	public boolean isGetFocus(AdapterView<?> arg0) {
		boolean result = false;
		result = isTitleGetFocus(arg0);
		if (!result) {
			result = isBodyGetFocus(arg0);
		}
		return result;
	}

	private boolean isTitleGetFocus(AdapterView<?> arg0) {
		boolean result = false;
		if (null != menu.getTitleGridView() && arg0 == menu.getTitleGridView()) {
			result = true;
		}
		return result;
	}

	private boolean isBodyGetFocus(AdapterView<?> arg0) {
		boolean result = false;
		if (null != menu.getBodyGridView() && arg0 == menu.getBodyGridView()) {
			result = true;
		}
		return result;
	}

	public static List<MenuTitleItem> createMenuTitle() {
		List<MenuTitleItem> list = new ArrayList<MenuTitleItem>();

		List<MenuBodyItem> bodyList = new ArrayList<MenuBodyItem>();
		bodyList.add(getMenuBodyItem(body_str_101, R.drawable.ic_launcher, new OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentHelper.startEvent(context, ExampleActivity.class);
			}
		}));
		bodyList.add(getMenuBodyItem(body_str_102, R.drawable.ic_launcher, new OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentHelper.startEvent(context, TestActivity.class);
			}
		}));
		list.add(getMenuTitleItem(title_str_001, 0, bodyList));

		bodyList = new ArrayList<MenuBodyItem>();
		bodyList.add(getMenuBodyItem(body_str_201, R.drawable.ic_launcher, new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		}));
		list.add(getMenuTitleItem(title_str_002, 1, bodyList));
		list.add(getMenuTitleItem(title_str_003, 2, null));

		bodyList = new ArrayList<MenuBodyItem>();
		bodyList.add(getMenuBodyItem(body_str_401, R.drawable.ic_launcher, new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		}));
		bodyList.add(getMenuBodyItem(body_str_402, R.drawable.ic_launcher, new OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentSysHelper.startSetting(context);
			}
		}));
		bodyList.add(getMenuBodyItem(body_str_403, R.drawable.ic_launcher, new OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentHelper.startEvent(context, AboutSysActivity.class);
			}
		}));
		bodyList.add(getMenuBodyItem(body_str_404, R.drawable.ic_launcher, new OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentHelper.exitProgram();
			}
		}));
		list.add(getMenuTitleItem(title_str_004, 4, bodyList));

		return list;
	}

	private static MenuTitleItem getMenuTitleItem(String name, int flag, List<MenuBodyItem> bodyList) {
		MenuTitleItem item = new MenuTitleItem();
		item.setName(name);
		item.setFlag(flag);
		item.setBodyList(bodyList);
		return item;
	}

	private static MenuBodyItem getMenuBodyItem(String name, int iconId, OnClickListener click) {
		MenuBodyItem item = new MenuBodyItem();
		item.setName(name);
		item.setIconId(iconId);
		item.setClick(click);
		return item;
	}

	public void menuBodyItemClick(Context context, String name, int page) {
		LogHelper.log(context, "the menu body which clicked is:" + name);
		if (name == null) {
			ToastHelper.showCustomToast(context, "the name is null");
		} else if (name.equals(body_str_102)) {

		}
	}

}
