package com.fimi.album.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.album.R;
import com.fimi.album.adapter.MediaDetailViewPaperAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.presenter.MediaDetailPresenter;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.FontUtil;

public class MediaDetailActivity extends BaseActivity {
    private int currentSelectPosition;
    private ImageButton ibBottomDelete;
    private ImageButton ibMediaBack;
    private LinearLayout llHeadViewGroup;
    private MediaDetailPresenter mMediaDetailPresenter;
    private MediaDetailViewPaperAdapter mMediaDetailViewPaperAdapter;
    private RelativeLayout rlHeadDirection;
    private TextView tvDeleteTip;
    private TextView tvPhotoName;
    private ViewPager viewpaper;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }

    public void initData() {
        this.llHeadViewGroup = (LinearLayout) findViewById(R.id.shoto_top_tab_ll);
        this.rlHeadDirection = (RelativeLayout) findViewById(R.id.media_select_bottom_rl);
        this.rlHeadDirection.setVisibility(0);
        this.viewpaper = (ViewPager) findViewById(R.id.viewpaper);
        this.ibMediaBack = (ImageButton) findViewById(R.id.media_back_btn);
        this.ibBottomDelete = (ImageButton) findViewById(R.id.bottom_delete_ibtn);
        this.tvDeleteTip = (TextView) findViewById(R.id.delete_tv);
        this.tvPhotoName = (TextView) findViewById(R.id.photo_name_tv);
        FontUtil.changeFontLanTing(getAssets(), this.tvDeleteTip, this.tvPhotoName);
        Intent intent = getIntent();
        if (intent != null) {
            this.currentSelectPosition = intent.getIntExtra(AlbumConstant.SELECTPOSITION, 0);
        }
        this.mMediaDetailPresenter = new MediaDetailPresenter(this, this.viewpaper);
        this.mMediaDetailViewPaperAdapter = new MediaDetailViewPaperAdapter(this.mMediaDetailPresenter);
        new LinearLayoutManager(this).setOrientation(0);
        this.viewpaper.setAdapter(this.mMediaDetailViewPaperAdapter);
        if (this.currentSelectPosition < this.mMediaDetailViewPaperAdapter.getCount()) {
            this.mMediaDetailPresenter.updateFileName(this.currentSelectPosition);
            this.viewpaper.setCurrentItem(this.currentSelectPosition);
        }
    }

    public void doTrans() {
        this.ibMediaBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MediaDetailActivity.this.finish();
            }
        });
        this.ibBottomDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MediaDetailActivity.this.mMediaDetailPresenter.deleteItem(MediaDetailActivity.this.viewpaper.getCurrentItem());
            }
        });
        this.viewpaper.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                MediaDetailActivity.this.mMediaDetailPresenter.updateItem(position);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.album_activity_media_detial;
    }

    public TextView getPhotoText() {
        return this.tvPhotoName;
    }

    public RelativeLayout getRlHeadDirection() {
        return this.rlHeadDirection;
    }

    public LinearLayout getLlHeadViewGroup() {
        return this.llHeadViewGroup;
    }
}
