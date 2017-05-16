package com.wjj.weiguan.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wjj.weiguan.R;
import com.wjj.weiguan.pojo.WeiboInfo;
import com.wjj.weiguan.util.AsyncImageLoader;
import com.wjj.weiguan.util.AsyncImageLoader.ImageCallback;

public class HomeAdapter extends BaseAdapter {
	List<WeiboInfo> weiboList=null;
	Context context=null;
	private LinearLayout layout_forward;
	private LinearLayout layout_comments;
	private LinearLayout layout_thumb_up;
	
	public HomeAdapter(Context context,List<WeiboInfo> weiboList) {
		this.context=context;
		this.weiboList=weiboList;
	}

	@Override
	public int getCount() {
		return weiboList.size();
	} 

	@Override
	public Object getItem(int arg0) {
		return weiboList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
		
		ContentHolder holder=new ContentHolder();
		holder.content_head=(ImageView) convertView.findViewById(R.id.content_head);
		holder.content_image=(ImageView) convertView.findViewById(R.id.content_image);
		holder.content_user=(TextView) convertView.findViewById(R.id.content_user);
		holder.content_time=(TextView) convertView.findViewById(R.id.content_time);
		holder.content_text=(TextView) convertView.findViewById(R.id.content_text);
		holder.forward=(TextView) convertView.findViewById(R.id.tv_forward);
		holder.comments=(TextView) convertView.findViewById(R.id.tv_comments);
		holder.thumb_up=(TextView) convertView.findViewById(R.id.tv_thumb_up);
		
		WeiboInfo weiboInfo=null;
		if (!weiboList.isEmpty()) {
			weiboInfo = weiboList.get(position);
		}
		if (weiboInfo!=null) {
			convertView.setTag(weiboInfo.getId());
//			row.setTag(R.id.tag_first, weiboInfo.getId());
//			row.setTag(R.id.tag_second,weiboInfo.getUser_id());
			holder.content_user.setText(weiboInfo.getUser_name());
			holder.content_time.setText(weiboInfo.getTime());
			holder.content_text.setText(weiboInfo.getText());
			if (weiboInfo.getForward()!="0") {
				holder.forward.setText(weiboInfo.getForward());
			}
			if (weiboInfo.getComments()!="0") {
				holder.comments.setText(weiboInfo.getComments());
			}
			if (weiboInfo.getThumb_up()!="0") {
				holder.thumb_up.setText(weiboInfo.getThumb_up());
			}
			
			setWeiboDrawable(weiboInfo, holder);
		} else {
			Log.i("123", "weiboInfo为空");
		}
		
		layout_forward=(LinearLayout) convertView.findViewById(R.id.layout_forward);
		layout_forward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "转发", Toast.LENGTH_SHORT).show();
			}
		});
		layout_comments=(LinearLayout) convertView.findViewById(R.id.layout_comments);
		layout_comments.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "评论", Toast.LENGTH_SHORT).show();
			}
		});
		layout_thumb_up=(LinearLayout) convertView.findViewById(R.id.layout_thumb_up);
		layout_thumb_up.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "点赞", Toast.LENGTH_SHORT).show();
			}
		});
		
		return convertView;
	}

	class ContentHolder{
		public ImageView content_head;
		public ImageView content_image;
		public TextView content_user;
		public TextView content_time;
		public TextView content_text;
		public TextView forward;
		public TextView comments;
		public TextView thumb_up;
	}
	
	public void setWeiboDrawable(WeiboInfo weiboInfo,ContentHolder holder){
		AsyncImageLoader loader=new AsyncImageLoader();
		//微博中的图片
		if (weiboInfo.isImage()) {
			//如果微博项中有图片，先把图片控件可见
			holder.content_image.setVisibility(View.VISIBLE);
			Drawable drawable = loader.loadDrawable(
					weiboInfo.getImage_url(), 
					holder.content_image, 
					new ImageCallback() {
						@Override
						public void setImage(ImageView image, Drawable drawable) {
							image.setImageDrawable(drawable);
						}
					});
			if (drawable!=null) {
				holder.content_image.setImageDrawable(drawable);
			}
		}
		//微博中的头像
		Drawable drawable = loader.loadDrawable(
				weiboInfo.getUser_head(), 
				holder.content_head, 
				new ImageCallback() {
					@Override
					public void setImage(ImageView image, Drawable drawable) {
						image.setImageDrawable(drawable);
					}
				});
		if (drawable!=null) {
			holder.content_head.setImageDrawable(drawable);
		}
		
	}

//	public void setWeiboBitmap(WeiboInfo weiboInfo,ContentHolder holder){
//		AsyncImageLoaderByLruCache loaderByLruCache=new AsyncImageLoaderByLruCache();
//		//微博中的图片
//		if (weiboInfo.isImage()) {
//			Bitmap bitmap = loaderByLruCache.loadBitmap(holder.content_image,weiboInfo.getImage_url(), new ImageLoadListener() {
//				
//				@Override
//				public void imageLoad(Bitmap bitmap, ImageView image) {
//					image.setImageBitmap(bitmap);
//				}
//			});
//			if (bitmap!=null) {
//				holder.content_image.setImageBitmap(bitmap);
//			} else {
//				Log.i("123", "微博bitmap为空");
//			}
//			
//		}
//		//微博中的头像
//		Bitmap bitmap = loaderByLruCache.loadBitmap(holder.content_head,weiboInfo.getUser_head(), new ImageLoadListener() {
//			
//			@Override
//			public void imageLoad(Bitmap bitmap, ImageView image) {
//				image.setImageBitmap(bitmap);
//			}
//		});
//		if (bitmap!=null) {
//			holder.content_head.setImageBitmap(bitmap);
//		} else {
//			Log.i("123", "头像bitmap为空");
//		}
//	}
	
//	public void setWeiboDrawable2(WeiboInfo weiboInfo,ContentHolder holder){
//		AsyncImageLoaderByLruCache2 loaderByLruCache2=new AsyncImageLoaderByLruCache2();
//		//微博中的图片
//		if (weiboInfo.isImage()) {
//			Drawable drawable = loaderByLruCache2.loadDrawable(holder.content_image,weiboInfo.getImage_url(), new ImageLoadListener() {
//				
//				@Override
//				public void imageLoad(Drawable drawable, ImageView image) {
//					image.setImageDrawable(drawable);
//				}
//			});
//			if (drawable!=null) {
//				holder.content_image.setImageDrawable(drawable);
//			} else {
//				Log.i("123", "微博drawable为空");
//			}
//			
//		}
//		//微博中的头像
//		Drawable drawable = loaderByLruCache2.loadDrawable(holder.content_head,weiboInfo.getUser_head(), new ImageLoadListener() {
//			
//			@Override
//			public void imageLoad(Drawable drawable, ImageView image) {
//				image.setImageDrawable(drawable);
//			}
//		});
//		if (drawable!=null) {
//			holder.content_head.setImageDrawable(drawable);
//		} else {
//			Log.i("123", "头像drawable为空");
//		}
//	}
}
