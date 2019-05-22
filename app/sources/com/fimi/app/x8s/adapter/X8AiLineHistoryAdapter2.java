package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8AiLineHistoryListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8EditorCustomDialog;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.kernel.utils.DateUtil;
import com.umeng.commonsdk.proguard.g;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class X8AiLineHistoryAdapter2 extends Adapter<HistoryViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<X8AiLinePointInfo> list;
    private IX8AiLineHistoryListener mX8AiLineSelectListener;
    String p = null;
    private int type;

    public class HistoryViewHolder extends ViewHolder {
        private ImageView mImgSaveFlag;
        public TextView mTvItemTitle1;
        public TextView mTvItemTitle2;
        public TextView mTvItemTitle3;
        public TextView mTvItemTitle4;
        public TextView mTvItemTitle5;
        public TextView mTvItemTitle6;
        public View rlRootView;
        private View rlSaveFlag;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
            this.mTvItemTitle1 = (TextView) itemView.findViewById(R.id.tvItme1);
            this.mTvItemTitle2 = (TextView) itemView.findViewById(R.id.tvItme2);
            this.mTvItemTitle3 = (TextView) itemView.findViewById(R.id.tvItme3);
            this.mTvItemTitle4 = (TextView) itemView.findViewById(R.id.tvItme4);
            this.mTvItemTitle5 = (TextView) itemView.findViewById(R.id.tvItme5);
            this.mTvItemTitle6 = (TextView) itemView.findViewById(R.id.tvItme6);
            this.mImgSaveFlag = (ImageView) itemView.findViewById(R.id.img_save_flag);
            this.rlSaveFlag = itemView.findViewById(R.id.rlSaveFlag);
        }
    }

    public X8AiLineHistoryAdapter2(Context context, List<X8AiLinePointInfo> list, int type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.type = type;
        this.p = context.getString(R.string.x8_ai_fly_line_history_time_pattern);
    }

    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(this.inflater.inflate(R.layout.x8_ai_line_history_item_layout, parent, false));
    }

    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        X8AiLinePointInfo info = (X8AiLinePointInfo) this.list.get(position);
        holder.mTvItemTitle1.setText("" + (position + 1));
        holder.mTvItemTitle2.setText(info.getLocality());
        holder.mTvItemTitle3.setText(DateUtil.getStringByFormat3(info.getTime(), this.p));
        holder.mTvItemTitle4.setText("" + getDistanceString(info.getDistance()));
        holder.mTvItemTitle5.setText("" + secToTime((int) (info.getDistance() / (((float) info.getSpeed()) / 10.0f))));
        holder.mTvItemTitle6.setText(info.getName());
        if (info.getSaveFlag() == 1) {
            if (this.type == 0) {
                holder.mImgSaveFlag.setBackgroundResource(R.drawable.x8_btn_save_flag_selector);
            } else {
                holder.mImgSaveFlag.setBackgroundResource(R.drawable.x8_btn_cancle_save_flag_selector);
            }
        } else if (info.getSaveFlag() == 0) {
            holder.mImgSaveFlag.setBackgroundResource(R.drawable.x8_btn_unsave_flag_selector);
        }
        holder.rlSaveFlag.setTag(info);
        holder.rlSaveFlag.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8AiLinePointInfo info1 = (X8AiLinePointInfo) view.getTag();
                if (X8AiLineHistoryAdapter2.this.type == 0) {
                    if (info1.getSaveFlag() == 1) {
                        info1.setSaveFlag(0);
                        X8AiLinePointInfoHelper.getIntance().updatelineSaveFlag(0, info1.getId().longValue());
                        if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                            X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position)).getId().longValue(), 0, position);
                        }
                        X8AiLineHistoryAdapter2.this.notifyItemChanged(position);
                    } else if (info1.getSaveFlag() != 0) {
                    } else {
                        if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.favoritesCapacity() == 20) {
                            X8AiLineHistoryAdapter2.this.showMaxSaveDialog();
                            return;
                        }
                        info1.setSaveFlag(1);
                        X8AiLinePointInfoHelper.getIntance().updatelineSaveFlag(1, info1.getId().longValue());
                        if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                            X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position)).getId().longValue(), 1, position);
                            X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.addLineItem((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position));
                        }
                        X8AiLineHistoryAdapter2.this.notifyItemChanged(position);
                        if (info1.getName() != null && info1.getName().equals("")) {
                            X8AiLineHistoryAdapter2.this.showEditorDialog(position, info1);
                        }
                    }
                } else if (X8AiLineHistoryAdapter2.this.type == 1 && info1.getSaveFlag() == 1) {
                    X8AiLineHistoryAdapter2.this.showCancleDialog(info1, position);
                }
            }
        });
        holder.rlRootView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                    X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onSelectId(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position)).getId().longValue(), X8AiLineHistoryAdapter2.this.type);
                }
            }
        });
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void addData(X8AiLinePointInfo info) {
        this.list.add(0, info);
        Collections.sort(this.list, new Comparator<X8AiLinePointInfo>() {
            public int compare(X8AiLinePointInfo a, X8AiLinePointInfo b) {
                return (int) (b.getId().longValue() - a.getId().longValue());
            }
        });
        notifyDataSetChanged();
    }

    public void removeData(X8AiLinePointInfo info, int position) {
        this.list.remove(info);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.list.size() - position);
    }

    public void setOnX8AiLineSelectListener(IX8AiLineHistoryListener listener) {
        this.mX8AiLineSelectListener = listener;
    }

    public void showCancleDialog(final X8AiLinePointInfo info, final int index) {
        new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_ai_line_cancle_save_title), this.context.getString(R.string.x8_ai_line_cancle_save_tip), this.context.getString(R.string.x8_ai_line_save_cancle1), this.context.getString(R.string.x8_ai_line_save_ok1), new onDialogButtonClickListener() {
            public void onLeft() {
            }

            public void onRight() {
                info.setSaveFlag(0);
                X8AiLinePointInfoHelper.getIntance().updatelineSaveFlag(0, info.getId().longValue());
                if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                    X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(index)).getId().longValue(), 0, index);
                }
                X8AiLineHistoryAdapter2.this.list.remove(info);
                X8AiLineHistoryAdapter2.this.notifyItemRemoved(index);
                X8AiLineHistoryAdapter2.this.notifyItemRangeChanged(index, X8AiLineHistoryAdapter2.this.list.size() - index);
            }
        }).show();
    }

    public void showMaxSaveDialog() {
        new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_ai_line_max_save_title), this.context.getString(R.string.x8_ai_line_max_save_tip), this.context.getString(R.string.x8_ai_line_save_cancle), this.context.getString(R.string.x8_ai_line_save_ok), new onDialogButtonClickListener() {
            public void onLeft() {
            }

            public void onRight() {
                X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.goFavorites();
            }
        }).show();
    }

    public String getDistanceString(float distance) {
        return X8NumberUtil.getDistanceNumberString((float) Math.round(distance), 0, false);
    }

    public static String secToTime(int time) {
        if (time <= 0) {
            return "00:00";
        }
        String timeStr;
        int minute = time / 60;
        if (minute == 0) {
            timeStr = time + g.ap;
        } else if (minute < 60) {
            timeStr = minute + "min " + (time % 60) + g.ap;
        } else {
            int hour = minute / 60;
            if (hour > 99) {
                return "99:59:59";
            }
            minute %= 60;
            timeStr = hour + "h " + minute + "min " + ((time - (hour * 3600)) - (minute * 60)) + g.ap;
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        if (i < 0 || i >= 10) {
            return "" + i;
        }
        return "0" + Integer.toString(i);
    }

    public void showEditorDialog(final int positon, final X8AiLinePointInfo info1) {
        new X8EditorCustomDialog(this.context, this.context.getString(R.string.x8_ai_line_editor), new X8EditorCustomDialog.onDialogButtonClickListener() {
            public void onLeft() {
            }

            public void onRight(String text) {
                if (!text.equals("")) {
                    info1.setName(text);
                    X8AiLinePointInfoHelper.getIntance().updateLineName(text, info1.getId().longValue());
                    if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                        X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(positon)).getId().longValue(), 1, positon);
                    }
                    X8AiLineHistoryAdapter2.this.notifyItemChanged(positon);
                }
            }
        }).show();
    }
}
