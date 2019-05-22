package com.fimi.album.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.example.album.R;
import me.relex.photodraweeview.PhotoDraweeView;

public class MediaDetialViewHolder extends ViewHolder {
    public PhotoDraweeView mPhotoDraweeView;
    public ProgressBar mProgressBar;
    public RelativeLayout mRlItem;

    public MediaDetialViewHolder(View itemView) {
        super(itemView);
        this.mRlItem = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        this.mPhotoDraweeView = (PhotoDraweeView) itemView.findViewById(R.id.photo_drawee_view);
        this.mProgressBar = (ProgressBar) itemView.findViewById(R.id.loading);
    }

    public void setRlItemBg(int color) {
        this.mRlItem.setBackgroundResource(color);
    }
}
