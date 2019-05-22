package com.fimi.app.x8s.entity;

import android.support.v4.media.session.PlaybackStateCompat;
import android.view.animation.Animation;
import com.fimi.app.x8s.tools.X8FileHelper;
import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.fds.FdsUploadTask;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.X8FcLogManager;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Future;

public class X8B2oxFile extends X8FdsFile {
    private FdsUploadTask fdsUploadTask;
    private File file;
    private String fileName;
    private boolean isUpload;
    private String nameShow;
    private Animation operatingAnim;
    private String showLen;
    private Future<?> taskFutrue;
    private File zipFile;
    private String[] zipFileSuffix = new String[]{X8FcLogManager.prexFC, X8FcLogManager.prexCM, X8FcLogManager.prexAPP, X8FcLogManager.prexFcStatus};

    public void setZipFileSuffix(String[] zipFileSuffix) {
        this.zipFileSuffix = zipFileSuffix;
    }

    public String getShowLen() {
        return this.showLen;
    }

    public void setShowLen(String showLen) {
        this.showLen = showLen;
    }

    public boolean isUpload() {
        return this.isUpload;
    }

    public void setUpload(boolean upload) {
        this.isUpload = upload;
    }

    public void setState(FdsUploadState state) {
        this.state = state;
    }

    public Future<?> getTaskFutrue() {
        return this.taskFutrue;
    }

    public void setTaskFutrue(Future<?> taskFutrue) {
        this.taskFutrue = taskFutrue;
    }

    public void stopTask() {
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getFileFdsUrl() {
        return this.filefdsUrl;
    }

    public void setFileFdsUrl(String fileUrl) {
        this.filefdsUrl = fileUrl;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix() {
        return this.fileSuffix;
    }

    public String[] getNeedZipFileBySuffix() {
        return this.zipFileSuffix;
    }

    public void setRunable(FdsUploadTask fdsUploadTask) {
        this.fdsUploadTask = fdsUploadTask;
    }

    public FdsUploadTask getRunable() {
        return this.fdsUploadTask;
    }

    public void setZipFile(File f) {
        this.zipFile = f;
    }

    public File getZipFile() {
        return this.zipFile;
    }

    public String getNameShow() {
        return this.nameShow;
    }

    public void setNameShow(String nameShow) {
        this.nameShow = nameShow;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return this.file;
    }

    public FdsUploadState getState() {
        return this.state;
    }

    public boolean setFile(File file, String prx) {
        this.file = file;
        this.fileName = file.getName();
        boolean ret = false;
        try {
            this.nameShow = new SimpleDateFormat(DateUtil.dateFormatHMS).format(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").parse(this.fileName));
            if (this.fileName.endsWith(X8FcLogManager.prexSD)) {
                this.isUpload = true;
                ret = true;
                setState(FdsUploadState.SUCCESS);
            }
            setFileSuffix(X8FcLogManager.prexSD);
            setShowLen(calculationLen());
        } catch (Exception e) {
            this.nameShow = this.fileName;
        }
        return ret;
    }

    public void resetFile(File file) {
        this.file = file;
    }

    public String calculationLen() {
        long fileS = X8FileHelper.getDirSize(this.file);
        String size = "0B";
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileS < 1024) {
            if (fileS == 0) {
                return "0.00B";
            }
            return df.format((double) fileS) + "B";
        } else if (fileS < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            return df.format(((double) fileS) / 1024.0d) + "K";
        } else {
            if (fileS < 1073741824) {
                return df.format(((double) fileS) / 1048576.0d) + "M";
            }
            return df.format(((double) fileS) / 1.073741824E9d) + "G";
        }
    }

    public String getFilePrexName() {
        return this.fileName.substring(0, this.fileName.lastIndexOf("."));
    }
}
