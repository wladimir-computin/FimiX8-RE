package com.fimi.x8sdk.connect.tcp;

import com.fimi.kernel.connect.tcp.IDataChannel;
import com.fimi.kernel.connect.tcp.SocketManager;
import com.fimi.kernel.connect.tcp.SocketOption;
import com.fimi.x8sdk.common.Constants;

public class FileDataTcpConnect implements IDataChannel {
    private SocketManager fileSocketManager = new SocketManager();

    public FileDataTcpConnect() {
        SocketOption fileOption = this.fileSocketManager.getSocketOption();
        fileOption.setHost(Constants.A12_TCP_CMD_HOST);
        fileOption.setPort(Constants.A12_TCP_File_PORT);
        fileOption.setReceiveBufferSize(SocketOption.RECEIVE_BUFFER_SIZE);
        fileOption.setAutoReconnect(false);
    }

    public synchronized void sendFileData(byte[] data, int offset, int count) {
        if (!this.fileSocketManager.isConnected()) {
            this.fileSocketManager.connect();
        }
        this.fileSocketManager.send(data, offset, count);
    }

    public SocketManager getFileSocketManager() {
        return this.fileSocketManager;
    }
}
