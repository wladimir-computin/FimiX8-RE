package com.fimi.kernel.utils;

public class BitUtil {
    public static int getBitByByte(int num, int index) {
        return Math.abs(((1 << index) & num) >> index);
    }

    public static int getBitByByte2(int num, int index) {
        return (num >> (index - 1)) & 1;
    }

    public static void main(String[] args) {
        System.out.println("====" + Integer.toBinaryString(3));
        System.out.print("=======>" + getBitByByte2(3, 1));
    }
}
