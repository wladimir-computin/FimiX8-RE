package com.fimi.kernel.connect.retransmission;

import android.os.SystemClock;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IDataTransfer;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

public class RetransmissionThread extends Thread {
    private boolean isLoop = true;
    private IDataTransfer mDataTransfer;
    public LinkedBlockingDeque<BaseCommand> mListReSend = new LinkedBlockingDeque();
    private final int sleepTime = GLMapStaticValue.ANIMATION_FLUENT_TIME;

    public void addToSendList(BaseCommand mBaseCommand) {
        this.mListReSend.add(mBaseCommand);
    }

    public boolean removeFromListByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        boolean ret = false;
        synchronized (this.mListReSend) {
            BaseCommand remove = null;
            Iterator it = this.mListReSend.iterator();
            while (it.hasNext()) {
                BaseCommand msg = (BaseCommand) it.next();
                if (msg.getMsgGroudId() == groupId && msg.getMsgId() == cmdId && msg.getMsgSeq() == seq) {
                    remove = msg;
                    break;
                }
            }
            if (remove != null) {
                ret = true;
                this.mListReSend.remove(remove);
            }
        }
        return ret;
    }

    public boolean removeFromListByCmdIDLinkPacket4(int groupId, int cmdId, int seq, LinkPacket4 packet) {
        boolean ret = false;
        synchronized (this.mListReSend) {
            BaseCommand remove = null;
            Iterator it = this.mListReSend.iterator();
            while (it.hasNext()) {
                BaseCommand msg = (BaseCommand) it.next();
                if (msg.getMsgGroudId() == groupId && msg.getMsgId() == cmdId && msg.getMsgSeq() == seq) {
                    remove = msg;
                    break;
                }
            }
            if (remove != null) {
                ret = true;
                packet.setPersonalDataCallBack(remove.getPersonalDataCallBack());
                packet.setUiCallBack(remove.getUiCallBack());
                this.mListReSend.remove(remove);
            }
        }
        return ret;
    }

    public RetransmissionThread(IDataTransfer mDataTransfer) {
        this.mDataTransfer = mDataTransfer;
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
                        int seq = bcd.getCurrentSendNum();
                        if (seq >= bcd.getReSendNum()) {
                            removeData = bcd;
                            break;
                        }
                        bcd.setCurrentSendNum(seq + 1);
                        this.mDataTransfer.sendRestransmissionData(bcd);
                        bcd.setLastSendTime(SystemClock.uptimeMillis());
                    }
                }
                if (removeData != null) {
                    this.mDataTransfer.onSendTimeOut(removeData.getMsgGroudId(), removeData.getMsgId(), removeData);
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
