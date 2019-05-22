package com.fimi.app.x8s.test;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;

public class TestJava {
    public static void main(String[] args) {
        System.out.println(byte2float(new byte[]{(byte) 51, (byte) 51, (byte) -64, (byte) -64}, 0));
        System.out.println(2.0f);
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return new byte[]{(byte) (intBits & 255), (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & intBits) >> 8), (byte) ((16711680 & intBits) >> 16), (byte) ((ViewCompat.MEASURED_STATE_MASK & intBits) >> 24)};
    }

    public static float byteToFloat(byte[] arr, int index) {
        return Float.intBitsToFloat((((ViewCompat.MEASURED_STATE_MASK & (arr[index + 0] << 24)) | (16711680 & (arr[index + 1] << 16))) | (MotionEventCompat.ACTION_POINTER_INDEX_MASK & (arr[index + 2] << 8))) | (arr[index + 3] & 255));
    }

    public static float byte2float(byte[] b, int index) {
        return Float.intBitsToFloat((int) (((long) (((int) (((long) (((int) (((long) (b[index + 0] & 255)) | (((long) b[index + 1]) << 8))) & 65535)) | (((long) b[index + 2]) << 16))) & ViewCompat.MEASURED_SIZE_MASK)) | (((long) b[index + 3]) << 24)));
    }
}
