package com.wjj.weiguan;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.UsersAPI;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.dao.UserDao;
import com.wjj.weiguan.pojo.User;

public class OAuthActivity extends Activity{
	AuthInfo mAuthInfo=null;
	SsoHandler mSsoHandler=null;
	Oauth2AccessToken mAccessToken=null;
	UsersAPI mUsersAPI=null;
	User user=null;
	UserDao userDao=null;
	Dialog dialog=null;
	boolean isDismiss=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.oauth);
		
		mAuthInfo=new AuthInfo(OAuthActivity.this, Constants.APP_KEY, 
				Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(OAuthActivity.this, mAuthInfo);
		
		userDao=new UserDao(OAuthActivity.this);
		user=new User();
		
		View dialogView=View.inflate(this, R.layout.oauth_dialog, null);
		dialog=new Dialog(this,R.style.auth_style);
		dialog.setContentView(dialogView);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		
		//授权
		Button btn_oauth=(Button) dialogView.findViewById(R.id.btn_oauth);
		btn_oauth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mSsoHandler.authorize(new AuthListener());
			}
		});
	}
	
	class AuthListener implements WeiboAuthListener{

		@Override
		public void onCancel() {
			
		}

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
	        if (mAccessToken.isSessionValid()) {
	        	toast("授权成功");
	        	long validPeriod=mAccessToken.getExpiresTime()-System.currentTimeMillis();
	        	long day=validPeriod/(1000*60*60*24);
	        	Log.i("123", "有效期是："+String.valueOf(day)+"天");
	        	Log.i("123", "uid="+mAccessToken.getUid());
	        	user.setToken(mAccessToken.getToken());
	        	getUserInfo(mAccessToken);
	        } else {
			    // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
	            String code = values.getString("code", "有误");
	            toast(code);
	        }
		}
		@Override
		public void onWeiboException(WeiboException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getUserInfo(Oauth2AccessToken mAccessToken){
		mUsersAPI=new UsersAPI(OAuthActivity.this, Constants.APP_KEY, mAccessToken);
		long uid=Long.parseLong(mAccessToken.getUid());
		mUsersAPI.show(uid, new Listener());
	}
	
	class Listener implements RequestListener{
		@Override
		public void onWeiboException(WeiboException e) {
			Log.i("123", e.getMessage());
			toast("应用审核通过后才能支持多用户登录");
			e.printStackTrace();
			dialog.dismiss();
			OAuthActivity.this.finish();
		}
		
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				Log.i("123", "用户信息："+response);
				try {
					JSONObject jsonObject=new JSONObject(response);
					user.setUser_id(jsonObject.getString("id"));
					user.setUser_name(jsonObject.getString("screen_name"));
					user.setDescription(jsonObject.getString("description"));
					user.setFollowers_count(jsonObject.getString("followers_count"));
					user.setFriends_count(jsonObject.getString("friends_count"));
					user.setStatuses_count(jsonObject.getString("statuses_count"));
					user.setUser_head(jsonObject.getString("profile_image_url"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				List<User> list = userDao.findAllUsers();
				int count=0;
				if (list!=null) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getUser_id().equals(user.getUser_id())) {
							count++;
						}
					}
				}
				if (count==0) {
					Log.i("1234", "插入："+user.getUser_id());
					userDao.insertUser(user);
				} else {
					userDao.updateUser(user);
					Log.i("1234", "更新："+user.getUser_id());
				}
				dialog.dismiss();
				
				//跳转到登录界面
				Intent intent=new Intent(OAuthActivity.this,LoginActivity.class);
				startActivity(intent);
				OAuthActivity.this.finish();
	        } else {
				toast("无用户数据");
				Log.i("123", "无用户数据");
			}

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
	        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }

	}
	
	private void toast(String str){
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
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
