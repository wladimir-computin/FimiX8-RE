package com.fimi.app.x8s.manager;

import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.X8VcManager;

public class X8FpvManager {
    public static boolean isUpdateing = false;
    private int fpvModeState = 0;
    private int lastState = 0;
    private X8MapVideoController mMapVideoController;
    private X8VcManager mX8VcManager;
    private int state = 0;

    public X8FpvManager(X8VcManager mX8VcManager, X8MapVideoController mMapVideoController) {
        this.mX8VcManager = mX8VcManager;
        this.mMapVideoController = mMapVideoController;
    }

    public void onDroneConnectState(boolean isConnect) {
        if (isConnect) {
            this.state = 1;
            if (this.state != this.lastState) {
                this.lastState = 1;
                this.fpvModeState = 1;
            }
            sendVcSetFpvMode();
            setVcFpvLostSeq();
            return;
        }
        this.lastState = 0;
        this.state = 0;
        this.fpvModeState = 0;
    }

    public void sendVcSetFpvMode() {
        if (this.fpvModeState == 1) {
            this.fpvModeState = 2;
            this.mX8VcManager.setVcFpvMode(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        X8FpvManager.this.fpvModeState = 3;
                    } else {
                        X8FpvManager.this.fpvModeState = 1;
                    }
                }
            }, 1);
        }
    }

    public void setVcFpvLostSeq() {
        if (!isUpdateing && this.fpvModeState == 3 && this.mMapVideoController.getVideoView().getmH264Decoder() != null && this.mMapVideoController.getVideoView().getmH264Decoder().getmH264Player() != null) {
            this.mX8VcManager.setVcFpvLostSeq(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            }, this.mMapVideoController.getVideoView().getmH264Decoder().getmH264Player().getLostSeq());
        }
    }
}
