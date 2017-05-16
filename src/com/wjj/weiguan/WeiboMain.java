package com.wjj.weiguan;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class WeiboMain extends TabActivity {
	RadioGroup radioGroup;
	TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_main);
		
		radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
		tabHost = this.getTabHost();
		
		TabSpec homeSpec = tabHost
				.newTabSpec("home")
				.setIndicator("home")
				.setContent(new Intent(WeiboMain.this,HomeActivity.class));
		
		TabSpec msgSpec = tabHost
				.newTabSpec("msg")
				.setIndicator("msg")
				.setContent(new Intent(WeiboMain.this,MsgActivity.class));
		
		TabSpec meSpec = tabHost
				.newTabSpec("me")
				.setIndicator("me")
				.setContent(new Intent(WeiboMain.this,MeActivity.class));
		
//		TabSpec researchSpec = tabHost
//				.newTabSpec("research")
//				.setIndicator("research")
//				.setContent(new Intent(WeiboMain.this,SearchActivity.class));
		
		TabSpec moreSpec = tabHost
				.newTabSpec("more")
				.setIndicator("more")
				.setContent(new Intent(WeiboMain.this,MoreActivity.class));
		
		tabHost.addTab(homeSpec);
		tabHost.addTab(msgSpec);
		tabHost.addTab(meSpec);
//		tabHost.addTab(researchSpec);
		tabHost.addTab(moreSpec);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					tabHost.setCurrentTabByTag("home");
					break;
				case R.id.radio1:
					tabHost.setCurrentTabByTag("msg");
					break;
				case R.id.radio2:
					tabHost.setCurrentTabByTag("me");
					break;
//				case R.id.radio3:
//					tabHost.setCurrentTabByTag("research");
//					break;
				case R.id.radio4:
					tabHost.setCurrentTabByTag("more");
					break;

				default:
					break;
				}
			}
		});
	}
}
