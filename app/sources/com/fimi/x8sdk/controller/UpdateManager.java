package com.fimi.x8sdk.controller;

import android.content.Context;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.ivew.IUpdateAction;
import com.fimi.x8sdk.ivew.IX8UpdateProgressView;
import com.fimi.x8sdk.presenter.X8UpdatePresenter;
import com.fimi.x8sdk.update.fwpack.FwInfo;
import java.util.List;

public class UpdateManager implements IUpdateAction {
    IUpdateAction updateAction;

    public UpdateManager(Context context) {
        this.updateAction = new X8UpdatePresenter(context);
    }

    public void queryCurUpdateStatus(UiCallBackListener callBackListener) {
        this.updateAction.queryCurUpdateStatus(callBackListener);
    }

    public void firmwareBuildPack(List<FwInfo> fwInfoList) {
        this.updateAction.firmwareBuildPack(fwInfoList);
    }

    public void setOnUpdateProgress(IX8UpdateProgressView ix8UpdateProgressView) {
        this.updateAction.setOnUpdateProgress(ix8UpdateProgressView);
    }

    public void removeNoticeList() {
        this.updateAction.removeNoticeList();
    }
}
