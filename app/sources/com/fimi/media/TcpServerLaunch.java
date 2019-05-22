package com.fimi.media;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerLaunch extends Thread {
    private static TextView logView;
    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TcpServerLaunch.s.length() > GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION) {
                TcpServerLaunch.s.delete(0, TcpServerLaunch.s.length());
            }
            TcpServerLaunch.s.append((String) msg.obj);
            TcpServerLaunch.logView.setText(TcpServerLaunch.s.toString());
        }
    };
    static StringBuffer s = new StringBuffer();

    public TcpServerLaunch(TextView logView) {
        logView = logView;
    }

    public void run() {
        main();
    }

    public void main() {
        try {
            while (true) {
                Socket s = new ServerSocket(54321).accept();
                mHandler.obtainMessage(0, "start").sendToTarget();
                new TcpServer(s).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
