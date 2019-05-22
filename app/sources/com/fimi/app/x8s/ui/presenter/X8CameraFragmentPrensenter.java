package com.fimi.app.x8s.ui.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.RecyclerListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.FrescoControllerListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.interfaces.OnDownloadUiListener;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.widget.DownloadStateView.State;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.BodyRecycleViewHolder;
import com.fimi.app.x8s.adapter.HeadRecyleViewHolder;
import com.fimi.app.x8s.adapter.PanelRecycleViewHolder;
import com.fimi.app.x8s.adapter.X8sPanelRecycleAdapter;
import com.fimi.app.x8s.ui.album.x8s.X8BaseMediaFragmentPresenter;
import com.fimi.app.x8s.ui.album.x8s.X8MediaFileDownloadManager;
import com.fimi.app.x8s.ui.album.x8s.X8MediaThumDownloadManager;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.kernel.utils.FrescoUtils;
import com.fimi.kernel.utils.LogUtil;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class X8CameraFragmentPrensenter<T extends MediaModel> extends X8BaseMediaFragmentPresenter<T> implements Callback {
    public static final String LOCALFILEDELETEEIVER = "LOCALFILEDELETEEIVER";
    public static final String LOCLAFILEDELETEITEM = "LOCLAFILEDELETEITEM";
    private static final String TAG = "X8CameraFragmentPrensenter";
    private int defaultBound = 2;
    private Handler durationHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private LocalFileDeleteReceiver mLocalFileDeleteReceiver;
    private X8MediaThumDownloadManager mMediaDownloadManager;
    private OnDownloadUiListener mOnDownloadUiListener = new OnDownloadUiListener() {
        public void onProgress(MediaModel model, int progrss) {
        }

        public void onSuccess(MediaModel responseObj) {
            Message updateMessage = new Message();
            updateMessage.what = 2;
            updateMessage.arg1 = X8CameraFragmentPrensenter.this.modelList.indexOf(responseObj);
            X8CameraFragmentPrensenter.this.mainHandler.sendMessage(updateMessage);
            HostLogBack.getInstance().writeLog("Alanqiu  ==================mOnDownloadUiListener:" + X8CameraFragmentPrensenter.this.modelList.indexOf(responseObj));
        }

        public void onFailure(MediaModel reasonObj) {
        }

        public void onStop(MediaModel reasonObj) {
        }
    };
    private OnDownloadUiListener mOnOriginalDownloadUiListener = new OnDownloadUiListener() {
        public void onProgress(MediaModel model, int progrss) {
            model.setProgress(progrss);
            if (X8CameraFragmentPrensenter.this.isResh) {
                X8CameraFragmentPrensenter.this.mPanelRecycleAdapter.notifyItemChanged(model.getItemPosition(), Integer.valueOf(0));
            }
        }

        public void onSuccess(MediaModel model) {
            LogUtil.i("download", "onSuccess1: name:" + model.getName());
            model.setProgress(0);
            X8CameraFragmentPrensenter.this.mPanelRecycleAdapter.notifyItemChanged(model.getItemPosition());
            Intent intent = new Intent();
            intent.setAction(X8LocalFragmentPresenter.UPDATELOCALITEMRECEIVER);
            intent.putExtra(X8LocalFragmentPresenter.UPDATELOCALITEM, model.clone());
            LocalBroadcastManager.getInstance(X8CameraFragmentPrensenter.this.context).sendBroadcast(intent);
            X8CameraFragmentPrensenter.this.sendBroadcastMediaScannerScanFile(model.getFileLocalPath());
        }

        public void onFailure(MediaModel model) {
            LogUtil.i("download", "onFailure1: name:" + model.getName());
            X8CameraFragmentPrensenter.this.mPanelRecycleAdapter.notifyItemChanged(model.getItemPosition());
        }

        public void onStop(MediaModel model) {
            LogUtil.i("download", "onStop1: name:" + model.getName());
            X8CameraFragmentPrensenter.this.mPanelRecycleAdapter.notifyItemChanged(model.getItemPosition());
        }
    };
    private Handler mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);

    public class LocalFileDeleteReceiver extends BroadcastReceiver {
        public void onReceive(Context context, final Intent intent) {
            if (X8CameraFragmentPrensenter.this.modelList != null && intent.getAction().equals(X8CameraFragmentPrensenter.LOCALFILEDELETEEIVER)) {
                X8CameraFragmentPrensenter.this.durationHandler.post(new Runnable() {
                    public void run() {
                        for (T select : (List) intent.getSerializableExtra(X8CameraFragmentPrensenter.LOCLAFILEDELETEITEM)) {
                            String currentDate = select.getFormatDate();
                            Iterator it = X8CameraFragmentPrensenter.this.modelList.iterator();
                            while (it.hasNext()) {
                                MediaModel model = (MediaModel) it.next();
                                if (!model.isCategory() && !model.isHeadView() && model.getName().equals(select.getName()) && model.getFileLocalPath().equals(select.getFileLocalPath())) {
                                    model.setDownLoadOriginalFile(false);
                                    X8CameraFragmentPrensenter.this.mainHandler.post(new Runnable() {
                                        public void run() {
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public X8CameraFragmentPrensenter(RecyclerView mRecyclerView, X8sPanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context) {
        super(mRecyclerView, mPanelRecycleAdapter, mISelectData, context, true);
        doTrans();
        LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            this.mGridLayoutManager = (GridLayoutManager) manager;
        }
        this.mMediaDownloadManager = X8MediaThumDownloadManager.getInstance();
        this.mMediaDownloadManager.setOnDownloadUiListener(this.mOnDownloadUiListener);
        registerReciver();
    }

    private void doTrans() {
        this.mRecyclerView.setRecyclerListener(new RecyclerListener() {
            public void onViewRecycled(ViewHolder holder) {
                if (holder instanceof BodyRecycleViewHolder) {
                    BodyRecycleViewHolder mBodyRecycleViewHolder = (BodyRecycleViewHolder) holder;
                    mBodyRecycleViewHolder.tvDuringdate.setVisibility(8);
                    mBodyRecycleViewHolder.ivSelect.setVisibility(8);
                }
            }
        });
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                X8CameraFragmentPrensenter.this.isScrollRecycle = false;
                X8CameraFragmentPrensenter.this.durationHandler.sendEmptyMessage(1);
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) <= X8CameraFragmentPrensenter.this.defaultBound) {
                    X8CameraFragmentPrensenter.this.isScrollRecycle = false;
                    X8CameraFragmentPrensenter.this.durationHandler.sendEmptyMessage(1);
                    return;
                }
                X8CameraFragmentPrensenter.this.isScrollRecycle = true;
                X8CameraFragmentPrensenter.this.durationHandler.sendEmptyMessage(1);
            }
        });
    }

    public boolean handleMessage(Message message) {
        try {
            if (message.what == 1) {
                if (this.mGridLayoutManager != null) {
                    int firstVisibleItem = this.mGridLayoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItem = this.mGridLayoutManager.findLastVisibleItemPosition();
                    if (firstVisibleItem != -1) {
                        if (this.isScrollRecycle) {
                            this.mMediaDownloadManager.stopDownload();
                            this.durationHandler.removeMessages(1);
                        } else {
                            while (firstVisibleItem <= lastVisibleItem) {
                                MediaModel mediaModel = getModel(firstVisibleItem);
                                if (!(mediaModel == null || mediaModel.isCategory() || mediaModel.isDownLoadThum() || mediaModel.getFileUrl() == null)) {
                                    this.mMediaDownloadManager.addData(mediaModel);
                                }
                                firstVisibleItem++;
                            }
                            if (this.mMediaDownloadManager.getCount() > 0 && !this.mMediaDownloadManager.isDownload) {
                                this.mMediaDownloadManager.startDownload();
                            }
                        }
                    }
                }
            } else if (message.what == 2) {
                this.mPanelRecycleAdapter.notifyItemChanged(message.arg1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position >= 0) {
            if (holder instanceof HeadRecyleViewHolder) {
                doHeadTrans((HeadRecyleViewHolder) holder, position);
            } else if (holder instanceof BodyRecycleViewHolder) {
                doBodyTrans((BodyRecycleViewHolder) holder, position);
            } else {
                doPanelTrans((PanelRecycleViewHolder) holder, position);
            }
        }
    }

    private void doHeadTrans(HeadRecyleViewHolder headRecyleViewHolder, int position) {
        headRecyleViewHolder.mTvHeard.setText(this.context.getString(R.string.x8_album_head_title, new Object[]{DataManager.obtain().getX8CameraVideoCount() + "", DataManager.obtain().getX8CameraPhotoCount() + ""}));
    }

    private void doPanelTrans(final PanelRecycleViewHolder holder, final int position) {
        final MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            holder.tvTitleDescription.setText(getModel(position).getFormatDate());
            if (mediaModel.isSelect()) {
                holder.mBtnAllSelect.setImageResource(R.drawable.x8_ablum_select);
            } else {
                holder.mBtnAllSelect.setImageResource(R.drawable.x8_ablum_unselect);
            }
        }
        holder.mBtnAllSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8CameraFragmentPrensenter.this.onItemCategoryClick(holder, position, mediaModel);
            }
        });
        if (this.isEnterSelectMode) {
            holder.mBtnAllSelect.setVisibility(0);
        } else {
            holder.mBtnAllSelect.setVisibility(8);
        }
    }

    private void onItemCategoryClick(PanelRecycleViewHolder holder, int position, MediaModel mediaModel) {
        if (mediaModel != null) {
            CopyOnWriteArrayList<MediaModel> internalList = (CopyOnWriteArrayList) this.stateHashMap.get(mediaModel.getFormatDate());
            if (mediaModel.isSelect()) {
                perfomSelectCategory(internalList, false);
            } else {
                perfomSelectCategory(internalList, true);
            }
        }
    }

    private void perfomSelectCategory(CopyOnWriteArrayList<MediaModel> internalList, boolean isSelect) {
        Iterator it = internalList.iterator();
        while (it.hasNext()) {
            MediaModel mMediaModel = (MediaModel) it.next();
            if (isSelect) {
                if (!mMediaModel.isSelect()) {
                    mMediaModel.setSelect(true);
                    addSelectModel(mMediaModel);
                }
            } else if (mMediaModel.isSelect()) {
                mMediaModel.setSelect(false);
                removeSelectModel(mMediaModel);
            }
        }
        notifyAllVisible();
        callBackSelectSize(this.selectList.size());
        if (this.selectList.size() == (this.modelList.size() - this.stateHashMap.size()) - 1) {
            callAllSelectMode(true);
        } else {
            callAllSelectMode(false);
        }
    }

    private void doBodyTrans(final BodyRecycleViewHolder holder, final int position) {
        final MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            mediaModel.setItemPosition(position);
            final String currentFilePath = mediaModel.getThumLocalFilePath();
            if (!TextUtils.isEmpty(currentFilePath)) {
                if (mediaModel.isVideo()) {
                    holder.sdvImageView.setBackgroundResource(R.drawable.album_video_loading);
                } else {
                    holder.sdvImageView.setBackgroundResource(R.drawable.album_photo_loading);
                }
                if (mediaModel.isDownLoadThum() && !mediaModel.isDownloading()) {
                    holder.sdvImageView.setTag(currentFilePath);
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + currentFilePath, this.defaultPhtotWidth, this.defaultPhtotHeight, new FrescoControllerListener() {
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);
                            ImagePipeline imagePipeline = Fresco.getImagePipeline();
                            imagePipeline.evictFromMemoryCache(Uri.parse(X8CameraFragmentPrensenter.this.perfix + currentFilePath));
                            imagePipeline.evictFromDiskCache(Uri.parse(X8CameraFragmentPrensenter.this.perfix + currentFilePath));
                            imagePipeline.evictFromCache(Uri.parse(X8CameraFragmentPrensenter.this.perfix + currentFilePath));
                            FrescoUtils.displayPhoto(holder.sdvImageView, X8CameraFragmentPrensenter.this.perfix + currentFilePath, X8CameraFragmentPrensenter.this.defaultPhtotWidth, X8CameraFragmentPrensenter.this.defaultPhtotHeight);
                        }

                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                        }
                    });
                } else if (!(currentFilePath.equals(holder.sdvImageView.getTag()) || mediaModel.isDownloading())) {
                    LogUtil.i(TAG, "doBodyTrans3: ");
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + currentFilePath, this.defaultPhtotWidth, this.defaultPhtotHeight);
                }
                if (mediaModel.isVideo()) {
                    holder.mIvVideoFlag.setImageResource(R.drawable.x8_ablumn_normal_vedio_mark);
                    holder.tvDuringdate.setTag(currentFilePath);
                    if (!TextUtils.isEmpty(mediaModel.getVideoDuration())) {
                        holder.tvDuringdate.setVisibility(0);
                        holder.tvDuringdate.setText(mediaModel.getVideoDuration());
                    }
                } else {
                    holder.mIvVideoFlag.setImageResource(R.drawable.x8_ablumn_normal_photo_mark);
                    holder.tvDuringdate.setVisibility(4);
                }
                if (this.isEnterSelectMode) {
                    if (mediaModel.isSelect()) {
                        changeSelectViewState(mediaModel, holder, 0);
                    } else {
                        changeSelectViewState(mediaModel, holder, 8);
                    }
                } else if (mediaModel.isSelect()) {
                    changeSelectViewState(mediaModel, holder, 0);
                } else {
                    changeSelectViewState(mediaModel, holder, 8);
                }
                holder.sdvImageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        X8CameraFragmentPrensenter.this.onItemClick(holder, view, position);
                    }
                });
                holder.sdvImageView.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        X8CameraFragmentPrensenter.this.onItemLongClick(holder, view, position);
                        return true;
                    }
                });
                if (this.isCamera) {
                    changeDownloadProgress(holder, mediaModel.getProgress());
                    if (mediaModel.isDownloading()) {
                        changeDownloadState(holder, State.DOWNLOADING);
                        showDownloadImg(holder, true);
                    } else if (mediaModel.isStop()) {
                        changeDownloadState(holder, State.PAUSE);
                        showDownloadImg(holder, true);
                    } else if (mediaModel.isDownloadFail()) {
                        changeDownloadState(holder, State.DOWNLOAD_FAIL);
                        showDownloadImg(holder, true);
                    } else {
                        showDownloadImg(holder, false);
                    }
                    if (mediaModel.isDownLoadOriginalFile()) {
                        holder.mIvDownloaded.setVisibility(0);
                    } else {
                        holder.mIvDownloaded.setVisibility(8);
                    }
                    if (mediaModel.getFileSize() > 0) {
                        holder.mFileSize.setText(ByteUtil.getNetFileSizeDescription(mediaModel.getFileSize()));
                    } else {
                        holder.mFileSize.setVisibility(8);
                    }
                    holder.mDownloadStateView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (mediaModel.isStop() || mediaModel.isDownloadFail()) {
                                X8MediaFileDownloadManager.getInstance().startDownload(mediaModel);
                            } else {
                                mediaModel.setDownloading(false);
                                mediaModel.setStop(true);
                                mediaModel.stopTask();
                            }
                            X8CameraFragmentPrensenter.this.mPanelRecycleAdapter.notifyItemChanged(mediaModel.getItemPosition());
                        }
                    });
                }
            }
        }
    }

    private void onItemLongClick(BodyRecycleViewHolder holder, View view, int position) {
        if (!this.isEnterSelectMode) {
            this.isEnterSelectMode = true;
            callBackEnterSelectMode();
        }
        preformMode(getModel(position), holder);
        callBackSelectSize(this.selectList.size());
    }

    public void onItemClick(BodyRecycleViewHolder holder, View view, int position) {
        MediaModel currentMode = getModel(position);
        if (this.isEnterSelectMode) {
            preformMode(currentMode, holder);
            callBackSelectSize(this.selectList.size());
            return;
        }
        goMediaDetailActivity(this.modelList.indexOf(currentMode));
    }

    public void showCategorySelectView(boolean state) {
        int firstVisibleItem = this.mGridLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = this.mGridLayoutManager.findLastVisibleItemPosition();
        if (firstVisibleItem != -1) {
            while (firstVisibleItem <= lastVisibleItem) {
                MediaModel mediaModel = getModel(firstVisibleItem);
                if (mediaModel != null && mediaModel.isCategory()) {
                    this.mPanelRecycleAdapter.notifyItemChanged(firstVisibleItem);
                }
                firstVisibleItem++;
            }
        }
    }

    public void registerReciver() {
        this.mLocalFileDeleteReceiver = new LocalFileDeleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LOCALFILEDELETEEIVER);
        LocalBroadcastManager.getInstance(this.context).registerReceiver(this.mLocalFileDeleteReceiver, intentFilter);
    }

    public void registerDownloadListerner() {
        X8MediaFileDownloadManager.getInstance().setUiDownloadListener(this.mOnOriginalDownloadUiListener);
        if (this.mPanelRecycleAdapter != null) {
            this.mPanelRecycleAdapter.notifyDataSetChanged();
        }
    }

    public void unRegisterReciver() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(this.mLocalFileDeleteReceiver);
    }
}
