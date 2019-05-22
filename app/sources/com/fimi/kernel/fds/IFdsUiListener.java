package com.fimi.kernel.fds;

public interface IFdsUiListener {
    void onFailure(IFdsFileModel iFdsFileModel);

    void onProgress(IFdsFileModel iFdsFileModel, int i);

    void onStop(IFdsFileModel iFdsFileModel);

    void onSuccess(IFdsFileModel iFdsFileModel);
}
