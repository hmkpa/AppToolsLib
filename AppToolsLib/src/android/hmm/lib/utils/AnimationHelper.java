package android.hmm.lib.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;

/*********************************************************
 * @Title: AnimationHelper.java
 * @Package android.hmm.lib.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author heming
 * @date May 4, 2014 -- 8:00:40 PM
 * @version V1.0
 *********************************************************/
public class AnimationHelper {

	public static void setAnimationOfAbsListView(Context context,
			AbsListView view) {
		// setAnimationOfAbsListView(context, view, R.anim.list_item_animation_1);
	}

	/***************************************************
	 * 给listView添加动画
	 * 每次刷新数据时，都需要设置，不然只在第一次才会出现动画
	 * 
	 * @param context
	 * @param view
	 * @param id:动画文件的id
	 ****************************************************/
	public static void setAnimationOfAbsListView(Context context,
			AbsListView view, int id) {
		Animation animation = AnimationUtils.loadAnimation(context, id);
		// 得到一个LayoutAnimationController对象；
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		// 设置控件显示的顺序；
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		// 设置控件显示间隔时间；
		lac.setDelay(0.2f);
		// 为ListView设置LayoutAnimationController属性；
		view.setLayoutAnimation(lac);
	}

	
}
