package com.fimi.x8sdk.controller;

import com.fimi.x8sdk.entity.ErrCodeEntity;
import com.fimi.x8sdk.listener.ErrcodeListener;
import com.fimi.x8sdk.process.FcErrProcess;
import java.util.List;

public class ErrCodeController {
    public void registerErrCodeListener(ErrcodeListener listener) {
        FcErrProcess.getFcErrProcess().registerErrListener(listener);
    }

    public void removeErrCodeListener(ErrcodeListener listener) {
        FcErrProcess.getFcErrProcess().removeErrListener(listener);
    }

    public void removeAllErrCodeListener() {
        FcErrProcess.getFcErrProcess().removeAllErrList();
    }

    public List<ErrCodeEntity> getErrCodes() {
        return FcErrProcess.getFcErrProcess().getErrCodeEntities();
    }
}
