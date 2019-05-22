package com.fimi.x8sdk.modulestate;

import com.fimi.kernel.utils.BitUtil;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.dataparser.AutoRelayHeart;

public class RelayState extends BaseState {
    private long lastRlHeartTime;
    private AutoRelayHeart relayHeart;
    RelayState relayState;

    public void updateLastRlHeartTime() {
        this.lastRlHeartTime = System.currentTimeMillis();
    }

    public boolean isRlTimeOut() {
        return System.currentTimeMillis() - this.lastRlHeartTime >= 1500;
    }

    public boolean isAvailable() {
        return true;
    }

    public RelayState getRelayState() {
        return this.relayState;
    }

    public void setRelayState(RelayState relayState) {
        this.relayState = relayState;
    }

    public AckVersion getRelayVersion() {
        return StateManager.getInstance().getVersionState().getModuleRepeaterRcVersion();
    }

    public boolean isConnect() {
        AckVersion version = StateManager.getInstance().getVersionState().getModuleRepeaterRcVersion();
        if (version != null && version.getSoftVersion() > 0) {
            return true;
        }
        return false;
    }

    public void setRelayHeart(AutoRelayHeart relayHeart) {
        this.relayHeart = relayHeart;
    }

    public AutoRelayHeart getRelayHeart() {
        return this.relayHeart;
    }

    public int getApModel() {
        return this.relayHeart == null ? 0 : BitUtil.getBitByByte(this.relayHeart.getStatus(), 10);
    }
}
