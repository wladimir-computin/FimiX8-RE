package com.fimi.x8sdk.controller;

import android.content.Context;
import com.fimi.x8sdk.ivew.IUpdateCheckAction;
import com.fimi.x8sdk.presenter.X8UpdateCheckPresenter;

public class X8UpdateCheckManager {
    private static X8UpdateCheckManager x8UpdateCheckManager = new X8UpdateCheckManager();
    private static X8UpdateCheckPresenter x8UpdateCheckPresenter;

    public X8UpdateCheckManager() {
        x8UpdateCheckPresenter = new X8UpdateCheckPresenter();
    }

    public static X8UpdateCheckManager getInstance() {
        return x8UpdateCheckManager;
    }

    public void setOnIUpdateCheckAction(Context context, IUpdateCheckAction iUpdateCheckAction) {
        x8UpdateCheckPresenter.setIUpdateCheckAction(context, iUpdateCheckAction);
    }

    public void queryCurSystemStatus() {
        x8UpdateCheckPresenter.queryCurSystemStatus();
    }

    public void setPresenterLockMotor(int lock) {
        x8UpdateCheckPresenter.setPresenterLockMotor(lock);
    }

    public void removeNoticeList() {
        x8UpdateCheckPresenter.removeNoticeList();
    }
}
