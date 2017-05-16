package com.sina.weibo.sdk;

import android.content.Context;
import android.util.SparseArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

public class LiveAPI extends AbsOpenAPI{
	private static final int READ_API_CREATE = 0;
	private static final String API_BASE_URL = API_SERVER + "/proxy/live";
	
	private static final SparseArray<String> sAPIList = new SparseArray<String>();
	static{
		sAPIList.put(READ_API_CREATE, API_BASE_URL + "/create");
	}

	public LiveAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
		super(context, appKey, accessToken);
	}

	public void create(String title,String width,String height,RequestListener listener){
		WeiboParameters params = buildCreateParameters(title, width, height);
		requestAsync(sAPIList.get(READ_API_CREATE), params, HTTPMETHOD_POST, listener);
	}
	
	private WeiboParameters buildCreateParameters(String title,String width,String height){
		WeiboParameters params = new WeiboParameters(mAppKey);
		params.put("title", title);
		params.put("width", width);
		params.put("height", height);
		return params;
	}
}
