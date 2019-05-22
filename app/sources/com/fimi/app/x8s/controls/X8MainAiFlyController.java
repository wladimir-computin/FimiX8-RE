package com.fimi.app.x8s.controls;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiAerialPhotographConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiAutoPhotoConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiFixedwingConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiFollowConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiGravitationConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiHeadingLockConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiLandingConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiLinesConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiPoint2PointConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiReturnConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiSarConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiScrewConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiSurroundToPointConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiTakeoffConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiTtipodConfirmModule;
import com.fimi.app.x8s.enums.X8AiFlyMenuEnum;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8AiFlyListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AutoFcHeart;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.X8CameraSettings;

public class X8MainAiFlyController extends AbsX8MenuBoxControllers implements OnClickListener {
    private X8sMainActivity activity;
    private View aiFlyBlank;
    private View confirmContent;
    private View contentView;
    FcManager fcManager;
    private ImageView imgAiAerialPhotograph;
    private ImageView imgAiAutoPhoto;
    private ImageView imgAiFixedwing;
    private ImageView imgAiFollow;
    private ImageView imgAiFollowToHostpot;
    private ImageView imgAiHeadingLock;
    private ImageView imgAiPointToPoint;
    private ImageView imgAiReturnHome;
    private ImageView imgAiRout;
    private ImageView imgAiSar;
    private ImageView imgAiSrcew;
    private ImageView imgAiSurroundToPoint;
    private ImageView imgAiTakeLandOff;
    private ImageView imgAiTtipod;
    private ImageView imgFlyGravitation;
    private IX8AiFlyListener listener;
    private FcCtrlManager mFcCtrlManager;
    private X8AiAerialPhotographConfirmModule mX8AiAerialPhotographConfirmModule;
    private X8AiAutoPhotoConfirmModule mX8AiAutoPhotoConfirmModule;
    private X8AiFixedwingConfirmModule mX8AiFixedwingConfirmModule;
    private X8AiFollowConfirmModule mX8AiFollowConfirmModule;
    private X8AiGravitationConfirmModule mX8AiGravitationConfirmModule;
    private X8AiHeadingLockConfirmModule mX8AiHeadingLockConfirmModule;
    private X8AiLandingConfirmModule mX8AiLandingConfirmModule;
    private X8AiLinesConfirmModule mX8AiLinesConfirmModule;
    private X8AiPoint2PointConfirmModule mX8AiPoint2PointConfirmModule;
    private X8AiReturnConfirmModule mX8AiReturnConfirmModule;
    private X8AiSarConfirmModule mX8AiSarConfirmModule;
    private X8AiScrewConfirmModule mX8AiScrewComnfirmModule;
    private X8AiSurroundToPointConfirmModule mX8AiSurroundToPointConfirmModule;
    private X8AiTakeoffConfirmModule mX8AiTakeoffConfirmModule;
    private X8AiTtipodConfirmModule mX8AiTtipodConfirmModule;
    private X8AiFlyMenuEnum menuState = X8AiFlyMenuEnum.ALL_ITEMS;
    private View rlAiFly;
    private View rlAiFlyItems;
    private ViewStub stubAiFlyAllItems;
    private ScrollView svAiItems;
    private TextView tvAerialShot;
    private TextView tvAiAutoPhoto;
    private TextView tvAiFollow;
    private TextView tvAiGravitation;
    private TextView tvAiPointToPoint;
    private TextView tvAiReturnHome;
    private TextView tvAiRout;
    private TextView tvAiSar;
    private TextView tvAiScrew;
    private TextView tvAiSurroundToPoint;
    private TextView tvAiTakeLandOff;
    private TextView tvFixedwing;
    private TextView tvHeadLock;
    private TextView tvTripod;

    public X8MainAiFlyController(View rootView) {
        super(rootView);
    }

    public void setX8AiFlyListener(X8sMainActivity activity, IX8AiFlyListener listener) {
        this.listener = listener;
        this.activity = activity;
    }

