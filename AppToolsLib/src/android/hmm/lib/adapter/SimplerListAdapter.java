package android.hmm.lib.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014年5月13日
 * Description:  
 */
public class SimplerListAdapter extends BaseAdapter{

	private Context context;
	private List<SimplerListCell> list;
	
	public SimplerListAdapter(Context context){
		this.context = context;
	}
	
	public void setList(List<SimplerListCell> list){
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = getConvertView();
//			 android.R.id.icon, android.R.id.text1
			viewHolder.tv = (TextView) convertView.findViewById(android.R.id.text1);
			viewHolder.iv = (ImageView) convertView.findViewById(android.R.id.icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		android.widget.LinearLayout.LayoutParams lp = 
				(android.widget.LinearLayout.LayoutParams) viewHolder.iv.getLayoutParams();
		lp.width = 35;
		lp.height = 40;
		lp.bottomMargin = 5;
		lp.topMargin = 5;
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		viewHolder.iv.setLayoutParams(lp);
		
		viewHolder.iv.setScaleType(ScaleType.FIT_XY);
		viewHolder.iv.setImageDrawable(list.get(position).drawable);
		
		lp = (android.widget.LinearLayout.LayoutParams) viewHolder.tv.getLayoutParams();
		lp.gravity = Gravity.CENTER;
		viewHolder.tv.setLayoutParams(lp);
		
		viewHolder.tv.setText(list.get(position).text);
		viewHolder.tv.setTextColor(Color.WHITE);
		viewHolder.tv.setTextSize(20);
		return convertView;
	}
	
	private View getConvertView(){
		return LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, null);
	}
	
	private class ViewHolder{
		private ImageView iv;
		private TextView tv;
	}
	
	public class SimplerListCell{
		private String text;
		private Drawable drawable;
		
		public SimplerListCell(String text, Drawable drawable){
			this.text = text;
			this.drawable = drawable;
		}
		
		public void setText(String text){
			this.text = text;
		}
		
		public void setDrawable(Drawable drawable){
			this.drawable = drawable;
		}
	}

}
