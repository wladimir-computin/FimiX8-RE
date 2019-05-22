package com.fimi.widget.sticklistview.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

class WrapperView extends ViewGroup {
    Drawable mDivider;
    int mDividerHeight;
    View mHeader;
    View mItem;
    int mItemTop;

    public WrapperView(Context c) {
        super(c);
    }

    /* Access modifiers changed, original: 0000 */
    public void update(View item, View header, Drawable divider, int dividerHeight) {
        if (item == null) {
            throw new NullPointerException("List view item must not be null.");
        }
        if (this.mItem != item) {
            removeView(this.mItem);
            this.mItem = item;
            Object parent = item.getParent();
            if (!(parent == null || parent == this || !(parent instanceof ViewGroup))) {
                ((ViewGroup) parent).removeView(item);
            }
            addView(item);
        }
        if (this.mHeader != header) {
            if (this.mHeader != null) {
                removeView(this.mHeader);
            }
            this.mHeader = header;
            if (header != null) {
                addView(header);
            }
        }
        if (this.mDivider != divider) {
            this.mDivider = divider;
            this.mDividerHeight = dividerHeight;
            invalidate();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasHeader() {
        return this.mHeader != null;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams params;
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE);
        int measuredHeight = 0;
        if (this.mHeader != null) {
            params = this.mHeader.getLayoutParams();
            if (params == null || params.height <= 0) {
                this.mHeader.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(0, 0));
            } else {
                this.mHeader.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(params.height, NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE));
            }
            measuredHeight = 0 + this.mHeader.getMeasuredHeight();
        } else if (this.mDivider != null) {
            measuredHeight = 0 + this.mDividerHeight;
        }
        params = this.mItem.getLayoutParams();
        if (params == null || params.height <= 0) {
            this.mItem.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(0, 0));
        } else {
            this.mItem.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(params.height, NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE));
        }
        setMeasuredDimension(measuredWidth, measuredHeight + this.mItem.getMeasuredHeight());
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        r = getWidth();
        b = getHeight();
        if (this.mHeader != null) {
            int headerHeight = this.mHeader.getMeasuredHeight();
            this.mHeader.layout(0, 0, r, headerHeight);
            this.mItemTop = headerHeight;
            this.mItem.layout(0, headerHeight, r, b);
        } else if (this.mDivider != null) {
            this.mDivider.setBounds(0, 0, r, this.mDividerHeight);
            this.mItemTop = this.mDividerHeight;
            this.mItem.layout(0, this.mDividerHeight, r, b);
        } else {
            this.mItemTop = 0;
            this.mItem.layout(0, 0, r, b);
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mHeader == null && this.mDivider != null) {
            if (VERSION.SDK_INT < 11) {
                canvas.clipRect(0, 0, getWidth(), this.mDividerHeight);
            }
            this.mDivider.draw(canvas);
        }
    }
}
