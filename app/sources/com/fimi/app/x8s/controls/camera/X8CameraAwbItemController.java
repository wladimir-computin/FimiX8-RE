package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.x8sdk.controller.CameraManager;

public class X8CameraAwbItemController extends AbsX8Controllers implements OnSeekBarChangeListener {
    private SeekBar awbSeekBar;
    private CameraManager cameraManager;
    private Context context;
    private boolean isUser = false;

    public X8CameraAwbItemController(View rootView, CameraManager manager) {
        super(rootView);
        this.cameraManager = manager;
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

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
    }

    public boolean onClickBackKey() {
        return false;
    }
}
