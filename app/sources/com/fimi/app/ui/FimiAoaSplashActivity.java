package com.fimi.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.fimi.app.x8s.X8Application;
import com.fimi.x8sdk.controller.ConnectRcManager;

public class FimiAoaSplashActivity extends Activity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & 4194304) != 0) {
            if (getIntent().getAction().equals("android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
                X8Application.isAoaTopActivity = true;
                ConnectRcManager.getInstance().connectRC(this);
            }
            finish();
            return;
        }
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory("android.intent.category.LAUNCHER") && action != null && action.equals("android.intent.action.MAIN")) {
                finish();
                return;
            } else if (action.equals("android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
                X8Application.isAoaTopActivity = true;
                ConnectRcManager.getInstance().connectRC(this);
                finish();
                return;
            }
        }
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
