package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.command.FwUpdateCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.GpsInfoCmd;
import com.fimi.x8sdk.ivew.IFcAction;

public class FcPresenter extends BasePresenter implements IFcAction {
    public FcPresenter() {
        addNoticeListener();
    }

    public void takeOff(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).takeOff((byte) 16));
    }

    public void takeOffExit(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).takeOff((byte) 19));
    }

    public void land(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).land((byte) 21));
    }

    public void landExit(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).land((byte) 24));
    }

    public void setFollowStandby(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowCmd(84));
    }

    public void setFollowExcute(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowCmd(80));
    }

    public void setFollowExit(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowCmd(83));
    }

    public void getFwVersion(byte moduleName, byte type, UiCallBackListener listener) {
        sendCmd(new FwUpdateCollection(this, listener).getVersion(moduleName, type));
    }

    public void setAiFollowPoint2Point(double longitude, double latitude, int altitude, int speed, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowPoint2Point(longitude, latitude, altitude, speed));
    }

    public void setAiFollowPoint2PointExcute(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowPoint2PointExcute((byte) 48));
    }

    public void getAiFollowPoint(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowPoint2PointExcute((byte) 53));
    }

    public void setAiFollowPoint2PointExite(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowPoint2PointExcute((byte) 51));
    }

    public void setAiSurroundExcute(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiSurroundExcute(FcCollection.MSG_SET_SURROUND_EXCUTE));
    }

    public void setAiSurroundExite(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiSurroundExcute(FcCollection.MSG_SET_SURROUND_EXIT));
    }

    public void setAiSurroundCiclePoint(double longitude, double latitude, float altitude, double longitudeTakeoff, double latitudeTakeoff, float altitudeTakeoff, int type, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiSurroundPoint(68, longitude, latitude, altitude, longitudeTakeoff, latitudeTakeoff, altitudeTakeoff, type));
    }

    public void getAiSurroundCiclePoint(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiSurroundPoint());
    }

    public void setAiSurroundSpeed(int value, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiSurroundSpeed(FcCollection.MSG_SET_SURROUND_SPEED, value));
    }

    public void setAiSurroundOrientation(int value, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiSurroundOrientation(FcCollection.MSG_SET_SURROUND_DEVICE_ORIENTATION, value));
    }

    public void getAiSurroundSpeed(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiSurroundSpeed());
    }

    public void getAiSurroundOrientation(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiSurroundOrientation());
    }

    public void setAiRetureHome(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiRetureHome(26));
    }

    public void setAiRetureHomeExite(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiRetureHome(29));
    }

    public void setAiLinePoints(CmdAiLinePoints points, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiLinePoints(points));
    }

    public void setAiLinePointsAction(CmdAiLinePointsAction action, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiLinePointsAction(action));
    }

    public void setAiLineExcute(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiLineExcute());
    }

    public void setAiLineExite(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiLineExite());
    }

    public void getAiLinePoint(int number, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiLinePoint(number));
    }

    public void setAiAutoPhotoValue(CmdAiAutoPhoto v, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiAutoPhotoValue(v));
    }

    public void getAiLinePointValue(int number, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiLinePointsAction(number));
    }

    public void setAiAutoPhotoExcute(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiAutoPhotoExcute());
    }

    public void setAiAutoPhotoExit(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiAutoPhotoExit());
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
        if (groupId == 3) {
            onNormalResponseWithParamForNav(isAck, packet, bcd);
        } else if (groupId == 16 && 177 == msgId) {
            onNormalResponseWithParam(isAck, packet, bcd);
        }
    }

    public void syncTime(UiCallBackListener callBackListener) {
        sendCmd(new FcCollection(this, callBackListener).setSyncTimeCmd());
    }

    public void setAiVcOpen(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowVcEnable(96));
    }

    public void setAiVcClose(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowVcEnable(97));
    }

    public void setAiVcNotityFc(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowVcEnable(98));
    }

    public void setAiFollowModle(int type, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowModle(type));
    }

    public void getAiFollowModle(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiFollowModle());
    }

    public void setAiFollowSpeed(int value, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowSpeed(value));
    }

    public void getAiFollowSpeed(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getAiFollowSpeed());
    }

    public void setHomePoint(float h, double lat, double lng, int mode, float accuracy, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setHomePoint(h, lat, lng, mode, accuracy));
    }

    public void setScrewPrameter(AckAiScrewPrameter prameter, UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setScrewPrameter(prameter));
    }

    public void getScrewPrameter(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getScrewPrameter());
    }

    public void setScrewStart(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setScrewStart());
    }

    public void setScrewPause(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setScrewPause());
    }

    public void setScrewResume(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setScrewResume());
    }

    public void setScrewExite(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setScrewExite());
    }

    public void sysCtrlMode2AiVc(UiCallBackListener listener, int ctrlMode) {
        sendCmd(new FcCollection(this, listener).sysCtrlMode2AiVc(ctrlMode));
    }

    public void setPressureInfo(float alt, float hPa) {
        sendCmd(new FcCollection(this, null).setPressureInfo(alt, hPa));
    }

    public void setGpsInfo(GpsInfoCmd gps) {
        sendCmd(new FcCollection(this, null).setGpsInfo(gps));
    }
}
