package com.fimi.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostLogBack {
    private static HostLogBack hostLogBack = new HostLogBack();
    Logger logger = LoggerFactory.getLogger("host_app_log");

    public static HostLogBack getInstance() {
        return hostLogBack;
    }

    public void writeLog(String logStr) {
        this.logger.info("App ==>  " + logStr);
    }

    public void writeRelayLog(String logStr) {
        this.logger.info("Relay==>" + logStr);
    }
}
