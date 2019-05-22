package com.fimi.app.x8s.ui.album.x8s;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class CustomMediaContoller {
    private static final int MESSAGE_HIDE_CONTOLL = 5;
    private static final int MESSAGE_SEEK_NEW_POSITION = 4;
    private static final int MESSAGE_SHOW_PROGRESS = 2;
    private static final int PAUSE_IMAGE_HIDE = 3;
    private static final int SET_VIEW_HIDE = 1;
    private static final int TIME_OUT = 3000;
    private final int SEEKBAR_TIME = 1000;
    private TextView allTime;
    private ImageView backBtn;
    private Context context;
    private long duration;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    CustomMediaContoller.this.hide();
                    return;
                case 2:
                    CustomMediaContoller.this.setProgress();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean isDragging;
    private boolean isShow;
    private boolean isShowContoller;
    private View itemView;
    private IFmMediaPlayer mediaPlayer;
    private ImageView miniPlay;
    private TextView name;
    private ImageView play;
    private SeekBar seekBar;
    private TextView time;
    private View view;

    public CustomMediaContoller(Context context, View view, IFmMediaPlayer meidaPlayer) {
        this.view = view;
        this.mediaPlayer = meidaPlayer;
        this.itemView = view.findViewById(R.id.media_contoller);
        this.itemView.setVisibility(4);
        this.isShow = false;
        this.isDragging = false;
        this.isShowContoller = true;
        this.context = context;
        this.duration = meidaPlayer.getDuration();
        initView();
        initAction();
    }

    private void initView() {
        this.seekBar = (SeekBar) this.itemView.findViewById(R.id.play_sb);
        this.allTime = (TextView) this.itemView.findViewById(R.id.total_time_tv);
        this.time = (TextView) this.itemView.findViewById(R.id.time_current_tv);
        this.miniPlay = (ImageView) this.itemView.findViewById(R.id.mini_player_btn);
        this.play = (ImageView) this.view.findViewById(R.id.player_btn);
        this.time.setText("" + generateTime(0));
        this.allTime.setText("" + generateTime(this.duration));
        this.name = (TextView) this.itemView.findViewById(R.id.tv_photo_name);
        this.backBtn = (ImageView) this.itemView.findViewById(R.id.ibtn_return);
    }

    public void setNameAndDuration(String mediaName, String duration) {
        this.name.setText(mediaName);
        this.allTime.setText(duration);
    }

    private void initAction() {
        this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CustomMediaContoller.this.time.setText(CustomMediaContoller.this.generateTime((long) ((((float) (CustomMediaContoller.this.duration * ((long) progress))) * 1.0f) / 100.0f)));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                CustomMediaContoller.this.setProgress();
                CustomMediaContoller.this.isDragging = true;
                CustomMediaContoller.this.handler.removeMessages(2);
                CustomMediaContoller.this.show();
                CustomMediaContoller.this.handler.removeMessages(1);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                CustomMediaContoller.this.isDragging = false;
                CustomMediaContoller.this.mediaPlayer.seekTo((int) ((((float) (CustomMediaContoller.this.duration * ((long) seekBar.getProgress()))) * 1.0f) / 100.0f));
                CustomMediaContoller.this.handler.removeMessages(2);
                CustomMediaContoller.this.isDragging = false;
                CustomMediaContoller.this.handler.sendEmptyMessageDelayed(2, 1000);
                CustomMediaContoller.this.show();
            }
        });
        this.view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        if (!CustomMediaContoller.this.isShow) {
                            CustomMediaContoller.this.show();
                            break;
                        }
                        CustomMediaContoller.this.hide();
                        break;
                    case 1:
                        if (CustomMediaContoller.this.isShow) {
                            CustomMediaContoller.this.show();
                            break;
                        }
                        break;
                    case 2:
                        CustomMediaContoller.this.handler.removeMessages(1);
                        break;
                }
                return true;
            }
        });
        this.play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CustomMediaContoller.this.onPlay();
            }
        });
        this.miniPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CustomMediaContoller.this.onPlay();
            }
        });
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CustomMediaContoller.this.mediaPlayer.onDestroy();
            }
        });
    }

    private long setProgress() {
        if (this.isDragging) {
            return 0;
        }
        long position = this.mediaPlayer.getCurrentPosition();
        this.duration = this.mediaPlayer.getDuration();
        if (!generateTime(this.duration).equals(this.allTime.getText().toString())) {
            this.allTime.setText(generateTime(this.duration));
        }
        if (this.seekBar != null && this.duration > 0) {
            long pos = (100 * position) / this.duration;
            if (((int) (position / 1000)) % 60 > 0) {
                this.seekBar.setProgress((int) pos);
            } else {
                this.seekBar.setProgress(0);
            }
        }
        this.time.setText(generateTime(position));
        this.handler.sendEmptyMessageDelayed(2, 1000);
        return position;
    }

    public void onPlay() {
        if (this.mediaPlayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    public void start() {
        this.play.setVisibility(8);
        this.miniPlay.setImageResource(R.drawable.x8_btn_media_stop_selector);
        this.mediaPlayer.start();
    }

    public void pause() {
        this.miniPlay.setImageResource(R.drawable.x8_btn_media_play_selector);
        this.play.setVisibility(0);
        this.mediaPlayer.pause();
        this.handler.removeMessages(2);
        this.handler.removeMessages(1);
    }

    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        if (totalSeconds / 3600 > 0) {
            return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(totalSeconds / 3600), Integer.valueOf(minutes), Integer.valueOf(seconds)});
        }
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    public void hide() {
        if (this.isShow) {
            this.isShow = false;
            this.handler.removeMessages(1);
            this.itemView.setVisibility(4);
        }
    }

    public void show(int timeout) {
        this.handler.removeMessages(1);
        this.handler.sendEmptyMessageDelayed(1, (long) timeout);
    }

    public void show() {
        if (this.isShowContoller) {
            this.isShow = true;
            this.itemView.setVisibility(0);
            show(3000);
        }
    }

    public void setShowContoller(boolean isShowContoller) {
        this.isShowContoller = isShowContoller;
        if (isShowContoller) {
            this.isShow = true;
            this.itemView.setVisibility(0);
        }
    }

    public void reset() {
        this.time.setText("" + generateTime(0));
        this.play.setImageResource(R.drawable.album_btn_media_play_big_selector);
        this.miniPlay.setImageResource(R.drawable.x8_btn_media_play_selector);
        this.seekBar.setProgress(0);
        this.play.setVisibility(0);
        this.handler.removeMessages(2);
        this.itemView.setVisibility(0);
    }

    public void startSeekbar() {
        this.handler.sendEmptyMessageDelayed(2, 1000);
        show();
    }

    public void onError() {
        this.play.setImageResource(R.drawable.album_btn_media_play_big_selector);
        this.miniPlay.setImageResource(R.drawable.x8_btn_media_play_selector);
        this.handler.removeMessages(2);
        this.play.setVisibility(0);
    }
}
