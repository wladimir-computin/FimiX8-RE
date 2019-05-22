package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.R;
import com.fimi.x8sdk.command.X8GimbalCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckGetHorizontalAdjust;
import com.fimi.x8sdk.dataparser.AckGetPitchSpeed;
import com.fimi.x8sdk.ivew.IGbAciton;

public class X8GimbalPresenter extends BasePresenter implements IGbAciton {
    public X8GimbalPresenter() {
        addNoticeListener();
    }

    public void setAiAutoPhotoPitchAngle(int angle, UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).setAiAutoPhotoPitchAngle(angle));
    }

    public void setHorizontalAdjust(float angle, UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).setHorizontalAdjust(angle));
    }

    public void getHorizontalAdjust(UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).getHorizontalAdjust());
    }

    public void setPitchSpeed(int speed, UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).setPitchSpeed(speed));
    }

    public void getPitchSpeed(UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).getPitchSpeed());
    }

    public void restGcSystemParams(UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).restGcParams());
    }

    public void getGcParams(UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).getGcParams());
    }

    public void setGcParams(int value, float params, UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).setGcParams(value, params));
    }

    public void setGcGain(int data0, UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).setGcGain(data0));
    }

    public void getGcGain(UiCallBackListener listener) {
        sendCmd(new X8GimbalCollection(this, listener).fetchGcGain());
    }

    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        reponseCmd(false, groupId, msgId, null, bcd);
    }

    /* Access modifiers changed, original: protected */
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId == 9) {
            switch (msgId) {
                case 6:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 30:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 31:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 40:
                    onErrorResponseWithParam(isAck, packet, bcd);
                    return;
                case 41:
                    if (isAck && isNotNull(packet.getUiCallBack())) {
                        AckGetPitchSpeed pitchSpeed = (AckGetPitchSpeed) packet;
                        if (pitchSpeed.getMsgRpt() == 0) {
                            packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), pitchSpeed);
                            return;
                        } else {
                            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail), null);
                            return;
                        }
                    }
                    return;
                case 42:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 43:
                    if (isAck && isNotNull(packet.getUiCallBack())) {
                        AckGetHorizontalAdjust horizontalAdjust = (AckGetHorizontalAdjust) packet;
                        if (horizontalAdjust.getMsgRpt() == 0) {
                            packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), Float.valueOf(horizontalAdjust.getAngle()));
                            return;
                        } else {
                            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail), null);
                            return;
                        }
                    }
                    return;
                case 47:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 105:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 106:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                default:
                    return;
            }
        }
    }
}
