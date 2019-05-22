package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.VersionEntity;
import java.util.ArrayList;

public class FirmwareUpgradeAdapter extends Adapter<ViewHolder> {
    private final int VIEW_TYPE_END = 1;
    private final int VIEW_TYPE_NORMAL = 0;
    private Context context;
    private ArrayList<VersionEntity> list;
    private OnUpdateItemClickListener listener;

    static class FmEndViewHolder extends ViewHolder {
        TextView tvStatementLine1;
        TextView tvStatementLine2;
        TextView tvStatementLine3;

        public FmEndViewHolder(View itemView) {
            super(itemView);
            this.tvStatementLine1 = (TextView) itemView.findViewById(R.id.tv_statement_line1);
            this.tvStatementLine2 = (TextView) itemView.findViewById(R.id.tv_statement_line2);
            this.tvStatementLine3 = (TextView) itemView.findViewById(R.id.tv_statement_line3);
        }
    }

    static class FmNormalViewHolder extends ViewHolder {
        TextView tvUpdate;
        TextView tvVersionCode;
        TextView tvVersionName;

        public FmNormalViewHolder(View itemView) {
            super(itemView);
            this.tvVersionName = (TextView) itemView.findViewById(R.id.tv_version_name);
            this.tvVersionCode = (TextView) itemView.findViewById(R.id.tv_version_code);
            this.tvUpdate = (TextView) itemView.findViewById(R.id.tv_update);
        }
    }

    public interface OnUpdateItemClickListener {
        void onUpdateItemClick(int i);
    }

    public FirmwareUpgradeAdapter(ArrayList<VersionEntity> list) {
        this.list = list;
    }

    public void changeDatas(ArrayList<VersionEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(this.context).inflate(viewType == 0 ? R.layout.x8_main_general_fw_upgrade_item_normal : R.layout.x8_main_general_fw_upgrade_item_end, parent, false);
        if (viewType == 0) {
            return new FmNormalViewHolder(view);
        }
        return new FmEndViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FmNormalViewHolder) {
            int i;
            VersionEntity item = (VersionEntity) this.list.get(position);
            FmNormalViewHolder holder1 = (FmNormalViewHolder) holder;
            if (item.getVersionName() != null) {
                holder1.tvVersionName.setText(item.getVersionName());
            }
            if (item.getVersionCode() != null) {
                holder1.tvVersionCode.setText(item.getVersionCode());
            }
            holder1.tvUpdate.setVisibility(item.hasNewVersion() ? 0 : 8);
            TextView textView = holder1.tvVersionName;
            Resources resources = this.context.getResources();
            if (item.hasNewVersion()) {
                i = R.color.x8_fc_all_setting_blue;
            } else {
                i = R.color.white_100;
            }
            textView.setTextColor(resources.getColor(i));
        }
    }

    public int getItemCount() {
        return this.list.size() + 1;
    }

    public int getItemViewType(int position) {
        return position == this.list.size() ? 1 : 0;
    }

    public void setOnUpdateItemClickListener(OnUpdateItemClickListener listener) {
        this.listener = listener;
    }
}
