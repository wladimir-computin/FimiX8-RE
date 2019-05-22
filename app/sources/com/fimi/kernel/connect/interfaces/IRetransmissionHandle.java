package com.fimi.kernel.connect.interfaces;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.LinkPacket;

public interface IRetransmissionHandle {
    boolean removeFromListByCmdID(int i, int i2, int i3, LinkPacket linkPacket);

    boolean removeFromListByCmdIDLinkPacket4(int i, int i2, int i3, LinkPacket4 linkPacket4);
}
