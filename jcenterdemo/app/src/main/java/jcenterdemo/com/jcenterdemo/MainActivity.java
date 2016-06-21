package jcenterdemo.com.jcenterdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mIconMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //YQ 一定要先于main加载
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        mIconMaker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(30.0f);
        mBaiduMap.setMapStatus(msu);
        addInfosOverlay(Info.infos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        mIconMaker.recycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    /**
     * 初始化图层
     */
    public void addInfosOverlay(List<Info> infos) {
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (Info info : infos) {
            // 位置
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mIconMaker).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);
    }
}
