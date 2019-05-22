package com.fimi.app.x8s.ui.album.x8s;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.fimi.album.biz.SuffixUtils;
import com.fimi.album.download.interfaces.IMediaFileLoad;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.app.x8s.ui.album.x8s.listener.DownMediaFileLinstener;
import com.fimi.kernel.connect.session.MediaDataListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.x8sdk.command.X8DownLoadCmd;
import com.fimi.x8sdk.command.X8MediaCmd;
import com.fimi.x8sdk.dataparser.MediaFileDownLoadPacket;
import com.fimi.x8sdk.dataparser.X8MediaFileInfo;
import com.fimi.x8sdk.dataparser.X8MediaFileInfo.MediaFileType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8MediaFileLoad<T extends MediaModel> implements IMediaFileLoad, MediaDataListener {
    private String X8_MEDIA_DES = "media.xml";
    X8MediaFileInfo downingFileInfo;
    long fileLength = 0;
    DownMediaFileLinstener fileLinstener = new DownMediaFileLinstener() {
        public void onSartFile() {
            X8MediaFileLoad.this.sendCmd(new X8DownLoadCmd().getMediaXmlFile(X8MediaFileLoad.this.X8_MEDIA_DES));
        }

        public void onProgress(MediaFileDownLoadPacket downLoadPacket) {
            try {
                X8MediaFileLoad.this.fileLength = X8MediaFileLoad.this.randomAccessFile.length();
                if (X8MediaFileLoad.this.fileLength >= ((long) downLoadPacket.getOffSet())) {
                    X8MediaFileLoad.this.randomAccessFile.seek(X8MediaFileLoad.this.fileLength);
                    X8MediaFileLoad.this.randomAccessFile.write(downLoadPacket.getPlayData());
                    if (downLoadPacket.isFinished()) {
                        onEndFile(DownFileResultEnum.Success);
                    }
                } else if (!X8MediaFileLoad.this.isAwait) {
                    X8MediaFileLoad.this.isAwait = true;
                    HandlerManager.obtain().getHandlerInMainThread().postDelayed(new Runnable() {
                        public void run() {
                            X8MediaFileLoad.this.reqNextPacket(X8MediaFileLoad.this.fileLength);
                            X8MediaFileLoad.this.isAwait = false;
                        }
                    }, 3000);
                }
            } catch (IOException e) {
                onEndFile(DownFileResultEnum.Fail);
                e.printStackTrace();
            }
        }

        public void onDownFilePre(X8MediaFileInfo fileInfo) {
            X8MediaFileLoad.this.createRootFile();
            X8MediaFileLoad.this.startDownloadTask(fileInfo);
        }

        public void onEndFile(DownFileResultEnum resultEnum) {
            switch (AnonymousClass3.$SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[resultEnum.ordinal()]) {
                case 1:
                    X8MediaFileLoad.this.mHandler.sendEmptyMessageDelayed(0, 500);
                    return;
                case 3:
                    X8MediaFileLoad.this.closeWriteStream();
                    X8MediaFileLoad.this.parseOnlineData();
                    NoticeManager.getInstance().removeMediaListener(X8MediaFileLoad.this);
                    X8MediaFileLoad.this.mHandler.sendEmptyMessageDelayed(1, 500);
                    return;
                default:
                    return;
            }
        }
    };
    X8FileInfo info;
    boolean isAwait = false;
    private boolean isErr = false;
    private List<MediaModel> listData;
    private OnX8MediaFileListener listener;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X8MediaFileLoad.this.listener.onComplete(false);
                    return;
                case 1:
                    X8MediaFileLoad.this.listener.onComplete(true);
                    return;
                default:
                    return;
            }
        }
    };
    public SuffixUtils mSuffixUtils = SuffixUtils.obtain();
    private short max_size = NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE;
    public final String orginPath = (this.rootPath + "/orgin");
    RandomAccessFile randomAccessFile;
    File rootFile = new File(this.xmlPath);
    private String rootPath = (Environment.getExternalStorageDirectory().getPath() + "/x8/media");
    public final String tempPath = (this.rootPath + "/temp");
    List<VideoThumInfo> thumInfos = new ArrayList();
    public final String thumPath = (this.rootPath + "/thum");
    public final String xmlPath = (this.rootPath + "/" + this.X8_MEDIA_DES);

    /* renamed from: com.fimi.app.x8s.ui.album.x8s.X8MediaFileLoad$3 */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum = new int[DownFileResultEnum.values().length];

        static {
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Fail.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Stop.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Success.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public X8MediaFileLoad(OnX8MediaFileListener listener, List<MediaModel> listData) {
        this.listener = listener;
        this.listData = listData;
        this.info = new X8FileInfo();
        NoticeManager.getInstance().addMediaListener(this);
    }

    public void startLoad() {
        this.fileLinstener.onSartFile();
    }

    private void sendCmd(X8MediaCmd cmd) {
        if (cmd != null) {
            SessionManager.getInstance().sendCmd(cmd);
        }
    }

    public void stopLoad() {
        this.info.setStop(true);
    }

    public void mediaDataCallBack(byte[] data) {
        if (data != null && data.length > 0) {
            byte cmdType = data[0];
            if (cmdType == (byte) 0) {
                X8MediaFileInfo x8MediaFileInfo = new X8MediaFileInfo();
                x8MediaFileInfo.unPacket(data);
                x8MediaFileInfo.setMediaFileType(MediaFileType.RootFile);
                this.fileLinstener.onDownFilePre(x8MediaFileInfo);
            } else if (cmdType == (byte) 1) {
                MediaFileDownLoadPacket downLoadPacket = new MediaFileDownLoadPacket();
                downLoadPacket.unPacket(data);
                if (this.X8_MEDIA_DES.equals(downLoadPacket.getFileName())) {
                    this.fileLinstener.onProgress(downLoadPacket);
                }
            }
        }
    }

    private void createRootFile() {
        File rootDir = new File(this.rootPath);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        if (this.rootFile.exists()) {
            this.rootFile.delete();
        }
        try {
            this.rootFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.randomAccessFile = new RandomAccessFile(this.rootFile, "rwd");
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    private void startDownloadTask(X8MediaFileInfo x8MediaFileInfo) {
        this.downingFileInfo = x8MediaFileInfo;
        sendCmd(new X8DownLoadCmd().downMediaFile(0, NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE, x8MediaFileInfo.getFileName(), false));
    }

    private void reqNextPacket(long offset) {
        if (((long) this.downingFileInfo.getFileSize()) - offset > 0) {
            this.max_size = ((long) this.downingFileInfo.getFileSize()) - offset >= ((long) this.max_size) ? this.max_size : (short) ((int) (((long) this.downingFileInfo.getFileSize()) - offset));
            sendCmd(new X8DownLoadCmd().downMediaFile((int) offset, this.max_size, this.X8_MEDIA_DES, false));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:62:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0290 A:{SYNTHETIC, Splitter:B:31:0x0290} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x03ae A:{SYNTHETIC, Splitter:B:40:0x03ae} */
    public void parseOnlineData() {
        /*
        r32 = this;
        r22 = new java.io.File;
        r0 = r32;
        r0 = r0.thumPath;
        r27 = r0;
        r0 = r22;
        r1 = r27;
        r0.<init>(r1);
        r27 = r22.exists();
        if (r27 != 0) goto L_0x0018;
    L_0x0015:
        r22.mkdir();
    L_0x0018:
        r20 = new java.io.File;
        r0 = r32;
        r0 = r0.orginPath;
        r27 = r0;
        r0 = r20;
        r1 = r27;
        r0.<init>(r1);
        r27 = r20.exists();
        if (r27 != 0) goto L_0x0030;
    L_0x002d:
        r20.mkdir();
    L_0x0030:
        r21 = new java.io.File;
        r0 = r32;
        r0 = r0.tempPath;
        r27 = r0;
        r0 = r21;
        r1 = r27;
        r0.<init>(r1);
        r27 = r21.exists();
        if (r27 != 0) goto L_0x0048;
    L_0x0045:
        r21.mkdir();
    L_0x0048:
        r0 = r32;
        r0 = r0.thumInfos;
        r27 = r0;
        r27.clear();
        r26 = new java.io.File;
        r0 = r32;
        r0 = r0.xmlPath;
        r27 = r0;
        r26.<init>(r27);
        r27 = r26.exists();
        if (r27 == 0) goto L_0x0293;
    L_0x0062:
        r6 = 0;
        r18 = 0;
        r0 = r32;
        r0 = r0.listData;	 Catch:{ Exception -> 0x03cc }
        r27 = r0;
        r27.clear();	 Catch:{ Exception -> 0x03cc }
        r7 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x03cc }
        r27 = new java.io.FileReader;	 Catch:{ Exception -> 0x03cc }
        r0 = r27;
        r1 = r26;
        r0.<init>(r1);	 Catch:{ Exception -> 0x03cc }
        r0 = r27;
        r7.<init>(r0);	 Catch:{ Exception -> 0x03cc }
    L_0x007e:
        r18 = r7.readLine();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        if (r18 == 0) goto L_0x03b2;
    L_0x0084:
        r27 = "\\|";
        r0 = r18;
        r1 = r27;
        r17 = r0.split(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        if (r17 == 0) goto L_0x007e;
    L_0x0090:
        r0 = r17;
        r0 = r0.length;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r28 = 4;
        r0 = r27;
        r1 = r28;
        if (r0 != r1) goto L_0x007e;
    L_0x009d:
        r19 = new com.fimi.album.entity.MediaModel;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r19.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = 0;
        r13 = r17[r27];	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = java.lang.Long.parseLong(r13);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r28;
        r0.setFileSize(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = 1;
        r8 = r17[r27];	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = 2;
        r12 = r17[r27];	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r0.setName(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = 3;
        r25 = r17[r27];	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r25;
        r0.setFileUrl(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r10 = new java.text.SimpleDateFormat;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = "yyyy-MMM-dd_HH:mm:ss";
        r28 = java.util.Locale.US;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r28;
        r10.<init>(r0, r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r9 = r10.parse(r8);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r15 = new java.text.SimpleDateFormat;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = "yyyy.MM.dd HH:mm:ss";
        r0 = r27;
        r15.<init>(r0);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r16 = r15.format(r9);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r16;
        r0.setFormatDate(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r9.getTime();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r28;
        r0.setCreateDate(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r9.getTime();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r27 = r0.append(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setMd5(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.orginPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r19;
        r1 = r27;
        r0.setLocalFileDir(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r27;
        r27 = r0.judgeVideo(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        if (r27 == 0) goto L_0x0294;
    L_0x0134:
        r27 = 1;
        r0 = r19;
        r1 = r27;
        r0.setVideo(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r25;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r27 = r0.append(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r14 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r27;
        r0 = r0.fileFormatThm;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r28;
        r0 = r0.fileFormatJpg;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r27;
        r1 = r28;
        r23 = r12.replace(r0, r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r25;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r23;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r24 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r0.setFileUrl(r14);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r25;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r28;
        r0 = r0.fileFormatThm;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r29 = r0;
        r0 = r29;
        r0 = r0.fileFormatRlv;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r29 = r0;
        r0 = r28;
        r1 = r29;
        r28 = r12.replace(r0, r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setThumFileUrl(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r24;
        r0.setThumName(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r0.setDownLoadOriginalPath(r14);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.thumPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = "/";
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r23;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setThumLocalFilePath(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.orginPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = "/";
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r27 = r0.append(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setFileLocalPath(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.orginPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r32;
        r1 = r27;
        r27 = r0.isExits(r1, r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setDownLoadOriginalFile(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.thumPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r28;
        r0 = r0.fileFormatThm;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r29 = r0;
        r0 = r29;
        r0 = r0.fileFormatJpg;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r29 = r0;
        r0 = r28;
        r1 = r29;
        r28 = r12.replace(r0, r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r30 = java.lang.Long.parseLong(r13);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r1 = r27;
        r2 = r28;
        r3 = r30;
        r27 = r0.isExits(r1, r2, r3);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setDownLoadThum(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
    L_0x027a:
        r0 = r32;
        r0 = r0.listData;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r27;
        r1 = r19;
        r0.add(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        goto L_0x007e;
    L_0x0289:
        r11 = move-exception;
        r6 = r7;
    L_0x028b:
        r11.printStackTrace();	 Catch:{ all -> 0x03ca }
        if (r6 == 0) goto L_0x0293;
    L_0x0290:
        r6.close();	 Catch:{ IOException -> 0x03bf }
    L_0x0293:
        return;
    L_0x0294:
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r27;
        r27 = r0.judgePhotho(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        if (r27 == 0) goto L_0x007e;
    L_0x02a2:
        r27 = 0;
        r0 = r19;
        r1 = r27;
        r0.setVideo(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r25;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r27 = r0.append(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r14 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r23 = r12;
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r25;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r23;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r24 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r0.setFileUrl(r14);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r25;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r28;
        r0 = r0.fileFormatJpg;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r0 = r32;
        r0 = r0.mSuffixUtils;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r29 = r0;
        r0 = r29;
        r0 = r0.fileFormatRlv;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r29 = r0;
        r0 = r28;
        r1 = r29;
        r28 = r12.replace(r0, r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setThumFileUrl(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r24;
        r0.setThumName(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r0.setDownLoadOriginalPath(r14);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.thumPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = "/";
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r1 = r23;
        r27 = r0.append(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setThumLocalFilePath(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27.<init>();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.orginPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = r0;
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r28 = "/";
        r27 = r27.append(r28);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r27;
        r27 = r0.append(r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r27.toString();	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setFileLocalPath(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.orginPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r0 = r32;
        r1 = r27;
        r27 = r0.isExits(r1, r12);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setDownLoadOriginalFile(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r0 = r0.thumPath;	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r27 = r0;
        r28 = java.lang.Long.parseLong(r13);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r32;
        r1 = r27;
        r2 = r28;
        r27 = r0.isExits(r1, r12, r2);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        r0 = r19;
        r1 = r27;
        r0.setDownLoadThum(r1);	 Catch:{ Exception -> 0x0289, all -> 0x03aa }
        goto L_0x027a;
    L_0x03aa:
        r27 = move-exception;
        r6 = r7;
    L_0x03ac:
        if (r6 == 0) goto L_0x03b1;
    L_0x03ae:
        r6.close();	 Catch:{ IOException -> 0x03c5 }
    L_0x03b1:
        throw r27;
    L_0x03b2:
        if (r7 == 0) goto L_0x0293;
    L_0x03b4:
        r7.close();	 Catch:{ IOException -> 0x03b9 }
        goto L_0x0293;
    L_0x03b9:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x0293;
    L_0x03bf:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x0293;
    L_0x03c5:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x03b1;
    L_0x03ca:
        r27 = move-exception;
        goto L_0x03ac;
    L_0x03cc:
        r11 = move-exception;
        goto L_0x028b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.app.x8s.ui.album.x8s.X8MediaFileLoad.parseOnlineData():void");
    }

    public boolean isExits(String path, String name) {
        if (new File(path, name).exists()) {
            return true;
        }
        return false;
    }

    public boolean isExits(String path, String name, long fileSize) {
        File file = new File(path, name);
        if (!file.exists() || file.length() <= 0) {
            return false;
        }
        return true;
    }

    private void closeWriteStream() {
        if (this.randomAccessFile != null) {
            try {
                this.randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
