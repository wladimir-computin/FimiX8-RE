package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.tools.ImageUtils;

public class X8AiGravitationComfirmUi implements OnClickListener {
    private View contentView;
    private View mBtnOk;
    private CheckBox mCbTip;
    private ImageView mImGravitationFlag;
    private View mImgReturn;
    private X8MainAiFlyController mX8MainAiFlyController;

    public X8AiGravitationComfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_gravitation_comfirm_layout, (ViewGroup) parent, true);
        initViews(this.contentView);
        initActions();
    }

    private void initActions() {
        this.mImgReturn.setOnClickListener(this);
        this.mBtnOk.setOnClickListener(this);
    }

    private void initViews(View contentView) {
        this.mImgReturn = contentView.findViewById(R.id.img_ai_fly_gravitation_return);
        this.mBtnOk = contentView.findViewById(R.id.btn_ai_gravitation_confirm_ok);
        this.mCbTip = (CheckBox) contentView.findViewById(R.id.cb_ai_gravitation_confirm_ok);
        this.mImGravitationFlag = (ImageView) contentView.findViewById(R.id.img_gravitation_flag);
        this.mImGravitationFlag.setImageBitmap(ImageUtils.getBitmapByPath(contentView.getContext(), R.drawable.x8_img_ai_gravitation_flag));
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_ai_fly_gravitation_return) {
            this.mX8MainAiFlyController.onCloseConfirmUi();
        } else if (i == R.id.btn_ai_gravitation_confirm_ok) {
            if (this.mCbTip.isChecked()) {
                X8AiConfig.getInstance().setAiFlyGravitation(false);
            } else {
                X8AiConfig.getInstance().setAiFlyGravitation(true);
            }
            this.mX8MainAiFlyController.onGravitationConfirmOkClick();
        }
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
    }

    public void setX8MainAiFlyController(X8MainAiFlyController x8MainAiFlyController) {
        this.mX8MainAiFlyController = x8MainAiFlyController;
    }
}
