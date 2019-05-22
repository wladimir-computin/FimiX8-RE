package com.fimi.app.x8s.ui.presenter;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.fimi.album.adapter.MediaDetailViewPaperAdapter;
import com.fimi.album.adapter.MediaDetialViewHolder;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.FrescoControllerListener;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.OnDownloadUiListener;
import com.fimi.album.iview.IViewpaper;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.ui.album.x8s.FmMediaPlayer;
import com.fimi.app.x8s.ui.album.x8s.X8FimiPlayerActivity;
import com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity;
import com.fimi.app.x8s.ui.album.x8s.X8MediaFileDownloadManager;
import com.fimi.app.x8s.widget.videoview.X8CustomVideoView;
import com.fimi.app.x8s.widget.videoview.X8FmMediaInfo;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.utils.FileTool;
import com.fimi.kernel.utils.FrescoUtils;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.x8sdk.controller.CameraManager;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.relex.photodraweeview.OnPhotoTapListener;

public class X8MediaDetailPresenter<T extends X8MediaDetailActivity> implements IViewpaper, OnPhotoTapListener {
    CameraManager cameraManager = new CameraManager();
    private int defaultDisplayHeight;
    private int defaultDisplayWidth;
    private ViewGroup mCacheContainer;
    private int mCurrentPosition;
    private DataManager<MediaModel> mDataManager = DataManager.obtain();
    private X8MediaDetailActivity mMediaActivity;
    private OnDownloadUiListener mOnOriginalDownloadUiListener = new OnDownloadUiListener() {
        public void onProgress(MediaModel model, int progrss) {
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                Log.i("moweiru", "progrssxxxxxxxxxxxxxxxxxxxx==" + progrss);
                model.setProgress(progrss);
                if (X8MediaDetailPresenter.this.mMediaActivity.topBarShowing()) {
                    X8MediaDetailPresenter.this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                    X8MediaDetailPresenter.this.mMediaActivity.getRlDownloadBottom().setVisibility(0);
                } else {
                    X8MediaDetailPresenter.this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                    X8MediaDetailPresenter.this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                }
                X8MediaDetailPresenter.this.mMediaActivity.getBtnStart().setText(R.string.media_detail_cancle);
                X8MediaDetailPresenter.this.mMediaActivity.getTvPercent().setText(model.getProgress() + "%");
                X8MediaDetailPresenter.this.mMediaActivity.getMediaDownloadProgressView().setCurrentCount((float) model.getProgress());
            }
        }

