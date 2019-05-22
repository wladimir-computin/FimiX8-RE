package com.fimi.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.fimi.sdk.R;

public class CustomLoadDialog extends Dialog {
    public CustomLoadDialog(@NonNull Context context) {
        super(context);
    }

    public CustomLoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomLoadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fimisdk_dialog_loading);
    }
}
