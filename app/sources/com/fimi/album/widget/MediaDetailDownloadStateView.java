package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.example.album.R;
import com.fimi.kernel.utils.AbViewUtil;
import com.fimi.kernel.utils.FontUtil;

public class MediaDetailDownloadStateView extends View {
    private static int DOWNLOADING_COLOR = Color.parseColor("#38bbff");
    private static int DOWNLOAD_FAIL_COLOR = Color.parseColor("#f23206");
    private static int TEXT_COLOR = Color.parseColor("#ffffffff");
    private Context mContext;
    private Paint mPaint;
    private com.fimi.album.widget.DownloadStateView.State mState = com.fimi.album.widget.DownloadStateView.State.PAUSE;
    private Paint mTextPaint;
    private int sweepAngle = 0;

    public enum State {
        PAUSE,
        DOWNLOADING,
        DOWNLOAD_FAIL
    }

    public MediaDetailDownloadStateView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public MediaDetailDownloadStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public MediaDetailDownloadStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(2.0f);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setColor(DOWNLOADING_COLOR);
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(AbViewUtil.dip2px(this.mContext, 12.0f));
        this.mTextPaint.setColor(TEXT_COLOR);
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.mTextPaint.setTypeface(FontUtil.getDINAlernateBold(this.mContext.getAssets()));
        setBackgroundResource(R.drawable.album_btn_media_pause);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(2.0f, 2.0f, (float) (getWidth() - 2), (float) (getWidth() - 2));
        if (this.mState == com.fimi.album.widget.DownloadStateView.State.PAUSE) {
            setBackgroundResource(R.drawable.album_btn_media_pause);
            canvas.drawArc(rectF, -90.0f, (float) this.sweepAngle, false, this.mPaint);
        } else if (this.mState == com.fimi.album.widget.DownloadStateView.State.DOWNLOADING) {
            String text = Math.round(((float) this.sweepAngle) / 3.6f) + "%";
            FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
            canvas.drawText(text, (float) (getWidth() / 2), (((float) getHeight()) - ((((float) getHeight()) - (fontMetrics.bottom - fontMetrics.top)) / 2.0f)) - fontMetrics.bottom, this.mTextPaint);
            setBackgroundResource(R.drawable.album_btn_media_detail_download);
            this.mPaint.setColor(DOWNLOADING_COLOR);
            canvas.drawArc(rectF, -90.0f, (float) this.sweepAngle, false, this.mPaint);
        } else if (this.mState == com.fimi.album.widget.DownloadStateView.State.DOWNLOAD_FAIL) {
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

    public void setState(com.fimi.album.widget.DownloadStateView.State state) {
        if (this.mState != state) {
            this.mState = state;
            invalidate();
        }
    }

    public com.fimi.album.widget.DownloadStateView.State getState() {
        return this.mState;
    }
}
