package com.fimi.kernel.dataparser.fmlink4;

import android.util.Log;
import com.fimi.kernel.dataparser.milink.ByteArrayToIntArray;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class LinkPayLoad4 {
    public static final int MAX_PAYLOAD_SIZE = 1024;
    private int groupId;
    public int index;
    public ByteBuffer payload = ByteBuffer.allocate(1024);

    public ByteBuffer getData() {
        return this.payload;
    }

    public int size() {
        return this.payload.position();
    }

    public void add(byte c) {
        this.payload.put(c);
    }

    public void resetIndex() {
        this.index = 0;
    }

    public byte getByte() {
        byte result = (byte) ((this.payload.get(this.index + 0) & 255) | (byte) 0);
        this.index++;
        return result;
    }

    public short getShort() {
        short result = (short) ((this.payload.get(this.index + 0) & 255) | ((short) (((this.payload.get(this.index + 1) & 255) << 8) | (short) 0)));
        this.index += 2;
        return result;
    }

    public int getInt() {
        int result = (((0 | ((this.payload.get(this.index + 3) & 255) << 24)) | ((this.payload.get(this.index + 2) & 255) << 16)) | ((this.payload.get(this.index + 1) & 255) << 8)) | (this.payload.get(this.index + 0) & 255);
        this.index += 4;
        return result;
    }

    public long getLong() {
        long result = (((((((0 | ((((long) this.payload.get(this.index + 7)) & 255) << 56)) | ((((long) this.payload.get(this.index + 6)) & 255) << 48)) | ((((long) this.payload.get(this.index + 5)) & 255) << 40)) | ((((long) this.payload.get(this.index + 4)) & 255) << 32)) | ((((long) this.payload.get(this.index + 3)) & 255) << 24)) | ((((long) this.payload.get(this.index + 2)) & 255) << 16)) | ((((long) this.payload.get(this.index + 1)) & 255) << 8)) | (((long) this.payload.get(this.index + 0)) & 255);
        this.index += 8;
        return result;
    }

    public long getLongReverse() {
        long result = (((((((0 | ((((long) this.payload.get(this.index + 0)) & 255) << 56)) | ((((long) this.payload.get(this.index + 1)) & 255) << 48)) | ((((long) this.payload.get(this.index + 2)) & 255) << 40)) | ((((long) this.payload.get(this.index + 3)) & 255) << 32)) | ((((long) this.payload.get(this.index + 4)) & 255) << 24)) | ((((long) this.payload.get(this.index + 5)) & 255) << 16)) | ((((long) this.payload.get(this.index + 6)) & 255) << 8)) | (((long) this.payload.get(this.index + 7)) & 255);
        this.index += 8;
        return result;
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public float getThreeFloat() {
        byte result = (byte) ((this.payload.get(this.index + 2) & 255) | (byte) 0);
        byte result2 = (byte) ((this.payload.get(this.index + 1) & 255) | (byte) 0);
        byte result3 = (byte) ((this.payload.get(this.index + 0) & 255) | (byte) 0);
        BigInteger bigInteger = new BigInteger(1, new byte[]{result, result2, result3});
        this.index += 3;
        return Float.parseFloat(bigInteger.toString());
    }

    public Double getDouble() {
        return Double.valueOf(Double.longBitsToDouble(getLong()));
    }

    public void putByte(byte data) {
        add(data);
    }

    public void putShort(short data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
    }

    public void putChar(char data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
    }

    public char getChar() {
        char result = (char) ((this.payload.get(this.index + 0) & 255) | ((char) (((this.payload.get(this.index + 1) & 255) << 8) | 0)));
        this.index += 2;
        return result;
    }

    public void putuInt32(long data) {
        add((byte) ((int) (data >> null)));
        add((byte) ((int) (data >> 8)));
        add((byte) ((int) (data >> 16)));
        add((byte) ((int) (data >> 24)));
    }

    public long getuInt32() {
        long result = (((0 | ((((long) this.payload.get(this.index + 3)) & 255) << 24)) | ((((long) this.payload.get(this.index + 2)) & 255) << 16)) | ((((long) this.payload.get(this.index + 1)) & 255) << 8)) | (((long) this.payload.get(this.index + 0)) & 255);
        this.index += 4;
        return result;
    }

    public void putThreeByte(int data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
    }

    public void putInt(int data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
    }

    public void putLong(long data) {
        add((byte) ((int) (data >> null)));
        add((byte) ((int) (data >> 8)));
        add((byte) ((int) (data >> 16)));
        add((byte) ((int) (data >> 24)));
        add((byte) ((int) (data >> 32)));
        add((byte) ((int) (data >> 40)));
        add((byte) ((int) (data >> 48)));
        add((byte) ((int) (data >> 56)));
    }

    public void putFloat(float data) {
        putInt(Float.floatToIntBits(data));
    }

    public void putDouble(double data) {
        putLong(Double.doubleToLongBits(data));
    }

    public void setIndex(int x) {
        this.index = x;
    }

    public void putBytes(byte[] data) {
        this.payload.put(data);
    }

    public int getIntCRC(byte[] msg, int offset) {
        int len = size();
        byte[] bytes = new byte[len];
        System.arraycopy(this.payload.array(), 0, bytes, 0, len);
        System.arraycopy(this.payload.array(), 0, msg, offset, len);
        return ByteArrayToIntArray.CRC32Software(bytes, len);
    }

    public int getIntCrc() {
        int len = size();
        byte[] bytes = new byte[len];
        System.arraycopy(this.payload.array(), 0, bytes, 0, len);
        Log.d("moweiru", "crc:" + ByteHexHelper.bytesToHexString(bytes));
        return ByteArrayToIntArray.CRC32Software(bytes, len);
    }

    public int getMsgId() {
        return this.payload.get(1) & 255;
    }

    public int getVer() {
        return this.payload.get(2) & 15;
    }

    public int getMsgRpt() {
        return ((this.payload.get(3) & 255) << 4) | ((this.payload.get(2) >> 4) & 15);
    }

    public int getGroupId() {
        return this.payload.get(0) & 255;
    }

    public void getByteArray(byte[] data) {
        System.arraycopy(this.payload.array(), this.index, data, 0, data.length);
        this.index += data.length;
    }

    public byte[] getPayloadData() {
        int len = size();
        byte[] bytes = new byte[len];
        System.arraycopy(this.payload.array(), 0, bytes, 0, len);
        return bytes;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getString(int nPos) {
        int len = size();
        byte[] datas = new byte[(len - nPos)];
        this.payload.get(datas, nPos, len - nPos);
        return new String(datas);
    }
}
