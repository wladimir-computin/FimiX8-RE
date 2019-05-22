package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.tools.X8NumberUtil;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8MapPointMarkerViewGroup extends ViewGroup {
    private int defaultMagin;
    private boolean isRelation;
    private boolean isSelect;
    private int lintTop;
    private int magin1;
    private int magin2;
    private int magin3;
    private int paintColor;
    int tempWidth;
    private int textBg;
    private int type;

    public X8MapPointMarkerViewGroup(Context context) {
        this(context, null);
    }

    public X8MapPointMarkerViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X8MapPointMarkerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.defaultMagin = 10;
        getAttrs(context, attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.X8MapPointView);
            this.type = mTypedArray.getInt(R.styleable.X8MapPointView_type, 1);
            this.magin1 = mTypedArray.getDimensionPixelSize(R.styleable.X8MapPointView_margin1, dip2px((float) this.defaultMagin));
            this.magin2 = mTypedArray.getDimensionPixelSize(R.styleable.X8MapPointView_margin2, dip2px((float) this.defaultMagin));
            this.magin3 = mTypedArray.getDimensionPixelSize(R.styleable.X8MapPointView_margin3, dip2px((float) this.defaultMagin));
            mTypedArray.recycle();
        }
        this.paintColor = getContext().getResources().getColor(R.color.black_65);
        this.textBg = R.drawable.x8_ai_follow_marker_info_bg;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.type == 1) {
            onLayoutForTyp1(changed, l, t, r, b);
        } else if (this.type == 2) {
            onLayoutForTyp2(changed, l, t, r, b);
        } else if (this.type == 3) {
            onLayoutForTyp3(changed, l, t, r, b);
        } else {
            onLayoutForTyp4(changed, l, t, r, b);
        }
    }

    public void onLayoutForTyp4(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    bottom = 0 + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    bottom = 0 + h;
                }
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin1;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin1;
                    bottom = top + h;
                }
                countH = bottom;
            } else if (i == 2) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin2;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin2;
                    bottom = top + h;
                }
                countH = bottom;
            } else if (i == 3) {
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = getChildAt(2).getMeasuredHeight();
                top = (int) ((((float) countH) - (0.5f * ((float) tmeph))) - (0.5f * ((float) h)));
                bottom = (int) ((((float) countH) - (0.5f * ((float) tmeph))) + (0.5f * ((float) h)));
                float d = (((float) countH) - (0.5f * ((float) tmeph))) / ((float) getMeasuredHeight());
            }
            child.layout(left, top, right, bottom);
        }
    }

    public void onLayoutForTyp3(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    bottom = 0 + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    bottom = 0 + h;
                }
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin1;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin1;
                    bottom = top + h;
                }
                countH = bottom;
            } else if (i == 2) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin2;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin2;
                    bottom = top + h;
                }
                countH = bottom;
            } else if (i == 3) {
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = getChildAt(2).getMeasuredHeight();
                top = (int) ((((float) countH) - (0.5f * ((float) tmeph))) - (0.5f * ((float) h)));
                bottom = (int) ((((float) countH) - (0.5f * ((float) tmeph))) + (0.5f * ((float) h)));
                float d = (((float) countH) - (0.5f * ((float) tmeph))) / ((float) getMeasuredHeight());
            } else if (i == 4) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin3;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin3;
                    bottom = top + h;
                }
                countH = bottom;
            }
            child.layout(left, top, right, bottom);
        }
    }

    public void onLayoutForTyp2(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    bottom = 0 + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    bottom = 0 + h;
                }
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin2;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin2;
                    bottom = top + h;
                }
                countH = bottom;
            } else if (i == 2) {
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = getChildAt(1).getMeasuredHeight();
                top = (int) ((((float) countH) - (0.5f * ((float) tmeph))) - (0.5f * ((float) h)));
                bottom = (int) ((((float) countH) - (0.5f * ((float) tmeph))) + (0.5f * ((float) h)));
                float d = (((float) countH) - (0.5f * ((float) tmeph))) / ((float) getMeasuredHeight());
            }
            child.layout(left, top, right, bottom);
        }
    }

    public void onLayoutForTyp1(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    bottom = 0 + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    bottom = 0 + h;
                }
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin2;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin2;
                    bottom = top + h;
                }
                countH = bottom;
            } else if (i == 2) {
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = getChildAt(1).getMeasuredHeight();
                top = (int) ((((float) countH) - (0.5f * ((float) tmeph))) - (0.5f * ((float) h)));
                bottom = (int) ((((float) countH) - (0.5f * ((float) tmeph))) + (0.5f * ((float) h)));
                float dd = (((float) countH) - (0.5f * ((float) tmeph))) / ((float) getMeasuredHeight());
                this.lintTop = top;
            } else if (i == 3) {
                if (this.tempWidth == w) {
                    right = 0 + w;
                    top = countH + this.magin3;
                    bottom = top + h;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                    top = countH + this.magin3;
                    bottom = top + h;
                }
                countH = bottom;
            }
            child.layout(left, top, right, bottom);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        Paint p = new Paint();
        if (this.isSelect) {
            this.paintColor = getContext().getResources().getColor(R.color.colorAccent);
            this.textBg = R.drawable.x8_ai_follow_marker_info_select_bg;
        } else {
            this.paintColor = getContext().getResources().getColor(R.color.black_65);
            this.textBg = R.drawable.x8_ai_follow_marker_info_bg;
        }
        p.setColor(this.paintColor);
        p.setStrokeWidth(5.0f);
        View v1;
        View v2;
        if (this.type == 1) {
            TextView v12 = (TextView) getChildAt(0);
            TextView v22 = (TextView) getChildAt(3);
            v12.setBackgroundResource(this.textBg);
            int startY = v12.getMeasuredHeight();
            int endY = getMeasuredHeight() - v22.getMeasuredHeight();
            if (this.isRelation) {
                v22.setBackgroundResource(R.drawable.x8_ai_follow_marker_info_select_bg);
                canvas.drawLine((float) (getMeasuredWidth() / 2), (float) startY, (float) (getMeasuredWidth() / 2), (float) (endY - this.lintTop), p);
                p.setColor(getContext().getResources().getColor(R.color.colorAccent));
                canvas.drawLine((float) (getMeasuredWidth() / 2), (float) this.lintTop, (float) (getMeasuredWidth() / 2), (float) endY, p);
            } else {
                v22.setBackgroundResource(this.textBg);
                canvas.drawLine((float) (getMeasuredWidth() / 2), (float) startY, (float) (getMeasuredWidth() / 2), (float) endY, p);
            }
        } else if (this.type == 2) {
            v1 = getChildAt(0);
            v2 = getChildAt(1);
            v1.setBackgroundResource(this.textBg);
            canvas.drawLine((float) (getMeasuredWidth() / 2), (float) v1.getMeasuredHeight(), (float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() - (v2.getMeasuredHeight() / 2)), p);
        } else if (this.type == 3) {
            v1 = getChildAt(0);
            v2 = getChildAt(4);
            getChildAt(1).setBackgroundResource(this.textBg);
            v2.setBackgroundResource(this.textBg);
            canvas.drawLine((float) (getMeasuredWidth() / 2), (float) (v1.getMeasuredHeight() / 2), (float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() - v2.getMeasuredHeight()), p);
        } else if (this.type == 4) {
            v1 = getChildAt(0);
            v2 = getChildAt(2);
            getChildAt(1).setBackgroundResource(this.textBg);
            canvas.drawLine((float) (getMeasuredWidth() / 2), (float) (v1.getMeasuredHeight() / 2), (float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() - (v2.getMeasuredHeight() / 2)), p);
        }
        super.onDraw(canvas);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int count = getChildCount();
        int i = 0;
        while (i < count) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            int w = childAt.getMeasuredWidth();
            int h = childAt.getMeasuredHeight();
            if (this.type == 1 || this.type == 2) {
                if (i != 2) {
                    if (w > this.tempWidth) {
                        this.tempWidth = w;
                    }
                    height += h;
                }
            } else if ((this.type == 3 || this.type == 4) && i != 3) {
                if (w > this.tempWidth) {
                    this.tempWidth = w;
                }
                height += h;
            }
            i++;
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec, this.tempWidth), measureHeight(heightMeasureSpec, calcAllHeight(height)));
    }

    public int calcAllHeight(int height) {
        if (this.type == 1) {
            return (this.magin2 + height) + this.magin3;
        }
        if (this.type == 2) {
            return height + this.magin2;
        }
        if (this.type == 3) {
            return ((this.magin1 + height) + this.magin2) + this.magin3;
        }
        if (this.type == 4) {
            return (this.magin1 + height) + this.magin2;
        }
        return height;
    }

    private int measureHeight(int measureSpec, int height) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
            return size;
        }
        int result = height;
        if (mode == Integer.MIN_VALUE) {
            return Math.min(result, size);
        }
        return result;
    }

    public int dip2px(float dipValue) {
        return (int) ((dipValue * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    private int measureWidth(int measureSpec, int width) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
            return size;
        }
        int result = width;
        if (mode == Integer.MIN_VALUE) {
            return Math.min(result, size);
        }
        return result;
    }

    public void setAngle(float angle) {
        ((ImageView) getChildAt(1)).setRotation(angle);
        postInvalidate();
    }

    public void setValueWithPio(int bottomRes, float heightVale, int i, int poi, float angle, boolean select, boolean isRelation) {
        ((TextView) getChildAt(0)).setText("" + X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ((ImageView) getChildAt(1)).setBackgroundResource(bottomRes);
        ((ImageView) getChildAt(1)).setRotation(angle);
        ((TextView) getChildAt(2)).setText("" + i);
        ((TextView) getChildAt(3)).setText("" + poi);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setValueNoPio(int bottomRes, float heightVale, int i, float angle, boolean select, boolean isRelation) {
        ((TextView) getChildAt(0)).setText("" + X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ((ImageView) getChildAt(1)).setBackgroundResource(bottomRes);
        ((ImageView) getChildAt(1)).setRotation(angle);
        ((TextView) getChildAt(2)).setText("" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setPioValue(int bottomRes, float heightVale, int i, boolean select, boolean isRelation) {
        ((TextView) getChildAt(0)).setText("" + X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ((ImageView) getChildAt(1)).setBackgroundResource(bottomRes);
        ((TextView) getChildAt(2)).setText("");
        ((TextView) getChildAt(3)).setText("POI" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setPointEventValue(int topRes, int bottomRes, float heightVale, int i, float angle, boolean select, boolean isRelation) {
        ((ImageView) getChildAt(0)).setImageResource(topRes);
        ((TextView) getChildAt(1)).setText("" + X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ((ImageView) getChildAt(2)).setBackgroundResource(bottomRes);
        ((ImageView) getChildAt(2)).setRotation(angle);
        ((TextView) getChildAt(3)).setText("" + i);
        ((TextView) getChildAt(4)).setText("" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setPointEventNoPioValue(int topRes, int bottomRes, float heightVale, int i, float angle, boolean select, boolean isRelation) {
        ((ImageView) getChildAt(0)).setImageResource(topRes);
        ((TextView) getChildAt(1)).setText("" + X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ((ImageView) getChildAt(2)).setBackgroundResource(bottomRes);
        ((ImageView) getChildAt(2)).setRotation(angle);
        ((TextView) getChildAt(3)).setText("" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }
}
