package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.PhotoParamItemEntity;
import com.fimi.app.x8s.viewHolder.CameraParamListener;
import com.fimi.app.x8s.viewHolder.PhotoParamsViewHolder;
import java.util.List;

public class PhotoParamsAdapter extends Adapter {
    private Context context;
    private boolean isEnable = false;
    private List<PhotoParamItemEntity> pList;
    private CameraParamListener paramListener;

    public PhotoParamsAdapter(Context context, List<PhotoParamItemEntity> mlist) {
        this.context = context;
        this.pList = mlist;
    }

    public void setParamListener(CameraParamListener paramListener) {
        this.paramListener = paramListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoParamsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.x8_photo_param_list_item, parent, false));
    }

    public void updateData(List<PhotoParamItemEntity> uplist) {
        if (uplist != null) {
            this.pList = uplist;
        }
        notifyDataSetChanged();
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PhotoParamItemEntity itemEntity = (PhotoParamItemEntity) this.pList.get(position);
        if (holder instanceof PhotoParamsViewHolder) {
            ((PhotoParamsViewHolder) holder).initItemData((PhotoParamItemEntity) this.pList.get(position), this.isEnable);
            holder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoParamsAdapter.this.paramListener != null) {
                        PhotoParamsAdapter.this.paramListener.gotoSubItem(itemEntity.getParamKey(), itemEntity.getParamValue(), holder);
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.pList != null ? this.pList.size() : 0;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public void setEnable(boolean enable) {
        this.isEnable = enable;
        notifyDataSetChanged();
    }
}
