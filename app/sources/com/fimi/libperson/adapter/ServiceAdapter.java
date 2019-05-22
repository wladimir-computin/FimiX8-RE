package com.fimi.libperson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.libperson.R;
import java.util.List;

public class ServiceAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater = null;
    private List<ServiceItem> list = null;

    private class ViewHolder {
        ImageView mIvArrow;
        TextView mTvItemTitle;

        private ViewHolder() {
        }

        public View initView(ViewGroup parent) {
            View view = LayoutInflater.from(ServiceAdapter.this.context).inflate(R.layout.adapt_language_setting, parent, false);
            this.mTvItemTitle = (TextView) view.findViewById(R.id.tv_item_title);
            this.mIvArrow = (ImageView) view.findViewById(R.id.iv_arrow);
            FontUtil.changeFontLanTing(ServiceAdapter.this.context.getAssets(), this.mTvItemTitle);
            return view;
        }
    }

    public ServiceAdapter(List<ServiceItem> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.list.size();
    }

    public ServiceItem getItem(int position) {
        return (ServiceItem) this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = holder.initView(parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ServiceItem itemBean = (ServiceItem) this.list.get(position);
        holder.mTvItemTitle.setText(this.context.getString(itemBean.getInfo()));
        if (itemBean.isSelect()) {
            holder.mIvArrow.setVisibility(0);
            holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.login_forget_password_frequently));
        } else {
            holder.mIvArrow.setVisibility(4);
            holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.login_font_select));
        }
        return convertView;
    }
}
