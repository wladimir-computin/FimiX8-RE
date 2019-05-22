package com.fimi.app.x8s.controls;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.fimi.TcpClient;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.X8AiModeState;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraPersonLacationListener;
import com.fimi.app.x8s.manager.X8MapGetCityManager;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.x8sdk.dataparser.AutoFcHeart;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8RightIconForMapController extends AbsX8Controllers implements OnClickListener {
    private X8sMainActivity activity;
    private ImageButton imbAiFly;
    private ImageButton imbLocation;
    private ImageView imgAiReturnHome;
    private ImageView imgAiTakeLandOff;
    private ImageView imgSetHomeByDv;
    private ImageView imgSetHomeByMan;
    private X8AiModeState mX8AiModeState;
    private X8TLRDialogManager mX8TLRDialogManager;
    private IX8CameraPersonLacationListener personLacationListener;
    private View root;
    private LinearLayout vSetHomePoint;
    private LinearLayout vTakeoffLandingAiFly;

    public X8RightIconForMapController(View root) {
        super(root);
    }

    public X8RightIconForMapController(View root, X8sMainActivity activity, IX8CameraPersonLacationListener personLacationListener, X8AiModeState mX8AiModeState) {
        super(root);
        this.root = root;
        this.activity = activity;
        this.personLacationListener = personLacationListener;
        this.mX8AiModeState = mX8AiModeState;
        this.mX8TLRDialogManager = new X8TLRDialogManager(this);
        this.imbLocation = (ImageButton) root.findViewById(R.id.imb_location);
        this.imbAiFly = (ImageButton) root.findViewById(R.id.imb_ai_fly);
        this.vSetHomePoint = (LinearLayout) root.findViewById(R.id.ll_set_home_point);
        this.vTakeoffLandingAiFly = (LinearLayout) root.findViewById(R.id.ll_takeoff_landing_aifly);
        this.imgAiTakeLandOff = (ImageView) root.findViewById(R.id.imb_x8_take_off_land);
        this.imgAiReturnHome = (ImageView) root.findViewById(R.id.imb_x8_ai_reture);
        this.imgSetHomeByDv = (ImageView) root.findViewById(R.id.img_set_home_by_dv);
        this.imgSetHomeByMan = (ImageView) root.findViewById(R.id.img_set_home_by_man);
        getDroneState();
        if (this.isConect) {
            onFcHeart(null, this.isLowpower);
        } else {
            setAiFlyEnabled(false);
        }
        changeState();
        this.imgSetHomeByDv.setEnabled(false);
        this.imgSetHomeByMan.setEnabled(false);
        switchByCloseFullScreen(true);
        initAction();
    }

    public void initViews(View rootView) {
    }

    public void initActions() {
    }

    public void initAction() {
        this.imbLocation.setOnClickListener(this);
        this.imgSetHomeByDv.setOnClickListener(this);
        this.imgSetHomeByMan.setOnClickListener(this);
        this.imbAiFly.setOnClickListener(this);
        this.imgAiTakeLandOff.setOnClickListener(this);
        this.imgAiReturnHome.setOnClickListener(this);
    }

    public void defaultVal() {
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void onClick(View v) {
        int id = v.getId();
        X8DoubleCustomDialog dialog;
        if (id == this.imbAiFly.getId()) {
            this.activity.onAiFlyClick();
        } else if (id == this.imbLocation.getId()) {
            showPersonLocation();
        } else if (id == R.id.imb_x8_take_off_land) {
            openTakeOffOrLandingUi();
        } else if (id == R.id.imb_x8_ai_reture) {
            this.mX8TLRDialogManager.showReturnDialog();
        } else if (id == R.id.img_set_home_by_dv) {
            dialog = new X8DoubleCustomDialog(this.root.getContext(), this.root.getContext().getString(R.string.x8_switch_home2_title), this.root.getContext().getString(R.string.x8_switch_home2_drone_msg), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8MapGetCityManager.onSetHomeEvent(X8RightIconForMapController.this.activity, 0);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else if (id == R.id.img_set_home_by_man) {
            dialog = new X8DoubleCustomDialog(this.root.getContext(), this.root.getContext().getString(R.string.x8_switch_home2_title), this.root.getContext().getString(R.string.x8_switch_home2_phone_title), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8MapGetCityManager.onSetHomeEvent(X8RightIconForMapController.this.activity, 1);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private void openTakeOffOrLandingUi() {
        if (StateManager.getInstance().getX8Drone().isInSky()) {
            this.mX8TLRDialogManager.showLandingDialog();
        } else {
            this.mX8TLRDialogManager.showTakeOffDialog();
        }
    }

    public void showPersonLocation() {
        if (this.personLacationListener != null) {
            this.personLacationListener.showPersonLocation();
        }
    }

    public void switchByCloseFullScreen(boolean isFullVideo) {
        int i;
        int i2 = 8;
        ImageButton imageButton = this.imbLocation;
        if (isFullVideo) {
            i = 8;
        } else {
            i = 0;
        }
        imageButton.setVisibility(i);
        LinearLayout linearLayout = this.vSetHomePoint;
        if (!isFullVideo) {
            i2 = 0;
        }
        linearLayout.setVisibility(i2);
    }

    public void switch2Map(boolean isShow) {
        int i = 8;
        if (!this.mX8AiModeState.isAiModeStateReady()) {
            int i2;
            ImageButton imageButton = this.imbLocation;
            if (isShow) {
                i2 = 8;
            } else {
                i2 = 0;
            }
            imageButton.setVisibility(i2);
            LinearLayout linearLayout = this.vSetHomePoint;
            if (!isShow) {
                i = 0;
            }
            linearLayout.setVisibility(i);
        }
    }

    public void closeUiForSetting() {
        showAll(false);
    }

    public void closeUiForTaskRunning() {
        boolean isShow;
        int i;
        int i2 = 8;
        this.vTakeoffLandingAiFly.setVisibility(8);
        if (!true || this.activity.getmMapVideoController().isFullVideo()) {
            isShow = false;
        } else {
            isShow = true;
        }
        ImageButton imageButton = this.imbLocation;
        if (isShow) {
            i = 0;
        } else {
            i = 8;
        }
        imageButton.setVisibility(i);
        LinearLayout linearLayout = this.vSetHomePoint;
        if (isShow) {
            i2 = 0;
        }
        linearLayout.setVisibility(i2);
    }

    public void openUiForSetting() {
        boolean isShow;
        int i;
        int i2 = 8;
        this.vTakeoffLandingAiFly.setVisibility(1 == null ? 8 : 0);
        if (1 == null || this.activity.getmMapVideoController().isFullVideo()) {
            isShow = false;
        } else {
            isShow = true;
        }
        ImageButton imageButton = this.imbLocation;
        if (isShow) {
            i = 0;
        } else {
            i = 8;
        }
        imageButton.setVisibility(i);
        LinearLayout linearLayout = this.vSetHomePoint;
        if (isShow) {
            i2 = 0;
        }
        linearLayout.setVisibility(i2);
        TcpClient.getIntance().sendLog("openUiForSetting--->" + isShow);
    }

    public void openUiForTaskRunning() {
        boolean isShow;
        int i;
        int i2 = 8;
        if (this.mX8AiModeState.isAiModeStateIdle()) {
            this.vTakeoffLandingAiFly.setVisibility(0);
        }
        if (this.activity.getmMapVideoController().isFullVideo()) {
            isShow = false;
        } else {
            isShow = true;
        }
        ImageButton imageButton = this.imbLocation;
        if (isShow) {
            i = 0;
        } else {
            i = 8;
        }
        imageButton.setVisibility(i);
        LinearLayout linearLayout = this.vSetHomePoint;
        if (isShow) {
            i2 = 0;
        }
        linearLayout.setVisibility(i2);
    }

    public void showAll(boolean isShow) {
        int i;
        int i2 = 8;
        ImageButton imageButton = this.imbLocation;
        if (isShow) {
            i = 0;
        } else {
            i = 8;
        }
        imageButton.setVisibility(i);
        LinearLayout linearLayout = this.vSetHomePoint;
        if (isShow) {
            i = 0;
        } else {
            i = 8;
        }
        linearLayout.setVisibility(i);
        LinearLayout linearLayout2 = this.vTakeoffLandingAiFly;
        if (isShow) {
            i2 = 0;
        }
        linearLayout2.setVisibility(i2);
    }

    public void showLocation() {
        boolean isShow;
        int i;
        int i2 = 8;
        if (this.activity.getmMapVideoController().isFullVideo()) {
            isShow = false;
        } else {
            isShow = true;
        }
        ImageButton imageButton = this.imbLocation;
        if (isShow) {
            i = 0;
        } else {
            i = 8;
        }
        imageButton.setVisibility(i);
        LinearLayout linearLayout = this.vSetHomePoint;
        if (isShow) {
            i2 = 0;
        }
        linearLayout.setVisibility(i2);
    }

    public void showAiFlyIcon() {
        this.vTakeoffLandingAiFly.setVisibility(0);
    }

    public void d() {
        int i = R.drawable.x8_img_take_off_small;
        i = R.drawable.x8_img_landing_small;
        i = R.drawable.x8_img_return_small;
    }

    public void onFcHeart(AutoFcHeart fcHeart, boolean isLowPow) {
        boolean takeoffLanding;
        int ctrtype;
        DroneState state = StateManager.getInstance().getX8Drone();
        if (state.isInSky()) {
            this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_small_landing);
            this.imgAiReturnHome.setEnabled(true);
            takeoffLanding = false;
            if (StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
                takeoffLanding = true;
            } else {
                ctrtype = StateManager.getInstance().getX8Drone().getCtrlType();
                if (ctrtype == 1) {
                    takeoffLanding = false;
                } else if (ctrtype == 3) {
                    takeoffLanding = true;
                } else if (ctrtype == 2) {
                    takeoffLanding = true;
                }
            }
            this.imgAiTakeLandOff.setEnabled(takeoffLanding);
        }
        if (state.isOnGround()) {
            if (state.isCanFly()) {
                this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_small_takeoff);
                ctrtype = StateManager.getInstance().getX8Drone().getCtrlType();
                takeoffLanding = false;
                if (ctrtype == 1) {
                    takeoffLanding = false;
                } else if (ctrtype == 3) {
                    takeoffLanding = true;
                } else if (ctrtype == 2) {
                    takeoffLanding = true;
                }
                this.imgAiTakeLandOff.setEnabled(takeoffLanding);
            } else {
                this.imgAiTakeLandOff.setEnabled(false);
            }
            this.imgAiReturnHome.setEnabled(false);
            ctrtype = StateManager.getInstance().getX8Drone().getCtrlType();
            if (ctrtype != 1) {
                if (ctrtype != 3) {
                    if (ctrtype == 2) {
                    }
                }
            }
        }
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        getDroneState();
        changeState();
        if (!b) {
            this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_small_takeoff);
            setAiFlyEnabled(false);
            this.mX8TLRDialogManager.onDroneConnected(b);
        }
    }

    public void changeState() {
        if (this.isConect && this.isInSky) {
            setChangeHomeEnabled(true);
        } else {
            setChangeHomeEnabled(false);
        }
    }

    public void setAiFlyEnabled(boolean b) {
        this.imgAiTakeLandOff.setEnabled(b);
        this.imgAiReturnHome.setEnabled(b);
    }

    public void setChangeHomeEnabled(boolean b) {
        this.imgSetHomeByDv.setEnabled(b);
        this.imgSetHomeByMan.setEnabled(b);
    }

    public void showSportState(AutoFcSportState state) {
        this.mX8TLRDialogManager.showSportState(state);
    }

    public X8sMainActivity getActivity() {
        return this.activity;
    }
}
