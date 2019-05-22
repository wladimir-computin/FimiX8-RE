package com.fimi.app.x8s.controls.fcsettting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8GimbalHorizontalTrimListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8HorizontalTrimView;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8GimbalHorizontalTrimController extends AbsX8Controllers {
    private final float MAX_ANGLE = 10.0f;
    private final float MIN_ANGLE = -10.0f;
    private final X8sMainActivity activity;
    private X8HorizontalTrimView horizontalTrimView;
    private IX8GimbalHorizontalTrimListener listener;

    public X8GimbalHorizontalTrimController(View rootView, X8sMainActivity activity) {
        super(rootView);
        this.activity = activity;
    }

    public void initViews(View rootView) {
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void openUi() {
        this.isShow = true;
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_view_horizontal_trim_layout, (ViewGroup) this.rootView, true);
        this.horizontalTrimView = (X8HorizontalTrimView) this.handleView.findViewById(R.id.x8_horizontal_trim_view);
        if (this.listener != null) {
            this.horizontalTrimView.setListener(this.listener);
        }
        this.horizontalTrimView.setEnabled(false);
        initData();
        super.openUi();
    }

    public void setListener(IX8GimbalHorizontalTrimListener listener) {
        this.listener = listener;
    }

    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    private void initData() {
        if (this.activity.getmX8GimbalManager() != null && StateManager.getInstance().getX8Drone().isConnect()) {
            this.activity.getmX8GimbalManager().getHorizontalAdjust(new UiCallBackListener<Float>() {
                public void onComplete(CmdResult cmdResult, Float value) {
                    if (cmdResult.isSuccess()) {
                        if (value.floatValue() > 10.0f) {
                            value = Float.valueOf(10.0f);
                        } else if (value.floatValue() < -10.0f) {
                            value = Float.valueOf(-10.0f);
                        }
                        X8GimbalHorizontalTrimController.this.horizontalTrimView.setCurrValue((float) ((int) (value.floatValue() * 10.0f)));
                    }
                }
            });
        }
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (b && this.horizontalTrimView != null) {
            this.horizontalTrimView.setEnabled(b);
        }
    }

    public void onSettingReady() {
        if (StateManager.getInstance().getX8Drone().isConnect() && this.activity.getmX8GimbalManager() != null) {
            this.activity.getmX8GimbalManager().setHorizontalAdjust(Float.valueOf(NumberUtil.decimalPointStr((double) this.horizontalTrimView.getCurrValue(), 1)).floatValue(), new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            });
        }
    }

    public boolean onClickBackKey() {
        if (this.isShow) {
            closeUi();
        }
        return false;
    }
}
