package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.tools.ImageUtils;

public class X8AiFollowConfirmUi implements OnClickListener {
    private View btnOk;
    private CheckBox cbTip;
    private View contentView;
    private ImageView imgFlag;
    private View imgReturn;
    private X8MainAiFlyController listener;
    private X8MainAiFlyController mX8MainAiFlyController;
    private int menuIndex;
    private ScrollView svTips;
    private TextView tvContentTip1;
    private TextView tvContentTip2;
    private TextView tvTitle;
    private View vConfirm;
    private View vItem1;
    private View vItem2;
    private View vItem3;
    private View vItemSelect;

    public X8AiFollowConfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_follow_confirm_layout, (ViewGroup) parent, true);
        initViews(this.contentView);
        initActions();
    }

    public void setX8MainAiFlyController(X8MainAiFlyController mX8MainAiFlyController) {
        this.mX8MainAiFlyController = mX8MainAiFlyController;
    }

    public void initViews(View rootView) {
        this.imgReturn = rootView.findViewById(R.id.img_ai_follow_return);
        this.btnOk = rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.cbTip = (CheckBox) rootView.findViewById(R.id.cb_ai_follow_confirm_ok);
        this.vItemSelect = rootView.findViewById(R.id.ll_ai_follow_item);
        this.vConfirm = rootView.findViewById(R.id.rl_ai_follow_info_confirm);
        this.tvTitle = (TextView) rootView.findViewById(R.id.tv_ai_follow_title);
        this.tvContentTip1 = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title1);
        this.tvContentTip2 = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title2);
        this.svTips = (ScrollView) rootView.findViewById(R.id.sv_ai_items);
        this.vItem1 = rootView.findViewById(R.id.rl_ai_follow_normal);
        this.vItem2 = rootView.findViewById(R.id.rl_ai_follow_parallel);
        this.vItem3 = rootView.findViewById(R.id.rl_ai_follow_lockup);
        this.vItemSelect.setVisibility(0);
        this.vConfirm.setVisibility(8);
        this.tvTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_follow));
        this.imgFlag = (ImageView) rootView.findViewById(R.id.img_follow_flag);
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.vItem1.setOnClickListener(this);
        this.vItem2.setOnClickListener(this);
        this.vItem3.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_return) {
            if (this.menuIndex == 0) {
                this.mX8MainAiFlyController.onCloseConfirmUi();
                return;
            }
            this.menuIndex = 0;
            this.svTips.fullScroll(33);
            this.vItemSelect.setVisibility(0);
            this.vConfirm.setVisibility(8);
            this.tvTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_follow));
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            if (this.menuIndex == 1) {
                if (this.cbTip.isChecked()) {
                    X8AiConfig.getInstance().setAiFollowNormalCourse(false);
                } else {
                    X8AiConfig.getInstance().setAiFollowNormalCourse(true);
                }
            } else if (this.menuIndex == 2) {
                if (this.cbTip.isChecked()) {
                    X8AiConfig.getInstance().setAiFollowParallelCourse(false);
                } else {
                    X8AiConfig.getInstance().setAiFollowParallelCourse(true);
                }
            } else if (this.menuIndex == 3) {
                if (this.cbTip.isChecked()) {
                    X8AiConfig.getInstance().setAiFollowLockupCourse(false);
                } else {
                    X8AiConfig.getInstance().setAiFollowLockupCourse(true);
                }
            }
            this.mX8MainAiFlyController.onFollowConfirmOkClick(this.menuIndex - 1);
        } else if (id == R.id.rl_ai_follow_normal) {
            this.menuIndex = 1;
            if (X8AiConfig.getInstance().isAiFollowNormalCourse()) {
                onSelectItem(this.contentView.getContext().getString(R.string.x8_ai_fly_follow_normal), this.contentView.getContext().getString(R.string.x8_ai_fly_follow_normal_tip1), R.drawable.x8_img_follow_normal_flag);
            } else {
                this.mX8MainAiFlyController.onFollowConfirmOkClick(this.menuIndex - 1);
            }
        } else if (id == R.id.rl_ai_follow_parallel) {
            this.menuIndex = 2;
            if (X8AiConfig.getInstance().isAiFollowParallelCourse()) {
                onSelectItem(this.contentView.getContext().getString(R.string.x8_ai_fly_follow_parallel), this.contentView.getContext().getString(R.string.x8_ai_fly_follow_parallel_tip1), R.drawable.x8_img_follow_flag1);
            } else {
                this.mX8MainAiFlyController.onFollowConfirmOkClick(this.menuIndex - 1);
            }
        } else if (id == R.id.rl_ai_follow_lockup) {
            this.menuIndex = 3;
            if (X8AiConfig.getInstance().isAiFollowLockupCourse()) {
                onSelectItem(this.contentView.getContext().getString(R.string.x8_ai_fly_follow_lockup), this.contentView.getContext().getString(R.string.x8_ai_fly_follow_lockup_tip1), R.drawable.x8_img_follow_lock_flag);
            } else {
                this.mX8MainAiFlyController.onFollowConfirmOkClick(this.menuIndex - 1);
            }
        }
    }

    public void onSelectItem(String title, String content, int res) {
        this.vItemSelect.setVisibility(8);
        this.vConfirm.setVisibility(0);
        this.tvTitle.setText(title);
        this.tvContentTip1.setText(content);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.contentView.getContext(), res));
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnOk.setEnabled(true);
        } else {
            this.btnOk.setEnabled(false);
        }
    }
}
