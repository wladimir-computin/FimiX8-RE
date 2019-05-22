package com.fimi.app.x8s.ui.album.x8s;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.widget.HackyViewPager;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.CategoryAdapter;
import com.fimi.app.x8s.manager.X8FpvManager;
import com.fimi.app.x8s.ui.presenter.X8MediaPresenter;
import com.fimi.app.x8s.ui.presenter.X8MediaPresenter.IMediaCameraConnected;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.widget.CustomLoadManage;
import com.fimi.x8sdk.modulestate.StateManager;
import java.util.LinkedList;
import java.util.List;

public class X8MediaActivity extends BaseActivity implements ISelectData, IMediaCameraConnected {
    private static final String TAG = "X8MediaActivity";
    private boolean isFirstLoadLocalMedia;
    Button mBtnCancle;
    Button mBtnIsSelect;
    private CategoryAdapter mCategolyAdapter;
    private List<Fragment> mFragmentList;
    HackyViewPager mHackyViewPager;
    ImageButton mIbtnReturn;
    RelativeLayout mRlHead;
    RelativeLayout mRlTopBar;
    TabLayout mTlTitleCategoly;
    TextView mTvMediaSelect;
    TextView mTvSelectTitle;
    private X8CameraFragment mX8CameraFragment;
    private X8LocalMediaLocalFragment mX8LocalMediaFragment;
    private X8MediaPresenter mX8MediaPresenter;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_x8_media;
    }

    public void initData() {
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        X8FpvManager.isUpdateing = true;
        initView();
        getX9MediaPresenter();
        this.mX8MediaPresenter.setCameraConnectedState(this);
        this.mX8CameraFragment = new X8CameraFragment();
        this.mX8LocalMediaFragment = new X8LocalMediaLocalFragment();
        this.mFragmentList = new LinkedList();
        this.mFragmentList.add(this.mX8CameraFragment);
        this.mFragmentList.add(this.mX8LocalMediaFragment);
        this.mCategolyAdapter = new CategoryAdapter(getSupportFragmentManager(), this.mFragmentList);
        this.mHackyViewPager.setAdapter(this.mCategolyAdapter);
        this.mHackyViewPager.setOverScrollMode(2);
        this.mTlTitleCategoly.setupWithViewPager(this.mHackyViewPager);
        for (int index = 0; index < this.mTlTitleCategoly.getTabCount(); index++) {
            View tabItem = LayoutInflater.from(this).inflate(R.layout.x8_tab_view, null);
            if (StateManager.getInstance().getCamera().getToken() > 0) {
                if (index == 0) {
                    changeViewVariablw(tabItem, getResources().getColor(R.color.x8_media_tab_select), 0, R.string.x8_online_media);
                } else {
                    changeViewVariablw(tabItem, getResources().getColor(R.color.x8_media_tab_unselect), 4, R.string.x8_local_media);
                }
            } else if (index == 0) {
                changeViewVariablw(tabItem, getResources().getColor(R.color.x8_media_tab_unselect), 0, R.string.x8_online_media);
            } else {
                changeViewVariablw(tabItem, getResources().getColor(R.color.x8_media_tab_select), 0, R.string.x8_local_media);
            }
            Tab tab = this.mTlTitleCategoly.getTabAt(index);
            if (tab != null) {
                tab.setCustomView(tabItem);
            }
        }
    }

    private void initView() {
        this.mIbtnReturn = (ImageButton) findViewById(R.id.ibtn_return);
        this.mTvMediaSelect = (TextView) findViewById(R.id.tv_media_select);
        this.mTlTitleCategoly = (TabLayout) findViewById(R.id.tl_title_categoly);
        this.mRlHead = (RelativeLayout) findViewById(R.id.rl_head);
        this.mHackyViewPager = (HackyViewPager) findViewById(R.id.viewpaper);
        this.mBtnCancle = (Button) findViewById(R.id.btn_cancle);
        this.mBtnIsSelect = (Button) findViewById(R.id.btn_is_select);
        this.mTvSelectTitle = (TextView) findViewById(R.id.tv_select_title);
        this.mRlTopBar = (RelativeLayout) findViewById(R.id.rl_top_bar);
        FontUtil.changeFontLanTing(getAssets(), this.mTvMediaSelect, this.mBtnCancle, this.mBtnIsSelect, this.mTvSelectTitle);
    }

    private void changeViewVariablw(View view, int resColor, int indicatorState, int resStr) {
        TextView tvTitleDescription = (TextView) view.findViewById(R.id.tv_title_desprition);
        tvTitleDescription.setTextColor(resColor);
        if (resStr != 0) {
            tvTitleDescription.setText(resStr);
        }
        FontUtil.changeFontLanTing(getAssets(), tvTitleDescription);
    }

    public void doTrans() {
        this.mIbtnReturn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (X8MediaFileDownloadManager.getInstance().hasDownloading()) {
                    X8MediaActivity.this.showDialogTip();
                } else {
                    X8MediaActivity.this.finish();
                }
            }
        });
        this.mTlTitleCategoly.addOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                X8MediaActivity.this.changeViewVariablw(tab.getCustomView(), X8MediaActivity.this.getResources().getColor(R.color.x8_media_tab_select), 0, 0);
                X8MediaActivity.this.getX9MediaPresenter().currentFragmentType();
                if (X8MediaActivity.this.isFirstLoadLocalMedia) {
                    X8MediaActivity.this.isFirstLoadLocalMedia = false;
                } else {
                    X8MediaActivity.this.getX9MediaPresenter().switchLoadMedia();
                }
            }

            public void onTabUnselected(Tab tab) {
                X8MediaActivity.this.changeViewVariablw(tab.getCustomView(), X8MediaActivity.this.getResources().getColor(R.color.x8_media_tab_unselect), 4, 0);
            }

            public void onTabReselected(Tab tab) {
            }
        });
        this.mTvMediaSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8MediaActivity.this.getX9MediaPresenter().enterSelectMode(true, true);
                if (X9HandleType.isCameraView()) {
                    X8MediaActivity.this.mTvSelectTitle.setText(X8MediaActivity.this.getString(R.string.album_select_camera_title, new Object[]{"0", "0KB"}));
                } else {
                    X8MediaActivity.this.mTvSelectTitle.setText(X8MediaActivity.this.getString(R.string.album_select_title, new Object[]{"0"}));
                }
                X8MediaActivity.this.mRlTopBar.setVisibility(0);
                X8MediaActivity.this.mHackyViewPager.setScrollble(false);
                X8MediaActivity.this.changeBtnSelectState(X8MediaActivity.this.getString(com.example.album.R.string.media_select_all), X8MediaActivity.this.mBtnIsSelect);
            }
        });
        this.mBtnCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8MediaActivity.this.getX9MediaPresenter().enterSelectMode(false, true);
                X8MediaActivity.this.mHackyViewPager.setScrollble(true);
                X8MediaActivity.this.mRlTopBar.setVisibility(8);
            }
        });
        this.mBtnIsSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (X8MediaActivity.this.mBtnIsSelect.getText().equals(X8MediaActivity.this.getString(com.example.album.R.string.media_select_all))) {
                    X8MediaActivity.this.getX9MediaPresenter().selectBtn(true);
                    X8MediaActivity.this.changeBtnSelectState(X8MediaActivity.this.getString(com.example.album.R.string.media_select_all_no), X8MediaActivity.this.mBtnIsSelect);
                    return;
                }
                X8MediaActivity.this.getX9MediaPresenter().selectBtn(false);
                X8MediaActivity.this.changeBtnSelectState(X8MediaActivity.this.getString(com.example.album.R.string.media_select_all), X8MediaActivity.this.mBtnIsSelect);
            }
        });
    }

    private void changeBtnSelectState(String changeText, TextView textView) {
        textView.setText(changeText);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHackyViewPager.setCurrentItem(0);
        getmX8LocalMediaFragment().unRegisterReciver();
        getmX8CameraFragment().unRegisterReciver();
        X8MediaFileDownloadManager.getInstance().stopAllDownload();
        X8MediaThumDownloadManager.getInstance().stopDownload();
        NoticeManager.getInstance().removeALLMediaListener();
        X8FpvManager.isUpdateing = false;
    }

    public void selectSize(int size, long capacity) {
        if (X9HandleType.isCameraView()) {
            String capacityStr;
            float capacityfloat = ((float) capacity) / 1024.0f;
            if (capacityfloat > 1024.0f) {
                capacityfloat /= 1024.0f;
                if (capacityfloat > 1024.0f) {
                    capacityStr = NumberUtil.decimalPointStr((double) (capacityfloat / 1024.0f), 1) + "G";
                } else {
                    capacityStr = NumberUtil.decimalPointStr((double) capacityfloat, 1) + "M";
                }
            } else {
                capacityStr = NumberUtil.decimalPointStr((double) capacityfloat, 1) + "KB";
            }
            if (size == 0) {
                this.mTvSelectTitle.setText(getString(R.string.album_select_camera_title, new Object[]{size + "", "0KB"}));
            } else {
                this.mTvSelectTitle.setText(getString(R.string.album_select_camera_title, new Object[]{size + "", capacityStr}));
            }
        } else {
            this.mTvSelectTitle.setText(getString(R.string.album_select_title, new Object[]{size + ""}));
        }
        getX9MediaPresenter().selectFileSize(size);
    }

    public void enterSelectMode() {
        this.mRlTopBar.setVisibility(0);
        getX9MediaPresenter().enterSelectMode(true, true);
        LogUtil.i(TAG, "enterSelectMode: ");
    }

    public void quitSelectMode() {
        this.mRlTopBar.setVisibility(8);
        this.mHackyViewPager.setScrollble(true);
        getX9MediaPresenter().enterSelectMode(false, false);
    }

    public void deleteFile() {
        this.mRlTopBar.setVisibility(8);
        this.mHackyViewPager.setScrollble(true);
        getX9MediaPresenter().enterSelectMode(false, false);
    }

    public void allSelectMode(boolean isAll) {
        if (isAll) {
            changeBtnSelectState(getString(com.example.album.R.string.media_select_all_no), this.mBtnIsSelect);
        } else {
            changeBtnSelectState(getString(com.example.album.R.string.media_select_all), this.mBtnIsSelect);
        }
    }

    public void startDownload() {
        this.mRlTopBar.setVisibility(8);
        this.mHackyViewPager.setScrollble(true);
        getX9MediaPresenter().enterSelectMode(false, false);
    }

    public void onDeleteComplete() {
        showSelectBtn();
    }

    public void initComplete(boolean isCamera) {
        if (isCamera) {
            getX9MediaPresenter().removeCameraDefaultVaribale();
            getX9MediaPresenter().forCameraFolder();
        } else {
            getX9MediaPresenter().reDefaultVaribale();
            getX9MediaPresenter().forEachFile(DirectoryPath.getX8LocalMedia());
        }
        if (StateManager.getInstance().getCamera().getToken() > 0 && isCamera) {
            this.mHackyViewPager.setCurrentItem(0);
        } else if (StateManager.getInstance().getCamera().getToken() < 0 && !isCamera) {
            this.isFirstLoadLocalMedia = true;
            this.mHackyViewPager.setCurrentItem(1);
        }
    }

    public void addSingleFile() {
        this.mTvMediaSelect.setVisibility(0);
        getmX8LocalMediaFragment().noDataTipCallback(false);
    }

    public List<Fragment> getFragmentList() {
        return this.mFragmentList;
    }

    public TabLayout getTlTitleCategoly() {
        return this.mTlTitleCategoly;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.mRlTopBar.getVisibility() == 0) {
                getX9MediaPresenter().enterSelectMode(false, true);
                this.mHackyViewPager.setScrollble(true);
                this.mRlTopBar.setVisibility(8);
                return true;
            } else if (X8MediaFileDownloadManager.getInstance().hasDownloading()) {
                showDialogTip();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showDialogTip() {
        new X8DoubleCustomDialog(this, getString(R.string.x8_album_warn_tip), getString(R.string.x8_album_exit_tip), new onDialogButtonClickListener() {
            public void onLeft() {
            }

            public void onRight() {
                X8MediaActivity.this.finish();
            }
        }).show();
    }

    public X8MediaPresenter getX9MediaPresenter() {
        if (this.mX8MediaPresenter == null) {
            this.mX8MediaPresenter = new X8MediaPresenter(this);
        }
        return this.mX8MediaPresenter;
    }

    public void showSelectBtn() {
        if (this.mX8MediaPresenter.isModelListEmpty()) {
            this.mTvMediaSelect.setVisibility(4);
        } else {
            this.mTvMediaSelect.setVisibility(0);
        }
    }

    public RelativeLayout getRlTopBar() {
        return this.mRlTopBar;
    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    public X8CameraFragment getmX8CameraFragment() {
        return this.mX8CameraFragment;
    }

    public X8LocalMediaLocalFragment getmX8LocalMediaFragment() {
        return this.mX8LocalMediaFragment;
    }

    public void onCameraConnectedState(boolean isConnected) {
        if (!isConnected) {
            this.mX8MediaPresenter.onDisConnect();
            X8MediaFileDownloadManager.getInstance().stopAllDownload();
            CustomLoadManage.dismiss();
        }
    }
}
