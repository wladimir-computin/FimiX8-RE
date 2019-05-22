package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.fimi.app.x8s.R;

public class X8FollowSpeedView extends View {
    private int cursorColor = R.color.x8_follow_speed_line_cursor;
    private int cursorH;
    private int cursorW;
    private boolean isClick;
    private boolean isInit;
    private boolean isRight = true;
    private boolean isSet;
    private int lineBgColor = R.color.x8_follow_speed_line_bg;
    private int lineH;
    private OnChangeListener listener;
    private Paint paint;
    private int panding;
    private float pos;
    private int progessColor = R.color.x8_follow_speed_line_progess;
    private RectF rectF;
    private int s;
    private int thumW;
    private int v;
    private int w;

    public interface OnChangeListener {
        void onChange(float f, boolean z);

        void onSendData();
    }

    public X8FollowSpeedView(Context context) {
        super(context);
        initView(context);
    }

    public X8FollowSpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public X8FollowSpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.lineH = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_line_h);
        this.cursorW = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_cursor_w);
        this.cursorH = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_cursor_h);
        this.thumW = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_thum_w);
        this.panding = this.thumW / 2;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.w = getWidth();
        int h = getHeight();
        if (this.w > 0) {
            if (!this.isInit) {
                setInitSpeed(this.s, this.v);
                this.isInit = true;
            }
            this.rectF = new RectF((float) (this.panding + 0), (float) ((h / 2) - (this.lineH / 2)), (float) (this.w - this.panding), (float) ((h / 2) + (this.lineH / 2)));
            this.paint.setColor(getContext().getResources().getColor(this.lineBgColor));
            canvas.drawRoundRect(this.rectF, 3.0f, 3.0f, this.paint);
            if (this.isRight) {
                this.rectF = new RectF((float) (this.w / 2), (float) ((h / 2) - (this.lineH / 2)), ((float) (this.w / 2)) + this.pos, (float) ((h / 2) + (this.lineH / 2)));
            } else {
                this.rectF = new RectF(((float) (this.w / 2)) - this.pos, (float) ((h / 2) - (this.lineH / 2)), (float) (this.w / 2), (float) ((h / 2) + (this.lineH / 2)));
            }
            this.paint.setColor(getContext().getResources().getColor(this.progessColor));
            canvas.drawRect(this.rectF, this.paint);
            this.paint.setColor(getContext().getResources().getColor(this.cursorColor));
            if (this.isRight) {
                canvas.drawCircle(this.rectF.right, (float) (h / 2), (float) (this.thumW / 2), this.paint);
            } else {
                canvas.drawCircle(this.rectF.left, (float) (h / 2), (float) (this.thumW / 2), this.paint);
            }
            this.rectF = new RectF((float) ((this.w / 2) - (this.cursorW / 2)), (float) ((h / 2) - (this.cursorH / 2)), (float) ((this.w / 2) + (this.cursorW / 2)), (float) ((h / 2) + (this.cursorH / 2)));
            this.paint.setColor(getContext().getResources().getColor(this.cursorColor));
            canvas.drawRoundRect(this.rectF, 3.0f, 3.0f, this.paint);
            if (this.listener != null) {
                if (this.isSet) {
                    this.listener.onChange(this.v == 0 ? 0.0f : (((float) Math.abs(this.s)) * 1.0f) / ((float) this.v), this.isRight);
                    this.isSet = false;
                } else {
                    this.listener.onChange((this.pos * 1.0f) / ((float) ((this.w / 2) - this.panding)), this.isRight);
                }
            }
            if (this.isClick) {
                this.isClick = false;
                if (this.listener != null) {
                    this.listener.onSendData();
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 1:
                if (this.listener != null) {
                    this.listener.onSendData();
                    break;
                }
                break;
            case 2:
                calculProgress(event.getX());
                break;
        }
        return true;
    }

    private void calculProgress(float x) {
        if (x >= ((float) (this.w / 2))) {
            if (x > ((float) (this.w - this.panding))) {
                x = (float) (this.w - this.panding);
            }
            this.isRight = true;
            this.pos = x - ((float) (this.w / 2));
        } else {
            if (x < ((float) (this.panding + 0))) {
                x = (float) (this.panding + 0);
            }
            this.isRight = false;
            this.pos = ((float) (this.w / 2)) - x;
        }
        postInvalidate();
    }

    public void setOnSpeedChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public void setLeftClick(int v, int MAX, int MIN) {
        if (this.isRight) {
            if (v < MIN) {
                this.isRight = false;
            }
            v -= 10;
        } else {
            v -= 10;
            if (Math.abs(v) > MAX - MIN) {
                v = -(MAX - MIN);
            }
        }
        setSpeedByMeasure(v, MAX - MIN);
        setInitSpeed(v, MAX - MIN);
        this.isClick = true;
        postInvalidate();
    }

    public void setRightClick(int v, int MAX, int MIN) {
        if (this.isRight) {
            v += 10;
            if (v > MAX - MIN) {
                v = MAX - MIN;
            }
        } else {
            if (Math.abs(v) < MIN) {
                this.isRight = true;
            }
            v += 10;
        }
        setSpeedByMeasure(v, MAX - MIN);
        setInitSpeed(v, MAX - MIN);
        this.isClick = true;
        postInvalidate();
    }

    public void setSpeed(int s, int v) {
        setSpeedByMeasure(s, v);
        if (this.isInit) {
            if (s >= 0) {
                this.isRight = true;
            } else {
                s = -s;
                this.isRight = false;
            }
            float x = (((float) s) * 1.0f) / ((float) v);
            this.pos = ((((float) ((this.w / 2) - this.panding)) * 1.0f) * ((float) s)) / ((float) v);
            postInvalidate();
        }
    }

    private void setSpeedByMeasure(int s, int v) {
        this.s = s;
        this.v = v;
        this.isSet = true;
    }

    private void setInitSpeed(int s, int v) {
        if (s >= 0) {
            this.isRight = true;
        } else {
            s = -s;
            this.isRight = false;
        }
        this.pos = ((((float) ((this.w / 2) - this.panding)) * 1.0f) * ((float) s)) / ((float) v);
    }
}
