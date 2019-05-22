package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.network.entity.UpfirewareDto;
import java.util.List;

public class UpdateContentAdapter extends BaseAdapter {
    List<UpfirewareDto> list;
    Context mContext;

    private class ViewHold {
        TextView tvSyscontent;
        TextView tvSysname;
        TextView tvSysnameFlag;

        private ViewHold() {
        }
    }

    public UpdateContentAdapter(List<UpfirewareDto> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int i) {
        return this.list.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHandle;
        if (view == null) {
            viewHandle = new ViewHold();
            view = LayoutInflater.from(this.mContext).inflate(R.layout.x8_item_update_detail, null);
            viewHandle.tvSysnameFlag = (TextView) view.findViewById(R.id.tv_sysname_flag);
            viewHandle.tvSysname = (TextView) view.findViewById(R.id.tv_sysname);
            viewHandle.tvSyscontent = (TextView) view.findViewById(R.id.tv_sys_content);
            FontUtil.changeFontLanTing(this.mContext.getAssets(), viewHandle.tvSyscontent);
            view.setTag(viewHandle);
        } else {
            viewHandle = (ViewHold) view.getTag();
        }
        viewHandle.tvSysnameFlag.setText(getHanNumber(i));
        viewHandle.tvSysname.setText(((UpfirewareDto) this.list.get(i)).getSysName());
        viewHandle.tvSyscontent.setText(((UpfirewareDto) this.list.get(i)).getUpdateContent());
        return view;
    }

    public void updateList(List<UpfirewareDto> needUpgrade) {
        this.list = needUpgrade;
        notifyDataSetChanged();
    }

    public String getHanNumber(int position) {
        return (position + 1) + ".";
    }
}
