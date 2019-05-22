package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.X8AiLinePointEntity;
import java.util.List;

public class X8AiLinePointValueAdapter extends Adapter<X8AiPointValueViewHolder> {
    private LayoutInflater inflater;
    private boolean isAll;
    private List<X8AiLinePointEntity> list;
    private OnItemClickListener listener;
    private int selectIndex = -1;
    private int type;

    public interface OnItemClickListener {
        void onItemClicked(int i, int i2, boolean z);
    }

    protected static class X8AiPointValueViewHolder extends ViewHolder {
        Button btn;
        View root;

        public X8AiPointValueViewHolder(View itemView) {
            super(itemView);
            this.btn = (Button) itemView.findViewById(R.id.btn_item);
            this.root = itemView.findViewById(R.id.btn_item);
        }
    }

    public boolean isAll() {
        return this.isAll;
    }

    public void setAll(boolean all) {
        this.isAll = all;
    }

    public X8AiLinePointValueAdapter(Context context, List<X8AiLinePointEntity> list, int type) {
        this.list = list;
        this.type = type;
        this.inflater = LayoutInflater.from(context);
    }

    public X8AiPointValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new X8AiPointValueViewHolder(this.inflater.inflate(R.layout.x8_ai_line_point_value_item, parent, false));
    }

    public void onBindViewHolder(X8AiPointValueViewHolder holder, int position) {
        if (this.type == 0) {
            onSigleSelect(holder, position);
            setSigleListener(holder);
        } else if (this.type == 1) {
            onMulSelect(holder, position);
        }
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public void setSigleListener(X8AiPointValueViewHolder holder) {
        if (this.listener != null) {
            holder.root.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    boolean isSelect = true;
                    int pos = ((Integer) v.getTag()).intValue();
                    if (pos == X8AiLinePointValueAdapter.this.selectIndex) {
                        isSelect = false;
                    }
                    X8AiLinePointValueAdapter.this.listener.onItemClicked(pos, X8AiLinePointValueAdapter.this.selectIndex, isSelect);
                    if (isSelect) {
                        X8AiLinePointValueAdapter.this.selectIndex = pos;
                    } else {
                        X8AiLinePointValueAdapter.this.selectIndex = -1;
                    }
                }
            });
        }
    }

    public void onSigleSelect(X8AiPointValueViewHolder holder, int position) {
        holder.root.setTag(Integer.valueOf(position));
        holder.btn.setText("" + ((X8AiLinePointEntity) this.list.get(position)).getnPos());
        int state = ((X8AiLinePointEntity) this.list.get(position)).getState();
        if (state == 0) {
            holder.btn.setSelected(false);
        } else if (state == 1) {
            holder.btn.setSelected(true);
        }
    }

    public void onMulSelect(X8AiPointValueViewHolder holder, int position) {
        holder.root.setTag(Integer.valueOf(position));
        holder.btn.setText("" + ((X8AiLinePointEntity) this.list.get(position)).getnPos());
        int state = ((X8AiLinePointEntity) this.list.get(position)).getState();
        if (state == 0) {
            holder.btn.setEnabled(true);
            holder.btn.setSelected(false);
        } else if (state == 1) {
            holder.btn.setEnabled(true);
            holder.btn.setSelected(true);
        } else if (state == 2) {
            holder.btn.setEnabled(false);
            holder.btn.setSelected(false);
        }
        if (state != 2) {
            setMulListener(holder, state);
        }
    }

    public void setMulListener(X8AiPointValueViewHolder holder, final int state) {
        if (this.listener != null) {
            holder.root.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    boolean isSelect = true;
                    int pos = ((Integer) v.getTag()).intValue();
                    if (state == 1) {
                        isSelect = false;
                    }
                    X8AiLinePointValueAdapter.this.listener.onItemClicked(pos, X8AiLinePointValueAdapter.this.selectIndex, isSelect);
                }
            });
        }
    }
}
