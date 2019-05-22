package com.fimi.kernel.upgrade.interfaces;

import java.util.List;

public interface IUpgradeListener {
    void onConnectFialed(String str);

    void onCurrentUpgrade(int i);

    void onFileNotFind();

    void onInterrupt(String str, List<IFirmwareInfo> list);

    void onProcess(long j, long j2);

    void onResult(boolean z, String str);

    void onSendError(String str);

    void onUploadComplete(int i, List<IFirmwareInfo> list);
}
