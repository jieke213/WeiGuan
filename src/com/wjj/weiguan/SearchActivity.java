package com.wjj.weiguan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.SearchAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.LoginActivity.UserSession;

public class SearchActivity extends Activity implements OnClickListener{
	Button btn_search;
	EditText et_search;
	SearchAPI mSearchAPI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		
		initView();
		
		Oauth2AccessToken mAccessToken=new Oauth2AccessToken();
		mAccessToken.setToken(UserSession.nowUser.getToken());
		mAccessToken.setUid(UserSession.nowUser.getUser_id());
		mSearchAPI=new SearchAPI(SearchActivity.this, Constants.APP_KEY, mAccessToken);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_search:
			String content=et_search.getText().toString();
			if (!content.trim().equals("")) {
				mSearchAPI.users(content, 10, listener);
			} else {
				Toast.makeText(SearchActivity.this, "查询内容不能为空！", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}
	
	private RequestListener listener=new RequestListener() {
		@Override
		public void onComplete(String response) {
			Log.i("123", "搜索信息："+response);
		}
		
		@Override
		public void onWeiboException(WeiboException e) {
			Log.i("123", "有误："+e.getMessage());
		}
	};
	
	private void initView() {
		btn_search=(Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		et_search=(EditText) findViewById(R.id.et_search);
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
