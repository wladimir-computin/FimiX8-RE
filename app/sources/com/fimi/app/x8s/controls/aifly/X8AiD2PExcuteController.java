package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.X8AiTrackController.OnAiTrackControllerListener;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiPoint2PointExcuteConfirmModule;
import com.fimi.app.x8s.enums.X8AiMapItem;
import com.fimi.app.x8s.enums.X8AiPointState;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiItemMapListener;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.interfaces.IX8Point2PointExcuteConttrollerListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8MainPitchingAngle;
import com.fimi.app.x8s.widget.X8MainPitchingAngle.OnProgressListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.dataparser.AckGetAiPoint;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8AiD2PExcuteController extends AbsX8AiController implements OnClickListener, OnProgressListener, IX8MarkerListener, onDialogButtonClickListener, OnAiTrackControllerListener {
    protected int MAX_WIDTH;
    private X8sMainActivity activity;
    private View blank;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgBack;
    private ImageView imgNext;
    private ImageView imgVcToggle;
    private boolean isDraw;
    protected boolean isNextShow;
    protected boolean isShow;
    private IX8Point2PointExcuteConttrollerListener listener;
    private AiGetPointState mAiGetPointState = AiGetPointState.IDLE;
    private IX8NextViewListener mIX8NextViewListener = new IX8NextViewListener() {
        public void onBackClick() {
            X8AiD2PExcuteController.this.closeNextUi(true);
        }

        public void onExcuteClick() {
            X8AiD2PExcuteController.this.state = X8AiPointState.RUNNING;
            X8AiD2PExcuteController.this.isDraw = true;
            X8AiD2PExcuteController.this.openVcToggle();
            X8AiD2PExcuteController.this.mTipBgView.setVisibility(8);
            X8AiD2PExcuteController.this.closeNextUi(false);
            X8AiD2PExcuteController.this.mapVideoController.getFimiMap().getAiPoint2PointManager().resetMapEvent();
            X8AiD2PExcuteController.this.mapVideoController.getFimiMap().getAiPoint2PointManager().setRunning();
        }

        public void onSaveClick() {
        }
    };
    private X8AiTipWithCloseView mTipBgView;
    private X8AiPoint2PointExcuteConfirmModule mX8AiFollowPoint2PointExcuteConfirmModule;
    private X8MapVideoController mapVideoController;
    private View nextRootView;
    private X8AiPointState state = X8AiPointState.IDLE;
    private ViewStub stubPoint2Point;
    private int timeSend = 0;
    private TextView tvP2PTip;
    private X8MainPitchingAngle vHeight;
    protected int width = X8Application.ANIMATION_WIDTH;

    private enum AiGetPointState {
        IDLE,
        GET_POINT,
        END
    }

    public void onMarkerSelect(boolean onSelect, float altitude, MapPointLatLng mpl, boolean isClick) {
        if (onSelect) {
            this.vHeight.setVisibility(0);
            this.vHeight.setProcess(altitude);
            return;
        }
        this.vHeight.setVisibility(8);
    }

    public void onMarkerSizeChange(int size) {
    }

    public void onInterestSizeEnable(boolean b) {
    }

    public Rect getDeletePosition() {
        return null;
    }

    public void onRunIndex(int index, int action) {
    }

    public int getOration() {
        return 0;
    }

    public void onLeft() {
    }

    public void onRight() {
        if (this.state == X8AiPointState.RUNNING || this.state == X8AiPointState.RUNNING2) {
            p2pTaskExite();
        }
    }

    public X8AiD2PExcuteController(X8sMainActivity activity, View rootView, X8AiPointState state) {
        super(rootView);
        this.activity = activity;
        this.state = state;
    }

    public void setListener(IX8Point2PointExcuteConttrollerListener listener) {
        this.listener = listener;
    }

    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.state == X8AiPointState.RUNNING || this.state == X8AiPointState.RUNNING2) {
                showExitDialog();
                return;
            }
            closeUi();
            if (this.listener != null) {
                this.listener.onPoint2PointBackClick();
            }
        } else if (id == R.id.img_ai_follow_next) {
            if (this.mapVideoController.getFimiMap().getAiPoint2PointManager().getMapPointLatLng() != null && this.listener != null) {
                openNextUi();
                this.mapVideoController.getFimiMap().getAiPoint2PointManager().getMapPointLatLng().altitude = this.vHeight.getRegulationProgress();
                this.mapVideoController.getFimiMap().getAiPoint2PointManager().calcDistance();
                setMapPoint(this.mapVideoController.getFimiMap().getAiPoint2PointManager().getMapPointLatLng());
            }
        } else if (id == R.id.x8_main_ai_point2point_next_blank) {
            closeNextUi(true);
        } else if (id == R.id.img_vc_targgle) {
            if (this.imgVcToggle.isSelected()) {
                setAiVcClose();
            } else {
                setAiVcOpen();
            }
        } else if (id != R.id.rl_flag_small) {
        } else {
            if (this.tvP2PTip.getVisibility() == 0) {
                this.tvP2PTip.setVisibility(8);
            } else {
                this.tvP2PTip.setVisibility(0);
            }
        }
    }

    public void openUi() {
        this.isShow = true;
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_follow_point2point_layout, (ViewGroup) this.rootView, true);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.imgNext = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_next);
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_back);
        this.tvP2PTip = (TextView) this.handleView.findViewById(R.id.img_ai_p2p_tip);
        this.vHeight = (X8MainPitchingAngle) this.handleView.findViewById(R.id.x8_pitching_angle);
        this.mTipBgView = (X8AiTipWithCloseView) this.handleView.findViewById(R.id.img_ai_follow_tip);
        this.imgVcToggle = (ImageView) this.handleView.findViewById(R.id.img_vc_targgle);
        this.imgVcToggle.setOnClickListener(this);
        if (this.state == X8AiPointState.IDLE) {
            this.imgNext.setVisibility(0);
            this.imgVcToggle.setVisibility(8);
            this.mTipBgView.setTipText(getString(R.string.x8_ai_fly_follow_select_point));
            this.mTipBgView.setVisibility(0);
            this.mapVideoController.getFimiMap().getAiPoint2PointManager().setPoint2PointMarkerSelectListener(this);
            this.mapVideoController.getFimiMap().getAiPoint2PointManager().setOnMapClickListener();
        } else {
            this.imgNext.setVisibility(8);
            openVcToggle();
            this.mTipBgView.setVisibility(8);
            this.vHeight.setVisibility(8);
        }
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_point2point_next_content);
        this.blank = this.rootView.findViewById(R.id.x8_main_ai_point2point_next_blank);
        this.mX8AiFollowPoint2PointExcuteConfirmModule = new X8AiPoint2PointExcuteConfirmModule();
        this.vHeight.setOnProgressListener(this);
        this.mapVideoController.getFimiMap().setmX8AiItemMapListener(new IX8AiItemMapListener() {
            public X8AiMapItem getCurrentItem() {
                return X8AiMapItem.AI_POINT_TO_POINT;
            }
        });
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        this.imgNext.setOnClickListener(this);
        this.imgBack.setOnClickListener(this);
        this.blank.setOnClickListener(this);
        this.flagSmall.setOnClickListener(this);
        super.openUi();
    }

    public void closeUi() {
        this.isShow = false;
        this.activity.getmX8AiTrackController().closeUi();
        this.state = X8AiPointState.IDLE;
        this.mapVideoController.getFimiMap().setmX8AiItemMapListener(null);
        this.mapVideoController.getFimiMap().getAiPoint2PointManager().clearPoint2PointMarker();
        this.mapVideoController.getFimiMap().getAiPoint2PointManager().resetMapEvent();
        this.imgVcToggle.setVisibility(8);
        setAiVcClose();
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        closeIconByNextUi();
        this.mX8AiFollowPoint2PointExcuteConfirmModule.init(this.activity, this.nextRootView);
        if (this.mX8AiFollowPoint2PointExcuteConfirmModule != null) {
            this.mX8AiFollowPoint2PointExcuteConfirmModule.setListener(this.mIX8NextViewListener, this.activity.getFcManager());
        }
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.start();
        }
    }

    public void onProgressChanage(float progress) {
        this.mapVideoController.getFimiMap().getAiPoint2PointManager().setMarkerViewInfo(progress);
    }

    public void closeNextUi(final boolean b) {
        this.blank.setVisibility(8);
        if (this.isNextShow) {
            this.isNextShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{0.0f, (float) this.width});
            translationRight.setDuration(300);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8AiD2PExcuteController.this.nextRootView.setVisibility(8);
                    ((ViewGroup) X8AiD2PExcuteController.this.nextRootView).removeAllViews();
                    X8AiD2PExcuteController.this.imgBack.setVisibility(0);
                    X8AiD2PExcuteController.this.flagSmall.setVisibility(0);
                    if (b) {
                        X8AiD2PExcuteController.this.imgNext.setVisibility(0);
                        X8AiD2PExcuteController.this.vHeight.setVisibility(0);
                    }
                }
            });
        }
    }

    public void setMapPoint(MapPointLatLng mapPoint) {
        this.mX8AiFollowPoint2PointExcuteConfirmModule.setMapPoint(mapPoint);
    }

    public void setMapVideoController(X8MapVideoController mapVideoController) {
        this.mapVideoController = mapVideoController;
    }

    public void showAiPointView() {
        if (this.state == X8AiPointState.RUNNING) {
            this.state = X8AiPointState.RUNNING2;
            if (this.listener != null) {
                this.listener.onPoint2PointRunning();
            }
        }
        if (!this.isDraw) {
            getRunningPoint();
        }
    }

    public void cancleByModeChange(int taskMode) {
        boolean z = true;
        if (taskMode != 1) {
            z = false;
        }
        onTaskComplete(z);
    }

    public void getPoint() {
        this.activity.getFcManager().getAiFollowPoint(new UiCallBackListener<AckGetAiPoint>() {
            public void onComplete(CmdResult cmdResult, AckGetAiPoint ackGetAiPoint) {
                if (!cmdResult.isSuccess() || ackGetAiPoint.getfLatLng() == null) {
                    X8AiD2PExcuteController.this.mAiGetPointState = AiGetPointState.IDLE;
                    return;
                }
                X8AiD2PExcuteController.this.mapVideoController.getFimiMap().getAiPoint2PointManager().setMarkerByDevice(ackGetAiPoint.getfLatLng().latitude, ackGetAiPoint.getfLatLng().longitude, ackGetAiPoint.getAltitude());
                X8AiD2PExcuteController.this.mAiGetPointState = AiGetPointState.END;
                X8AiD2PExcuteController.this.isDraw = true;
            }
        });
    }

    public boolean isShow() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 4) {
            return false;
        }
        return this.isShow;
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fly_point_to_point), this.rootView.getContext().getString(R.string.x8_ai_fly_p2p_exite), this);
        }
        this.dialog.show();
    }

    public void p2pTaskExite() {
        this.activity.getFcManager().setAiFollowPoint2PointExite(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiD2PExcuteController.this.onTaskComplete(false);
                }
            }
        });
    }

    public void onDroneConnected(boolean b) {
        if (!this.isShow) {
            return;
        }
        if (b) {
            sysAiVcCtrlMode();
        } else {
            onDroneDisconnectTaskComplete();
        }
    }

    private void onTaskComplete(boolean isShow) {
        closeUi();
        if (this.listener != null) {
            this.listener.onPoint2PointBackClick();
            this.listener.onPoint2PointComplete(isShow);
        }
    }

    private void onDroneDisconnectTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeUi();
        if (this.listener != null) {
            this.listener.onPoint2PointBackClick();
            this.listener.onPoint2PointComplete(false);
        }
    }

    public void setAiVcOpen() {
        this.activity.getFcManager().setAiVcOpen(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiD2PExcuteController.this.imgVcToggle.setSelected(true);
                    X8AiD2PExcuteController.this.activity.getmX8AiTrackController().openUi();
                }
            }
        });
    }

    public void setAiVcClose() {
        this.activity.getFcManager().setAiVcClose(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiD2PExcuteController.this.imgVcToggle.setSelected(false);
                    X8AiD2PExcuteController.this.activity.getmX8AiTrackController().closeUi();
                }
            }
        });
    }

    public void setAiVcNotityFc() {
        this.activity.getFcManager().setAiVcNotityFc(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
    }

    public void setGoEnabled(boolean b) {
        if (b) {
            setAiVcNotityFc();
        }
    }

    public void onTouchActionDown() {
    }

    public void onTouchActionUp() {
    }

    public void onTracking() {
        this.imgVcToggle.setEnabled(true);
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void getRunningPoint() {
        if (this.mAiGetPointState == AiGetPointState.IDLE) {
            this.mAiGetPointState = AiGetPointState.GET_POINT;
            getPoint();
        }
    }

    public void openVcToggle() {
        if (this.mapVideoController.isFullVideo()) {
            this.imgVcToggle.setVisibility(0);
        } else {
            this.imgVcToggle.setVisibility(8);
        }
    }

    public void switchMapVideo(boolean isVideo) {
        if (!this.isDraw) {
            return;
        }
        if (isVideo) {
            this.imgVcToggle.setVisibility(8);
        } else {
            this.imgVcToggle.setVisibility(0);
        }
    }

    public void closeIconByNextUi() {
        this.imgNext.setVisibility(8);
        this.imgBack.setVisibility(8);
        this.flagSmall.setVisibility(8);
        this.vHeight.setVisibility(8);
    }

    public void sysAiVcCtrlMode() {
        if (this.state != X8AiPointState.IDLE) {
            return;
        }
        if (this.timeSend == 0) {
            this.timeSend = 1;
            this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                }
            }, X8Task.VCM_FLY_TO.ordinal());
            return;
        }
        this.timeSend = 0;
    }
}
