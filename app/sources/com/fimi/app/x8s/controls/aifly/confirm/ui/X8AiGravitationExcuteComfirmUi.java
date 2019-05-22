package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8SeekBarView;
import com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener;
import com.fimi.app.x8s.widget.X8TabHost;

public class X8AiGravitationExcuteComfirmUi implements OnClickListener {
    private int HIGHT_MAX = 10;
    private int HIGHT_MAX_PROGRESS = ((this.HIGHT_MAX - this.HIGHT_MIN) * 20);
    private int HIGHT_MIN = 5;
    private int LEVEL_MAX = 30;
    private int LEVEL_MAX_PROGRESS = ((this.LEVEL_MAX - this.LEVEL_MIN) * 10);
    private int LEVEL_MIN = 20;
    private final View contentView;
    private Button mBtnExcuteAdvancedSetting;
    private Button mBtnExcuteDefault;
    private Button mBtnExcuteOk;
    private Button mBtnExcuteSave;
    private ImageView mExcuteReturn;
    private TextView mExcuteTitle;
    private LinearLayout mLlAdvancedSetting;
    private RelativeLayout mRlExcuteSetting;
    private RelativeLayout mRlHightMinus;
    private RelativeLayout mRlHightPlus;
    private RelativeLayout mRlLevelMinus;
    private RelativeLayout mRlLevelPlus;
    private X8SeekBarView mSbHeight;
    private X8SeekBarView mSbLevel;
    private TextView mTvHeight;
    private TextView mTvLevel;
    private X8TabHost mXthRotate;
    private X8TabHost mXthVidotape;
    private SlideChangeListener seekBarHightListener = new SlideChangeListener() {
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        public void onProgress(X8SeekBarView slideView, int progress) {
            X8AiGravitationExcuteComfirmUi.this.mTvHeight.setText(X8NumberUtil.getDistanceNumberString((float) (X8AiGravitationExcuteComfirmUi.this.HIGHT_MIN + (progress / 20)), 0, true));
        }

        public void onStop(X8SeekBarView slideView, int progress) {
        }
    };
    private SlideChangeListener seekBarLevelListener = new SlideChangeListener() {
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        public void onProgress(X8SeekBarView slideView, int progress) {
            X8AiGravitationExcuteComfirmUi.this.mTvLevel.setText(X8NumberUtil.getDistanceNumberString((float) (X8AiGravitationExcuteComfirmUi.this.LEVEL_MIN + (progress / 10)), 0, true));
        }

        public void onStop(X8SeekBarView slideView, int progress) {
        }
    };

