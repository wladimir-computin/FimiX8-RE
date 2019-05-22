package com.fimi.kernel.connect.retransmission;

import android.os.SystemClock;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

public class TimerSendQueueThread extends Thread {
    private boolean isLoop = true;
    public LinkedBlockingDeque<BaseCommand> mListReSend = new LinkedBlockingDeque();
    private final int sleepTime = GLMapStaticValue.ANIMATION_FLUENT_TIME;

    public void addToSendList(BaseCommand mBaseCommand) {
        this.mListReSend.add(mBaseCommand);
    }

    public boolean removeFromListByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        BaseCommand remove = null;
        Iterator it = this.mListReSend.iterator();
        while (it.hasNext()) {
            BaseCommand msg = (BaseCommand) it.next();
            if (msg.getMsgGroudId() == groupId && msg.getMsgId() == cmdId && msg.getMsgSeq() == seq) {
                remove = msg;
                break;
            }
        }
        if (remove == null) {
            return false;
        }
        packet.setPersonalDataCallBack(remove.getPersonalDataCallBack());
        this.mListReSend.remove(remove);
        return true;
    }

    public void run() {
        while (this.isLoop) {
            if (this.mListReSend.isEmpty()) {
                try {
                    Thread.sleep(3000);
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
                    }
                }
                if (removeData != null) {
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
