package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class RecyclerViewItemRelayout extends RelativeLayout {
    public RecyclerViewItemRelayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecyclerViewItemRelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewItemRelayout(Context context) {
        super(context);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), (int) (((float) getDefaultSize(0, widthMeasureSpec)) * 0.75f));
        super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE));
    }
}
