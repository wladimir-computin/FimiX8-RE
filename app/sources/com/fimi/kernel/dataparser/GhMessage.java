package com.fimi.kernel.dataparser;

import com.fimi.kernel.dataparser.milink.LinkMessage;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.dataparser.milink.LinkPayload;

public class GhMessage extends LinkMessage {
    private byte cmdId;
    private byte commanType;
    private byte rpt;
    private byte version;

    public byte getRpt() {
        return this.rpt;
    }

    public void setRpt(byte rpt) {
        this.rpt = rpt;
    }

    public byte getVersion() {
        return this.version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getCmdId() {
        return this.cmdId;
    }

    public void setCmdId(byte cmdId) {
        this.cmdId = cmdId;
    }

    public byte getCommanType() {
        return this.commanType;
    }

    public void setCommanType(byte commanType) {
        this.commanType = commanType;
    }

    public void fillPayload(LinkPacket packet) {
    }

    public void unpack(LinkPayload payload) {
    }

    public void fillPayloadCommon(LinkPacket packet) {
        packet.payload.putByte((byte) getMsgGroudId());
        packet.payload.putByte((byte) getMsgId());
        packet.payload.putByte(getRpt());
        packet.payload.putByte(getVersion());
        packet.payload.putByte(getCmdId());
        packet.payload.putByte(getCommanType());
    }

    public void unpackCommon(LinkPayload payload) {
        payload.resetIndex();
        setMsgGroudId(payload.getByte() & 255);
        setMsgId(payload.getByte() & 255);
        setRpt(payload.getByte());
        setVersion(payload.getByte());
        setCmdId(payload.getByte());
        setCommanType(payload.getByte());
    }
}
