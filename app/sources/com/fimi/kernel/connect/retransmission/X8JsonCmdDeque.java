package com.fimi.kernel.connect.retransmission;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.usb.AOAConnect;
import java.util.concurrent.LinkedBlockingDeque;

public class X8JsonCmdDeque {
    private AOAConnect aoaConnect;
    private LinkedBlockingDeque<BaseCommand> cmdQueue = new LinkedBlockingDeque();
    private long lastSendTime;

    public X8JsonCmdDeque(AOAConnect aoaConnect) {
        this.aoaConnect = aoaConnect;
    }

    public void checkJsonCmdSendTime() {
        if (this.cmdQueue != null && !this.cmdQueue.isEmpty() && System.currentTimeMillis() - this.lastSendTime >= 200) {
            this.lastSendTime = System.currentTimeMillis();
            this.aoaConnect.putInQueue((BaseCommand) this.cmdQueue.poll());
        }
    }

    public void addJsonCmd(BaseCommand cmd) {
        if (cmd != null) {
            this.cmdQueue.add(cmd);
        }
    }
}
