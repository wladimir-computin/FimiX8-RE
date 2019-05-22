package com.fimi.x8sdk.modulestate;

import com.fimi.x8sdk.dataparser.AckNoFlyNormal;

public class NfzState extends BaseState {
    AckNoFlyNormal mAckNoFlyNormal;

    public boolean isAvailable() {
        return false;
    }

    public void setAckNoFlyNormal(AckNoFlyNormal mAckNoFlyNormal) {
        this.mAckNoFlyNormal = mAckNoFlyNormal;
    }

    public AckNoFlyNormal getAckNoFlyNormal() {
        return this.mAckNoFlyNormal;
    }
}
