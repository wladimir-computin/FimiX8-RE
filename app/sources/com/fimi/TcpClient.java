package com.fimi;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

public class TcpClient extends Thread {
    private static TcpClient instance;
    private static boolean isSetUpTcpClient = true;
    public LinkedBlockingDeque<String> cmdQuene = new LinkedBlockingDeque();
    boolean isLoop = false;
    boolean isWait = false;
    OutputStream out = null;
    Socket s = null;

    public static void createInit() {
        instance = new TcpClient();
        if (isSetUpTcpClient) {
            instance.start();
        }
    }

    public static TcpClient getIntance() {
        return instance;
    }

    public void run() {
        try {
            this.s = new Socket("192.168.43.1", 54321);
            if (this.s != null) {
                this.out = this.s.getOutputStream();
                if (this.out != null) {
                    this.cmdQuene.offer(" CLIENT SETUP");
                    while (!this.isLoop) {
                        if (this.cmdQuene.size() > 0) {
                            this.out.write(((String) this.cmdQuene.poll()).getBytes());
                        } else {
                            sendSignal();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSignal() {
        synchronized (this.cmdQuene) {
            if (this.isWait) {
                this.isWait = false;
                this.cmdQuene.notify();
            } else {
                this.isWait = true;
                try {
                    this.cmdQuene.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendLog(String s) {
        if (instance != null && this.out != null) {
            this.cmdQuene.offer(s + "");
            sendSignal();
        }
    }

    public void exit() {
        this.isLoop = true;
        if (this.out != null) {
            try {
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.s != null) {
            try {
                this.s.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        interrupt();
    }
}
