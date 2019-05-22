package com.fimi.app.x8s.controls.fcsettting;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.x8sdk.controller.FcCtrlManager;

public class X8RockerCalibrationController extends AbsX8MenuBoxControllers implements OnClickListener {
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private IX8CalibrationListener listener;

    public X8RockerCalibrationController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.contentView = rootView.findViewById(R.id.x8_rl_main_rc_item_rocker_mode_layout);
        this.imgReturn = (ImageView) this.contentView.findViewById(R.id.img_return);
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
    }

    public void onDroneConnected(boolean b) {
        if (this.isShow) {
        }
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

    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(0);
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
}
