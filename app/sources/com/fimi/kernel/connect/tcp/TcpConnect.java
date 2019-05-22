package com.fimi.kernel.connect.tcp;

import android.util.Log;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.ResultListener;
import com.fimi.kernel.connect.SocketOption;
import com.fimi.kernel.dataparser.CmdSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

public class TcpConnect extends BaseConnect implements Runnable {
    boolean autoConnect = false;
    private CmdSession cmdSession;
    private LinkedBlockingDeque<Object> dataQue = new LinkedBlockingDeque();
    private boolean exitTcp = false;
    private ReadThread readThread;
    Socket socket;
    SocketOption socketOption;
    private WriteThread writeThread;
    private ResultListener x9ResultListener;

    public class ReadThread extends Thread {
        private InputStream mInputStream;

        public void run() {
            try {
                this.mInputStream = TcpConnect.this.socket.getInputStream();
                if (this.mInputStream != null) {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        this.mInputStream.read(buffer);
                        TcpConnect.this.x9ResultListener.messageReceived(buffer);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e1) {
                Log.d("moweiru==>", e1.getMessage().toString());
                e1.printStackTrace();
            }
        }
    }

    public class WriteThread extends Thread {
        private int count;
        OutputStream mOutput;

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        public void run() {
            /*
            r7 = this;
            r2 = com.fimi.kernel.connect.tcp.TcpConnect.this;	 Catch:{ Exception -> 0x006c }
            r2 = r2.socket;	 Catch:{ Exception -> 0x006c }
            r2 = r2.getOutputStream();	 Catch:{ Exception -> 0x006c }
            r7.mOutput = r2;	 Catch:{ Exception -> 0x006c }
            r2 = r7.mOutput;	 Catch:{ Exception -> 0x006c }
            if (r2 != 0) goto L_0x000f;
        L_0x000e:
            return;
        L_0x000f:
            r2 = com.fimi.kernel.connect.tcp.TcpConnect.this;	 Catch:{ Exception -> 0x006c }
            r2 = r2.exitTcp;	 Catch:{ Exception -> 0x006c }
            if (r2 != 0) goto L_0x000e;
        L_0x0017:
            r2 = com.fimi.kernel.connect.tcp.TcpConnect.this;	 Catch:{ Exception -> 0x006c }
            r3 = r2.dataQue;	 Catch:{ Exception -> 0x006c }
            monitor-enter(r3);	 Catch:{ Exception -> 0x006c }
            r2 = com.fimi.kernel.connect.tcp.TcpConnect.this;	 Catch:{ all -> 0x0069 }
            r2 = r2.dataQue;	 Catch:{ all -> 0x0069 }
            r2 = r2.isEmpty();	 Catch:{ all -> 0x0069 }
            if (r2 != 0) goto L_0x0067;
        L_0x002a:
            r2 = com.fimi.kernel.connect.tcp.TcpConnect.this;	 Catch:{ all -> 0x0069 }
            r2 = r2.dataQue;	 Catch:{ all -> 0x0069 }
            r0 = r2.poll();	 Catch:{ all -> 0x0069 }
            r0 = (com.fimi.kernel.connect.BaseCommand) r0;	 Catch:{ all -> 0x0069 }
            r2 = "moweiru";
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0069 }
            r4.<init>();	 Catch:{ all -> 0x0069 }
            r5 = "tcp write cmd:";
            r4 = r4.append(r5);	 Catch:{ all -> 0x0069 }
            r5 = r0.getCmdData();	 Catch:{ all -> 0x0069 }
            r5 = com.fimi.kernel.dataparser.milink.ByteHexHelper.bytesToHexString(r5);	 Catch:{ all -> 0x0069 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x0069 }
            r4 = r4.toString();	 Catch:{ all -> 0x0069 }
            android.util.Log.d(r2, r4);	 Catch:{ all -> 0x0069 }
            if (r0 == 0) goto L_0x0067;
        L_0x0058:
            r2 = r7.mOutput;	 Catch:{ all -> 0x0069 }
            r4 = r0.getCmdData();	 Catch:{ all -> 0x0069 }
            r5 = 0;
            r6 = r0.getCmdData();	 Catch:{ all -> 0x0069 }
            r6 = r6.length;	 Catch:{ all -> 0x0069 }
            r2.write(r4, r5, r6);	 Catch:{ all -> 0x0069 }
        L_0x0067:
            monitor-exit(r3);	 Catch:{ all -> 0x0069 }
            goto L_0x000f;
        L_0x0069:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0069 }
            throw r2;	 Catch:{ Exception -> 0x006c }
        L_0x006c:
            r1 = move-exception;
            r2 = "moweiru";
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "write exception:";
            r3 = r3.append(r4);
            r4 = r1.getMessage();
            r3 = r3.append(r4);
            r3 = r3.toString();
            android.util.Log.d(r2, r3);
            r1.printStackTrace();
            goto L_0x000e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.connect.tcp.TcpConnect$WriteThread.run():void");
        }
    }

    public TcpConnect(SocketOption option, ResultListener listener) {
        this.socketOption = option;
        this.autoConnect = option.isAutoReconnect();
        this.x9ResultListener = listener;
    }

    public void run() {
    }

    private void connectSocket(SocketOption option) {
        try {
            Log.d("moweiru", "cmd tcp connect success");
            this.socket = new Socket(option.getHost(), option.getPort());
        } catch (IOException ex) {
            Log.d("moweiru", "fail to connect tcp ,exception:" + ex.getMessage().toString());
            ex.printStackTrace();
        }
    }

    public void startSession() {
        connectSocket(this.socketOption);
        this.readThread = new ReadThread();
        this.writeThread = new WriteThread();
        this.readThread.start();
        this.writeThread.start();
    }

    public void closeSession() {
        this.exitTcp = false;
        if (this.readThread != null) {
            this.readThread.interrupt();
        }
        if (this.writeThread != null) {
            this.writeThread.interrupt();
        }
        if (this.socket != null && this.socket.isConnected()) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCmd(BaseCommand cmd) {
        this.dataQue.add(cmd);
    }

    public boolean isDeviceConnected() {
        return false;
    }

    private void sendHeartBit() {
        this.dataQue.add(null);
    }

    public void sendJsonCmd(BaseCommand cmd) {
    }
}
