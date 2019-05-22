package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fimi.album.widget.DownloadStateView;
import com.fimi.album.widget.MediaStrokeTextView;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.SizeTool;

public class BodyRecycleViewHolder extends ViewHolder {
    public ImageView ivSelect;
    public DownloadStateView mDownloadStateView;
    public TextView mFileSize;
    public ImageView mIvDownloadMask;
    public ImageView mIvDownloaded;
    public ImageView mIvSelectMask;
    public ImageView mIvVideoFlag;
    public TextView mTvDownloadState;
    public SimpleDraweeView sdvImageView;
    public MediaStrokeTextView tvDuringdate;

    public BodyRecycleViewHolder(View itemView) {
        super(itemView);
        this.sdvImageView = (SimpleDraweeView) itemView.findViewById(R.id.simpledraweeview);
        this.ivSelect = (ImageView) itemView.findViewById(R.id.selected_iv);
        this.tvDuringdate = (MediaStrokeTextView) itemView.findViewById(R.id.duringdate_tv);
        this.tvDuringdate.getPaint().setFakeBoldText(true);
        this.mIvDownloadMask = (ImageView) itemView.findViewById(R.id.iv_download_mask);
        this.mIvSelectMask = (ImageView) itemView.findViewById(R.id.iv_select_mask);
        this.mIvVideoFlag = (ImageView) itemView.findViewById(R.id.iv_video_flag);
        this.mFileSize = (TextView) itemView.findViewById(R.id.tv_filesize);
        this.mFileSize.getPaint().setFakeBoldText(true);
        this.mIvDownloaded = (ImageView) itemView.findViewById(R.id.iv_downloaded);
        this.mDownloadStateView = (DownloadStateView) itemView.findViewById(R.id.download_state_view);
        this.mTvDownloadState = (TextView) itemView.findViewById(R.id.tv_download_state);
    }

    private void initSimpleDraweeViewParams(Context context, View view) {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int screenWidth = mDisplayMetrics.widthPixels;
        int scteenHeight = mDisplayMetrics.heightPixels;
        if (screenWidth < scteenHeight) {
            screenWidth = scteenHeight;
        }
        layoutParams.width = ((screenWidth - (SizeTool.pixToDp(2.5f, context) * 3)) - (SizeTool.pixToDp(8.0f, context) * 2)) / 4;
        layoutParams.height = (layoutParams.width * 9) / 16;
        view.setLayoutParams(layoutParams);
    }

    private void initImageViewParams(Context context, View parentView, View view) {
        parentView.measure(0, 0);
        int currentHeight = parentView.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.addRule(11);
        layoutParams.rightMargin = SizeTool.pixToDp(12.0f, context);
        layoutParams.topMargin = currentHeight - SizeTool.pixToDp(25.0f, context);
        view.setLayoutParams(layoutParams);
    }

    private void initTextViewParams(Context context, View parentView, View view) {
        parentView.measure(0, 0);
        int currentHeight = parentView.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = SizeTool.pixToDp(10.0f, context);
        layoutParams.topMargin = currentHeight - SizeTool.pixToDp(24.0f, context);
        view.setLayoutParams(layoutParams);
    }
}
