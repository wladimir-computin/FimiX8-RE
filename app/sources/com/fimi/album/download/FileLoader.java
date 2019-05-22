package com.fimi.album.download;

import com.fimi.album.download.entity.FileInfo;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.download.task.DownloadTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileLoader {
    ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void queueDownload(FileInfo info, OnDownloadListener listener) {
        this.executorService.submit(new DownloadTask(info, listener));
    }
}
