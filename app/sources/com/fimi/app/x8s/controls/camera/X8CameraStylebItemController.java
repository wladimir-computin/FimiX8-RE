package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.x8sdk.controller.CameraManager;

public class X8CameraStylebItemController extends AbsX8Controllers implements OnSeekBarChangeListener {
    private SeekBar awbSeekBar;
    private CameraManager cameraManager;
    private Context context;
    private boolean isUser = false;

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public X8CameraStylebItemController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.awbSeekBar = (SeekBar) rootView.findViewById(R.id.awb_seekBar);
        this.awbSeekBar.setOnSeekBarChangeListener(this);
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.isUser = fromUser;
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public boolean onClickBackKey() {
        return false;
    }
}
