package com.wjj.weiguan;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhotoActivity extends Activity {
	private Button btn_query;
	private EditText et_phone;
	private TextView tv_resul;
	
	// 命名空间  
    private static final String nameSpace = "http://WebXml.com.cn/";  
    // 调用的方法名称  
    private static final String methodName = "getMobileCodeInfo";  
    // EndPoint  
    private static final String endPoint = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx";
    // SOAP Action  
    private static final String soapAction = "http://WebXml.com.cn/getMobileCodeInfo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo);
		
		btn_query=(Button) findViewById(R.id.btn_query);
		et_phone=(EditText) findViewById(R.id.et_phone);
		tv_resul=(TextView) findViewById(R.id.tv_result);
		
		
		btn_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new Thread(){
					public void run() {
		                getRemoteInfo();  
					};
				}.start();
				
			}
		});

		
	}

	private void getRemoteInfo()  {
        //指定WebService的命名空间和调用的方法名  
        SoapObject soapObject = new SoapObject(nameSpace, methodName);  
  
        //设置需调用WebService接口需要传入的两个参数mobileCode、userId  
        String phone=et_phone.getText().toString().trim();
        soapObject.addProperty("mobileCode", phone);  
        soapObject.addProperty("userId", "");  
  
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本  
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
  
        envelope.bodyOut = soapObject;  
        // 设置是否调用的是dotNet开发的WebService  
        envelope.dotNet = true;  
        // 等价于envelope.bodyOut = rpc;  
        envelope.setOutputSoapObject(soapObject);  
  
        HttpTransportSE transport = new HttpTransportSE(endPoint);  
        try {  
            // 调用WebService  
            transport.call(soapAction, envelope);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        // 获取返回的数据  
        SoapObject object = (SoapObject) envelope.bodyIn;  
        // 获取返回的结果  
        String result;
        if (object!=null) {
        	result = object.getProperty(0).toString();  
		} else {
			result="归属地信息获取失败！";
		}
        // 将WebService返回的结果显示在TextView中  
        Message message=new Message();
        message.obj=result;
        message.what=1;
        handler.sendMessage(message);
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==1) {
				tv_resul.setText(msg.obj.toString());
			}
			
		};
	};

}
