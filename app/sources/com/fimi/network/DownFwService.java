package com.fimi.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.fimi.host.Entity.DownloadFwSelectInfo;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDownloadListener;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.x9.upgrade.X9UpdateUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DownFwService extends Service {
    private static final int PROGRESS_MAX = 100;
    public static volatile int checkingTaskCount = 0;
    static DownState state = DownState.UnStart;
    public DisposeDataHandle dataHandle = new DisposeDataHandle(new DisposeDownloadListener() {
        public void onProgress(int progrss, int currentLength) {
            int pro = ((DownFwService.checkingTaskCount * 100) + progrss) / DownFwService.this.needDownDto.size();
            if (progrss != 100) {
                DownFwService.state = DownState.Downing;
            } else if (X9UpdateUtil.getDownList() != null && X9UpdateUtil.getDownList().size() > DownFwService.checkingTaskCount) {
                ((UpfirewareDto) X9UpdateUtil.getDownList().get(DownFwService.checkingTaskCount)).setDownResult("0");
            }
            if (pro == 100) {
                DownFwService.state = DownState.Finish;
            }
            if (DisposeDataHandle.isStop) {
                DownFwService.state = DownState.StopDown;
            }
            DownFwService.this.reportProgress(DownFwService.state, pro, ((UpfirewareDto) DownFwService.this.needDownDto.get(DownFwService.checkingTaskCount)).getSysName());
            if (progrss == 100) {
                DownFwService.this.downFirmware();
            }
        }

        public void onSuccess(Object responseObj) {
            if (X9UpdateUtil.getDownList() != null && X9UpdateUtil.getDownList().size() > DownFwService.checkingTaskCount) {
                ((UpfirewareDto) X9UpdateUtil.getDownList().get(DownFwService.checkingTaskCount)).setDownResult("0");
            }
            if (DownFwService.this.needDownDto == null || DownFwService.this.needDownDto.size() < 1 || DownFwService.checkingTaskCount == DownFwService.this.needDownDto.size()) {
                DownFwService.state = DownState.Finish;
            }
            DownFwService.this.reportProgress(DownFwService.state, (DownFwService.checkingTaskCount * 100) / DownFwService.this.needDownDto.size(), "");
        }

        public void onFailure(Object reasonObj) {
            int pro = 0;
            if (X9UpdateUtil.getDownList() == null || X9UpdateUtil.getDownList().size() <= DownFwService.checkingTaskCount) {
                pro = (DownFwService.checkingTaskCount * 100) / DownFwService.this.needDownDto.size();
            } else {
                ((UpfirewareDto) X9UpdateUtil.getDownList().get(DownFwService.checkingTaskCount)).setDownResult("1");
            }
            DownFwService.state = DownState.DownFail;
            DownFwService.this.reportProgress(DownFwService.state, pro, "");
            DownFwService.this.startCheckingTask();
            DownFwService.this.downFirmware();
        }
    });
    DownloadManager downloadManager = new DownloadManager();
    private ArrayList<DownloadFwSelectInfo> mSelectList;
    List<UpfirewareDto> needDownDto;

    public enum DownState {
        UnStart,
        Start,
        Downing,
        Finish,
        DownFail,
        StopDown
    }

    public static DownState getState() {
        return state;
    }

    public static void setState(DownState state) {
        state = state;
        DisposeDataHandle.isStop = state == DownState.StopDown;
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            this.mSelectList = (ArrayList) intent.getSerializableExtra("listDownloadFwSelectInfo");
            if (this.mSelectList != null) {
                this.needDownDto = HostConstants.getNeedDownFw(false, this.mSelectList);
            } else {
                this.needDownDto = HostConstants.getNeedDownFw(true, this.mSelectList);
            }
            X9UpdateUtil.setDownList(this.needDownDto);
            state = DownState.Start;
            downFirmware();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private synchronized void startCheckingTask() {
        checkingTaskCount++;
    }

    private void downFirmware() {
        if (this.needDownDto != null && this.needDownDto.size() >= 1 && checkingTaskCount < this.needDownDto.size()) {
            UpfirewareDto upfirewareDto = (UpfirewareDto) this.needDownDto.get(checkingTaskCount);
            String fileMd5 = DirectoryPath.getFileOfMd5(DirectoryPath.getFwPath(upfirewareDto.getSysName()));
            if (fileMd5 == null || !fileMd5.equalsIgnoreCase(upfirewareDto.getFileEncode())) {
                this.downloadManager.downLoadFw((UpfirewareDto) this.needDownDto.get(checkingTaskCount), this.dataHandle);
                return;
            }
            startCheckingTask();
            downFirmware();
        }
    }

    private void reportProgress(DownState state, int progress, String name) {
        if (!state.equals(DownState.StopDown)) {
            CopyOnWriteArrayList<IDownProgress> list = DownNoticeMananger.getDownNoticManger().getNoticeList();
            if (list != null && list.size() > 0) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    ((IDownProgress) it.next()).onProgress(state, progress, name);
                }
            }
        }
    }
}
