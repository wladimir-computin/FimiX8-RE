package com.fimi.kernel.connect.session;

import com.fimi.kernel.connect.model.UpdateDateMessage;

public interface UpdateDateListener {
    void onUpdateDateCallBack(UpdateDateMessage updateDateMessage);
}
