package com.wjj.weiguan.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.TextView;

import com.wjj.weiguan.LoadActivity;
import com.wjj.weiguan.R;

public class Tools {

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			@SuppressWarnings("deprecation")
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void checkNetwork(final LoadActivity context) {
		if (!isNetworkAvailable(context)) {
			TextView msg = new TextView(context);
			msg.setGravity(Gravity.CENTER);
			msg.setText("当前没有网络，请设置网络！");
			new AlertDialog.Builder(context).setIcon(R.drawable.not)
					.setTitle("网络状态提示").setView(msg).setCancelable(false)
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(Settings.ACTION_SETTINGS);
							context.startActivity(intent);
							context.finish();
						}
					}).show();
		}

	}

	// 通过url地址解析出Drawable
	public static Drawable getDrawableFromUrl(final String url) {
		try {
			URLConnection connection = new URL(url).openConnection();
			InputStream is = connection.getInputStream();
			Drawable drawable = Drawable.createFromStream(is, "image");
			return drawable;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Bitmap getDiskBitmap(String pathString){  
		Bitmap bitmap = null;  
		try  {   
			File file = new File(pathString);    
		if(file.exists()){      
			bitmap = BitmapFactory.decodeFile(pathString);    
		}  
		} catch (Exception e){		
			
		}  
		return bitmap;
	} 

	// 把Bitmap保存到本地文件中
	public static void saveFile(Bitmap bm, String fileName) throws IOException {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/微关";
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.flush();
		bos.close();
	}

}
