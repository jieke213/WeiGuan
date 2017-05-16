package com.wjj.weiguan;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.wjj.weiguan.crop.CropActivity;
import com.wjj.weiguan.util.Tools;
import com.wjj.weiguan.view.BaseStripAdapter;
import com.wjj.weiguan.view.StripListView;

/**
 * 此demo用来展示如何结合定位SDK实现室内定位，并使用MyLocationOverlay绘制定位位置
 */
public class IndoorLocationActivity extends Activity implements OnClickListener{

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;
    View mainview;

    StripListView stripListView;
    BaseStripAdapter mFloorListAdapter;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    static Context mContext;
    
    // UI相关
    Button btn_normal,btn_satellite;
	Button btn_open_traffic,btn_close_traffic,btn_jietu,btn_share;
    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    
    byte[] datas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //千万要加上这句代码（否则出错）
        SDKInitializer.initialize(getApplicationContext());
        
        mContext = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout layout = new RelativeLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainview = inflater.inflate(R.layout.baidumap, null);
        layout.addView(mainview);

        requestLocButton = (Button) mainview.findViewById(R.id.button1);
        btn_normal=(Button) mainview.findViewById(R.id.btn_normal);
		btn_normal.setOnClickListener(this);
		btn_satellite=(Button) mainview.findViewById(R.id.btn_satellite);
		btn_satellite.setOnClickListener(this);
		btn_open_traffic=(Button) mainview.findViewById(R.id.btn_open_traffic);
		btn_open_traffic.setOnClickListener(this);
		btn_close_traffic=(Button) mainview.findViewById(R.id.btn_close_traffic);
		btn_close_traffic.setOnClickListener(this);
		btn_jietu=(Button) mainview.findViewById(R.id.btn_jietu);
		btn_jietu.setOnClickListener(this);
		btn_share=(Button) mainview.findViewById(R.id.btn_share);
		btn_share.setOnClickListener(this);
		
        mCurrentMode = LocationMode.NORMAL;
        requestLocButton.setText("普通");
        OnClickListener btnClickListener = new OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true,
                                mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true,
                                mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true,
                                mCurrentMarker));
                        break;
                    default:
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

        // 地图初始化
        mMapView = (MapView) mainview.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 开启室内图
        mBaiduMap.setIndoorEnable(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        stripListView = new StripListView(this);
        layout.addView(stripListView);
        setContentView(layout);
        mFloorListAdapter = new BaseStripAdapter(IndoorLocationActivity.this);

        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b == false || mapBaseIndoorMapInfo == null) {
                    stripListView.setVisibility(View.INVISIBLE);
                    return;
                }
                mFloorListAdapter.setmFloorList(mapBaseIndoorMapInfo.getFloors());
                stripListView.setVisibility(View.VISIBLE);
                stripListView.setStripAdapter(mFloorListAdapter);
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
        });

    }
    
    @Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_normal:
			// 普通地图
			btn_normal.setVisibility(View.INVISIBLE);
			btn_satellite.setVisibility(View.VISIBLE);
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.btn_satellite:
			// 卫星地图
			btn_satellite.setVisibility(View.INVISIBLE);
			btn_normal.setVisibility(View.VISIBLE);
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.btn_open_traffic:
			// 开启交通图
			btn_open_traffic.setVisibility(View.INVISIBLE);
			btn_close_traffic.setVisibility(View.VISIBLE);
			mBaiduMap.setTrafficEnabled(true);
			break;
		case R.id.btn_close_traffic:
			// 关闭交通图
			btn_close_traffic.setVisibility(View.INVISIBLE);
			btn_open_traffic.setVisibility(View.VISIBLE);
			mBaiduMap.setTrafficEnabled(false);
			break;
		case R.id.btn_jietu:
			// 截图
			Toast.makeText(IndoorLocationActivity.this, "正在截图...", Toast.LENGTH_SHORT).show();
			
			final Handler handler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					if (msg.what==1) {
						Toast.makeText(IndoorLocationActivity.this, "截图完成！", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(IndoorLocationActivity.this,CropActivity.class);
						startActivity(intent);
						btn_share.setVisibility(View.VISIBLE);
					}
				}
			};
			
			new Thread(){
				public void run() {
					mBaiduMap.snapshot(new SnapshotReadyCallback() {
						@Override
						public void onSnapshotReady(Bitmap bitmap) {
							try {
								Tools.saveFile(bitmap, "/crop.jpg");
								bitmap.recycle();
								bitmap=null;
							} catch (IOException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(1);
						}
					});
				};
			}.start();
			break;
		case R.id.btn_share:
			// 分享到微博
			Intent intent=new Intent(IndoorLocationActivity.this,ShareActivity.class);
			startActivity(intent);
			btn_share.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {
            // mapview 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            String bid = location.getBuildingID();
            if (bid != null && mMapBaseIndoorMapInfo != null) {
                Log.i("indoor", "bid = " + bid + " mid = " + mMapBaseIndoorMapInfo.getID());
                if (bid.equals(mMapBaseIndoorMapInfo.getID())) {// 校验是否满足室内定位模式开启条件
                    // Log.i("indoor","bid = mMapBaseIndoorMapInfo.getID()");
                    String floor = location.getFloor().toUpperCase();// 楼层
                    Log.i("indoor", "floor = " + floor + " position = " + mFloorListAdapter.getPosition(floor));
                    Log.i("indoor", "radius = " + location.getRadius() + " type = " + location.getNetworkLocationType());

                    boolean needUpdateFloor = true;
                    if (lastFloor == null) {
                        lastFloor = floor;
                    } else {
                        if (lastFloor.equals(floor)) {
                            needUpdateFloor = false;
                        } else {
                            lastFloor = floor;
                        }
                    }
                    if (needUpdateFloor) {// 切换楼层
                        mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                        mFloorListAdapter.setSelectedPostion(mFloorListAdapter.getPosition(floor));
                        mFloorListAdapter.notifyDataSetInvalidated();
                    }

                    if (!location.isIndoorLocMode()) {
                        mLocClient.startIndoorMode();// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
                        Log.i("indoor", "start indoormod");
                    }
                }
            }

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            
                Toast.makeText(IndoorLocationActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
