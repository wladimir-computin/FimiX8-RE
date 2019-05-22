package com.fimi.soul.media.player.widget;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;
import com.fimi.soul.media.player.FimiMediaPlayer;
import com.fimi.soul.media.player.IMediaPlayer;
import com.fimi.soul.media.player.IMediaPlayer.OnBufferingUpdateListener;
import com.fimi.soul.media.player.IMediaPlayer.OnCompletionListener;
import com.fimi.soul.media.player.IMediaPlayer.OnErrorListener;
import com.fimi.soul.media.player.IMediaPlayer.OnInfoListener;
import com.fimi.soul.media.player.IMediaPlayer.OnPreparedListener;
import com.fimi.soul.media.player.IMediaPlayer.OnVideoSizeChangedListener;
import com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl;
import com.fimi.soul.media.player.widget.IRenderView.IRenderCallback;
import com.fimi.soul.media.player.widget.IRenderView.ISurfaceHolder;
import com.twitter.sdk.android.core.internal.VineCardUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FimiVideoView extends FrameLayout implements MediaPlayerControl {
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int[] s_allAspectRatio = new int[]{0, 1, 2, 3, 4, 5};
    private String TAG = "IjkVideoView";
    private int decodeType = 0;
    private boolean isLog = false;
    private List<Integer> mAllRenders = new ArrayList();
    private Context mAppContext;
    private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            FimiVideoView.this.mCurrentBufferPercentage = percent;
        }
    };
    private boolean mCanPause = true;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private OnCompletionListener mCompletionListener = new OnCompletionListener() {
        public void onCompletion(IMediaPlayer mp) {
            FimiVideoView.this.mCurrentState = 5;
            FimiVideoView.this.mTargetState = 5;
            if (FimiVideoView.this.mMediaController != null) {
                FimiVideoView.this.mMediaController.hide();
            }
            if (FimiVideoView.this.mOnCompletionListener != null) {
                FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer);
            }
        }
    };
    private int mCurrentAspectRatio = s_allAspectRatio[0];
    private int mCurrentAspectRatioIndex = 0;
    private int mCurrentBufferPercentage;
    private int mCurrentRender = 0;
    private int mCurrentRenderIndex = 0;
    private int mCurrentState = 0;
    private OnErrorListener mErrorListener = new OnErrorListener() {
        public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
            Log.d(FimiVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
            FimiVideoView.this.mCurrentState = -1;
            FimiVideoView.this.mTargetState = -1;
            if (FimiVideoView.this.mMediaController != null) {
                FimiVideoView.this.mMediaController.hide();
            }
            if (mp.getDataSource().contains("rtsp:")) {
                Intent resetRstp = new Intent("resetRTSP");
                resetRstp.putExtra("rtsp", 1);
                FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
            }
            if (FimiVideoView.this.mOnErrorListener == null || !FimiVideoView.this.mOnErrorListener.onError(FimiVideoView.this.mMediaPlayer, framework_err, impl_err)) {
                return false;
            }
            return true;
        }
    };
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private Map<String, String> mHeaders;
    private OnInfoListener mInfoListener = new OnInfoListener() {
        public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
            if (FimiVideoView.this.mOnInfoListener != null) {
                FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
            }
            switch (arg1) {
                case 10001:
                    FimiVideoView.this.mVideoRotationDegree = arg2;
                    Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                        break;
                    }
                    break;
            }
            return true;
        }
    };
    private IMediaController mMediaController;
    private IMediaPlayer mMediaPlayer = null;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    OnPreparedListener mPreparedListener = new OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            FimiVideoView.this.mCurrentState = 2;
            if (FimiVideoView.this.mOnPreparedListener != null) {
                FimiVideoView.this.mOnPreparedListener.onPrepared(FimiVideoView.this.mMediaPlayer);
            }
            if (FimiVideoView.this.mMediaController != null) {
                FimiVideoView.this.mMediaController.setEnabled(true);
            }
            FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
            FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
            int seekToPosition = FimiVideoView.this.mSeekWhenPrepared;
            if (seekToPosition != 0) {
                FimiVideoView.this.seekTo(seekToPosition);
            }
            if (FimiVideoView.this.mVideoWidth == 0 || FimiVideoView.this.mVideoHeight == 0) {
                if (FimiVideoView.this.mTargetState == 3) {
                    FimiVideoView.this.start();
                }
            } else if (FimiVideoView.this.mRenderView != null) {
                FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                if (!FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mSurfaceWidth == FimiVideoView.this.mVideoWidth && FimiVideoView.this.mSurfaceHeight == FimiVideoView.this.mVideoHeight)) {
                    if (FimiVideoView.this.mTargetState == 3) {
                        FimiVideoView.this.start();
                        if (FimiVideoView.this.mMediaController != null) {
                            FimiVideoView.this.mMediaController.show();
                        }
                    } else if (!FimiVideoView.this.isPlaying() && ((seekToPosition != 0 || FimiVideoView.this.getCurrentPosition() > 0) && FimiVideoView.this.mMediaController != null)) {
                        FimiVideoView.this.mMediaController.show(0);
                    }
                }
            }
            if (mp.getDataSource().contains("rtsp:")) {
                FimiVideoView.this.getHandler().sendEmptyMessage(0);
                Intent resetRstp = new Intent("resetRTSP");
                resetRstp.putExtra("rtsp", 0);
                FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
            }
        }
    };
    private IRenderView mRenderView;
    IRenderCallback mSHCallback = new IRenderCallback() {
        public void onSurfaceChanged(@NonNull ISurfaceHolder holder, int format, int w, int h) {
            if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                Log.e(FimiVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                return;
            }
            FimiVideoView.this.mSurfaceWidth = w;
            FimiVideoView.this.mSurfaceHeight = h;
            boolean isValidState;
            if (FimiVideoView.this.mTargetState == 3) {
                isValidState = true;
            } else {
                isValidState = false;
            }
            boolean hasValidSize;
            if (!FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mVideoWidth == w && FimiVideoView.this.mVideoHeight == h)) {
                hasValidSize = true;
            } else {
                hasValidSize = false;
            }
            if (FimiVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                if (FimiVideoView.this.mSeekWhenPrepared != 0) {
                    FimiVideoView.this.seekTo(FimiVideoView.this.mSeekWhenPrepared);
                }
                FimiVideoView.this.start();
            }
        }

        public void onSurfaceCreated(@NonNull ISurfaceHolder holder, int width, int height) {
            if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                return;
            }
            FimiVideoView.this.mSurfaceHolder = holder;
            if (FimiVideoView.this.mMediaPlayer != null) {
                FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
            } else {
                FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
            }
        }

        public void onSurfaceDestroyed(@NonNull ISurfaceHolder holder) {
            if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                return;
            }
            FimiVideoView.this.mSurfaceHolder = null;
            FimiVideoView.this.releaseWithoutStop();
        }
    };
    private int mSeekWhenPrepared;
    OnVideoSizeChangedListener mSizeChangedListener = new OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
            FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
            FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
            FimiVideoView.this.mVideoSarNum = mp.getVideoSarNum();
            FimiVideoView.this.mVideoSarDen = mp.getVideoSarDen();
            if (FimiVideoView.this.mVideoWidth != 0 && FimiVideoView.this.mVideoHeight != 0) {
                if (FimiVideoView.this.mRenderView != null) {
                    FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                    FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                }
                FimiVideoView.this.requestLayout();
            }
        }
    };
    private int mSurfaceHeight;
    private ISurfaceHolder mSurfaceHolder = null;
    private int mSurfaceWidth;
    private int mTargetState = 0;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    OutputStream outputStream;

    public FimiVideoView(Context context) {
        super(context);
        initVideoView(context);
    }

    public FimiVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public FimiVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    public FimiVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    public int getmCurrentState() {
        return this.mCurrentState;
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    public void setRenderView(IRenderView renderView) {
        View renderUIView;
        if (this.mRenderView != null) {
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.setDisplay(null);
            }
            renderUIView = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(renderUIView);
        }
        if (renderView != null) {
            this.mRenderView = renderView;
            renderView.setAspectRatio(this.mCurrentAspectRatio);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                renderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }
            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                renderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }
            renderUIView = this.mRenderView.getView();
            renderUIView.setLayoutParams(new LayoutParams(-2, -2, 17));
            addView(renderUIView);
            this.mRenderView.addRenderCallback(this.mSHCallback);
            this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    public void setZOrderMediaOverlay(boolean isM) {
        if (this.mRenderView instanceof SurfaceRenderView) {
            ((SurfaceRenderView) this.mRenderView).setZOrderMediaOverlay(isM);
        }
    }

    public void setHideSurfaceView(boolean isVisiable) {
        if (this.mRenderView instanceof SurfaceRenderView) {
            ((SurfaceRenderView) this.mRenderView).getHolder().setFormat(isVisiable ? -2 : 4);
        }
    }

    public void setRender(int render) {
        switch (render) {
            case 0:
                setRenderView(null);
                return;
            case 1:
                setRenderView(new SurfaceRenderView(getContext()));
                return;
            case 2:
                TextureRenderView renderView = new TextureRenderView(getContext());
                if (this.mMediaPlayer != null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                    renderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
                    renderView.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
                    renderView.setAspectRatio(this.mCurrentAspectRatio);
                }
                setRenderView(renderView);
                return;
            default:
                Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", new Object[]{Integer.valueOf(render)}));
                return;
        }
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo(this.decodeType);
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    public void setDecodeType(int decodeType) {
        this.decodeType = decodeType;
    }

    private void openVideo(int decodeType) {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            release(false);
            ((AudioManager) this.mAppContext.getSystemService("audio")).requestAudioFocus(null, 3, 1);
            FimiMediaPlayer ijkMediaPlayer = null;
            try {
                if (this.mUri != null) {
                    ijkMediaPlayer = FimiMediaPlayer.getIntance();
                    FimiMediaPlayer.native_setLogLevel(3);
                    ijkMediaPlayer.setOption(4, "mediacodec", (long) decodeType);
                    ijkMediaPlayer.setOption(4, "framedrop", 20);
                    ijkMediaPlayer.setOption(4, "start-on-prepared", 0);
                    ijkMediaPlayer.setOption(1, "http-detect-range-support", 0);
                    ijkMediaPlayer.setOption(2, "skip_loop_filter", 48);
                    ijkMediaPlayer.setOption(1, "analyzeduration", "20000");
                    ijkMediaPlayer.setOption(1, "probsize", "4096");
                }
                this.mMediaPlayer = ijkMediaPlayer;
                Context context = getContext();
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mCurrentBufferPercentage = 0;
                if (VERSION.SDK_INT > 14) {
                    this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
                } else {
                    this.mMediaPlayer.setDataSource(this.mUri.toString());
                }
                bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync(2, 2);
                this.mCurrentState = 1;
                attachMediaController();
            } catch (IOException ex) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException ex2) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex2);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setMediaController(IMediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            View anchorView;
            this.mMediaController.setMediaPlayer(this);
            if (getParent() instanceof View) {
                anchorView = (View) getParent();
            } else {
                anchorView = this;
            }
            this.mMediaController.setAnchorView(anchorView);
            this.mMediaController.setEnabled(isInPlaybackState());
        }
    }

    public void setOnPreparedListener(OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    private void bindSurfaceHolder(IMediaPlayer mp, ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setDisplay(null);
            } else {
                holder.bindToMediaPlayer(mp);
            }
        }
    }

    public void releaseWithoutStop() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setDisplay(null);
        }
    }

    public void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 164 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == 126) {
                if (this.mMediaPlayer.isPlaying()) {
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode != 86 && keyCode != 127) {
                toggleMediaControlsVisiblity();
            } else if (!this.mMediaPlayer.isPlaying()) {
                return true;
            } else {
                pause();
                this.mMediaController.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    public void start() {
        if (this.mCurrentState == 5) {
            setVideoURI(this.mUri);
        }
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void pause() {
        Log.d(VineCardUtils.PLAYER_CARD, "pause");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.pause();
            if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.pause();
                this.mCurrentState = 4;
            }
            this.mTargetState = 4;
        }
    }

    public void suspend() {
        Log.d("Good", "suspend");
        release(false);
    }

    public void resume() {
        Log.d("Good", "resume");
        openVideo(this.decodeType);
    }

    public int getDuration() {
        Log.d(VineCardUtils.PLAYER_CARD, "getDuration");
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        Log.d(VineCardUtils.PLAYER_CARD, "seekto");
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo((long) msec);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public void onFinishInflate() {
        Log.d(VineCardUtils.PLAYER_CARD, "onFinishInflate");
        super.onFinishInflate();
        if (isPlaying()) {
            pause();
        }
    }

    private boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public int toggleAspectRatio() {
        this.mCurrentAspectRatioIndex++;
        this.mCurrentAspectRatioIndex %= s_allAspectRatio.length;
        this.mCurrentAspectRatio = 1;
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
        }
        return this.mCurrentAspectRatio;
    }

    public int toggleAspectRatioPOi() {
        this.mCurrentAspectRatioIndex++;
        this.mCurrentAspectRatioIndex %= s_allAspectRatio.length;
        this.mCurrentAspectRatio = 3;
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
        }
        return this.mCurrentAspectRatio;
    }

    private void initRenders() {
        this.mAllRenders.clear();
        this.mAllRenders.add(Integer.valueOf(1));
        if (this.mAllRenders.isEmpty()) {
            this.mAllRenders.add(Integer.valueOf(1));
        }
        this.mCurrentRender = ((Integer) this.mAllRenders.get(this.mCurrentRenderIndex)).intValue();
        setRender(this.mCurrentRender);
    }

    public int toggleRender() {
        this.mCurrentRenderIndex++;
        this.mCurrentRenderIndex %= this.mAllRenders.size();
        this.mCurrentRender = ((Integer) this.mAllRenders.get(this.mCurrentRenderIndex)).intValue();
        setRender(this.mCurrentRender);
        return this.mCurrentRender;
    }

    public void onPreviewInputNetData(byte[] data, int nPos, int len) {
        if (this.mMediaPlayer == null) {
            return;
        }
        if (data != null) {
            this.mMediaPlayer.PlayerPreviewInputNetData(data, nPos, len);
        } else {
            Toast.makeText(getContext(), "FimiView onPreviewInputNetData data = null", 1).show();
        }
    }

    public String getLogString() {
        String log = "";
        if (this.mMediaPlayer == null) {
            return log;
        }
        int[] network_info_int = new int[64];
        this.mMediaPlayer.PlayerGetNetworkInfo(network_info_int);
        return network_info_int[0] + " (kbps)\n" + network_info_int[1] + " (receive)\n" + network_info_int[2] + " (lose) \n" + " " + network_info_int[3] + " (35ms)\n" + network_info_int[4] + " (40ms)\n";
    }
}
