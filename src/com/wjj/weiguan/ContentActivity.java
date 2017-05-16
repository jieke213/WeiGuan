package com.wjj.weiguan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.StatusesAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.LoginActivity.UserSession;

public class ContentActivity extends Activity {
	StatusesAPI mStatusesAPI=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content);
		
//		Intent intent = getIntent();
//		if (intent!=null) {
//			Bundle bundle = intent.getExtras();
//			if (bundle!=null) {
//				if (bundle.containsKey("weiboId")) {
//					String weiboId = bundle.getString("weiboId");
//					String userId = bundle.getString("userId");
//					long id = Long.parseLong(weiboId);
//					long uid = Long.parseLong(userId);
//					Log.i("123", "微博ID="+id+"，用户ID="+uid);
//					init(uid,id);
//				}
//			}
//		}
	}
	
	public void init(long uid, long id){
		Oauth2AccessToken mAccessToken=new Oauth2AccessToken();
		mAccessToken.setToken(UserSession.nowUser.getToken());
		mStatusesAPI=new StatusesAPI(ContentActivity.this, Constants.APP_KEY,mAccessToken);
		mStatusesAPI.go(uid, id, new Listener());
	}
	
	class Listener implements RequestListener{

		@Override
		public void onComplete(String response) {
			Log.i("123", response);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Log.i("123", "出现异常"+arg0.getMessage());
			arg0.printStackTrace();
			
		}
		
	}
}
