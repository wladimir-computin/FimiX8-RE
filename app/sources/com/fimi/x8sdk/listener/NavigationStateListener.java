package com.fimi.x8sdk.listener;

import com.fimi.x8sdk.modulestate.DroneState;

public interface NavigationStateListener {
    void onNavigationState(DroneState droneState);
}
