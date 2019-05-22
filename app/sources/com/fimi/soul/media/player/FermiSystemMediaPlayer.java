package com.fimi.soul.media.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.fimi.soul.media.player.IFermiMediaPlayer.FermiPlyaerState;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class FermiSystemMediaPlayer implements IFermiMediaPlayer, Callback, OnSeekBarChangeListener {
    private Context context;
    private String dataSourceUrl;
    private Handler handler = new InnerHandler();
    private boolean isAutoPlay;
    private boolean ischanging = false;
    private OnMediaSizeChangedListener onMediaSizeChangedListener;
    private List<OnPlayerStateChangedListener> onPlayerStateChangedListenerArray = new ArrayList();
    private OnProgressChangedListener onProgressChangedListener;
    private MediaPlayer player = null;
    private SeekBar seekBar;
    private int startPosition = 0;
    private SurfaceView surfaceView;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    class InnerHandler extends Handler {
        InnerHandler() {
        }

        public void handleMessage(Message msg) {
            if (FermiSystemMediaPlayer.this.onProgressChangedListener != null) {
                FermiSystemMediaPlayer.this.onProgressChangedListener.onProgressChanged((long) msg.arg1, (long) msg.arg2);
            }
            super.handleMessage(msg);
        }
    }

    private Context getContext() {
        return this.context;
    }

    public FermiSystemMediaPlayer(Context context) {
        this.context = context;
        this.timerTask = new TimerTask() {
            public void run() {
                if (!FermiSystemMediaPlayer.this.ischanging && FermiSystemMediaPlayer.this.player != null && FermiSystemMediaPlayer.this.seekBar != null && !FermiSystemMediaPlayer.this.ischanging) {
                    FermiSystemMediaPlayer.this.seekBar.setProgress(FermiSystemMediaPlayer.this.player.getCurrentPosition());
                    Message msg = new Message();
                    msg.arg1 = FermiSystemMediaPlayer.this.player.getCurrentPosition();
                    msg.arg2 = FermiSystemMediaPlayer.this.player.getDuration();
                    FermiSystemMediaPlayer.this.handler.sendMessage(msg);
                }
            }
        };
        this.timer.schedule(this.timerTask, 0, 1000);
    }

    public void play() {
        if (this.player != null) {
            try {
                if (!(this.player == null || this.player.isPlaying())) {
                    this.player.stop();
                }
                this.player.prepare();
                if (!isAutoPlay()) {
                    this.player.start();
                    onPlayerStateChange(FermiPlyaerState.Playing);
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void prepare() {
        if (this.player != null) {
            try {
                this.player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (this.player != null && this.player.isPlaying()) {
            this.player.pause();
            onPlayerStateChange(FermiPlyaerState.Pause);
        }
    }

    public void stop() {
        if (this.player != null) {
            this.player.stop();
            onPlayerStateChange(FermiPlyaerState.Stop);
        }
    }

    public void setMediaUri(String uri, String user, String password) {
        this.dataSourceUrl = uri;
        if (this.player != null) {
            try {
                this.player.setDataSource(this.dataSourceUrl);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e2) {
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                e3.printStackTrace();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
    }

    public void setMediaUri(String uri) {
        setMediaUri(uri, null, null);
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        surfaceView.getHolder().addCallback(this);
    }

    public void setSeekBar(SeekBar sb) {
        this.seekBar = sb;
        sb.setOnSeekBarChangeListener(this);
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.onProgressChangedListener = listener;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.player = new MediaPlayer();
        this.player.setAudioStreamType(3);
        this.player.setDisplay(surfaceHolder);
        this.player.setOnSeekCompleteListener(new OnSeekCompleteListener() {
            public void onSeekComplete(MediaPlayer mp) {
                FermiSystemMediaPlayer.this.ischanging = false;
                mp.start();
            }
        });
        try {
            this.player.setDataSource(this.dataSourceUrl);
            this.player.prepare();
            this.player.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    if (FermiSystemMediaPlayer.this.seekBar != null) {
                        FermiSystemMediaPlayer.this.seekBar.setMax(mp.getDuration());
                    }
                    if (FermiSystemMediaPlayer.this.isAutoPlay) {
                        FermiSystemMediaPlayer.this.seekTo((long) FermiSystemMediaPlayer.this.startPosition);
                        mp.start();
                        FermiSystemMediaPlayer.this.onPlayerStateChange(FermiPlyaerState.Playing);
                    }
                }
            });
            this.player.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                }
            });
            this.player.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    if (FermiSystemMediaPlayer.this.onMediaSizeChangedListener != null) {
                        Log.d("Good", width + ":" + height);
                        FermiSystemMediaPlayer.this.onMediaSizeChangedListener.onMediaSizeChanged(FermiSystemMediaPlayer.this, width, height);
                    }
                }
            });
            this.player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (this.player.isPlaying()) {
            this.player.stop();
        }
    }

    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
        if (fromUser) {
            this.player.seekTo(progress);
            if (this.onProgressChangedListener != null) {
                this.onProgressChangedListener.onProgressChanged((long) progress, (long) this.player.getDuration());
            }
        }
    }

    public void onStartTrackingTouch(SeekBar arg0) {
        this.ischanging = true;
    }

    public void onStopTrackingTouch(SeekBar arg0) {
    }

    public boolean isAutoPlay() {
        return this.isAutoPlay;
    }

    public void setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public boolean isPlaying() {
        if (this.player != null) {
            return this.player.isPlaying();
        }
        return false;
    }

    public void addOnPlayerStateChangedListener(OnPlayerStateChangedListener listener) {
        if (listener != null) {
            this.onPlayerStateChangedListenerArray.add(listener);
        }
    }

    private void onPlayerStateChange(FermiPlyaerState state) {
        if (this.onPlayerStateChangedListenerArray.size() > 0) {
            for (OnPlayerStateChangedListener item : this.onPlayerStateChangedListenerArray) {
                item.OnPlayerStateChanged(state, this);
            }
        }
    }

    public int getPosition() {
        return this.player.getCurrentPosition();
    }

    public long getDuration() {
        return (long) this.player.getDuration();
    }

    public void seekTo(long position) {
        this.player.seekTo((int) position);
    }

    public long getCurrentPosition() {
        return (long) this.player.getCurrentPosition();
    }

    public void setPlayPosition(int position) {
        this.startPosition = position;
    }

    public void setOnMediaSizeChangedListener(OnMediaSizeChangedListener listener) {
        this.onMediaSizeChangedListener = listener;
    }
}
