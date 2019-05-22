package com.fimi.kernel.connect.filter;

import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

public class ProtocolObjFilter extends IoFilterAdapter {
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
    }
}
