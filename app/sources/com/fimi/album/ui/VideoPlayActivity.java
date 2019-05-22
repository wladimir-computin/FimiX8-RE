package com.fimi.album.ui;

import android.view.KeyEvent;
import android.widget.RelativeLayout;
import com.example.album.R;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.iview.IVideoPlayer;
import com.fimi.album.presenter.VideoPlayerPresneter;
import com.fimi.album.widget.CustomVideoView;
import com.fimi.album.widget.CustomVideoView.VideoPlayerListener;
import com.fimi.kernel.base.BaseActivity;

public class VideoPlayActivity extends BaseActivity implements VideoPlayerListener, IVideoPlayer {
    private CustomVideoView mCustomVideoView;
    private RelativeLayout mRl;
    private String mUrl;
    private VideoPlayerPresneter mVideoPlayerPresneter;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }

    public void initData() {
        this.mRl = (RelativeLayout) findViewById(R.id.activity_video_play);
        this.mUrl = getIntent().getStringExtra(AlbumConstant.VIDEOPLARURL);
        this.mVideoPlayerPresneter = new VideoPlayerPresneter(this);
        this.mCustomVideoView = new CustomVideoView(this, this.mRl);
        this.mCustomVideoView.setDataSource(this.mUrl);
        this.mCustomVideoView.setListener(this);
        this.mRl.addView(this.mCustomVideoView);
    }

    public void doTrans() {
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.album_activity_video_play;
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        if (this.mVideoPlayerPresneter != null) {
            this.mVideoPlayerPresneter.startPlay();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        if (this.mVideoPlayerPresneter != null) {
            this.mVideoPlayerPresneter.removeCallBack();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mCustomVideoView.destory();
        this.mRl.removeView(this.mCustomVideoView);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBufferUpdate(int time) {
    }

    public void onClickFullScreenBtn() {
    }

    public void onClickVideo() {
    }

    public void onClickBackBtn() {
        finish();
    }

    public void onClickPlay() {
    }

    public void onAdVideoLoadSuccess() {
    }

    public void onAdVideoLoadFailed() {
    }

    public void onAdVideoLoadComplete() {
        finish();
    }

    public void timeFunction() {
        if (this.mCustomVideoView != null) {
            this.mCustomVideoView.timeFunction();
        }
    }
}
