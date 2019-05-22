package com.fimi.libperson;

import android.view.View;
import android.view.View.OnClickListener;
import com.fimi.kernel.base.BaseActivity;

public abstract class BasePersonActivity extends BaseActivity implements OnClickListener {
    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* Access modifiers changed, original: protected */
    public void initMVP() {
    }

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }

    public void onClick(View view) {
    }
}
