package com.fimi.kernel.dataparser;

import com.fimi.kernel.dataparser.milink.LinkMessage;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.dataparser.milink.LinkPayload;

public class X9Message extends LinkMessage {
    private byte[] content;
    private int seqIndex;

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getSeqIndex() {
        return this.seqIndex;
    }

    public void setSeqIndex(int seqIndex) {
        this.seqIndex = seqIndex;
    }

    public void fillPayload(LinkPacket packet) {
    }

    public void unpack(LinkPayload payload) {
    }

    public void decrypt() throws Exception {
        if (this.content != null) {
            setMsgGroudId(this.content[0]);
            setMsgId(this.content[1]);
            setErrorCode(this.content[2]);
        }
    }
}
