package com.fimi.kernel.utils;

import java.text.DecimalFormat;

public class NumberUtil {
    public static String decimalPointStr(double decimal, int number) {
        Exception e;
        DecimalFormat df;
        if (number == 1) {
            try {
                df = new DecimalFormat("0.0");
                try {
                    return df.format(decimal).replace(",", ".");
                } catch (Exception e2) {
                    e = e2;
                    DecimalFormat decimalFormat = df;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } else if (number == 2) {
            df = new DecimalFormat("0.00");
            return df.format(decimal).replace(",", ".");
        } else if (number == 4) {
            df = new DecimalFormat("0.0000");
            return df.format(decimal).replace(",", ".");
        } else if (number != 7) {
            return decimal + "";
        } else {
            df = new DecimalFormat("0.0000000");
            return df.format(decimal).replace(",", ".");
        }
        e.printStackTrace();
        return "0";
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
