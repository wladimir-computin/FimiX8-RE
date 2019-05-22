package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.section.X8B2oxSection;
import com.fimi.app.x8s.entity.X8B2oxFile;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.host.HostConstants;
import com.fimi.kernel.fds.FdsManager;
import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.utils.DNSLookupThread;
import com.fimi.libperson.ui.me.login.LoginActivity;
import com.fimi.widget.X8ToastUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class X8B2oxAdapter extends Adapter<ViewHolder> {
    private Context context;
    private X8DoubleCustomDialog loginDialogRestart;
    private Map<String, X8B2oxSection> sections = new LinkedHashMap();

    public class X8B2oxHeaderViewHolder extends ViewHolder {
        public View rlRootView;
        public TextView tvTitle;

        public X8B2oxHeaderViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvItme1);
        }
    }

    public class X8B2oxViewHolder extends ViewHolder {
        private ImageView mImgSaveFlag;
        public TextView mTvItemTitle1;
        public TextView mTvItemTitle2;
        public TextView mTvItemTitle3;
        public View rlRootView;
        private View rlSaveFlag;

        public X8B2oxViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
            this.mTvItemTitle1 = (TextView) itemView.findViewById(R.id.tvItme1);
            this.mTvItemTitle2 = (TextView) itemView.findViewById(R.id.tvItme2);
            this.mTvItemTitle3 = (TextView) itemView.findViewById(R.id.tvItme6);
            this.mImgSaveFlag = (ImageView) itemView.findViewById(R.id.img_save_flag);
            this.rlSaveFlag = itemView.findViewById(R.id.rlSaveFlag);
        }
    }

    public X8B2oxAdapter(Context context) {
        this.context = context;
    }

    public void addSection(String title, X8B2oxSection section) {
        this.sections.put(title, section);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new X8B2oxHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_black_box_header_layout, parent, false));
        }
        if (viewType == 1) {
            return new X8B2oxViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_black_box_item_layout, parent, false));
        }
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        int currentPos = 0;
        for (Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = (X8B2oxSection) entry.getValue();
            int sectionTotal = section.getSectionItemsTotal();
            if (position >= currentPos && position <= (currentPos + sectionTotal) - 1 && section.isHasHeader()) {
                if (position == currentPos) {
                    onHeaderBindViewHolder(holder, section.getTitle());
                } else {
                    onBindContentViewHolder(holder, getPositionInSection(position), position, section);
                }
            }
            currentPos += sectionTotal;
        }
    }

    private void onBindContentViewHolder(ViewHolder holder, final int positionSection, final int position1, X8B2oxSection section) {
        final X8B2oxFile file = (X8B2oxFile) section.getList().get(positionSection);
        ((X8B2oxViewHolder) holder).mTvItemTitle1.setText(file.getNameShow());
        ((X8B2oxViewHolder) holder).mTvItemTitle3.setText(file.getShowLen());
        int resId = R.drawable.x8_img_upload_success;
        switch (file.getState()) {
            case IDLE:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_idel);
                break;
            case WAIT:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_wait);
                break;
            case LOADING:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_runing);
                if (((X8B2oxViewHolder) holder).mImgSaveFlag.getAnimation() == null) {
                    Animation rorateAnimation = AnimationUtils.loadAnimation(this.context, R.anim.rotate_anim);
                    rorateAnimation.setInterpolator(new LinearInterpolator());
                    ((X8B2oxViewHolder) holder).mImgSaveFlag.startAnimation(rorateAnimation);
                    break;
                }
                break;
            case SUCCESS:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_success);
                ((X8B2oxViewHolder) holder).mImgSaveFlag.clearAnimation();
                ((X8B2oxViewHolder) holder).rlSaveFlag.setOnClickListener(null);
                break;
            case FAILED:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_restart);
                ((X8B2oxViewHolder) holder).mImgSaveFlag.clearAnimation();
                break;
            case STOP:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_idel);
                ((X8B2oxViewHolder) holder).mImgSaveFlag.clearAnimation();
                break;
        }
        if (file.getState() == FdsUploadState.IDLE || file.getState() == FdsUploadState.STOP || file.getState() == FdsUploadState.FAILED) {
            ((X8B2oxViewHolder) holder).rlSaveFlag.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!DNSLookupThread.isDSNSuceess()) {
                        X8ToastUtil.showToast(X8B2oxAdapter.this.context, X8B2oxAdapter.this.context.getString(R.string.x8_fds_connect_internet), 0);
                    } else if (HostConstants.getUserDetail().getFimiId() == null || HostConstants.getUserDetail().getFimiId().equals("")) {
                        X8B2oxAdapter.this.loginDialogRestart = new X8DoubleCustomDialog(X8B2oxAdapter.this.context, X8B2oxAdapter.this.context.getString(R.string.x8_modify_black_box_login_title), X8B2oxAdapter.this.context.getString(R.string.x8_modify_black_box_login_content), X8B2oxAdapter.this.context.getString(R.string.x8_modify_black_box_login_go), new onDialogButtonClickListener() {
                            public void onLeft() {
                                X8B2oxAdapter.this.loginDialogRestart.dismiss();
                            }

                            public void onRight() {
                                X8B2oxAdapter.this.context.startActivity(new Intent(X8B2oxAdapter.this.context, LoginActivity.class));
                            }
                        });
                        X8B2oxAdapter.this.loginDialogRestart.show();
                    } else {
                        file.setSectionPostion(positionSection);
                        file.setItemPostion(position1);
                        FdsManager.getInstance().startDownload(file);
                        X8B2oxAdapter.this.notifyItemChanged(position1);
                    }
                }
            });
        }
    }

    public void onHeaderBindViewHolder(ViewHolder holder, String title) {
        ((X8B2oxHeaderViewHolder) holder).tvTitle.setText(title);
    }

    public void uploadAll() {
        int position = 0;
        for (Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = (X8B2oxSection) entry.getValue();
            int listSize = section.getSectionItemsTotal() - 1;
            position++;
            for (int i = 0; i < listSize; i++) {
                X8B2oxFile x8B2oxFile = (X8B2oxFile) section.getList().get(i);
                if (x8B2oxFile.getState() == FdsUploadState.IDLE || x8B2oxFile.getState() == FdsUploadState.STOP || x8B2oxFile.getState() == FdsUploadState.FAILED) {
                    x8B2oxFile.setSectionPostion(i);
                    x8B2oxFile.setItemPostion(position);
                    FdsManager.getInstance().startDownload(x8B2oxFile);
                }
                position++;
            }
        }
        notifyDataSetChanged();
    }

    public int getItemCount() {
        int count = 0;
        for (Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            count += ((X8B2oxSection) entry.getValue()).getSectionItemsTotal();
        }
        return count;
    }

    public int getItemViewType(int position) {
        int currentPos = 0;
        for (Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = (X8B2oxSection) entry.getValue();
            int sectionTotal = section.getSectionItemsTotal();
            if (position < currentPos || position > (currentPos + sectionTotal) - 1 || !section.isHasHeader()) {
                currentPos += sectionTotal;
            } else if (position == currentPos) {
                return 0;
            } else {
                return 1;
            }
        }
        throw new IndexOutOfBoundsException("Invalid position");
    }

    public int getPositionInSection(int position) {
        int currentPos = 0;
        for (Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = (X8B2oxSection) entry.getValue();
            int sectionTotal = section.getSectionItemsTotal();
            if (position < currentPos || position > (currentPos + sectionTotal) - 1) {
                currentPos += sectionTotal;
            } else {
                return (position - currentPos) - (section.isHasHeader() ? 1 : 0);
            }
        }
        throw new IndexOutOfBoundsException("Invalid position");
    }

    public void clear() {
        this.sections.clear();
        notifyDataSetChanged();
    }
}
