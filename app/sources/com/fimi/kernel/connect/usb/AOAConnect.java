package com.fimi.kernel.connect.usb;

import android.app.PendingIntent;
import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import ch.qos.logback.core.util.CloseUtil;
import com.fimi.host.CmdLogBack;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.ResultListener;
import com.fimi.kernel.connect.interfaces.IDataTransfer;
import com.fimi.kernel.connect.interfaces.IRetransmissionHandle;
import com.fimi.kernel.connect.interfaces.IRetransmissionJsonHandle;
import com.fimi.kernel.connect.interfaces.IRetransmissionUsbHandle;
import com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle;
import com.fimi.kernel.connect.retransmission.RetransmissionJsonThread;
import com.fimi.kernel.connect.retransmission.RetransmissionThread;
import com.fimi.kernel.connect.retransmission.RetransmissionUsbThread;
import com.fimi.kernel.connect.retransmission.TimerSendQueueThread;
import com.fimi.kernel.connect.retransmission.X8JsonCmdDeque;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

public class AOAConnect extends BaseConnect implements IDataTransfer, IRetransmissionHandle, ITimerSendQueueHandle, IRetransmissionJsonHandle, IRetransmissionUsbHandle {
    LinkedBlockingDeque<Object> dataQue = new LinkedBlockingDeque();
    FileInputStream inputStream;
    private boolean isAoaDeviceConecect = false;
    private boolean isConnect = false;
    boolean isWait = false;
    UsbAccessory mAccessory;
    Context mContext;
    private IUSBStatusListener mIAoaConnectListener;
    PendingIntent mPermissionIntent;
    private boolean mPermissionRequestPending = false;
    private X8JsonCmdDeque mX8JsonCmdDeque;
    FileOutputStream outputStream;
    ParcelFileDescriptor parcelFileDescriptor;
    Thread readThread;
    ResultListener resultListener;
    private RetransmissionJsonThread retransmissionJsonThread;
    private RetransmissionThread retransmissionThread;
    private RetransmissionUsbThread retransmissionUsbThread;
    private TimerSendQueueThread timerSendQueueThread;
    UsbManager usbManager;
    Thread writeThread;

    class ReadThread extends Thread {
        ReadThread() {
        }

