package com.fimi.app.x8s.ui.activity;

import android.app.Activity;
import android.content.Context;
import com.fimi.kernel.utils.LanguageUtil;

public class X8BaseActivity extends Activity {
    /* Access modifiers changed, original: protected */
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase));
    }
}
