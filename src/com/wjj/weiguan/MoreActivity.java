package com.wjj.weiguan;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wjj.weiguan.adapter.ToolsAdapter;
import com.wjj.weiguan.pojo.ToolItem;

public class MoreActivity extends Activity {
	Button btn_photo;
	Button btn_baidumap;
	ListView lv_tools;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more);
		
		lv_tools=(ListView) findViewById(R.id.lv_tools);
		
		final List<ToolItem> list=new ArrayList<ToolItem>();
		list.add(new ToolItem(R.drawable.baidu, "百度地图"));
		list.add(new ToolItem(R.drawable.phone, "手机号查询"));
		ToolsAdapter adapter=new ToolsAdapter(MoreActivity.this,list);
		lv_tools.setAdapter(adapter);
		
		lv_tools.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ToolItem item = list.get(position);
				switch (item.getReId()) {
				case R.drawable.baidu:
					Intent intent2=new Intent(MoreActivity.this,IndoorLocationActivity.class);
					startActivity(intent2);
					break;
				case R.drawable.phone:
					Intent intent=new Intent(MoreActivity.this,PhotoActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}
	
	private long mExitTime;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}	
		return super.onKeyDown(keyCode, event);
	}
	
}
