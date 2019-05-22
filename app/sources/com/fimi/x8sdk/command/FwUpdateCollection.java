package com.fimi.x8sdk.command;

import android.support.graphics.drawable.PathInterpolatorCompat;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.connect.usb.LinkMsgType;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.X8BaseCmd.X8S_Module;

public class FwUpdateCollection extends X8BaseCmd {
    public static final byte MSG_GROUP_FW_UPDATE = (byte) 16;
    public static final byte MSG_ID_CHECK_CUR_STATUS = (byte) 5;
    public static final byte MSG_ID_GET_START_UPDATE = (byte) 2;
    public static final byte MSG_ID_GET_VERSION = (byte) -79;
    public static final byte MSG_ID_NOTIFY_UPGRADE = (byte) 4;
    public static final byte MSG_ID_PUT_FILE = (byte) 3;
    public static final byte MSG_ID_UPDATE_STATUS = (byte) 6;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public FwUpdateCollection(IPersonalDataCallBack callBack, UiCallBackListener listener) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = listener;
    }

    public X8SendCmd getVersion(byte moduleName, byte type) {
        X8SendCmd sendCmd = getUpdateBase(moduleName);
        sendCmd.setPayLoad(new byte[]{(byte) 16, MSG_ID_GET_VERSION, (byte) 0, (byte) 0, type, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) -1, (byte) 2, (byte) 0});
        sendCmd.packSendCmd();
        return sendCmd;
    }

    private X8SendCmd getUpdateBase(byte moduleName) {
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

    public X8SendCmd getCameraVer() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8S_Module.MODULE_CAMERA.ordinal());
        byte[] payload = new byte[4];
        payload[0] = (byte) 16;
        payload[1] = MSG_ID_GET_VERSION;
        payload[2] = (byte) 4;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd requestStartUpdate() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8S_Module.MODULE_CAMERA.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 16, (byte) 2, (byte) 0, (byte) 0});
        sendCmd.setReSendNum(0);
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        return sendCmd;
    }

    public X8SendCmd requestUploadFile(byte[] fileSize, byte[] crc) {
        X8SendCmd sendCmd = getUpdateBase((byte) X8S_Module.MODULE_CAMERA.ordinal());
        byte[] payload = new byte[12];
        payload[0] = (byte) 16;
        payload[1] = (byte) 3;
        payload[2] = (byte) 0;
        payload[3] = (byte) 0;
        System.arraycopy(fileSize, 0, payload, 4, fileSize.length);
        System.arraycopy(crc, 0, payload, 8, crc.length);
        sendCmd.setPayLoad(payload);
        sendCmd.setReSendNum(10);
        sendCmd.setOutTime(PathInterpolatorCompat.MAX_NUM_POINTS);
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        HostLogBack.getInstance().writeLog("Alanqiu  ==========" + sendCmd.toString());
        return sendCmd;
    }

    public X8SendCmd sendFwFileContent(int fileOffset, byte[] payLoad) {
        int msgLen = payLoad.length + 12;
        short payloadLen = (short) payLoad.length;
        byte[] content = new byte[msgLen];
        int checksum = 0;
        int i = 0 + 1;
        content[0] = (byte) (msgLen >> 0);
        int i2 = i + 1;
        content[i] = (byte) (msgLen >> 8);
        i = i2 + 1;
        content[i2] = (byte) (msgLen >> 16);
        i2 = i + 1;
        content[i] = (byte) (msgLen >> 24);
        i = i2 + 1;
        content[i2] = (byte) (fileOffset >> 0);
        i2 = i + 1;
        content[i] = (byte) (fileOffset >> 8);
        i = i2 + 1;
        content[i2] = (byte) (fileOffset >> 16);
        i2 = i + 1;
        content[i] = (byte) (fileOffset >> 24);
        i = i2 + 1;
        content[i2] = (byte) (payloadLen >> 0);
        i2 = i + 1;
        content[i] = (byte) (payloadLen >> 8);
        for (byte b : payLoad) {
            checksum += b & 255;
        }
        i = i2 + 1;
        content[i2] = (byte) (checksum >> 0);
        i2 = i + 1;
        content[i] = (byte) (checksum >> 8);
        System.arraycopy(payLoad, 0, content, i2, payloadLen);
        X8SendCmd x8SendCmd = new X8SendCmd();
        x8SendCmd.setFileOffset(fileOffset + payloadLen);
        x8SendCmd.setCmdData(x8SendCmd.addUSBHeader(content, 6));
        x8SendCmd.setLinkMsgType(LinkMsgType.FwUploadData);
        return x8SendCmd;
    }

    public X8SendCmd queryCurSystemStatus() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8S_Module.MODULE_CAMERA.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 16, (byte) 5, (byte) 0, (byte) 0});
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        return sendCmd;
    }

    public X8SendCmd queryCurUpdateStatus() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8S_Module.MODULE_CAMERA.ordinal());
        sendCmd.setPayLoad(new byte[]{(byte) 16, (byte) 6, (byte) 0, (byte) 0});
        sendCmd.setReSendNum(0);
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        return sendCmd;
    }
}
