package com.fimi.album.presenter;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.album.R;
import com.facebook.imagepipeline.image.ImageInfo;
import com.fimi.album.adapter.MediaDetailViewPaperAdapter;
import com.fimi.album.adapter.MediaDetialViewHolder;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.FrescoControllerListener;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IViewpaper;
import com.fimi.album.ui.MediaDetailActivity;
import com.fimi.album.ui.VideoPlayActivity;
import com.fimi.kernel.utils.FileTool;
import com.fimi.kernel.utils.FrescoUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;
import me.relex.photodraweeview.OnPhotoTapListener;

public class MediaDetailPresenter<T extends MediaDetailActivity> implements IViewpaper, OnPhotoTapListener {
    private ViewGroup cacheContainer;
    private int defaultDisplayHeight;
    private int defaultDisplayWidth;
    private DataManager<MediaModel> mDataManager = DataManager.obtain();
    private MediaDetailActivity mMediaActivity;
    private CopyOnWriteArrayList<? extends MediaModel> modelList;
    protected String perfix = "file://";
    private ViewPager viewPaper;
    private WeakReference<T> weakReference;

    public MediaDetailPresenter(T activity, ViewPager viewPaper) {
        this.weakReference = new WeakReference(activity);
        this.mMediaActivity = (MediaDetailActivity) this.weakReference.get();
        this.defaultDisplayWidth = this.mMediaActivity.getResources().getDisplayMetrics().widthPixels;
        this.defaultDisplayHeight = this.mMediaActivity.getResources().getDisplayMetrics().heightPixels;
        this.viewPaper = viewPaper;
        initData();
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
        MediaDetialViewHolder viewHolder = new MediaDetialViewHolder(view);
        container.setTag(viewHolder);
        this.cacheContainer = container;
        initItemData(viewHolder, position, mediaModel);
        container.addView(view);
        return view;
    }

    public void updateItem(int position) {
        this.mMediaActivity.getPhotoText().setText(((MediaModel) this.modelList.get(position)).getName());
    }

    private void initItemData(final MediaDetialViewHolder mMediaDetialViewHolder, int position, MediaModel mediaModel) {
        this.mMediaActivity.getPhotoText().setText(mediaModel.getName());
        String filePath = mediaModel.getFileLocalPath();
        String fileUrl = "";
        if (new File(filePath).exists()) {
            fileUrl = this.perfix + filePath;
        } else if (mediaModel.isVideo()) {
            fileUrl = mediaModel.getThumFileUrl();
        } else {
            fileUrl = mediaModel.getFileUrl();
        }
        mMediaDetialViewHolder.mProgressBar.setVisibility(0);
        mMediaDetialViewHolder.mPhotoDraweeView.setOnPhotoTapListener(this);
        FrescoUtils.displayPhoto(mMediaDetialViewHolder.mPhotoDraweeView, fileUrl, this.defaultDisplayWidth, this.defaultDisplayHeight, new FrescoControllerListener() {
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                mMediaDetialViewHolder.mProgressBar.setVisibility(8);
            }

            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                mMediaDetialViewHolder.mProgressBar.setVisibility(8);
                mMediaDetialViewHolder.mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
    }

    private void goVideoPlayActivity(MediaDetialViewHolder mMediaDetialViewHolder, int position) {
        MediaModel mediaModel = (MediaModel) this.modelList.get(position);
        if (mediaModel.isVideo()) {
            Intent intent = new Intent(this.mMediaActivity, VideoPlayActivity.class);
            intent.putExtra(AlbumConstant.VIDEOPLARURL, mediaModel.getFileUrl());
            this.mMediaActivity.startActivity(intent);
        }
    }

    public void deleteItem(int position) {
        if (position < this.modelList.size()) {
            MediaModel mMediaModel = (MediaModel) this.modelList.get(position);
            Intent deleteIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(AlbumConstant.DELETEITEM, mMediaModel);
            deleteIntent.putExtras(bundle);
            deleteIntent.setAction(AlbumConstant.DELETEITEMACTION);
            LocalBroadcastManager.getInstance(this.mMediaActivity.getApplicationContext()).sendBroadcast(deleteIntent);
            ((MediaDetailViewPaperAdapter) this.viewPaper.getAdapter()).deleteItem(position);
            FileTool.deleteFile(mMediaModel.getFileLocalPath());
            if (this.modelList.size() == 0) {
                this.mMediaActivity.finish();
                return;
            }
            return;
        }
        this.mMediaActivity.finish();
    }

    public void onPhotoTap(View view, float x, float y) {
        if (this.mMediaActivity.getLlHeadViewGroup().isShown()) {
            this.mMediaActivity.getLlHeadViewGroup().setVisibility(8);
        } else {
            this.mMediaActivity.getLlHeadViewGroup().setVisibility(0);
        }
        if (this.mMediaActivity.getRlHeadDirection().isShown()) {
            this.mMediaActivity.getRlHeadDirection().setVisibility(8);
        } else {
            this.mMediaActivity.getRlHeadDirection().setVisibility(0);
        }
    }

    public void updateFileName(int currentSelectPosition) {
        if (currentSelectPosition < this.modelList.size()) {
            this.mMediaActivity.getPhotoText().setText(((MediaModel) this.modelList.get(currentSelectPosition)).getName());
        }
    }
}
