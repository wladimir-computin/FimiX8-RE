package com.fimi.x8sdk.listener;

public interface IX8FiveKeyDefine {
    void backCenterDownSwitch(boolean z);

    void batteryFrame(boolean z);

    void cancelParameterSetting();

    void mapFPVSwitch(boolean z);

    void mediaFrame(boolean z);

    void meteringSwitch(boolean z);

    void selfCheckFrame(boolean z);

    void setContrastRatio(boolean z, boolean z2);

    void setSaturation(boolean z, boolean z2);

    void shootModeSwitch(boolean z);
}
