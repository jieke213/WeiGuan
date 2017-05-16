package com.sina.weibo.sdk;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;


public class RemindAPI extends AbsOpenAPI {
	public RemindAPI(Context context, String appKey,Oauth2AccessToken accessToken) {
		super(context, appKey, accessToken);
	}

	//获取某个用户各种未读消息数
	public void unread_count(long uid,RequestListener listener){
		WeiboParameters params = buildUnRead_Count(uid);
		requestAsync("https://rm.api.weibo.com/2/remind/unread_count.json", 
				params, HTTPMETHOD_GET, listener);
	}
	
	private WeiboParameters buildUnRead_Count(long uid){
		WeiboParameters params = new WeiboParameters(mAppKey);
		params.put("uid", uid);
		return params;
	}
}
