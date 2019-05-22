package com.fimi.kernel.connect;

public class SeqCache {
    private final int MAX_LEN = 100;
    private int index;
    private int[] seqIndex = new int[100];

    public void add2SeqCache(int seq) {
        int[] iArr = this.seqIndex;
        int i = this.index;
        this.index = i + 1;
        iArr[i] = seq;
        if (this.index == 100) {
            this.index = 0;
        }
    }

    public boolean isExist(int seq) {
        for (int s : this.seqIndex) {
            if (seq == s) {
                return true;
            }
        }
        return false;
    }
}
