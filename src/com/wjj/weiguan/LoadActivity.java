package com.wjj.weiguan;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.UsersAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.dao.UserDao;
import com.wjj.weiguan.pojo.User;
import com.wjj.weiguan.util.Tools;

public class LoadActivity extends Activity {
	ImageView image_load;
	UsersAPI mUsersAPI=null;
	User user=null;
	UserDao userDao=null;
	List<User> userList=null;
	int count=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load);
		
		image_load=(ImageView) findViewById(R.id.image_load);
		
		AlphaAnimation animation=new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(5000);
		image_load.setAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				init();
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}
			@Override
			public void onAnimationEnd(Animation arg0) {
				//再次启动程序时，会检查数据库中有没有授权时保存的用户数据。如果有，则无需授权，先更新数据库然后直接登录
				userDao=new UserDao(LoadActivity.this);
				userList = userDao.findAllUsers();
				Oauth2AccessToken mAccessToken=null;
				if (userList!=null && !userList.isEmpty() && Tools.isNetworkAvailable(LoadActivity.this)) {
					for (int i = 0; i < userList.size(); i++) {
						user=userList.get(i);
						mAccessToken=new Oauth2AccessToken();
						mAccessToken.setUid(user.getUser_id());
						mAccessToken.setToken(user.getToken());
						
						mUsersAPI=new UsersAPI(LoadActivity.this, Constants.APP_KEY, mAccessToken);
						long uid=Long.parseLong(mAccessToken.getUid());
						mUsersAPI.show(uid, listener);
					}
				}
			}
		});
	}
	
	private RequestListener listener =new  RequestListener(){
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
					//获取新的数据之后，更新数据库
					userDao.updateUser(user);
					
					count++;//计时器，当List集合中最后一个用户数据更新完成时，才跳转到登录界面
					if (count==userList.size()) {
						//更新完用户数据之后跳转到登录界面
						Intent intent=new Intent(LoadActivity.this,LoginActivity.class);
						startActivity(intent);
						LoadActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        } else {
				Log.i("123", "无用户数据");
			}

		}
		@Override
		public void onWeiboException(WeiboException e) {
			
		}
	};
	
	public void init(){
		Tools.checkNetwork(LoadActivity.this);
		if (Tools.isNetworkAvailable(LoadActivity.this)) {
			UserDao userDao=new UserDao(this);
			List<User> userList = userDao.findAllUsers();
			if (userList==null ||userList.isEmpty()) {
				Intent intent=new Intent(this,OAuthActivity.class);
				startActivity(intent);
				finish();
			}
		}
		
	}

}
