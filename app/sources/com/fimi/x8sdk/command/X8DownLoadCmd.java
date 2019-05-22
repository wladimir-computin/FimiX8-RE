package com.fimi.x8sdk.command;

import android.support.v4.view.MotionEventCompat;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.milink.LinkPayload;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8DownLoadCmd extends BaseCommand {
    private X8MediaCmd createBaseComand() {
        return new X8MediaCmd();
    }

    public X8MediaCmd getMediaXmlFile(String xmlFileName) {
        X8MediaCmd x8MediaCmd = createBaseComand();
        short len = (short) xmlFileName.getBytes().length;
        byte[] nameBytes = xmlFileName.getBytes();
        byte[] payload = new byte[(len + 7)];
        byte[] lenBytes = ByteHexHelper.intToByteArray(payload.length);
        int i = 0 + 1;
        payload[0] = (byte) 0;
        System.arraycopy(lenBytes, 0, payload, i, lenBytes.length);
        int length = lenBytes.length + 1;
        i = length + 1;
        payload[length] = (byte) (len & 255);
        length = i + 1;
        payload[i] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & len) >> 8);
        System.arraycopy(nameBytes, 0, payload, length, nameBytes.length);
        x8MediaCmd.packCmd(payload);
        x8MediaCmd.setUsbCmdKey(payload[0]);
        return x8MediaCmd;
    }

    public X8MediaCmd downMediaFile(int offset, short maxLen, String downFileName, boolean isStop) {
        int i;
        int i2;
        X8MediaCmd x8MediaCmd = createBaseComand();
        short len = (short) downFileName.getBytes().length;
        byte[] nameBytes = downFileName.getBytes();
        byte[] payload = new byte[(len + 13)];
        byte[] lenBytes = ByteHexHelper.intToByteArray(payload.length);
        if (isStop) {
            i = 0 + 1;
            payload[0] = (byte) 2;
            i2 = i;
        } else {
            i = 0 + 1;
            payload[0] = (byte) 1;
            i2 = i;
        }
        System.arraycopy(lenBytes, 0, payload, i2, lenBytes.length);
        i2 = lenBytes.length + 1;
        byte[] offeBytes = ByteHexHelper.intToByteArray(offset);
        System.arraycopy(offeBytes, 0, payload, i2, offeBytes.length);
        i2 += offeBytes.length;
        if (maxLen >= NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE) {
            maxLen = NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE;
        }
        i = i2 + 1;
        payload[i2] = (byte) (maxLen & 255);
        i2 = i + 1;
        payload[i] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & maxLen) >> 8);
        i = i2 + 1;
        payload[i2] = (byte) (len & 255);
        i2 = i + 1;
        payload[i] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & len) >> 8);
        System.arraycopy(nameBytes, 0, payload, i2, nameBytes.length);
        x8MediaCmd.setUsbCmdKey(offset);
        x8MediaCmd.setFileOffset(offset);
        x8MediaCmd.setReSendNum(5);
        x8MediaCmd.setOutTime(1000);
        x8MediaCmd.packCmd(payload);
        return x8MediaCmd;
    }

    public X8MediaCmd downMediaFile2(int offset, short maxLen, String downFileName) {
        X8MediaCmd x8MediaCmd = createBaseComand();
        short len = (short) downFileName.getBytes().length;
        byte[] nameBytes = downFileName.getBytes();
        byte[] payload = new byte[(len + 13)];
        int i = 0 + 1;
        payload[0] = (byte) 1;
        int payLoadLen = payload.length;
        int i2 = i + 1;
        payload[i] = (byte) (payLoadLen & 255);
        i = i2 + 1;
        payload[i2] = (byte) ((payLoadLen >> 8) & 255);
        i2 = i + 1;
        payload[i] = (byte) ((payLoadLen >> 16) & 255);
        i = i2 + 1;
        payload[i2] = (byte) ((payLoadLen >> 24) & 255);
        i2 = i + 1;
        payload[i] = (byte) (offset & 255);
        i = i2 + 1;
        payload[i2] = (byte) ((offset >> 8) & 255);
        i2 = i + 1;
        payload[i] = (byte) ((offset >> 16) & 255);
        i = i2 + 1;
        payload[i2] = (byte) ((offset >> 24) & 255);
        if (maxLen >= NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE) {
            maxLen = NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE;
        }
        i2 = i + 1;
        payload[i] = (byte) (maxLen & 255);
        i = i2 + 1;
        payload[i2] = (byte) ((maxLen & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        i2 = i + 1;
        payload[i] = (byte) (len & 255);
        i = i2 + 1;
        payload[i2] = (byte) ((len & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        System.arraycopy(nameBytes, 0, payload, i, nameBytes.length);
        x8MediaCmd.setReSendNum(5);
        x8MediaCmd.setOutTime(1000);
        x8MediaCmd.packCmd(payload);
        return x8MediaCmd;
    }

    public void unpack(LinkPayload payload) {
    }
}
