package com.fimi.app.x8s.ui.album.x8s;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.album.adapter.MediaDetailViewPaperAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.widget.HackyViewPager;
import com.fimi.album.widget.MediaDetailDownloadStateView;
import com.fimi.album.widget.MediaDownloadProgressView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.ui.presenter.X8MediaDetailPresenter;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.videoview.X8CustomVideoView;
import com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener;
import com.fimi.app.x8s.widget.videoview.X8FmMediaInfo;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.widget.X8ToastUtil;

public class X8MediaDetailActivity extends BaseActivity implements VideoPlayerListener {
    private static final String TAG = "X8MediaDetailActivity";
    private int currentSelectPosition;
    private Button mBtnPlayMax;
    private Button mBtnStart;
    private X8CustomVideoView mCustomVideoView;
    private FragmentManager mFragmentManager;
    private ImageButton mIbtnBottomDelete;
    private ImageButton mIbtnDelete;
    private ImageButton mIbtnDwonload;
    private ImageButton mIbtnLeftSlide;
    private ImageButton mIbtnMediaBack;
    private ImageButton mIbtnRightSlide;
    private ImageView mIvTopBar;
    private MediaDetailDownloadStateView mMediaDetailDownloadStateView;
    private X8MediaDetailPresenter mMediaDetailPresenter;
    private MediaDetailViewPaperAdapter mMediaDetailViewPaperAdapter;
    private MediaDownloadProgressView mMediaDownloadProgressView;
    private RelativeLayout mRelativeLayout;
    private RelativeLayout mRlDelete;
    private RelativeLayout mRlDownload;
    private RelativeLayout mRlDownloadBottom;
    private RelativeLayout mRlSelectBottom;
    private TextView mTvDelete;
    private TextView mTvDownload;
    private TextView mTvFileName;
    private TextView mTvPercent;
    private HackyViewPager mViewPager;
    private TextView tvPhotoName;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
    }

    public void initData() {
        this.mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        this.mIvTopBar = (ImageView) findViewById(R.id.iv_top_bar);
        this.mViewPager = (HackyViewPager) findViewById(R.id.viewpaper);
        this.mIbtnMediaBack = (ImageButton) findViewById(R.id.media_back_btn);
        this.mIbtnBottomDelete = (ImageButton) findViewById(R.id.ibtn_delete);
        this.mBtnPlayMax = (Button) findViewById(R.id.btn_play_max);
        this.mIbtnLeftSlide = (ImageButton) findViewById(R.id.ibtn_left_slide);
        this.mIbtnRightSlide = (ImageButton) findViewById(R.id.ibtn_right_slide);
        this.mMediaDetailDownloadStateView = (MediaDetailDownloadStateView) findViewById(R.id.download_state_view);
        this.tvPhotoName = (TextView) findViewById(R.id.tv_photo_name);
        this.mTvFileName = (TextView) findViewById(R.id.tv_file_name);
        this.mTvPercent = (TextView) findViewById(R.id.tv_percent);
        this.mRlDelete = (RelativeLayout) findViewById(R.id.rl_delete);
        this.mRlDownload = (RelativeLayout) findViewById(R.id.rl_download);
        this.mIbtnDelete = (ImageButton) findViewById(R.id.ibtn_delete);
        this.mTvDelete = (TextView) findViewById(R.id.tv_bottom_delete);
        this.mIbtnDwonload = (ImageButton) findViewById(R.id.ibtn_download);
        this.mTvDownload = (TextView) findViewById(R.id.tv_bottom_download);
        this.mRlDownloadBottom = (RelativeLayout) findViewById(R.id.rl_media_download);
        this.mRlSelectBottom = (RelativeLayout) findViewById(R.id.rl_bottom_bar);
        this.mMediaDownloadProgressView = (MediaDownloadProgressView) findViewById(R.id.pv_progress);
        this.mBtnStart = (Button) findViewById(R.id.btn_start);
        FontUtil.changeFontLanTing(getAssets(), this.tvPhotoName);
        this.mIbtnDelete.setAlpha(1.0f);
        this.mIbtnDelete.setEnabled(true);
        this.mIbtnDwonload.setAlpha(1.0f);
        this.mIbtnDwonload.setEnabled(true);
        this.mTvDelete.setAlpha(1.0f);
        this.mTvDelete.setEnabled(true);
        this.mTvDownload.setAlpha(1.0f);
        this.mTvDownload.setEnabled(true);
        this.mMediaDownloadProgressView.setFrontColor(-16717571);
        this.mMediaDownloadProgressView.setMaxCount(100.0f);
        Intent intent = getIntent();
        if (intent != null) {
            this.currentSelectPosition = intent.getIntExtra(AlbumConstant.SELECTPOSITION, 0);
        }
        if (this.currentSelectPosition < 0) {
            this.currentSelectPosition = 0;
        }
        this.mMediaDetailPresenter = new X8MediaDetailPresenter(this, this.mViewPager);
        this.mMediaDetailViewPaperAdapter = new MediaDetailViewPaperAdapter(this.mMediaDetailPresenter);
        this.mMediaDetailViewPaperAdapter.notifyDataSetChanged();
        new LinearLayoutManager(this).setOrientation(0);
        this.mViewPager.setAdapter(this.mMediaDetailViewPaperAdapter);
        if (this.currentSelectPosition < this.mMediaDetailViewPaperAdapter.getCount()) {
            this.mMediaDetailPresenter.updateFileName(this.currentSelectPosition);
            this.mViewPager.setCurrentItem(this.currentSelectPosition);
            this.mMediaDetailPresenter.updateItem(this.currentSelectPosition);
        }
        this.mFragmentManager = getSupportFragmentManager();
    }

    public void doTrans() {
        this.mIbtnMediaBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8MediaDetailActivity.this.finish();
                X8MediaDetailActivity.this.mMediaDetailPresenter.setOnDestory();
            }
        });
        this.mIbtnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new X8DoubleCustomDialog(X8MediaDetailActivity.this.mContext, X8MediaDetailActivity.this.getString(R.string.x8_album_warn_tip), X8MediaDetailActivity.this.getString(R.string.album_dialog_delete_title), X8MediaDetailActivity.this.getString(R.string.media_delete), new onDialogButtonClickListener() {
                    public void onLeft() {
                    }

                    public void onRight() {
                        X8MediaDetailActivity.this.mMediaDetailPresenter.deleteItem(X8MediaDetailActivity.this.mViewPager.getCurrentItem());
                    }
                }).show();
            }
        });
        this.mIbtnDwonload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8MediaDetailActivity.this.mMediaDetailPresenter.downloadFile(X8MediaDetailActivity.this.mViewPager.getCurrentItem());
            }
        });
        this.mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                X8MediaDetailActivity.this.mMediaDetailPresenter.updateItem(position);
                X8MediaDetailActivity.this.mMediaDetailPresenter.showTopBottom(position);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.mIbtnLeftSlide.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8MediaDetailActivity.this.mViewPager.setCurrentItem(X8MediaDetailActivity.this.mViewPager.getCurrentItem() - 1, true);
            }
        });
        this.mIbtnRightSlide.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8MediaDetailActivity.this.mViewPager.setCurrentItem(X8MediaDetailActivity.this.mViewPager.getCurrentItem() + 1, true);
            }
        });
        this.mBtnPlayMax.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (X8MediaDetailActivity.this.mMediaDetailPresenter.isDownloadFinish()) {
                    X8MediaDetailActivity.this.mMediaDetailPresenter.startActivity();
                } else {
                    X8ToastUtil.showToast(X8MediaDetailActivity.this, X8MediaDetailActivity.this.getString(R.string.x8_download_hint), 1);
                }
            }
        });
        this.mBtnStart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8MediaDetailActivity.this.mMediaDetailPresenter.downloadFile(X8MediaDetailActivity.this.mViewPager.getCurrentItem());
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.x8_activity_media_detial;
    }

    public TextView getPhotoText() {
        return this.tvPhotoName;
    }

    public ImageView getIvTopBar() {
        return this.mIvTopBar;
    }

    public ImageButton getIbtnBottomDelete() {
        return this.mIbtnBottomDelete;
    }

    public X8CustomVideoView getCustomVideoView() {
        if (this.mCustomVideoView == null) {
            this.mCustomVideoView = new X8CustomVideoView(this, this.mRelativeLayout);
        }
        return this.mCustomVideoView;
    }

    public RelativeLayout getRelativeLayout() {
        return this.mRelativeLayout;
    }

    public FragmentManager getMediaFragmentManager() {
        return this.mFragmentManager;
    }

    public ImageButton getIbtnMediaBack() {
        return this.mIbtnMediaBack;
    }

    public void onBufferUpdate(int time) {
        LogUtil.i(TAG, "onBufferUpdate: time:" + time);
    }

    public void onClickFullScreenBtn() {
        LogUtil.i(TAG, "onClickFullScreenBtn: ");
    }

    public void onClickVideo() {
        LogUtil.i(TAG, "onClickVideo: ");
    }

    public void onClickBackBtn() {
        LogUtil.i(TAG, "onClickBackBtn: ");
    }

    public void onClickPlay(X8FmMediaInfo info) {
        LogUtil.i("moweiru", "onClickPlay===== " + info.toString());
        String name = this.mMediaDetailPresenter.getMediaFileName();
        if (name != null && !name.equals("")) {
            this.mMediaDetailPresenter.startActivity();
        }
    }

    public void onVideoPause(boolean isPause) {
        int i;
        int i2 = 8;
        boolean z = false;
        ImageButton imageButton = this.mIbtnRightSlide;
        if (isPause) {
            i = 8;
        } else {
            i = 0;
        }
        imageButton.setVisibility(i);
        ImageButton imageButton2 = this.mIbtnLeftSlide;
        if (!isPause) {
            i2 = 0;
        }
        imageButton2.setVisibility(i2);
        HackyViewPager hackyViewPager = this.mViewPager;
        if (!isPause) {
            z = true;
        }
        hackyViewPager.setScrollble(z);
        LogUtil.i(TAG, "onVideoPause: ");
    }

    public void onVideoLoadSuccess() {
        LogUtil.i(TAG, "onVideoLoadSuccess: ");
    }

    public void onVideoLoadFailed() {
        LogUtil.i(TAG, "onVideoLoadFailed: ");
    }

    public void onVideoLoadComplete() {
        LogUtil.i(TAG, "onVideoLoadComplete: ");
    }

    public void showBar(boolean isShow) {
        if (isShow) {
            showTopBar(true);
            this.mMediaDetailPresenter.updateItem(this.mMediaDetailPresenter.getCurrentPosition());
            return;
        }
        showTopBar(false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.mMediaDetailPresenter.setOnDestory();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showTopBar(boolean isShow) {
        int i;
        int i2 = 0;
        this.mIvTopBar.setVisibility(isShow ? 0 : 4);
        RelativeLayout relativeLayout = this.mRlSelectBottom;
        if (isShow) {
            i = 0;
        } else {
            i = 4;
        }
        relativeLayout.setVisibility(i);
        relativeLayout = this.mRlDownloadBottom;
        if (isShow) {
            i = 0;
        } else {
            i = 4;
        }
        relativeLayout.setVisibility(i);
        ImageButton imageButton = this.mIbtnMediaBack;
        if (isShow) {
            i = 0;
        } else {
            i = 4;
        }
        imageButton.setVisibility(i);
        TextView textView = this.tvPhotoName;
        if (!isShow) {
            i2 = 4;
        }
        textView.setVisibility(i2);
    }

    public boolean topBarShowing() {
        return this.mIvTopBar.getVisibility() == 0;
    }

    public MediaDetailViewPaperAdapter getMediaDetailViewPaperAdapter() {
        return this.mMediaDetailViewPaperAdapter;
    }

    public Button getBtnPlayMax() {
        return this.mBtnPlayMax;
    }

    public TextView getTvFileName() {
        return this.mTvFileName;
    }

    public TextView getTvPercent() {
        return this.mTvPercent;
    }

    public RelativeLayout getRlDelete() {
        return this.mRlDelete;
    }

    public RelativeLayout getRlDownload() {
        return this.mRlDownload;
    }

    public ImageButton getIbtnDelete() {
        return this.mIbtnDelete;
    }

    public ImageButton getIbtnDwonload() {
        return this.mIbtnDwonload;
    }

    public Button getBtnStart() {
        return this.mBtnStart;
    }

    public RelativeLayout getRlDownloadBottom() {
        return this.mRlDownloadBottom;
    }

    public RelativeLayout getRlSelectBottom() {
        return this.mRlSelectBottom;
    }

    public MediaDownloadProgressView getMediaDownloadProgressView() {
        return this.mMediaDownloadProgressView;
    }
}
