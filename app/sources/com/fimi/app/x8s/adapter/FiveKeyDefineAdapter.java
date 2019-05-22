package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.fimi.app.x8s.R;
import com.fimi.x8sdk.modulestate.StateManager;

public class FiveKeyDefineAdapter extends Adapter<FiveKeyViewHolder> {
    private String[] arr;
    private int colorBlue;
    private int colorWhite;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    private int selectIndex;

    protected static class FiveKeyViewHolder extends ViewHolder {
        Button btn;

        public FiveKeyViewHolder(View itemView) {
            super(itemView);
            this.btn = (Button) itemView.findViewById(R.id.btn_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int i);
    }

    public FiveKeyDefineAdapter(Context context, String[] arr) {
        this.arr = arr;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorWhite = context.getResources().getColor(R.color.white_100);
        this.colorBlue = context.getResources().getColor(R.color.x8_fc_all_setting_blue);
    }

    public FiveKeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FiveKeyViewHolder(this.inflater.inflate(R.layout.x8_main_rc_five_key_define_item, parent, false));
    }

    public void onBindViewHolder(final FiveKeyViewHolder holder, final int position) {
        if (this.arr.length - 1 == position) {
            holder.btn.setVisibility(8);
        } else {
            holder.btn.setVisibility(0);
        }
        holder.btn.setText(this.arr[position]);
        if (position == this.selectIndex && StateManager.getInstance().getX8Drone().isConnect()) {
            holder.btn.setTextColor(this.colorBlue);
            holder.btn.setAlpha(1.0f);
            holder.btn.setEnabled(true);
        } else if (StateManager.getInstance().getX8Drone().isConnect()) {
            holder.btn.setTextColor(this.colorWhite);
            holder.btn.setAlpha(1.0f);
            holder.btn.setEnabled(true);
        } else {
            holder.btn.setTextColor(this.colorWhite);
            holder.btn.setAlpha(0.6f);
            holder.btn.setEnabled(false);
        }
        if (this.listener != null && this.selectIndex != position) {
            holder.btn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!FiveKeyDefineAdapter.this.arr[FiveKeyDefineAdapter.this.selectIndex].equalsIgnoreCase(holder.btn.getText().toString())) {
                        FiveKeyDefineAdapter.this.listener.onItemClicked(position);
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.arr.length;
    }

    public void setItemSelect(int index) {
        if (index >= 0 && index <= this.arr.length - 1) {
            this.selectIndex = index;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
