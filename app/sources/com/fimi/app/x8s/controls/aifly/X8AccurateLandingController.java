package com.fimi.app.x8s.controls.aifly;

import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.x8sdk.dataparser.AckAccurateLandingState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AccurateLandingController {
    private X8AiTipWithCloseView mTipBgView;
    private View root;

    public X8AccurateLandingController(View root) {
        this.root = root;
        this.mTipBgView = (X8AiTipWithCloseView) root.findViewById(R.id.v_accurate_landing_tip);
    }

    public void onDroneConnected(boolean b) {
        if (!b) {
            this.mTipBgView.setVisibility(8);
            this.mTipBgView.setClose(false);
        } else if (AckAccurateLandingState.isTimeOut()) {
            this.mTipBgView.setVisibility(8);
            this.mTipBgView.setClose(false);
        } else if (!this.mTipBgView.isClose()) {
            if (StateManager.getInstance().getX8Drone().isAutoLandingCheckObj()) {
                this.mTipBgView.setTipText(this.root.getContext().getString(R.string.x8_accurate_check_obj1));
            } else {
                this.mTipBgView.setTipText(this.root.getContext().getString(R.string.x8_accurate_check_obj0));
            }
            this.mTipBgView.setVisibility(0);
        }
    }
}
