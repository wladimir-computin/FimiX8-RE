package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.utils.DateUtil;
import java.util.List;

public class X8AiLineHistoryAdapter extends BaseAdapter implements OnItemClickListener {
    private Context context;
    private int defaultColor;
    private List<X8AiLinePointInfo> mList;
    private int select = -1;
    private int selectColor;

    public class ViewHolder {
        public TextView mTvItemTitle1;
        public TextView mTvItemTitle2;
        public TextView mTvItemTitle3;

        public View initView(ViewGroup parent) {
            View view = LayoutInflater.from(X8AiLineHistoryAdapter.this.context).inflate(R.layout.x8_ai_line_history_item_layout, parent, false);
            this.mTvItemTitle1 = (TextView) view.findViewById(R.id.tvItme1);
            this.mTvItemTitle2 = (TextView) view.findViewById(R.id.tvItme2);
            this.mTvItemTitle3 = (TextView) view.findViewById(R.id.tvItme3);
            return view;
        }
    }

    public X8AiLineHistoryAdapter(Context context, List<X8AiLinePointInfo> mList) {
        this.context = context;
        this.mList = mList;
        this.defaultColor = context.getResources().getColor(R.color.white_90);
        this.selectColor = context.getResources().getColor(R.color.x8_fc_all_setting_blue);
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
        X8AiLinePointInfo info = (X8AiLinePointInfo) this.mList.get(position);
        String[] str = DateUtil.getStringByFormat2(info.getTime(), this.context.getString(R.string.x8_ai_fly_line_history_time_pattern));
        holder.mTvItemTitle1.setText(str[0]);
        holder.mTvItemTitle2.setText(str[1]);
        holder.mTvItemTitle3.setText("" + String.format(this.context.getString(R.string.x8_ai_fly_line_history_distance), new Object[]{"" + info.getDistance()}));
        if (this.select == position) {
            holder.mTvItemTitle1.setTextColor(this.selectColor);
            holder.mTvItemTitle2.setTextColor(this.selectColor);
            holder.mTvItemTitle3.setTextColor(this.selectColor);
        } else {
            holder.mTvItemTitle1.setTextColor(this.defaultColor);
            holder.mTvItemTitle2.setTextColor(this.defaultColor);
            holder.mTvItemTitle3.setTextColor(this.defaultColor);
        }
        return convertView;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.select = position;
        notifyDataSetChanged();
    }

    public X8AiLinePointInfo getItemSelect() {
        if (this.select == -1) {
            return null;
        }
        return (X8AiLinePointInfo) this.mList.get(this.select);
    }
}
