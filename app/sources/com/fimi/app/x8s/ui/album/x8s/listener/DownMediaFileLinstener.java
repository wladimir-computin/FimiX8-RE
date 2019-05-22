package com.fimi.app.x8s.ui.album.x8s.listener;

import com.fimi.app.x8s.ui.album.x8s.DownFileResultEnum;
import com.fimi.x8sdk.dataparser.MediaFileDownLoadPacket;
import com.fimi.x8sdk.dataparser.X8MediaFileInfo;

public interface DownMediaFileLinstener {
    void onDownFilePre(X8MediaFileInfo x8MediaFileInfo);

    void onEndFile(DownFileResultEnum downFileResultEnum);

    void onProgress(MediaFileDownLoadPacket mediaFileDownLoadPacket);

    void onSartFile();
}
