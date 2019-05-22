package com.fimi.kernel.connect.interfaces;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;

public interface IPersonalDataCallBack {
    void onPersonalDataCallBack(int i, int i2, ILinkMessage iLinkMessage);

    void onPersonalSendTimeOut(int i, int i2, BaseCommand baseCommand);
}
