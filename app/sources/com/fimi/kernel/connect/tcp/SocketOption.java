package com.fimi.kernel.connect.tcp;

public class SocketOption {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final int RECEIVE_BUFFER_SIZE = 10240;
    private String charSet = "UTF-8";
    private String host;
    private boolean isAutoReconnect = false;
    private int port;
    private int receiveBufferSize = RECEIVE_BUFFER_SIZE;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReceiveBufferSize() {
        return this.receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public String getCharSet() {
        return this.charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public boolean isAutoReconnect() {
        return this.isAutoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.isAutoReconnect = autoReconnect;
    }
}
