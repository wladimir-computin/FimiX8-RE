package com.fimi.app.x8s.controls;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8MainTopBarListener;
import com.fimi.app.x8s.tools.TimeFormateUtil;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8BatteryReturnLandingView;
import com.fimi.app.x8s.widget.X8MainElectricView;
import com.fimi.app.x8s.widget.X8MainPowerView;
import com.fimi.app.x8s.widget.X8MainReturnTimeTextView;
import com.fimi.app.x8s.widget.X8MainTopRightView;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.dataparser.AutoFcBattery;
import com.fimi.x8sdk.dataparser.AutoFcHeart;
import com.fimi.x8sdk.dataparser.AutoFcSignalState;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.dataparser.AutoRelayHeart;
import com.fimi.x8sdk.entity.ConectState;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8MainTopBarController extends AbsX8Controllers implements OnClickListener {
    private ImageButton ibtnReturn;
    private ImageButton ibtnSetting;
    private AutoFcSportState lastState = null;
    private IX8MainTopBarListener listener;
    private ImageView mIvDistance;
    private ImageView mIvFlyState;
    private ImageView mIvHight;
    private TextView mTvDistance;
    private TextView mTvDistanceUnit;
    private TextView mTvHight;
    private TextView mTvHightUnit;
    private TextView mTvHs;
    private TextView mTvSpeedUnit;
    private TextView mTvVs;
    private X8MainElectricView mX8MainElectricView;
    private X8MainPowerView mX8MainPowerView;
    private X8MainReturnTimeTextView mX8MainReturnTimeTextView;
    private X8MainTopRightView mX8MainTopCenterView;
    private TextView tvConnectState;
    private View vDroneInfoState;
    private X8BatteryReturnLandingView vLandingReturnView;

    public X8MainTopBarController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.main_top_bars);
        this.mX8MainReturnTimeTextView = (X8MainReturnTimeTextView) rootView.findViewById(R.id.x8_return_time_text_view);
        this.mX8MainElectricView = (X8MainElectricView) rootView.findViewById(R.id.electric_view);
        this.mX8MainPowerView = (X8MainPowerView) rootView.findViewById(R.id.power_view);
        this.ibtnSetting = (ImageButton) rootView.findViewById(R.id.x8_ibtn_setting);
        this.ibtnReturn = (ImageButton) rootView.findViewById(R.id.x8_ibtn_return);
        this.mTvHight = (TextView) rootView.findViewById(R.id.tv_hight);
        this.mIvHight = (ImageView) rootView.findViewById(R.id.iv_fly_hight);
        this.mTvDistance = (TextView) rootView.findViewById(R.id.tv_distance);
        this.mIvDistance = (ImageView) rootView.findViewById(R.id.iv_fly_distance);
        this.mTvVs = (TextView) rootView.findViewById(R.id.tv_vs);
        this.mTvHs = (TextView) rootView.findViewById(R.id.tv_hs);
        this.tvConnectState = (TextView) rootView.findViewById(R.id.tv_connect_state);
        this.mIvFlyState = (ImageView) rootView.findViewById(R.id.iv_fly_state);
        this.mX8MainTopCenterView = (X8MainTopRightView) rootView.findViewById(R.id.x8main_top_center_view);
        this.mTvHightUnit = (TextView) rootView.findViewById(R.id.tv_height_lable);
        this.mTvDistanceUnit = (TextView) rootView.findViewById(R.id.tv_distance_lable);
        this.mTvSpeedUnit = (TextView) rootView.findViewById(R.id.tv_vs_unit);
        this.vDroneInfoState = rootView.findViewById(R.id.x8_drone_info_state);
        this.vLandingReturnView = (X8BatteryReturnLandingView) rootView.findViewById(R.id.v_landing_return_view);
    }

    public void setX8sMainActivity(X8sMainActivity activity) {
        this.vLandingReturnView.setX8sMainActivity(activity);
    }

    public void initActions() {
        this.ibtnSetting.setOnClickListener(this);
        this.ibtnReturn.setOnClickListener(this);
        this.vDroneInfoState.setOnClickListener(this);
    }

    public void defaultVal() {
        this.mX8MainTopCenterView.defaultVal();
        this.mX8MainElectricView.setPercent(0);
        this.vLandingReturnView.resetByDidconnect();
        this.mTvDistance.setText(R.string.x8_na);
        this.mTvHight.setText(R.string.x8_na);
        this.mIvDistance.setBackgroundResource(R.drawable.x8_main_fly_distance_unconnect);
        this.mIvHight.setBackgroundResource(R.drawable.x8_main_fly_hight_unconnect);
        this.mTvVs.setText(R.string.x8_na);
        this.mTvHs.setText(R.string.x8_na);
        this.tvConnectState.setText(R.string.x8_fly_status_unconnect);
        this.mX8MainPowerView.setPercent(0);
        this.mIvFlyState.setBackgroundResource(R.drawable.x8_main_fly_state_unconnect);
        this.mX8MainReturnTimeTextView.setStrTime(getString(R.string.x8_na));
        this.mTvHightUnit.setText("");
        this.mTvDistanceUnit.setText("");
        this.mTvSpeedUnit.setText("");
    }

    public void onDisconnectDroneVal() {
        this.mX8MainTopCenterView.onDisconnectDroneVal();
        this.mX8MainElectricView.setPercent(0);
        this.vLandingReturnView.resetByDidconnect();
        this.mTvDistance.setText(R.string.x8_na);
        this.mTvHight.setText(R.string.x8_na);
        this.mIvDistance.setBackgroundResource(R.drawable.x8_main_fly_distance_unconnect);
        this.mIvHight.setBackgroundResource(R.drawable.x8_main_fly_hight_unconnect);
        this.mTvVs.setText(R.string.x8_na);
        this.mTvHs.setText(R.string.x8_na);
        this.tvConnectState.setText(R.string.x8_fly_status_unconnect);
        this.mX8MainPowerView.setPercent(0);
        this.mIvFlyState.setBackgroundResource(R.drawable.x8_main_fly_state_unconnect);
        this.mX8MainReturnTimeTextView.setStrTime(getString(R.string.x8_na));
        this.mTvHightUnit.setText("");
        this.mTvDistanceUnit.setText("");
        this.mTvSpeedUnit.setText("");
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.x8_ibtn_setting) {
            this.listener.onSettingClick();
        } else if (id == R.id.x8_ibtn_return) {
            this.listener.onMainReback();
        } else if (id == R.id.x8_drone_info_state) {
            this.listener.onDroneInfoStateClick();
        }
    }

    public void setListener(IX8MainTopBarListener listener) {
        this.listener = listener;
        this.mX8MainTopCenterView.setListener(listener);
    }

    public void onFcHeart(AutoFcHeart fcHeart, boolean isLowPower) {
        if (fcHeart.getCtrlType() == 1) {
            this.mIvFlyState.setBackgroundResource(R.drawable.x8_main_atti_mode);
        } else if (fcHeart.getCtrlType() == 2) {
            this.mIvFlyState.setBackgroundResource(R.drawable.x8_main_gps_mode);
        } else if (fcHeart.getCtrlType() == 3) {
            this.mIvFlyState.setBackgroundResource(R.drawable.x8_main_vpu_mode);
        }
    }

    public void onPowerChange(int percent) {
        this.mX8MainPowerView.setPercent(percent);
    }

    public void onConnectedState(ConectState state) {
        if (state.isConnectDrone()) {
            DroneState droneState = StateManager.getInstance().getX8Drone();
            if (droneState.isOnGround()) {
                if (droneState.isCanFly()) {
                    setTvConnectState(R.string.x8_fly_status_can_fly);
                } else {
                    setTvConnectState(R.string.x8_fly_status_cannot_takeoff);
                }
                X8FcLogManager.getInstance().onDeviceStateChange(0);
                return;
            } else if (droneState.isTakeOffing()) {
                setTvConnectState(R.string.x8_fly_status_taking);
                return;
            } else if (droneState.isInSky()) {
                X8FcLogManager.getInstance().onDeviceStateChange(1);
                if (StateManager.getInstance().getX8Drone().getCtrlMode() == 7) {
                    setTvConnectState(R.string.x8_fly_status_returning);
                    return;
                } else {
                    setTvConnectState(R.string.x8_fly_status_flying);
                    return;
                }
            } else if (droneState.isLanding()) {
                setTvConnectState(R.string.x8_fly_status_landing);
                return;
            } else {
                return;
            }
        }
        setTvConnectState(R.string.x8_fly_status_connectiong);
    }

    public void showCamState(AutoCameraStateADV cameraStateADV) {
    }

    public void setTvConnectState(int resId) {
        this.tvConnectState.setText(resId);
    }

    public void showSingal(AutoFcSignalState signalState) {
        this.mX8MainTopCenterView.setFcSingal(signalState);
    }

    public void onBatteryListener(AutoFcBattery autoFcBattery) {
        this.mX8MainTopCenterView.setFcBattey(autoFcBattery);
        int percent = autoFcBattery.getRemainPercentage();
        this.mX8MainElectricView.setPercent(percent);
        this.mX8MainReturnTimeTextView.setStrTime(TimeFormateUtil.getRecordTime(autoFcBattery.getRemainingTime()));
        this.vLandingReturnView.setPercent(autoFcBattery.getLandingCapacity(), autoFcBattery.getRhtCapacity(), autoFcBattery.getTotalCapacity(), percent);
    }

    public void showSportState(AutoFcSportState state) {
        this.mIvDistance.setBackgroundResource(R.drawable.x8_main_fly_distance);
        this.mIvHight.setBackgroundResource(R.drawable.x8_main_fly_hight);
        this.mTvHight.setText(X8NumberUtil.getDistanceNumberNoPrexString(state.getHeight(), 1));
        this.mTvDistance.setText(X8NumberUtil.getDistanceNumberNoPrexString(state.getHomeDistance(), 1));
        this.mTvHightUnit.setText(X8NumberUtil.getPrexDistance());
        this.mTvDistanceUnit.setText(X8NumberUtil.getPrexDistance());
        this.mTvHs.setText(X8NumberUtil.getSpeedNumberNoPrexString(((float) state.getDownVelocity()) / 100.0f, 1));
        this.mTvVs.setText(X8NumberUtil.getSpeedNumberNoPrexString(((float) state.getGroupSpeed()) / 100.0f, 1));
        this.mTvSpeedUnit.setText(X8NumberUtil.getPrexSpeed());
        this.lastState = state;
    }

    public void switchUnity() {
        if (this.lastState != null) {
            showSportState(this.lastState);
        }
    }

    public void showRelayHeart(AutoRelayHeart autoRelayHeart) {
        this.mX8MainTopCenterView.setRelayHeart(autoRelayHeart);
    }

    public boolean onClickBackKey() {
        return false;
    }
}
