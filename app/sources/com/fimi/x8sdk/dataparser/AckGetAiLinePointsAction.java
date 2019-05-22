package com.fimi.x8sdk.dataparser;

import android.support.annotation.NonNull;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction.Cmd;

public class AckGetAiLinePointsAction extends X8BaseMessage implements Comparable<AckGetAiLinePointsAction> {
    public int cmd0;
    public int cmd1;
    public int count;
    public int para0;
    public int para1;
    public int pos;
    public int time;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.pos = packet.getPayLoad4().getByte();
        this.count = packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        this.cmd0 = packet.getPayLoad4().getByte();
        this.cmd1 = packet.getPayLoad4().getByte();
        for (int i = 0; i < 14; i++) {
            packet.getPayLoad4().getByte();
        }
        this.time = packet.getPayLoad4().getByte();
        this.para0 = packet.getPayLoad4().getByte();
        this.para1 = packet.getPayLoad4().getByte();
    }

    public int compareTo(@NonNull AckGetAiLinePointsAction o) {
        return this.pos - o.pos;
    }

    public int getAction() {
        if (this.cmd0 == 0) {
            return 0;
        }
        if (this.cmd0 == Cmd.HOVER.ordinal() && this.cmd1 == 0) {
            return 1;
        }
        if (this.cmd0 == Cmd.VIDEO.ordinal()) {
            return 2;
        }
        if (this.cmd0 == Cmd.PHOTO.ordinal() && this.para1 == 1) {
            return 4;
        }
        if (this.cmd0 == Cmd.HOVER.ordinal() && this.cmd1 == Cmd.HOVER.ordinal()) {
            return 5;
        }
        if (this.cmd0 == Cmd.PHOTO.ordinal() && this.para1 == 3) {
            return 6;
        }
        return 0;
    }
}
