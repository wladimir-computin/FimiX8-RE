package com.fimi.host;

import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdLogBack {
    private static CmdLogBack hostLogBack = new CmdLogBack();
    private boolean isLog = true;
    Logger logger = LoggerFactory.getLogger("x8s_cmd_log");

    public static CmdLogBack getInstance() {
        return hostLogBack;
    }

    public void writeLog(byte[] bytes, boolean isUp) {
        if (this.isLog) {
            this.logger.info((isUp ? "send-->" : "recv-->") + "" + ByteHexHelper.bytesToHexString(bytes));
        }
    }
}
