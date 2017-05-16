package com.wjj.weiguan.adapter;

import java.util.List;

import com.wjj.weiguan.R;
import com.wjj.weiguan.pojo.RemindItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RemindAdapter extends BaseAdapter {
	Context context;
	List<RemindItem> list=null;
	
	public RemindAdapter(Context context,List<RemindItem> list){
		this.list=list;
		this.context=context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContentHolder holder=null;
		if (convertView==null) {
			holder=new ContentHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.remind_item, parent,false);
			holder.image_remind=(ImageView) convertView.findViewById(R.id.image_remind);
			holder.text=(TextView) convertView.findViewById(R.id.tv_remind);
			holder.tv_num=(TextView) convertView.findViewById(R.id.tv_num);
			holder.image_arrow=(ImageView) convertView.findViewById(R.id.image_arrow);
			convertView.setTag(holder);
		} else {
			holder=(ContentHolder) convertView.getTag();
		}
		holder.image_remind.setImageResource(list.get(position).getReId());
		holder.text.setText(list.get(position).getText());
		String numStr=list.get(position).getTv_num();
		int num=Integer.parseInt(numStr);
		if (num>0) {
			holder.tv_num.setText("("+String.valueOf(num)+")");
			holder.tv_num.setVisibility(View.VISIBLE);
			holder.image_arrow.setVisibility(View.INVISIBLE);
		} else {
			holder.tv_num.setVisibility(View.INVISIBLE);
			holder.image_arrow.setVisibility(View.VISIBLE);
		}
		
		
		return convertView;
	}

	class ContentHolder{
		public ImageView image_remind;
		public TextView text;
		public TextView tv_num;
		public ImageView image_arrow;
	}
}
