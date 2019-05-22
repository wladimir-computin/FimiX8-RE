package com.fimi.x8sdk.listener;

public class ConnectStatusListener {

    public interface ICamConectCallBack {
        void onConnected();

        void onConnectionClosed();
    }

    public interface IEngineCallback {
        void onConnected();

        void onConnectionClosed();

        void onConnectionEstablished();
    }

    public interface IFCConnectCallBack {
        void onConnected();

        void onConnectionClosed();

        void onReConnected();
    }

    public interface IRCConnectCallBack {
        void onConnected();

        void onConnectionClosed();
    }
}
