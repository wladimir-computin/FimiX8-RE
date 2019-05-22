package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.UpdateCurrentProgressEntity;
import java.util.ArrayList;
import java.util.List;

public class AckUpdateCurrentProgress extends X8BaseMessage {
    int deviceNumber;
    List<UpdateCurrentProgressEntity> updateCurrentProgressEntitys = new ArrayList();

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.deviceNumber = packet.getPayLoad4().getByte();
        if (this.deviceNumber >= 0) {
            this.updateCurrentProgressEntitys.clear();
            for (int i = 0; i < this.deviceNumber; i++) {
                UpdateCurrentProgressEntity updateCurrentProgressEntity = new UpdateCurrentProgressEntity();
                byte devTargetID = packet.getPayLoad4().getByte();
                byte devModuleID = packet.getPayLoad4().getByte();
                byte msgModuleID = packet.getPayLoad4().getByte();
                byte stage = packet.getPayLoad4().getByte();
                byte schedule = packet.getPayLoad4().getByte();
                short result = packet.getPayLoad4().getShort();
                updateCurrentProgressEntity.setDevTargetID(devTargetID);
                updateCurrentProgressEntity.setDevModuleID(devModuleID);
                updateCurrentProgressEntity.setMsgModuleID(msgModuleID);
                updateCurrentProgressEntity.setStage(stage);
                updateCurrentProgressEntity.setSchedule(schedule);
                updateCurrentProgressEntity.setResult(result);
                this.updateCurrentProgressEntitys.add(updateCurrentProgressEntity);
            }
        }
    }

    public int getDeviceNumber() {
        return this.deviceNumber;
    }

    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public List<UpdateCurrentProgressEntity> getUpdateCurrentProgressEntitys() {
        return this.updateCurrentProgressEntitys;
    }

    public void setUpdateCurrentProgressEntitys(List<UpdateCurrentProgressEntity> updateCurrentProgressEntitys) {
        this.updateCurrentProgressEntitys = updateCurrentProgressEntitys;
    }

    public String toString() {
        StringBuffer updateCurrentProgressEntityStr = new StringBuffer();
        for (UpdateCurrentProgressEntity updateCurrentProgressEntity : this.updateCurrentProgressEntitys) {
            updateCurrentProgressEntityStr.append("updateCurrentProgressEntity:" + updateCurrentProgressEntity.toString());
        }
        return "AckUpdateCurrentProgress{deviceNumber=" + this.deviceNumber + ", updateCurrentProgressEntitys=" + updateCurrentProgressEntityStr.toString() + CoreConstants.CURLY_RIGHT;
    }
}
