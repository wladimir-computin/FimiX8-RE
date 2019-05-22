package com.fimi.x8sdk.listener;

import com.fimi.x8sdk.entity.ConectState;

public interface ConnectListener {
    void onConnectedState(ConectState conectState);
}
