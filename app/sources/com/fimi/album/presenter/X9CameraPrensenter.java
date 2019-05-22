package com.fimi.album.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
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
import com.example.album.R;
import com.fimi.album.adapter.BodyRecycleViewHolder;
import com.fimi.album.adapter.PanelRecycleAdapter;
import com.fimi.album.adapter.PanelRecycleViewHolder;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.ISelectData;
import com.fimi.kernel.utils.FrescoUtils;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class X9CameraPrensenter<T extends MediaModel> extends BaseFragmentPresenter implements Callback {
    private int defaultBound = 50;
    private Handler durationHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private GridLayoutManager mGridLayoutManager;
    private Handler mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);

    public X9CameraPrensenter(RecyclerView mRecyclerView, PanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context) {
        super(mRecyclerView, mPanelRecycleAdapter, mISelectData, context);
        doTrans();
        LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            this.mGridLayoutManager = (GridLayoutManager) manager;
        }
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
                BaseFragmentPresenter.isScrollRecycle = false;
                X9CameraPrensenter.this.durationHandler.sendEmptyMessage(1);
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) <= X9CameraPrensenter.this.defaultBound) {
                    BaseFragmentPresenter.isScrollRecycle = false;
                    X9CameraPrensenter.this.durationHandler.sendEmptyMessage(1);
                    return;
                }
                BaseFragmentPresenter.isScrollRecycle = true;
            }
        });
    }

    public boolean handleMessage(Message message) {
        return true;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof BodyRecycleViewHolder) {
            doBodyTrans((BodyRecycleViewHolder) holder, position);
        } else {
            doPanelTrans((PanelRecycleViewHolder) holder, position);
        }
    }

    private void doPanelTrans(final PanelRecycleViewHolder holder, final int position) {
        final MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            holder.tvTitleDescription.setText(getModel(position).getFormatDate());
            if (mediaModel.isSelect()) {
                holder.ivIconSelect.setImageResource(R.drawable.album_btn_category_select_press);
                holder.tvAllSelect.setText(R.string.media_select_all_no);
            } else {
                holder.ivIconSelect.setImageResource(R.drawable.album_btn_category_select_normal);
                holder.tvAllSelect.setText(R.string.media_select_all);
            }
        }
        holder.rlRightSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X9CameraPrensenter.this.onItemCategoryClick(holder, position, mediaModel);
            }
        });
    }

    private void onItemCategoryClick(PanelRecycleViewHolder holder, int position, MediaModel mediaModel) {
        if (mediaModel != null) {
            CopyOnWriteArrayList<MediaModel> internalList = (CopyOnWriteArrayList) this.stateHashMap.get(mediaModel.getFormatDate());
            if (this.context.getString(R.string.media_select_all).equals(holder.tvAllSelect.getText())) {
                perfomSelectCategory(internalList, true);
            } else {
                perfomSelectCategory(internalList, false);
            }
        }
    }

    private void perfomSelectCategory(CopyOnWriteArrayList<MediaModel> internalList, boolean isSelect) {
        Iterator it = internalList.iterator();
        while (it.hasNext()) {
            MediaModel mMediaModel = (MediaModel) it.next();
            if (isSelect) {
                mMediaModel.setSelect(true);
                addSelectModel(mMediaModel);
            } else {
                mMediaModel.setSelect(false);
                removeSelectModel(mMediaModel);
            }
        }
        this.mPanelRecycleAdapter.notifyItemRangeChanged(this.mGridLayoutManager.findFirstVisibleItemPosition(), this.mGridLayoutManager.findLastVisibleItemPosition());
    }

    private void doBodyTrans(final BodyRecycleViewHolder holder, final int position) {
        MediaModel mediaModel = getModel(position);
        mediaModel.setItemPosition(position);
        if (mediaModel != null) {
            String currentFilePath = mediaModel.getFileLocalPath();
            if (!TextUtils.isEmpty(currentFilePath)) {
                if (!currentFilePath.equals(holder.sdvImageView.getTag()) && mediaModel.isLoadThulm()) {
                    holder.sdvImageView.setTag(currentFilePath);
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + currentFilePath, this.defaultPhtotWidth, this.defaultPhtotHeight);
                    mediaModel.setLoadThulm(true);
                } else if (!currentFilePath.equals(holder.sdvImageView.getTag())) {
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + currentFilePath, this.defaultPhtotWidth, this.defaultPhtotHeight);
                }
                if (mediaModel.isVideo()) {
                    holder.tvDuringdate.setTag(currentFilePath);
                    if (!TextUtils.isEmpty(mediaModel.getVideoDuration())) {
                        holder.tvDuringdate.setVisibility(0);
                        holder.tvDuringdate.setText(mediaModel.getVideoDuration());
                    }
                } else {
                    holder.tvDuringdate.setVisibility(8);
                }
                if (this.isEnterSelectMode) {
                    if (mediaModel.isSelect()) {
                        changeViewState(holder.ivSelect, 0, R.drawable.album_icon_share_media_active);
                    } else {
                        changeViewState(holder.ivSelect, 0, R.drawable.album_icon_share_media_nomal);
                    }
                } else if (mediaModel.isSelect()) {
                    changeViewState(holder.ivSelect, 0, R.drawable.album_icon_share_media_active);
                } else {
                    changeViewState(holder.ivSelect, 8, R.drawable.album_icon_share_media_nomal);
                }
                holder.sdvImageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        X9CameraPrensenter.this.onItemClick(holder, view, position);
                    }
                });
                holder.sdvImageView.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        X9CameraPrensenter.this.onItemLongClick(holder, view, position);
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
        preformMode(getModel(position), holder.ivSelect, 0, R.drawable.album_icon_share_media_active, R.drawable.album_icon_share_media_nomal);
        callBackSelectSize(this.selectList.size());
    }

    public void onItemClick(BodyRecycleViewHolder holder, View view, int position) {
        MediaModel currentMode = getModel(position);
        if (this.isEnterSelectMode) {
            preformMode(currentMode, holder.ivSelect, 0, R.drawable.album_icon_share_media_active, R.drawable.album_icon_share_media_nomal);
            callBackSelectSize(this.selectList.size());
            return;
        }
        goMediaDetailActivity(this.modelList.indexOf(currentMode));
    }
}
