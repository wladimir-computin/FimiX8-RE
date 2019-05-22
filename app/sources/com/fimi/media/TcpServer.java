package com.fimi.media;

import com.fimi.kernel.animutils.IOUtils;
import com.fimi.kernel.connect.tcp.SocketOption;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Thread {
    private InputStream in = null;
    boolean isLoop;

    public TcpServer(Socket s) {
        try {
            this.in = s.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            log("client-stop");
        }
    }

    public void run() {
        if (this.in != null) {
            byte[] data = new byte[SocketOption.RECEIVE_BUFFER_SIZE];
            while (!this.isLoop) {
                try {
                    log("rev: " + new String(data, 0, this.in.read(data)));
                } catch (IOException e) {
                    e.printStackTrace();
                    this.isLoop = true;
                    if (this.in != null) {
                        try {
                            this.in.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(54321);
            log("server-start");
            while (true) {
                Socket s = ss.accept();
                log("client-connect");
                new TcpServer(s).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String t) {
        TcpServerLaunch.mHandler.obtainMessage(0, "" + t + IOUtils.LINE_SEPARATOR_UNIX).sendToTarget();
    }
}
