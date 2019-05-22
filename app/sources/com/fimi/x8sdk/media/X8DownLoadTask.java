package com.fimi.x8sdk.media;

import ch.qos.logback.core.CoreConstants;
import com.fimi.x8sdk.command.X8DownLoadCmd;

public class X8DownLoadTask {
    private int fileLen;
    private String fileName;
    private int offSet;
    private X8DownLoadCmd x8DownLoadCmd;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getOffSet() {
        return this.offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public int getFileLen() {
        return this.fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public X8DownLoadCmd getX8DownLoadCmd() {
        return this.x8DownLoadCmd;
    }

    public void setX8DownLoadCmd(X8DownLoadCmd x8DownLoadCmd) {
        this.x8DownLoadCmd = x8DownLoadCmd;
    }

    public String toString() {
        return "X8DownLoadTask{fileName='" + this.fileName + CoreConstants.SINGLE_QUOTE_CHAR + ", offSet=" + this.offSet + ", fileLen=" + this.fileLen + ", x8DownLoadCmd=" + this.x8DownLoadCmd + CoreConstants.CURLY_RIGHT;
    }
}
