package com.fimi.kernel.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNSLookupThread extends Thread {
    private InetAddress addr;
    private String hostname;

    public DNSLookupThread(String hostname) {
        this.hostname = hostname;
    }

    public void run() {
        try {
            set(InetAddress.getByName(this.hostname));
        } catch (UnknownHostException e) {
        }
    }

    private synchronized void set(InetAddress addr) {
        this.addr = addr;
    }

    public synchronized String getIP() {
        String hostAddress;
        if (this.addr != null) {
            hostAddress = this.addr.getHostAddress();
        } else {
            hostAddress = null;
        }
        return hostAddress;
    }

    public static boolean isDSNSuceess() {
        DNSLookupThread dnsTh = new DNSLookupThread("www.baidu.com");
        dnsTh.start();
        try {
            dnsTh.join(500);
        } catch (Exception e) {
        }
        if (dnsTh.getIP() != null) {
            return true;
        }
        return false;
    }
}
