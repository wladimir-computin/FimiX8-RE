package com.fimi.x8sdk.common;

import android.content.Context;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IDataCallBack;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.connect.session.JsonListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.connect.session.UpdateDateListener;
import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.R;
import com.fimi.x8sdk.rtp.X8Rtp;

public abstract class BasePresenter implements IPersonalDataCallBack, IDataCallBack {
    public static final int ERR = -1;
    public static final int SUCCESS = 0;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void addNoticeListener() {
        NoticeManager.getInstance().add2NoticeList(this);
    }

    public void addNoticeListener(VideodDataListener listener) {
        NoticeManager.getInstance().add2NoticeList((IDataCallBack) this, listener);
    }

    public void addNoticeListener(UpdateDateListener listener) {
        NoticeManager.getInstance().add2NoticeList((IDataCallBack) this, listener);
    }

    public void addNoticeListener(JsonListener listener) {
        NoticeManager.getInstance().add2NoticeList((IDataCallBack) this, listener);
    }

    public void removeNoticeListener() {
        NoticeManager.getInstance().removeNoticeList(this);
    }

    public void removeFpvListener() {
        NoticeManager.getInstance().removeFpvListener();
    }

    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
    }

    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
    }

    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
    }

    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
    }

    public void sendCmd(BaseCommand cmd) {
        if (cmd != null) {
            SessionManager.getInstance().sendCmd(cmd);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean isNotNull(Object obj) {
        return obj != null;
    }

    /* Access modifiers changed, original: protected */
    public void reponseCmd(boolean isAck, int group, int msgId, ILinkMessage packet, BaseCommand bcd) {
    }

    public void onNormalResponse(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (isNotNull(packet.getUiCallBack())) {
            packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), null);
        }
    }

    public void onNormalResponseWithParam(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (!isNotNull(packet.getUiCallBack())) {
        } else {
            if (packet.getMsgRpt() == 0) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), packet);
                return;
            }
            if (!(this.context == null || packet.getMsgRpt() == 69)) {
                X8ToastUtil.showToast(this.context, this.context.getString(R.string.cmd_fail) + " Error Code=" + packet.getMsgRpt(), 0);
            }
            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail, packet.getMsgRpt()), packet);
        }
    }

    public void onErrorResponseWithParam(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (isNotNull(packet.getUiCallBack())) {
            packet.getUiCallBack().onComplete(null, packet);
        }
    }

    public void onNormalResponseWithParamForFCCTRL(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (!isNotNull(packet.getUiCallBack())) {
        } else {
            if (packet.getMsgRpt() == 0) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), packet);
                return;
            }
            if (this.context != null) {
                String s = X8Rtp.getRtpStringFcCtrl(this.context, packet.getMsgRpt());
                if (!s.equals("")) {
                    X8ToastUtil.showToast(this.context, s, 0);
                }
            }
            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail, packet.getMsgRpt()), packet);
        }
    }

    public void onNormalResponseWithParamForMaintenance(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (!isNotNull(packet.getUiCallBack())) {
        } else {
            if (packet.getMsgRpt() == 0) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), packet);
                return;
            }
            if (this.context != null) {
                X8ToastUtil.showToast(this.context, this.context.getString(R.string.cmd_fail) + " Error Code=" + packet.getMsgRpt(), 0);
            }
            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail, packet.getMsgRpt()), packet);
        }
    }

    public void onNormalResponseWithParamForTelemetry(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (!isNotNull(packet.getUiCallBack())) {
        } else {
            if (packet.getMsgRpt() == 0) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), packet);
                return;
            }
            if (this.context != null) {
                X8ToastUtil.showToast(this.context, this.context.getString(R.string.cmd_fail) + " Error Code=" + packet.getMsgRpt(), 0);
            }
            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail, packet.getMsgRpt()), packet);
        }
    }

    public void onNormalResponseWithParamForGimbal(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (!isNotNull(packet.getUiCallBack())) {
        } else {
            if (packet.getMsgRpt() == 0) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), packet);
                return;
            }
            if (this.context != null) {
                X8ToastUtil.showToast(this.context, this.context.getString(R.string.cmd_fail) + " Error Code=" + packet.getMsgRpt(), 0);
            }
            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail, packet.getMsgRpt()), packet);
        }
    }

    public void onNormalResponseWithParamWithRcCTRL(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (!isNotNull(packet.getUiCallBack())) {
        } else {
            if (packet.getMsgRpt() == 0) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), packet);
                return;
            }
            if (this.context != null) {
                X8ToastUtil.showToast(this.context, this.context.getString(R.string.cmd_fail) + " Error Code=" + packet.getMsgRpt(), 0);
            }
            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail, packet.getMsgRpt()), packet);
        }
    }

    public void onNormalResponseWithParamForNav(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (!isAck) {
            bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
        } else if (!isNotNull(packet.getUiCallBack())) {
        } else {
            if (packet.getMsgRpt() == 0) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), packet);
                return;
            }
            if (this.context != null) {
                String s = X8Rtp.getFcNavString(this.context, packet.getMsgRpt());
                if (!s.equals("")) {
                    X8ToastUtil.showToast(this.context, s, 0);
                }
            }
            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail, packet.getMsgRpt()), packet);
        }
    }
}
