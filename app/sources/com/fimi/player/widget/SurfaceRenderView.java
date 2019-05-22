package com.fimi.player.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.fimi.player.IMediaPlayer;
import com.fimi.player.widget.IRenderView.IRenderCallback;
import com.fimi.player.widget.IRenderView.ISurfaceHolder;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SurfaceRenderView extends SurfaceView implements IRenderView {
    private MeasureHelper mMeasureHelper;
    private SurfaceCallback mSurfaceCallback;

    private static final class InternalSurfaceHolder implements ISurfaceHolder {
        private SurfaceHolder mSurfaceHolder;
        private SurfaceRenderView mSurfaceView;

        public InternalSurfaceHolder(@NonNull SurfaceRenderView surfaceView, @Nullable SurfaceHolder surfaceHolder) {
            this.mSurfaceView = surfaceView;
            this.mSurfaceHolder = surfaceHolder;
        }

        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                mp.setDisplay(this.mSurfaceHolder);
            }
        }

        @NonNull
        public IRenderView getRenderView() {
            return this.mSurfaceView;
        }

        @Nullable
        public SurfaceHolder getSurfaceHolder() {
            return this.mSurfaceHolder;
        }

        @Nullable
        public SurfaceTexture getSurfaceTexture() {
            return null;
        }

        @Nullable
        public Surface openSurface() {
            if (this.mSurfaceHolder == null) {
                return null;
            }
            return this.mSurfaceHolder.getSurface();
        }
    }

    private static final class SurfaceCallback implements Callback {
        private int mFormat;
        private int mHeight;
        private boolean mIsFormatChanged;
        private Map<IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap();
        private SurfaceHolder mSurfaceHolder;
        private WeakReference<SurfaceRenderView> mWeakSurfaceView;
        private int mWidth;

        public SurfaceCallback(@NonNull SurfaceRenderView surfaceView) {
            this.mWeakSurfaceView = new WeakReference(surfaceView);
        }

        public void addRenderCallback(@NonNull IRenderCallback callback) {
            this.mRenderCallbackMap.put(callback, callback);
            ISurfaceHolder surfaceHolder = null;
            if (this.mSurfaceHolder != null) {
                if (null == null) {
                    surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
                }
                callback.onSurfaceCreated(surfaceHolder, this.mWidth, this.mHeight);
            }
            if (this.mIsFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
                }
                callback.onSurfaceChanged(surfaceHolder, this.mFormat, this.mWidth, this.mHeight);
            }
        }

        public void removeRenderCallback(@NonNull IRenderCallback callback) {
            this.mRenderCallbackMap.remove(callback);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            this.mSurfaceHolder = holder;
            this.mIsFormatChanged = false;
            this.mFormat = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            for (IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0);
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            this.mSurfaceHolder = null;
            this.mIsFormatChanged = false;
            this.mFormat = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            for (IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceDestroyed(surfaceHolder);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.mSurfaceHolder = holder;
            this.mIsFormatChanged = true;
            this.mFormat = format;
            this.mWidth = width;
            this.mHeight = height;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            for (IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceChanged(surfaceHolder, format, width, height);
            }
        }
    }

    public SurfaceRenderView(Context context) {
        super(context);
        initView(context);
    }

    public SurfaceRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper(this);
        this.mSurfaceCallback = new SurfaceCallback(this);
        getHolder().addCallback(this.mSurfaceCallback);
        getHolder().setType(0);
    }

    public View getView() {
        return this;
    }

    public boolean shouldWaitForResize() {
        return true;
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            getHolder().setFixedSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            this.mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    public void setVideoRotation(int degree) {
        Log.e("", "SurfaceView doesn't support rotation (" + degree + ")!\n");
    }

    public void setAspectRatio(int aspectRatio) {
        this.mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
    }

    public void addRenderCallback(IRenderCallback callback) {
        this.mSurfaceCallback.addRenderCallback(callback);
    }

    public void removeRenderCallback(IRenderCallback callback) {
        this.mSurfaceCallback.removeRenderCallback(callback);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(SurfaceRenderView.class.getName());
    }

    @TargetApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (VERSION.SDK_INT >= 14) {
            info.setClassName(SurfaceRenderView.class.getName());
        }
    }
}
