package com.fimi.app.x8s.controls;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.camera.CameraParamStatus;
import com.fimi.app.x8s.controls.camera.CameraParamStatus.CameraModelStatus;
import com.fimi.app.x8s.entity.X8AiModeState;
import com.fimi.app.x8s.entity.X8AiModeState.AiModeState;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraPersonLacationListener;
import com.fimi.app.x8s.interfaces.IX8MainRightMenuListener;
import com.fimi.app.x8s.tools.TimeFormateUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.ui.album.x8s.X8MediaActivity;
import com.fimi.app.x8s.widget.X8ModuleSwitcher;
import com.fimi.app.x8s.widget.X8ShutterImageView;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.Constants;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.widget.StrokeTextView;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.cmdsenum.PanoramaPhotographType;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckPanoramaPhotographType;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CurParamsJson;
import com.fimi.x8sdk.listener.IX8PanoramicInformationListener;
import com.fimi.x8sdk.modulestate.CameraState;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.rtp.X8Rtp;

public class X8MainRightMenuController extends AbsX8Controllers implements OnClickListener {
    private X8sMainActivity activity;
    AutoCameraStateADV autoCameraStateADV;
    CameraManager cameraManager;
    private CameraState cameraState;
    private Context context;
    CameraModelStatus curMode;
    private int curModeType;
    private FcCtrlManager fcCtrlManager;
    private ImageButton imbCameraTools;
    private ImageButton imbMedia;
    private X8ShutterImageView imbPhotoVideo;
    private X8ModuleSwitcher imbSwitchPhotoVideo;
    public IX8PanoramicInformationListener ix8PanoramicInformationListener = new IX8PanoramicInformationListener() {
        public void onPanoramicInformationChange(AckPanoramaPhotographType ackPanoramaPhotographType) {
            HostLogBack.getInstance().writeLog("Alanqiu  ============reponseCmd:" + ackPanoramaPhotographType.toString());
            if (ackPanoramaPhotographType.getCurrentNum() >= (byte) 1) {
                if (ackPanoramaPhotographType.getCurrentNum() >= ackPanoramaPhotographType.getTotalNum()) {
                    X8MainRightMenuController.this.tvPanoramaNumber.postDelayed(new Runnable() {
                        public void run() {
                            X8MainRightMenuController.this.tvPanoramaNumber.setVisibility(8);
                            StateManager.getInstance().getCamera().setTakingPanoramicPhotos(false);
                        }
                    }, 3000);
                }
                if (!X8MainRightMenuController.this.getPanoramicStart()) {
                    StateManager.getInstance().getCamera().setTakingPanoramicPhotos(true);
                    X8MainRightMenuController.this.tvPanoramaNumber.setVisibility(0);
                }
                X8MainRightMenuController.this.tvPanoramaNumber.setText(String.format(X8MainRightMenuController.this.getString(R.string.x8_panorama_number), new Object[]{Byte.valueOf(ackPanoramaPhotographType.getCurrentNum()), Byte.valueOf(ackPanoramaPhotographType.getTotalNum())}));
            }
        }
    };
    private ImageView mIvHotDot;
    private StrokeTextView mTvRecordTime;
    private X8AiModeState mX8AiModeState;
    private IX8MainRightMenuListener mainRightMenuListener;
    private IX8CameraPersonLacationListener personLacationListener;
    private boolean pivTake = false;
    private boolean selfShow;
    private StrokeTextView tvPanoramaNumber;