        public void run() {
            super.run();
            if (AOAConnect.this.inputStream != null) {
                byte[] buffer = new byte[2048];
                while (AOAConnect.this.isConnect) {
                    try {
                        int len = AOAConnect.this.inputStream.read(buffer);
                        if (len != -1) {
                            byte[] data = new byte[len];
                            System.arraycopy(buffer, 0, data, 0, len);
                            if (AOAConnect.this.resultListener != null && AOAConnect.this.isConnect) {
                                AOAConnect.this.resultListener.messageReceived(data);
                            }
                            if (AOAConnect.this.mX8JsonCmdDeque != null && AOAConnect.this.isConnect) {
                                AOAConnect.this.mX8JsonCmdDeque.checkJsonCmdSendTime();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        AOAConnect.this.mIAoaConnectListener.onAoaIoError(1);
                        return;
                    }
                }
            }
        }
    }

    class WriteThread extends Thread {
        WriteThread() {
        }

        public void run() {
            super.run();
            if (AOAConnect.this.outputStream != null) {
                while (AOAConnect.this.isConnect) {
                    try {
                        synchronized (AOAConnect.this.dataQue) {
                            if (AOAConnect.this.dataQue.isEmpty()) {
                                AOAConnect.this.sendSignal();
                            } else {
                                BaseCommand command = (BaseCommand) AOAConnect.this.dataQue.poll();
                                if (command != null) {
                                    command.setLastSendTime(SystemClock.uptimeMillis());
                                    switch (command.getLinkMsgType()) {
                                        case FmLink4:
                                            if (command.isAddToSendQueue()) {
                                                AOAConnect.this.retransmissionThread.addToSendList(command);
                                                break;
                                            }
                                            break;
                                        case JsonData:
                                            AOAConnect.this.retransmissionJsonThread.addToSendList(command);
                                            break;
                                        case MediaDownData:
                                            AOAConnect.this.retransmissionUsbThread.addToSendList(command);
                                            break;
                                    }
                                }
                                AOAConnect.this.sendData(command);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    public AOAConnect(Context mContext, UsbAccessory usbAccessory, ResultListener resultListener, IUSBStatusListener mIAoaConnectListener) {
        this.mContext = mContext;
        this.resultListener = resultListener;
        this.usbManager = (UsbManager) mContext.getSystemService("usb");
        this.mAccessory = usbAccessory;
        this.mIAoaConnectListener = mIAoaConnectListener;
        this.mX8JsonCmdDeque = new X8JsonCmdDeque(this);
        this.isAoaDeviceConecect = false;
        if (usbAccessory != null) {
            openUsbAccessory(this.mAccessory);
        }
    }

    private void openUsbAccessory(UsbAccessory usbAccessory) {
        this.parcelFileDescriptor = this.usbManager.openAccessory(usbAccessory);
        if (this.parcelFileDescriptor != null) {
            FileDescriptor fd = this.parcelFileDescriptor.getFileDescriptor();
            this.inputStream = new FileInputStream(fd);
            this.outputStream = new FileOutputStream(fd);
            try {
                this.outputStream.write(0);
                this.isAoaDeviceConecect = true;
            } catch (IOException e) {
                e.printStackTrace();
                this.isAoaDeviceConecect = false;
            }
        }
    }

    public void sendRestransmissionData(BaseCommand bcd) {
        sendData(bcd);
    }

    public void onSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
        if (bcd.getPersonalDataCallBack() == null) {
            NoticeManager.getInstance().onSendTimeOut(groupId, cmdId, bcd);
        } else {
            NoticeManager.getInstance().onPersonalSendTimeOut(groupId, cmdId, bcd, bcd.getPersonalDataCallBack());
        }
    }

    public boolean removeFromListByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        if (this.retransmissionThread == null) {
            return false;
        }
        return this.retransmissionThread.removeFromListByCmdID(groupId, cmdId, seq, packet);
    }

    public boolean removeFromListByCmdIDLinkPacket4(int groupId, int cmdId, int seq, LinkPacket4 packet) {
        if (this.retransmissionThread == null) {
            return false;
        }
        return this.retransmissionThread.removeFromListByCmdIDLinkPacket4(groupId, cmdId, seq, packet);
    }

    public boolean removeFromTimerSendQueueByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        if (this.timerSendQueueThread == null) {
            return false;
        }
        return this.timerSendQueueThread.removeFromListByCmdID(groupId, cmdId, seq, packet);
    }

    public BaseCommand removeFromListByCmdID(int cmdId, String camKey) {
        if (this.retransmissionJsonThread == null) {
            return null;
        }
        return this.retransmissionJsonThread.removeFromListByCmdID(cmdId, camKey);
    }

    public boolean removeFromListByUsbCmdKey(int cmdKey) {
        if (this.retransmissionUsbThread == null) {
            return false;
        }
        return this.retransmissionUsbThread.removeFromListByUsbCmdKey(cmdKey);
    }

    public boolean removeFormListByOffset(int fileOffset) {
        if (this.retransmissionUsbThread == null) {
            return false;
        }
        return this.retransmissionUsbThread.removeFromListByOffset(fileOffset);
    }

    private void sendData(BaseCommand command) {
        try {
            if (this.outputStream != null && this.isConnect) {
                CmdLogBack.getInstance().writeLog(command.getCmdData(), true);
                if (command.getCmdData() != null) {
                    this.outputStream.write(command.getCmdData(), 0, command.getCmdData().length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSignal() {
        synchronized (this.dataQue) {
            if (this.isWait) {
                this.isWait = false;
                this.dataQue.notify();
            } else {
                this.isWait = true;
                try {
                    this.dataQue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isAoaDeviceConecect() {
        return this.isAoaDeviceConecect;
    }

    public void startSession() {
        this.isConnect = true;
        this.readThread = new ReadThread();
        this.writeThread = new WriteThread();
        this.readThread.start();
        this.writeThread.start();
        this.retransmissionThread = new RetransmissionThread(this);
        this.resultListener.setRetransmissionHandle(this);
        this.resultListener.setTimerSendQueueHandle(this);
        this.resultListener.setRetransmissionJsonHandle(this);
        this.resultListener.setRetransmissionUsbHandle(this);
        this.retransmissionJsonThread = new RetransmissionJsonThread();
        this.retransmissionUsbThread = new RetransmissionUsbThread(this);
        this.retransmissionThread.start();
        this.retransmissionJsonThread.start();
        this.retransmissionUsbThread.start();
    }

    public void closeUsbAccessory() {
        if (this.parcelFileDescriptor != null) {
            synchronized (this.parcelFileDescriptor) {
                CloseUtil.closeQuietly(this.parcelFileDescriptor);
            }
            this.parcelFileDescriptor = null;
        }
        if (this.inputStream != null) {
            synchronized (this.inputStream) {
                CloseUtil.closeQuietly(this.inputStream);
            }
            this.inputStream = null;
        }
        if (this.outputStream != null) {
            synchronized (this.outputStream) {
                CloseUtil.closeQuietly(this.outputStream);
            }
            this.outputStream = null;
        }
        if (this.isAoaDeviceConecect) {
            SessionManager.getInstance().removeSession();
        }
    }

    public void closeSession() {
        this.isConnect = false;
        if (this.readThread != null) {
            this.readThread.interrupt();
            this.readThread = null;
        }
        if (this.writeThread != null) {
            this.writeThread.interrupt();
            this.writeThread = null;
        }
        if (this.retransmissionThread != null) {
            this.retransmissionThread.exit();
            this.retransmissionThread = null;
        }
        if (this.timerSendQueueThread != null) {
            this.timerSendQueueThread.exit();
            this.timerSendQueueThread = null;
        }
        if (this.retransmissionJsonThread != null) {
            this.retransmissionJsonThread.exit();
            this.retransmissionJsonThread = null;
        }
        if (this.retransmissionUsbThread != null) {
            this.retransmissionUsbThread.exit();
            this.retransmissionUsbThread = null;
        }
        this.mX8JsonCmdDeque = null;
        closeUsbAccessory();
    }

    public void sendCmd(BaseCommand cmd) {
        switch (cmd.getLinkMsgType()) {
            case FmLink4:
                this.dataQue.add(cmd);
                sendSignal();
                return;
            case JsonData:
                if (this.mX8JsonCmdDeque != null) {
                    this.mX8JsonCmdDeque.addJsonCmd(cmd);
                    return;
                }
                return;
            case FwUploadData:
                sendData(cmd);
                return;
            default:
                this.dataQue.add(cmd);
                sendSignal();
                return;
        }
    }

    public void putInQueue(BaseCommand cmd) {
        this.dataQue.add(cmd);
        sendSignal();
    }

    public void sendJsonCmd(BaseCommand cmd) {
        if (this.mX8JsonCmdDeque != null) {
            this.mX8JsonCmdDeque.addJsonCmd(cmd);
        }
    }

    public boolean isDeviceConnected() {
        return false;
    }
}
