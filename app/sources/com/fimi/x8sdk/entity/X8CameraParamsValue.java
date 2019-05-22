package com.fimi.x8sdk.entity;

import com.fimi.x8sdk.jsonResult.CurParamsJson;

public class X8CameraParamsValue {
    static X8CameraParamsValue paramsValue;
    private CurParamsJson curParamsJson = new CurParamsJson();

    public static X8CameraParamsValue getInstance() {
        if (paramsValue == null) {
            synchronized (X8CameraParamsValue.class) {
                paramsValue = new X8CameraParamsValue();
            }
        }
        return paramsValue;
    }

    public CurParamsJson getCurParamsJson() {
        return this.curParamsJson;
    }
}
