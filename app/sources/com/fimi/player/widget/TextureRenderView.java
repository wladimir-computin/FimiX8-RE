package com.fimi.player.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.fimi.player.IMediaPlayer;
import com.fimi.player.ISurfaceTextureHolder;
import com.fimi.player.widget.IRenderView.IRenderCallback;
import com.fimi.player.widget.IRenderView.ISurfaceHolder;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TargetApi(14)
public class TextureRenderView extends TextureView implements IRenderView {
    private MeasureHelper mMeasureHelper;
    private SurfaceCallback mSurfaceCallback;

    private static final class InternalSurfaceHolder implements ISurfaceHolder {
        private SurfaceTexture mSurfaceTexture;
        private TextureRenderView mTextureView;

        public InternalSurfaceHolder(@NonNull TextureRenderView textureView, @Nullable SurfaceTexture surfaceTexture) {
            this.mTextureView = textureView;
            this.mSurfaceTexture = surfaceTexture;
        }

        @TargetApi(16)
        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                if (VERSION.SDK_INT < 16 || !(mp instanceof ISurfaceTextureHolder)) {
                    mp.setSurface(openSurface());
                    return;
                }
                ISurfaceTextureHolder textureHolder = (ISurfaceTextureHolder) mp;
                this.mTextureView.mSurfaceCallback.setOwnSurfaceTecture(false);
                SurfaceTexture surfaceTexture = textureHolder.getSurfaceTexture();
                if (surfaceTexture != null) {
                    this.mTextureView.setSurfaceTexture(surfaceTexture);
                } else {
                    textureHolder.setSurfaceTexture(this.mSurfaceTexture);
                }
            }
        }

        @NonNull
        public IRenderView getRenderView() {
            return this.mTextureView;
        }

        @Nullable
        public SurfaceHolder getSurfaceHolder() {
            return null;
        }

        @Nullable
        public SurfaceTexture getSurfaceTexture() {
            return this.mSurfaceTexture;
        }

        @Nullable
        public Surface openSurface() {
            if (this.mSurfaceTexture == null) {
                return null;
            }
            return new Surface(this.mSurfaceTexture);
        }
    }

    private static final class SurfaceCallback implements SurfaceTextureListener {
        private int mHeight;
        private boolean mIsFormatChanged;
        private boolean mOwnSurfaceTecture = true;
        private Map<IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap();
        private SurfaceTexture mSurfaceTexture;
        private WeakReference<TextureRenderView> mWeakRenderView;
        private int mWidth;

        public SurfaceCallback(@NonNull TextureRenderView renderView) {
            this.mWeakRenderView = new WeakReference(renderView);
        }

        public void setOwnSurfaceTecture(boolean ownSurfaceTecture) {
            this.mOwnSurfaceTecture = ownSurfaceTecture;
        }

        public void addRenderCallback(@NonNull IRenderCallback callback) {
            this.mRenderCallbackMap.put(callback, callback);
            ISurfaceHolder surfaceHolder = null;
            if (this.mSurfaceTexture != null) {
                if (null == null) {
                    surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), this.mSurfaceTexture);
                }
                callback.onSurfaceCreated(surfaceHolder, this.mWidth, this.mHeight);
            }
            if (this.mIsFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), this.mSurfaceTexture);
                }
                callback.onSurfaceChanged(surfaceHolder, 0, this.mWidth, this.mHeight);
            }
        }

        public void removeRenderCallback(@NonNull IRenderCallback callback) {
            this.mRenderCallbackMap.remove(callback);
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            this.mSurfaceTexture = surface;
            this.mIsFormatChanged = false;
            this.mWidth = 0;
            this.mHeight = 0;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), surface);
            for (IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0);
            }
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            this.mSurfaceTexture = surface;
            this.mIsFormatChanged = true;
            this.mWidth = width;
            this.mHeight = height;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), surface);
            for (IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceChanged(surfaceHolder, 0, width, height);
            }
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            this.mSurfaceTexture = surface;
            this.mIsFormatChanged = false;
            this.mWidth = 0;
            this.mHeight = 0;
            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), surface);
            for (IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceDestroyed(surfaceHolder);
            }
            return this.mOwnSurfaceTecture;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    }

    public TextureRenderView(Context context) {
        super(context);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper(this);
        this.mSurfaceCallback = new SurfaceCallback(this);
        setSurfaceTextureListener(this.mSurfaceCallback);
    }

    public View getView() {
        return this;
    }

    public boolean shouldWaitForResize() {
        return false;
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
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
        this.mMeasureHelper.setVideoRotation(degree);
        setRotation((float) degree);
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

    public ISurfaceHolder getSurfaceHolder() {
        return new InternalSurfaceHolder(this, this.mSurfaceCallback.mSurfaceTexture);
    }

    public void addRenderCallback(IRenderCallback callback) {
        this.mSurfaceCallback.addRenderCallback(callback);
    }

    public void removeRenderCallback(IRenderCallback callback) {
        this.mSurfaceCallback.removeRenderCallback(callback);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TextureRenderView.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TextureRenderView.class.getName());
    }
}
