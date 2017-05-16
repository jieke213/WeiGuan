package com.wjj.weiguan;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
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

public class ShareActivity extends Activity {
	Button btn_send_share;
	ImageView image_share;
	EditText et_share;
	Bitmap bitmap;
	TextView tv_share;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share);
		
		Oauth2AccessToken mAccessToken=new Oauth2AccessToken();
		mAccessToken.setToken(UserSession.nowUser.getToken());
		mAccessToken.setUid(UserSession.nowUser.getUser_id());
		final StatusesAPI mStatusesAPI=new StatusesAPI(ShareActivity.this, 
				Constants.APP_KEY, mAccessToken);
		
		btn_send_share=(Button) findViewById(R.id.btn_send_share);
		image_share=(ImageView) findViewById(R.id.image_share);
		et_share=(EditText) findViewById(R.id.et_share);
		tv_share=(TextView) findViewById(R.id.tvShare);
		
		String filePaht=Environment.getExternalStorageDirectory().getPath()+"/微关/crop_select.jpg";
		bitmap=getDiskBitmap(filePaht);
		image_share.setImageBitmap(bitmap);
		
		btn_send_share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String content=et_share.getText().toString();
				tv_share.setVisibility(View.VISIBLE);
				tv_share.setText("正在分享...");
				if (content.trim().equals("")) {
					content="分享图片";
					mStatusesAPI.upload(content, bitmap, String.valueOf(0), String.valueOf(0), listener);
				} else {
					mStatusesAPI.upload(content, bitmap, String.valueOf(0), String.valueOf(0), listener);
				}
				
			}
		});
	}
	
	private RequestListener listener=new RequestListener() {
    	@Override
		public void onComplete(String reponse) {
			Log.i("123", "reponse:"+reponse);
			bitmap.recycle();
			bitmap=null;
			toast("分享成功！");
			tv_share.setVisibility(View.INVISIBLE);
			ShareActivity.this.finish();
		}
		@Override
		public void onWeiboException(WeiboException e) {
			Log.i("123", "分享失败:"+e.getMessage());
			e.printStackTrace();
			bitmap.recycle();
			bitmap=null;
			toast("分享失败");
			tv_share.setVisibility(View.INVISIBLE);
			ShareActivity.this.finish();
		}
	};

    private Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {

		}
		return bitmap;
	}
    
    private void toast(String str){
    	Toast.makeText(ShareActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
