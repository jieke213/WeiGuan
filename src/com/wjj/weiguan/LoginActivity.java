package com.wjj.weiguan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wjj.weiguan.dao.UserDao;
import com.wjj.weiguan.pojo.User;
import com.wjj.weiguan.util.AsyncImageLoader;
import com.wjj.weiguan.util.AsyncImageLoader.ImageCallback2;

public class LoginActivity extends Activity implements OnClickListener{
	Button btn_login,btn_cancel;
	ImageView image_head;
	Spinner sp_loginname;
	TextView tv_description;
	List<User> userList=null;
	ImageView image_add;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		initView();
		init();
		
	}
	
	private void init(){
		UserDao userDao=new UserDao(LoginActivity.this);
		userList = userDao.findAllUsers();
		if (/*userList.isEmpty() || */userList==null) {
			Intent intent=new Intent(this,OAuthActivity.class);
			startActivity(intent);
			finish();
		} else {
			List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
			for (User user:userList) {
				HashMap<String, Object> userMap=new HashMap<String,Object>();
				userMap.put("name", user.getUser_name());
				list.add(userMap);
			}
			SimpleAdapter adapter=new SimpleAdapter(
					LoginActivity.this, 
					list, 
					R.layout.spinneritem, 
					new String[]{"name"}, 
					new int[]{R.id.t1});
			sp_loginname.setAdapter(adapter);
			
			sp_loginname.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					User user = userList.get(arg2);
					tv_description.setText(user.getDescription());
					AsyncImageLoader loader=new AsyncImageLoader();
					loader.loadDrawable(user.getUser_head(), new ImageCallback2() {
						
						@Override
						public void setImage(String url, Drawable drawable) {
							image_head.setImageDrawable(drawable);
						}
					});
					
					//保存当前 登录的用户
					UserSession.nowUser=user;
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
			});
			
//			String [] datasourse={user.getUser_name()};
//			ArrayAdapter<String> adapter=new ArrayAdapter<String>(
//					LoginActivity.this,
//					R.layout.spinneritem,
//					R.id.t1,
//					datasourse);
//			sp_loginname.setAdapter(adapter);
		}
	}
	
	public static class UserSession{
		public static User nowUser;
	}

	private void initView() {
		btn_cancel=(Button) findViewById(R.id.btn_cancel);
		btn_login=(Button) findViewById(R.id.btn_login);
		btn_cancel.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		image_head=(ImageView) findViewById(R.id.image_head);
		tv_description=(TextView) findViewById(R.id.tv_description);
		sp_loginname=(Spinner) findViewById(R.id.sp_loginname);
		image_add=(ImageView) findViewById(R.id.image_add);
		image_add.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_login:
			Intent intent_home=new Intent(LoginActivity.this,WeiboMain.class);
			startActivity(intent_home);
			finish();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.image_add:
			Intent intent=new Intent(LoginActivity.this,OAuthActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
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
