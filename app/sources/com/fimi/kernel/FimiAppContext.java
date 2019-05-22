package com.fimi.kernel;

import android.content.Context;

public class FimiAppContext {
    public static final int ALL = 3;
    public static final int DELETE_LOCAL_FILE = 1;
    public static final int PHOTO = 1;
    public static final int UI_DENSITY = 2;
    public static final int UI_HEIGHT = 1280;
    public static final int UI_WIDTH = 720;
    public static final String UUID_NOTIFY = "49535343-1e4d-4bd9-ba61-23c647249616";
    public static final String UUID_READ = "49535343-6daa-4d02-abf6-19569aca69fe";
    public static final String UUID_SERVER = "49535343-fe7d-4ae5-8fa9-9fafd205e455";
    public static final String UUID_WRITE = "49535343-8841-43f4-a8d4-ecbe34729bb3";
    public static final int VIDEO = 0;
    private static Context mContext = null;

    private FimiAppContext() {
    }

    public static void initKernel(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
