package com.fimi.kernel.utils;

import android.annotation.SuppressLint;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewCompat;
import com.fimi.kernel.Constants;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint({"UseValueOf", "DefaultLocale"})
public class ByteUtil {
    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 255).byteValue();
            temp >>= 8;
        }
        return b;
    }

    public static short byteToShort(byte[] b, int index) {
        return (short) (((short) (b[index] & 255)) | ((short) (((short) (b[index + 1] & 255)) << 8)));
    }

    public static char byteToChar(byte[] b, int index) {
        return (char) (((b[index] & 255) << 8) | (b[index + 1] & 255));
    }

    public static String byteToString(byte[] data) {
        ByteBuffer bbuf = ByteBuffer.allocate(data.length);
        for (int i = 0; i < data.length; i++) {
            bbuf.put(data[i + 0]);
        }
        return new String(bbuf.array());
    }

    public static byte[] getByteArray(byte[] cmd, int start, int length) {
        byte[] data = new byte[length];
        if (length > 0) {
            try {
                System.arraycopy(cmd, start, data, 0, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
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

    public static String bytesToHexString2(byte[] src) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < src.length; i++) {
            sb.append(Character.forDigit((src[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(src[i] & 15, 16));
        }
        return sb.toString();
    }

    @SuppressLint({"DefaultLocale"})
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        byte[] d;
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        if (length == 1) {
            d = new byte[2];
        } else {
            d = new byte[length];
        }
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        if (d.length != 1) {
            return d;
        }
        d[1] = d[0];
        d[0] = (byte) 0;
        return d;
    }

    public static int getUnsignedByte(byte cmd) {
        byte c = cmd;
        if (c < (byte) 0) {
            return c + 256;
        }
        return c;
    }

    public static int get2ByteToInt(byte low, byte high) {
        return (high << 8) | getUnsignedByte(low);
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    @SuppressLint({"DefaultLocale"})
    public static byte hexStringToByte(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return (byte) 0;
        }
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        char[] hexChars = hexString.toUpperCase().toCharArray();
        return (byte) ((charToByte(hexChars[0]) << 4) | charToByte(hexChars[1]));
    }

    public static byte getCalibration(byte[] cmd) {
        byte total = (byte) 0;
        for (int i = 0; i < cmd.length - 1; i++) {
            total = (byte) (cmd[i] + total);
        }
        return total;
    }

    public static byte getCalibrationAll(byte[] cmd) {
        byte total = (byte) 0;
        for (byte b : cmd) {
            total = (byte) (b + total);
        }
        return total;
    }

    public static byte[] float2byte(float f) {
        int i;
        int fbit = Float.floatToIntBits(f);
        byte[] b = new byte[4];
        for (i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - (i * 8)));
        }
        int len = b.length;
        byte[] dest = new byte[len];
        System.arraycopy(b, 0, dest, 0, len);
        for (i = 0; i < len / 2; i++) {
            byte temp = dest[i];
            dest[i] = dest[(len - i) - 1];
            dest[(len - i) - 1] = temp;
        }
        return dest;
    }

    public static float byte2float(byte[] b, int index) {
        return Float.intBitsToFloat((int) (((long) (((int) (((long) (((int) (((long) (b[index + 0] & 255)) | (((long) b[index + 1]) << 8))) & 65535)) | (((long) b[index + 2]) << 16))) & ViewCompat.MEASURED_SIZE_MASK)) | (((long) b[index + 3]) << 24)));
    }

    public static long byte2long(byte[] b, int index) {
        return (((((((long) b[index + 0]) & 255) | (((long) b[index + 1]) << 8)) & 65535) | (((long) b[index + 2]) << 16)) & 16777215) | (((long) b[index + 3]) << 24);
    }

    public static byte[] intToByte(int intValue) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) ((intValue >> (i * 8)) & 255);
        }
        return b;
    }

    public static int byteToInt(byte[] b) {
        int n = 0;
        for (int i = 0; i < 4; i++) {
            n = (n << 8) | (b[i] & 255);
        }
        return n;
    }

    public static int bytesToInt(byte[] src, int offset) {
        return (((src[offset] & 255) | ((src[offset + 1] & 255) << 8)) | ((src[offset + 2] & 255) << 16)) | ((src[offset + 3] & 255) << 24);
    }

    public int bytesToInt2(byte[] src, int offset) {
        return ((((src[offset] & 255) << 24) | ((src[offset + 1] & 255) << 16)) | ((src[offset + 2] & 255) << 8)) | (src[offset + 3] & 255);
    }

    public static byte[] shortToByteArray(short s) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            shortBuf[i] = (byte) ((s >>> (((shortBuf.length - 1) - i) * 8)) & 255);
        }
        return shortBuf;
    }

    public static int get2UnsignedByteToInt(byte[] cmd, int start) {
        int a = getUnsignedByte(cmd[start + 1]);
        return (a << 8) | getUnsignedByte(cmd[start]);
    }

    public static String byteToBitString(byte b) {
        return "" + ((byte) ((b >> 7) & 1)) + ((byte) ((b >> 6) & 1)) + ((byte) ((b >> 5) & 1)) + ((byte) ((b >> 4) & 1)) + ((byte) ((b >> 3) & 1)) + ((byte) ((b >> 2) & 1)) + ((byte) ((b >> 1) & 1)) + ((byte) ((b >> 0) & 1));
    }

    public static byte[] toLH(int n) {
        return new byte[]{(byte) (n & 255), (byte) ((n >> 8) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 24) & 255)};
    }

    public static byte[] toHL(int n) {
        return new byte[]{(byte) (n & 255), (byte) ((n >> 8) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 24) & 255)};
    }

    public static byte[] toHL(long n) {
        return new byte[]{(byte) ((int) (n & 255)), (byte) ((int) ((n >> 8) & 255)), (byte) ((int) ((n >> 16) & 255)), (byte) ((int) ((n >> 24) & 255)), (byte) ((int) ((n >> 32) & 255)), (byte) ((int) ((n >> 40) & 255)), (byte) ((int) ((n >> 48) & 255)), (byte) ((int) ((n >> 56) & 255))};
    }

    public static byte[] longToBytes(long x) {
        buffer.clear();
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ix++) {
            num = (num << 8) | ((long) (byteNum[ix] & 255));
        }
        return num;
    }

    public static int getLHByteToInt(byte[] cmd, int start) {
        return (((cmd[start + 3] << 24) | (getUnsignedByte(cmd[start + 2]) << 16)) | (getUnsignedByte(cmd[start + 1]) << 8)) | getUnsignedByte(cmd[start]);
    }

    public static int get4ByteToInt(byte[] cmd, int start) {
        return (((cmd[start + 3] << 24) | (getUnsignedByte(cmd[start + 2]) << 16)) | (getUnsignedByte(cmd[start + 1]) << 8)) | getUnsignedByte(cmd[start]);
    }

    public static final int crc16CheckSum(byte[] data) {
        int crc = 65535;
        for (int i = 0; i < data.length - 2; i++) {
            int tmp = data[i] ^ (crc & 255);
            tmp ^= (tmp & 255) << 4;
            crc = (((crc >> 8) ^ ((tmp & 255) << 8)) ^ ((tmp & 255) << 3)) ^ ((tmp & 255) >> 4);
        }
        return crc;
    }

    public static final int crcAddCheckSum(byte[] data) {
        if (data.length < 3) {
            return 0;
        }
        int crc = 0;
        for (byte unsignedByte : getByteArray(data, 1, data.length - 3)) {
            crc += getUnsignedByte(unsignedByte);
        }
        return crc;
    }

    public static boolean isCheckSumCorrect(byte[] cmd) {
        byte[] cmdCheckSum = intToByte(CRCUtil.crc16Calculate(cmd, Constants.CRC16_LENGTH));
        byte[] originalCheckSum = getByteArray(cmd, 14, 2);
        if (cmdCheckSum[0] == originalCheckSum[0] && cmdCheckSum[1] == originalCheckSum[1]) {
            return true;
        }
        return false;
    }

    public static boolean isCheckSumCorrectPayload(byte[] cmd) {
        int[] crcInts = CRCUtil.bytesToInts(Arrays.copyOfRange(cmd, 20, cmd.length));
        byte[] crc32s = longToBytes((long) ((int) CRCUtil.calcCRC32(crcInts, crcInts.length)));
        byte[] originalCheckSum = Arrays.copyOfRange(cmd, 16, 20);
        if (crc32s[0] == originalCheckSum[0] && crc32s[1] == originalCheckSum[1] && crc32s[2] == originalCheckSum[2] && crc32s[3] == originalCheckSum[3]) {
            return true;
        }
        return false;
    }

    public static String toHex(byte b) {
        String result = Integer.toHexString(b & 255);
        if (result.length() == 1) {
            return '0' + result;
        }
        return result;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[(byte_1.length + byte_2.length)];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static Object[] splitAry(byte[] ary, int subSize) {
        int count;
        int i;
        int j;
        if (ary.length % subSize == 0) {
            count = ary.length / subSize;
        } else {
            count = (ary.length / subSize) + 1;
        }
        List<List<Byte>> subAryList = new ArrayList();
        for (i = 0; i < count; i++) {
            int index = i * subSize;
            List<Byte> list = new ArrayList();
            j = 0;
            int index2 = index;
            while (j < subSize && index2 < ary.length) {
                index = index2 + 1;
                list.add(Byte.valueOf(ary[index2]));
                j++;
                index2 = index;
            }
            subAryList.add(list);
        }
        Object[] subAry = new Object[subAryList.size()];
        for (i = 0; i < subAryList.size(); i++) {
            List<Byte> subList = (List) subAryList.get(i);
            byte[] subAryItem = new byte[subList.size()];
            for (j = 0; j < subList.size(); j++) {
                subAryItem[j] = ((Byte) subList.get(j)).byteValue();
            }
            subAry[i] = subAryItem;
        }
        return subAry;
    }

    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if (bytes == null || bytes.length == 0 || offset < 0 || dateLen <= 0 || offset >= bytes.length || bytes.length - offset < dateLen) {
            return null;
        }
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            return new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static int getHeight4(byte data) {
        return (data & 240) >> 4;
    }

    public static int getLow4(byte data) {
        return data & 15;
    }

    public static int getByte6(byte data) {
        return (data & 240) >> 4;
    }

    public static int getByte8(byte data) {
        return (data & 240) >> 8;
    }

    public static String deletTailChar0(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int ascii : data) {
            if (ascii == 0) {
                break;
            }
            sb.append((char) ascii);
        }
        return sb.toString();
    }

    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1073741824) {
            bytes.append(format.format(((double) size) / 1.073741824E9d)).append("GB");
        } else if (size >= PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            bytes.append(format.format(((double) size) / 1048576.0d)).append("MB");
        } else if (size >= 1024) {
            bytes.append(format.format(((double) size) / 1024.0d)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }
}
