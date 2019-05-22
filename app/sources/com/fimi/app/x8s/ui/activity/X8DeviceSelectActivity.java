package com.fimi.app.x8s.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.map.MapType;

public class X8DeviceSelectActivity extends Activity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        setContentView(R.layout.x8s_host_new_main);
    }

    public void onDeviceClick(View view) {
        GlobalConfig.getInstance().setMapType(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_OPTION, false) ? MapType.AMap : MapType.GoogleMap);
        startActivity(new Intent(this, X8sMainActivity.class));
    }
}
