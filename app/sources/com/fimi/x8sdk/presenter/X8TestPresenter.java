package com.fimi.x8sdk.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.kernel.connect.tcp.SocketOption;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.milink.ByteArrayToIntArray;
import com.fimi.kernel.utils.FileUtil;
import com.fimi.x8sdk.command.AoaTestColletion;
import com.fimi.x8sdk.command.FwUpdateCollection;
import com.fimi.x8sdk.command.X8SendCmd;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.connect.tcp.FileDataTcpConnect;
import com.fimi.x8sdk.dataparser.CameraRequestUpdate;
import com.fimi.x8sdk.dataparser.CameraVersion;
import com.fimi.x8sdk.ivew.IAOATestView;
import com.fimi.x8sdk.ivew.ICameraTestUpdateView;
import com.fimi.x8sdk.update.fwpack.ByteHexHelper;
import com.fimi.x8sdk.update.fwpack.FirmwareBuildPack;
import java.io.FileInputStream;

public class X8TestPresenter extends BasePresenter {
    FileDataTcpConnect fileDataTcpConnect;
    IAOATestView iaoaTestView;
    VideodDataListener listener = new VideodDataListener() {
        public void onRawdataCallBack(byte[] str) {
            if (X8TestPresenter.this.iaoaTestView != null) {
            }
        }
    };
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                X8TestPresenter.this.updateView.showDataConnect(true);
            } else if (msg.what == 1) {
                X8TestPresenter.this.updateView.showDataConnect(false);
            } else if (msg.what == 3) {
                X8TestPresenter.this.updateView.showUploadProgress(3);
            } else if (msg.what == 4) {
                X8TestPresenter.this.updateView.showUploadProgress(4);
            }
        }
    };
    ICameraTestUpdateView updateView;

    public X8TestPresenter(ICameraTestUpdateView updateView, IAOATestView iaoaTestView) {
        this.updateView = updateView;
        this.iaoaTestView = iaoaTestView;
        this.fileDataTcpConnect = new FileDataTcpConnect();
        addNoticeListener(this.listener);
    }

    public void getCameraVer() {
        sendCmd(new FwUpdateCollection().getCameraVer());
    }

    public void requestUpdate() {
        sendCmd(new FwUpdateCollection().requestStartUpdate());
    }

    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        super.onDataCallBack(groupId, msgId, packet);
        if (groupId == 16) {
            if (msgId == -79) {
                CameraVersion a12Version = (CameraVersion) packet;
                this.updateView.showA12Version(a12Version.getMainVersion() + "" + a12Version.getStepVer());
            }
            if (msgId == 2) {
                this.updateView.reqestUpdate(((CameraRequestUpdate) packet).getMsgRpt());
            }
            if (msgId == 3) {
                this.updateView.requstUploadFileACK(0);
            }
        }
    }

    public void requestUploadFile() {
        byte[] fileBytes = FileUtil.getFileBytes(FirmwareBuildPack.PKG_UPDATE_FILE);
        byte[] byteCrc = ByteHexHelper.intToFourHexBytes(ByteArrayToIntArray.CRC32Software(fileBytes, fileBytes.length));
        sendCmd(new FwUpdateCollection().requestUploadFile(ByteHexHelper.intToFourHexBytes(fileBytes.length), byteCrc));
    }

    public void connectDataChanel() {
        boolean isConnect = this.fileDataTcpConnect.getFileSocketManager().connect();
        Log.d("moweiru", "isConnect:" + isConnect);
        if (isConnect) {
            this.mHandler.sendEmptyMessage(0);
        } else {
            this.mHandler.sendEmptyMessage(1);
        }
    }

    public void disConnectDataChanel() {
        this.mHandler.sendEmptyMessage(2);
    }

    public void uploadFwFile() {
        try {
            FileInputStream fis = new FileInputStream(FirmwareBuildPack.PKG_UPDATE_FILE);
            byte[] sendBuffer = new byte[SocketOption.RECEIVE_BUFFER_SIZE];
            long total = 0;
            while (true) {
                int length = fis.read(sendBuffer);
                if (length > 0) {
                    total += (long) length;
                    this.fileDataTcpConnect.sendFileData(sendBuffer, 0, length);
                    Thread.sleep(200);
                    this.mHandler.sendEmptyMessage(3);
                } else {
                    this.mHandler.sendEmptyMessage(4);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendContent(String content) {
        sendCmd(new AoaTestColletion().getTestContent(content));
    }

    public X8SendCmd getTestContent(String content) {
        return null;
    }
}
