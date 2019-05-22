package com.fimi.app.x8s.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class X8DoubleCustomDialog extends Dialog {

    public interface onDialogButtonClickListener {
        void onLeft();

        void onRight();
    }

    public X8DoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        if (title != null) {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
        ((TextView) findViewById(R.id.tv_message)).setText(message);
        TextView tvRight = (TextView) findViewById(R.id.btn_right);
        ((TextView) findViewById(R.id.btn_left)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeft();
                }
                X8DoubleCustomDialog.this.dismiss();
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRight();
                }
                X8DoubleCustomDialog.this.dismiss();
            }
        });
    }

    public X8DoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, String btnRight, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        if (title != null) {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
        ((TextView) findViewById(R.id.tv_message)).setText(message);
        TextView tvLeft = (TextView) findViewById(R.id.btn_left);
        TextView tvRight = (TextView) findViewById(R.id.btn_right);
        tvRight.setText(btnRight);
        tvLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeft();
                }
                X8DoubleCustomDialog.this.dismiss();
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRight();
                }
                X8DoubleCustomDialog.this.dismiss();
            }
        });
    }

    public X8DoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, String btnLeft, String btnRight, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        if (title != null) {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
        ((TextView) findViewById(R.id.tv_message)).setText(message);
        TextView tvLeft = (TextView) findViewById(R.id.btn_left);
        TextView tvRight = (TextView) findViewById(R.id.btn_right);
        tvLeft.setText(btnLeft);
        tvRight.setText(btnRight);
        tvLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeft();
                }
                X8DoubleCustomDialog.this.dismiss();
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRight();
                }
                X8DoubleCustomDialog.this.dismiss();
            }
        });
    }
}
