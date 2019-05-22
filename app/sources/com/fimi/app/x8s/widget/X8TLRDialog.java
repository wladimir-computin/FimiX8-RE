package com.fimi.app.x8s.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8TLRDialog extends Dialog {
    private ImageView imgFlag;
    private boolean isNeedChange;
    private String prex;
    int res = 0;
    float temp = 0.0f;
    int tmpRes = 0;
    private TextView tvMessage;
    private TextView tvTitle;

    public X8TLRDialog(@NonNull Context context, @Nullable String title, @NonNull String message, boolean isNeedChange, int drawId, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_return_dialog_custom);
        this.isNeedChange = isNeedChange;
        this.prex = getContext().getString(R.string.x8_ai_fly_return_home_tip);
        this.tvMessage = (TextView) findViewById(R.id.tv_message);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.imgFlag = (ImageView) findViewById(R.id.img_ai_return_flag);
        TextView tvLeft = (TextView) findViewById(R.id.btn_left);
        TextView tvRight = (TextView) findViewById(R.id.btn_right);
        this.tvTitle.setText(title);
        tvLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeft();
                }
                X8TLRDialog.this.dismiss();
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRight();
                }
                X8TLRDialog.this.dismiss();
            }
        });
        if (isNeedChange) {
            AutoFcSportState state = StateManager.getInstance().getX8Drone().getFcSportState();
            if (state != null) {
                showSportState(state);
                return;
            }
            return;
        }
        this.tvMessage.setText(message);
        this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(getContext(), drawId));
    }

    public void showSportState(AutoFcSportState state) {
        if (this.isNeedChange) {
            float h = state.getHeight();
            if (state.getHomeDistance() <= 10.0f) {
                boolean isOpenAccurateLanding = StateManager.getInstance().getX8Drone().isOpenAccurateLanding();
                if (h <= 6.0f) {
                    this.temp = 3.0f;
                    if (isOpenAccurateLanding) {
                        if (GlobalConfig.getInstance().isShowmMtric()) {
                            this.tmpRes = R.drawable.x8_img_ai_return_5;
                        } else {
                            this.tmpRes = R.drawable.x8_img_ai_return_ft_5;
                        }
                    } else if (GlobalConfig.getInstance().isShowmMtric()) {
                        this.tmpRes = R.drawable.x8_img_ai_return_1;
                    } else {
                        this.tmpRes = R.drawable.x8_img_ai_return_ft_1;
                    }
                } else {
                    this.temp = h;
                    if (isOpenAccurateLanding) {
                        if (GlobalConfig.getInstance().isShowmMtric()) {
                            this.tmpRes = R.drawable.x8_img_ai_return_6;
                        } else {
                            this.tmpRes = R.drawable.x8_img_ai_return_ft_6;
                        }
                    } else if (GlobalConfig.getInstance().isShowmMtric()) {
                        this.tmpRes = R.drawable.x8_img_ai_return_2;
                    } else {
                        this.tmpRes = R.drawable.x8_img_ai_return_ft_2;
                    }
                }
            } else if (h <= StateManager.getInstance().getX8Drone().getReturnHomeHight()) {
                this.temp = StateManager.getInstance().getX8Drone().getReturnHomeHight();
                if (GlobalConfig.getInstance().isShowmMtric()) {
                    this.tmpRes = R.drawable.x8_img_ai_return_3;
                } else {
                    this.tmpRes = R.drawable.x8_img_ai_return_ft_3;
                }
            } else {
                this.temp = h;
                if (GlobalConfig.getInstance().isShowmMtric()) {
                    this.tmpRes = R.drawable.x8_img_ai_return_4;
                } else {
                    this.tmpRes = R.drawable.x8_img_ai_return_ft_4;
                }
            }
            this.tvMessage.setText(String.format(this.prex, new Object[]{X8NumberUtil.getDistanceNumberString(this.temp, 1, true)}));
            if (this.tmpRes != 0 && this.tmpRes != this.res) {
                this.res = this.tmpRes;
                this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(getContext(), this.res));
            }
        }
    }
}
