package com.fimi.kernel.utils;

public class CaCrc {
    public static long CRC16calc(int[] ptr, int len) {
        long crc = -1;
        int i = 0;
        while (true) {
            int k = i;
            int len2 = len;
            len = len2 - 1;
            if (len2 <= 0) {
                return crc;
            }
            long xbit = 2147483648L;
            i = k + 1;
            long data = (long) ptr[k];
            for (long bits = 0; bits < 32; bits++) {
                if ((2147483648L & crc) == 2147483648L) {
                    crc = ((crc << 1) & 4294967295L) ^ 79764919;
                } else {
                    crc = (crc << 1) & 4294967295L;
                }
                if ((data & xbit) == xbit) {
                    crc ^= 79764919;
                }
                xbit >>= 1;
            }
        }
    }

    public static byte[] getbyte(int x) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) ((x >> (i * 8)) & 255);
        }
        return b;
    }

    public static byte[] getbyte(long x) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) ((int) ((x >> (i * 8)) & 255));
        }
        return b;
    }

    public static byte[] getbyteshort(short x) {
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) ((x >> (i * 8)) & 255);
        }
        return b;
    }
}
