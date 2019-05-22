package com.fimi.app.x8s.controls.fcsettting;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.X8DroneInfoStateAdapter;
import com.fimi.app.x8s.adapter.X8DroneInfoStateAdapter.OnEventClickListener;
import com.fimi.app.x8s.entity.X8DroneInfoState;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8DroneStateListener;
import com.fimi.app.x8s.manager.X8DroneInfoStatemManager;
import com.fimi.x8sdk.modulestate.StateManager;
import java.util.ArrayList;
import java.util.List;

public class X8DroneInfoStateController extends AbsX8MenuBoxControllers implements OnEventClickListener {
    private X8DroneInfoStateAdapter adapter;
    List<X8DroneInfoState> list;
    private IX8DroneStateListener listener;
    private View parentView;

    public enum Mode {
        GPS,
        CAMP,
        MAGNETIC,
        IMU,
        BATTERY,
        GIMBAL
    }

    public enum State {
        NA,
        NORMAL,
        MIDDLE,
        ERROR
    }

    public IX8DroneStateListener getListener() {
        return this.listener;
    }

    public void setListener(IX8DroneStateListener listener) {
        this.listener = listener;
    }

    public X8DroneInfoStateController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.list = new ArrayList();
        this.parentView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_all_setting_drone_info_state, (ViewGroup) rootView, true);
        RecyclerView recyclerView = (RecyclerView) this.parentView.findViewById(R.id.ryv_drone_state);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        Resources res = rootView.getContext().getResources();
        String[] states = res.getStringArray(R.array.x8_drone_info_state_array);
        for (int i = 0; i < states.length; i++) {
            X8DroneInfoState state = new X8DroneInfoState();
            state.setName(states[i]);
            state.setState(State.NA);
            state.setMode(getMode(i));
            state.setInfo(res.getString(R.string.x8_na));
            state.setErrorEvent(getEvent(state.getMode()));
            this.list.add(state);
        }
        this.adapter = new X8DroneInfoStateAdapter(this.list);
        this.adapter.setOnEventClickListener(this);
        recyclerView.setAdapter(this.adapter);
        initActions();
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public Mode getMode(int index) {
        Mode mode = Mode.GPS;
        switch (index) {
            case 0:
                return Mode.GPS;
            case 1:
                return Mode.CAMP;
            case 2:
                return Mode.MAGNETIC;
            case 3:
                return Mode.IMU;
            case 4:
                return Mode.BATTERY;
            case 5:
                return Mode.GIMBAL;
            default:
                return mode;
        }
    }

    public boolean checkError(Mode mode) {
        switch (mode) {
            case GPS:
                return X8DroneInfoStatemManager.isGpsError();
            case CAMP:
                return X8DroneInfoStatemManager.isCompassError();
            case MAGNETIC:
                return StateManager.getInstance().getErrorCodeState().isMagneticError();
            case IMU:
                return X8DroneInfoStatemManager.isImuError();
            case BATTERY:
                return X8DroneInfoStatemManager.isBatteryError();
            case GIMBAL:
                return X8DroneInfoStatemManager.isGcError();
            default:
                return false;
        }
    }

    public String getInfo(Mode mode) {
        String s = "";
        switch (mode) {
            case GPS:
                return getString(R.string.x8_fc_state_exception);
            case CAMP:
                return getString(R.string.x8_fc_state_exception);
            case MAGNETIC:
                int magnetic = StateManager.getInstance().getX8Drone().getFcSingal().getMagnetic();
                if (magnetic >= 0 && magnetic <= 20) {
                    return getString(R.string.x8_fc_item_magnetic_field_error1);
                }
                if (magnetic < 21 || magnetic > 40) {
                    return getString(R.string.x8_fc_item_magnetic_field_error3);
                }
                return getString(R.string.x8_fc_item_magnetic_field_error2);
            case IMU:
                return getString(R.string.x8_fc_state_exception);
            case BATTERY:
                return getString(R.string.x8_fc_state_exception);
            case GIMBAL:
                return getString(R.string.x8_fc_state_exception);
            default:
                return s;
        }
    }

    public int getEvent(Mode mode) {
        switch (mode) {
            case CAMP:
                return 1;
            default:
                return 0;
        }
    }

    public void onItemClick(int index, X8DroneInfoState obj) {
        switch (obj.getMode()) {
            case CAMP:
                this.listener.onCalibrationItemClick();
                return;
            default:
                return;
        }
    }

    public void onDroneConnected(boolean b) {
        if (b) {
            for (X8DroneInfoState state : this.list) {
                if (checkError(state.getMode())) {
                    state.setInfo(getInfo(state.getMode()));
                    state.setState(State.ERROR);
                } else {
                    String s = getString(R.string.x8_fc_state_normal);
                    State st = State.NORMAL;
                    if (Mode.MAGNETIC == state.getMode()) {
                        int magnetic = StateManager.getInstance().getX8Drone().getFcSingal().getMagnetic();
                        if (magnetic >= 0 && magnetic <= 20) {
                            s = getString(R.string.x8_fc_item_magnetic_field_error1);
                        } else if (magnetic < 21 || magnetic > 40) {
                            s = getString(R.string.x8_fc_item_magnetic_field_error3);
                            st = State.ERROR;
                        } else {
                            s = getString(R.string.x8_fc_item_magnetic_field_error2);
                            st = State.MIDDLE;
                        }
                    }
                    state.setInfo(s);
                    state.setState(st);
                    this.adapter.notifyDataSetChanged();
                }
                this.adapter.notifyDataSetChanged();
            }
            return;
        }
        for (X8DroneInfoState state2 : this.list) {
            state2.setState(State.NA);
            state2.setInfo(getString(R.string.x8_na));
            this.adapter.notifyDataSetChanged();
        }
    }
}
