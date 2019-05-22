package com.fimi.kernel.connect.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.SystemClock;
import com.fimi.kernel.Constants;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.BaseConnect.DeviceConnectState;
import com.fimi.kernel.connect.ResultListener;
import com.fimi.kernel.connect.interfaces.IDataTransfer;
import com.fimi.kernel.connect.interfaces.IRetransmissionHandle;
import com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle;
import com.fimi.kernel.connect.interfaces.ble.IBleSendData;
import com.fimi.kernel.connect.retransmission.RetransmissionThread;
import com.fimi.kernel.connect.retransmission.TimerSendQueueThread;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.dataparser.milink.Parser;
import com.fimi.kernel.utils.ByteUtil;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BleConnect extends BaseConnect implements IDataTransfer, IRetransmissionHandle, ITimerSendQueueHandle {
    private final int SEND_LEN = 20;
    BaseCommand bleStateCommand;
    private BluetoothGattCharacteristic characterWrite;
    public LinkedBlockingDeque<Object> cmdQuene = new LinkedBlockingDeque();
    public LinkedBlockingDeque<byte[]> cmdQuene2 = new LinkedBlockingDeque();
    public int[] cmds = new int[]{193, 201, 202, 203, 204};
    private Context context;
    private BluetoothDevice device;
    private ResultListener gh2ResultListener;
    boolean isWait = false;
    private long lastTime;
    Logger logger = LoggerFactory.getLogger("gh2_communication_log");
    private IBleSendData mBleSendDataHandle;
    private CheckDeviceConnectThread mCheckDeviceConnectThread;
    private DeviceConnectState mDeviceConnectState = DeviceConnectState.IDEL;
    private ReadThread mReadThread;
    private RetransmissionThread mRetransmissionThread;
    private TimerSendQueueThread mTimerSendQueueThread;
    private WriteThread mWriteThread;
    private Parser p = new Parser();

    public class CheckDeviceConnectThread extends Thread {
        private boolean isLoop = true;

        public void exit() {
            this.isLoop = false;
            interrupt();
        }

        public void run() {
            BleConnect.this.receiveLog(" CheckDeviceConnectThread run");
            while (this.isLoop) {
                if (System.currentTimeMillis() - BleConnect.this.lastTime > 2000 && BleConnect.this.cmdQuene.size() <= 0 && Constants.isCheckDeviceConnect && SessionManager.getInstance().hasSession()) {
                    BleConnect.this.sendCmd(BleConnect.this.bleStateCommand);
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ReadThread extends Thread {
        private boolean mExit;

        public void releaseConnection() {
            this.mExit = true;
            interrupt();
        }

        public void run() {
            while (!this.mExit) {
                try {
                    if (BleConnect.this.cmdQuene2.isEmpty()) {
                        Thread.sleep(100);
                    } else {
                        byte[] data = (byte[]) BleConnect.this.cmdQuene2.poll();
                        if (data != null) {
                            BleConnect.this.gh2ResultListener.messageReceived(data);
                            BleConnect.this.receiveLog(ByteUtil.bytesToHexString(data) + "=======");
                        }
                    }
                } catch (Exception e) {
                    BleConnect.this.receiveLog(" sessionhandler writethread error " + e.toString());
                    return;
                }
            }
        }
    }

    public class WriteThread extends Thread {
        private boolean mExit;

        public void releaseConnection() {
            this.mExit = true;
            interrupt();
        }

        public void run() {
            while (!this.mExit) {
                try {
                    if (BleConnect.this.cmdQuene.isEmpty()) {
                        BleConnect.this.sendSignal();
                    } else {
                        BaseCommand cmd = (BaseCommand) BleConnect.this.cmdQuene.poll();
                        if (cmd != null) {
                            cmd.setLastSendTime(SystemClock.uptimeMillis());
                            if (cmd.isAddToSendQueue()) {
                                if (cmd.isTimerCmd()) {
                                    BleConnect.this.mTimerSendQueueThread.addToSendList(cmd);
                                } else {
                                    BleConnect.this.mRetransmissionThread.addToSendList(cmd);
                                }
                            }
                            BleConnect.this.sendDividedData(cmd.getMsgGroudId(), cmd.getMsgId(), cmd.getCmdData(), cmd.getCmdData().length);
                        }
                    }
                } catch (Exception e) {
                    BleConnect.this.receiveLog(" sessionhandler writethread error " + e.toString());
                    return;
                }
            }
        }
    }

    public BleConnect(IBleSendData mIBleSendData, ResultListener listener) {
        this.mBleSendDataHandle = mIBleSendData;
        this.gh2ResultListener = listener;
    }

    public void setBleState(BaseCommand bleStateCommand) {
        this.bleStateCommand = bleStateCommand;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public void sendCmd(BaseCommand cmd) {
        this.cmdQuene.offer(cmd);
        sendSignal();
    }

    public void sendJsonCmd(BaseCommand cmd) {
    }

    public void sendTimeCmd(BaseCommand cmd) {
        sendDividedData(cmd.getMsgGroudId(), cmd.getMsgId(), cmd.getCmdData(), cmd.getCmdData().length);
    }

    public void startSession() {
        setClientAddress(this.device.getAddress());
        setClientName(this.device.getName());
        SessionManager.getInstance().addSession(this, this.device.getAddress());
        receiveLog("startSession");
        this.mWriteThread = new WriteThread();
        this.mReadThread = new ReadThread();
        this.mRetransmissionThread = new RetransmissionThread(this);
        this.mTimerSendQueueThread = new TimerSendQueueThread();
        this.gh2ResultListener.setRetransmissionHandle(this);
        this.gh2ResultListener.setTimerSendQueueHandle(this);
        if (this.mWriteThread != null) {
            this.mWriteThread.start();
        }
        if (this.mReadThread != null) {
            this.mReadThread.start();
        }
        if (this.mRetransmissionThread != null) {
            this.mRetransmissionThread.start();
        }
        if (this.mTimerSendQueueThread != null) {
            this.mTimerSendQueueThread.start();
        }
    }

    public void startRetransmissionThread() {
        receiveLog("" + hashCode() + " 蓝牙通途收发启动.................");
        if (this.mCheckDeviceConnectThread == null) {
            receiveLog("" + hashCode() + " 开启连接检测线程.................");
            this.mDeviceConnectState = DeviceConnectState.CONNECTED;
            SessionManager.getInstance().onDeviveState(1);
            this.mCheckDeviceConnectThread = new CheckDeviceConnectThread();
            updateTime();
            this.mCheckDeviceConnectThread.start();
        }
    }

    public void closeSession() {
        receiveLog("closeSession");
        if (this.mWriteThread != null) {
            this.mWriteThread.releaseConnection();
            this.mWriteThread = null;
        }
        if (this.mReadThread != null) {
            this.mReadThread.releaseConnection();
            this.mReadThread = null;
        }
        if (this.mCheckDeviceConnectThread != null) {
            this.mCheckDeviceConnectThread.exit();
            this.mCheckDeviceConnectThread = null;
        }
        if (this.mRetransmissionThread != null) {
            this.mRetransmissionThread.exit();
            this.mRetransmissionThread = null;
        }
        if (this.mTimerSendQueueThread != null) {
            this.mTimerSendQueueThread.exit();
            this.mTimerSendQueueThread = null;
        }
        this.mDeviceConnectState = DeviceConnectState.DISCONNECT;
        SessionManager.getInstance().onDeviveState(0);
        SessionManager.getInstance().removeSession();
    }

    public boolean isDeviceConnected() {
        return this.mDeviceConnectState == DeviceConnectState.CONNECTED;
    }

    private boolean isUpdateCmd(int groupId, int msgId) {
        if (groupId != 9) {
            return false;
        }
        for (int c : this.cmds) {
            if (msgId == c) {
                return true;
            }
        }
        return false;
    }

    private void sendDividedData(int groupId, int msgId, byte[] bytes, int len) {
        int slices;
        int divided = len / 20;
        int remainder = len % 20;
        if (remainder == 0) {
            slices = divided;
        } else {
            slices = divided + 1;
        }
        int deviceModle = bytes[22] & 255;
        int sleepTime = 10;
        if (groupId == 9 && msgId == 201 && deviceModle == 8) {
            sleepTime = 15;
        } else if (groupId == 9 && msgId == 201 && deviceModle == 3) {
            sleepTime = 15;
        }
        for (int i = 0; i < slices; i++) {
            byte[] buff;
            if (i != slices - 1 || remainder == 0) {
                buff = new byte[20];
                System.arraycopy(bytes, i * 20, buff, 0, 20);
                this.mBleSendDataHandle.sendMessage(groupId, msgId, buff, sleepTime);
            } else {
                buff = new byte[remainder];
                System.arraycopy(bytes, i * 20, buff, 0, remainder);
                this.mBleSendDataHandle.sendMessage(groupId, msgId, buff, sleepTime);
            }
        }
        updateTime();
        sendLog(ByteHexHelper.bytesToHexString(bytes));
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

    public void sendRestransmissionData(BaseCommand cmd) {
        sendLog("" + hashCode() + " 重发数据 seq =" + cmd.getMsgSeq() + " " + Integer.toHexString(cmd.getMsgId()));
        sendDividedData(cmd.getMsgGroudId(), cmd.getMsgId(), cmd.getCmdData(), cmd.getCmdData().length);
    }

    public void onSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
        NoticeManager.getInstance().onSendTimeOut(groupId, cmdId, bcd);
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

    public void updateTime() {
        this.lastTime = System.currentTimeMillis();
    }

    public void receiveLog(String msg) {
        this.logger.info("                App ==>" + msg);
    }

    public void sendLog(String msg) {
        this.logger.info("             send   ==> " + msg);
    }

    public void onBleData(byte[] data) {
        this.cmdQuene2.add(data);
    }
}
