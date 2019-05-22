package com.fimi.album.biz;

public class X9HandleType {
    public static FragmentType fragmentType = FragmentType.CAMERA;

    public enum FragmentType {
        CAMERA,
        LOCAL_MEDIA_LIB
    }

    public static boolean isCameraView() {
        return fragmentType == FragmentType.CAMERA;
    }
}
