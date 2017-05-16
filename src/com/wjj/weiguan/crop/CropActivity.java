package com.wjj.weiguan.crop;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.wjj.weiguan.R;
import com.wjj.weiguan.util.Tools;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * 
 * @author zhy
 * 
 */
public class CropActivity extends Activity {
	private ClipImageLayout mClipImageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crop);

		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		
		Button btn_crop=(Button) findViewById(R.id.btn_crop);
		btn_crop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bitmap bitmap = mClipImageLayout.clip();
				try {
					Tools.saveFile(bitmap, "/crop_select.jpg");
					Toast.makeText(CropActivity.this, "裁剪成功！", Toast.LENGTH_SHORT).show();
					finish();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
