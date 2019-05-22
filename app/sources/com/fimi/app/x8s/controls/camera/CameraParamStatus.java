package com.fimi.app.x8s.controls.camera;

public class CameraParamStatus {
    public static CameraModelStatus modelStatus = CameraModelStatus.ideal;

    public enum CameraModelStatus {
        ideal,
        takePhoto,
        record,
        recording
    }
}
