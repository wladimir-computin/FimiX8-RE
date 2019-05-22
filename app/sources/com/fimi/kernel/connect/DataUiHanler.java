package com.fimi.kernel.connect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.ILinkMessage;

public class DataUiHanler extends Handler {
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        IPersonalDataCallBack callBack = msg.obj;
        switch (msg.what) {
            case 0:
                callBack.onPersonalDataCallBack(msg.arg1, msg.arg2, (ILinkMessage) bundle.getSerializable("target"));
                return;
            case 1:
                callBack.onPersonalSendTimeOut(msg.arg1, msg.arg2, (BaseCommand) bundle.getSerializable("target"));
                return;
            default:
                return;
        }
    }
}
