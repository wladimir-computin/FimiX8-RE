package com.fimi.player;

public interface FimiLibLoader {
    void loadLibrary(String str) throws UnsatisfiedLinkError, SecurityException;
}
