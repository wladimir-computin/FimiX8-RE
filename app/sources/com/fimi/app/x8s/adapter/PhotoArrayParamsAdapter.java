package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.viewHolder.CameraArrayParamsViewHolder;
import java.util.List;
import java.util.Map;

public class PhotoArrayParamsAdapter extends Adapter {
    private Context context;
    private PhotoArrayItemClickListener itemClickListener;
    private Map<String, String> keyMap;
    private List<String> pList;
    private String paramKey;
    private int select_position;

    public interface PhotoArrayItemClickListener {
        void onItemClickListener(String str, String str2);
    }

    public PhotoArrayParamsAdapter(Context context, List<String> mlist) {
        this.context = context;
        this.pList = mlist;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CameraArrayParamsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.x8_photo_array_param_list_item, parent, false));
    }

    public void updateData(List<String> uplist, Map<String, String> map, String paramKey, int selected_index) {
        if (uplist != null) {
            this.pList = uplist;
        }
        if (map != null) {
            this.keyMap = map;
        }
        if (paramKey != null) {
            this.paramKey = paramKey;
        }
        this.select_position = selected_index;
        notifyDataSetChanged();
    }

    public void setSelect_position(int select_position) {
        this.select_position = select_position;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof CameraArrayParamsViewHolder) {
            ((CameraArrayParamsViewHolder) holder).initView((String) this.pList.get(position), this.keyMap);
            ((CameraArrayParamsViewHolder) holder).upSelected(position == this.select_position);
        }
        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoArrayParamsAdapter.this.itemClickListener != null) {
                    PhotoArrayParamsAdapter.this.itemClickListener.onItemClickListener(PhotoArrayParamsAdapter.this.paramKey, (String) PhotoArrayParamsAdapter.this.pList.get(position));
                }
            }
        });
    }

    public int getItemCount() {
        return this.pList != null ? this.pList.size() : 0;
    }

    public void setItemClickListener(PhotoArrayItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
