package android.hmm.lib.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.hmm.lib.R;
import android.hmm.lib.adapter.SimplerListAdapter;
import android.hmm.lib.dialog.CustomDialog;
import android.hmm.lib.model.AppInfo;
import android.hmm.lib.tclass.IThreadTask;
import android.hmm.lib.utils.AnimationHelper;
import android.hmm.lib.utils.AppHelper;
import android.hmm.lib.utils.LogHelper;
import android.hmm.lib.widget.BaseTabLayout;
import android.hmm.lib.widget.LoadingView;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @Title: ApplicationLayout.java
 * @Package android.box.systools.layout
 * @Description: TODO(用一句话描述该文件做什么)
 * @author heming
 * @date May 4, 2014 -- 7:31:28 PM
 * @version V1.0
 */
public class ApplicationLayout extends BaseTabLayout {

	private LoadingView mLoadingView;
	private List<AppInfo> listApp = new ArrayList<AppInfo>();

	public ApplicationLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		createViews();
		super.initBaseViews();

		mLoadingView = new LoadingView(context, handler, this);
		mLoadingView.setTaskType(LoadingView.type_fast);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.hmm.lib.widget.BaseViewPager#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		if(mLoadingView != null){
			mLoadingView.destoryData();
			mLoadingView = null;
		}
		super.onDetachedFromWindow();
	}

	/**********************************************************
	 * (non-Javadoc)
	 * 
	 * @see android.hmm.lib.widget.BaseTabLayout#handleRecieveMessage(android.os.Message)
	 *********************************************************/
	@Override
	public void handleRecieveMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleRecieveMessage(msg);
		if (msg.what == EVENT_PAGE_CHANGE) {
			submitTask();
		}
	}

	private void createViews() {
		createItemListView("用户应用", 3);
		createItemListView("所有应用", 1);
		createItemListView("正在运行", 5);
		createItemListView("SD卡应用", 4);
		createItemListView("系统应用", 2);
	}
	
	private void createItemListView(String title, int tag){
		ListView listView = new ListView(getContext());
		listView.setTag(tag);
		listView.setOnItemClickListener(listViewItemClick);
		super.setBodyView(title, listView);
	}
	
	private AdapterView.OnItemClickListener listViewItemClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			showAppDetailInfo(listApp.get(position));
		}
	}; 

	private void showAppDetailInfo(AppInfo appInfo){
		String pkg = appInfo.getPackageName();
		
		String title = appInfo.getAppName();
		String message= null;
		int iconId = android.R.drawable.btn_star;
		iconId = android.R.drawable.ic_dialog_info;
		View view = null;
		String btnTxt1 = getResources().getString(android.R.string.yes);
		String btnTxt2 = null;
		String btnTxt3 = null;
		
//		view = LayoutInflater.from(context).inflate(android.R.layout.simple_selectable_list_item, null);
//		view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_activated_2, null);
		
		int resource = android.R.layout.simple_list_item_activated_2;
		
		if(!AppHelper.existPackageName(getContext(), pkg)){
			message = "this " + appInfo.getAppName() + " is uninstalled";
			submitTask();
		} else{
			ListView lv = new ListView(getContext());
			view = lv;
			List<Map<String, String>> data = getAppDetailInfo(appInfo);
			String[] from = new String[] {"title","message"};
			int[] to = new int[] {android.R.id.text1, android.R.id.text2};
			
			SimpleAdapter adapter = new SimpleAdapter(getContext(), data, resource, from, to);
			lv.setAdapter(adapter);
		}
		
		android.content.DialogInterface.OnClickListener onClick1 = 
				new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				};
				
		android.content.DialogInterface.OnClickListener onClick2 = new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		};

		android.content.DialogInterface.OnClickListener onClick3 = new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		};
		
		CustomDialog.createAlertDialog(activity, title, message, iconId, view, 
				btnTxt1, onClick1, btnTxt2, onClick2, btnTxt3, onClick3);
	}
	
	
	private List<Map<String, String>> getAppDetailInfo(AppInfo appInfo){
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		if(null != appInfo.getPackageName()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("package", appInfo.getPackageName());
		}
		if(null != appInfo.getClassName()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("clsname", appInfo.getClassName());
		}
		if(null != appInfo.getPackageName()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("package", appInfo.getPackageName());
		}
		if(null != appInfo.getPackageName()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("package", appInfo.getPackageName());
		}
		return data;
	}
	
	
	public void updateListView(ListView view) {
		LogHelper.log(getContext(), "--->updateListView");
		AnimationHelper.setAnimationOfAbsListView(activity, view, R.anim.list_item_animation);
		SimplerListAdapter mAdapter = new SimplerListAdapter(activity);
		view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// TODO Auto-generated method stub
			}
		});

		List<SimplerListAdapter.SimplerListCell> listItems = 
				new ArrayList<SimplerListAdapter.SimplerListCell>();// 创建一个List集合
		for (int i = 0; i < listApp.size(); i++) {// 遍历数组
			SimplerListAdapter.SimplerListCell cell = 
					mAdapter.new SimplerListCell(listApp.get(i).getAppName(),listApp.get(i).getIcon());
			Map<String, Object> map = new HashMap<String, Object>();// 创建哈希表
			map.put("image", listApp.get(i).getIcon());
			map.put("title", listApp.get(i).getAppName());
			listItems.add(cell);
		}
		mAdapter.setList(listItems);
		view.setAdapter(mAdapter);
		requestLayout();
		
		setTitle(listItems.size()+"");
	}

	private void setTitle(String size){
		if(dContext != null){
			Window win = dContext.getWindow();
			String title = win.getAttributes().getTitle().toString().split("\\|")[0];
			title += "|"+ getBodyTitle() + "(" + size + ")";
			win.setTitle(title);
		}
	}
	
	public void submitTask() {
		mLoadingView.submitTask(new IThreadTask() {

			@Override
			public void prepare() {
				// TODO Auto-generated method stub
				setTitle("*");
				((ListView) getBodyView()).setAdapter(null);
				listApp.clear();
				mLoadingView.showLoadingView();
			}
			
			@Override
			public void process() {
				// TODO Auto-generated method stub
				start2LoadingListApp();
			}

			@Override
			public void handleResult() {
				// TODO Auto-generated method stub
				updateListView((ListView) getBodyView());
				mLoadingView.closeLoadingView();
			}

			@Override
			public void handleError() {
				// TODO Auto-generated method stub
			}
		});
	}

	private void start2LoadingListApp() {
		int type = Integer.valueOf(getBodyView().getTag().toString());
		listApp = AppHelper.getCategoryApp(getContext(), type);
	}
	
}
