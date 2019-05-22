package com.fimi.x8sdk.dataparser;

import ch.qos.logback.core.CoreConstants;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.twitter.sdk.android.core.internal.scribe.EventsFilesManager;

public class AckVersion extends X8BaseMessage {
    int hardwareVersion;
    int idA;
    int idB;
    int idC;
    int idD;
    int model = -1;
    int reserved1;
    int reserved2;
    int softVersion = -1;
    int type;
    String versionDetails;

    public String getVersionDetails() {
        return this.versionDetails;
    }

    public void setVersionDetails(String versionDetails) {
        this.versionDetails = versionDetails;
    }

    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        int verLen = packet.getPayLoad4().getPayloadData().length;
        if (verLen > 26) {
            this.type = packet.getPayLoad4().getByte();
            this.model = packet.getPayLoad4().getByte();
            toModelID(this.type);
            this.reserved1 = packet.getPayLoad4().getByte();
            this.reserved2 = packet.getPayLoad4().getByte();
            this.hardwareVersion = packet.getPayLoad4().getShort();
            this.softVersion = packet.getPayLoad4().getShort();
            this.idA = packet.getPayLoad4().getInt();
            this.idB = packet.getPayLoad4().getInt();
            this.idC = packet.getPayLoad4().getInt();
            this.idD = packet.getPayLoad4().getInt();
            int index = packet.getPayLoad4().index;
            byte[] versionDetl = new byte[(verLen - index)];
            System.arraycopy(packet.getPayLoad4().getPayloadData(), index, versionDetl, 0, verLen - index);
            this.versionDetails = new String(versionDetl);
            return;
        }
        this.type = packet.getPayLoad4().getByte();
        this.model = packet.getPayLoad4().getByte();
        toModelID(this.type);
        this.hardwareVersion = packet.getPayLoad4().getShort();
        this.softVersion = packet.getPayLoad4().getShort();
        this.idA = packet.getPayLoad4().getInt();
        this.idB = packet.getPayLoad4().getInt();
        this.idC = packet.getPayLoad4().getInt();
        this.idD = packet.getPayLoad4().getInt();
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getHardwareVersion() {
        return this.hardwareVersion;
    }

    public void setHardwareVersion(int hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public int getSoftVersion() {
        return this.softVersion;
    }

    public void setSoftVersion(int softVersion) {
        this.softVersion = softVersion;
    }

    public int getIdA() {
        return this.idA;
    }

    public void setIdA(int idA) {
        this.idA = idA;
    }

    public int getIdB() {
        return this.idB;
    }

    public void setIdB(int idB) {
        this.idB = idB;
    }

    public int getIdC() {
        return this.idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public int getIdD() {
        return this.idD;
    }

    public void setIdD(int idD) {
        this.idD = idD;
    }

    public int getReserved1() {
        return this.reserved1;
    }

    public void setReserved1(int reserved1) {
        this.reserved1 = reserved1;
    }

    public int getReserved2() {
        return this.reserved2;
    }

    public void setReserved2(int reserved2) {
        this.reserved2 = reserved2;
    }

    private void toModelID(int type) {
        if (type == 0) {
            this.model = 3;
        } else if (type == 1) {
            this.model = 3;
        } else if (type == 9) {
            this.model = 1;
        } else if (type == 11) {
            this.model = 3;
        } else if (type == 12) {
            this.model = 3;
        } else if (type == 14) {
            this.model = 0;
        } else if (type == 3) {
            this.model = 6;
        } else if (type == 5) {
            this.model = 3;
        } else if (type == 10) {
            this.model = 3;
        } else if (type == 4) {
            this.model = 2;
        } else if (type == 13) {
            this.model = 1;
        } else {
            this.model = -1;
        }
    }

    public String toString() {
        return "AckVersion{type=" + this.type + ", model=" + this.model + ", reserved1=" + this.reserved1 + ", reserved2=" + this.reserved2 + ", hardwareVersion=" + this.hardwareVersion + ", softVersion=" + this.softVersion + ", idA=" + this.idA + ", idB=" + this.idB + ", idC=" + this.idC + ", idD=" + this.idD + ", versionDetails='" + this.versionDetails + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }

    public String getShowDetails() {
        String dt = getVersionDetails();
        if (dt == null) {
            return "";
        }
        String[] split = dt.split(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
        if (split.length > 0) {
            return split[split.length - 1];
        }
        return dt;
    }
}
