package com.fimi.soul.media.player;

public interface FimiLibLoader {
    void loadLibrary(String str) throws UnsatisfiedLinkError, SecurityException;
}
