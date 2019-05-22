package com.fimi.app.x8s.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import java.util.Map;

public class CameraArrayParamsViewHolder extends ViewHolder {
    private TextView paramView;

    public CameraArrayParamsViewHolder(View itemView) {
        super(itemView);
        this.paramView = (TextView) itemView.findViewById(R.id.item_value);
    }

    public void initView(String params, Map<String, String> paramMap) {
        if (paramMap == null || paramMap.get(params) == null) {
            this.paramView.setText(params);
        } else {
            this.paramView.setText((CharSequence) paramMap.get(params));
        }
    }

    public void upSelected(boolean selected) {
        this.paramView.setSelected(selected);
    }
}
