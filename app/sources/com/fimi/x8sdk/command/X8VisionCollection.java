package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.X8BaseCmd.X8S_Module;

public class X8VisionCollection extends X8BaseCmd {
    public static final byte MSG_GROUP_VISION_0F = (byte) 15;
    public static final byte MSG_SET_FPV_LOST_SEQ = (byte) 17;
    public static final byte MSG_SET_FPV_MODE = (byte) 16;
    public static final byte MSG_SET_RECTF = (byte) 3;
    public static final byte MSG_TRACKING_RECTF = (byte) 4;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBackListener;

    public X8VisionCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBackListener) {
        this.personalDataCallBack = callBack;
        this.uiCallBackListener = uiCallBackListener;
    }

    private X8SendCmd getFCBase(byte moduleName) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) 1);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId(moduleName);
        packet4.getHeader4().setSeq(this.seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBackListener);
        return x8SendCmd;
    }

    public X8SendCmd setVcRectF(int x, int y, int w, int h, int classfier) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_CV.ordinal());
        payload = new byte[14];
        int i = 0 + 1;
        payload[0] = (byte) 15;
        int i2 = i + 1;
        payload[i] = (byte) 3;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) (x & 255);
        i2 = i + 1;
        payload[i] = (byte) ((x >> 8) & 255);
        i = i2 + 1;
        payload[i2] = (byte) (y & 255);
        i2 = i + 1;
        payload[i] = (byte) ((y >> 8) & 255);
        i = i2 + 1;
        payload[i2] = (byte) (w & 255);
        i2 = i + 1;
        payload[i] = (byte) ((w >> 8) & 255);
        i = i2 + 1;
        payload[i2] = (byte) (h & 255);
        i2 = i + 1;
        payload[i] = (byte) ((h >> 8) & 255);
        i = i2 + 1;
        payload[i2] = (byte) (classfier & 255);
        i2 = i + 1;
        payload[i] = (byte) ((classfier >> 8) & 255);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setVcFpvMode(int vcFpvMode) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_CV.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = (byte) 15;
        int i2 = i + 1;
        payload[i] = (byte) 16;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) vcFpvMode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setVcFpvLostSeq(int seq) {
        X8SendCmd sendCmd = getFCBase((byte) X8S_Module.MODULE_CV.ordinal());
        payload = new byte[8];
        int i = 0 + 1;
        payload[0] = (byte) 15;
        int i2 = i + 1;
        payload[i] = (byte) 17;
        i = i2 + 1;
        payload[i2] = (byte) 0;
        i2 = i + 1;
        payload[i] = (byte) 0;
        i = i2 + 1;
        payload[i2] = (byte) (seq & 255);
        i2 = i + 1;
        payload[i] = (byte) ((seq >> 8) & 255);
        i = i2 + 1;
        payload[i2] = (byte) ((seq >> 16) & 255);
        i2 = i + 1;
        payload[i] = (byte) ((seq >> 24) & 255);
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }
}
