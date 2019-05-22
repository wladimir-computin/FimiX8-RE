package com.fimi.app.x8s.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.fimi.app.x8s.map.manager.gaode.GaodeMapLocationManager;

public class CustomLocationModeActivity extends Activity {
    private AMap aMap;
    private GaodeMapLocationManager mGaodeMapLocationManager;
    private MapView mapView;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.mapView = new MapView(this);
        this.mapView.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(this.mapView);
        TextView textView = new TextView(this);
        textView.setText("自定义效果\n 1、定位成功后， 小蓝点和和地图一起移动到定位点\n 2、手势操作地图后模式修改为 仅定位不移动到中心点");
        layout.addView(textView);
        setContentView(layout);
        init();
    }

    private void init() {
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
            this.mGaodeMapLocationManager = new GaodeMapLocationManager(this, this.aMap);
            this.mGaodeMapLocationManager.setUpMap();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
        this.mGaodeMapLocationManager.onResume();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
        this.mGaodeMapLocationManager.onPause();
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
        this.mGaodeMapLocationManager.onDestroy();
    }
}
