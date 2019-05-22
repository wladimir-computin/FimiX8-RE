package com.fimi.kernel.fds;

public class FdsCount {
    private int complete;
    private int remainder;
    private int state;
    private int total;

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemainder() {
        return this.remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }

    public int getComplete() {
        return this.complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public void completeIncrease() {
        this.complete++;
    }
}
