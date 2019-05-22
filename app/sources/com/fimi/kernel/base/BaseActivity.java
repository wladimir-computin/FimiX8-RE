package com.fimi.kernel.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import com.fimi.kernel.utils.LanguageUtil;
import com.fimi.kernel.utils.StatusBarUtil;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected int marginStatus;
    Bundle savedInstanceState;
    protected int statusBarHeight;

    public abstract void doTrans();

    public abstract int getContentViewLayoutID();

    public abstract void initData();

    public abstract void setStatusBarColor();

    /* Access modifiers changed, original: protected */
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase));
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.savedInstanceState = savedInstanceState;
        BaseAppManager.getInstance().addActivity(this);
        setStatusBarColor();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getBundleExtras(extras);
        }
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            int height = getResources().getDisplayMetrics().heightPixels;
            this.statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
            this.marginStatus = (height * 18) / 1920;
            initData();
            doTrans();
            return;
        }
        throw new IllegalArgumentException("You must return a right contentView layout resource Id");
    }

    public Bundle getSavedInstanceState() {
        return this.savedInstanceState;
    }

    /* Access modifiers changed, original: protected */
    public void getBundleExtras(Bundle extras) {
    }

    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
    }

    /* Access modifiers changed, original: protected */
    public void readyService(Class<?> clazz) {
        startService(new Intent(this, clazz));
    }

    /* Access modifiers changed, original: protected */
    public void readyGo(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
        overridePendingTransition(17432576, 17432577);
    }

    /* Access modifiers changed, original: protected */
    public void readyGo(Class<?> clazz, String target, boolean b) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(target, b);
        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
    }

    /* Access modifiers changed, original: protected */
    public void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
    }

    /* Access modifiers changed, original: protected */
    public void readyGoThenKill(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
        finish();
        overridePendingTransition(17432576, 17432577);
    }

    /* Access modifiers changed, original: protected */
    public void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
        finish();
    }

    /* Access modifiers changed, original: protected */
    public void readyGoThenKillAllActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(268468224);
        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
        finish();
    }

    /* Access modifiers changed, original: protected */
    public void readyGoThenKillAllActivity(Intent intent) {
        intent.setFlags(268468224);
        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
        finish();
    }

    /* Access modifiers changed, original: protected */
    public void readyGoThenKillAllActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(268468224);
        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
        finish();
    }

    /* Access modifiers changed, original: protected */
    public void readyGoForResult(Class<?> clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }

    /* Access modifiers changed, original: protected */
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    @RequiresApi(api = 24)
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
