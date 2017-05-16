package com.wjj.weiguan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.StatusesAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.LoginActivity.UserSession;
import com.wjj.weiguan.adapter.HomeAdapter;
import com.wjj.weiguan.pojo.WeiboInfo;
import com.wjj.weiguan.util.RefreshableView;
import com.wjj.weiguan.util.RefreshableView.PullToRefreshListener;

public class HomeActivity extends Activity implements OnClickListener{
	ImageView refresh,write;
	TextView tv_username;
	ListView lv_home;
	RelativeLayout layout_progress;
	StatusesAPI mStatusesAPI=null;
	List<WeiboInfo> weiboList=null;
	HomeAdapter adapter=null;
	RefreshableView refreshableView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		
		initView();
		layout_progress.setVisibility(View.VISIBLE);//进度加载出现
		
		Oauth2AccessToken mAccessToken=new Oauth2AccessToken();
		Log.i("123", "token="+UserSession.nowUser.getToken()+",uid="+UserSession.nowUser.getUser_id());
		mAccessToken.setToken(UserSession.nowUser.getToken());
		mAccessToken.setUid(UserSession.nowUser.getUser_id());		
		mStatusesAPI=new StatusesAPI(HomeActivity.this, Constants.APP_KEY, mAccessToken);
		mStatusesAPI.friendsTimeline(0, 0, 20, 1, false, 0, false, listener);
	}
	
	private RequestListener listener =new RequestListener(){

		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				Log.i("123", "微博信息："+response);
				weiboList = getWeiboList(response);
				adapter=new HomeAdapter(HomeActivity.this, weiboList);
				lv_home.setAdapter(adapter);
				layout_progress.setVisibility(View.INVISIBLE);//进度加载消失
				
				//ListView的项点击事件
//				lv_home.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
////						Object object = view.getTag();
//						Object obj_id=view.getTag(R.id.tag_first);
//						Object obj_user_id=view.getTag(R.id.tag_second);
//						if (obj_id!=null && obj_user_id!=null) {
//							String weiboId=obj_id.toString();
//							String userId=obj_user_id.toString();
//							Intent intent=new Intent(HomeActivity.this,ContentActivity.class);
//							Bundle bundle=new Bundle();
//							bundle.putString("weiboId", weiboId);
//							bundle.putString("userId", userId);
//							intent.putExtras(bundle);
//							startActivity(intent);
//						} else {
//							Log.i("123", "object is empty");
//						}
//					}
//				});
			}
			
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			
		}
		
	};
	
	private void initView(){
		refresh=(ImageView) findViewById(R.id.refresh);
		write=(ImageView) findViewById(R.id.write);
		refresh.setOnClickListener(this);
		write.setOnClickListener(this);
		tv_username=(TextView) findViewById(R.id.tv_username);
		tv_username.setText(UserSession.nowUser.getUser_name());
		lv_home=(ListView) findViewById(R.id.lv_home);
		layout_progress=(RelativeLayout) findViewById(R.id.layout_progress);
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				layout_progress.setVisibility(View.VISIBLE);//下来刷新时显示加载进度
			}
		};
		
		//下拉刷新
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
				//刷新微博
				if (weiboList!=null && !weiboList.isEmpty()) {
					weiboList.clear();
					handler.sendEmptyMessage(123);
					mStatusesAPI.friendsTimeline(0, 0, 20, 1, false, 0, false, listener);
				}
			}
		}, 0);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.refresh:
			//刷新微博，先把微博容器清空，然后重新装入20条微博数据
			if (weiboList!=null && !weiboList.isEmpty()) {
				weiboList.clear();
				layout_progress.setVisibility(View.VISIBLE);//点击刷新按钮时显示加载进度
				mStatusesAPI.friendsTimeline(0, 0, 20, 1, false, 0, false, listener);
			}
			
			break;
		case R.id.write:
			Intent intent=new Intent(HomeActivity.this,WriteWeiboActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	
	public List<WeiboInfo> getWeiboList(String response){
		try {
			JSONObject jsonObject=new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("statuses");
			int length = jsonArray.length();
			
			for (int i = 0; i < length; i++) {
				JSONObject jsonObject_wb = jsonArray.getJSONObject(i);
				if (jsonObject_wb!=null) {
					//创建一个对象，存储每条微博数据
					WeiboInfo weiboInfo=new WeiboInfo();
					//获得用户的数据
					JSONObject jsonObject_user = jsonObject_wb.getJSONObject("user");
					String id=jsonObject_wb.getString("id");
					String user_id = jsonObject_user.getString("id");
					String user_name = jsonObject_user.getString("screen_name");
					String user_head = jsonObject_user.getString("profile_image_url");
					String time = jsonObject_wb.getString("created_at");
					String text = jsonObject_wb.getString("text");
					String reposts_count = jsonObject_wb.getString("reposts_count");
					String comments_count = jsonObject_wb.getString("comments_count");
					String attitudes_count = jsonObject_wb.getString("attitudes_count");
					boolean haveImage=false;
					if (jsonObject_wb.has("thumbnail_pic")) {
						haveImage=true;
						String thumbnail_pic = jsonObject_wb.getString("thumbnail_pic");
						weiboInfo.setImage_url(thumbnail_pic);
					}
					Date startDate=new Date(time);
					Date nowDate = Calendar.getInstance().getTime();
					long time_cha=(nowDate.getTime()-startDate.getTime())/1000;
					if (time_cha/60>=1) {
						time=time_cha/60+"分钟前";
						if (time_cha/3600>=1) {
							time=time_cha/3600+"小时前";
						}
					} else {
						time=time_cha+"秒前";
					}
					if (weiboList==null) {
						weiboList=new ArrayList<WeiboInfo>();
					}
					weiboInfo.setId(id);
					weiboInfo.setUser_id(user_id);
					weiboInfo.setUser_name(user_name);
					weiboInfo.setUser_head(user_head);
					weiboInfo.setImage(haveImage);
					weiboInfo.setTime(time);
					weiboInfo.setText(text);
					weiboInfo.setForward(reposts_count);
					weiboInfo.setComments(comments_count);
					weiboInfo.setThumb_up(attitudes_count);
					
					weiboList.add(weiboInfo);
				}
			}
			return weiboList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
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
