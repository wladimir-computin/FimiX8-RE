package com.fimi.x8sdk.connect.datatype;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IRetransmissionJsonHandle;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.dataparser.JsonMessage;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;

public class JsonDataChanel implements IDataChanel {
    public static String testString = "";
    IRetransmissionJsonHandle retransmissionHandle;

    public void forwardData(byte[] data) {
        try {
            String json = new String(data);
            JSONObject rtJson = JSON.parseObject(json);
            int msg_id = rtJson.getInteger("msg_id").intValue();
            String camKey = rtJson.getString("type");
            if (this.retransmissionHandle != null) {
                BaseCommand jsonCmdData = this.retransmissionHandle.removeFromListByCmdID(msg_id, camKey);
                if (jsonCmdData != null) {
                    notifyCamJsonData(new JsonMessage(msg_id, rtJson, jsonCmdData.getJsonUiCallBackListener()));
                }
            }
            if (msg_id == 7 && camKey != null && camKey.equals("temp")) {
                JSONObject rt = new JsonMessage(msg_id, rtJson, null).getJsonRt();
                if (rt != null) {
                    CameraParamsJson paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class);
                    if (paramsJson != null) {
                        String param = paramsJson.getParam();
                        if (!(param == null || param.equals(""))) {
                            testString = paramsJson.getParam();
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(json)) {
                notifyCamJsonData(msg_id, rtJson);
            }
        } catch (Exception e) {
        }
    }

    private void notifyCamJsonData(int msgId, JSONObject respJson) {
        NoticeManager.getInstance().onJsonDataCallBack(msgId, respJson);
    }

    private void notifyCamJsonData(JsonMessage message) {
        NoticeManager.getInstance().onJsonDataUICallBack(message);
    }

    public void setRetransmissionHandle(IRetransmissionJsonHandle retransmissionHandle) {
        this.retransmissionHandle = retransmissionHandle;
    }
}
