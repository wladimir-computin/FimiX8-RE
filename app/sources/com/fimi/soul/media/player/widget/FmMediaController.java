package com.fimi.soul.media.player.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import ch.qos.logback.core.CoreConstants;
import com.baidu.tts.client.SpeechSynthesizer;
import com.fimi.x8sdk.R;
import com.twitter.sdk.android.core.internal.VineCardUtils;
import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.Locale;

public class FmMediaController extends FrameLayout {
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int sDefaultTimeout = 3000;
    private static final int updatePause = 0;
    private View mAnchor;
    private Context mContext;
    private TextView mCurrentTime;
    private View mDecor;
    private LayoutParams mDecorLayoutParams;
    private boolean mDragging;
    private TextView mEndTime;
    private ImageButton mFfwdButton;
    private OnClickListener mFfwdListener;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private boolean mFromXml;
    private Handler mHandler;
    private OnLayoutChangeListener mLayoutChangeListener;
    private boolean mListenersSet;
    private ImageButton mNextButton;
    private OnClickListener mNextListener;
    private ImageButton mPauseButton;
    private OnClickListener mPauseListener;
    private MediaPlayerControl mPlayer;
    private ImageButton mPrevButton;
    private OnClickListener mPrevListener;
    private SeekBar mProgress;
    private ImageButton mRewButton;
    private OnClickListener mRewListener;
    private View mRoot;
    private OnSeekBarChangeListener mSeekListener;
    private boolean mShowing;
    private OnTouchListener mTouchListener;
    private boolean mUseFastForward;
    private Window mWindow;
    private WindowManager mWindowManager;

    public interface MediaPlayerControl {
        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        int getAudioSessionId();

        int getBufferPercentage();

        int getCurrentPosition();

        int getDuration();

        boolean isPlaying();

        void pause();

        void seekTo(int i);

        void start();
    }

