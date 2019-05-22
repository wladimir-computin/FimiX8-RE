package com.fimi.app.x8s.interfaces;

public interface IX8BatterySettingListener {
    void setLowPowerOperation(int i);

    void setLowPowerSetting(float f);

    void setLowPowerWarning(float f);

    void setSeriousLowPowerOperation(int i);

    void setSeriousLowPowerSetting(float f);

    void setSeriousLowPowerWarning(float f);
}
