package com.fimi.x8sdk.command;

import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.x8sdk.command.X8BaseCmd.X8S_Module;

public class X8GimbalCollection extends X8BaseCmd {
    public static final byte MSG_GROUP_FC_GIMBAL = (byte) 9;
    public static final int MSG_ID_GET_GC_PARAM = 106;
    public static final byte MSG_ID_GET_GIMBAL_GAIN = (byte) 31;
    public static final int MSG_ID_GET_HORIZONTAL_ADJUST = 43;
    public static final int MSG_ID_GET_PITCH_SPEED = 41;
    public static final int MSG_ID_POSRATE = 6;
    public static final int MSG_ID_REST_GC_PARAM = 47;
    public static final int MSG_ID_SET_GC_PARAM = 105;
    public static final byte MSG_ID_SET_GIMBAL_GAIN = (byte) 30;
    public static final int MSG_ID_SET_HORIZONTAL_ADJUST = 42;
    public static final int MSG_ID_SET_PITCH_SPEED = 40;
    public static final int STATE = 1;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public X8GimbalCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBack) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = uiCallBack;
    }

    private X8SendCmd getBase(byte moduleName) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) 1);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId(moduleName);
        packet4.getHeader4().setSeq(this.seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBack);
        return x8SendCmd;
    }

    public X8SendCmd setAiAutoPhotoPitchAngle(int aiAutoPhotoPitchAngle) {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[17];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = (byte) 6;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 10;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        aiAutoPhotoPitchAngle *= 100;
        i2 = i + 1;
        payload[i] = (byte) (aiAutoPhotoPitchAngle >> 0);
        i = i2 + 1;
        payload[i2] = (byte) (aiAutoPhotoPitchAngle >> 8);
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setHorizontalAdjust(float angle) {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        payload = new byte[8];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_DISABLE_TRIPOD;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        byte[] temp = ByteUtil.float2byte(angle);
        i = i2 + 1;
        payload[i2] = temp[0];
        i2 = i + 1;
        payload[i] = temp[1];
        i = i2 + 1;
        payload[i2] = temp[2];
        i2 = i + 1;
        payload[i] = temp[3];
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        String sendString = ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd getHorizontalAdjust() {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_ENABLE_AERAILSHOT;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        String sendString = ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd setPitchSpeed(int speed) {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_GET_AUTO_HOME;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) speed;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getPitchSpeed() {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_ENABLE_TRIPOD;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        String sendString = ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd restGcParams() {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        payload = new byte[4];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_SET_ENABLE_FIXWING;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getGcParams() {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_AUTOSEND_PANORAMA_PHOTOGRAPH;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGcParams(int model, float param) {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = (byte) 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_PANORAMA_PHOTOGRAPH;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) model;
        byte[] temp = ByteUtil.float2byte(param);
        i2 = i + 1;
        payload[i] = temp[0];
        i = i2 + 1;
        payload[i2] = temp[1];
        i2 = i + 1;
        payload[i] = temp[2];
        i = i2 + 1;
        payload[i2] = temp[3];
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGcGain(int data) {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[5];
        payload[0] = (byte) 9;
        payload[1] = MSG_ID_SET_GIMBAL_GAIN;
        payload[4] = (byte) data;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd fetchGcGain() {
        X8SendCmd sendCmd = getBase((byte) X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 9;
        payload[1] = MSG_ID_GET_GIMBAL_GAIN;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        HostLogBack.getInstance().writeLog("获取===");
        return sendCmd;
    }
}
