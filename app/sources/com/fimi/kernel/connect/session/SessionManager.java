package com.fimi.kernel.connect.session;

import android.os.Handler;
import android.os.Message;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.interfaces.IConnectResultListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SessionManager {
    private static SessionManager mSessionManager = new SessionManager();
    public boolean CONNECTION_SUCCEED = false;
    private final int CONNECT_NETWORK = 0;
    private final int DEVICE_CONNECT = 3;
    private final int DEVICE_CONNECT_ERROR = 5;
    private final int DEVICE_DISCONNECT = 4;
    private final int DISCONNECT_NETWORK = 1;
    private CopyOnWriteArrayList<IConnectResultListener> list = new CopyOnWriteArrayList();
    private Handler mHanlder = new Handler() {
        public void handleMessage(Message msg) {
            Iterator it;
            switch (msg.what) {
                case 0:
                    String clientMessage = msg.obj;
                    if (clientMessage == null) {
                        clientMessage = "startSession";
                    }
                    it = SessionManager.this.list.iterator();
                    while (it.hasNext()) {
                        ((IConnectResultListener) it.next()).onConnected(clientMessage);
                    }
                    return;
                case 1:
                    it = SessionManager.this.list.iterator();
                    while (it.hasNext()) {
                        ((IConnectResultListener) it.next()).onDisconnect("removeSession");
                    }
                    return;
                case 3:
                    it = SessionManager.this.list.iterator();
                    while (it.hasNext()) {
                        ((IConnectResultListener) it.next()).onDeviceConnect();
                    }
                    return;
                case 4:
                    it = SessionManager.this.list.iterator();
                    while (it.hasNext()) {
                        ((IConnectResultListener) it.next()).onDeviceDisConnnect();
                    }
                    return;
                case 5:
                    it = SessionManager.this.list.iterator();
                    while (it.hasNext()) {
                        ((IConnectResultListener) it.next()).onConnectError("");
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private BaseConnect mSession;

    public int getSize() {
        return this.list.size();
    }

    public static void initInstance() {
        getInstance();
    }

    public void add2NoticeList(IConnectResultListener callback) {
        this.list.add(callback);
    }

    public void removeNoticeList(IConnectResultListener callBack) {
        IConnectResultListener remove = null;
        Iterator it = this.list.iterator();
        while (it.hasNext()) {
            IConnectResultListener l = (IConnectResultListener) it.next();
            if (l != null && l == callBack) {
                remove = l;
                break;
            }
        }
        if (remove != null) {
            this.list.remove(remove);
        }
    }

    public static SessionManager getInstance() {
        return mSessionManager;
    }

    public synchronized void addSession(BaseConnect mSession) {
        this.mSession = mSession;
        this.mHanlder.sendEmptyMessage(0);
    }

    public synchronized void addSession(BaseConnect mSession, String clientMessage) {
        this.mSession = mSession;
        this.mHanlder.obtainMessage(0, clientMessage).sendToTarget();
    }

    public synchronized void removeSession() {
        this.mSession = null;
        this.mHanlder.sendEmptyMessage(1);
    }

    public synchronized BaseConnect getSession() {
        return this.mSession;
    }

    public void sendCmd(BaseCommand cmd) {
        try {
            if (this.mSession != null) {
                this.mSession.sendCmd(cmd);
            }
        } catch (Exception e) {
        }
    }

    public void sendJsonCmd(BaseCommand cmd) {
        try {
            if (this.mSession != null) {
                synchronized (this.mSession) {
                    this.mSession.sendJsonCmd(cmd);
                }
            }
        } catch (Exception e) {
        }
    }

    public void sendTimeCmd(BaseCommand cmd) {
        try {
            if (this.mSession != null) {
                this.mSession.sendTimeCmd(cmd);
            }
        } catch (Exception e) {
        }
    }

    public synchronized boolean hasSession() {
        return this.mSession != null;
    }

    public void closeSession() {
        if (this.mSession != null) {
            this.mSession.closeSession();
        }
    }

    public synchronized boolean isDeviceConnected() {
        boolean ret;
        ret = false;
        if (this.mSession != null && this.mSession.isDeviceConnected()) {
            ret = true;
        }
        return ret;
    }

    public synchronized void onDeviveState(int type) {
        if (type == 0) {
            this.mHanlder.sendEmptyMessage(4);
        } else if (type == 2) {
            this.mHanlder.sendEmptyMessage(5);
        } else {
            this.mHanlder.sendEmptyMessage(3);
        }
    }

    public void showListAll() {
        Iterator it = this.list.iterator();
        while (it.hasNext()) {
            IConnectResultListener iConnectResultListener = (IConnectResultListener) it.next();
        }
    }
}
