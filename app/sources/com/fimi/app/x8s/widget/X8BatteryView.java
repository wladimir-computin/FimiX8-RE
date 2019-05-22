package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.fimi.app.x8s.R;

@SuppressLint({"AppCompatCustomView"})
public class X8BatteryView extends ImageView {
    public static int COLOR_ABNORMAL_YELLOW = 0;
    public static int COLOR_NORMAL_WHITE = 0;
    public static int COLOR_SERIOUS_RED = 0;
    public static final int STATE_CORRUPTED = 2;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_OVER_PRESSURE = 1;
    public static final int STATE_OVER_RELEASE = 3;
    public static final int STATE_OVER_RELEASE_SERIOUS = 4;
    private int height;
    private int mColor;
    private int mPower = 100;
    private int width;

    public X8BatteryView(Context context) {
        super(context);
    }

    public X8BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        COLOR_NORMAL_WHITE = getContext().getResources().getColor(R.color.x8_battery_state_normal);
        COLOR_ABNORMAL_YELLOW = getContext().getResources().getColor(R.color.x8_battery_state_abnormal);
        COLOR_SERIOUS_RED = getContext().getResources().getColor(R.color.x8_battery_state_serious);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.X8_RulerView);
        this.mColor = typedArray.getColor(R.styleable.X8_RulerView_batteryColor, -1);
        this.mPower = typedArray.getInt(R.styleable.X8_RulerView_batteryPower, 100);
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
        typedArray.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(this.mColor);
        paint.setStyle(Style.FILL);
        canvas.drawRect(new RectF((float) (this.width / 7), ((float) ((this.width * 3) / 10)) + (((float) ((this.height - ((this.width * 3) / 10)) * (100 - this.mPower))) / 100.0f), (float) (this.width - (this.width / 7)), (float) (this.height - (this.width / 7))), paint);
    }

    public void setPower(int power) {
        this.mPower = power;
        if (this.mPower < 0) {
            this.mPower = 100;
        }
        invalidate();
    }

    public void setState(int state) {
        switch (state) {
            case 0:
                this.mColor = COLOR_NORMAL_WHITE;
                setBackgroundResource(R.drawable.x8_img_battery_setting_normal);
                break;
            case 1:
            case 3:
                this.mColor = COLOR_ABNORMAL_YELLOW;
                setBackgroundResource(R.drawable.x8_img_battery_setting_abnormal);
                break;
            case 2:
            case 4:
                this.mColor = COLOR_SERIOUS_RED;
                setBackgroundResource(R.drawable.x8_img_battery_setting_serious);
                break;
        }
        invalidate();
    }
}
