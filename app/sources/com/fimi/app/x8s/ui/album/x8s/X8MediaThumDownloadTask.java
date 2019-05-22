package com.fimi.app.x8s.ui.album.x8s;

import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.app.x8s.ui.album.x8s.listener.DownMediaFileLinstener;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.session.MediaDataListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.x8sdk.command.X8DownLoadCmd;
import com.fimi.x8sdk.command.X8MediaCmd;
import com.fimi.x8sdk.dataparser.MediaFileDownLoadPacket;
import com.fimi.x8sdk.dataparser.X8MediaFileInfo;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8MediaThumDownloadTask implements Runnable, MediaDataListener {
    DownMediaFileLinstener downMediaFileLinstener = new DownMediaFileLinstener() {
        public void onSartFile() {
            X8MediaThumDownloadTask.this.createNewFile();
        }

        public void onDownFilePre(X8MediaFileInfo x8MediaFileInfo) {
        }

        public void onProgress(MediaFileDownLoadPacket downLoadPacket) {
            if (X8MediaThumDownloadTask.this.randomAccessFile != null && downLoadPacket != null && X8MediaThumDownloadTask.this.model != null) {
                try {
                    if (X8MediaThumDownloadTask.this.finished >= X8MediaThumDownloadTask.this.model.getFileSize() || X8MediaThumDownloadTask.this.model.getFileSize() <= 0 || downLoadPacket.getPlayloadSize() <= (short) 0) {
                        onEndFile(DownFileResultEnum.Fail);
                    } else if (X8MediaThumDownloadTask.this.finished == ((long) downLoadPacket.getOffSet())) {
                        X8MediaThumDownloadTask.this.finished = X8MediaThumDownloadTask.this.finished + ((long) downLoadPacket.getPlayloadSize());
                        X8MediaThumDownloadTask.this.randomAccessFile.write(downLoadPacket.getPlayData());
                        if (downLoadPacket.isFinished()) {
                            onEndFile(DownFileResultEnum.Success);
                        }
                    } else {
                        X8MediaThumDownloadTask.this.sendCmd(new X8DownLoadCmd().downMediaFile((int) X8MediaThumDownloadTask.this.finished, NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE, X8MediaThumDownloadTask.this.model.getThumFileUrl(), false));
                    }
                } catch (IOException e) {
                    onEndFile(DownFileResultEnum.Fail);
                    e.printStackTrace();
                }
            }
        }

        public void onEndFile(DownFileResultEnum resultEnum) {
            switch (AnonymousClass2.$SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[resultEnum.ordinal()]) {
                case 1:
                    NoticeManager.getInstance().removeMediaListener(X8MediaThumDownloadTask.this);
                    X8MediaThumDownloadTask.this.model.setDownLoadThum(true);
                    X8MediaThumDownloadTask.this.listener.onSuccess(X8MediaThumDownloadTask.this.model);
                    HostLogBack.getInstance().writeLog("Alanqiu  ================onEndFile:" + X8MediaThumDownloadTask.this.model.toString());
                    return;
                case 3:
                    NoticeManager.getInstance().removeMediaListener(X8MediaThumDownloadTask.this);
                    X8MediaThumDownloadTask.this.listener.onFailure(X8MediaThumDownloadTask.this.model);
                    return;
                default:
                    return;
            }
        }
    };
    private long finished = 0;
    private OnDownloadListener listener;
    short max_size = NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE;
    private MediaModel model;
    RandomAccessFile randomAccessFile;

    /* renamed from: com.fimi.app.x8s.ui.album.x8s.X8MediaThumDownloadTask$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum = new int[DownFileResultEnum.values().length];

        static {
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Success.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Stop.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Fail.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public X8MediaThumDownloadTask(MediaModel model, OnDownloadListener listener) {
        this.model = model;
        this.listener = listener;
        model.setThumDownloading(true);
        NoticeManager.getInstance().addMediaListener(this);
    }

    public void run() {
        this.downMediaFileLinstener.onSartFile();
    }

    private void sendCmd(X8MediaCmd cmd) {
        if (cmd != null) {
            SessionManager.getInstance().sendCmd(cmd);
        }
    }

    private void createNewFile() {
        if (this.model.getThumLocalFilePath() != null) {
            File rootFile = new File(this.model.getThumLocalFilePath());
            if (rootFile.exists() && rootFile.length() == this.model.getFileSize()) {
                this.downMediaFileLinstener.onEndFile(DownFileResultEnum.Success);
                return;
            }
            if (rootFile.exists()) {
                rootFile.delete();
            }
            try {
                rootFile.createNewFile();
                this.randomAccessFile = new RandomAccessFile(rootFile, "rwd");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (this.model.getThumFileUrl() == null) {
                this.downMediaFileLinstener.onEndFile(DownFileResultEnum.Fail);
                return;
            }
            HostLogBack.getInstance().writeLog("Alanqiu  ===============createNewFile:" + this.model.getThumFileUrl());
            sendCmd(new X8DownLoadCmd().downMediaFile((int) this.finished, NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE, this.model.getThumFileUrl(), false));
        }
    }

    public void mediaDataCallBack(byte[] data) {
        if (data != null && data.length > 0 && data[0] == (byte) 1) {
            MediaFileDownLoadPacket downLoadPacket = new MediaFileDownLoadPacket();
            downLoadPacket.unPacket(data);
            this.downMediaFileLinstener.onProgress(downLoadPacket);
            HostLogBack.getInstance().writeLog("Alanqiu  ===============Thum===mediaDataCallBack:" + downLoadPacket.toString());
        }
    }

    public void removeMediaListener() {
        NoticeManager.getInstance().removeMediaListener(this);
    }
}
