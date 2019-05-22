package com.fimi.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.album.R;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.INodataTip;
import com.fimi.album.iview.IRecycleAdapter;

public class PanelRecycleAdapter<T extends MediaModel> extends BaseRecycleAdapter {
    public static final String TAG = PanelRecycleAdapter.class.getName();
    private IRecycleAdapter mIRecycleAdapter;

    public PanelRecycleAdapter(Context context, INodataTip mINodataTip) {
        super(context, mINodataTip);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 16) {
            return new PanelRecycleViewHolder(LayoutInflater.from(this.context).inflate(R.layout.album_panel_view_holder, parent, false));
        }
        return new BodyRecycleViewHolder(LayoutInflater.from(this.context).inflate(R.layout.album_body_view_holder, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this.mIRecycleAdapter != null) {
            this.mIRecycleAdapter.onBindViewHolder(holder, position);
        }
    }

    public void setmIRecycleAdapter(IRecycleAdapter mIRecycleAdapter) {
        this.mIRecycleAdapter = mIRecycleAdapter;
    }
}
