package android.hmm.lib.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014年5月15日
 * Description:  
 */
public class CustomDialog {
	
	public static Dialog createAlertDialog(final Activity context,
			String title, String btnTxt1, String btnTxt2,
			android.content.DialogInterface.OnClickListener onClick1,
			android.content.DialogInterface.OnClickListener onClick2){
	
		return createAlertDialog(context, title, null,-1,null, btnTxt1,btnTxt2,onClick1,onClick2);
	};
	
	public static Dialog createAlertDialog(final Activity context,
			String title, int iconId, String btnTxt1, String btnTxt2,
			android.content.DialogInterface.OnClickListener onClick1,
			android.content.DialogInterface.OnClickListener onClick2){
	
		return createAlertDialog(context, title, null, iconId, btnTxt1,btnTxt2,onClick1,onClick2);
	};
	
	public static Dialog createAlertDialog(final Activity context,
			String title, String message, String btnTxt1, String btnTxt2,
			android.content.DialogInterface.OnClickListener onClick1,
			android.content.DialogInterface.OnClickListener onClick2){
	
		return createAlertDialog(context, title, message, -1, btnTxt1,btnTxt2,onClick1,onClick2);
	};
	
	public static Dialog createAlertDialog(final Activity context,
			String title, String message, int iconId, String btnTxt1, String btnTxt2,
			android.content.DialogInterface.OnClickListener onClick1,
			android.content.DialogInterface.OnClickListener onClick2){
	
		return createAlertDialog(context, title, message, iconId, null, btnTxt1,btnTxt2,onClick1,onClick2);
	};
	
	public static Dialog createAlertDialog(final Activity context,
			String title, String message,int iconId, View view, String btnTxt1, String btnTxt2,
			android.content.DialogInterface.OnClickListener onClick1,
			android.content.DialogInterface.OnClickListener onClick2){
	
		return createAlertDialog(context, title,message,iconId, view, btnTxt1,onClick1,btnTxt2,onClick2,null,null);
	};
	
	public static Dialog createAlertDialog(final Activity context,
			String title, String message, int iconId, String btnTxt1, 
			android.content.DialogInterface.OnClickListener onClick1,
			String btnTxt2,
			android.content.DialogInterface.OnClickListener onClick2,
			String btnTxt3,
			android.content.DialogInterface.OnClickListener onClick3){
	
		return createAlertDialog(context, title, message,iconId, null,
				btnTxt1,onClick1,btnTxt2,onClick2,btnTxt3,onClick3);
	};
	
	public static Dialog createAlertDialog(final Activity context,
			String title, String message,int iconId, View view, String btnTxt1, 
			android.content.DialogInterface.OnClickListener onClick1,
			String btnTxt2,
			android.content.DialogInterface.OnClickListener onClick2,
			String btnTxt3,
			android.content.DialogInterface.OnClickListener onClick3){
	
		return createAlertDialog(context, title, message,iconId, view, 
				btnTxt1,onClick1,
				btnTxt2,onClick2,
				btnTxt3,onClick3,
				null,null,null,
				null,-1,null,
				null,null);
	};
	
	
	public static Dialog createAlertDialog(final Activity context,
			String title, String message, int iconId, View view, 
			String btnTxt1, DialogInterface.OnClickListener onClick1,
			String btnTxt2, DialogInterface.OnClickListener onClick2,
			String btnTxt3, DialogInterface.OnClickListener onClick3,
			
			CharSequence[] multiChoiceItems, boolean[] multiChoiceCheckedItems, 
			DialogInterface.OnMultiChoiceClickListener multiChoiceListener,
			
			CharSequence[] singleChoiceItemsitems, int singleChoiceCheckedItem, 
			DialogInterface.OnClickListener singleChoiceListener,
			
			CharSequence[] items, 
			DialogInterface.OnClickListener itemsListener
			){
		
//		LayoutInflater inflater = context.getLayoutInflater();
//		View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (null != title) {
			builder.setTitle(title);
		}
		if (null != message) {
			builder.setMessage(message);
		}
		if (-1 != iconId) {
			builder.setIcon(iconId);
		}
		if(null != view){
			builder.setView(view);
		}
		if(null != btnTxt1){
			builder.setPositiveButton(btnTxt1, onClick1);
		}
		if(null != btnTxt2){
			builder.setNegativeButton(btnTxt2, onClick2);
		}
		if(null != btnTxt3){
			builder.setNeutralButton(btnTxt3, onClick3);
		}
		if(null != multiChoiceItems){
			builder.setMultiChoiceItems(multiChoiceItems, multiChoiceCheckedItems, multiChoiceListener);
		}
		if(null != singleChoiceItemsitems){
			builder.setSingleChoiceItems(singleChoiceItemsitems, singleChoiceCheckedItem, singleChoiceListener);
		}
		if(null != items){
			builder.setItems(items, itemsListener);
		}
		Dialog dialog = builder.create();
		return dialog;
	};
	
}
