package com.fimi.app.x8s.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class X8SingleCustomDialog extends Dialog {

    public interface onDialogButtonClickListener {
        void onSingleButtonClick();
    }

    public X8SingleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_view_custom_dialog);
        if (title != null) {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
        ((TextView) findViewById(R.id.tv_message)).setText(message);
        ((Button) findViewById(R.id.tv_sure)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8SingleCustomDialog.this.dismiss();
                if (listener != null) {
                    listener.onSingleButtonClick();
                }
            }
        });
    }
}
