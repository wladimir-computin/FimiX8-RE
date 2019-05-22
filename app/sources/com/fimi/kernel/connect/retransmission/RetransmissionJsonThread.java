package com.fimi.kernel.connect.retransmission;

import android.os.SystemClock;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.session.NoticeManager;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

public class RetransmissionJsonThread extends Thread {
    private boolean isLoop = true;
    public LinkedBlockingDeque<BaseCommand> mListReSend = new LinkedBlockingDeque();

    public void addToSendList(BaseCommand mBaseCommand) {
        this.mListReSend.add(mBaseCommand);
    }

    public BaseCommand removeFromListByCmdID(int cmdId, String camKey) {
        BaseCommand removeCmd = null;
        synchronized (this.mListReSend) {
            Iterator it = this.mListReSend.iterator();
            while (it.hasNext()) {
                BaseCommand msg = (BaseCommand) it.next();
                if ((msg.getMsgId() == cmdId && camKey == null) || (msg.getMsgId() == cmdId && camKey.equals(msg.getCamKey()))) {
                    removeCmd = msg;
                    break;
                }
            }
            if (removeCmd != null) {
                this.mListReSend.remove(removeCmd);
            }
        }
        return removeCmd;
    }

    public void run() {
        while (this.isLoop) {
            if (this.mListReSend.isEmpty()) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                BaseCommand removeData = null;
                Iterator it = this.mListReSend.iterator();
                while (it.hasNext()) {
                    BaseCommand bcd = (BaseCommand) it.next();
                    if (SystemClock.uptimeMillis() - bcd.getLastSendTime() >= ((long) bcd.getOutTime())) {
                        removeData = bcd;
                        break;
                    }
                }
                if (removeData != null) {
                    NoticeManager.getInstance().onSendTimeOut(removeData.getMsgGroudId(), removeData.getMsgId(), removeData);
                    this.mListReSend.remove(removeData);
                }
            }
        }
    }

    public void exit() {
        this.isLoop = false;
        interrupt();
    }
}
