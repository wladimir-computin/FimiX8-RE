package com.fimi.app.x8s.ui.album.x8s;

import android.os.Build.VERSION;
import android.view.View;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.ui.album.x8s.FmMediaPlayer.OnActivityHander;
import com.fimi.app.x8s.widget.videoview.X8FmMediaInfo;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.permission.PermissionManager;
import com.fimi.player.widget.FimiVideoView;

public class X8FimiPlayerActivity extends BaseActivity implements OnActivityHander {
    private FmMediaPlayer mediaPlayer;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }

    public void initData() {
        if (VERSION.SDK_INT >= 23) {
            PermissionManager.requestFind_LocationPermissions();
            PermissionManager.requestCoarseLocationPermissions();
            PermissionManager.requestStoragePermissions();
        }
        X8FmMediaInfo info = (X8FmMediaInfo) getIntent().getSerializableExtra(FmMediaPlayer.FM_MEDIA_INFO);
        getWindow().setFlags(1024, 1024);
        View controlView = findViewById(R.id.media_layout);
        this.mediaPlayer = new FmMediaPlayer((FimiVideoView) findViewById(R.id.fimi_video), info, this);
        this.mediaPlayer.initFmPlayer(this, controlView);
        this.mediaPlayer.startPlay();
        this.mediaPlayer.setShowContoller(true);
    }

    public void doTrans() {
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_x8_fimi_player;
    }

    public void onBack() {
        finish();
    }

    public void onBackPressed() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.onDestroy();
        }
    }
}
