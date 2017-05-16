package com.wjj.weiguan;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.LiveAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.LoginActivity.UserSession;
import com.wjj.weiguan.adapter.ToolsAdapter;
import com.wjj.weiguan.pojo.ToolItem;
import com.wjj.weiguan.pojo.User;
import com.wjj.weiguan.util.AsyncImageLoader;
import com.wjj.weiguan.util.AsyncImageLoader.ImageCallback2;

public class MeActivity extends Activity {
	ImageView me_head;
	TextView me_user,me_description;
	TextView weibo_num,guanzhu_num,fensi_num;
	ListView lv_live;
	User user=null;
	LiveAPI mLiveAPI=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me);
		
		user=UserSession.nowUser;
		Oauth2AccessToken mAccessToken=new Oauth2AccessToken();
		mAccessToken.setToken(UserSession.nowUser.getToken());
		mAccessToken.setUid(UserSession.nowUser.getUser_id());
		mLiveAPI=new LiveAPI(MeActivity.this, Constants.APP_KEY, mAccessToken);
		
		initView();
		
		//设置用户信息
		AsyncImageLoader loader=new AsyncImageLoader();
		loader.loadDrawable(user.getUser_head(), new ImageCallback2() {
			
			@Override
			public void setImage(String url, Drawable drawable) {
				me_head.setImageDrawable(drawable);
			}
		});
		me_user.setText(user.getUser_name());
		me_description.setText(user.getDescription());
		weibo_num.setText(user.getStatuses_count());
		guanzhu_num.setText(user.getFriends_count());
		fensi_num.setText(user.getFollowers_count());
		
		//设置直播
		final List<ToolItem> list=new ArrayList<ToolItem>();
		list.add(new ToolItem(R.drawable.live, "直播 "));
		ToolsAdapter adapter=new ToolsAdapter(MeActivity.this, list);
		lv_live.setAdapter(adapter);
		lv_live.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				ToolItem item = list.get(position);
				switch (item.getReId()) {
				case R.drawable.live:
					mLiveAPI.create("我的直播", String.valueOf(100), String.valueOf(100), listener);
					break;

				default:
					break;
				}
			}
		});
	}
	
	private RequestListener listener=new RequestListener(){
		@Override
		public void onComplete(String reponse) {
			Log.i("123", "直播："+reponse);
			toast("直播："+reponse);
		}
		@Override
		public void onWeiboException(WeiboException e) {
			Log.i("123", "直播："+e.getMessage());
			e.printStackTrace();
			toast("该应用使用人数还较少，还没有权限开启直播哦~");
		}
		
	};

	private void initView() {
		me_head=(ImageView) findViewById(R.id.me_head);
		me_user=(TextView) findViewById(R.id.me_user);
		me_description=(TextView) findViewById(R.id.me_description);
		weibo_num=(TextView) findViewById(R.id.weibo_num);
		guanzhu_num=(TextView) findViewById(R.id.guanzhu_num);
		fensi_num=(TextView) findViewById(R.id.fensi_num);
		lv_live=(ListView) findViewById(R.id.lv_live);
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
	
	private void toast(String str){
		Toast.makeText(MeActivity.this, str, Toast.LENGTH_SHORT).show();
	}
}
