package com.fimi.kernel.dataparser.milink;

import android.support.v4.view.ViewCompat;

public class Parser {
    static boolean msg_received;
    private int crcFrame;
    private int crcHeader;
    private int destId;
    private int encryptType;
    private int index;
    private int lenTypeCfg;
    private LinkPacket m;
    private int reserver;
    private int seq;
    private int srcId;
    State state = State.UNINIT;
    private int subDestId;
    private int subSrcId;
    private int version;

    enum State {
        UNINIT,
        IDLE,
        STX,
        VERSION,
        LENT_TYPE_CFG1,
        LENT_TYPE_CFG2,
        SRC_ID,
        SUB_SRC_ID,
        DEST_ID,
        SUB_DEST_ID,
        ENCRYPT_TYPE,
        RESERVE1,
        RESERVE2,
        RESERVE3,
        SEQ1,
        SEQ2,
        CRC_HEADER1,
        CRC_HEADER2,
        CRC_FRAME1,
        CRC_FRAME2,
        CRC_FRAME3,
        CRC_FRAME4,
        PAYLOAD
    }

    public LinkPacket unPacket(int c) {
        msg_received = false;
        switch (this.state) {
            case UNINIT:
            case IDLE:
                if (c == 254) {
                    this.state = State.STX;
                    this.m = new LinkPacket();
                    break;
                }
                break;
            case STX:
                if (!msg_received) {
                    clearData();
                    if (c == 3) {
                        this.version = c;
                        this.m.getHeader().setVersion((byte) this.version);
                        this.state = State.VERSION;
                        break;
                    }
                    this.state = State.IDLE;
                    break;
                }
                msg_received = false;
                this.state = State.IDLE;
                break;
            case VERSION:
                this.lenTypeCfg = c;
                this.state = State.LENT_TYPE_CFG1;
                break;
            case LENT_TYPE_CFG1:
                this.lenTypeCfg = (this.lenTypeCfg & 255) | ((c & 255) << 8);
                this.m.getHeader().setLenTypeCfg(this.lenTypeCfg);
                this.state = State.LENT_TYPE_CFG2;
                this.index = this.m.getHeader().getDataLen();
                break;
            case LENT_TYPE_CFG2:
                this.srcId = c;
                this.m.getHeader().setSrcId((byte) this.srcId);
                this.state = State.SRC_ID;
                break;
            case SRC_ID:
                this.subSrcId = c;
                this.m.getHeader().setSubSrcId((byte) this.subSrcId);
                this.state = State.SUB_SRC_ID;
                break;
            case SUB_SRC_ID:
                this.destId = c;
                this.m.getHeader().setDestId((byte) this.destId);
                this.state = State.DEST_ID;
                break;
            case DEST_ID:
                this.subDestId = c;
                this.m.getHeader().setSubDestId((byte) this.subDestId);
                this.state = State.SUB_DEST_ID;
                break;
            case SUB_DEST_ID:
                this.encryptType = c;
                this.m.getHeader().setEncryptType((byte) this.encryptType);
                this.state = State.ENCRYPT_TYPE;
                break;
            case ENCRYPT_TYPE:
                this.reserver = c;
                this.state = State.RESERVE1;
                break;
            case RESERVE1:
                this.reserver = (this.reserver & 255) | ((c & 255) << 8);
                this.state = State.RESERVE2;
                break;
            case RESERVE2:
                this.reserver = (this.reserver & 65535) | ((c & 255) << 16);
                this.m.getHeader().setReserver(this.reserver);
                this.state = State.RESERVE3;
                break;
            case RESERVE3:
                this.seq = c;
                this.state = State.SEQ1;
                break;
            case SEQ1:
                this.seq = (this.seq & 255) | ((c & 255) << 8);
                this.m.getHeader().setSeq(this.seq);
                this.state = State.SEQ2;
                break;
            case SEQ2:
                this.crcHeader = c;
                this.state = State.CRC_HEADER1;
                break;
            case CRC_HEADER1:
                this.crcHeader = (this.crcHeader & 255) | ((c & 255) << 8);
                this.m.getHeader().setCrcHeader(this.crcHeader);
                this.state = State.CRC_HEADER2;
                break;
            case CRC_HEADER2:
                this.crcFrame = c;
                this.state = State.CRC_FRAME1;
                break;
            case CRC_FRAME1:
                this.crcFrame = (this.crcFrame & 255) | ((c & 255) << 8);
                this.state = State.CRC_FRAME2;
                break;
            case CRC_FRAME2:
                this.crcFrame = (this.crcFrame & 65535) | ((c & 255) << 16);
                this.state = State.CRC_FRAME3;
                break;
            case CRC_FRAME3:
                this.crcFrame = (this.crcFrame & ViewCompat.MEASURED_SIZE_MASK) | ((c & 255) << 24);
                this.state = State.CRC_FRAME4;
                this.m.getHeader().setCrcFrame(this.crcFrame);
                this.m.getHeader().unPacket();
                this.state = State.CRC_FRAME4;
                break;
            case CRC_FRAME4:
                try {
                    if (this.index > 0) {
                        this.m.payload.add((byte) c);
                        this.index--;
                        if (this.index == 0) {
                            if (this.m.payload.size() == this.m.getHeader().getPayloadLen()) {
                                this.state = State.PAYLOAD;
                                if (this.m.getHeader().checkCRC(this.m.payload.getIntCrc())) {
                                    this.m.setMsgId(this.m.payload.getMsgId());
                                    this.m.setMsgGroupId(this.m.payload.getGroupId());
                                    msg_received = true;
                                }
                            }
                            this.state = State.IDLE;
                            break;
                        }
                    }
                    this.state = State.IDLE;
                    break;
                } catch (Exception e) {
                    String info = " h " + this.m.getHeader().getPayloadLen() + " p " + this.m.payload.size();
                    String hex = ByteHexHelper.bytesToHexString(this.m.encodePacket());
                    String x = null;
                    x.toString();
                    break;
                }
                break;
        }
        if (msg_received) {
            return this.m;
        }
        return null;
    }

    public void clearData() {
        this.version = 0;
        this.lenTypeCfg = 0;
        this.srcId = 0;
        this.subSrcId = 0;
        this.destId = 0;
        this.subDestId = 0;
        this.encryptType = 0;
        this.reserver = 0;
        this.seq = 0;
        this.crcHeader = 0;
        this.crcFrame = 0;
        this.index = 0;
    }
}
