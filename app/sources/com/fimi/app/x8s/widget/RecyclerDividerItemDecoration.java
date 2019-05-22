package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class RecyclerDividerItemDecoration extends ItemDecoration {
    public static final int HORIZONTAL_LIST = 0;
    public static final int VERTICAL_LIST = 1;
    private int mDividerHeight = 2;
    private int mOrientation;
    private Paint mPaint;

    public RecyclerDividerItemDecoration(Context context, int orientation, int dividerHeight, int dividerColor) {
        setOrientation(orientation);
        this.mDividerHeight = dividerHeight;
        this.mPaint = new Paint(1);
        this.mPaint.setColor(context.getResources().getColor(dividerColor));
        this.mPaint.setStyle(Style.STROKE);
    }

    public void setOrientation(int orientation) {
        if (orientation == 0 || orientation == 1) {
            this.mOrientation = orientation;
            return;
        }
        throw new IllegalArgumentException("invalid orientation");
    }

    public void onDraw(Canvas c, RecyclerView parent) {
        if (this.mOrientation == 1) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, State state) {
        super.onDraw(c, parent, state);
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            int top = child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin;
            int bottom = top + this.mDividerHeight;
            if (this.mPaint != null) {
                c.drawRect((float) left, (float) top, (float) right, (float) bottom, this.mPaint);
            }
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            int left = child.getRight() + ((LayoutParams) child.getLayoutParams()).rightMargin;
            int right = left + this.mDividerHeight;
            if (this.mPaint != null) {
                c.drawRect((float) left, (float) top, (float) right, (float) bottom, this.mPaint);
            }
        }
    }

    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (this.mOrientation == 1) {
            outRect.set(0, 0, 0, this.mDividerHeight);
        } else {
            outRect.set(0, 0, this.mDividerHeight, 0);
        }
    }
}
