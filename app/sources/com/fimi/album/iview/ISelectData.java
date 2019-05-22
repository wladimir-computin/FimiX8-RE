package com.fimi.album.iview;

public interface ISelectData {
    void addSingleFile();

    void allSelectMode(boolean z);

    void deleteFile();

    void enterSelectMode();

    void initComplete(boolean z);

    void onDeleteComplete();

    void quitSelectMode();

    void selectSize(int i, long j);

    void startDownload();
}
