package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.interfaces.IX8RcRockerListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8TabItem;
import com.fimi.app.x8s.widget.X8TabItem.OnSelectListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetRcMode;

public class X8RockerModeController extends AbsX8MenuBoxControllers implements OnClickListener {
    public static final int ROCKER_AMERICAN = 1;
    public static final int ROCKER_CHINESE = 3;
    public static final int ROCKER_JAPANESE = 2;
    private int curMode = 0;
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private ImageView imgRockerLeft;
    private ImageView imgRockerRight;
    private IX8CalibrationListener listener;
    private Context mContext;
    IX8RcRockerListener onRcCtrlModelListener;
    private X8TabItem thSwitchRocker;
    private TextView tvLeftSideDown;
    private TextView tvLeftSideLeft;
    private TextView tvLeftSideRight;
    private TextView tvLeftSideUp;
    private TextView tvRightSideDown;
    private TextView tvRightSideLeft;
    private TextView tvRightSideRight;
    private TextView tvRightSideUp;

    public X8RockerModeController(View rootView, IX8RcRockerListener onRcCtrlModelListener) {
        super(rootView);
        this.onRcCtrlModelListener = onRcCtrlModelListener;
    }

    public void onRcConnected(boolean isConnect) {
        if (((this.thSwitchRocker != null ? 1 : 0) & this.isShow) != 0) {
            this.thSwitchRocker.setEnabled(isConnect);
            this.thSwitchRocker.setAlpha(isConnect ? 1.0f : 0.4f);
        }
    }

    public void showApDialog(final int index) {
        new X8DoubleCustomDialog(this.rootView.getContext(), getString(R.string.x8_rc_setting_model_dialog_title), getString(R.string.x8_rc_setting_model_dialog_content), new onDialogButtonClickListener() {
            public void onLeft() {
                X8RockerModeController.this.switchRocker(X8RockerModeController.this.curMode);
            }

            public void onRight() {
                byte mode = (byte) 0;
                switch (index) {
                    case 0:
                        mode = (byte) 2;
                        break;
                    case 1:
                        mode = (byte) 1;
                        break;
                    case 2:
                        mode = (byte) 3;
                        break;
                }
                final byte temp = mode;
                X8RockerModeController.this.fcCtrlManager.setRcCtrlMode(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8RockerModeController.this.switchRocker(index);
                            if (X8RockerModeController.this.onRcCtrlModelListener != null) {
                                X8RockerModeController.this.onRcCtrlModelListener.onRcCtrlModelListener(temp);
                                return;
                            }
                            return;
                        }
                        X8RockerModeController.this.switchRocker(X8RockerModeController.this.curMode);
                    }
                }, mode);
            }
        }).show();
    }

    public void initViews(View rootView) {
        this.contentView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_rc_item_rocker_mode, (ViewGroup) rootView, true);
        this.imgReturn = (ImageView) this.contentView.findViewById(R.id.img_return);
        this.thSwitchRocker = (X8TabItem) this.contentView.findViewById(R.id.th_switch_rockers);
        this.imgRockerLeft = (ImageView) this.contentView.findViewById(R.id.img_rocker_left);
        this.imgRockerRight = (ImageView) this.contentView.findViewById(R.id.img_rocker_right);
        this.tvLeftSideLeft = (TextView) this.contentView.findViewById(R.id.tv_left_side_left);
        this.tvLeftSideRight = (TextView) this.contentView.findViewById(R.id.tv_left_side_right);
        this.tvLeftSideUp = (TextView) this.contentView.findViewById(R.id.tv_left_side_up);
        this.tvLeftSideDown = (TextView) this.contentView.findViewById(R.id.tv_left_side_down);
        this.tvRightSideLeft = (TextView) this.contentView.findViewById(R.id.tv_right_side_left);
        this.tvRightSideRight = (TextView) this.contentView.findViewById(R.id.tv_right_side_right);
        this.tvRightSideUp = (TextView) this.contentView.findViewById(R.id.tv_right_side_up);
        this.tvRightSideDown = (TextView) this.contentView.findViewById(R.id.tv_right_side_down);
        this.mContext = this.contentView.getContext();
        initActions();
    }

    public void initActions() {
        if (this.contentView != null) {
            this.imgReturn.setOnClickListener(this);
            this.thSwitchRocker.setOnSelectListener(new OnSelectListener() {
                public void onSelect(int index, String text) {
                    if (index != X8RockerModeController.this.curMode) {
                        X8RockerModeController.this.showApDialog(index);
                    }
                }
            });
        }
    }

    public void onDroneConnected(boolean b) {
    }

    public void defaultVal() {
    }

    public void onClick(View v) {
        if (v.getId() == R.id.img_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onCalibrationReturn();
            }
        }
    }

    private void requestDefaultValue() {
        if (this.fcCtrlManager != null) {
            this.fcCtrlManager.getRcCtrlMode(new UiCallBackListener<AckGetRcMode>() {
                public void onComplete(CmdResult cmdResult, AckGetRcMode obj) {
                    if (cmdResult.isSuccess()) {
                        switch (obj.getMode()) {
                            case 1:
                                X8RockerModeController.this.switchRocker(1);
                                return;
                            case 2:
                                X8RockerModeController.this.switchRocker(0);
                                return;
                            case 3:
                                X8RockerModeController.this.switchRocker(2);
                                return;
                            default:
                                return;
                        }
                    }
                }
            });
        }
    }

    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(0);
        requestDefaultValue();
        getDroneState();
        onRcConnected(this.isRcConnect);
    }

    public void closeItem() {
        this.isShow = false;
        this.contentView.setVisibility(8);
        defaultVal();
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setCalibrationListener(IX8CalibrationListener listener) {
        this.listener = listener;
    }

    private void switchRocker(int rocker) {
        this.curMode = rocker;
        this.thSwitchRocker.setSelect(rocker);
        this.imgRockerLeft.setImageLevel(rocker);
        this.imgRockerRight.setImageLevel(rocker);
        switch (rocker) {
            case 0:
                this.tvLeftSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left_turn);
                this.tvLeftSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right_turn);
                this.tvLeftSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_forward);
                this.tvLeftSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_back_off);
                this.tvRightSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left);
                this.tvRightSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right);
                this.tvRightSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_up);
                this.tvRightSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_down);
                return;
            case 1:
                this.tvLeftSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left_turn);
                this.tvLeftSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right_turn);
                this.tvLeftSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_up);
                this.tvLeftSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_down);
                this.tvRightSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left);
                this.tvRightSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right);
                this.tvRightSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_forward);
                this.tvRightSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_back_off);
                return;
            case 2:
                this.tvLeftSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left);
                this.tvLeftSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right);
                this.tvLeftSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_forward);
                this.tvLeftSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_back_off);
                this.tvRightSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left_turn);
                this.tvRightSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right_turn);
                this.tvRightSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_up);
                this.tvRightSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_down);
                return;
            default:
                return;
        }
    }
}
