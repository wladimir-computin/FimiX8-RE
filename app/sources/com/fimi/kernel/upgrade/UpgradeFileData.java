package com.fimi.kernel.upgrade;

import com.fimi.kernel.dataparser.milink.ByteArrayToIntArray;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.utils.CaCrc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpgradeFileData {
    public static final int PACKETLENGHT = 176;
    private long currentLen;
    byte[] ida_crc;
    byte[] idb_crc;
    byte[] idc_crc;
    private int index;
    Logger logtest = LoggerFactory.getLogger("gh2_communication_log");
    private List<BufferData> mList = new ArrayList();
    private long totalLen;

    public int getIndex() {
        return this.index;
    }

    public void setIndexIncrement() {
        setCurrentLen(((BufferData) this.mList.get(this.index)).playloadLen);
        this.index++;
    }

    public void setIndexDecline() {
        setCurrentLen(-((BufferData) this.mList.get(this.index)).playloadLen);
        this.index--;
    }

    public boolean isFinish() {
        return this.index == this.mList.size();
    }

    public long getTotalLen() {
        return this.totalLen;
    }

    public void setTotalLen(long totalLen) {
        this.totalLen = totalLen;
    }

    public long getCurrentLen() {
        return this.currentLen;
    }

    public void setCurrentLen(int len) {
        this.currentLen += (long) len;
    }

    public int initData(String path, byte[] a, byte[] b, byte[] c) {
        byte[] buf = sourFileData(path);
        if (buf == null) {
            return -1;
        }
        this.ida_crc = a;
        this.idb_crc = b;
        this.idc_crc = c;
        addInformation(buf);
        divideFile(buf);
        showAllData();
        return 1;
    }

    public byte[] sourFileData(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        int len = (int) file.length();
        setTotalLen(file.length());
        byte[] buf = new byte[len];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(buf);
            in.close();
            return buf;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return buf;
        } catch (IOException e2) {
            e2.printStackTrace();
            return buf;
        }
    }

    public List<BufferData> getUpdatePacketData(byte[] fileInputStream, int packetLenght) {
        int packNum;
        List<BufferData> mList = new ArrayList();
        int remainder = fileInputStream.length % packetLenght;
        int divided = fileInputStream.length / packetLenght;
        if (remainder == 0) {
            packNum = divided;
        } else {
            packNum = divided + 1;
        }
        for (int i = 0; i < packNum; i++) {
            BufferData data = new BufferData();
            byte[] buff;
            if (i != packNum - 1 || remainder == 0) {
                buff = new byte[packetLenght];
                System.arraycopy(fileInputStream, i * packetLenght, buff, 0, packetLenght);
                data.data = buff;
                data.len = buff.length;
                data.playloadLen = buff.length;
                mList.add(data);
            } else {
                buff = new byte[remainder];
                System.arraycopy(fileInputStream, i * packetLenght, buff, 0, remainder);
                data.data = buff;
                data.len = buff.length;
                data.playloadLen = remainder;
                mList.add(data);
                System.out.println("-----------");
            }
        }
        return mList;
    }

    public void addInformation(byte[] buf) {
        arrayCopy(16, this.ida_crc, buf);
        arrayCopy(20, this.idb_crc, buf);
        arrayCopy(24, this.idc_crc, buf);
        arrayCopy(48, CaCrc.getbyte((int) CaCrc.CRC16calc(ByteArrayToIntArray.getInt(buf), 12)), buf);
        int[] fileLength = ByteArrayToIntArray.getInt(buf);
        arrayCopy(buf.length - 4, CaCrc.getbyte((int) CaCrc.CRC16calc(fileLength, fileLength.length - 1)), buf);
    }

    public void divideFile(byte[] buf) {
        int slices;
        int remainder = buf.length % 176;
        int divided = buf.length / 176;
        if (remainder == 0) {
            slices = divided;
        } else {
            slices = divided + 1;
        }
        for (int i = 0; i < slices; i++) {
            BufferData data = new BufferData();
            byte[] buff;
            if (i != slices - 1 || remainder == 0) {
                buff = new byte[176];
                System.arraycopy(buf, i * 176, buff, 0, 176);
                data.data = buff;
                data.len = buff.length;
                data.playloadLen = buff.length;
                this.mList.add(data);
            } else {
                buff = new byte[176];
                System.arraycopy(buf, i * 176, buff, 0, remainder);
                data.data = buff;
                data.len = buff.length;
                data.playloadLen = remainder;
                this.mList.add(data);
                System.out.println("-----------");
            }
            ByteHexHelper.bytesToHexString(data.data);
        }
    }

    public void showAllData() {
        for (int i = 0; i < this.mList.size(); i++) {
            BufferData d = (BufferData) this.mList.get(i);
            System.out.println(ByteHexHelper.bytesToHexString(d.data) + " UpgradeFileData-->" + " " + d.len);
        }
    }

    public BufferData getBufferData() {
        return (BufferData) this.mList.get(this.index);
    }

    public List<BufferData> getBufferList() {
        return this.mList;
    }

    public void arrayCopy(int srcPos, byte[] dst, byte[] readfile) {
        for (int i = 0; i < 4; i++) {
            readfile[srcPos + i] = dst[i];
        }
    }
}
