package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.X8ErrorCode;
import java.util.List;

public class X8ErrorCodeAdapter extends BaseAdapter {
    private Context context;
    private List<X8ErrorCode> mList;

    public class ViewHolder {
        public ImageView mImgBg;
        public ImageView mIvArrow;
        public TextView mTvItemTitle;

        public View initView(ViewGroup parent) {
            View view = LayoutInflater.from(X8ErrorCodeAdapter.this.context).inflate(R.layout.x8_adapt_error_code, parent, false);
            this.mTvItemTitle = (TextView) view.findViewById(R.id.tv_item_title);
            this.mIvArrow = (ImageView) view.findViewById(R.id.iv_arrow);
            this.mImgBg = (ImageView) view.findViewById(R.id.iv_bg);
            return view;
        }
    }

    public X8ErrorCodeAdapter(Context context, List<X8ErrorCode> mList) {
        this.context = context;
        this.mList = mList;
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return this.mList.get(position);
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
        X8ErrorCode itemBean = (X8ErrorCode) this.mList.get(position);
        holder.mTvItemTitle.setText(itemBean.getTitle());
        switch (itemBean.getLevel()) {
            case serious:
                holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type1));
                holder.mIvArrow.setBackgroundResource(R.drawable.x8_error_code_type1_icon);
                holder.mImgBg.setBackgroundResource(R.drawable.x8_error_code_type1);
                break;
            case medium:
                holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type2));
                holder.mIvArrow.setBackgroundResource(R.drawable.x8_error_code_type2_icon);
                holder.mImgBg.setBackgroundResource(R.drawable.x8_error_code_type2);
                break;
            case slight:
                holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type3));
                holder.mIvArrow.setBackgroundResource(R.drawable.x8_error_code_type3_icon);
                holder.mImgBg.setBackgroundResource(R.drawable.x8_error_code_type3);
                break;
        }
        return convertView;
    }
}
