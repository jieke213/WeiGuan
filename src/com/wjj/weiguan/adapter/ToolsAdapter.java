package com.wjj.weiguan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjj.weiguan.R;
import com.wjj.weiguan.pojo.ToolItem;

public class ToolsAdapter extends BaseAdapter {
	List<ToolItem> list=null;
	Context context;
	
	public ToolsAdapter(Context context,List<ToolItem> list){
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContentHolder holder=null;
		if (convertView==null) {
			holder=new ContentHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.tools_item, parent, false);
			holder.image_tools=(ImageView) convertView.findViewById(R.id.image_tools);
			holder.tv_tools=(TextView) convertView.findViewById(R.id.tv_tools);
			convertView.setTag(holder);
		} else {
			holder=(ContentHolder) convertView.getTag();
		}
		
		holder.image_tools.setImageResource(list.get(position).getReId());
		holder.tv_tools.setText(list.get(position).getText());

		return convertView;
	}

	class ContentHolder{
		public ImageView image_tools;
		public TextView tv_tools;
	}
}
