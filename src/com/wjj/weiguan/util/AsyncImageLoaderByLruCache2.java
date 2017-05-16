package com.wjj.weiguan.util;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

public class AsyncImageLoaderByLruCache2 {
	/**
	 * 使用新式的LurCache代替SoftReference
	 * 图片的url地址作为LurCahce的key
	 */
	private final int CACHE_SIZE;
	
	private LruCache<String, Drawable> lruCache;
	
	private MyThread myThread=null;
	
	public AsyncImageLoaderByLruCache2(){
		int maxMemmory=(int) Runtime.getRuntime().maxMemory();
		CACHE_SIZE=maxMemmory/8;//使缓存的大小为最大可用内存的1/8
		
		lruCache=new LruCache<String,Drawable>(CACHE_SIZE){
			@Override
			protected int sizeOf(String key, Drawable value) {
				return value.getIntrinsicWidth()*value.getIntrinsicHeight();
			}
		};
	}
	
	public Drawable loadDrawable(final ImageView image,final String imageUrl,final ImageLoadListener imageLoadListener){
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Drawable drawable=(Drawable)msg.obj;
				if (drawable!=null) {
					image.setVisibility(View.VISIBLE);
					imageLoadListener.imageLoad(drawable, image);
				}
			}
		};
		
		if (lruCache.get(imageUrl)!=null) {
			Drawable drawable = lruCache.get(imageUrl);
			if (drawable!=null) {
				Message msg = handler.obtainMessage();
				msg.obj=drawable;
				handler.sendMessage(msg);
				return drawable;
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
			Drawable drawable = Tools.getDrawableFromUrl(imageUrl);
			lruCache.put(imageUrl, drawable);
			Message msg = handler.obtainMessage();
			msg.obj=drawable;
			handler.sendMessage(msg);
		}
	}
	
	
	public interface ImageLoadListener{
		public void imageLoad(Drawable drawable,ImageView image);
	}
}
