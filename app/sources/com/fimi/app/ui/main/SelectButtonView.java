package com.fimi.app.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.fimi.android.app.R;
import com.fimi.kernel.utils.DensityUtil;

public class SelectButtonView extends FrameLayout {
    Button[] buttons;
    int length;
    Context mContext;

    public SelectButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void initView(int length) {
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        LinearLayout layout = new LinearLayout(this.mContext);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(0);
        this.buttons = new Button[length];
        this.length = length;
        for (int i = 0; i < length; i++) {
            LayoutParams btnLayoutParams = new LayoutParams(DensityUtil.dip2px(this.mContext, 13.0f), DensityUtil.dip2px(this.mContext, 3.0f));
            Button button = new Button(this.mContext);
            button.setBackgroundResource(R.drawable.host_main_btn_selected);
            btnLayoutParams.leftMargin = DensityUtil.dip2px(this.mContext, 6.0f);
            button.setLayoutParams(btnLayoutParams);
            this.buttons[i] = button;
            layout.addView(button);
        }
        addView(layout);
    }

    public void setProductPositon(int positon) {
        int i = 0;
        while (i < this.length) {
            this.buttons[i].setSelected(i == positon);
            i++;
        }
    }
}
