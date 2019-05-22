package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.cmdsenum.X8FpvSignalState;
import com.fimi.x8sdk.modulestate.StateManager;

public class AutoRelayHeart extends X8BaseMessage {
    int channel;
    short status;

    public void unPacket(LinkPacket4 packet) {
        float distance;
        super.decrypt(packet);
        this.status = packet.getPayLoad4().getShort();
        if (StateManager.getInstance().getX8Drone() == null || StateManager.getInstance().getX8Drone().getFcSportState() == null) {
            distance = 0.0f;
        } else {
            distance = StateManager.getInstance().getX8Drone().getFcSportState().getHomeDistance();
        }
        X8FcLogManager.getInstance().cmLogWrite(getPayloadData(packet), System.currentTimeMillis(), distance);
    }

    public int getChannel() {
        return this.channel;
    }

    public int getTxPower() {
        return (this.status >> 11) & 1;
    }

    public int getImageTranmission() {
        return (this.status >> 3) & 127;
    }

    public X8FpvSignalState getX8FpvSignalState() {
        int imageTranmission = getImageTranmission();
        if (imageTranmission > 45) {
            return X8FpvSignalState.STRONG;
        }
        if (imageTranmission > 45 || imageTranmission < 20) {
            return X8FpvSignalState.LOW;
        }
        return X8FpvSignalState.MIDDLE;
    }

    public short getStatus() {
        return this.status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String toString() {
        return "AutoRelayHeart{status=" + this.status + CoreConstants.CURLY_RIGHT;
    }
}
