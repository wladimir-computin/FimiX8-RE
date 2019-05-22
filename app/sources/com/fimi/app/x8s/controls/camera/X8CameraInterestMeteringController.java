package com.fimi.app.x8s.controls.camera;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.kernel.utils.AbViewUtil;
import com.fimi.x8sdk.controller.CameraManager;

public class X8CameraInterestMeteringController extends AbsX8MenuBoxControllers implements OnClickListener {
    private final int DELAYED_TIME = 5000;
    private final int MSG_LOCK_EV = 2;
    private final int MSG_METERING = 1;
    private CameraManager cameraManager;
    @SuppressLint({"HandlerLeak"})
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgID = msg.what;
            if (msgID == 1) {
                X8CameraInterestMeteringController.this.x8IvInterestMetering.setVisibility(8);
            } else if (msgID == 2) {
                X8CameraInterestMeteringController.this.x8IvInterestMetering.setAlpha(0.5f);
            }
        }
    };
    private int interestMeteringIndex;
    private boolean isLockEv;
    private int leftX;
    private View parentView;
    private int topY;
    private ImageView x8IvInterestMetering;

    public X8CameraInterestMeteringController(View rootView) {
        super(rootView);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void onClick(View view) {
        if (view.getId() != R.id.x8_iv_interest_metering) {
            return;
        }
        if (this.isLockEv) {
            this.x8IvInterestMetering.setVisibility(8);
            this.isLockEv = false;
            this.handler.removeMessages(2);
            return;
        }
        this.cameraManager.setInterestMetering((this.interestMeteringIndex + 24) + "");
        this.x8IvInterestMetering.setImageResource(R.drawable.x8_camera_interest_metering_pressed);
        this.isLockEv = true;
        this.handler.removeMessages(1);
        this.handler.sendEmptyMessageDelayed(2, 5000);
    }

    public void initViews(View rootView) {
        this.parentView = rootView.findViewById(R.id.x8_rl_interest_merering);
        this.x8IvInterestMetering = (ImageView) this.parentView.findViewById(R.id.x8_iv_interest_metering);
        this.x8IvInterestMetering.setOnClickListener(this);
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void setImageViewXY(float x, float y) {
        int i = 220;
        if (this.isLockEv && this.x8IvInterestMetering.getVisibility() == 0) {
            this.x8IvInterestMetering.setAlpha(1.0f);
            this.handler.removeMessages(2);
            this.handler.sendEmptyMessageDelayed(2, 5000);
            return;
        }
        this.interestMeteringIndex = AbViewUtil.xyToBox(this.parentView.getContext(), x, y);
        this.cameraManager.setInterestMetering(this.interestMeteringIndex + "");
        this.handler.removeMessages(1);
        LayoutParams params = (LayoutParams) this.x8IvInterestMetering.getLayoutParams();
        this.leftX = (int) (x - ((float) ((this.x8IvInterestMetering.getWidth() == 0 ? 220 : this.x8IvInterestMetering.getWidth()) / 2)));
        if (AbViewUtil.getScreenWidth(this.rootView.getContext()) - this.x8IvInterestMetering.getWidth() < this.leftX) {
            this.leftX = AbViewUtil.getScreenWidth(this.rootView.getContext());
        }
        if (this.x8IvInterestMetering.getHeight() != 0) {
            i = this.x8IvInterestMetering.getHeight();
        }
        this.topY = (int) (y - ((float) (i / 2)));
        if (AbViewUtil.getScreenHeight(this.rootView.getContext()) - this.x8IvInterestMetering.getHeight() < this.topY) {
            this.topY = AbViewUtil.getScreenHeight(this.rootView.getContext());
        }
        params.topMargin = this.topY;
        params.leftMargin = this.leftX;
        this.x8IvInterestMetering.setLayoutParams(params);
        this.x8IvInterestMetering.setVisibility(0);
        this.x8IvInterestMetering.setAlpha(1.0f);
        this.x8IvInterestMetering.setImageResource(R.drawable.x8_camera_interest_metering);
        this.handler.sendEmptyMessageDelayed(1, 5000);
    }

    public void setIvInterestMeteringVisibility(int visibility) {
        this.x8IvInterestMetering.setVisibility(visibility);
    }

    public int getIvInterestMeteringVisibility() {
        return this.x8IvInterestMetering.getVisibility();
    }
}
