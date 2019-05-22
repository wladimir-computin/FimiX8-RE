package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.viewHolder.CameraEVParamListener;
import com.fimi.app.x8s.viewHolder.CameraParamsViewHolder;
import java.util.List;

public class CameraEVParamsAdatper extends Adapter {
    private Context context;
    private boolean isEnable = false;
    private CameraEVParamListener listener;
    private String paramKey;
    private List<String> plist;
    private int selectIndex = 0;

    public CameraEVParamsAdatper(Context context, List<String> params, CameraEVParamListener paramListener, String typeKey) {
        this.context = context;
        this.plist = params;
        this.paramKey = typeKey;
        this.listener = paramListener;
    }

    public void updateDatas(List<String> params) {
        this.plist = params;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CameraParamsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.x8_iso_recycler_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        if ((holder instanceof CameraParamsViewHolder) && this.plist.size() > 0) {
            ((CameraParamsViewHolder) holder).initView((String) this.plist.get(position), this.isEnable);
            ((CameraParamsViewHolder) holder).upSelected(position == this.selectIndex);
            holder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (CameraEVParamsAdatper.this.isEnable && CameraEVParamsAdatper.this.listener != null) {
                        CameraEVParamsAdatper.this.listener.updateParams(CameraEVParamsAdatper.this.paramKey, (String) CameraEVParamsAdatper.this.plist.get(position));
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.plist != null ? this.plist.size() : 0;
    }

    public void upSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }

    public void setEnable(boolean enable) {
        this.isEnable = enable;
        notifyDataSetChanged();
    }
}
