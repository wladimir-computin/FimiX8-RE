package com.fimi.app.x8s.viewHolder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class CameraParamsViewHolder extends ViewHolder {
    private Context context;
    private TextView paramView;

    public CameraParamsViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        this.paramView = (TextView) itemView.findViewById(R.id.params_item);
    }

    public void initView(String params, boolean enable) {
        this.paramView.setText(params);
        int[][] states;
        if (enable) {
            this.paramView.setEnabled(true);
            states = new int[3][];
            states[0] = new int[]{16842919};
            states[1] = new int[]{16842913};
            states[2] = new int[]{16842910};
            this.paramView.setTextColor(new ColorStateList(states, new int[]{this.context.getResources().getColor(R.color.x8_value_unselected), this.context.getResources().getColor(R.color.x8_value_select), this.context.getResources().getColor(R.color.x8_value_unselected)}));
            return;
        }
        this.paramView.setEnabled(false);
        states = new int[3][];
        states[0] = new int[]{16842919};
        states[1] = new int[]{16842913};
        states[2] = new int[]{-16842910};
        this.paramView.setTextColor(new ColorStateList(states, new int[]{this.context.getResources().getColor(R.color.x8_value_disable), this.context.getResources().getColor(R.color.x8_value_disable_select), this.context.getResources().getColor(R.color.x8_value_disable)}));
    }

    public void upSelected(boolean selected) {
        this.paramView.setSelected(selected);
    }
}
