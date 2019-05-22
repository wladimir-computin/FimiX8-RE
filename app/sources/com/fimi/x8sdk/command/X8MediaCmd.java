package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.usb.LinkMsgType;
import com.fimi.kernel.dataparser.milink.LinkPayload;
import com.fimi.kernel.dataparser.usb.UsbLinkPacket;

public class X8MediaCmd extends BaseCommand {
    public void packCmd(byte[] cmdData) {
        setCmdData(addUSBHeader(cmdData));
    }

    public byte[] addUSBHeader(byte[] cmdData) {
        setLinkMsgType(LinkMsgType.MediaDownData);
        UsbLinkPacket packet = new UsbLinkPacket(4);
        packet.getUsbPayLoad().putBytes(cmdData);
        return packet.packCmd();
    }

    public void unpack(LinkPayload payload) {
    }
}
