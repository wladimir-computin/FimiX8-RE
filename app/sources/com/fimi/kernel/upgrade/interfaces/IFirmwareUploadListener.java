package com.fimi.kernel.upgrade.interfaces;

import java.util.List;

public interface IFirmwareUploadListener {
    void onCurrentUpgrade(int i);

    void onDeviceConnectInterrupt(List<IFirmwareInfo> list);

    void onProcess(int i, int i2);

    void onUpgradeErrorInfo(String str);

    void onUploaAlldError();

    void onUploadComplete(int i, List<IFirmwareInfo> list);
}
