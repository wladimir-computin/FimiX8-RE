package com.fimi.libperson.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.libperson.R;

public class TitleView extends FrameLayout {
    private ImageView mIvLeft = ((ImageView) findViewById(R.id.iv_return));
    private TextView mTvRight = ((TextView) findViewById(R.id.tv_right));
    private TextView mTvTitle = ((TextView) findViewById(R.id.tv_title));

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.sub_login_title, this);
        this.mIvLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((Activity) TitleView.this.getContext()).finish();
            }
        });
        FontUtil.changeFontLanTing(context.getAssets(), this.mTvRight, this.mTvTitle);
    }

    public void setTvTitle(String text) {
        this.mTvTitle.setText(text);
    }

    public void setTvRightText(String rightText) {
        this.mTvRight.setText(rightText);
    }

    public void setTvRightVisible(int visible) {
        this.mTvRight.setVisibility(visible);
    }

    public void setIvLeftListener(OnClickListener l) {
        this.mIvLeft.setOnClickListener(l);
    }

    public void setTvRightListener(OnClickListener l) {
        this.mTvRight.setOnClickListener(l);
    }

    public void setRightTvIsVisible(boolean isVisible) {
        this.mTvRight.setVisibility(isVisible ? 0 : 4);
    }
}
