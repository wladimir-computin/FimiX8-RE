package com.fimi.album.x9;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.fimi.album.download.FileLoader;
import com.fimi.album.download.entity.FileInfo;
import com.fimi.album.download.interfaces.IMediaFileLoad;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaFileInfo;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.x9.interfaces.OnX9MediaFileListener;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.Dom4jManager;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class X9MediaFileLoad<T extends MediaModel> implements IMediaFileLoad {
    String imagePath = DirectoryPath.getX9LocalMedia();
    private FileInfo info;
    private List<MediaModel> listData;
    private OnX9MediaFileListener listener;
    private Dom4jManager mDom4jManager;
    private FileLoader mFileLoader;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X9MediaFileLoad.this.listener.onComplete(false);
                    return;
                case 1:
                    X9MediaFileLoad.this.listener.onComplete(true);
                    return;
                default:
                    return;
            }
        }
    };
    String thumPath = (Environment.getExternalStorageDirectory().getPath() + "/MiDroneMini/cache");
    String urlPrex = "http://192.168.40.1:8000/FIMI_PHOTO/";
    String urlThumPrex = "http://192.168.40.1:8000/FIMI_PHOTO/.thumbnails/";
    String xmlPath = (Environment.getExternalStorageDirectory().getPath() + "/MiDroneMini");
    String xmlUrl = "http://192.168.40.1:8000/FIMI_PHOTO/MediaFileList.xml";

    public X9MediaFileLoad(OnX9MediaFileListener listener, List<MediaModel> listData) {
        this.listener = listener;
        this.listData = listData;
        this.mFileLoader = new FileLoader();
        this.mDom4jManager = new Dom4jManager();
        this.info = new FileInfo();
    }

    public void startLoad() {
        this.info.setPath(this.xmlPath);
        this.info.setUrl(this.xmlUrl);
        this.info.setFileName("mini.mfl");
        this.mFileLoader.queueDownload(this.info, new OnDownloadListener() {
            public void onProgress(Object responseObj, long progrss, long currentLength) {
            }

            public void onSuccess(Object responseObj) {
                X9MediaFileLoad.this.parseXml(X9MediaFileLoad.this.info);
                X9MediaFileLoad.this.mHandler.obtainMessage(1).sendToTarget();
            }

            public void onFailure(Object reasonObj) {
                X9MediaFileLoad.this.mHandler.obtainMessage(0).sendToTarget();
            }

            public void onStop(MediaModel reasonObj) {
            }
        });
    }

    public void parseXml(FileInfo info) {
        List<MediaFileInfo> list = this.mDom4jManager.readXML(info.getPath() + "/" + info.getFileName(), "file", MediaFileInfo.class);
        File fileDir = new File(this.imagePath);
        File thumDir = new File(this.thumPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        if (!thumDir.exists()) {
            thumDir.mkdirs();
        }
        for (int i = 0; i < list.size(); i++) {
            MediaFileInfo mf = (MediaFileInfo) list.get(i);
            MediaModel m = new MediaModel();
            m.setName(mf.getName());
            m.setFileSize(Long.parseLong(mf.getSize()));
            m.setMd5(mf.getMd5());
            m.setLocalFileDir(this.imagePath);
            m.setLocalThumFileDir(this.thumPath);
            String url;
            String thum;
            if (mf.getAttr().equals("pic")) {
                url = this.urlPrex + mf.getName();
                thum = this.urlThumPrex + mf.getName();
                m.setFileUrl(url);
                m.setThumFileUrl(thum);
                m.setThumName(mf.getName());
                m.setVideo(false);
            } else if (mf.getAttr().equals("video")) {
                m.setVideo(true);
                url = this.urlPrex + mf.getName();
                String rename = mf.getName().replace(Constants.VIDEO_FILE_SUFFIX, ".jpg");
                thum = this.urlThumPrex + rename;
                m.setThumName(rename);
                m.setFileUrl(url);
                m.setThumFileUrl(thum);
                String time = mf.getTime();
                String[] durations = mf.getTime().split(":");
                if (durations.length == 3 && durations[0].equals("00")) {
                    time = time.substring(3);
                }
                m.setVideoDuration(time);
                m.setVideo(true);
            }
            m.setFileLocalPath(this.imagePath + "/" + m.getName());
            m.setThumLocalFilePath(this.thumPath + "/" + m.getThumName());
            if (mf.getGentime() != null) {
                try {
                    m.setFormatDate(changeDateFormat(mf.getGentime()));
                    m.setCreateDate(stringToLong(mf.getGentime()));
                } catch (ParseException e) {
                    m.setFormatDate(mf.getGentime());
                    m.setCreateDate(0);
                    e.printStackTrace();
                }
                m.setThumSize(Long.parseLong(mf.getThumbsize()));
                m.setDownLoadOriginalFile(isExits(m.getLocalFileDir(), m.getName()));
                m.setDownLoadThum(isExits(m.getLocalThumFileDir(), m.getThumName()));
                this.listData.add(m);
            }
        }
    }

    public String changeDateFormat(String time) throws ParseException {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").parse(time));
    }

    public static long stringToLong(String strTime) throws ParseException {
        Date date = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").parse(strTime);
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }

    public void stopLoad() {
        this.info.setStop(true);
    }

    public List<MediaModel> getConfigList() {
        return this.listData;
    }

    public boolean isExits(String path, String name) {
        if (new File(path, name).exists()) {
            return true;
        }
        return false;
    }
}
