package com.fimi.kernel.animutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.widget.ImageView;
import com.fimi.kernel.base.BaseApplication;
import java.lang.ref.SoftReference;

public class AnimationsContainer {
    private static AnimationsContainer mInstance;
    public int FPS = 58;
    private Context mContext = BaseApplication.getContext();
    private int resId;

    public class FramesSequenceAnimation {
        private Bitmap mBitmap = null;
        private Options mBitmapOptions;
        private int mDelayMillis;
        private int[] mFrames;
        private Handler mHandler = new Handler();
        private int mIndex;
        private boolean mIsRunning;
        private OnAnimationStoppedListener mOnAnimationStoppedListener;
        private boolean mShouldRun;
        private SoftReference<ImageView> mSoftReferenceImageView;

        public boolean isShouldRun() {
            return this.mShouldRun;
        }

        public FramesSequenceAnimation(ImageView imageView, int[] frames, int fps) {
            this.mFrames = frames;
            this.mIndex = -1;
            this.mSoftReferenceImageView = new SoftReference(imageView);
            this.mShouldRun = false;
            this.mIsRunning = false;
            this.mDelayMillis = 100;
            imageView.setImageResource(this.mFrames[0]);
            if (VERSION.SDK_INT >= 11) {
                Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                this.mBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
                this.mBitmapOptions = new Options();
                this.mBitmapOptions.inBitmap = this.mBitmap;
                this.mBitmapOptions.inMutable = true;
                this.mBitmapOptions.inSampleSize = 1;
                this.mBitmapOptions.inPreferredConfig = Config.RGB_565;
            }
        }

        private int getNext() {
            this.mIndex++;
            if (this.mIndex >= this.mFrames.length) {
                this.mIndex = 0;
            }
            return this.mFrames[this.mIndex];
        }

        public synchronized void start() {
            this.mShouldRun = true;
            if (!this.mIsRunning) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        ImageView imageView = (ImageView) FramesSequenceAnimation.this.mSoftReferenceImageView.get();
                        if (!FramesSequenceAnimation.this.mShouldRun || imageView == null) {
                            FramesSequenceAnimation.this.mIsRunning = false;
                            if (FramesSequenceAnimation.this.mOnAnimationStoppedListener != null) {
                                FramesSequenceAnimation.this.mOnAnimationStoppedListener.AnimationStopped();
                                return;
                            }
                            return;
                        }
                        FramesSequenceAnimation.this.mIsRunning = true;
                        FramesSequenceAnimation.this.mHandler.postDelayed(this, (long) FramesSequenceAnimation.this.mDelayMillis);
                        if (imageView.isShown()) {
                            int imageRes = FramesSequenceAnimation.this.getNext();
                            if (FramesSequenceAnimation.this.mBitmap != null) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = BitmapFactory.decodeResource(imageView.getResources(), imageRes, FramesSequenceAnimation.this.mBitmapOptions);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                    return;
                                }
                                imageView.setImageResource(imageRes);
                                FramesSequenceAnimation.this.mBitmap.recycle();
                                FramesSequenceAnimation.this.mBitmap = null;
                                return;
                            }
                            imageView.setImageResource(imageRes);
                        }
                    }
                });
            }
        }

        public synchronized void stop() {
            this.mShouldRun = false;
        }

        public void setOnAnimStopListener(OnAnimationStoppedListener listener) {
            this.mOnAnimationStoppedListener = listener;
        }
    }

    public interface OnAnimationStoppedListener {
        void AnimationStopped();
    }

    public static AnimationsContainer getInstance(int resId, int fps) {
        if (mInstance == null) {
            mInstance = new AnimationsContainer();
        }
        mInstance.setResId(resId, fps);
        return mInstance;
    }

    public void setResId(int resId, int fps) {
        this.resId = resId;
        this.FPS = fps;
    }

    public FramesSequenceAnimation createProgressDialogAnim(ImageView imageView) {
        return new FramesSequenceAnimation(imageView, getData(this.resId), this.FPS);
    }

    private int[] getData(int resId) {
        TypedArray array = this.mContext.getResources().obtainTypedArray(resId);
        int len = array.length();
        int[] intArray = new int[array.length()];
        for (int i = 0; i < len; i++) {
            intArray[i] = array.getResourceId(i, 0);
        }
        array.recycle();
        return intArray;
    }
}
