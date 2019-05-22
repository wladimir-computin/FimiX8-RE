package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class X8AiTipWithCloseView extends RelativeLayout implements OnClickListener {
    private boolean isClose;
    private TextView tvTip;
    private View vClose;

    public boolean isClose() {
        return this.isClose;
    }

    public void setClose(boolean close) {
        this.isClose = close;
    }

    public X8AiTipWithCloseView(Context context) {
        super(context);
        initView(context);
    }

    public X8AiTipWithCloseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public X8AiTipWithCloseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.x8_ai_tip_with_close_view, this, true);
        this.tvTip = (TextView) findViewById(R.id.tv_tip);
        this.vClose = findViewById(R.id.tl_close);
        initActions();
    }

    public void initActions() {
        this.vClose.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.tl_close) {
            setVisibility(8);
            this.isClose = true;
        }
    }

    public void setTipText(String text) {
        this.tvTip.setText(text);
    }

    public void showTip() {
        setVisibility(0);
    }

    public boolean isVisibility() {
        return getVisibility() == 0;
    }
}
