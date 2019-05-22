package com.fimi.player.pragma;

import android.util.Log;
import java.util.Locale;

public class DebugLog {
    public static final boolean ENABLE_DEBUG = true;
    public static final boolean ENABLE_ERROR = true;
    public static final boolean ENABLE_INFO = true;
    public static final boolean ENABLE_VERBOSE = true;
    public static final boolean ENABLE_WARN = true;

    public static int e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }

    public static int efmt(String tag, String fmt, Object... args) {
        return Log.e(tag, String.format(Locale.US, fmt, args));
    }

    public static int i(String tag, String msg) {
        return Log.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    public static int ifmt(String tag, String fmt, Object... args) {
        return Log.i(tag, String.format(Locale.US, fmt, args));
    }

    public static int w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    public static int wfmt(String tag, String fmt, Object... args) {
        return Log.w(tag, String.format(Locale.US, fmt, args));
    }

    public static int d(String tag, String msg) {
        return Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    public static int dfmt(String tag, String fmt, Object... args) {
        return Log.d(tag, String.format(Locale.US, fmt, args));
    }

    public static int v(String tag, String msg) {
        return Log.v(tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return Log.v(tag, msg, tr);
    }

    public static int vfmt(String tag, String fmt, Object... args) {
        return Log.v(tag, String.format(Locale.US, fmt, args));
    }

    public static void printStackTrace(Throwable e) {
        e.printStackTrace();
    }

    public static void printCause(Throwable e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            e = cause;
        }
        printStackTrace(e);
    }
}
