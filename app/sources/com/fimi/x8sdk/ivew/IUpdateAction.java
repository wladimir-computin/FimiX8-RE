package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.update.fwpack.FwInfo;
import java.util.List;

public interface IUpdateAction {
    void firmwareBuildPack(List<FwInfo> list);

    void queryCurUpdateStatus(UiCallBackListener uiCallBackListener);

    void removeNoticeList();

    void setOnUpdateProgress(IX8UpdateProgressView iX8UpdateProgressView);
}
