package com.fimi.libdownfw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fimi.host.Entity.DownloadFwSelectInfo;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.libdownfw.R;
import com.fimi.network.entity.UpfirewareDto;
import java.util.ArrayList;
import java.util.List;

public class DownloadFwSelectAdapter extends BaseAdapter {
    private Context context;
    private List<DownloadFwSelectInfo> list;
    private SelectListener mSelectListener;
    private List<UpfirewareDto> mUpfirewareDtoList = new ArrayList();

    public interface SelectListener {
        void onSelect(boolean z);
    }

    private class ViewHandle {
        View cbSelect;
        TextView tvInfo;
        TextView tvTitle;
        View vLinOne;

        private ViewHandle() {
        }
    }

    public DownloadFwSelectAdapter(Context context, List<DownloadFwSelectInfo> list) {
        this.list = list;
        this.context = context;
        if (list != null && list.size() > 0) {
            this.mUpfirewareDtoList = ((DownloadFwSelectInfo) list.get(0)).getDtoList();
        }
    }

    public void setSelectListener(SelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }

    public int getCount() {
        return this.mUpfirewareDtoList.size();
    }

    public Object getItem(int position) {
        return this.mUpfirewareDtoList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHandle viewHandle;
        if (convertView == null) {
            viewHandle = new ViewHandle();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.list_download_fm_select_info_item, null);
            viewHandle.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHandle.tvInfo = (TextView) convertView.findViewById(R.id.tv_info);
            viewHandle.cbSelect = convertView.findViewById(R.id.cb_select);
            viewHandle.vLinOne = convertView.findViewById(R.id.v_lin_one);
            convertView.setTag(viewHandle);
        } else {
            viewHandle = (ViewHandle) convertView.getTag();
        }
        viewHandle.tvTitle.setText(((UpfirewareDto) this.mUpfirewareDtoList.get(position)).getSysName());
        viewHandle.tvInfo.setText(((UpfirewareDto) this.mUpfirewareDtoList.get(position)).getUpdateContent());
        FontUtil.changeFontLanTing(this.context.getAssets(), viewHandle.tvTitle, viewHandle.tvInfo);
        updateSelected();
        return convertView;
    }

    public void update(int position) {
        if (((DownloadFwSelectInfo) this.list.get(position)).isSelect()) {
            ((DownloadFwSelectInfo) this.list.get(position)).setSelect(false);
        } else {
            ((DownloadFwSelectInfo) this.list.get(position)).setSelect(true);
        }
        for (DownloadFwSelectInfo downloadFwSelectInfo : this.list) {
        }
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateSelected();
    }

    private void updateSelected() {
        if (this.mSelectListener != null) {
            boolean ret = false;
            for (DownloadFwSelectInfo info : this.list) {
                if (info.isSelect()) {
                    ret = true;
                    break;
                }
            }
            this.mSelectListener.onSelect(ret);
        }
    }

    public boolean hasForceSign() {
        for (DownloadFwSelectInfo info : this.list) {
            if (info.isForceSign()) {
                return true;
            }
        }
        return false;
    }
}
