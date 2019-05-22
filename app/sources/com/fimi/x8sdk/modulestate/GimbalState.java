package com.fimi.x8sdk.modulestate;

import ch.qos.logback.core.CoreConstants;
import com.fimi.x8sdk.dataparser.AutoGimbalState;

public class GimbalState extends BaseState {
    public int errorCode;
    public int pitchAngle;
    public int rollAngle;
    public int stateCode;
    public int yawAnagle;

    public int getErrorCode() {
        return this.errorCode;
    }

    public int getStateCode() {
        return this.stateCode;
    }

    public int getRollAngle() {
        return this.rollAngle;
    }

    public int getPitchAngle() {
        return this.pitchAngle;
    }

    public int getYawAnagle() {
        return this.yawAnagle;
    }

    public boolean isAvailable() {
        return false;
    }

    public void setAutoGimbalState(AutoGimbalState state) {
        this.errorCode = state.getErrorCode();
        this.stateCode = state.getStateCode();
        this.rollAngle = state.getRollAngle();
        this.pitchAngle = state.getPitchAngle();
        this.yawAnagle = state.getYawAnagle();
    }

    public String toString() {
        return "GimbalState{errorCode=" + this.errorCode + ", stateCode=" + this.stateCode + ", rollAngle=" + this.rollAngle + ", pitchAngle=" + this.pitchAngle + ", yawAnagle=" + this.yawAnagle + CoreConstants.CURLY_RIGHT;
    }
}
