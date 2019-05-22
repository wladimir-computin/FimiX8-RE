package com.fimi.app.x8s.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.alibaba.fastjson.JSON;
import com.fimi.TcpClient;
import com.fimi.app.x8s.X8Application;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.network.FwManager;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.x8sdk.controller.ConnectRcManager;
import java.util.ArrayList;

public class X8SplashActivity extends Activity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThreadUtils.execute(new Runnable() {
            public void run() {
                X8SplashActivity.this.syncServerFwInfo();
            }
        });
        if ((getIntent().getFlags() & 4194304) != 0) {
            if (getIntent().getAction().equals("android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
                TcpClient.getIntance().sendLog(" connect --》home--》usb out2in--->");
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
                TcpClient.getIntance().sendLog("main runing ---> usb is in--->");
                X8Application.isAoaTopActivity = true;
                ConnectRcManager.getInstance().connectRC(this);
                finish();
                return;
            }
        }
        startActivity(new Intent(this, X8DeviceSelectActivity.class));
        finish();
    }

    private void syncServerFwInfo() {
        new FwManager().getX9FwNetDetail(new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                try {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    LogUtil.d("moweiru", "responseObj:" + responseObj);
                    if (netModel.isSuccess() && netModel.getData() != null) {
                        HostConstants.saveFirmwareDetail(JSON.parseArray(netModel.getData().toString(), UpfirewareDto.class));
                    }
                } catch (Exception e) {
                    HostConstants.saveFirmwareDetail(new ArrayList());
                }
            }

            public void onFailure(Object reasonObj) {
            }
        }));
    }
}
