package com.fimi.x8sdk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X8LogBack {
    private static X8LogBack x9LogBack = new X8LogBack();
    Logger x9HandLogger = LoggerFactory.getLogger("x9_hand_log");

    public static X8LogBack getInstance() {
        return x9LogBack;
    }
}