    public FmMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLayoutChangeListener = new OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                FmMediaController.this.updateFloatingWindowLayout();
                if (FmMediaController.this.mShowing) {
                    FmMediaController.this.mWindowManager.updateViewLayout(FmMediaController.this.mDecor, FmMediaController.this.mDecorLayoutParams);
                }
            }
        };
        this.mTouchListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0 && FmMediaController.this.mShowing) {
                    FmMediaController.this.hide();
                }
                return false;
            }
        };
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (FmMediaController.this.mPlayer.isPlaying()) {
                            FmMediaController.this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
                            return;
                        } else {
                            FmMediaController.this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
                            return;
                        }
                    case 1:
                        FmMediaController.this.hide();
                        return;
                    case 2:
                        Log.d(VineCardUtils.PLAYER_CARD, "handle SHOW_PROGRESS");
                        int pos = FmMediaController.this.setProgress();
                        if (!FmMediaController.this.mDragging && FmMediaController.this.mShowing && FmMediaController.this.mPlayer.isPlaying()) {
                            sendMessageDelayed(obtainMessage(2), (long) (1000 - (pos % 1000)));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mPauseListener = new OnClickListener() {
            public void onClick(View v) {
                FmMediaController.this.doPauseResume();
                FmMediaController.this.show(3000);
            }
        };
        this.mSeekListener = new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar bar) {
                Log.d(VineCardUtils.PLAYER_CARD, "onStartTrackingTouch");
                FmMediaController.this.show(CoreConstants.MILLIS_IN_ONE_HOUR);
                FmMediaController.this.mDragging = true;
                FmMediaController.this.mHandler.removeMessages(2);
            }

            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                Log.d(VineCardUtils.PLAYER_CARD, "onProgressChanged");
                if (fromuser) {
                    long newposition = (((long) progress) * ((long) FmMediaController.this.mPlayer.getDuration())) / 1000;
                    FmMediaController.this.mPlayer.seekTo((int) newposition);
                    if (FmMediaController.this.mCurrentTime != null) {
                        FmMediaController.this.mCurrentTime.setText(FmMediaController.this.stringForTime((int) newposition));
                    }
                }
            }

            public void onStopTrackingTouch(SeekBar bar) {
                Log.d(VineCardUtils.PLAYER_CARD, "onStopTrackingTouch");
                FmMediaController.this.mDragging = false;
                FmMediaController.this.setProgress();
                FmMediaController.this.updatePausePlay();
                FmMediaController.this.show(3000);
                FmMediaController.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mRewListener = new OnClickListener() {
            public void onClick(View v) {
                FmMediaController.this.mPlayer.seekTo(FmMediaController.this.mPlayer.getCurrentPosition() - 5000);
                FmMediaController.this.setProgress();
                FmMediaController.this.show(3000);
            }
        };
        this.mFfwdListener = new OnClickListener() {
            public void onClick(View v) {
                FmMediaController.this.mPlayer.seekTo(FmMediaController.this.mPlayer.getCurrentPosition() + SpeechSynthesizer.MAX_QUEUE_SIZE);
                FmMediaController.this.setProgress();
                FmMediaController.this.show(3000);
            }
        };
        this.mRoot = this;
        this.mContext = context;
        this.mUseFastForward = true;
        this.mFromXml = true;
    }

    @SuppressLint({"MissingSuperCall"})
    public void onFinishInflate() {
        if (this.mRoot != null) {
            initControllerView(this.mRoot);
        }
    }

    public FmMediaController(Context context, boolean useFastForward) {
        super(context);
        this.mLayoutChangeListener = /* anonymous class already generated */;
        this.mTouchListener = /* anonymous class already generated */;
        this.mHandler = /* anonymous class already generated */;
        this.mPauseListener = /* anonymous class already generated */;
        this.mSeekListener = /* anonymous class already generated */;
        this.mRewListener = /* anonymous class already generated */;
        this.mFfwdListener = /* anonymous class already generated */;
        this.mContext = context;
        this.mUseFastForward = useFastForward;
        initFloatingWindowLayout();
        initFloatingWindow();
    }

    public FmMediaController(Context context) {
        this(context, true);
    }

    private void initWindow() {
        try {
            Method[] meths = Class.forName("com.android.internal.policy.PolicyManager").getMethods();
            Method makenewwindow = null;
            for (int i = 0; i < meths.length; i++) {
                if (meths[i].getName().endsWith("makeNewWindow")) {
                    makenewwindow = meths[i];
                }
            }
            this.mWindow = (Window) makenewwindow.invoke(null, new Object[]{this.mContext});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFloatingWindow() {
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        initWindow();
        this.mWindow.setWindowManager(this.mWindowManager, null, null);
        this.mWindow.requestFeature(1);
        this.mDecor = this.mWindow.getDecorView();
        this.mDecor.setOnTouchListener(this.mTouchListener);
        this.mWindow.setContentView(this);
        this.mWindow.setBackgroundDrawableResource(17170445);
        this.mWindow.setVolumeControlStream(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(262144);
        requestFocus();
    }

    private void initFloatingWindowLayout() {
        this.mDecorLayoutParams = new LayoutParams();
        LayoutParams p = this.mDecorLayoutParams;
        p.gravity = 51;
        p.height = -2;
        p.x = 0;
        p.format = -3;
        p.type = 1000;
        p.flags |= 8519712;
        p.token = null;
        p.windowAnimations = 0;
    }

    private void updateFloatingWindowLayout() {
        int[] anchorPos = new int[2];
        this.mAnchor.getLocationOnScreen(anchorPos);
        this.mDecor.measure(MeasureSpec.makeMeasureSpec(this.mAnchor.getWidth(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(this.mAnchor.getHeight(), Integer.MIN_VALUE));
        LayoutParams p = this.mDecorLayoutParams;
        p.width = this.mAnchor.getWidth();
        p.x = anchorPos[0] + ((this.mAnchor.getWidth() - p.width) / 2);
        p.y = (anchorPos[1] + this.mAnchor.getHeight()) - this.mDecor.getMeasuredHeight();
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        this.mPlayer = player;
        updatePausePlay();
    }

    public void setAnchorView(View view) {
        if (this.mAnchor != null) {
            this.mAnchor.removeOnLayoutChangeListener(this.mLayoutChangeListener);
        }
        this.mAnchor = view;
        if (this.mAnchor != null) {
            this.mAnchor.addOnLayoutChangeListener(this.mLayoutChangeListener);
        }
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(-1, -1);
        removeAllViews();
        addView(makeControllerView(), frameParams);
    }

    /* Access modifiers changed, original: protected */
    public View makeControllerView() {
        this.mRoot = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.x8_fm_media_controller, null);
        initControllerView(this.mRoot);
        return this.mRoot;
    }

    private void initControllerView(View v) {
        int i = 0;
        this.mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (this.mPauseButton != null) {
            this.mPauseButton.requestFocus();
            this.mPauseButton.setOnClickListener(this.mPauseListener);
        }
        this.mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
        if (this.mFfwdButton != null) {
            this.mFfwdButton.setOnClickListener(this.mFfwdListener);
            if (!this.mFromXml) {
                this.mFfwdButton.setVisibility(this.mUseFastForward ? 0 : 8);
            }
        }
        this.mRewButton = (ImageButton) v.findViewById(R.id.rew);
        if (this.mRewButton != null) {
            this.mRewButton.setOnClickListener(this.mRewListener);
            if (!this.mFromXml) {
                ImageButton imageButton = this.mRewButton;
                if (!this.mUseFastForward) {
                    i = 8;
                }
                imageButton.setVisibility(i);
            }
        }
        this.mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (!(this.mNextButton == null || this.mFromXml || this.mListenersSet)) {
            this.mNextButton.setVisibility(8);
        }
        this.mPrevButton = (ImageButton) v.findViewById(R.id.prev);
        if (!(this.mPrevButton == null || this.mFromXml || this.mListenersSet)) {
            this.mPrevButton.setVisibility(8);
        }
        this.mProgress = (SeekBar) v.findViewById(R.id.fmmediacontroller_progress);
        if (this.mProgress != null) {
            if (this.mProgress instanceof SeekBar) {
                this.mProgress.setOnSeekBarChangeListener(this.mSeekListener);
            }
            this.mProgress.setMax(1000);
        }
        this.mEndTime = (TextView) v.findViewById(R.id.time);
        this.mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        installPrevNextListeners();
    }

    public void show() {
        show(3000);
    }

    private void disableUnsupportedButtons() {
        try {
            if (!(this.mPauseButton == null || this.mPlayer.canPause())) {
                this.mPauseButton.setEnabled(false);
            }
            if (!(this.mRewButton == null || this.mPlayer.canSeekBackward())) {
                this.mRewButton.setEnabled(false);
            }
            if (this.mFfwdButton != null && !this.mPlayer.canSeekForward()) {
                this.mFfwdButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError e) {
        }
    }

    public void show(int timeout) {
        Log.d(VineCardUtils.PLAYER_CARD, "show");
        if (!(this.mShowing || this.mAnchor == null)) {
            setProgress();
            if (this.mPauseButton != null) {
                this.mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();
            updateFloatingWindowLayout();
            try {
                this.mWindowManager.addView(this.mDecor, this.mDecorLayoutParams);
            } catch (Exception e) {
                Log.e(VineCardUtils.PLAYER_CARD, "mWindowManager addview error");
            }
            this.mShowing = true;
        }
        updatePausePlay();
        this.mHandler.sendEmptyMessage(2);
        Message msg = this.mHandler.obtainMessage(1);
        if (timeout != 0) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(msg, (long) timeout);
        }
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void hide() {
        if (this.mAnchor != null && this.mShowing) {
            try {
                this.mHandler.removeMessages(2);
                this.mWindowManager.removeView(this.mDecor);
            } catch (IllegalArgumentException e) {
                Log.w("MediaController", "already removed");
            }
            this.mShowing = false;
        }
    }

    public static void main(String[] argv) {
        System.out.println(9);
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        if (hours > 0) {
            return this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    private int setProgress() {
        Log.d(VineCardUtils.PLAYER_CARD, "setProgress");
        if (this.mPlayer == null || this.mDragging) {
            return 0;
        }
        int position = this.mPlayer.getCurrentPosition();
        int duration = this.mPlayer.getDuration();
        Log.d(VineCardUtils.PLAYER_CARD, "position" + position + " duration" + duration);
        if (this.mProgress != null && duration > 0) {
            if (duration / 1000 > 0) {
                this.mProgress.setProgress((int) ((1000 * ((long) (position / 1000))) / ((long) (duration / 1000))));
            } else {
                this.mProgress.setProgress(0);
            }
        }
        if (this.mEndTime != null) {
            this.mEndTime.setText(stringForTime(duration));
        }
        if (!(this.mCurrentTime == null || this.mCurrentTime.getText().toString().equals(this.mEndTime.getText().toString()))) {
            this.mCurrentTime.setText(stringForTime(position));
        }
        if (position != 0) {
            return position;
        }
        this.mCurrentTime.setText(stringForTime(position));
        return position;
    }

    public boolean onTouchEvent(MotionEvent event) {
        show(3000);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        show(3000);
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        boolean uniqueDown = event.getRepeatCount() == 0 && event.getAction() == 0;
        if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
            if (uniqueDown) {
                doPauseResume();
                show(3000);
                if (this.mPauseButton != null) {
                    this.mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == 126) {
            if (uniqueDown && !this.mPlayer.isPlaying()) {
                this.mPlayer.start();
                updatePausePlay();
                show(3000);
            }
            return true;
        } else if (keyCode == 86 || keyCode == 127) {
            if (uniqueDown && this.mPlayer.isPlaying()) {
                this.mPlayer.pause();
                updatePausePlay();
                show(3000);
            }
            return true;
        } else if (keyCode == 25 || keyCode == 24 || keyCode == 164 || keyCode == 27) {
            return super.dispatchKeyEvent(event);
        } else {
            if (keyCode == 4) {
                if (uniqueDown) {
                    hide();
                    ((Activity) this.mContext).finish();
                }
                return true;
            } else if (keyCode == 82) {
                if (uniqueDown) {
                    hide();
                }
                return true;
            } else {
                show(3000);
                return super.dispatchKeyEvent(event);
            }
        }
    }

    private void updatePausePlay() {
        if (this.mRoot != null && this.mPauseButton != null) {
            if (this.mPlayer.isPlaying()) {
                this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
            } else {
                this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
            }
            this.mHandler.sendEmptyMessageDelayed(0, 200);
        }
    }

    public void update() {
        updatePausePlay();
    }

    private void doPauseResume() {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        } else {
            this.mPlayer.start();
        }
        updatePausePlay();
    }

    public void setEnabled(boolean enabled) {
        boolean z = true;
        if (this.mPauseButton != null) {
            this.mPauseButton.setEnabled(enabled);
        }
        if (this.mFfwdButton != null) {
            this.mFfwdButton.setEnabled(enabled);
        }
        if (this.mRewButton != null) {
            this.mRewButton.setEnabled(enabled);
        }
        if (this.mNextButton != null) {
            ImageButton imageButton = this.mNextButton;
            boolean z2 = enabled && this.mNextListener != null;
            imageButton.setEnabled(z2);
        }
        if (this.mPrevButton != null) {
            ImageButton imageButton2 = this.mPrevButton;
            if (!enabled || this.mPrevListener == null) {
                z = false;
            }
            imageButton2.setEnabled(z);
        }
        if (this.mProgress != null) {
            this.mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(FmMediaController.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(FmMediaController.class.getName());
    }

    private void installPrevNextListeners() {
        boolean z = true;
        if (this.mNextButton != null) {
            this.mNextButton.setOnClickListener(this.mNextListener);
            this.mNextButton.setEnabled(this.mNextListener != null);
        }
        if (this.mPrevButton != null) {
            this.mPrevButton.setOnClickListener(this.mPrevListener);
            ImageButton imageButton = this.mPrevButton;
            if (this.mPrevListener == null) {
                z = false;
            }
            imageButton.setEnabled(z);
        }
    }

    public void setPrevNextListeners(OnClickListener next, OnClickListener prev) {
        this.mNextListener = next;
        this.mPrevListener = prev;
        this.mListenersSet = true;
        if (this.mRoot != null) {
            installPrevNextListeners();
            if (!(this.mNextButton == null || this.mFromXml)) {
                this.mNextButton.setVisibility(0);
            }
            if (this.mPrevButton != null && !this.mFromXml) {
                this.mPrevButton.setVisibility(0);
            }
        }
    }
}
