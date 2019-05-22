package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8TabItem extends LinearLayout implements OnClickListener {
    private int backBg;
    private int curIndex;
    private int lineColor;
    private int lineStroke;
    private OnSelectListener mOnSelectListener;
    private int radius;
    private int selectTabBg;
    private int selectTextColor;
    private int space;
    private int tabHeight;
    private int tabWidth;
    private String[] textArr;
    private float textSize;
    private int unSelectTabBg;
    private int unSelectTextColor;

    public interface OnSelectListener {
        void onSelect(int i, String str);
    }

    public X8TabItem(Context context) {
        this(context, null);
    }

    public X8TabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X8TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.backBg = -1;
        this.unSelectTabBg = -16776961;
        this.selectTabBg = -1;
        this.tabWidth = 80;
        this.tabHeight = -1;
        this.unSelectTextColor = -1;
        this.selectTextColor = -16776961;
        this.textSize = 16.0f;
        this.space = 1;
        this.radius = 0;
        this.curIndex = 1;
        this.textArr = new String[0];
        setOrientation(0);
        readAttr(context, attrs);
        sove();
    }

    private void readAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.X8TabHost);
        this.curIndex = a.getInt(R.styleable.X8TabHost_default_index, 0);
        this.radius = a.getDimensionPixelSize(R.styleable.X8TabHost_radiusC, dpToPx(this.radius));
        this.backBg = a.getColor(R.styleable.X8TabHost_bg, -1);
        this.lineColor = a.getColor(R.styleable.X8TabHost_lineColor, -1);
        this.lineStroke = a.getDimensionPixelSize(R.styleable.X8TabHost_lineStroke, dpToPx(this.lineStroke));
        this.unSelectTabBg = a.getColor(R.styleable.X8TabHost_tab_unselect_color, Color.parseColor("#51B5EF"));
        this.selectTabBg = a.getColor(R.styleable.X8TabHost_tab_select_color, -1);
        this.unSelectTextColor = a.getColor(R.styleable.X8TabHost_text_unselect_color, -1);
        this.selectTextColor = a.getColor(R.styleable.X8TabHost_text_select_color, Color.parseColor("#51B5EF"));
        this.space = a.getDimensionPixelSize(R.styleable.X8TabHost_tab_space, 1);
        this.tabWidth = a.getDimensionPixelSize(R.styleable.X8TabHost_tab_width, dpToPx(this.tabWidth));
        this.tabHeight = a.getDimensionPixelSize(R.styleable.X8TabHost_tab_height, -1);
        this.textSize = a.getDimension(R.styleable.X8TabHost_text_sizeC, this.textSize);
        CharSequence[] arr = a.getTextArray(R.styleable.X8TabHost_src);
        if (arr != null) {
            String[] tArr = new String[arr.length];
            for (int i = 0; i < arr.length; i++) {
                tArr[i] = String.valueOf(arr[i]);
            }
            this.textArr = tArr;
        }
        a.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeWidth != NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE && this.tabWidth > -1) {
            for (i = 0; i < getChildCount(); i++) {
                ((LayoutParams) ((TextView) getChildAt(i)).getLayoutParams()).width = this.tabWidth;
            }
        }
        if (modeHeight != NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE && this.tabHeight > -1) {
            for (i = 0; i < getChildCount(); i++) {
                ((LayoutParams) ((TextView) getChildAt(i)).getLayoutParams()).height = this.tabHeight;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void sove() {
        GradientDrawable dd = new GradientDrawable();
        dd.setCornerRadii(new float[]{(float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius});
        dd.setStroke(this.lineStroke, this.lineColor);
        if (VERSION.SDK_INT >= 16) {
            setBackground(dd);
        } else {
            setBackgroundDrawable(dd);
        }
        removeAllViews();
        if (this.curIndex >= this.textArr.length || this.curIndex < 0) {
            this.curIndex = 0;
        }
        for (int i = 0; i < this.textArr.length; i++) {
            TextView tv = new TextView(getContext());
            LayoutParams params = new LayoutParams(0, -1);
            if (i > 0) {
                params.leftMargin = this.space;
            }
            GradientDrawable d = getFitGradientDrawable(i);
            if (this.curIndex == i) {
                tv.setTextColor(this.selectTextColor);
                d.setColor(this.selectTabBg);
            } else {
                tv.setTextColor(this.unSelectTextColor);
                d.setColor(this.unSelectTabBg);
            }
            tv.setText(this.textArr[i]);
            tv.setGravity(17);
            tv.setTextSize(0, this.textSize);
            if (VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
            params.weight = 1.0f;
            tv.setLayoutParams(params);
            tv.setTag(Integer.valueOf(i));
            tv.setOnClickListener(this);
            addView(tv);
        }
    }

    private GradientDrawable getFitGradientDrawable(int index) {
        GradientDrawable d;
        if (index == 0 && index == this.textArr.length - 1) {
            d = new GradientDrawable();
            d.setCornerRadii(new float[]{(float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius});
            return d;
        } else if (index == 0) {
            d = new GradientDrawable();
            d.setCornerRadii(new float[]{(float) this.radius, (float) this.radius, 0.0f, 0.0f, 0.0f, 0.0f, (float) this.radius, (float) this.radius});
            return d;
        } else if (index == this.textArr.length - 1) {
            d = new GradientDrawable();
            d.setCornerRadii(new float[]{0.0f, 0.0f, (float) this.radius, (float) this.radius, (float) this.radius, (float) this.radius, 0.0f, 0.0f});
            return d;
        } else {
            d = new GradientDrawable();
            d.setCornerRadii(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});
            return d;
        }
    }

    public void onClick(View v) {
        int index = ((Integer) v.getTag()).intValue();
        if (this.mOnSelectListener != null) {
            this.mOnSelectListener.onSelect(index, this.textArr[index]);
        }
    }

    public void setSelect(int index) {
        if (index != this.curIndex) {
            TextView tv = (TextView) getChildAt(this.curIndex);
            tv.setTextColor(this.unSelectTextColor);
            GradientDrawable d = getFitGradientDrawable(this.curIndex);
            d.setColor(this.unSelectTabBg);
            if (VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
            this.curIndex = index;
            tv = (TextView) getChildAt(this.curIndex);
            tv.setTextColor(this.selectTextColor);
            d = getFitGradientDrawable(this.curIndex);
            d.setColor(this.selectTabBg);
            if (VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
        }
    }

    public void upSelect(int index) {
        if (index != this.curIndex) {
            TextView tv = (TextView) getChildAt(this.curIndex);
            tv.setTextColor(this.unSelectTextColor);
            GradientDrawable d = getFitGradientDrawable(this.curIndex);
            d.setColor(this.unSelectTabBg);
            if (VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
            this.curIndex = index;
            tv = (TextView) getChildAt(this.curIndex);
            tv.setTextColor(this.selectTextColor);
            d = getFitGradientDrawable(this.curIndex);
            d.setColor(this.selectTabBg);
            if (VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
            if (this.mOnSelectListener != null) {
                this.mOnSelectListener.onSelect(index, this.textArr[index]);
            }
        }
    }

    public int getSelectIndex() {
        return this.curIndex;
    }

    /* Access modifiers changed, original: 0000 */
    public int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * ((float) dps));
    }

    /* Access modifiers changed, original: 0000 */
    public int spToPx(float spVal) {
        return (int) TypedValue.applyDimension(2, spVal, getResources().getDisplayMetrics());
    }

    public void setOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("zdy", "......" + getWidth() + " " + getHeight());
        if (getWidth() != 0) {
            int count = this.textArr.length;
            Paint p = new Paint();
            p.setColor(this.lineColor);
            p.setStyle(Style.FILL);
            float with = (((float) getWidth()) * 1.0f) / ((float) this.textArr.length);
            for (int i = 1; i < count; i++) {
                RectF r1 = new RectF();
                float nPos = with * ((float) i);
                r1.left = nPos - (((float) this.space) / 2.0f);
                r1.right = (((float) this.space) / 2.0f) + nPos;
                r1.top = (float) (this.lineStroke + 0);
                r1.bottom = (float) (getHeight() - this.lineStroke);
                canvas.drawRect(r1, p);
            }
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(b);
        }
    }

    public void resetCurIndex() {
        this.curIndex = 1;
    }
}
