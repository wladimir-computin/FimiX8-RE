package com.fimi.app.x8s.interfaces;

import com.alibaba.fastjson.JSONObject;

public interface IX8CameraMainSetListener {
    void awbSetting(String str);

    void colorSetting(String str);

    void evSetting(String str);

    void initCameraParams(JSONObject jSONObject);

    void initOptionsValue();

    void isoSetting(String str);

    void onGridLineSelect(int i);

    void showTopAndBottom(boolean z);

    void shutterSetting(String str);

    void updateResOrSize();
}
