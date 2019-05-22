package com.fimi.x8sdk.connect;

import com.fimi.host.CmdLogBack;
import com.fimi.kernel.connect.ResultListener;
import com.fimi.kernel.connect.interfaces.IRetransmissionHandle;
import com.fimi.kernel.connect.interfaces.IRetransmissionJsonHandle;
import com.fimi.kernel.connect.interfaces.IRetransmissionUsbHandle;
import com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle;
import com.fimi.x8sdk.connect.datatype.FmLinkDataChanel;
import com.fimi.x8sdk.connect.datatype.FwUploadDataChanel;
import com.fimi.x8sdk.connect.datatype.JsonDataChanel;
import com.fimi.x8sdk.connect.datatype.MediaDataChanel;
import com.fimi.x8sdk.connect.datatype.VideoDataChanel;

public class DataChanel implements ResultListener {
    FmLinkDataChanel fmLinkDataChanel = new FmLinkDataChanel();
    FwUploadDataChanel fwUploadDataChanel = new FwUploadDataChanel();
    JsonDataChanel jsonDataChanel = new JsonDataChanel();
    MediaDataChanel mediaDataChanel = new MediaDataChanel();
    VideoDataChanel videoDataChanel = new VideoDataChanel();

    public void messageReceived(byte[] buffer) {
        if (buffer.length > 5) {
            if ((buffer[2] & 15) != 0) {
            }
            int type = buffer[3];
            int dataLen = buffer.length - 5;
            byte[] data = new byte[dataLen];
            System.arraycopy(buffer, 5, data, 0, dataLen);
            if (type != 2) {
                log(buffer);
            }
            switch (type) {
                case 0:
                    this.fmLinkDataChanel.forwardData(data);
                    return;
                case 1:
                    this.jsonDataChanel.forwardData(data);
                    return;
                case 2:
                    this.videoDataChanel.forwardData(data);
                    return;
                case 4:
                    this.mediaDataChanel.forwardData(data);
                    return;
                case 5:
                    this.fmLinkDataChanel.forwardData(data);
                    return;
                case 6:
                    this.fwUploadDataChanel.forwardData(data);
                    return;
                default:
                    return;
            }
        }
    }

    public void log(byte[] bytes) {
        CmdLogBack.getInstance().writeLog(bytes, false);
    }

    public void setRetransmissionHandle(IRetransmissionHandle handle) {
        this.fmLinkDataChanel.setRetransmissionHandle(handle);
    }

    public void setTimerSendQueueHandle(ITimerSendQueueHandle handle) {
        this.fmLinkDataChanel.setTimerSendQueueHandle(handle);
    }

    public void setRetransmissionJsonHandle(IRetransmissionJsonHandle handle) {
        this.jsonDataChanel.setRetransmissionHandle(handle);
    }

    public void setRetransmissionUsbHandle(IRetransmissionUsbHandle handle) {
        this.fwUploadDataChanel.setRetransmissionHandle(handle);
        this.mediaDataChanel.setRetransmissionUsbHandle(handle);
    }

    public boolean isAppRequestCmd(int groupId, int msgId) {
        return false;
    }
}
