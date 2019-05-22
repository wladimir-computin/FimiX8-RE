package com.fimi.kernel.dataparser;

import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

public abstract class BaseIoFilterAdapter extends IoFilterAdapter {
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        super.messageReceived(nextFilter, session, message);
    }
}
