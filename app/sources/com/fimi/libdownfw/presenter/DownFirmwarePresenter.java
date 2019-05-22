package com.fimi.libdownfw.presenter;

import com.fimi.libdownfw.ivew.IFirmwareDownView;
import com.fimi.network.DownFwService.DownState;
import com.fimi.network.DownNoticeMananger;
import com.fimi.network.IDownProgress;

public class DownFirmwarePresenter implements IDownProgress {
    IFirmwareDownView iFirmwareDownView;

    public DownFirmwarePresenter(IFirmwareDownView iFirmwareDownView) {
        this.iFirmwareDownView = iFirmwareDownView;
        DownNoticeMananger.getDownNoticManger().addDownNoticeList(this);
    }

    public void onProgress(DownState isResult, int progrss, String name) {
        this.iFirmwareDownView.showDownFwProgress(isResult, progrss, name);
    }

    public void removerDownNoticeLisnter() {
        DownNoticeMananger.getDownNoticManger().remioveDownNoticeList(this);
    }
}
