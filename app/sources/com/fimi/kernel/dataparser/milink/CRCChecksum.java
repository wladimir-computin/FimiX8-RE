package com.fimi.kernel.dataparser.milink;

public class CRCChecksum {
    public static final int X25_INIT_CRC = 65535;

    public static int crc16_calculate(byte[] pBuffer, int length) {
        int crcTmp = 65535;
        for (int i = 0; i < length; i++) {
            crcTmp = crc16_accumulate(pBuffer[i] & 255, 65535 & crcTmp);
        }
        return crcTmp;
    }

    static int crc16_accumulate(int data, int crcAccum) {
        int tmp = data ^ (crcAccum & 255);
        tmp = (tmp ^ (tmp << 4)) & 255;
        return (((crcAccum >> 8) ^ (tmp << 8)) ^ (tmp << 3)) ^ (tmp >> 4);
    }

    public static void main(String[] args) {
        int y = crc16_calculate(new byte[]{(byte) -2, (byte) 3, (byte) 32, (byte) 0, (byte) 3, (byte) 0, (byte) 8, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, 14);
    }
}
