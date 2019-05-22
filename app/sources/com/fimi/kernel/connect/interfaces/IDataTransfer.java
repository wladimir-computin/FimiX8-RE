package com.fimi.kernel.connect.interfaces;

import com.fimi.kernel.connect.BaseCommand;

public interface IDataTransfer {
    void onSendTimeOut(int i, int i2, BaseCommand baseCommand);

    void sendRestransmissionData(BaseCommand baseCommand);
}