    public void initViews(View rootView) {
        this.rlAiFly = rootView.findViewById(R.id.x8_rl_main_ai_fly);
        this.aiFlyBlank = rootView.findViewById(R.id.x8_rl_main_ai_fly_blank);
        this.contentView = rootView.findViewById(R.id.rl_main_ai_fly_content);
        this.stubAiFlyAllItems = (ViewStub) rootView.findViewById(R.id.stub_ai_fly_items);
        this.confirmContent = rootView.findViewById(R.id.x8_main_ai_confirm_content);
        this.mX8AiPoint2PointConfirmModule = new X8AiPoint2PointConfirmModule();
        this.mX8AiSurroundToPointConfirmModule = new X8AiSurroundToPointConfirmModule();
        this.mX8AiLinesConfirmModule = new X8AiLinesConfirmModule();
        this.mX8AiAutoPhotoConfirmModule = new X8AiAutoPhotoConfirmModule();
        this.mX8AiFollowConfirmModule = new X8AiFollowConfirmModule();
        this.mX8AiTakeoffConfirmModule = new X8AiTakeoffConfirmModule();
        this.mX8AiLandingConfirmModule = new X8AiLandingConfirmModule();
        this.mX8AiReturnConfirmModule = new X8AiReturnConfirmModule();
        this.mX8AiAerialPhotographConfirmModule = new X8AiAerialPhotographConfirmModule();
        this.mX8AiFixedwingConfirmModule = new X8AiFixedwingConfirmModule();
        this.mX8AiHeadingLockConfirmModule = new X8AiHeadingLockConfirmModule();
        this.mX8AiTtipodConfirmModule = new X8AiTtipodConfirmModule();
        this.mX8AiScrewComnfirmModule = new X8AiScrewConfirmModule();
        this.mX8AiSarConfirmModule = new X8AiSarConfirmModule();
        this.mX8AiGravitationConfirmModule = new X8AiGravitationConfirmModule();
    }

    public void initActions() {
        this.aiFlyBlank.setOnClickListener(this);
    }

    public void defaultVal() {
        if (this.rlAiFlyItems != null && this.isShow) {
            setAllEnabled();
        }
    }

