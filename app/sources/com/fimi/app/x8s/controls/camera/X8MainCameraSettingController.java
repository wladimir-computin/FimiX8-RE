package com.fimi.app.x8s.controls.camera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.camera.CameraParamStatus.CameraModelStatus;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CameraMainSetListener;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FiveKeyDefineManager;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CameraCurParamsJson;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import com.fimi.x8sdk.jsonResult.CurParamsJson;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.presenter.FiveKeyDefinePresenter.ParameterType;
import java.util.List;

public class X8MainCameraSettingController extends AbsX8MenuBoxControllers implements OnClickListener {
    private ImageView camerSetting;
    private IX8CameraMainSetListener cameraMainSetListener;
    private CameraManager cameraManager;
    private X8CameraOtherSettingController cameraOtherController;
    private View cameraSettingblank;
    private Context context;
    private MenuMode curMenu = MenuMode.normal;
    private CameraModelStatus curMode = CameraModelStatus.takePhoto;
    String curParam;
    private X8CameraEVShutterISOController evShutterISOController;
    private FiveKeyDefineManager fiveKeyDefineManager;
    public JsonUiCallBackListener jsonUiCallBackListener = new JsonUiCallBackListener() {
        public void onComplete(JSONObject rt, Object o) {
            if (rt != null) {
                CameraParamsJson paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class);
                int rval = paramsJson.getRval();
                if (paramsJson != null) {
                    String paramType = paramsJson.getType();
                    if (paramType != null) {
                        switch (paramsJson.getMsg_id()) {
                            case 2:
                                if (paramType.equals("contrast") && rval >= 0) {
                                    X8MainCameraSettingController.this.paramsValue.getCurParamsJson().setContrast(X8MainCameraSettingController.this.curParam);
                                    return;
                                } else if (paramType.equals("saturation") && rval >= 0) {
                                    X8MainCameraSettingController.this.paramsValue.getCurParamsJson().setSaturation(X8MainCameraSettingController.this.curParam);
                                    return;
                                } else if (paramType.equals(CameraJsonCollection.KEY_METERMING_MODE) && rval >= 0) {
                                    X8MainCameraSettingController.this.strings = X8MainCameraSettingController.this.context.getResources().getStringArray(R.array.x8_meter_array);
                                    X8ToastUtil.showToast(X8MainCameraSettingController.this.context, X8MainCameraSettingController.this.context.getString(R.string.x8_metering_switch_hint) + X8MainCameraSettingController.this.strings[X8MainCameraSettingController.this.metermingModeOptions.indexOf(X8MainCameraSettingController.this.curParam)], 1);
                                    X8MainCameraSettingController.this.paramsValue.getCurParamsJson().setMetering_mode(X8MainCameraSettingController.this.curParam);
                                    return;
                                } else if (paramType.equals("capture_mode") && rval >= 0) {
                                    X8MainCameraSettingController.this.strings = X8MainCameraSettingController.this.context.getResources().getStringArray(R.array.x8_photo_mode_array);
                                    X8ToastUtil.showToast(X8MainCameraSettingController.this.context, X8MainCameraSettingController.this.context.getString(R.string.x8_metering_switch_hint) + X8MainCameraSettingController.this.strings[X8MainCameraSettingController.this.photoModeOptions.indexOf(X8MainCameraSettingController.this.curParam)], 1);
                                    X8MainCameraSettingController.this.paramsValue.getCurParamsJson().setCapture_mode(X8MainCameraSettingController.this.curParam);
                                    return;
                                } else if (paramType.equals(CameraJsonCollection.KEY_RECORD_MODE) && rval >= 0) {
                                    X8MainCameraSettingController.this.strings = X8MainCameraSettingController.this.context.getResources().getStringArray(R.array.x8_record_mode_array);
                                    X8ToastUtil.showToast(X8MainCameraSettingController.this.context, X8MainCameraSettingController.this.context.getString(R.string.x8_metering_switch_hint) + X8MainCameraSettingController.this.strings[X8MainCameraSettingController.this.recordModeOptions.indexOf(X8MainCameraSettingController.this.curParam)], 1);
                                    X8MainCameraSettingController.this.paramsValue.getCurParamsJson().setRecord_mode(X8MainCameraSettingController.this.curParam);
                                    return;
                                } else {
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                }
            }
        }
    };
    private List<String> metermingModeOptions;
    private View modeSettingView;
    private ViewStub modleStub;
    private ImageView otherSetting;
    private ViewStub otherSettingStub;
    private View otherSettingView;
    private View paramView;
    private X8CameraParamsValue paramsValue;
    private List<String> photoModeOptions;
    private List<String> recordModeOptions;
    private ImageView recordSetting;
    private View rlCameraSetting;
    String[] strings;
    private X8CameraTakePhotoSettingContoller takePhotoSettingContoller;
    private ViewStub x8ISOViewStub;

    private enum MenuMode {
        normal,
        camera,
        other
    }

    public void setCameraMainSetListener(IX8CameraMainSetListener cameraMainSetListener) {
        this.cameraMainSetListener = cameraMainSetListener;
        if (this.cameraOtherController != null) {
            this.cameraOtherController.setListener(this.cameraMainSetListener);
        }
        if (this.evShutterISOController != null) {
            this.evShutterISOController.setMainSetListener(this.cameraMainSetListener);
        }
        if (this.takePhotoSettingContoller != null) {
            this.takePhotoSettingContoller.setMainSetListener(this.cameraMainSetListener);
        }
    }

    public void setEvParamValue(String value) {
        if (this.evShutterISOController != null) {
            this.evShutterISOController.setEvParamValue(value);
        }
    }

    public void onISOSuccess(String value) {
        if (this.evShutterISOController != null) {
            this.evShutterISOController.setIOSParamValue(value);
        }
    }

    public void setCameraManager(CameraManager cameraManager) {
        if (cameraManager != null) {
            this.cameraManager = cameraManager;
            this.evShutterISOController.setCameraManager(cameraManager);
            this.takePhotoSettingContoller.setCameraManager(cameraManager);
            this.cameraOtherController.setCameraManager(cameraManager);
        }
    }

    public X8MainCameraSettingController(View rootView) {
        super(rootView);
        this.context = rootView.getContext();
        this.paramsValue = X8CameraParamsValue.getInstance();
    }

    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.x8_camera_setting_layout);
        this.rlCameraSetting = rootView.findViewById(R.id.x8_rl_main_camera_setting);
        this.cameraSettingblank = rootView.findViewById(R.id.x8_rl_main_camera_setting_blank);
        this.contentView = rootView.findViewById(R.id.rl_main_camera_setting_content);
        this.otherSettingStub = (ViewStub) rootView.findViewById(R.id.stub_camera_other_setting);
        this.x8ISOViewStub = (ViewStub) rootView.findViewById(R.id.stub_camera_iso_setting);
        this.modleStub = (ViewStub) rootView.findViewById(R.id.stub_camera_mode_setting);
        this.camerSetting = (ImageView) rootView.findViewById(R.id.camera_setting_btn);
        this.recordSetting = (ImageView) rootView.findViewById(R.id.record_setting_btn);
        this.otherSetting = (ImageView) rootView.findViewById(R.id.other_setting_btn);
        this.cameraSettingblank.setVisibility(0);
        View view = this.otherSettingStub.inflate();
        View isoView = this.x8ISOViewStub.inflate();
        View modeView = this.modleStub.inflate();
        if (this.otherSettingView == null) {
            this.otherSettingView = view.findViewById(R.id.rl_main_camera_otherSetting_layout);
            this.otherSettingView.setVisibility(8);
        }
        if (this.paramView == null) {
            this.paramView = isoView.findViewById(R.id.camera_params_setting);
        }
        if (this.modeSettingView == null) {
            this.modeSettingView = modeView.findViewById(R.id.x8_mode_setting_layout);
        }
    }

