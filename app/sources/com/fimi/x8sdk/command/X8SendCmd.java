package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.usb.LinkMsgType;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.LinkPayload;
import com.fimi.kernel.dataparser.usb.UsbLinkPacket;
import com.fimi.x8sdk.common.GlobalConfig;

public class X8SendCmd extends BaseCommand {
    LinkPacket4 linkPacket4;

    public X8SendCmd(LinkPacket4 packet4) {
        this.linkPacket4 = packet4;
    }

    public void setPayLoad(byte[] bytes) {
        setMsgGroudId(bytes[0] & 255);
        setMsgId(bytes[1] & 255);
        setMsgSeq(this.linkPacket4.getHeader4().getSeq());
        this.linkPacket4.getPayLoad4().putBytes(bytes);
    }

    public void packSendCmd() {
        byte[] cmdData = this.linkPacket4.packCmd();
        setLinkMsgType(LinkMsgType.FmLink4);
        if (GlobalConfig.getInstance().isAOAConnect()) {
            cmdData = addUSBHeader(cmdData);
        }
        setCmdData(cmdData);
    }

    private byte[] addUSBHeader(byte[] cmdData) {
        UsbLinkPacket packet = new UsbLinkPacket(0);
        packet.getUsbPayLoad().putBytes(cmdData);
        return packet.packCmd();
    }

    public void packSendCmd(int type, LinkMsgType linkMsgType) {
        byte[] cmdData = this.linkPacket4.packCmd();
        setLinkMsgType(linkMsgType);
        if (GlobalConfig.getInstance().isAOAConnect()) {
            cmdData = addUSBHeader(cmdData, type);
        }
        setCmdData(cmdData);
    }

    public byte[] addUSBHeader(byte[] cmdData, int type) {
        UsbLinkPacket packet = new UsbLinkPacket(type);
        packet.getUsbPayLoad().putBytes(cmdData);
        return packet.packCmd();
    }

    public void unpack(LinkPayload payload) {
    }
}
