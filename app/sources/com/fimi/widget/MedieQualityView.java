package com.fimi.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.sdk.R;
import java.text.DecimalFormat;

public class MedieQualityView extends FrameLayout {
    int arg1 = 0;
    int arg2 = 0;
    boolean isAnimation = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String arg2Str;
            if (msg.what == 0) {
                arg2Str = new DecimalFormat("0.00").format((double) (((float) MedieQualityView.this.arg2) / 10.0f));
                MedieQualityView.this.tvArg1.setText("" + MedieQualityView.this.arg1);
                MedieQualityView.this.tvArg2.setText(arg2Str + "%");
                MedieQualityView.this.mHandler.sendEmptyMessage(1);
                return;
            }
            arg2Str = new DecimalFormat("0.00").format((double) (((float) MedieQualityView.this.arg2) / 10.0f));
            MedieQualityView.this.tvArg1.setText(" " + MedieQualityView.this.arg1);
            MedieQualityView.this.tvArg2.setText(" " + arg2Str + "%");
            MedieQualityView.this.mHandler.sendEmptyMessage(0);
        }
    };
    TextView tvArg1;
    TextView tvArg2;

    public MedieQualityView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_media_quality, this);
        this.tvArg1 = (TextView) findViewById(R.id.tv_arg1);
        this.tvArg2 = (TextView) findViewById(R.id.tv_arg2);
    }

    public void setMediaQuality(int arg1, int arg2) {
        LogUtil.d("moweiru", "arg1:" + arg1 + ";arg2:" + arg2);
        String arg2Str = new DecimalFormat("0.00").format((double) (((float) arg2) / 10.0f));
        if (this.isAnimation) {
            this.tvArg1.setText("" + arg1);
            this.tvArg2.setText(arg2Str + "%");
            this.isAnimation = false;
            return;
        }
        this.tvArg1.setText("  " + arg1);
        this.tvArg2.setText("  " + arg2Str + "%");
        this.isAnimation = true;
    }

    public void removeMessage() {
        this.mHandler.removeMessages(0);
    }

    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
    }
}
