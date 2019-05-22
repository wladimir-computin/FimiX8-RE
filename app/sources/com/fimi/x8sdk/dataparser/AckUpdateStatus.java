package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.CurUpdateFwEntity;
import java.util.ArrayList;
import java.util.List;

public class AckUpdateStatus extends X8BaseMessage {
    int devModuleId;
    int devTargetId;
    int deviceNumber;
    List<CurUpdateFwEntity> entityList = new ArrayList();
    int msgModuleId;
    int result;
    int schedule;
    int state;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.deviceNumber = packet.getPayLoad4().getByte();
        if (this.deviceNumber > 0) {
            this.entityList.clear();
            for (int i = 0; i < this.deviceNumber; i++) {
                this.devTargetId = packet.getPayLoad4().getByte();
                this.devModuleId = packet.getPayLoad4().getByte();
                this.msgModuleId = packet.getPayLoad4().getByte();
                this.state = packet.getPayLoad4().getByte();
                this.schedule = packet.getPayLoad4().getByte();
                this.result = packet.getPayLoad4().getByte();
                CurUpdateFwEntity updateFwEntity = new CurUpdateFwEntity();
                updateFwEntity.setDevTargetId(this.devTargetId);
                updateFwEntity.setDevModuleId(this.devModuleId);
                updateFwEntity.setResult(this.result);
                updateFwEntity.setSchedule(this.schedule);
                updateFwEntity.setState(this.state);
                updateFwEntity.setMsgModuleId(this.msgModuleId);
                this.entityList.add(updateFwEntity);
            }
        }
    }

    public int getDeviceNumber() {
        return this.deviceNumber;
    }

    public List<CurUpdateFwEntity> getEntityList() {
        return this.entityList;
    }
}
