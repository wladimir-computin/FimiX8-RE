package com.fimi.app.x8s.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class CameraParamsTitleViewHolder extends ViewHolder {
    private ImageView backBtn;
    private TextView paramView;

    public CameraParamsTitleViewHolder(View itemView) {
        super(itemView);
        this.paramView = (TextView) itemView.findViewById(R.id.params_key);
        this.backBtn = (ImageView) itemView.findViewById(R.id.item_back_btn);
    }

    public void initView(String params) {
        this.paramView.setText(params);
    }

    public void upSelected(boolean selected) {
        this.paramView.setSelected(selected);
    }
}
