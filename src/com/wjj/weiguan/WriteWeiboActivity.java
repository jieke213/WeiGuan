package com.wjj.weiguan;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.Constants;
import com.sina.weibo.sdk.StatusesAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wjj.weiguan.LoginActivity.UserSession;

public class WriteWeiboActivity extends Activity implements OnClickListener{
	Button btn_send,btn_cancel;
	Button btn_addImage;
	EditText et_weibo;
	ImageView images;
	TextView tv_limi;
	TextView tv_user;
	StatusesAPI mStatusesAPI=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.write);
		
		initView();
	}

	private void initView() {
		tv_user=(TextView) findViewById(R.id.tv_user);
		tv_user.setText(UserSession.nowUser.getUser_name());
		btn_addImage=(Button) findViewById(R.id.btn_addImage);
		btn_send=(Button) findViewById(R.id.send);
		btn_cancel=(Button) findViewById(R.id.cancel);
		et_weibo=(EditText) findViewById(R.id.et_weibo);
		images=(ImageView) findViewById(R.id.images);
		tv_limi=(TextView) findViewById(R.id.tv_limit);	
		
		btn_addImage.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
		et_weibo.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String content=et_weibo.getText().toString();
				boolean flag=false;
				int length = content.length();
				if (length<=140) {
					length=140-length;
					tv_limi.setTextColor(Color.GREEN);
				}else {
					length=length-140;
					tv_limi.setTextColor(Color.RED);
					flag=true;
				}
				tv_limi.setText(flag ? "-"+length:String.valueOf(length));
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.send:
			String content = et_weibo.getText().toString();
			if (!content.trim().equals("")) {
				sendWeibo(content);
				toast("发送微博成功！");
				finish();
			} else {
				toast("内容不能为空！");
			}
			break;
		case R.id.cancel:
			finish();
			break;
		case R.id.btn_addImage:
			
			break;

		default:
			break;
		}
	}

	private void sendWeibo(String content) {
		Oauth2AccessToken mAccessToken=new Oauth2AccessToken();
		mAccessToken.setToken(UserSession.nowUser.getToken());
		mAccessToken.setUid(UserSession.nowUser.getUser_id());
		mStatusesAPI=new StatusesAPI(WriteWeiboActivity.this,Constants.APP_KEY, mAccessToken);
		mStatusesAPI.update(content,String.valueOf(0.0), String.valueOf(0.0), new Listener());
	}
	
	class Listener implements RequestListener{

		@Override
		public void onComplete(String response) {
			Log.i("123", "response===="+response);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			
		}
	}
	
	private void toast(String str){
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
}
