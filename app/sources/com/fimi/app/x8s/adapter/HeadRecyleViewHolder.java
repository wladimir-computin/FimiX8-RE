package com.fimi.app.x8s.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class HeadRecyleViewHolder extends ViewHolder {
    public TextView mTvHeard;

    public HeadRecyleViewHolder(View itemView) {
        super(itemView);
        this.mTvHeard = (TextView) itemView.findViewById(R.id.tv_head);
        this.mTvHeard.setText("视频多少");
    }
}
