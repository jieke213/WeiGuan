package com.wjj.weiguan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.RemindAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.LoginActivity.UserSession;
import com.wjj.weiguan.adapter.RemindAdapter;
import com.wjj.weiguan.pojo.RemindItem;

public class MsgActivity extends Activity {
	RemindAPI mRemindAPI;
	ListView lv_msg;
	List<RemindItem> list=null;
	RemindAdapter adapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msg);
		
		lv_msg=(ListView) findViewById(R.id.lv_msg);
		list=new ArrayList<RemindItem>();
		
		
		final Oauth2AccessToken mAccessToken=new Oauth2AccessToken();
		mAccessToken.setToken(UserSession.nowUser.getToken());
		mAccessToken.setUid(UserSession.nowUser.getUser_id());
		mRemindAPI=new RemindAPI(MsgActivity.this, Constants.APP_KEY, mAccessToken);
		String uidStr=mAccessToken.getUid();
		long uid = Long.parseLong(uidStr);
		mRemindAPI.unread_count(uid, listener);

		
		
		
		lv_msg.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//				switch (item.getReId()) {
//				case R.drawable.at_me:
//					
//					break;
//				case R.drawable.comments_me:
//					
//					break;
//				case R.drawable.thumb_up_me:
//					
//					break;
//				default:
//					break;
//				}
			}
		});
		
	}
	
	private RequestListener listener=new RequestListener() {
		@Override
		public void onComplete(String reponse) {
			Log.i("123", reponse);
			try {
				JSONObject jsonObject=new JSONObject(reponse);
				String at = jsonObject.getString("mention_status");
				String cmt = jsonObject.getString("cmt");
				String thumb_up = jsonObject.getString("attention_mention_status");
//				String thumb_up = jsonObject.getString("attention_cmt");
				
				Log.i("123", at+","+cmt+","+thumb_up);
				list.add(new RemindItem(R.drawable.at_me, "@我的", at,R.drawable.remind_arrow));
				list.add(new RemindItem(R.drawable.comments_me, "评论", cmt,R.drawable.remind_arrow));
				list.add(new RemindItem(R.drawable.thumb_up_me, "赞", "0",R.drawable.remind_arrow));
				adapter=new RemindAdapter(MsgActivity.this, list);
				lv_msg.setAdapter(adapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onWeiboException(WeiboException e) {
			Log.i("123", e.getMessage());
			e.printStackTrace();
		}
	};
	
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
