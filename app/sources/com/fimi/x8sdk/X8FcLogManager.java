package com.fimi.x8sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import ch.qos.logback.classic.turbo.ReconfigureOnChangeFilter;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.x8sdk.appsetting.DataJsonFactory;
import com.fimi.x8sdk.appsetting.ValueSensity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class X8FcLogManager {
    private static String currentWrite = "";
    private static X8FcLogManager log = new X8FcLogManager();
    public static String prexAPP = "setting";
    public static String prexCM = "relay";
    public static String prexFC = "fc";
    public static String prexFcStatus = "fcStatus";
    public static String prexSD = "-sd";
    private static byte[] rn = "\r\n".getBytes();
    private final int REBUILD_FC_LOG_MESSAGE = 0;
    private FileOutputStream appLogOutputStream;
    public boolean closeRebuildFCLog = false;
    private OutputStream cmOutputStream;
    private OutputStream fcOutputStream;
    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X8FcLogManager.this.onDeviceStateChange(0);
                    X8FcLogManager.this.closeRebuildFCLog = true;
                    return;
                default:
                    return;
            }
        }
    };
    private boolean isWriteFirst;
    public LogState state = LogState.ONGROUND;

    public enum LogState {
        INSKY,
        ONGROUND
    }

    public String getCurrentWrite() {
        return currentWrite;
    }

    public static X8FcLogManager getInstance() {
        return log;
    }

    public void onDeviceStateChange(int type) {
        if (this.state == LogState.ONGROUND) {
            if (type == 1) {
                this.state = LogState.INSKY;
                initFileOutputStream();
            }
        } else if (this.state == LogState.INSKY && type == 0) {
            this.state = LogState.ONGROUND;
            closeFileOutputStream();
        }
    }

    public void initFileOutputStream() {
        if (this.fcOutputStream == null || this.cmOutputStream == null || this.appLogOutputStream == null) {
            String dirPath = DateUtil.getStringByFormat(System.currentTimeMillis(), "yyyy-MM-dd-HH-mm-ss-SSS");
            currentWrite = dirPath;
            try {
                File f = new File(DirectoryPath.getX8B2oxPath() + "/" + dirPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                String fcName = dirPath + "." + prexFC;
                String cmName = dirPath + "." + prexCM;
                String appName = dirPath + "." + prexAPP;
                String fcStatesName = dirPath + "." + prexFcStatus;
                this.fcOutputStream = new FileOutputStream(f.getAbsolutePath() + "/" + fcName, true);
                this.cmOutputStream = new FileOutputStream(f.getAbsolutePath() + "/" + cmName, true);
                this.appLogOutputStream = new FileOutputStream(f.getAbsolutePath() + "/" + appName, true);
                this.isWriteFirst = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeFileOutputStream() {
        currentWrite = "";
        this.isWriteFirst = false;
        if (this.fcOutputStream != null) {
            try {
                this.fcOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.fcOutputStream = null;
        }
        if (this.cmOutputStream != null) {
            try {
                this.cmOutputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.cmOutputStream = null;
        }
        if (this.appLogOutputStream != null) {
            try {
                this.appLogOutputStream.close();
            } catch (IOException e22) {
                e22.printStackTrace();
            }
            this.appLogOutputStream = null;
        }
    }

    public void cmLogWrite(byte[] bytes, long l, float distance) {
        if (this.cmOutputStream != null && this.state == LogState.INSKY) {
            try {
                byte[] time = ByteHexHelper.getLongBytes(l);
                byte[] d = ByteUtil.float2byte(distance);
                this.cmOutputStream.write(bytes);
                this.cmOutputStream.write(time);
                this.cmOutputStream.write(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fcLogWrite(byte[] bytes) {
        this.handler.removeMessages(0);
        this.handler.sendEmptyMessageDelayed(0, ReconfigureOnChangeFilter.DEFAULT_REFRESH_PERIOD);
        if (this.fcOutputStream != null && this.state == LogState.INSKY) {
            try {
                this.fcOutputStream.write(bytes);
                if (this.isWriteFirst) {
                    appAllLogWrite();
                    this.isWriteFirst = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (this.closeRebuildFCLog) {
            onDeviceStateChange(1);
            this.closeRebuildFCLog = false;
        }
    }

    public void appAllLogWrite() {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                this.appLogOutputStream.write(DataJsonFactory.getAllDataJsonString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appValueFloatChange(String tag, float oldV, float newV) {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                this.appLogOutputStream.write(DataJsonFactory.onValueChange(tag, oldV, newV).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appValueBoleanChange(String tag, boolean oldV, boolean newV) {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                this.appLogOutputStream.write(DataJsonFactory.onValueBooleanChange(tag, oldV, newV).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appValueSensityChange(String tag, ValueSensity oldV, ValueSensity newV) {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                this.appLogOutputStream.write(DataJsonFactory.appValueSensityChange(tag, oldV, newV).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
