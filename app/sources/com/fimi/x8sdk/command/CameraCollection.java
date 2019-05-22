package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.X8BaseCmd.X8S_Module;

public class CameraCollection extends X8BaseCmd {
    public static final byte MSGID_CAMERA_INTEREST_METERING = (byte) 12;
    public static final byte MSGID_CAMERA_STATE = (byte) 21;
    public static final byte MSGID_CAMERA_TF_CAP = (byte) 8;
    public static final byte MSGID_CLOSE_CAMERA = (byte) 1;
    public static final byte MSGID_START_RECORD = (byte) 2;
    public static final byte MSGID_STOP_RECORD = (byte) 3;
    public static final byte MSGID_STOP_TAKE_PHOTO = (byte) 5;
    public static final byte MSGID_TAKE_PHOTO = (byte) 4;
    public static final byte MSG_CAMERA_PHOTO_MODE = (byte) 10;
    public static final byte MSG_CAMERA_VIDEO_MODE = (byte) 11;
    public static final byte MSG_GROUP_CAMERA = (byte) 2;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public CameraCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBackListener) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = uiCallBackListener;
    }

    private X8SendCmd getCameraBase(short seqIndex) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) 1);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId((byte) X8S_Module.MODULE_CAMERA.ordinal());
        packet4.getHeader4().setSeq(seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBack);
        return x8SendCmd;
    }

    public X8SendCmd closeCamera() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = new byte[4];
        payload[0] = (byte) 2;
        payload[1] = (byte) 1;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd startRecord() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = new byte[4];
        payload[0] = (byte) 2;
        payload[1] = (byte) 2;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd stopRecord() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = new byte[4];
        payload[0] = (byte) 2;
        payload[1] = (byte) 3;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd takePhoto() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = new byte[4];
        payload[0] = (byte) 2;
        payload[1] = (byte) 4;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd stopTakePhoto() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = new byte[4];
        payload[0] = (byte) 2;
        payload[1] = (byte) 5;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getTFCardCAP() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = new byte[4];
        payLoad[0] = (byte) 8;
        payLoad[1] = (byte) 5;
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd switchVideoMode() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = new byte[4];
        payLoad[0] = (byte) 2;
        payLoad[1] = (byte) 11;
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setInterestMetering(byte meteringIndex) {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = new byte[5];
        payLoad[0] = (byte) 2;
        payLoad[1] = (byte) 12;
        payLoad[2] = meteringIndex;
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd switchPhotoMode() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = new byte[4];
        payLoad[0] = (byte) 2;
        payLoad[1] = (byte) 10;
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }
}
