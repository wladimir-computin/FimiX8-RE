package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.content.Intent;
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
import com.fimi.app.x8s.interfaces.AbsX8BaseConnectView;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8AiLineHistoryActivity;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;

public class X8AiLinesConfirmUi extends AbsX8BaseConnectView implements OnClickListener {
    private Activity activity;
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
    private TextView tvDevice;
    private TextView tvHistory;
    private TextView tvMap;
    private TextView tvTip1;
    private TextView tvTip2;
    private TextView tvTip3;
    private TextView tvTip4;
    private TextView tvTitle;
    private View vConfirm;
    private ImageView vItem1;
    private ImageView vItem2;
    private ImageView vItem3;
    private View vItemSelect;

    public X8AiLinesConfirmUi(Activity activity, View parent) {
        super(activity, parent);
        this.activity = activity;
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_lines_confirm_layout, (ViewGroup) parent, true);
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
        this.vConfirm = rootView.findViewById(R.id.rl_ai_confirm);
        this.tvTitle = (TextView) rootView.findViewById(R.id.tv_ai_follow_title);
        this.tvContentTip1 = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title1);
        this.vItem1 = (ImageView) rootView.findViewById(R.id.img_ai_lind_map1);
        this.vItem2 = (ImageView) rootView.findViewById(R.id.img_ai_lind_map2);
        this.vItem3 = (ImageView) rootView.findViewById(R.id.img_ai_lind_map3);
        this.vItemSelect = rootView.findViewById(R.id.ll_ai_line_item);
        this.svTips = (ScrollView) rootView.findViewById(R.id.sv_ai_items);
        this.imgFlag = (ImageView) rootView.findViewById(R.id.img_ai_line_flag);
        this.tvMap = (TextView) rootView.findViewById(R.id.tv_ai_lind_map1);
        this.tvDevice = (TextView) rootView.findViewById(R.id.tv_ai_lind_map2);
        this.tvHistory = (TextView) rootView.findViewById(R.id.tv_ai_lind_map3);
        this.vItemSelect.setVisibility(0);
        this.vConfirm.setVisibility(8);
        this.tvTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_route));
        if (this.isConect) {
            setFcHeart(this.isInSky, this.isLowpower);
        } else {
            this.vItem3.setEnabled(false);
            this.vItem2.setEnabled(false);
            this.vItem1.setEnabled(false);
            this.tvMap.setEnabled(false);
            this.tvDevice.setEnabled(false);
            this.tvHistory.setEnabled(false);
        }
        this.tvTip1 = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title1);
        this.tvTip2 = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title2);
        this.tvTip3 = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title3);
        this.tvTip4 = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title4);
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
            this.tvTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_route));
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            if (this.menuIndex == 1) {
                if (this.cbTip.isChecked()) {
                    X8AiConfig.getInstance().setAiLineCourse(false);
                } else {
                    X8AiConfig.getInstance().setAiLineCourse(true);
                }
                this.mX8MainAiFlyController.onLinesConfirmOkClick(this.menuIndex - 1);
            } else if (this.menuIndex == 2) {
                if (this.cbTip.isChecked()) {
                    X8AiConfig.getInstance().setAiLineCourseFpv(false);
                } else {
                    X8AiConfig.getInstance().setAiLineCourseFpv(true);
                }
                this.mX8MainAiFlyController.onLinesConfirmOkClick(this.menuIndex - 1);
            } else if (this.menuIndex == 3) {
                if (this.cbTip.isChecked()) {
                    X8AiConfig.getInstance().setAiLineCourseHistory(false);
                } else {
                    X8AiConfig.getInstance().setAiLineCourseHistory(true);
                }
                this.activity.startActivityForResult(new Intent(this.activity, X8AiLineHistoryActivity.class), X8sMainActivity.X8GETAILINEID);
            }
        } else if (id == R.id.img_ai_lind_map1) {
            this.menuIndex = 1;
            if (X8AiConfig.getInstance().isAiLineCourse()) {
                onSelectItem(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_map_model), String.format(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip11), new Object[]{X8NumberUtil.getDistanceNumberString(1000.0f, 0, true)}), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip12), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip13), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip14), R.drawable.x8_img_ai_line_map);
            } else {
                this.mX8MainAiFlyController.onLinesConfirmOkClick(this.menuIndex - 1);
            }
            this.btnOk.setEnabled(true);
        } else if (id == R.id.img_ai_lind_map2) {
            this.menuIndex = 2;
            if (X8AiConfig.getInstance().isAiLineCourseFpv()) {
                onSelectItem(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_vedio_model), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip21), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip12), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip13), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip14), R.drawable.x8_img_ai_line_fpv);
                return;
            }
            this.mX8MainAiFlyController.onLinesConfirmOkClick(this.menuIndex - 1);
        } else if (id == R.id.img_ai_lind_map3) {
            this.menuIndex = 3;
            if (X8AiConfig.getInstance().isAiLineCourseHistory()) {
                onSelectItem(this.contentView.getContext().getString(R.string.x8_ai_fly_line_history), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip31), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip32), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip33), this.contentView.getContext().getString(R.string.x8_ai_fly_lines_tip34), R.drawable.x8_img_ai_line_history);
                return;
            }
            this.activity.startActivityForResult(new Intent(this.activity, X8AiLineHistoryActivity.class), X8sMainActivity.X8GETAILINEID);
        }
    }

    public void onSelectItem(String title, String content1, String content2, String content3, String content4, int res) {
        this.vItemSelect.setVisibility(8);
        this.vConfirm.setVisibility(0);
        this.tvTitle.setText(title);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.contentView.getContext(), res));
        this.tvTip1.setText(content1);
        this.tvTip2.setText(content2);
        this.tvTip3.setText(content3);
        this.tvTip4.setText(content4);
    }

    public void setFcHeart(boolean inSky, boolean isLowPower) {
        this.vItem3.setEnabled(inSky);
        this.vItem2.setEnabled(inSky);
        this.vItem1.setEnabled(true);
        this.tvMap.setEnabled(true);
        this.tvDevice.setEnabled(inSky);
        this.tvHistory.setEnabled(inSky);
        if (this.menuIndex == 1) {
            if (isLowPower) {
                this.btnOk.setEnabled(true);
            } else {
                this.btnOk.setEnabled(false);
            }
        } else if (inSky && isLowPower) {
            this.btnOk.setEnabled(true);
        } else {
            this.btnOk.setEnabled(false);
        }
    }
}
