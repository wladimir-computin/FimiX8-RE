package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8MainTopBarListener;
import com.fimi.x8sdk.cmdsenum.X8FpvSignalState;
import com.fimi.x8sdk.cmdsenum.X8GpsNumState;
import com.fimi.x8sdk.cmdsenum.X8HandleSignalState;
import com.fimi.x8sdk.dataparser.AutoFcBattery;
import com.fimi.x8sdk.dataparser.AutoFcSignalState;
import com.fimi.x8sdk.dataparser.AutoRelayHeart;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8MainTopRightView extends RelativeLayout implements OnClickListener {
    private static final int IMAGE_TRANMISSION_LOW = 20;
    private static final int IMAGE_TRANMISSION_MID = 45;
    private int hightElectric = 0;
    private IX8MainTopBarListener listener;
    private int lowElectric = 0;
    private X8HandleSignalState mHandleSignalState = X8HandleSignalState.IDEL;
    private ImageView mIvBattery;
    private ImageView mIvFpvSignal;
    private ImageView mIvGps;
    private ImageView mIvHandleSignal;
    private TextView mIvMode;
    private X8FpvSignalState mX8FpvSignalState = X8FpvSignalState.IDEL;
    private X8GpsNumState mX8GpsNumState = X8GpsNumState.IDEL;
    private int middleElectric = 0;
    X8MiLantingStrokeTextView tvPercentCapacty;
    X8MiLantingStrokeTextView tvVoltage;

    public X8MainTopRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.x8_main_top_right, this, true);
        this.mIvMode = (TextView) findViewById(R.id.x8_iv_top_mode);
        this.mIvGps = (ImageView) findViewById(R.id.x8_iv_top_gps);
        this.mIvFpvSignal = (ImageView) findViewById(R.id.x8_iv_top_fpv_signal);
        this.mIvHandleSignal = (ImageView) findViewById(R.id.x8_iv_top_handle_signal);
        this.mIvBattery = (ImageView) findViewById(R.id.x8_iv_top_battery);
        this.tvVoltage = (X8MiLantingStrokeTextView) findViewById(R.id.x8_tv_top_battery);
        this.tvPercentCapacty = (X8MiLantingStrokeTextView) findViewById(R.id.x8_tv_top_battery_percent);
        this.mIvGps.setOnClickListener(this);
        this.mIvFpvSignal.setOnClickListener(this);
        this.mIvHandleSignal.setOnClickListener(this);
        this.mIvBattery.setOnClickListener(this);
        this.mIvMode.setOnClickListener(this);
        this.lowElectric = context.getResources().getColor(R.color.x8_battery_state_serious);
        this.middleElectric = context.getResources().getColor(R.color.x8_battery_state_abnormal);
        this.hightElectric = context.getResources().getColor(R.color.x8_battery_state_normal);
        this.tvPercentCapacty.setTextColor(this.hightElectric);
    }

    public void defaultVal() {
        this.mHandleSignalState = X8HandleSignalState.IDEL;
        this.mX8GpsNumState = X8GpsNumState.IDEL;
        this.mX8FpvSignalState = X8FpvSignalState.IDEL;
        this.mIvMode.setText(R.string.x8_na);
        this.tvVoltage.setText(R.string.x8_na);
        this.tvPercentCapacty.setTextColor(this.hightElectric);
        this.tvPercentCapacty.setText(R.string.x8_na);
        this.mIvHandleSignal.setImageResource(R.drawable.x8_main_handle_signal_unconnect);
        this.mIvFpvSignal.setImageResource(R.drawable.x8_main_fpv_signal_unconnect);
        this.mIvBattery.setImageResource(R.drawable.x8_main_battery_unconnect);
        this.mIvGps.setImageResource(R.drawable.x8_main_gps_single_unconnect);
    }

    public void onDisconnectDroneVal() {
        this.mIvMode.setText(R.string.x8_na);
        this.tvVoltage.setText(R.string.x8_na);
        this.tvPercentCapacty.setTextColor(this.hightElectric);
        this.tvPercentCapacty.setText(R.string.x8_na);
        this.mIvHandleSignal.setImageResource(R.drawable.x8_main_handle_signal_unconnect);
        this.mIvBattery.setImageResource(R.drawable.x8_main_battery_unconnect);
        this.mIvGps.setImageResource(R.drawable.x8_main_gps_single_unconnect);
    }

    public void setFcSingal(AutoFcSignalState signalState) {
        if (this.mHandleSignalState != signalState.getX8HandleSignalState()) {
            this.mHandleSignalState = signalState.getX8HandleSignalState();
            switch (this.mHandleSignalState) {
                case STRONG:
                    this.mIvHandleSignal.setImageResource(R.drawable.x8_main_handle_signal_normal);
                    break;
                case MIDDLE:
                    this.mIvHandleSignal.setImageResource(R.drawable.x8_main_handlesignal_middle);
                    break;
                case LOW:
                    this.mIvHandleSignal.setImageResource(R.drawable.x8_main_handlesignal_low);
                    break;
                default:
                    this.mIvHandleSignal.setImageResource(R.drawable.x8_main_handle_signal_active);
                    break;
            }
        }
        this.mIvMode.setText(signalState.getSatelliteNumber() + "");
        if (this.mX8GpsNumState != signalState.getSatelliteState()) {
            this.mX8GpsNumState = signalState.getSatelliteState();
            switch (this.mX8GpsNumState) {
                case STRONG:
                    this.mIvGps.setImageResource(R.drawable.x8_main_gps_single_nomal);
                    return;
                case MIDDLE:
                    this.mIvGps.setImageResource(R.drawable.x8_main_gps_single_middle);
                    return;
                case LOW:
                    this.mIvGps.setImageResource(R.drawable.x8_main_gps_single_weakness);
                    return;
                default:
                    return;
            }
        }
    }

    public void setRelayHeart(AutoRelayHeart autoRelayHeart) {
        if (autoRelayHeart.getX8FpvSignalState() != this.mX8FpvSignalState) {
            this.mX8FpvSignalState = autoRelayHeart.getX8FpvSignalState();
            switch (this.mX8FpvSignalState) {
                case STRONG:
                    this.mIvFpvSignal.setImageResource(R.drawable.x8_main_fpv_signal_normal);
                    return;
                case MIDDLE:
                    this.mIvFpvSignal.setImageResource(R.drawable.x8_main_fpv_signal_middle);
                    return;
                case LOW:
                    this.mIvFpvSignal.setImageResource(R.drawable.x8_main_fpv_signal_low);
                    return;
                default:
                    return;
            }
        }
    }

    public void setFcBattey(AutoFcBattery battey) {
        int type;
        int percent = battey.getRemainPercentage();
        int v = StateManager.getInstance().getX8Drone().getLowPowerValue();
        int imgColor = 0;
        if (percent < 15) {
            this.tvPercentCapacty.setTextColor(this.lowElectric);
        } else if (percent < 15 || percent >= v) {
            imgColor = 2;
            this.tvPercentCapacty.setTextColor(this.hightElectric);
        } else {
            imgColor = 1;
            this.tvPercentCapacty.setTextColor(this.middleElectric);
        }
        this.tvVoltage.setText(battey.getVoltage() + "V");
        this.tvPercentCapacty.setText(percent + "%");
        if (percent >= 66) {
            type = 3;
        } else if (percent >= 33 && percent < 66) {
            type = 2;
        } else if (percent < 15 || percent >= 33) {
            type = 0;
        } else {
            type = 1;
        }
        if (type == 0) {
            this.mIvBattery.setImageResource(R.drawable.x8_main_battery_low);
        } else if (type == 1) {
            if (imgColor == 2) {
                this.mIvBattery.setImageResource(R.drawable.x8_main_battery1);
            } else if (imgColor == 1) {
                this.mIvBattery.setImageResource(R.drawable.x8_main_battery_yellow1);
            }
        } else if (type == 2) {
            if (imgColor == 2) {
                this.mIvBattery.setImageResource(R.drawable.x8_main_battery2);
            } else if (imgColor == 1) {
                this.mIvBattery.setImageResource(R.drawable.x8_main_battery_yellow2);
            }
        } else if (type == 3) {
            this.mIvBattery.setImageResource(R.drawable.x8_main_battery_ful);
        }
    }

    public void onClick(View v) {
        if (this.listener != null) {
            int id = v.getId();
            if (id == R.id.x8_iv_top_gps) {
                this.listener.onGpsClick();
            } else if (id == R.id.x8_iv_top_fpv_signal) {
                this.listener.onFpvClick();
            } else if (id == R.id.x8_iv_top_handle_signal) {
                this.listener.onRcClick();
            } else if (id == R.id.x8_iv_top_battery) {
                this.listener.onBatteryClick();
            } else if (id == R.id.x8_iv_top_mode) {
                this.listener.onModelClick();
            }
        }
    }

    public void setListener(IX8MainTopBarListener listener) {
        this.listener = listener;
    }
}
