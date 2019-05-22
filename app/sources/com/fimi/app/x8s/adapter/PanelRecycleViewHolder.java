package com.fimi.app.x8s.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.DensityUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LanguageUtil;

public class PanelRecycleViewHolder extends ViewHolder {
    public ImageView mBtnAllSelect;
    public TextView tvTitleDescription;

    public PanelRecycleViewHolder(View itemView) {
        super(itemView);
        this.tvTitleDescription = (TextView) itemView.findViewById(R.id.title_description);
        this.mBtnAllSelect = (ImageView) itemView.findViewById(R.id.btn_all_select);
        LayoutParams params = (LayoutParams) this.mBtnAllSelect.getLayoutParams();
        if (LanguageUtil.isZh()) {
            params.width = DensityUtil.dip2px(itemView.getContext(), 54.3f);
        } else {
            params.width = DensityUtil.dip2px(itemView.getContext(), 70.6f);
        }
        this.mBtnAllSelect.setLayoutParams(params);
        FontUtil.changeFontLanTing(itemView.getContext().getAssets(), this.tvTitleDescription, this.mBtnAllSelect);
    }
}
