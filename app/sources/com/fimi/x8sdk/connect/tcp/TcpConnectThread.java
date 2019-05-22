package com.fimi.x8sdk.connect.tcp;

import android.content.Context;
import com.fimi.kernel.connect.SocketOption;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.connect.tcp.TcpConnect;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.connect.DataChanel;
import com.fimi.x8sdk.connect.IConnectHandler;

public class TcpConnectThread extends Thread implements IConnectHandler {
    DataChanel filterAdapter = new DataChanel();
    SocketOption option = new SocketOption();
    TcpConnect tcpConnect;

    public TcpConnectThread(Context ctx) {
        this.option.setHost(Constants.A12_TCP_CMD_HOST);
        this.option.setPort(Constants.A12_TCP_CMD_PORT);
        start();
    }

    public void run() {
        super.run();
        this.tcpConnect = new TcpConnect(this.option, this.filterAdapter);
        this.tcpConnect.startSession();
        SessionManager.getInstance().addSession(this.tcpConnect);
    }

    public void exit() {
        interrupt();
        this.tcpConnect.closeSession();
    }
}
