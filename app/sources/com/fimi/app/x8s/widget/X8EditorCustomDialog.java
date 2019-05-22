package com.fimi.app.x8s.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class X8EditorCustomDialog extends Dialog {

    public interface onDialogButtonClickListener {
        void onLeft();

        void onRight(String str);
    }

    public X8EditorCustomDialog(@NonNull Context context, @Nullable String title, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_editor_dialog_custom);
        if (title != null) {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
        final EditText etView = (EditText) findViewById(R.id.tv_message);
        TextView tvRight = (TextView) findViewById(R.id.btn_right);
        ((TextView) findViewById(R.id.btn_left)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeft();
                }
                X8EditorCustomDialog.this.dismiss();
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRight(etView.getText().toString().trim());
                }
                X8EditorCustomDialog.this.dismiss();
            }
        });
    }
}
