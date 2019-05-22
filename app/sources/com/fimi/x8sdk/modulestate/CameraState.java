package com.fimi.x8sdk.modulestate;

import com.fimi.kernel.utils.BitUtil;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.entity.CameraSystemState;

public class CameraState extends BaseState {
    AutoCameraStateADV autoCameraStateADV;
    CameraSystemState cameraSystemState = new CameraSystemState();
    private boolean takingPanoramicPhotos;
    private int token = -1;

    public boolean isAvailable() {
        return getLoginState() == 1;
    }

    public void setCameraStatus(int cameraStatus) {
        setLoginState(BitUtil.getBitByByte(cameraStatus, 2));
    }

    public int getToken() {
        return this.token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public AutoCameraStateADV getAutoCameraStateADV() {
        return this.autoCameraStateADV;
    }

    public boolean isDelayedPhotography() {
        if (this.autoCameraStateADV == null) {
            return false;
        }
        return this.autoCameraStateADV.isDelayedPhotography();
    }

    public boolean isTakingPanoramicPhotos() {
        return this.takingPanoramicPhotos;
    }

    public void setTakingPanoramicPhotos(boolean takingPanoramicPhotos) {
        this.takingPanoramicPhotos = takingPanoramicPhotos;
    }

    public void setAutoCameraStateADV(AutoCameraStateADV autoCameraStateADV) {
        this.autoCameraStateADV = autoCameraStateADV;
    }

    public CameraSystemState getCameraSystemState() {
        return this.cameraSystemState;
    }

    public boolean isConnect() {
        return this.token >= 0;
    }
}
