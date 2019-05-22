package com.fimi.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.fimi.sdk.R;

public class NetworkDialog extends Dialog {
    private int count = 0;
    private boolean isHint;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (NetworkDialog.this.count == 0) {
                    NetworkDialog.this.count = 1;
                    NetworkDialog.this.mTvLoad.setText(R.string.network_loading1);
                } else if (NetworkDialog.this.count == 1) {
                    NetworkDialog.this.count = 2;
                    NetworkDialog.this.mTvLoad.setText(R.string.network_loading2);
                } else {
                    NetworkDialog.this.count = 0;
                    NetworkDialog.this.mTvLoad.setText(R.string.network_loading3);
                }
                NetworkDialog.this.mHandler.sendEmptyMessageDelayed(0, 500);
            }
        }
    };
    private TextView mTvLoad;

    public NetworkDialog(Context context, int theme, boolean isHint) {
        super(context, theme);
        this.isHint = isHint;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fimisdk_dialog_network_loading);
        this.mTvLoad = (TextView) findViewById(R.id.tv_load);
        if (this.isHint) {
            this.mTvLoad.setVisibility(0);
            this.mHandler.sendEmptyMessage(0);
            return;
        }
        this.mTvLoad.setVisibility(8);
    }

    public void dismiss() {
        super.dismiss();
        if (this.isHint) {
            this.mHandler.removeMessages(0);
        }
    }
}
