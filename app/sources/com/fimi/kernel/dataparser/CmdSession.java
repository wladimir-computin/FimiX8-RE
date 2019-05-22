package com.fimi.kernel.dataparser;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import java.util.concurrent.LinkedBlockingDeque;

public class CmdSession {
    private BaseCommand cmd;
    public LinkedBlockingDeque<Object> cmdQue = new LinkedBlockingDeque();
    private BaseConnect connect;
    public int sendCount;
    public int seq;
    private long startTime = System.currentTimeMillis();

    class CheckThread extends Thread {
        CheckThread() {
        }

        public void run() {
            while (CmdSession.this.cmd != null && System.currentTimeMillis() - CmdSession.this.startTime >= ((long) CmdSession.this.cmd.getOutTime()) && CmdSession.this.sendCount <= CmdSession.this.cmd.getReSendNum()) {
                CmdSession.this.reSend();
                CmdSession cmdSession = CmdSession.this;
                cmdSession.sendCount++;
            }
        }
    }

    public BaseCommand getCmd() {
        return this.cmd;
    }

    private void reSend() {
        if (this.cmd != null) {
            this.connect.sendCmd(this.cmd);
        }
    }

    public CmdSession(BaseCommand cmd, BaseConnect connect) {
        this.cmd = cmd;
        this.connect = connect;
        new CheckThread().start();
    }

    public void release() {
        this.cmd = null;
    }
}
