package com.fimi.app.x8s.ui.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.RecyclerListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.ISelectData;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.BodyRecycleViewHolder;
import com.fimi.app.x8s.adapter.HeadRecyleViewHolder;
import com.fimi.app.x8s.adapter.PanelRecycleViewHolder;
import com.fimi.app.x8s.adapter.X8sPanelRecycleAdapter;
import com.fimi.app.x8s.ui.album.x8s.X8BaseMediaFragmentPresenter;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.kernel.utils.DateFormater;
import com.fimi.kernel.utils.FrescoUtils;
import com.fimi.kernel.utils.VideoDuration;
import java.io.File;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class X8LocalFragmentPresenter<T extends MediaModel> extends X8BaseMediaFragmentPresenter {
    private static final String TAG = "X9LocalFragmentPresente";
    public static final String UPDATELOCALITEM = "UPDATELOCALITEM";
    public static final String UPDATELOCALITEMRECEIVER = "UPDATELOCALITEMRECEIVER";
    private int defaultBound = 50;
    private Handler durationHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private UpdateLocalItemReceiver mUpdateLocalItemReceiver;
    private Handler mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);

    public class UpdateLocalItemReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(X8LocalFragmentPresenter.UPDATELOCALITEMRECEIVER)) {
                MediaModel mediaModel = (MediaModel) intent.getSerializableExtra(X8LocalFragmentPresenter.UPDATELOCALITEM);
                if (mediaModel != null) {
                    X8LocalFragmentPresenter.this.mPanelRecycleAdapter.addNewItem(mediaModel);
                }
            }
        }
    }

    public X8LocalFragmentPresenter(RecyclerView mRecyclerView, X8sPanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context) {
        super(mRecyclerView, mPanelRecycleAdapter, mISelectData, context, false);
        doTrans();
        LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            this.mGridLayoutManager = (GridLayoutManager) manager;
        }
        registerReciver();
    }

    private void doTrans() {
        this.mRecyclerView.setRecyclerListener(new RecyclerListener() {
            public void onViewRecycled(ViewHolder holder) {
                if (holder instanceof BodyRecycleViewHolder) {
                    BodyRecycleViewHolder mBodyRecycleViewHolder = (BodyRecycleViewHolder) holder;
                    mBodyRecycleViewHolder.tvDuringdate.setVisibility(4);
                    mBodyRecycleViewHolder.ivSelect.setVisibility(8);
                }
            }
        });
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                X8LocalFragmentPresenter.this.isScrollRecycle = false;
                X8LocalFragmentPresenter.this.durationHandler.sendEmptyMessage(1);
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) <= X8LocalFragmentPresenter.this.defaultBound) {
                    X8LocalFragmentPresenter.this.isScrollRecycle = false;
                    X8LocalFragmentPresenter.this.durationHandler.sendEmptyMessage(1);
                    return;
                }
                X8LocalFragmentPresenter.this.isScrollRecycle = true;
            }
        });
    }

    public boolean handleMessage(Message message) {
        try {
            if (message.what != 1 || this.modelList.size() <= 0 || this.mGridLayoutManager == null) {
                if (message.what == 2) {
                    this.mPanelRecycleAdapter.notifyItemChanged(message.arg1);
                }
                return true;
            }
            int firstVisibleItem = this.mGridLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItem = this.mGridLayoutManager.findLastVisibleItemPosition();
            if (firstVisibleItem != -1) {
                while (firstVisibleItem <= lastVisibleItem) {
                    MediaModel mediaModel = getModel(firstVisibleItem);
                    if (!(mediaModel == null || mediaModel.isCategory() || !TextUtils.isEmpty(mediaModel.getVideoDuration()))) {
                        mediaModel.setVideoDuration(DateFormater.dateString(VideoDuration.getVideoDuration(this.context, mediaModel.getFileLocalPath()), "mm:ss"));
                        Message updateMessage = new Message();
                        updateMessage.what = 2;
                        updateMessage.arg1 = firstVisibleItem;
                        this.mainHandler.sendMessage(updateMessage);
                    }
                    firstVisibleItem++;
                    if (this.isScrollRecycle) {
                        this.durationHandler.removeMessages(1);
                        break;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof HeadRecyleViewHolder) {
            doHeadTrans((HeadRecyleViewHolder) holder, position);
        } else if (holder instanceof BodyRecycleViewHolder) {
            doBodyTrans((BodyRecycleViewHolder) holder, position);
        } else {
            doPanelTrans((PanelRecycleViewHolder) holder, position);
        }
    }

    private void doHeadTrans(HeadRecyleViewHolder headRecyleViewHolder, int position) {
        headRecyleViewHolder.mTvHeard.setText(this.context.getString(R.string.x8_album_head_title, new Object[]{DataManager.obtain().getLocalVideoCount() + "", DataManager.obtain().getLocalPhotoCount() + ""}));
    }

    private void doPanelTrans(final PanelRecycleViewHolder holder, final int position) {
        final MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            holder.tvTitleDescription.setText(getModel(position).getFormatDate().split(" ")[0]);
            if (mediaModel.isSelect()) {
                holder.mBtnAllSelect.setImageResource(R.drawable.x8_ablum_select);
            } else {
                holder.mBtnAllSelect.setImageResource(R.drawable.x8_ablum_unselect);
                holder.mBtnAllSelect.setSelected(false);
            }
        }
        holder.mBtnAllSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8LocalFragmentPresenter.this.onItemCategoryClick(holder, position, mediaModel);
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
            CopyOnWriteArrayList<MediaModel> internalList = (CopyOnWriteArrayList) this.stateHashMap.get(mediaModel.getFormatDate().split(" ")[0]);
            Log.i("moweiru", "(mediaModel.isSelect():" + mediaModel.isSelect());
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
        MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            mediaModel.setItemPosition(position);
            String photoUrl = null;
            String currentFilePath = mediaModel.getFileLocalPath();
            if (TextUtils.isEmpty(mediaModel.getThumLocalFilePath())) {
                photoUrl = currentFilePath;
            } else if (new File(mediaModel.getThumLocalFilePath()).exists()) {
                photoUrl = mediaModel.getThumLocalFilePath();
            }
            if (!TextUtils.isEmpty(currentFilePath) && !TextUtils.isEmpty(photoUrl)) {
                if (mediaModel.isVideo()) {
                    holder.sdvImageView.setBackgroundResource(R.drawable.album_video_loading);
                } else {
                    holder.sdvImageView.setBackgroundResource(R.drawable.album_photo_loading);
                }
                if (mediaModel.getFileSize() > 0) {
                    holder.mFileSize.setText(ByteUtil.getNetFileSizeDescription(mediaModel.getFileSize()));
                } else {
                    holder.mFileSize.setVisibility(8);
                }
                if (!currentFilePath.equals(holder.sdvImageView.getTag()) && !mediaModel.isLoadThulm()) {
                    holder.sdvImageView.setTag(currentFilePath);
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + photoUrl, this.defaultPhtotWidth, this.defaultPhtotHeight);
                    mediaModel.setLoadThulm(true);
                } else if (!currentFilePath.equals(holder.sdvImageView.getTag())) {
                    holder.sdvImageView.setTag(currentFilePath);
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + photoUrl, this.defaultPhtotWidth, this.defaultPhtotHeight);
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
                        X8LocalFragmentPresenter.this.onItemClick(holder, view, position);
                    }
                });
                holder.sdvImageView.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        X8LocalFragmentPresenter.this.onItemLongClick(holder, view, position);
                        return true;
                    }
                });
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
        this.mUpdateLocalItemReceiver = new UpdateLocalItemReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATELOCALITEMRECEIVER);
        LocalBroadcastManager.getInstance(this.context).registerReceiver(this.mUpdateLocalItemReceiver, intentFilter);
    }

    public void registerDownloadListerner() {
    }

    public void unRegisterReciver() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(this.mUpdateLocalItemReceiver);
    }
}
