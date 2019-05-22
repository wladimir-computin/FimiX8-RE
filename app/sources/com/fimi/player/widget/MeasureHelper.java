package com.fimi.player.widget;

import android.view.View;
import android.view.View.MeasureSpec;
import java.lang.ref.WeakReference;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public final class MeasureHelper {
    private int mCurrentAspectRatio = 0;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private WeakReference<View> mWeakView;

    public MeasureHelper(View view) {
        this.mWeakView = new WeakReference(view);
    }

    public View getView() {
        if (this.mWeakView == null) {
            return null;
        }
        return (View) this.mWeakView.get();
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        this.mVideoSarNum = videoSarNum;
        this.mVideoSarDen = videoSarDen;
    }

    public void setVideoRotation(int videoRotationDegree) {
        this.mVideoRotationDegree = videoRotationDegree;
    }

    public void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
            int tempSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempSpec;
        }
        int width = View.getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = View.getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mCurrentAspectRatio != 3) {
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
                int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
                int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
                if (widthSpecMode == Integer.MIN_VALUE && heightSpecMode == Integer.MIN_VALUE) {
                    float displayAspectRatio;
                    float specAspectRatio = ((float) widthSpecSize) / ((float) heightSpecSize);
                    switch (this.mCurrentAspectRatio) {
                        case 4:
                            displayAspectRatio = 1.7777778f;
                            if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                                displayAspectRatio = 1.0f / 1.7777778f;
                                break;
                            }
                        case 5:
                            displayAspectRatio = 1.3333334f;
                            if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                                displayAspectRatio = 1.0f / 1.3333334f;
                                break;
                            }
                        default:
                            displayAspectRatio = ((float) this.mVideoWidth) / ((float) this.mVideoHeight);
                            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                                displayAspectRatio = (((float) this.mVideoSarNum) * displayAspectRatio) / ((float) this.mVideoSarDen);
                                break;
                            }
                    }
                    boolean shouldBeWider = displayAspectRatio > specAspectRatio;
                    switch (this.mCurrentAspectRatio) {
                        case 0:
                        case 4:
                        case 5:
                            if (!shouldBeWider) {
                                height = heightSpecSize;
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            }
                            width = widthSpecSize;
                            height = (int) (((float) width) / displayAspectRatio);
                            break;
                        case 1:
                            if (!shouldBeWider) {
                                width = widthSpecSize;
                                height = (int) (((float) width) / displayAspectRatio);
                                break;
                            }
                            height = heightSpecSize;
                            width = (int) (((float) height) * displayAspectRatio);
                            break;
                        default:
                            if (!shouldBeWider) {
                                height = Math.min(this.mVideoHeight, heightSpecSize);
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            }
                            width = Math.min(this.mVideoWidth, widthSpecSize);
                            height = (int) (((float) width) / displayAspectRatio);
                            break;
                    }
                } else if (widthSpecMode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE && heightSpecMode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
                    width = widthSpecSize;
                    height = heightSpecSize;
                    if (this.mVideoWidth * height < this.mVideoHeight * width) {
                        width = (this.mVideoWidth * height) / this.mVideoHeight;
                    } else if (this.mVideoWidth * height > this.mVideoHeight * width) {
                        height = (this.mVideoHeight * width) / this.mVideoWidth;
                    }
                } else if (widthSpecMode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
                    width = widthSpecSize;
                    height = (this.mVideoHeight * width) / this.mVideoWidth;
                    if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                        height = heightSpecSize;
                    }
                } else if (heightSpecMode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
                    height = heightSpecSize;
                    width = (this.mVideoWidth * height) / this.mVideoHeight;
                    if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                        width = widthSpecSize;
                    }
                } else {
                    width = this.mVideoWidth;
                    height = this.mVideoHeight;
                    if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                        height = heightSpecSize;
                        width = (this.mVideoWidth * height) / this.mVideoHeight;
                    }
                    if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                        width = widthSpecSize;
                        height = (this.mVideoHeight * width) / this.mVideoWidth;
                    }
                }
            }
        } else {
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        }
        this.mMeasuredWidth = width;
        this.mMeasuredHeight = height;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public void setAspectRatio(int aspectRatio) {
        this.mCurrentAspectRatio = aspectRatio;
    }
}
