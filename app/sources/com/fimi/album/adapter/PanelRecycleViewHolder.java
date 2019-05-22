package com.fimi.album.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.album.R;
import com.fimi.kernel.utils.FontUtil;

public class PanelRecycleViewHolder extends ViewHolder {
    public ImageView ivIconSelect;
    public RelativeLayout rlRightSelect;
    public TextView tvAllSelect;
    public TextView tvTitleDescription;

    public PanelRecycleViewHolder(View itemView) {
        super(itemView);
        this.tvTitleDescription = (TextView) itemView.findViewById(R.id.title_description);
        this.rlRightSelect = (RelativeLayout) itemView.findViewById(R.id.right_select);
        this.tvAllSelect = (TextView) itemView.findViewById(R.id.all_select);
        this.ivIconSelect = (ImageView) itemView.findViewById(R.id.icon_select);
        FontUtil.changeFontLanTing(itemView.getContext().getAssets(), this.tvTitleDescription, this.tvAllSelect);
    }
}
