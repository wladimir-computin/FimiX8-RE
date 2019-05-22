package com.fimi.kernel.connect.udp;

import android.os.SystemClock;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.ResultListener;
import com.fimi.kernel.connect.SocketOption;
import com.fimi.kernel.connect.interfaces.IDataTransfer;
import com.fimi.kernel.connect.interfaces.IRetransmissionHandle;
import com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle;
import com.fimi.kernel.connect.retransmission.RetransmissionThread;
import com.fimi.kernel.connect.retransmission.TimerSendQueueThread;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.utils.ByteUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class UdpConnect extends BaseConnect implements IDataTransfer, IRetransmissionHandle, ITimerSendQueueHandle {
    private static DatagramPacket packetRcv;
    private static DatagramPacket packetSend;
    private static DatagramSocket socket = null;
    public LinkedBlockingDeque<Object> cmdQuene = new LinkedBlockingDeque();
    InetAddress hostAddress = null;
    boolean isWait = false;
    private long lastTime;
    private CheckDeviceConnectThread mCheckDeviceConnectThread;
    private RetransmissionThread mRetransmissionThread;
    private TimerSendQueueThread mTimerSendQueueThread;
    private byte[] msgRcv = new byte[1024];
    private ReadThread readThread;
    private SendThread sendThread;
    private SocketOption socketOption;
    private boolean udpLife = true;
    private ResultListener x9Listener;

    public class CheckDeviceConnectThread extends Thread {
        private boolean isLoop = true;

        public void exit() {
            this.isLoop = false;
            interrupt();
        }

        public void run() {
            while (this.isLoop) {
                if (System.currentTimeMillis() - UdpConnect.this.lastTime > 3000) {
                    UdpConnect.this.closeSession();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ReadThread extends Thread {
        ReadThread() {
        }

        public void run() {
            while (UdpConnect.this.udpLife) {
                try {
                    UdpConnect.socket.receive(UdpConnect.packetRcv);
                    UdpConnect.this.updateTime();
                    if (UdpConnect.this.x9Listener != null) {
                        UdpConnect.this.x9Listener.messageReceived(UdpConnect.packetRcv.getData());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SendThread extends Thread {
        private SendThread() {
        }

        public void run() {
            super.run();
            while (UdpConnect.this.udpLife) {
                if (UdpConnect.this.cmdQuene.isEmpty()) {
                    UdpConnect.this.sendSignal();
                } else {
                    BaseCommand cmd = (BaseCommand) UdpConnect.this.cmdQuene.poll();
                    if (cmd != null) {
                        cmd.setLastSendTime(SystemClock.uptimeMillis());
                        if (cmd.isAddToSendQueue()) {
                            if (cmd.isTimerCmd()) {
                                UdpConnect.this.mTimerSendQueueThread.addToSendList(cmd);
                            } else {
                                UdpConnect.this.mRetransmissionThread.addToSendList(cmd);
                            }
                        }
                        UdpConnect.this.sendDatas(cmd);
                    }
                }
            }
        }
    }

    public UdpConnect(DatagramSocket socket, SocketOption option, ResultListener listener) throws UnknownHostException {
        this.socketOption = option;
        this.x9Listener = listener;
        socket = socket;
        packetRcv = new DatagramPacket(this.msgRcv, this.msgRcv.length);
        this.hostAddress = InetAddress.getByName(this.socketOption.getHost());
    }

    public void sendRestransmissionData(BaseCommand bcd) {
        sendDatas(bcd);
    }

    public void onSendTimeOut(int group_id, int cmdId, BaseCommand bcd) {
        if (bcd.getPersonalDataCallBack() == null) {
            NoticeManager.getInstance().onSendTimeOut(group_id, cmdId, bcd);
        } else {
            NoticeManager.getInstance().onPersonalSendTimeOut(group_id, cmdId, bcd, bcd.getPersonalDataCallBack());
        }
    }

    public boolean removeFromListByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        if (this.mRetransmissionThread == null) {
            return false;
        }
        return this.mRetransmissionThread.removeFromListByCmdID(groupId, cmdId, seq, packet);
    }

    public boolean removeFromListByCmdIDLinkPacket4(int groupId, int cmdId, int seq, LinkPacket4 packet) {
        return false;
    }

    public boolean removeFromTimerSendQueueByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        if (this.mTimerSendQueueThread == null) {
            return false;
        }
        return this.mTimerSendQueueThread.removeFromListByCmdID(groupId, cmdId, seq, packet);
    }

    public void sendDatas(BaseCommand cmd) {
        if (cmd.getCmdData().length >= 1024) {
            for (Object object : ByteUtil.splitAry(cmd.getCmdData(), 1024)) {
                writeCmd((byte[]) object);
            }
            return;
        }
        writeCmd(cmd.getCmdData());
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

    private void writeCmd(byte[] cmd) {
        try {
            packetSend = new DatagramPacket(cmd, cmd.length, this.hostAddress, this.socketOption.getPort());
            socket.send(packetSend);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startSession() {
        this.readThread = new ReadThread();
        this.sendThread = new SendThread();
        this.mRetransmissionThread = new RetransmissionThread(this);
        this.mTimerSendQueueThread = new TimerSendQueueThread();
        this.mCheckDeviceConnectThread = new CheckDeviceConnectThread();
        this.readThread.start();
        this.sendThread.start();
        this.x9Listener.setRetransmissionHandle(this);
        this.x9Listener.setTimerSendQueueHandle(this);
        this.mRetransmissionThread.start();
        this.mTimerSendQueueThread.start();
        updateTime();
        this.mCheckDeviceConnectThread.start();
    }

    public void closeSession() {
        this.udpLife = false;
        if (this.readThread != null) {
            this.readThread.interrupt();
        }
        if (this.sendThread != null) {
            this.sendThread.interrupt();
        }
        if (this.mRetransmissionThread != null) {
            this.mRetransmissionThread.exit();
        }
        if (this.mCheckDeviceConnectThread != null) {
            this.mCheckDeviceConnectThread.exit();
        }
        if (this.mTimerSendQueueThread != null) {
            this.mTimerSendQueueThread.exit();
            this.mTimerSendQueueThread = null;
        }
        if (socket != null) {
            socket.close();
        }
        SessionManager.getInstance().removeSession();
    }

    public void sendCmd(BaseCommand cmd) {
        try {
            this.cmdQuene.offer(cmd, (long) cmd.getOutTime(), TimeUnit.MILLISECONDS);
            sendSignal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isDeviceConnected() {
        return this.udpLife;
    }

    public void updateTime() {
        this.lastTime = System.currentTimeMillis();
    }

    public void sendJsonCmd(BaseCommand cmd) {
    }
}
