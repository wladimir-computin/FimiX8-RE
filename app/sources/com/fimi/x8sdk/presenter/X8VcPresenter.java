package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.X8VisionCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.ivew.IVcAction;

public class X8VcPresenter extends BasePresenter implements IVcAction {
    public X8VcPresenter() {
        addNoticeListener();
    }

    public void setVcRectF(UiCallBackListener listener, int x, int y, int w, int h, int classfier) {
        sendCmd(new X8VisionCollection(this, listener).setVcRectF(x, y, w, h, classfier));
    }

    public void setVcFpvMode(UiCallBackListener listener, int mode) {
        sendCmd(new X8VisionCollection(this, listener).setVcFpvMode(mode));
    }

    public void setVcFpvLostSeq(UiCallBackListener listener, int seq) {
        sendCmd(new X8VisionCollection(this, listener).setVcFpvLostSeq(seq));
    }

    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
    }

    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
    }

    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        reponseCmd(false, groupId, msgId, null, bcd);
    }

    /* Access modifiers changed, original: protected */
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId == 15) {
            switch (msgId) {
                case 3:
                    onNormalResponse(isAck, packet, bcd);
                    return;
                default:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
            }
        }
    }
}