    public void initActions() {
        this.cameraSettingblank.setOnClickListener(this);
        this.camerSetting.setOnClickListener(this);
        this.recordSetting.setOnClickListener(this);
        this.otherSetting.setOnClickListener(this);
        this.cameraOtherController = new X8CameraOtherSettingController(this.otherSettingView);
        this.evShutterISOController = new X8CameraEVShutterISOController(this.paramView);
        this.takePhotoSettingContoller = new X8CameraTakePhotoSettingContoller(this.modeSettingView);
        this.curMenu = MenuMode.normal;
        this.curMode = CameraModelStatus.takePhoto;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.x8_rl_main_camera_setting_blank) {
            closeAiUi(true);
        } else if (id == R.id.camera_setting_btn) {
            menuSelect(MenuMode.normal);
        } else if (id == R.id.other_setting_btn) {
            menuSelect(MenuMode.other);
        } else if (id == R.id.record_setting_btn) {
            menuSelect(MenuMode.camera);
        }
    }

    private void menuSelect(MenuMode mode) {
        this.curMenu = mode;
        this.camerSetting.setSelected(false);
        this.otherSetting.setSelected(false);
        this.recordSetting.setSelected(false);
        if (mode == MenuMode.normal) {
            this.cameraOtherController.closeUi();
            this.evShutterISOController.openUi();
            this.takePhotoSettingContoller.closeUi();
            this.camerSetting.setSelected(true);
        } else if (mode == MenuMode.camera) {
            this.cameraOtherController.closeUi();
            this.evShutterISOController.closeUi();
            this.takePhotoSettingContoller.openUi();
            this.recordSetting.setSelected(true);
        } else if (mode == MenuMode.other) {
            this.cameraOtherController.openUi();
            this.evShutterISOController.closeUi();
            this.takePhotoSettingContoller.closeUi();
            this.otherSetting.setSelected(true);
        }
    }

    public void showCameraSettingUI() {
        enableGesture(false);
        this.rlCameraSetting.setVisibility(0);
        menuSelect(this.curMenu);
        if (!this.isShow) {
            this.isShow = true;
            if (this.width == 0) {
                this.contentView.setAlpha(0.0f);
                this.contentView.post(new Runnable() {
                    public void run() {
                        X8MainCameraSettingController.this.contentView.setAlpha(1.0f);
                        X8MainCameraSettingController.this.MAX_WIDTH = X8MainCameraSettingController.this.rlCameraSetting.getWidth();
                        X8MainCameraSettingController.this.width = X8MainCameraSettingController.this.contentView.getWidth();
                        int h = X8MainCameraSettingController.this.contentView.getHeight();
                        ObjectAnimator animatorY = ObjectAnimator.ofFloat(X8MainCameraSettingController.this.contentView, "translationX", new float[]{(float) X8MainCameraSettingController.this.width, 0.0f});
                        animatorY.setDuration(300);
                        animatorY.start();
                    }
                });
            } else {
                ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.contentView, "translationX", new float[]{(float) this.width, 0.0f});
                animatorY.setDuration(300);
                animatorY.start();
            }
        }
        enableGesture(false);
    }

    public void closeAiUi(boolean b) {
        enableGesture(true);
        if (this.isShow) {
            this.isShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.contentView, "translationX", new float[]{0.0f, (float) this.width});
            translationRight.setDuration(300);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8MainCameraSettingController.this.rlCameraSetting.setVisibility(8);
                    X8MainCameraSettingController.this.curMenu = MenuMode.normal;
                    if (X8MainCameraSettingController.this.evShutterISOController != null) {
                        X8MainCameraSettingController.this.evShutterISOController.setCurModle();
                    }
                }
            });
        }
        if (this.cameraMainSetListener != null) {
            this.cameraMainSetListener.showTopAndBottom(b);
        }
        if (this.takePhotoSettingContoller != null) {
            this.takePhotoSettingContoller.closeUi();
        }
    }

    public void showCameraStatus(AutoCameraStateADV cameraStateADV) {
        if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
            this.recordSetting.setImageResource(R.drawable.x8_btn_photo_set_selector);
        } else if (CameraParamStatus.modelStatus == CameraModelStatus.record) {
            this.recordSetting.setImageResource(R.drawable.x8_btn_record_set_selector);
        }
        this.cameraOtherController.upSdcardStatus(cameraStateADV);
        if (CameraParamStatus.modelStatus != this.curMode) {
            this.curMode = CameraParamStatus.modelStatus;
            if (this.evShutterISOController != null) {
                this.evShutterISOController.setCurModle();
            }
            if (this.takePhotoSettingContoller != null) {
                this.takePhotoSettingContoller.setCurModel();
            }
            if (this.cameraOtherController != null) {
                this.cameraOtherController.setCurModle();
            }
        }
    }

    public void defaultVal() {
        initCameraParams();
    }

    public void parseParamsValue(JSONObject rt) {
        if (this.evShutterISOController != null) {
            this.evShutterISOController.initData((CameraCurParamsJson) JSON.parseObject(rt.toString(), new TypeReference<CameraCurParamsJson>() {
            }, new Feature[0]));
        }
        if (this.takePhotoSettingContoller != null) {
            this.takePhotoSettingContoller.initData(rt);
        }
    }

    public void initCameraParams() {
        this.cameraManager.getCameraCurParams(new JsonUiCallBackListener() {
            public void onComplete(JSONObject rt, Object o) {
                if (rt != null && rt.getIntValue("msg_id") == 3) {
                    if (X8MainCameraSettingController.this.cameraMainSetListener != null) {
                        X8MainCameraSettingController.this.cameraMainSetListener.initCameraParams(rt);
                    }
                    X8MainCameraSettingController.this.parseParamsValue(rt);
                }
                X8MainCameraSettingController.this.cameraManager.getCameraKeyOptions(CameraJsonCollection.KEY_METERMING_MODE, new JsonUiCallBackListener() {
                    public void onComplete(JSONObject rt, Object o) {
                        if (rt != null) {
                            CameraParamsJson paramsJson = (CameraParamsJson) JSON.toJavaObject(rt, CameraParamsJson.class);
                            String paramType = paramsJson.getParam();
                            if (paramsJson != null && paramType != null && paramType.equals(CameraJsonCollection.KEY_METERMING_MODE)) {
                                X8MainCameraSettingController.this.metermingModeOptions = paramsJson.getOptions();
                                if (X8MainCameraSettingController.this.metermingModeOptions != null && X8MainCameraSettingController.this.metermingModeOptions.size() > 0) {
                                    for (int m = X8MainCameraSettingController.this.metermingModeOptions.size() - 1; m >= 0; m--) {
                                        if (((String) X8MainCameraSettingController.this.metermingModeOptions.get(m)).equals(X8MainCameraSettingController.this.context.getResources().getString(R.string.x8_meter_roi))) {
                                            X8MainCameraSettingController.this.metermingModeOptions.remove(m);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                X8MainCameraSettingController.this.cameraManager.getCameraKeyOptions(CameraJsonCollection.KEY_RECORD_MODE, new JsonUiCallBackListener() {
                    public void onComplete(JSONObject rt, Object o) {
                        if (rt != null) {
                            CameraParamsJson paramsJson = (CameraParamsJson) JSON.toJavaObject(rt, CameraParamsJson.class);
                            String paramType = paramsJson.getParam();
                            if (paramsJson != null && paramType != null && paramType.equals(CameraJsonCollection.KEY_RECORD_MODE)) {
                                X8MainCameraSettingController.this.recordModeOptions = paramsJson.getOptions();
                            }
                        }
                    }
                });
                X8MainCameraSettingController.this.cameraManager.getCameraKeyOptions("capture_mode", new JsonUiCallBackListener() {
                    public void onComplete(JSONObject rt, Object o) {
                        if (rt != null) {
                            CameraParamsJson paramsJson = (CameraParamsJson) JSON.toJavaObject(rt, CameraParamsJson.class);
                            String paramType = paramsJson.getParam();
                            if (paramsJson != null && paramType != null && paramType.equals("capture_mode")) {
                                X8MainCameraSettingController.this.photoModeOptions = paramsJson.getOptions();
                            }
                        }
                    }
                });
            }
        });
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.evShutterISOController != null) {
            this.evShutterISOController.onDroneConnected(b);
        }
        if (this.takePhotoSettingContoller != null) {
            this.takePhotoSettingContoller.onDroneConnected(b);
        }
        if (this.cameraOtherController != null) {
            this.cameraOtherController.onDroneConnected(b);
        }
    }

    public int getCurrentContrast() {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (curParamsJson == null) {
            return 0;
        }
        return Integer.parseInt(curParamsJson.getContrast() == null ? "0" : curParamsJson.getContrast());
    }

    public int getCurrentSaturation() {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (curParamsJson == null) {
            return 0;
        }
        return Integer.parseInt(curParamsJson.getSaturation() == null ? "0" : curParamsJson.getSaturation());
    }

    public String getCurrentParams(String key) {
        String currentParams = "";
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (curParamsJson == null) {
            return currentParams;
        }
        if (key == "capture_mode") {
            return curParamsJson.getCapture_mode();
        }
        if (key == CameraJsonCollection.KEY_RECORD_MODE) {
            return curParamsJson.getRecord_mode();
        }
        return curParamsJson.getMetering_mode();
    }

    public String fiveKeySetContrast(ParameterType parameterType, int param) {
        this.curParam = "";
        if (parameterType == ParameterType.ORIGINAL_VALUE) {
            this.curParam = this.fiveKeyDefineManager.setCameraContrast("contrast", param, parameterType, this.jsonUiCallBackListener);
        } else {
            this.curParam = this.fiveKeyDefineManager.setCameraContrast("contrast", getCurrentContrast(), parameterType, this.jsonUiCallBackListener);
        }
        return this.curParam;
    }

    public String fiveKeySetSaturation(ParameterType parameterType, int param) {
        this.curParam = "";
        if (parameterType == ParameterType.ORIGINAL_VALUE) {
            this.curParam = this.fiveKeyDefineManager.setCameraSaturation("saturation", param, parameterType, this.jsonUiCallBackListener);
        } else {
            this.curParam = this.fiveKeyDefineManager.setCameraSaturation("saturation", getCurrentSaturation(), parameterType, this.jsonUiCallBackListener);
        }
        return this.curParam;
    }

    public void fiveKeySetMetering() {
        if (StateManager.getInstance().getCamera().getToken() > 0 && this.metermingModeOptions != null && this.metermingModeOptions.size() > 0) {
            this.curParam = this.fiveKeyDefineManager.setFiveKeyCameraKeyParams(CameraJsonCollection.KEY_METERMING_MODE, this.metermingModeOptions, getCurrentParams(CameraJsonCollection.KEY_METERMING_MODE), this.jsonUiCallBackListener);
        }
    }

    public void fiveKeyShootModeSwitch() {
        if (StateManager.getInstance().getCamera().getToken() > 0) {
            if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
                if (this.photoModeOptions != null && this.photoModeOptions.size() > 0) {
                    this.curParam = this.fiveKeyDefineManager.setFiveKeyCameraKeyParams("capture_mode", this.photoModeOptions, getCurrentParams("capture_mode"), this.jsonUiCallBackListener);
                }
            } else if (this.recordModeOptions != null && this.recordModeOptions.size() > 0) {
                this.curParam = this.fiveKeyDefineManager.setFiveKeyCameraKeyParams(CameraJsonCollection.KEY_RECORD_MODE, this.recordModeOptions, getCurrentParams(CameraJsonCollection.KEY_RECORD_MODE), this.jsonUiCallBackListener);
            }
        }
    }

    public void setFiveKeyManager(FiveKeyDefineManager fiveKeyDefineManager) {
        this.fiveKeyDefineManager = fiveKeyDefineManager;
    }

    public void enableGesture(boolean b) {
        X8Application.enableGesture = b;
    }
}
