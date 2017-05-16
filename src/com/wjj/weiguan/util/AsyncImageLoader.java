package com.wjj.weiguan.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

public class AsyncImageLoader {
	private static HashMap<String, SoftReference<Drawable>> imageCache;
	MyThread myThread=null;
	
	public AsyncImageLoader() {
		if (imageCache==null) {
			imageCache=new HashMap<String, SoftReference<Drawable>>();
		}
	}
	
	public Drawable loadDrawable(final String url,final ImageCallback2 callback2){
		final Handler handler=new Handler(){
			public void handleMessage(Message msg) {
				Drawable drawable=(Drawable)msg.obj;
				if (drawable!=null) {
					callback2.setImage(url, (Drawable)msg.obj);
				}
			};
		};
		
		if (imageCache.containsKey(url)) {
			SoftReference<Drawable> softReference = imageCache.get(url);
			Drawable drawable = softReference.get();
			if (drawable!=null) {
				Message message = handler.obtainMessage();
				message.obj=drawable;
				handler.sendMessage(message);
				return drawable;
			}
		}
		new Thread(){
			public void run() {
				Drawable drawable=Tools.getDrawableFromUrl(url);
				imageCache.put(url, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage();
				message.obj=drawable;
				handler.sendMessage(message);
			};
		}.start();
		
		return null;
	}
	
	//第一种方式：异步加载图片
	public Drawable loadDrawable(final String url,final ImageView image,final ImageCallback callback){
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Drawable drawable=(Drawable)msg.obj;
				if (drawable!=null) {
					callback.setImage(image, drawable);
				}
			}
		};
		
		if (imageCache.containsKey(url)) {
			SoftReference<Drawable> softReference = imageCache.get(url);
			Drawable drawable = softReference.get();
			
			if (drawable!=null) {
				Message message = handler.obtainMessage();
				message.obj=drawable;
				handler.sendMessage(message);
				return drawable;
			}
		}
		
		new Thread(){
			public void run() {
				Drawable drawable=Tools.getDrawableFromUrl(url);
				imageCache.put(url, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage();
				message.obj=drawable;
				handler.sendMessage(message);
			};
		}.start();
		
//		if (myThread==null) {
//			myThread=new MyThread(url, handler);
//			myThread.start();
//			myThread=null;
//		}
		
		return null;
	}
	
	class MyThread extends Thread{
		String imageUrl;
		Handler handler;
		
		public MyThread(String imageUrl,Handler handler){
			this.imageUrl=imageUrl;
			this.handler=handler;
		}
		
		@Override
		public void run() {
			Drawable drawable=Tools.getDrawableFromUrl(imageUrl);
			imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
			Message message = handler.obtainMessage();
			message.obj=drawable;
			handler.sendMessage(message);
		}
	}
	
	public interface ImageCallback{
		public void setImage(ImageView image,Drawable drawable);
	}
	
	public interface ImageCallback2{
		public void setImage(String url,Drawable drawable);
	}
	
	
	//第二种方式：异步加载图片（有问题，带解决）
	public static void loadDrawable2(final String url,final ImageView image,final ImageCallback callback){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Drawable drawable=(Drawable) msg.obj;
				image.setVisibility(View.VISIBLE);
				callback.setImage(image, drawable);
//				image.setImageDrawable(drawable);
			}
		};
		
		new Thread(){
			public void run() {
				Drawable drawable = Tools.getDrawableFromUrl(url);
				Message msg=new Message();
				msg.obj=drawable;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	
}
