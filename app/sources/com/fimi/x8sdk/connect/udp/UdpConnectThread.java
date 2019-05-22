package com.fimi.x8sdk.connect.udp;

import android.content.Context;
import com.fimi.app.x8s.entity.X11CmdConstants;
import com.fimi.kernel.connect.SocketOption;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.connect.udp.UdpConnect;
import com.fimi.x8sdk.connect.DataChanel;
import com.fimi.x8sdk.connect.IConnectHandler;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpConnectThread extends Thread implements IConnectHandler {
    boolean isExit;
    Context mContext;
    SocketOption option = new SocketOption();

    public UdpConnectThread(Context mContext) {
        this.mContext = mContext;
        this.option.setHost("192.168.40.2");
        this.option.setPort(X11CmdConstants.X11_CMD_TCP_PORT);
        start();
    }

    public void exit() {
        this.isExit = true;
        interrupt();
    }

    public void run() {
        super.run();
        while (!this.isExit) {
            try {
                if (!SessionManager.getInstance().hasSession()) {
                    try {
                        UdpConnect udpConnect = new UdpConnect(new DatagramSocket(), this.option, new DataChanel());
                        udpConnect.startSession();
                        SessionManager.getInstance().addSession(udpConnect);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SocketException e2) {
                e2.printStackTrace();
            }
        }
    }
}