    public X8MainRightMenuController(View rootView, X8sMainActivity activity, X8AiModeState mX8AiModeState) {
        super(rootView);
        this.activity = activity;
        this.mX8AiModeState = mX8AiModeState;
    }

    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.main_right_menu);
        this.imbCameraTools = (ImageButton) rootView.findViewById(R.id.imb_camera_tools);
        this.imbSwitchPhotoVideo = (X8ModuleSwitcher) rootView.findViewById(R.id.imb_switch_photo_video_module);
        this.imbPhotoVideo = (X8ShutterImageView) rootView.findViewById(R.id.imb_photo_video);
        this.imbMedia = (ImageButton) rootView.findViewById(R.id.imb_meida);
        this.mTvRecordTime = (StrokeTextView) rootView.findViewById(R.id.tv_record_time);
        this.tvPanoramaNumber = (StrokeTextView) rootView.findViewById(R.id.tv_panorama_number);
        this.mIvHotDot = (ImageView) rootView.findViewById(R.id.iv_record_hot_dot);
    }

    public void initActions() {
        this.imbCameraTools.setOnClickListener(this);
        this.imbSwitchPhotoVideo.setOnClickListener(this);
        this.imbPhotoVideo.setOnClickListener(this);
        this.imbMedia.setOnClickListener(this);
        this.cameraManager = new CameraManager();
        this.cameraState = StateManager.getInstance().getCamera();
    }

    public void defaultVal() {
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        if (fcCtrlManager != null) {
            this.fcCtrlManager = fcCtrlManager;
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == this.imbCameraTools.getId()) {
            this.mainRightMenuListener.onCameraSettingClick();
        } else if (id == this.imbSwitchPhotoVideo.getId()) {
            this.imbPhotoVideo.setClickable(false);
            onPhotoVideoSwitcher();
        } else if (id == this.imbPhotoVideo.getId()) {
            if (!AbAppUtil.isFastClick(GLMapStaticValue.ANIMATION_FLUENT_TIME)) {
                this.imbPhotoVideo.setClickable(false);
                onPhotoVideoShutter();
                this.mainRightMenuListener.onCameraShutterClick();
            }
        } else if (id != this.imbMedia.getId()) {
        } else {
            if (!this.pivTake) {
                this.context.startActivity(new Intent(this.context, X8MediaActivity.class));
            } else if (!AbAppUtil.isFastClick(GLMapStaticValue.ANIMATION_FLUENT_TIME)) {
                this.imbPhotoVideo.setClickable(false);
                takePhoto();
            }
        }
    }

    public void showPersonLocation() {
        if (this.personLacationListener != null) {
            this.personLacationListener.showPersonLocation();
        }
    }

    public void onPhotoVideoSwitcher() {
        if (StateManager.getInstance().getCamera().isConnect()) {
            if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
                this.cameraManager.swithVideoMode(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                    }
                });
            } else {
                this.cameraManager.swithPhotoMode(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                    }
                });
            }
        } else if (this.imbSwitchPhotoVideo.getCurrentIndex() == 0) {
            this.imbSwitchPhotoVideo.setCurrentIndex(1);
            this.imbPhotoVideo.setCurrentIndex(1, 0);
        } else {
            this.imbSwitchPhotoVideo.setCurrentIndex(0);
            this.imbPhotoVideo.setCurrentIndex(0, 0);
        }
    }

    public void onPhotoVideoShutter() {
        if (this.autoCameraStateADV == null || this.autoCameraStateADV.getInfo() == 3) {
            X8ToastUtil.showToast(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_camera_rtp8), 1);
        } else if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
            takePhoto();
        } else if (this.autoCameraStateADV == null) {
        } else {
            if (this.autoCameraStateADV.getState() == 2) {
                stopRecord();
            } else {
                startRecord();
            }
        }
    }

    private void takePhoto() {
        if (this.autoCameraStateADV.getMode() == 20) {
            HostLogBack.getInstance().writeLog("Alanqiu  ============onItemClickListener:暂停" + StateManager.getInstance().getCamera().isTakingPanoramicPhotos());
            if (StateManager.getInstance().getCamera().isTakingPanoramicPhotos()) {
                this.fcCtrlManager.setPanoramaPhotographState(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess) {
                            X8MainRightMenuController.this.tvPanoramaNumber.setVisibility(8);
                        }
                        StateManager.getInstance().getCamera().setTakingPanoramicPhotos(false);
                    }
                }, FcCollection.MSG_ID_SET_PANORAMA_PHOTOGRAPH_STOP);
                return;
            } else if (StateManager.getInstance().getX8Drone().isInSky()) {
                this.fcCtrlManager.setPanoramaPhotographType(new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        HostLogBack.getInstance().writeLog("Alanqiu  ============onItemClickListener:cmdResult:" + cmdResult.toString());
                    }
                }, Constants.panoramaType + 1);
                return;
            } else {
                X8ToastUtil.showToast(this.context, "请起飞飞行器", 1);
                return;
            }
        }
        this.cameraManager.takePhoto(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                X8MainRightMenuController.this.imbPhotoVideo.setClickable(true);
                if (!cmdResult.isSuccess()) {
                    X8ToastUtil.showToast(X8MainRightMenuController.this.rootView.getContext(), X8Rtp.getRtpStringCamera(X8MainRightMenuController.this.rootView.getContext(), cmdResult.getmMsgRpt()), 1);
                }
            }
        });
    }

    private void startRecord() {
        this.cameraManager.startVideo(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                X8MainRightMenuController.this.imbPhotoVideo.setClickable(true);
                if (cmdResult.isSuccess()) {
                    X8MainRightMenuController.this.checkCameraParam();
                    return;
                }
                X8ToastUtil.showToast(X8MainRightMenuController.this.rootView.getContext(), X8Rtp.getRtpStringCamera(X8MainRightMenuController.this.rootView.getContext(), cmdResult.getmMsgRpt()), 1);
            }
        });
    }

    private void stopRecord() {
        this.cameraManager.stopVideo(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8MainRightMenuController.this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                }
            }
        });
    }

    public void setListener(IX8MainRightMenuListener mainRightMenuListener) {
        this.mainRightMenuListener = mainRightMenuListener;
    }

    public void setAiFly(boolean visiable) {
    }

    public void setPersonLacationListener(IX8CameraPersonLacationListener personLacationListener) {
        this.personLacationListener = personLacationListener;
    }

    public void showCameraView(boolean isShow) {
        if (!this.mX8AiModeState.isAiModeStateReady()) {
            this.handleView.setVisibility(isShow ? 0 : 8);
        }
    }

    public void switchByCloseFullScreen(boolean isFullVideo) {
        this.handleView.setVisibility(isFullVideo ? 0 : 8);
    }

    public void showCameraState(AutoCameraStateADV cameraStateADV) {
        if (this.curMode != CameraParamStatus.modelStatus) {
            this.imbPhotoVideo.setClickable(true);
        }
        if (cameraStateADV != null) {
            this.autoCameraStateADV = cameraStateADV;
            int status = cameraStateADV.getState();
            if (status == 6) {
                Toast.makeText(this.context, this.context.getResources().getString(R.string.x8_camera_error), 0).show();
                return;
            }
            if (status == 5) {
                X8ToastUtil.showToast(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_camera_take_success), 1);
            } else if (status == 9) {
                X8ToastUtil.showToast(this.context, getString(R.string.x8_camera_captured_successfully), 1);
                return;
            }
            int model = cameraStateADV.getMode();
            if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
                if (!(this.curMode == CameraParamStatus.modelStatus && (this.curModeType == model || getPanoramicStart()))) {
                    this.curMode = CameraParamStatus.modelStatus;
                    this.imbSwitchPhotoVideo.setCurrentIndex(0);
                    this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                    if (this.mainRightMenuListener != null) {
                        this.mainRightMenuListener.turnCameraModel();
                    }
                    if (model == 16) {
                        this.imbPhotoVideo.setCurrentIndex(0, 0);
                        this.curModeType = 16;
                    } else if (model == 19) {
                        this.imbPhotoVideo.setCurrentIndex(3, 0);
                        this.curModeType = 19;
                    } else if (model != 20) {
                        this.imbPhotoVideo.setCurrentIndex(0, 0);
                        this.curModeType = 16;
                    } else if (Constants.panoramaType == PanoramaPhotographType.PANORAMA_TYPE_LEVEL.ordinal()) {
                        this.imbPhotoVideo.setCurrentIndex(5, 0);
                    } else if (Constants.panoramaType == PanoramaPhotographType.PANORAMA_TYPE_RIGHT_ANGLE.ordinal()) {
                        this.imbPhotoVideo.setCurrentIndex(6, 0);
                    } else {
                        this.imbPhotoVideo.setCurrentIndex(7, 0);
                    }
                }
                if (this.autoCameraStateADV.isDelayedPhotography() || getPanoramicStart()) {
                    if (this.imbSwitchPhotoVideo.isClickable()) {
                        this.imbSwitchPhotoVideo.setCurrentIndex(2);
                        this.imbSwitchPhotoVideo.setClickable(false);
                        this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_unclickable);
                        this.imbMedia.setClickable(false);
                    }
                } else if (!this.imbSwitchPhotoVideo.isClickable()) {
                    this.imbSwitchPhotoVideo.setCurrentIndex(0);
                    this.imbSwitchPhotoVideo.setClickable(true);
                    this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                    this.imbMedia.setClickable(true);
                }
                this.pivTake = false;
            } else if (CameraParamStatus.modelStatus == CameraModelStatus.record) {
                if (!(this.curMode == CameraParamStatus.modelStatus && this.curModeType == model)) {
                    this.curMode = CameraParamStatus.modelStatus;
                    this.imbSwitchPhotoVideo.setCurrentIndex(1);
                    if (this.mainRightMenuListener != null) {
                        this.mainRightMenuListener.turnCameraModel();
                    }
                    if (model == 32) {
                        this.imbPhotoVideo.setCurrentIndex(1, 0);
                        this.curModeType = 32;
                    } else if (model == 33) {
                        this.imbPhotoVideo.setCurrentIndex(4, 0);
                        this.curModeType = 33;
                    } else {
                        this.imbPhotoVideo.setCurrentIndex(1, 0);
                        this.curModeType = 32;
                    }
                }
                this.pivTake = false;
            }
            if (CameraParamStatus.modelStatus == CameraModelStatus.recording) {
                if (this.curMode != CameraParamStatus.modelStatus) {
                    this.curMode = CameraParamStatus.modelStatus;
                    this.imbSwitchPhotoVideo.setCurrentIndex(1);
                    this.mIvHotDot.setVisibility(0);
                    this.mTvRecordTime.setVisibility(0);
                    this.imbPhotoVideo.setCurrentIndex(1, 1);
                    if (this.mainRightMenuListener != null) {
                        this.mainRightMenuListener.turnCameraModel();
                    }
                    checkCameraParam();
                }
                this.mTvRecordTime.setText(TimeFormateUtil.getRecordTime(cameraStateADV.getRecHour(), cameraStateADV.getRecMinute(), cameraStateADV.getRecSecond()));
                if (this.imbSwitchPhotoVideo.isClickable()) {
                    this.imbSwitchPhotoVideo.setCurrentIndex(3);
                    this.imbSwitchPhotoVideo.setClickable(false);
                    if (!this.pivTake) {
                        this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_unclickable);
                        this.imbMedia.setClickable(false);
                    }
                }
            } else if (!this.autoCameraStateADV.isDelayedPhotography() && !getPanoramicStart()) {
                if (!this.imbSwitchPhotoVideo.isClickable() && this.mIvHotDot.getVisibility() == 0) {
                    this.imbSwitchPhotoVideo.setCurrentIndex(1);
                    this.imbSwitchPhotoVideo.setClickable(true);
                }
                this.mIvHotDot.setVisibility(8);
                this.mTvRecordTime.setVisibility(8);
                this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                this.pivTake = false;
                if (!this.pivTake) {
                    this.imbMedia.setClickable(true);
                }
            }
        }
    }

    public void openUi() {
        this.selfShow = true;
        super.openUi();
        showCameraState(StateManager.getInstance().getCamera().getAutoCameraStateADV());
        showCameraView(this.activity.getmMapVideoController().isFullVideo());
    }

    public void closeUi() {
        this.selfShow = false;
        this.curMode = null;
        super.closeUi();
    }

    public void setOtherShow(boolean show) {
        if (show) {
            this.mX8AiModeState.setAiModeState(AiModeState.READY);
        } else {
            this.mX8AiModeState.setAiModeState(AiModeState.IDLE);
        }
    }

    public void setOtherStateRunning() {
        this.mX8AiModeState.setAiModeState(AiModeState.RUNNING);
    }

    public void setBackGround(int color) {
        this.handleView.setBackgroundColor(color);
    }

    public void checkCameraParam() {
        X8CameraParamsValue paramsValue = X8CameraParamsValue.getInstance();
        if (paramsValue != null) {
            CurParamsJson object = paramsValue.getCurParamsJson();
            if (object != null) {
                String video_resolution = object.getVideo_resolution();
                String system_type = object.getSystem_type();
                if (video_resolution == null || "".equals(video_resolution) || system_type == null || "".equals(system_type)) {
                    this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                    this.pivTake = false;
                } else if (video_resolution.equals("1920x1080 50P 16:9") || video_resolution.equals("1920x1080 25P 16:9")) {
                    if (system_type.equals("PAL")) {
                        this.pivTake = true;
                        this.imbMedia.setBackgroundResource(R.drawable.x8_piv_btn_selector);
                    }
                } else if ((video_resolution.equals("1920x1080 30P 16:9") || video_resolution.equals("1920x1080 60P 16:9")) && system_type.equals("NTSC")) {
                    this.pivTake = true;
                    this.imbMedia.setBackgroundResource(R.drawable.x8_piv_btn_selector);
                }
            }
        }
    }

    private boolean getPanoramicStart() {
        return StateManager.getInstance().getCamera().isTakingPanoramicPhotos();
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        this.imbPhotoVideo.setClickable(b);
        if (!(this.autoCameraStateADV == null || this.autoCameraStateADV.isDelayedPhotography() || getPanoramicStart())) {
            this.imbSwitchPhotoVideo.setClickable(b);
        }
        if (!b && CameraParamStatus.modelStatus == CameraModelStatus.recording) {
            this.imbPhotoVideo.setCurrentIndex(1, 0);
            this.curModeType = 32;
            this.mIvHotDot.setVisibility(8);
            this.mTvRecordTime.setVisibility(8);
            this.curMode = CameraModelStatus.ideal;
        }
        if (!b && getPanoramicStart()) {
            this.tvPanoramaNumber.setVisibility(8);
        }
    }

    public boolean onClickBackKey() {
        return false;
    }

    public int getHandleViewWidth() {
        return this.handleView.getWidth() + 20;
    }
}
