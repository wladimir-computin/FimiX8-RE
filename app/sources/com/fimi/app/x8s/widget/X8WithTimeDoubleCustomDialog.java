package com.fimi.app.x8s.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;

public class X8WithTimeDoubleCustomDialog extends Dialog {
    private int i = 10;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (X8WithTimeDoubleCustomDialog.this.i >= 0) {
                X8WithTimeDoubleCustomDialog.this.tvRight.setText(String.format(X8WithTimeDoubleCustomDialog.this.prex, new Object[]{Integer.valueOf(X8WithTimeDoubleCustomDialog.this.i = X8WithTimeDoubleCustomDialog.this.i - 1)}));
                X8WithTimeDoubleCustomDialog.this.mHandler.sendEmptyMessageDelayed(0, 1000);
                return;
            }
            X8WithTimeDoubleCustomDialog.this.dismiss();
        }
    };
    private String prex;
    final TextView tvRight;

    public X8WithTimeDoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        this.prex = context.getString(R.string.x8_battery_ok_time_tip);
        if (title != null) {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
        ((TextView) findViewById(R.id.tv_message)).setText(message);
        TextView tvLeft = (TextView) findViewById(R.id.btn_left);
        this.tvRight = (TextView) findViewById(R.id.btn_right);
        tvLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeft();
                }
                X8WithTimeDoubleCustomDialog.this.dismiss();
            }
        });
        this.tvRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRight();
                }
                X8WithTimeDoubleCustomDialog.this.dismiss();
            }
        });
        this.tvRight.setText(String.format(this.prex, new Object[]{Integer.valueOf(this.i)}));
    }

    public void show() {
        super.show();
        this.mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public void dismiss() {
        super.dismiss();
        this.mHandler.removeMessages(0);
    }
}
