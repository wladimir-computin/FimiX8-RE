package com.fimi.network;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class DownNoticeMananger {
    private static DownNoticeMananger noticeMananger = new DownNoticeMananger();
    CopyOnWriteArrayList<IDownProgress> noticeList = new CopyOnWriteArrayList();

    public void addDownNoticeList(IDownProgress iDownProgress) {
        this.noticeList.add(iDownProgress);
    }

    public void remioveDownNoticeList(IDownProgress iDownProgress) {
        Iterator it = this.noticeList.iterator();
        while (it.hasNext()) {
            IDownProgress iProgress = (IDownProgress) it.next();
            if (iProgress != null && iProgress == iDownProgress) {
                IDownProgress rm = iProgress;
                return;
            } else if (null != null) {
                this.noticeList.remove(null);
            }
        }
    }

    public static DownNoticeMananger getDownNoticManger() {
        return noticeMananger;
    }

    public CopyOnWriteArrayList<IDownProgress> getNoticeList() {
        return this.noticeList;
    }
}