    public X8AiGravitationExcuteComfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_gravitation_excute_confirm_layout, (ViewGroup) parent, true);
        initViews(this.contentView);
        initActions();
    }

    private void initActions() {
        if (this.contentView != null) {
            this.mExcuteReturn.setOnClickListener(this);
            this.mBtnExcuteOk.setOnClickListener(this);
            this.mBtnExcuteAdvancedSetting.setOnClickListener(this);
            this.mRlLevelMinus.setOnClickListener(this);
            this.mRlLevelPlus.setOnClickListener(this);
            this.mSbLevel.setOnSlideChangeListener(this.seekBarLevelListener);
            this.mRlHightMinus.setOnClickListener(this);
            this.mRlHightPlus.setOnClickListener(this);
            this.mSbHeight.setOnSlideChangeListener(this.seekBarHightListener);
            this.mBtnExcuteDefault.setOnClickListener(this);
            this.mBtnExcuteSave.setOnClickListener(this);
        }
    }

    private void initViews(View contentView) {
        this.mExcuteReturn = (ImageView) contentView.findViewById(R.id.img_ai_gravitation_excute_return);
        this.mExcuteTitle = (TextView) contentView.findViewById(R.id.tv_ai_gravitation_excute_title);
        this.mRlExcuteSetting = (RelativeLayout) contentView.findViewById(R.id.rl_gravitation_excute_setting);
        this.mXthVidotape = (X8TabHost) contentView.findViewById(R.id.x8_ai_gravitation_excute_videotape);
        this.mBtnExcuteAdvancedSetting = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_advanced_setting);
        this.mBtnExcuteOk = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_ok);
        this.mBtnExcuteDefault = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_default);
        this.mBtnExcuteSave = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_save);
        this.mLlAdvancedSetting = (LinearLayout) contentView.findViewById(R.id.ll_gravitation_excute_advanced_setting);
        this.mTvLevel = (TextView) contentView.findViewById(R.id.tv_ai_gravitation_excute_level);
        this.mRlLevelMinus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_level_minus);
        this.mSbLevel = (X8SeekBarView) contentView.findViewById(R.id.sb_ai_gravitation_excute_level);
        this.mRlLevelPlus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_level_plus);
        this.mTvHeight = (TextView) contentView.findViewById(R.id.tv_ai_gravitation_excute_height);
        this.mRlHightMinus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_heigth_minus);
        this.mSbHeight = (X8SeekBarView) contentView.findViewById(R.id.sb_ai_gravitation_excute_heigth);
        this.mRlHightPlus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_heigth_plus);
        this.mXthRotate = (X8TabHost) contentView.findViewById(R.id.x8_ai_gravitation_excute_rotate);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.img_ai_gravitation_excute_return) {
            returnExcuteSetting();
        } else if (id == R.id.btn_ai_gravitation_excute_ok) {
        } else {
            int l;
            int h;
            if (id == R.id.btn_ai_gravitation_excute_advanced_setting) {
                showAdvancedSetting();
            } else if (id == R.id.btn_ai_gravitation_excute_default) {
                recoverDefaultValue();
            } else if (id == R.id.btn_ai_gravitation_excute_save) {
                saveValue();
            } else if (id == R.id.rl_ai_gravitation_excute_level_minus) {
                if (this.mSbLevel.getProgress() != 0) {
                    l = this.mSbLevel.getProgress() - 10;
                    if (l < 0) {
                        l = 0;
                    }
                    this.mSbLevel.setProgress(l);
                }
            } else if (id == R.id.rl_ai_gravitation_excute_level_plus) {
                if (this.mSbLevel.getProgress() != this.LEVEL_MAX_PROGRESS) {
                    l = this.mSbLevel.getProgress() + 10;
                    if (l > this.LEVEL_MAX_PROGRESS) {
                        l = this.LEVEL_MAX_PROGRESS;
                    }
                    this.mSbLevel.setProgress(l);
                }
            } else if (id == R.id.rl_ai_gravitation_excute_heigth_minus) {
                if (this.mSbHeight.getProgress() != 0) {
                    h = this.mSbHeight.getProgress() - 20;
                    if (h < 0) {
                        h = 0;
                    }
                    this.mSbHeight.setProgress(h);
                }
            } else if (id == R.id.rl_ai_gravitation_excute_heigth_plus && this.mSbHeight.getProgress() != this.HIGHT_MAX_PROGRESS) {
                h = this.mSbHeight.getProgress() + 20;
                if (h > this.HIGHT_MAX_PROGRESS) {
                    h = this.HIGHT_MAX_PROGRESS;
                }
                this.mSbHeight.setProgress(h);
            }
        }
    }

    private void saveValue() {
        returnExcuteSetting();
        X8AiConfig.getInstance().setAiFlyGravitationHeight(this.mSbHeight.getProgress());
        X8AiConfig.getInstance().setAiFlyGravitationLevel(this.mSbLevel.getProgress());
        X8AiConfig.getInstance().setAiFlyGravitationRotate(this.mXthRotate.getSelectIndex());
    }

    private void recoverDefaultValue() {
        this.mSbLevel.setProgress(0);
        this.mTvLevel.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_gravitation_excute_advanced_setting_level));
        this.mSbHeight.setProgress(0);
        this.mTvHeight.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_gravitation_excute_advanced_setting_height));
        this.mXthRotate.setSelect(0);
        X8AiConfig.getInstance().setAiFlyGravitationHeight(0);
        X8AiConfig.getInstance().setAiFlyGravitationLevel(0);
        X8AiConfig.getInstance().setAiFlyGravitationRotate(0);
    }

    private void returnExcuteSetting() {
        this.mExcuteReturn.setVisibility(8);
        this.mRlExcuteSetting.setVisibility(0);
        this.mBtnExcuteDefault.setVisibility(8);
        this.mBtnExcuteSave.setVisibility(8);
        this.mBtnExcuteOk.setVisibility(0);
        this.mLlAdvancedSetting.setVisibility(8);
    }

    private void showAdvancedSetting() {
        this.mExcuteReturn.setVisibility(0);
        this.mRlExcuteSetting.setVisibility(8);
        this.mBtnExcuteDefault.setVisibility(0);
        this.mBtnExcuteSave.setVisibility(0);
        this.mBtnExcuteOk.setVisibility(8);
        this.mLlAdvancedSetting.setVisibility(0);
        this.mTvHeight.setText(X8NumberUtil.getDistanceNumberString((float) (this.HIGHT_MIN + (X8AiConfig.getInstance().getAiFlyGravitationHeight() / 20)), 0, true));
        this.mSbHeight.setProgress(X8AiConfig.getInstance().getAiFlyGravitationHeight());
        this.mTvLevel.setText(X8NumberUtil.getDistanceNumberString((float) (this.LEVEL_MIN + (X8AiConfig.getInstance().getAiFlyGravitationLevel() / 10)), 0, true));
        this.mSbLevel.setProgress(X8AiConfig.getInstance().getAiFlyGravitationLevel());
        this.mXthRotate.setSelect(X8AiConfig.getInstance().getAiFlyGravitationRotate());
    }
}