        public void onSuccess(MediaModel model) {
            LogUtil.i("download", "onSuccess: name:" + model.getName());
            Intent intent = new Intent();
            intent.setAction(X8LocalFragmentPresenter.UPDATELOCALITEMRECEIVER);
            intent.putExtra(X8LocalFragmentPresenter.UPDATELOCALITEM, model.clone());
            LocalBroadcastManager.getInstance(X8MediaDetailPresenter.this.mMediaActivity).sendBroadcast(intent);
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                LogUtil.i("download", "onSuccess2: ");
                model.setProgress(0);
                X8MediaDetailPresenter.this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                X8MediaDetailPresenter.this.mMediaActivity.getRlSelectBottom().setVisibility(0);
                X8MediaDetailPresenter.this.mMediaActivity.getRlDownload().setVisibility(8);
                X8MediaDetailPresenter.this.initItemData(X8MediaDetailPresenter.this.viewHolder, model, true);
            }
        }

        public void onFailure(MediaModel model) {
            LogUtil.i("download", "onFailure: name:" + model.getName());
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile333:" + X8MediaDetailPresenter.this.mediaModel.toString());
                X8MediaDetailPresenter.this.mMediaActivity.getBtnStart().setText(R.string.media_detail_start);
            }
        }

        public void onStop(MediaModel model) {
            LogUtil.i("download", "onStop: name:" + model.getName());
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile444:" + X8MediaDetailPresenter.this.mediaModel.toString());
                X8MediaDetailPresenter.this.mMediaActivity.getBtnStart().setText(R.string.media_detail_start);
            }
        }
    };
    private MediaModel mediaModel;
    private CopyOnWriteArrayList<? extends MediaModel> modelList;
    protected String perfix = "file://";
    private IPersonalDataCallBack personalDataCallBack = new IPersonalDataCallBack() {
        public void onPersonalDataCallBack(int groupId, int cmdId, ILinkMessage packet) {
            LogUtil.i("media", "onPersonalDataCallBack: ");
        }

        public void onPersonalSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
            LogUtil.i("media", "onPersonalSendTimeOut: ");
        }
    };
    private MediaDetialViewHolder viewHolder;
    private ViewPager viewPaper;
    private WeakReference<T> weakReference;

    public X8MediaDetailPresenter(T activity, ViewPager viewPaper) {
        this.weakReference = new WeakReference(activity);
        this.mMediaActivity = (X8MediaDetailActivity) this.weakReference.get();
        this.defaultDisplayWidth = this.mMediaActivity.getResources().getDisplayMetrics().widthPixels;
        this.defaultDisplayHeight = this.mMediaActivity.getResources().getDisplayMetrics().heightPixels;
        this.viewPaper = viewPaper;
        initData();
        X8MediaFileDownloadManager.getInstance().setUiDownloadListener(this.mOnOriginalDownloadUiListener);
    }

    private void initData() {
        if (X9HandleType.isCameraView()) {
            this.modelList = this.mDataManager.getX9CameraDataNoHeadList();
        } else {
            this.modelList = this.mDataManager.getLocalDataNoHeadList();
        }
    }

    public Object instantiateItem(ViewGroup container, int position) {
        MediaModel mediaModel = (MediaModel) this.modelList.get(position);
        View view = LayoutInflater.from(this.mMediaActivity.getApplicationContext()).inflate(R.layout.album_adapter_detail_item, container, false);
        this.viewHolder = new MediaDetialViewHolder(view);
        container.setTag(this.viewHolder);
        this.mCacheContainer = container;
        initItemData(this.viewHolder, mediaModel, false);
        container.addView(view);
        return view;
    }

    public void updateItem(int position) {
        this.mediaModel = (MediaModel) this.modelList.get(position);
        this.mCurrentPosition = position;
        MediaModel mediaModel = (MediaModel) this.modelList.get(position);
        HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile11111:" + mediaModel.toString());
        this.mMediaActivity.getPhotoText().setText(mediaModel.getName());
        if (X9HandleType.isCameraView()) {
            if (mediaModel.isDownLoadOriginalFile()) {
                this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                this.mMediaActivity.getRlDownload().setVisibility(8);
                if (this.mMediaActivity.topBarShowing()) {
                    this.mMediaActivity.getRlSelectBottom().setVisibility(0);
                } else {
                    this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                }
            } else if (this.mMediaActivity.topBarShowing()) {
                this.mMediaActivity.getRlDownload().setVisibility(0);
                this.mMediaActivity.getTvFileName().setText(mediaModel.getName());
                if (mediaModel.isDownloading()) {
                    this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                    this.mMediaActivity.getRlDownloadBottom().setVisibility(0);
                    this.mMediaActivity.getTvPercent().setText(mediaModel.getProgress() + "%");
                    this.mMediaActivity.getBtnStart().setText(R.string.media_detail_cancle);
                } else {
                    this.mMediaActivity.getRlSelectBottom().setVisibility(0);
                    this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                }
            } else {
                this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
            }
            initDownload(mediaModel);
        } else {
            this.mMediaActivity.getRlDownload().setVisibility(8);
            this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
            if (this.mMediaActivity.topBarShowing()) {
                this.mMediaActivity.getRlSelectBottom().setVisibility(0);
            } else {
                this.mMediaActivity.getRlSelectBottom().setVisibility(8);
            }
        }
        if (mediaModel.isVideo()) {
            this.mMediaActivity.getBtnPlayMax().setVisibility(0);
        } else {
            this.mMediaActivity.getBtnPlayMax().setVisibility(8);
        }
    }

    public void showTopBottom(int currentPosition) {
        MediaModel mediaModel = (MediaModel) this.modelList.get(currentPosition);
        if (mediaModel.isVideo()) {
            X8CustomVideoView customVideoView = (X8CustomVideoView) this.mCacheContainer.getTag(R.id.iv_top_bar + currentPosition);
            if (customVideoView != null) {
                if (this.mMediaActivity.topBarShowing()) {
                    customVideoView.showBar(false);
                } else {
                    customVideoView.showBar(false);
                }
                customVideoView.setTotalTime(mediaModel.getVideoDuration());
            }
        }
    }

    private void initDownload(MediaModel model) {
        this.mMediaActivity.getMediaDownloadProgressView().setCurrentCount((float) model.getProgress());
        if (model.isStop() && !model.isDownloading()) {
            HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile222:" + this.mediaModel.toString());
            this.mMediaActivity.getBtnStart().setText(R.string.media_detail_start);
        }
    }

    private void initItemData(final MediaDetialViewHolder mMediaDetialViewHolder, final MediaModel mediaModel, final boolean isReload) {
        String filePath = mediaModel.getFileLocalPath();
        String fileUrl = "";
        if (new File(filePath).exists() && !mediaModel.isVideo()) {
            fileUrl = this.perfix + filePath;
        } else if (mediaModel.isVideo()) {
            fileUrl = this.perfix + mediaModel.getThumLocalFilePath();
        } else {
            fileUrl = this.perfix + mediaModel.getThumLocalFilePath();
        }
        mMediaDetialViewHolder.mProgressBar.setVisibility(0);
        mMediaDetialViewHolder.mPhotoDraweeView.setOnPhotoTapListener(this);
        if (isReload) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(Uri.parse(fileUrl));
            imagePipeline.evictFromDiskCache(Uri.parse(fileUrl));
            imagePipeline.evictFromCache(Uri.parse(fileUrl));
        }
        FrescoUtils.displayPhoto(mMediaDetialViewHolder.mPhotoDraweeView, fileUrl, this.defaultDisplayWidth, this.defaultDisplayHeight, new FrescoControllerListener() {
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                mMediaDetialViewHolder.mProgressBar.setVisibility(8);
                HostLogBack.getInstance().writeLog("Alanqiu  ===========initItemData onFailure:" + isReload + "mediaModel:" + mediaModel.toString());
            }

            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                HostLogBack.getInstance().writeLog("Alanqiu  ===========initItemData:" + isReload + "mediaModel:" + mediaModel.toString());
                mMediaDetialViewHolder.mProgressBar.setVisibility(8);
                mMediaDetialViewHolder.mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
    }

    public void deleteItem(int position) {
        if (X9HandleType.isCameraView()) {
            this.cameraManager.deleteOnlineFile(((MediaModel) this.modelList.get(position)).getFileUrl(), new JsonUiCallBackListener() {
                public void onComplete(JSONObject rt, Object o) {
                    if (X8MediaDetailPresenter.this.mCurrentPosition < X8MediaDetailPresenter.this.modelList.size()) {
                        MediaModel mediaModel = (MediaModel) X8MediaDetailPresenter.this.modelList.get(X8MediaDetailPresenter.this.mCurrentPosition);
                        ((MediaDetailViewPaperAdapter) X8MediaDetailPresenter.this.viewPaper.getAdapter()).deleteItem(X8MediaDetailPresenter.this.mCurrentPosition);
                        X8MediaDetailPresenter.this.notifyMediaBroardcast(mediaModel);
                        if (X8MediaDetailPresenter.this.modelList.size() == 0) {
                            X8MediaDetailPresenter.this.mMediaActivity.finish();
                            X8MediaDetailPresenter.this.setOnDestory();
                            return;
                        } else if (X8MediaDetailPresenter.this.mCurrentPosition < X8MediaDetailPresenter.this.modelList.size()) {
                            X8MediaDetailPresenter.this.updateItem(X8MediaDetailPresenter.this.mCurrentPosition);
                            return;
                        } else {
                            X8MediaDetailPresenter.this.updateItem(X8MediaDetailPresenter.this.mCurrentPosition - 1);
                            return;
                        }
                    }
                    X8MediaDetailPresenter.this.mMediaActivity.finish();
                    X8MediaDetailPresenter.this.setOnDestory();
                }
            });
        } else if (position < this.modelList.size()) {
            MediaModel mediaModel = (MediaModel) this.modelList.get(position);
            ((MediaDetailViewPaperAdapter) this.viewPaper.getAdapter()).deleteItem(position);
            FileTool.deleteFile(mediaModel.getFileLocalPath());
            notifyMediaBroardcast(mediaModel);
            notifyCameraBroardcast(mediaModel);
            if (this.modelList.size() == 0) {
                this.mMediaActivity.finish();
                setOnDestory();
            } else if (this.mCurrentPosition < this.modelList.size()) {
                updateItem(this.mCurrentPosition);
            } else {
                updateItem(this.mCurrentPosition - 1);
            }
        } else {
            this.mMediaActivity.finish();
            setOnDestory();
        }
    }

    private void notifyMediaBroardcast(MediaModel model) {
        Intent deleteIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AlbumConstant.DELETEITEM, model);
        deleteIntent.putExtras(bundle);
        deleteIntent.setAction(AlbumConstant.DELETEITEMACTION);
        LocalBroadcastManager.getInstance(this.mMediaActivity.getApplicationContext()).sendBroadcast(deleteIntent);
    }

    public void notifyCameraBroardcast(MediaModel model) {
        Intent intent = new Intent();
        List<MediaModel> list = new ArrayList();
        list.add(model);
        intent.setAction(X8CameraFragmentPrensenter.LOCALFILEDELETEEIVER);
        intent.putExtra(X8CameraFragmentPrensenter.LOCLAFILEDELETEITEM, (Serializable) list);
        LocalBroadcastManager.getInstance(this.mMediaActivity).sendBroadcast(intent);
    }

    public void downloadFile(int position) {
        if (this.modelList.size() > 0) {
            MediaModel mMediaModel = (MediaModel) this.modelList.get(position);
            if (!mMediaModel.isDownloading() && !mMediaModel.isStop()) {
                X8MediaFileDownloadManager.getInstance().startDownload(mMediaModel);
            } else if (mMediaModel.isDownloading()) {
                mMediaModel.setStop(true);
                mMediaModel.setDownloading(false);
                mMediaModel.stopTask();
            } else if (mMediaModel.isStop() || mMediaModel.isDownloadFail()) {
                this.mMediaActivity.getBtnStart().setText(R.string.media_detail_cancle);
                X8MediaFileDownloadManager.getInstance().startDownload(mMediaModel);
            }
            updateItem(position);
        }
    }

    public void onPhotoTap(View view, float x, float y) {
        if (this.mMediaActivity.getIvTopBar().isShown()) {
            this.mMediaActivity.showTopBar(false);
            return;
        }
        this.mMediaActivity.showTopBar(true);
        updateItem(this.mCurrentPosition);
    }

    public void updateFileName(int currentSelectPosition) {
        if (currentSelectPosition < this.modelList.size()) {
            this.mMediaActivity.getPhotoText().setText(((MediaModel) this.modelList.get(currentSelectPosition)).getName());
        }
    }

    private boolean isCurrentModel(MediaModel model) {
        LogUtil.i("zhej", "isCurrentModel: modelNoList:" + this.modelList.size() + ",position:" + this.mCurrentPosition);
        if (this.mCurrentPosition >= this.modelList.size()) {
            return false;
        }
        return ((MediaModel) this.modelList.get(this.mCurrentPosition)).getFileUrl().equals(model.getFileUrl());
    }

    public void setOnDestory() {
        if (this.mOnOriginalDownloadUiListener != null) {
            this.mOnOriginalDownloadUiListener = null;
        }
    }

    public void showShare() {
    }

    public void hideFragment() {
    }

    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }

    public String getMediaFileName() {
        if (this.modelList == null || this.modelList.size() <= 0) {
            return null;
        }
        return ((MediaModel) this.modelList.get(this.mCurrentPosition)).getName();
    }

    public boolean isDownloadFinish() {
        return this.mediaModel.isDownLoadOriginalFile();
    }

    public void startActivity() {
        Log.i("moweiru", "startActivity==");
        if (this.modelList != null) {
            X8FmMediaInfo info = new X8FmMediaInfo();
            info.setName(((MediaModel) this.modelList.get(this.mCurrentPosition)).getName());
            info.setPath(this.perfix + ((MediaModel) this.modelList.get(this.mCurrentPosition)).getFileLocalPath());
            info.setDuration(((MediaModel) this.modelList.get(this.mCurrentPosition)).getVideoDuration());
            Intent intent = new Intent(this.mMediaActivity, X8FimiPlayerActivity.class);
            intent.putExtra(FmMediaPlayer.FM_MEDIA_INFO, info);
            this.mMediaActivity.startActivity(intent);
        }
    }
}
