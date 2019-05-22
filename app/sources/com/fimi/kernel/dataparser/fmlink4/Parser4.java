package com.fimi.kernel.dataparser.fmlink4;

public class Parser4 {
    static boolean messageReceived;
    private int crcFrame;
    private int crcHeader;
    private byte destId;
    private byte encryptType;
    private int index;
    private int len;
    private LinkPacket4 m;
    private byte reserve1;
    private byte reserve2;
    private byte reserve3;
    private short seq;
    private byte srcId;
    State state = State.UnUnit;
    private byte type;
    byte typeAndRes1Encry;
    private byte ver = (byte) 4;
    short verAndLen;

    enum State {
        UnUnit,
        Idle,
        StartFlag,
        Ver,
        Len,
        TypeAndRes1Encry,
        SrcId,
        DestId,
        Reserve2,
        Reserve3,
        Seq1,
        Seq2,
        CrcHeader1,
        CrcHeader2,
        CrcFrame1,
        CrcFrame2,
        CrcFrame3,
        CrcFrame4,
        PlayLoad
    }

    public LinkPacket4 unPacket(int buf) {
        messageReceived = false;
        switch (this.state) {
            case UnUnit:
            case Idle:
                if ((Header4.startFlag & 255) == buf) {
                    this.state = State.Ver;
                    this.m = new LinkPacket4();
                    break;
                }
                break;
            case Ver:
                if (!messageReceived) {
                    clearData();
                    this.verAndLen = (short) (buf & 255);
                    this.ver = (byte) (this.verAndLen & 31);
                    if (this.ver == (byte) 4) {
                        this.m.getHeader4().setVer(this.ver);
                        this.state = State.Len;
                        break;
                    }
                    this.state = State.Idle;
                    break;
                }
                messageReceived = false;
                this.state = State.Idle;
                break;
            case Len:
                this.verAndLen = (short) (((buf & 255) << 8) | (this.verAndLen & 255));
                this.len = (this.verAndLen >> 6) & 511;
                this.m.getHeader4().setLen(this.len);
                this.state = State.TypeAndRes1Encry;
                this.index = this.m.getHeader4().getDataLen();
                break;
            case TypeAndRes1Encry:
                this.typeAndRes1Encry = (byte) (buf & 255);
                this.type = (byte) (this.typeAndRes1Encry & 3);
                this.reserve1 = (byte) ((this.typeAndRes1Encry >> 2) & 7);
                this.encryptType = (byte) ((this.typeAndRes1Encry >> 5) & 7);
                this.m.getHeader4().setType(this.type);
                this.m.getHeader4().setEncryptType(this.encryptType);
                this.m.getHeader4().setReserve1(this.reserve1);
                this.state = State.SrcId;
                break;
            case SrcId:
                this.srcId = (byte) (buf & 255);
                this.m.getHeader4().setSrcId(this.srcId);
                this.state = State.DestId;
                break;
            case DestId:
                this.destId = (byte) (buf & 255);
                this.m.getHeader4().setDestId(this.destId);
                this.state = State.Reserve2;
                break;
            case Reserve2:
                this.reserve2 = (byte) (buf & 255);
                this.m.getHeader4().setReserve2(this.reserve2);
                this.state = State.Reserve3;
                break;
            case Reserve3:
                this.reserve3 = (byte) (buf & 255);
                this.m.getHeader4().setReserve3(this.reserve3);
                this.state = State.Seq1;
                break;
            case Seq1:
                this.seq = (short) ((byte) buf);
                this.state = State.Seq2;
                break;
            case Seq2:
                this.seq = (short) ((this.seq & 255) | ((buf & 255) << 8));
                this.m.getHeader4().setSeq(this.seq);
                this.state = State.CrcHeader1;
                break;
            case CrcHeader1:
                this.crcHeader = buf & 255;
                this.state = State.CrcHeader2;
                break;
            case CrcHeader2:
                this.crcHeader |= (buf & 255) << 8;
                this.state = State.CrcFrame1;
                this.m.getHeader4().setCrcHeader(this.crcHeader);
                break;
            case CrcFrame1:
                this.crcFrame = buf & 255;
                this.state = State.CrcFrame2;
                break;
            case CrcFrame2:
                this.crcFrame |= (buf & 255) << 8;
                this.state = State.CrcFrame3;
                break;
            case CrcFrame3:
                this.crcFrame |= (buf & 255) << 16;
                this.state = State.CrcFrame4;
                break;
            case CrcFrame4:
                this.crcFrame |= (buf & 255) << 24;
                this.m.getHeader4().setCrcFrame(this.crcFrame);
                this.state = State.PlayLoad;
                break;
            case PlayLoad:
                try {
                    if (this.index > 0) {
                        this.m.getPayLoad4().add((byte) buf);
                        this.index--;
                        if (this.index == 0) {
                            if (this.m.getPayLoad4().size() == this.m.getHeader4().getDataLen()) {
                                this.state = State.PlayLoad;
                                if (this.m.getHeader4().checkFrameCRC(this.m.getPayLoad4().getIntCrc())) {
                                    messageReceived = true;
                                }
                            }
                            this.state = State.Idle;
                            break;
                        }
                    }
                    this.state = State.Idle;
                    break;
                } catch (Exception e) {
                    String x = null;
                    x.toString();
                    break;
                }
                break;
        }
        if (messageReceived) {
            return this.m;
        }
        return null;
    }

    private void clearData() {
        this.len = 0;
        this.type = (byte) 0;
        this.reserve1 = (byte) 0;
        this.encryptType = (byte) 0;
        this.srcId = (byte) 0;
        this.destId = (byte) 0;
        this.reserve2 = (byte) 0;
        this.reserve3 = (byte) 0;
        this.seq = (short) 0;
        this.crcHeader = 0;
        this.crcFrame = 0;
        this.verAndLen = (short) 0;
        this.typeAndRes1Encry = (byte) 0;
    }
}
