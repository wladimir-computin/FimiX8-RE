package com.fimi.kernel.connect;

import com.fimi.kernel.connect.interfaces.IRetransmissionHandle;
import com.fimi.kernel.connect.interfaces.IRetransmissionJsonHandle;
import com.fimi.kernel.connect.interfaces.IRetransmissionUsbHandle;
import com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle;

public interface ResultListener {
    boolean isAppRequestCmd(int i, int i2);

    void messageReceived(byte[] bArr);

    void setRetransmissionHandle(IRetransmissionHandle iRetransmissionHandle);

    void setRetransmissionJsonHandle(IRetransmissionJsonHandle iRetransmissionJsonHandle);

    void setRetransmissionUsbHandle(IRetransmissionUsbHandle iRetransmissionUsbHandle);

    void setTimerSendQueueHandle(ITimerSendQueueHandle iTimerSendQueueHandle);
}
