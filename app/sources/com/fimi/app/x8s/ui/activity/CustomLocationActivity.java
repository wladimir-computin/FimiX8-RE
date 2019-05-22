package com.fimi.app.x8s.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.fimi.app.x8s.R;

public class CustomLocationActivity extends Activity {
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private AMap aMap;
    private RadioGroup mGPSModeGroup;
    private TextView mLocationErrText;
    private MapView mapView;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.location_mode_source_activity);
        this.mapView = (MapView) findViewById(R.id.map);
        this.mapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
            setUpMap();
            this.aMap.moveCamera(CameraUpdateFactory.zoomTo(15.5f));
        }
        this.mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
        this.mGPSModeGroup.setVisibility(8);
        this.mLocationErrText = (TextView) findViewById(R.id.location_errInfo_text);
        this.mLocationErrText.setVisibility(8);
    }

    private void setUpMap() {
        this.aMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.aMap.setMyLocationEnabled(true);
        setupLocationStyle();
    }

    private void setupLocationStyle() {
        this.aMap.setMyLocationStyle(new MyLocationStyle());
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
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
    }
}