    public void setAllEnabled() {
        this.imgAiTakeLandOff.setEnabled(false);
        this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_takeoff_selector);
        this.imgAiReturnHome.setEnabled(false);
        this.imgAiPointToPoint.setEnabled(false);
        this.imgAiRout.setEnabled(false);
        this.imgAiAutoPhoto.setEnabled(false);
        this.imgAiFollow.setEnabled(false);
        this.imgAiFollowToHostpot.setEnabled(false);
        this.imgAiSurroundToPoint.setEnabled(false);
        this.imgFlyGravitation.setEnabled(false);
        this.imgAiTtipod.setEnabled(false);
        this.imgAiAerialPhotograph.setEnabled(false);
        this.imgAiFixedwing.setEnabled(false);
        this.imgAiHeadingLock.setEnabled(false);
        this.imgAiSrcew.setEnabled(false);
        this.imgAiSar.setEnabled(false);
        this.tvAiTakeLandOff.setEnabled(false);
        this.tvAiReturnHome.setEnabled(false);
        this.tvAiRout.setEnabled(false);
        this.tvAiFollow.setEnabled(false);
        this.tvAiSurroundToPoint.setEnabled(false);
        this.tvAiPointToPoint.setEnabled(false);
        this.tvAiAutoPhoto.setEnabled(false);
        this.tvAiScrew.setEnabled(false);
        this.tvAiGravitation.setEnabled(false);
        this.tvAiSar.setEnabled(false);
        this.tvAerialShot.setEnabled(false);
        this.tvTripod.setEnabled(false);
        this.tvHeadLock.setEnabled(false);
        this.tvFixedwing.setEnabled(false);
        defalutNextUi(false);
    }

    public void defalutNextUi(boolean b) {
        switch (this.menuState) {
            case AI_LINE_CONFIRM:
                if (!b) {
                    onCloseConfirmUi();
                    return;
                } else if (this.mX8AiLinesConfirmModule != null) {
                    this.mX8AiLinesConfirmModule.setFcHeart(false, false);
                    return;
                } else {
                    return;
                }
            case AI_LANDING:
                onCloseConfirmUi();
                return;
            case AI_RETURN:
                onCloseConfirmUi();
                return;
            case AI_FOLLOW_CONFIRM:
                onCloseConfirmUi();
                return;
            case AI_POINT2POINT_CONFIRM:
                onCloseConfirmUi();
                return;
            case AI_SURROUNDPOINT_CONFIRM:
                onCloseConfirmUi();
                return;
            case AI_AUTO_PHOTO_CONFIRM:
                onCloseConfirmUi();
                return;
            case AI_FLY_GRAVITATION:
                onCloseConfirmUi();
                return;
            default:
                return;
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_take_land_off) {
            openTakeOffOrLandingUi();
        } else if (id == R.id.img_ai_return_home) {
            openReturnUi();
        } else if (id == R.id.img_ai_point_to_point) {
            openPoint2PointConfirmContent();
        } else if (id == R.id.img_ai_rout) {
            openLinesUi();
        } else if (id == R.id.img_ai_auto_photo) {
            openAutoPhotoUi();
        } else if (id == R.id.img_ai_follow) {
            openAiFollowConfirmContent();
        } else if (id == R.id.img_ai_follow_to_hostpot) {
        } else {
            if (id == R.id.img_ai_surround_to_point) {
                openSurroundPointUi();
            } else if (id == R.id.x8_rl_main_ai_fly_blank) {
                closeAiUi(true, true);
            } else if (id == R.id.img_ai_tripod) {
                openAiTtipod();
            } else if (id == R.id.img_ai_aerial_photograph) {
                openAiAerialPhotograph();
            } else if (id == R.id.img_ai_fixed_wing) {
                openAiFixedwing();
            } else if (id == R.id.img_ai_heading_lock) {
                openAiHeadingLock();
            } else if (id == R.id.img_ai_screw) {
                openAiScrew();
            } else if (id == R.id.img_ai_sar) {
                openAiSar();
            } else if (id == R.id.img_ai_fly_gravitation) {
                openGravitationUi();
            }
        }
    }

    public void setFcManager(FcManager fcManager, FcCtrlManager mFcCtrlManager) {
        this.fcManager = fcManager;
        this.mFcCtrlManager = mFcCtrlManager;
    }

    public void initAllItems() {
        if (this.menuState == X8AiFlyMenuEnum.ALL_ITEMS) {
            if (this.rlAiFlyItems == null) {
                View view = this.stubAiFlyAllItems.inflate();
                this.rlAiFlyItems = view.findViewById(R.id.x8_rl_main_ai_fly_items);
                this.imgAiTakeLandOff = (ImageView) view.findViewById(R.id.img_ai_take_land_off);
                this.svAiItems = (ScrollView) view.findViewById(R.id.sv_ai_items);
                this.imgAiReturnHome = (ImageView) view.findViewById(R.id.img_ai_return_home);
                this.imgAiPointToPoint = (ImageView) view.findViewById(R.id.img_ai_point_to_point);
                this.imgAiRout = (ImageView) view.findViewById(R.id.img_ai_rout);
                this.imgAiAutoPhoto = (ImageView) view.findViewById(R.id.img_ai_auto_photo);
                this.imgAiFollow = (ImageView) view.findViewById(R.id.img_ai_follow);
                this.imgAiFollowToHostpot = (ImageView) view.findViewById(R.id.img_ai_follow_to_hostpot);
                this.imgAiSurroundToPoint = (ImageView) view.findViewById(R.id.img_ai_surround_to_point);
                this.imgAiTtipod = (ImageView) view.findViewById(R.id.img_ai_tripod);
                this.imgAiAerialPhotograph = (ImageView) view.findViewById(R.id.img_ai_aerial_photograph);
                this.imgAiFixedwing = (ImageView) view.findViewById(R.id.img_ai_fixed_wing);
                this.imgAiHeadingLock = (ImageView) view.findViewById(R.id.img_ai_heading_lock);
                this.imgAiSrcew = (ImageView) view.findViewById(R.id.img_ai_screw);
                this.imgFlyGravitation = (ImageView) view.findViewById(R.id.img_ai_fly_gravitation);
                this.imgAiSar = (ImageView) view.findViewById(R.id.img_ai_sar);
                this.tvAiTakeLandOff = (TextView) view.findViewById(R.id.tv_ai_take_land_off);
                this.tvAiReturnHome = (TextView) view.findViewById(R.id.tv_ai_return_home);
                this.tvAiRout = (TextView) view.findViewById(R.id.tv_ai_rout);
                this.tvAiFollow = (TextView) view.findViewById(R.id.tv_ai_follow);
                this.tvAiSurroundToPoint = (TextView) view.findViewById(R.id.tv_ai_surround_to_point);
                this.tvAiPointToPoint = (TextView) view.findViewById(R.id.tv_ai_point_to_point);
                this.tvAiAutoPhoto = (TextView) view.findViewById(R.id.tv_ai_auto_photo);
                this.tvAiScrew = (TextView) view.findViewById(R.id.tv_ai_screw);
                this.tvAiGravitation = (TextView) view.findViewById(R.id.tv_ai_fly_gravitation);
                this.tvAiSar = (TextView) view.findViewById(R.id.tv_ai_sar);
                this.tvAerialShot = (TextView) view.findViewById(R.id.tv_ai_aerial_photograph);
                this.tvTripod = (TextView) view.findViewById(R.id.tv_ai_tripod);
                this.tvHeadLock = (TextView) view.findViewById(R.id.tv_ai_heading_lock);
                this.tvFixedwing = (TextView) view.findViewById(R.id.tv_ai_fixed_wing);
                this.imgAiTakeLandOff.setOnClickListener(this);
                this.imgAiReturnHome.setOnClickListener(this);
                this.imgAiPointToPoint.setOnClickListener(this);
                this.imgAiAutoPhoto.setOnClickListener(this);
                this.imgAiRout.setOnClickListener(this);
                this.imgAiFollow.setOnClickListener(this);
                this.imgAiFollowToHostpot.setOnClickListener(this);
                this.imgAiSurroundToPoint.setOnClickListener(this);
                this.imgFlyGravitation.setOnClickListener(this);
                this.imgAiTtipod.setOnClickListener(this);
                this.imgAiAerialPhotograph.setOnClickListener(this);
                this.imgAiFixedwing.setOnClickListener(this);
                this.imgAiHeadingLock.setOnClickListener(this);
                this.imgAiSrcew.setOnClickListener(this);
                this.imgAiSar.setOnClickListener(this);
            }
            this.rlAiFlyItems.setVisibility(0);
        }
        if (this.isConect) {
            onFcHeart(null, this.isLowpower);
        } else {
            setAllEnabled();
        }
    }

    public void closeAiUi(boolean isShowOther, boolean isShowRightIcon) {
        this.menuState = X8AiFlyMenuEnum.ALL_ITEMS;
        this.aiFlyBlank.setVisibility(8);
        if (this.isShow) {
            this.isShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.contentView, "translationX", new float[]{0.0f, (float) this.width});
            translationRight.setDuration(300);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8MainAiFlyController.this.rlAiFly.setVisibility(4);
                    X8MainAiFlyController.this.svAiItems.fullScroll(33);
                    X8MainAiFlyController.this.onCloseConfirmUi();
                }
            });
        }
        this.listener.onCloseAiUi(isShowOther, isShowRightIcon);
    }

    public void getCameraSetting() {
        X8CameraSettings.getSettings(this.activity.getCameraManager());
    }

    public void showAiUi() {
        getCameraSetting();
        getDroneState();
        this.rlAiFly.setVisibility(0);
        this.aiFlyBlank.setVisibility(0);
        initAllItems();
        if (!this.isShow) {
            Log.i("zdy", "showAiUi...........");
            this.isShow = true;
            if (this.width == 0) {
                this.contentView.setAlpha(0.0f);
                this.contentView.post(new Runnable() {
                    public void run() {
                        X8MainAiFlyController.this.contentView.setAlpha(1.0f);
                        X8MainAiFlyController.this.MAX_WIDTH = X8MainAiFlyController.this.rlAiFly.getWidth();
                        X8MainAiFlyController.this.width = X8MainAiFlyController.this.contentView.getWidth();
                        int h = X8MainAiFlyController.this.contentView.getHeight();
                        ObjectAnimator animatorY = ObjectAnimator.ofFloat(X8MainAiFlyController.this.contentView, "translationX", new float[]{(float) X8MainAiFlyController.this.width, 0.0f});
                        animatorY.setDuration(300);
                        animatorY.start();
                    }
                });
                return;
            }
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.contentView, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.start();
        }
    }

    public void openAiFollowConfirmContent() {
        this.rlAiFlyItems.setVisibility(4);
        this.confirmContent.setVisibility(0);
        this.mX8AiFollowConfirmModule.init(this.activity, this.confirmContent);
        this.mX8AiFollowConfirmModule.setListener(this);
        this.menuState = X8AiFlyMenuEnum.AI_FOLLOW_CONFIRM;
    }

    public void openPoint2PointConfirmContent() {
        if (X8AiConfig.getInstance().isAiP2PCourse()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiPoint2PointConfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiPoint2PointConfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_POINT2POINT_CONFIRM;
            return;
        }
        onPoint2PointConfirmOkClick();
    }

    public void openAiHeadingLock() {
        this.rlAiFlyItems.setVisibility(4);
        this.confirmContent.setVisibility(0);
        this.mX8AiHeadingLockConfirmModule.init(this.activity, this.confirmContent);
        this.mX8AiHeadingLockConfirmModule.setListener(this);
        this.menuState = X8AiFlyMenuEnum.AI_HEADINGLOCK;
    }

    public void onHeadingLockConfirmOkClick() {
        setHeadlockType();
    }

    public void openAiFixedwing() {
        if (X8AiConfig.getInstance().isAiFixedwingCourse()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiFixedwingConfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiFixedwingConfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_FIXEDWING;
            return;
        }
        onFixedwingConfirmOkClick();
    }

    public void onFixedwingConfirmOkClick() {
        setFixedwingType();
    }

    public void openAiTtipod() {
        if (X8AiConfig.getInstance().isAiTripodCourse()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiTtipodConfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiTtipodConfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_TTIPOD;
            return;
        }
        onTtipodConfirmOkClick();
    }

    public void onTtipodConfirmOkClick() {
        setTripodType();
    }

    public void openAiScrew() {
        if (X8AiConfig.getInstance().isAiScrew()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiScrewComnfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiScrewComnfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_SCREW;
            return;
        }
        onScrewConfirmOkClick();
    }

    public void onScrewConfirmOkClick() {
        this.listener.onAiScrewConfimCick();
        closeAiUi(false, false);
    }

    public void openAiSar() {
        if (X8AiConfig.getInstance().isAiSar()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiSarConfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiSarConfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_SAR;
            return;
        }
        onSarConfirmOkClick();
    }

    public void onSarConfirmOkClick() {
        this.listener.onAiSarConfimClick();
        closeAiUi(true, false);
    }

    public void openAiAerialPhotograph() {
        if (X8AiConfig.getInstance().isAiAerialPhotographCourse()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiAerialPhotographConfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiAerialPhotographConfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_AERIALPHOTOGRAPH;
            return;
        }
        onAerialPhotographConfirmOkClick();
    }

    public void onAerialPhotographConfirmOkClick() {
        setAerailShotType();
    }

    public void openTakeOffOrLandingUi() {
        if (StateManager.getInstance().getX8Drone().isInSky()) {
            openLandingUi();
        } else {
            openTakeoffUi();
        }
    }

    public void onFcHeart(AutoFcHeart fcHeart, boolean isLowPow) {
        boolean takeoffLanding;
        int ctrtype;
        DroneState state = StateManager.getInstance().getX8Drone();
        if (state.isInSky() && this.rlAiFlyItems != null) {
            this.tvAiTakeLandOff.setText(R.string.x8_ai_fly_land_off);
            this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_landing);
            this.imgAiReturnHome.setEnabled(true);
            this.tvAiReturnHome.setEnabled(true);
            boolean b = false;
            takeoffLanding = false;
            if (StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
                b = false;
                takeoffLanding = true;
            } else {
                ctrtype = StateManager.getInstance().getX8Drone().getCtrlType();
                if (ctrtype == 1) {
                    b = false;
                    takeoffLanding = false;
                } else if (ctrtype == 3) {
                    b = false;
                    takeoffLanding = true;
                } else if (ctrtype == 2) {
                    b = true;
                    takeoffLanding = true;
                }
            }
            this.imgAiTakeLandOff.setEnabled(takeoffLanding);
            this.imgAiPointToPoint.setEnabled(b);
            this.imgAiRout.setEnabled(b);
            this.imgAiAutoPhoto.setEnabled(b);
            this.imgAiFollow.setEnabled(b);
            this.imgAiFollowToHostpot.setEnabled(b);
            this.imgAiSurroundToPoint.setEnabled(b);
            this.imgFlyGravitation.setEnabled(b);
            this.imgAiTtipod.setEnabled(b);
            this.imgAiAerialPhotograph.setEnabled(b);
            this.imgAiFixedwing.setEnabled(b);
            this.imgAiHeadingLock.setEnabled(b);
            this.imgAiSrcew.setEnabled(b);
            this.imgAiSar.setEnabled(b);
            this.tvAiTakeLandOff.setEnabled(takeoffLanding);
            this.tvAiRout.setEnabled(b);
            this.tvAiFollow.setEnabled(b);
            this.tvAiSurroundToPoint.setEnabled(b);
            this.tvAiPointToPoint.setEnabled(b);
            this.tvAiAutoPhoto.setEnabled(b);
            this.tvAiScrew.setEnabled(b);
            this.tvAiGravitation.setEnabled(b);
            this.tvAiSar.setEnabled(b);
            this.tvAerialShot.setEnabled(b);
            this.tvTripod.setEnabled(b);
            this.tvHeadLock.setEnabled(b);
            this.tvFixedwing.setEnabled(b);
            onFcHeartChange(b, isLowPow);
        }
        if (state.isOnGround() && this.rlAiFlyItems != null) {
            if (state.isCanFly()) {
                this.tvAiTakeLandOff.setText(R.string.x8_ai_fly_take_off);
                this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_takeoff_selector);
                ctrtype = StateManager.getInstance().getX8Drone().getCtrlType();
                takeoffLanding = false;
                if (ctrtype == 1) {
                    takeoffLanding = false;
                } else if (ctrtype == 3) {
                    takeoffLanding = true;
                } else if (ctrtype == 2) {
                    takeoffLanding = true;
                }
                this.imgAiTakeLandOff.setEnabled(takeoffLanding);
                this.tvAiTakeLandOff.setEnabled(takeoffLanding);
                defalutNextUi(true);
            } else {
                this.tvAiTakeLandOff.setEnabled(false);
                this.imgAiTakeLandOff.setEnabled(false);
            }
            onFcHeartChange(false, isLowPow);
            this.imgAiReturnHome.setEnabled(false);
            this.tvAiReturnHome.setEnabled(false);
            takeoffLanding = false;
            ctrtype = StateManager.getInstance().getX8Drone().getCtrlType();
            if (ctrtype == 1) {
                takeoffLanding = false;
            } else if (ctrtype == 3) {
                takeoffLanding = false;
            } else if (ctrtype == 2) {
                takeoffLanding = true;
            }
            this.imgAiRout.setEnabled(takeoffLanding);
            this.imgAiPointToPoint.setEnabled(false);
            this.imgAiAutoPhoto.setEnabled(false);
            this.imgAiFollow.setEnabled(false);
            this.imgAiFollowToHostpot.setEnabled(false);
            this.imgAiSurroundToPoint.setEnabled(false);
            this.imgFlyGravitation.setEnabled(false);
            this.imgAiTtipod.setEnabled(false);
            this.imgAiAerialPhotograph.setEnabled(false);
            this.imgAiFixedwing.setEnabled(false);
            this.imgAiHeadingLock.setEnabled(false);
            this.imgAiSrcew.setEnabled(false);
            this.imgAiSar.setEnabled(false);
            this.tvAiRout.setEnabled(takeoffLanding);
            this.tvAiFollow.setEnabled(false);
            this.tvAiSurroundToPoint.setEnabled(false);
            this.tvAiPointToPoint.setEnabled(false);
            this.tvAiAutoPhoto.setEnabled(false);
            this.tvAiScrew.setEnabled(false);
            this.tvAiGravitation.setEnabled(false);
            this.tvAiSar.setEnabled(false);
            this.tvAerialShot.setEnabled(false);
            this.tvTripod.setEnabled(false);
            this.tvHeadLock.setEnabled(false);
            this.tvFixedwing.setEnabled(false);
        }
    }

    public void onFcHeartChange(boolean isInSky, boolean isLowPow) {
        switch (this.menuState) {
            case AI_LINE_CONFIRM:
                this.mX8AiLinesConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_TAKE_OFF:
                this.mX8AiTakeoffConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_LANDING:
                this.mX8AiLandingConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_RETURN:
                this.mX8AiReturnConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_FOLLOW_CONFIRM:
                this.mX8AiFollowConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_POINT2POINT_CONFIRM:
                this.mX8AiPoint2PointConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_SURROUNDPOINT_CONFIRM:
                this.mX8AiSurroundToPointConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_AUTO_PHOTO_CONFIRM:
                this.mX8AiAutoPhotoConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_FLY_GRAVITATION:
                this.mX8AiGravitationConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            case AI_FIXEDWING:
                this.mX8AiFixedwingConfirmModule.setFcHeart(isInSky, isLowPow);
                return;
            default:
                return;
        }
    }

    public void onTakeOffOrLandingClick() {
        if (this.fcManager == null) {
            return;
        }
        if (StateManager.getInstance().getX8Drone().isInSky()) {
            this.fcManager.land(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        X8MainAiFlyController.this.closeAiUi(true, false);
                    }
                }
            });
        } else {
            this.fcManager.takeOff(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        X8MainAiFlyController.this.closeAiUi(true, false);
                    }
                }
            });
        }
    }

    public void onFollowConfirmOkClick(int type) {
        setAiVcOpen(type);
    }

    public void setAiVcOpen(final int type) {
        this.fcManager.setAiVcOpen(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainAiFlyController.this.listener.onAiFollowConfirmClick(type);
                    X8MainAiFlyController.this.closeAiUi(false, false);
                }
            }
        });
    }

    public void onPoint2PointConfirmOkClick() {
        this.listener.onAiPoint2PointConfirmClick();
        closeAiUi(false, false);
    }

    public void openSurroundPointUi() {
        if (X8AiConfig.getInstance().isAiSurroundCourse()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiSurroundToPointConfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiSurroundToPointConfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_SURROUNDPOINT_CONFIRM;
            return;
        }
        onSurroundPointConfirmClick();
    }

    public void openGravitationUi() {
        if (X8AiConfig.getInstance().isAiFlyGravitation()) {
            this.rlAiFlyItems.setVisibility(4);
            this.confirmContent.setVisibility(0);
            this.mX8AiGravitationConfirmModule.init(this.activity, this.confirmContent);
            this.mX8AiGravitationConfirmModule.setListener(this);
            this.menuState = X8AiFlyMenuEnum.AI_FLY_GRAVITATION;
            return;
        }
        onGravitationConfirmOkClick();
    }

    public void onGravitationConfirmOkClick() {
        this.listener.onAiGravitationConfimClick();
        closeAiUi(false, false);
    }

    public void openLinesUi() {
        this.rlAiFlyItems.setVisibility(4);
        this.confirmContent.setVisibility(0);
        this.mX8AiLinesConfirmModule.init(this.activity, this.confirmContent);
        this.mX8AiLinesConfirmModule.setListener(this);
        this.menuState = X8AiFlyMenuEnum.AI_LINE_CONFIRM;
    }

    public void openAutoPhotoUi() {
        this.rlAiFlyItems.setVisibility(4);
        this.confirmContent.setVisibility(0);
        this.mX8AiAutoPhotoConfirmModule.init(this.activity, this.confirmContent);
        this.mX8AiAutoPhotoConfirmModule.setListener(this);
        this.menuState = X8AiFlyMenuEnum.AI_AUTO_PHOTO_CONFIRM;
    }

    public void openTakeoffUi() {
        this.rlAiFlyItems.setVisibility(4);
        this.confirmContent.setVisibility(0);
        this.mX8AiTakeoffConfirmModule.init(this.activity, this.confirmContent);
        this.mX8AiTakeoffConfirmModule.setListener(this);
        this.menuState = X8AiFlyMenuEnum.AI_TAKE_OFF;
    }

    public void openLandingUi() {
        this.rlAiFlyItems.setVisibility(4);
        this.confirmContent.setVisibility(0);
        this.mX8AiLandingConfirmModule.init(this.activity, this.confirmContent);
        this.mX8AiLandingConfirmModule.setListener(this);
        this.menuState = X8AiFlyMenuEnum.AI_LANDING;
    }

    public void openReturnUi() {
        this.rlAiFlyItems.setVisibility(4);
        this.confirmContent.setVisibility(0);
        this.mX8AiReturnConfirmModule.init(this.activity, this.confirmContent);
        this.mX8AiReturnConfirmModule.setListener(this, this.mFcCtrlManager);
        this.menuState = X8AiFlyMenuEnum.AI_RETURN;
    }

    public void onCloseConfirmUi() {
        this.confirmContent.setVisibility(8);
        ((ViewGroup) this.confirmContent).removeAllViews();
        this.rlAiFlyItems.setVisibility(0);
        this.menuState = X8AiFlyMenuEnum.ALL_ITEMS;
    }

    public void onSurroundPointConfirmClick() {
        this.listener.onAiSurroundPointConfirmClick();
        closeAiUi(false, false);
    }

    public void onAutoPhotoConfirmOk(int type) {
        setAiVcOpenForAutoPhoto(type);
    }

    public void setAiVcOpenForAutoPhoto(final int type) {
        this.fcManager.setAiVcOpen(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainAiFlyController.this.listener.onAiAutoPhotoConfirmClick(type);
                    X8MainAiFlyController.this.closeAiUi(false, false);
                }
            }
        });
    }

    public void onLinesConfirmOkClick(int mode) {
        this.listener.onAiLineConfirmClick(mode);
        closeAiUi(false, false);
    }

    public void onLinesConfirmOkByHistory(long lineId, int type) {
        this.listener.onAiLineConfirmClickByHistory(2, lineId, type);
        closeAiUi(false, false);
    }

    public void onRetureHomeClick() {
        this.fcManager.setAiRetureHome(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainAiFlyController.this.closeAiUi(true, false);
                }
            }
        });
    }

    public void showSportState(AutoFcSportState state) {
        if (this.menuState == X8AiFlyMenuEnum.AI_RETURN) {
            this.mX8AiReturnConfirmModule.showSportState(state);
        } else if (this.menuState == X8AiFlyMenuEnum.AI_HEADINGLOCK) {
            this.mX8AiHeadingLockConfirmModule.showSportState(state);
        } else if (this.menuState == X8AiFlyMenuEnum.AI_FIXEDWING) {
            this.mX8AiFixedwingConfirmModule.showSportState(state);
        }
    }

    public void setFixedwingType() {
        this.mFcCtrlManager.setEnableFixwing(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainAiFlyController.this.closeAiUi(true, false);
                }
            }
        });
    }

    public void setHeadlockType() {
        this.mFcCtrlManager.setEnableHeadingFree(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainAiFlyController.this.closeAiUi(true, false);
                }
            }
        });
    }

    public void setTripodType() {
        this.mFcCtrlManager.setEnableTripod(1, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainAiFlyController.this.closeAiUi(true, false);
                }
            }
        });
    }

    public void setAerailShotType() {
        this.mFcCtrlManager.setEnableAerailShot(1, new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainAiFlyController.this.closeAiUi(true, false);
                }
            }
        });
    }
}
