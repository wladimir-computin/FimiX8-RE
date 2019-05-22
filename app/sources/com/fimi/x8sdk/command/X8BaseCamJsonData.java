package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.usb.LinkMsgType;
import com.fimi.kernel.dataparser.milink.LinkPayload;
import com.fimi.kernel.dataparser.usb.UsbLinkPacket;
import com.fimi.x8sdk.common.GlobalConfig;

public class X8BaseCamJsonData extends BaseCommand {
    public static final int GROUP_JSON = 238;
    byte[] payLoad;

    public void setPayLoad(byte[] payLoad) {
        this.payLoad = payLoad;
    }

    public void packCmd() {
        if (this.payLoad != null && GlobalConfig.getInstance().isAOAConnect()) {
            this.payLoad = addUSBHeader(this.payLoad);
        }
        setLinkMsgType(LinkMsgType.JsonData);
        setReSendNum(0);
        setMsgGroudId(GROUP_JSON);
        setCmdData(this.payLoad);
    }

    public byte[] addUSBHeader(byte[] cmdData) {
        UsbLinkPacket packet = new UsbLinkPacket(1);
        packet.getUsbPayLoad().putBytes(cmdData);
        return packet.packCmd();
    }

    public void unpack(LinkPayload payload) {
    }
}
