package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.album.R;

public class DownloadStateView extends View {
    private static int DOWNLOADING_COLOR = Color.parseColor("#38bbff");
    private static int DOWNLOAD_FAIL_COLOR = Color.parseColor("#f23206");
    private Paint mPaint;
    private State mState = State.PAUSE;
    private int sweepAngle = 0;

    public enum State {
        PAUSE,
        DOWNLOADING,
        DOWNLOAD_FAIL
    }

    public DownloadStateView(Context context) {
        super(context);
        initView();
    }

    public DownloadStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DownloadStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(2.0f);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setColor(DOWNLOADING_COLOR);
        setBackgroundResource(R.drawable.album_btn_media_pause);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(2.0f, 2.0f, (float) (getWidth() - 2), (float) (getWidth() - 2));
        if (this.mState == State.PAUSE) {
            setBackgroundResource(R.drawable.album_btn_media_pause);
            canvas.drawArc(rectF, -90.0f, (float) this.sweepAngle, false, this.mPaint);
        } else if (this.mState == State.DOWNLOADING) {
            setBackgroundResource(R.drawable.album_btn_media_download);
            this.mPaint.setColor(DOWNLOADING_COLOR);
            canvas.drawArc(rectF, -90.0f, (float) this.sweepAngle, false, this.mPaint);
        } else if (this.mState == State.DOWNLOAD_FAIL) {
            setBackgroundResource(R.drawable.album_btn_media_redownload);
            this.mPaint.setColor(DOWNLOAD_FAIL_COLOR);
            canvas.drawArc(rectF, -90.0f, (float) this.sweepAngle, false, this.mPaint);
        }
    }

    public void setProgress(int progress) {
        if (this.sweepAngle != Math.round(((float) progress) * 3.6f)) {
            this.sweepAngle = Math.round(((float) progress) * 3.6f);
            invalidate();
        }
    }

    public void setState(State state) {
        if (this.mState != state) {
            this.mState = state;
            invalidate();
        }
    }

    public State getState() {
        return this.mState;
    }
}
