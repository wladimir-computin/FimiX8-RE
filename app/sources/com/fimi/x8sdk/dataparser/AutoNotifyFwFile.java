package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.FwType;
import java.util.ArrayList;
import java.util.List;

public class AutoNotifyFwFile extends X8BaseMessage {
    int devModuleId;
    int devTargetId;
    int fwNumber;
    List<FwType> fwTypeList = new ArrayList();
    int msgModuleId;
    int notifyType;
    int reserve;
    int result;
    int schedule;
    int stage;

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.notifyType = packet.getPayLoad4().getByte();
        switch (this.notifyType) {
            case 0:
                this.result = packet.getPayLoad4().getShort();
                return;
            case 1:
                this.result = packet.getPayLoad4().getShort();
                return;
            case 2:
                this.fwNumber = packet.getPayLoad4().getByte();
                if (this.fwNumber >= 0) {
                    this.fwTypeList.clear();
                    for (int i = 0; i < this.fwNumber; i++) {
                        FwType fwType = new FwType();
                        byte devTargetId = packet.getPayLoad4().getByte();
                        byte devModuleId = packet.getPayLoad4().getByte();
                        byte moduleId = packet.getPayLoad4().getByte();
                        fwType.setDevMouduleId(devModuleId);
                        fwType.setDevTargetId(devTargetId);
                        fwType.setMsgModuleId(moduleId);
                        this.fwTypeList.add(fwType);
                    }
                    return;
                }
                return;
            case 3:
                this.devTargetId = packet.getPayLoad4().getByte();
                this.devModuleId = packet.getPayLoad4().getByte();
                this.msgModuleId = packet.getPayLoad4().getByte();
                this.stage = packet.getPayLoad4().getByte();
                this.schedule = packet.getPayLoad4().getByte();
                return;
            case 4:
                this.devTargetId = packet.getPayLoad4().getByte();
                this.devModuleId = packet.getPayLoad4().getByte();
                this.msgModuleId = packet.getPayLoad4().getByte();
                this.stage = packet.getPayLoad4().getByte();
                this.result = packet.getPayLoad4().getShort();
                return;
            case 5:
                this.devTargetId = packet.getPayLoad4().getByte();
                this.devModuleId = packet.getPayLoad4().getByte();
                this.msgModuleId = packet.getPayLoad4().getByte();
                this.stage = packet.getPayLoad4().getByte();
                this.reserve = packet.getPayLoad4().getByte();
                return;
            case 6:
                this.result = packet.getPayLoad4().getShort();
                return;
            default:
                return;
        }
    }

    public int getNotifyType() {
        return this.notifyType;
    }

    public int getResult() {
        return this.result;
    }

    public int getFwNumber() {
        return this.fwNumber;
    }

    public void setFwNumber(int fwNumber) {
        this.fwNumber = fwNumber;
    }

    public int getDevTargetId() {
        return this.devTargetId;
    }

    public int getDevModuleId() {
        return this.devModuleId;
    }

    public int getMsgModuleId() {
        return this.msgModuleId;
    }

    public int getStage() {
        return this.stage;
    }

    public int getSchedule() {
        return this.schedule;
    }

    public int getReserve() {
        return this.reserve;
    }

    public List<FwType> getFwTypeList() {
        return this.fwTypeList;
    }

    public String toString() {
        return "AutoNotifyFwFile{notifyType=" + this.notifyType + ", result=" + this.result + ", fwNumber=" + this.fwNumber + ", devTargetId=" + this.devTargetId + ", devModuleId=" + this.devModuleId + ", msgModuleId=" + this.msgModuleId + ", stage=" + this.stage + ", schedule=" + this.schedule + ", reserve=" + this.reserve + CoreConstants.CURLY_RIGHT;
    }
}
