package com.fimi.kernel.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontUtil {
    private static Typeface lantingTF = null;
    private static Typeface sDINAlernateBold = null;

    public static Typeface getTf(AssetManager mgr) {
        if (lantingTF == null) {
            lantingTF = Typeface.createFromAsset(mgr, "lanting.TTF");
        }
        return lantingTF;
    }

    public static Typeface getDINAlernateBold(AssetManager mgr) {
        if (sDINAlernateBold == null) {
            sDINAlernateBold = Typeface.createFromAsset(mgr, "DIN_Alternate_Bold.TTF");
        }
        return sDINAlernateBold;
    }

    public static void changeDINAlernateBold(AssetManager mgr, View... root) {
        if (sDINAlernateBold == null) {
            sDINAlernateBold = Typeface.createFromAsset(mgr, "DIN_Alternate_Bold.TTF");
        }
        for (View v : root) {
            if (v instanceof Button) {
                ((Button) v).setTypeface(sDINAlernateBold);
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(sDINAlernateBold);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(sDINAlernateBold);
            }
        }
    }

    public static void changeFontLanTing(AssetManager mgr, View... root) {
        if (lantingTF == null) {
            lantingTF = Typeface.createFromAsset(mgr, "lanting.TTF");
        }
        for (View v : root) {
            if (v instanceof Button) {
                ((Button) v).setTypeface(lantingTF);
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(lantingTF);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(lantingTF);
            }
        }
    }

    public static void changeFontLanTingBlod(AssetManager mgr, View... root) {
        if (lantingTF == null) {
            lantingTF = Typeface.createFromAsset(mgr, "MILanTing_Bold.ttf");
        }
        for (View v : root) {
            if (v instanceof Button) {
                ((Button) v).setTypeface(lantingTF);
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(lantingTF);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(lantingTF);
            }
        }
    }

    public static void changeViewLanTing(AssetManager mgr, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                if (viewchild.getClass().equals(TextView.class)) {
                    changeFontLanTing(mgr, viewchild);
                }
                changeViewLanTing(mgr, viewchild);
            }
        }
    }
}
