package com.wjj.weiguan.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

public class AsyncImageLoaderByLruCache {
	/**
	 * 使用新式的LurCache代替SoftReference
	 * 图片的url地址作为LurCahce的key
	 */
	private final int CACHE_SIZE;
	
	private LruCache<String, Bitmap> lruCache;
	
	private MyThread myThread=null;
	
	public AsyncImageLoaderByLruCache(){
		int maxMemmory=(int) Runtime.getRuntime().maxMemory();
		CACHE_SIZE=maxMemmory/8;//使缓存的大小为最大可用内存的1/8
		
		lruCache=new LruCache<String,Bitmap>(CACHE_SIZE){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	
	public Bitmap loadBitmap(final ImageView image,final String imageUrl,final ImageLoadListener imageLoadListener){
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Bitmap bitmap=(Bitmap)msg.obj;
				if (bitmap!=null) {
					image.setVisibility(View.VISIBLE);
					imageLoadListener.imageLoad(bitmap, image);
				}
			}
		};
		
		if (lruCache.get(imageUrl)!=null) {
			Bitmap bitmap = lruCache.get(imageUrl);
			if (bitmap!=null) {
				Message msg = handler.obtainMessage();
				msg.obj=bitmap;
				handler.sendMessage(msg);
				return bitmap;
			}
		}
		
//		new Thread(){
//			public void run() {
//				Bitmap bitmap = loadBitmapFromUrl(imageUrl);
//				lruCache.put(imageUrl, bitmap);
//				Message msg = handler.obtainMessage();
//				msg.obj=bitmap;
//				handler.sendMessage(msg);
//			};
//		}.start();
		
		if (myThread==null) {
			myThread=new MyThread(imageUrl, handler);
			myThread.start();
			myThread=null;
		}
		
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
			Bitmap bitmap = loadBitmapFromUrl(imageUrl);
			lruCache.put(imageUrl, bitmap);
			Message msg = handler.obtainMessage();
			msg.obj=bitmap;
			handler.sendMessage(msg);
		}
	}
	
	public Bitmap loadBitmapFromUrl(String imageUrl){
		try {
			URL url = new URL(imageUrl);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public interface ImageLoadListener{
		public void imageLoad(Bitmap bitmap,ImageView image);
	}
}
