package com.fimi.x8sdk.modulestate;

import com.fimi.x8sdk.dataparser.AutoFcErrCode;
import com.fimi.x8sdk.entity.X8ErrorCodeInfo;
import java.util.ArrayList;
import java.util.List;

public class ErrorCodeState {
    public static long a = 0;
    public static boolean appTest = false;
    private int atc;
    private int lastAtc;
    private int lastMtc;
    private int lastNfzs;
    private int lastRcs;
    private long lastSystemStatusCodA;
    private long lastSystemStatusCodB;
    private long lastSystemStatusCodC;
    private int mtc;
    private int nfzs;
    private int rcs;
    private long systemStatusCodA;
    private long systemStatusCodB;
    private long systemStatusCodC;

    public static void setErrorCode(long n) {
        a = n;
    }

    private void setErrorCode(long systemStatusCodA, long systemStatusCodB, long systemStatusCodC) {
        if (appTest) {
            systemStatusCodB = a;
        }
        this.systemStatusCodA = systemStatusCodA;
        this.systemStatusCodB = systemStatusCodB;
        this.systemStatusCodC = systemStatusCodC;
    }

    public void getErrorCode() {
        this.lastSystemStatusCodA = this.systemStatusCodA;
        this.lastSystemStatusCodB = this.systemStatusCodB;
        this.lastSystemStatusCodC = this.systemStatusCodC;
    }

    public void setErrorCode(AutoFcErrCode fcErrCode) {
        setErrorCode(fcErrCode.getSystemStatusCodA(), fcErrCode.getSystemStatusCodB(), fcErrCode.getSystemStatusCodC());
    }

    public void setErroCodeAtcAndMtc(int mtc, int atc) {
        this.mtc = mtc & 255;
        this.atc = atc & 255;
    }

    public void setErrorCodeRcs(int rcs) {
        this.rcs = rcs;
    }

    public void setNfzErrorCode(int nfzs) {
        this.nfzs = nfzs;
    }

    public List<X8ErrorCodeInfo> getErrooInfo() {
        int i;
        List<X8ErrorCodeInfo> list = new ArrayList();
        this.lastSystemStatusCodA = this.systemStatusCodA;
        this.lastSystemStatusCodB = this.systemStatusCodB;
        this.lastSystemStatusCodC = this.systemStatusCodC;
        this.lastMtc = this.mtc;
        this.lastAtc = this.atc;
        this.lastRcs = this.rcs;
        this.lastNfzs = this.nfzs;
        if (0 != this.lastSystemStatusCodA) {
            for (i = 0; i < 32; i++) {
                if (((this.lastSystemStatusCodA >> i) & 1) == 1) {
                    list.add(new X8ErrorCodeInfo(0, i));
                }
            }
        }
        if (0 != this.lastSystemStatusCodB) {
            for (i = 0; i < 32; i++) {
                if (((this.lastSystemStatusCodB >> i) & 1) == 1) {
                    list.add(new X8ErrorCodeInfo(1, i));
                }
            }
        }
        if (0 != this.lastSystemStatusCodC) {
            for (i = 0; i < 32; i++) {
                if (((this.lastSystemStatusCodC >> i) & 1) == 1) {
                    list.add(new X8ErrorCodeInfo(2, i));
                }
            }
        }
        if (this.lastMtc != 0) {
            list.add(new X8ErrorCodeInfo(3, this.lastMtc, false));
        }
        if (this.lastAtc != 0) {
            list.add(new X8ErrorCodeInfo(4, this.lastAtc, false));
        }
        if (this.lastRcs != 0) {
            for (i = 0; i < 8; i++) {
                if ((((long) (this.lastRcs >> i)) & 1) == 1) {
                    list.add(new X8ErrorCodeInfo(5, i));
                }
            }
        }
        if (this.lastNfzs != 0) {
            list.add(new X8ErrorCodeInfo(6, this.lastNfzs, false));
        }
        return list;
    }

    public boolean unMountCloud() {
        return ((this.lastSystemStatusCodB & ((long) ((int) Math.pow(2.0d, 15.0d)))) >> 7) == 1;
    }

    public boolean isLowPower() {
        if (((this.lastSystemStatusCodB & ((long) ((int) Math.pow(2.0d, 7.0d)))) >> 7) == 1) {
            return false;
        }
        if (((this.lastSystemStatusCodB & ((long) ((int) Math.pow(2.0d, 11.0d)))) >> 11) == 1) {
            return false;
        }
        return true;
    }

    public boolean isGpsError() {
        int n = (int) Math.pow(2.0d, 0.0d);
        if ((this.lastSystemStatusCodA & ((long) n)) == 1) {
            return true;
        }
        if ((this.lastSystemStatusCodB & ((long) n)) == 1) {
            return true;
        }
        return false;
    }

    public boolean isMagneticError() {
        if (((this.lastSystemStatusCodA & ((long) ((int) Math.pow(2.0d, 20.0d)))) >> 20) == 1) {
            return true;
        }
        return false;
    }

    public boolean isCampError() {
        int i;
        int[] indexA = new int[]{2, 3};
        for (i = 0; i < indexA.length; i++) {
            if (((this.lastSystemStatusCodA & ((long) ((int) Math.pow(2.0d, (double) indexA[i])))) >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = new int[]{1, 3, 28};
        for (i = 0; i < indexB.length; i++) {
            if (((this.lastSystemStatusCodB & ((long) ((int) Math.pow(2.0d, (double) indexB[i])))) >> indexB[i]) == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isImuError() {
        int i;
        int[] indexA = new int[]{4, 5};
        for (i = 0; i < indexA.length; i++) {
            if (((this.lastSystemStatusCodA & ((long) ((int) Math.pow(2.0d, (double) indexA[i])))) >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = new int[]{2, 4};
        for (i = 0; i < indexB.length; i++) {
            if (((this.lastSystemStatusCodB & ((long) ((int) Math.pow(2.0d, (double) indexB[i])))) >> indexB[i]) == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isBatteryError() {
        int i;
        int[] indexA = new int[]{7, 8, 18, 19, 25, 26, 27};
        for (i = 0; i < indexA.length; i++) {
            if (((this.lastSystemStatusCodA & ((long) ((int) Math.pow(2.0d, (double) indexA[i])))) >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = new int[]{6, 7, 8, 9, 10, 11, 23, 27};
        for (i = 0; i < indexB.length; i++) {
            if (((this.lastSystemStatusCodB & ((long) ((int) Math.pow(2.0d, (double) indexB[i])))) >> indexB[i]) == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isGimbalError() {
        int i;
        int[] indexA = new int[]{14, 16};
        for (i = 0; i < indexA.length; i++) {
            if (((this.lastSystemStatusCodA & ((long) ((int) Math.pow(2.0d, (double) indexA[i])))) >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = new int[]{17, 18, 19, 20, 21, 22};
        for (i = 0; i < indexB.length; i++) {
            if (((this.lastSystemStatusCodB & ((long) ((int) Math.pow(2.0d, (double) indexB[i])))) >> indexB[i]) == 1) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        this.systemStatusCodA = 0;
        this.systemStatusCodB = 0;
        this.systemStatusCodC = 0;
        this.mtc = 0;
        this.atc = 0;
        this.rcs = 0;
        this.nfzs = 0;
    }
}
