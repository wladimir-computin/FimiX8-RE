package com.fimi.app.x8s;

import com.amap.api.maps.MapView;
import com.fimi.kernel.base.BaseActivity;

public class AMapActivity extends BaseActivity {
    MapView mapView;

    public void initData() {
        this.mapView = (MapView) findViewById(R.id.map);
        this.mapView.onCreate(getSavedInstanceState());
    }

    public void doTrans() {
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_amap;
    }

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }
}
