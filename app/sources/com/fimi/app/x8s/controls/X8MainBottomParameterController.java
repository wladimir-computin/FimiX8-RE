package com.fimi.app.x8s.controls;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.camera.CameraParamStatus;
import com.fimi.app.x8s.controls.camera.CameraParamStatus.CameraModelStatus;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CameraCurParamsJson;
import com.fimi.x8sdk.jsonResult.CurParamsJson;
import com.fimi.x8sdk.modulestate.GimbalState;
import com.fimi.x8sdk.modulestate.StateManager;
import java.util.List;

public class X8MainBottomParameterController extends AbsX8Controllers {
    public final String VALUE_VIDEO_RESOLUTION_1080P = "1920x1080";
    public final String VALUE_VIDEO_RESOLUTION_2K = "2560x1440";
    public final String VALUE_VIDEO_RESOLUTION_4K = "3840x2160";
    public final String VALUE_VIDEO_RESOLUTION_720P = "1280x720";
    private X8sMainActivity activity;
    AutoCameraStateADV cameraStateADV;
    int changeShowStatus = 0;
    private Context context;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (X8MainBottomParameterController.this.changeShowStatus == 0) {
                X8MainBottomParameterController.this.changeShowStatus = 1;
            } else {
                X8MainBottomParameterController.this.changeShowStatus = 0;
            }
            X8MainBottomParameterController.this.showTFCardStatus(X8MainBottomParameterController.this.changeShowStatus);
            X8MainBottomParameterController.this.mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };
    private ImageView mImgColor;
    private ImageView mIvCloud;
    private ImageView mIvEv;
    private ImageView mIvISO;
    private ImageView mIvRecord;
    private ImageView mIvSDK;
    private ImageView mIvShutter;
    private TextView mTvCloud;
    private TextView mTvColor;
    private TextView mTvEv;
    private TextView mTvISO;
    private TextView mTvRecord;
    private TextView mTvSDK;
    private TextView mTvShutter;
    private CurParamsJson paramsValue = X8CameraParamsValue.getInstance().getCurParamsJson();
    PercentRelativeLayout root_layout;
    String tfCardCapt = "";

    private void showTFCardStatus(int changeShowStatus) {
        if (this.cameraStateADV != null && StateManager.getInstance().getCamera() != null && StateManager.getInstance().getCamera().isConnect()) {
            int imgRes;
            int textColor;
            String text = "";
            switch (this.cameraStateADV.getInfo()) {
                case 0:
                    imgRes = R.drawable.x8_tf_card_nomal;
                    textColor = -1;
                    text = this.tfCardCapt;
                    break;
                case 1:
                    imgRes = R.drawable.x8_tf_card_low_fulling;
                    textColor = -1;
                    if (changeShowStatus != 0) {
                        text = this.tfCardCapt;
                        break;
                    } else {
                        text = getString(R.string.x8_tf_low);
                        break;
                    }
                case 2:
                    imgRes = R.drawable.x8_tf_card_exception;
                    textColor = SupportMenu.CATEGORY_MASK;
                    if (changeShowStatus != 0 && !this.tfCardCapt.equalsIgnoreCase(getString(R.string.x8_na))) {
                        text = this.tfCardCapt;
                        break;
                    } else {
                        text = getString(R.string.x8_tf_exception);
                        break;
                    }
                    break;
                case 3:
                    imgRes = R.drawable.x8_tf_card_no;
                    textColor = SupportMenu.CATEGORY_MASK;
                    text = getString(R.string.x8_tf_no_exit);
                    break;
                case 4:
                    imgRes = R.drawable.x8_tf_card_low_fulling;
                    textColor = SupportMenu.CATEGORY_MASK;
                    text = this.tfCardCapt;
                    break;
                case 5:
                    imgRes = R.drawable.x8_tf_fulled;
                    textColor = SupportMenu.CATEGORY_MASK;
                    text = this.tfCardCapt;
                    break;
                case 6:
                    imgRes = R.drawable.x8_tf_card_exception;
                    textColor = SupportMenu.CATEGORY_MASK;
                    if (changeShowStatus != 0 && !this.tfCardCapt.equalsIgnoreCase(getString(R.string.x8_na))) {
                        text = this.tfCardCapt;
                        break;
                    } else {
                        text = getString(R.string.x8_tf_exception);
                        break;
                    }
                    break;
                default:
                    imgRes = R.drawable.x8_tf_card_nomal;
                    textColor = -1;
                    text = this.tfCardCapt;
                    break;
            }
            setTFCardStatus(imgRes, textColor, text);
        }
    }

    private void setTFCardStatus(int imgRes, int textColor, String text) {
        this.mIvSDK.setBackgroundResource(imgRes);
        this.mTvSDK.setTextColor(textColor);
        this.mTvSDK.setText(text);
    }

    public X8MainBottomParameterController(View rootView, X8sMainActivity activity) {
        super(rootView);
        this.activity = activity;
    }

    public void initActions() {
    }

    public void showSportState(GimbalState state) {
        this.mTvCloud.setText(NumberUtil.decimalPointStr(((double) state.getPitchAngle()) / 100.0d, 1) + "°");
        this.mIvCloud.setBackgroundResource(R.drawable.x8_main_cloud_angle);
    }

    public void defaultVal() {
        this.mIvCloud.setBackgroundResource(R.drawable.x8_main_cloud_angle_unconnect);
        this.mIvEv.setBackgroundResource(R.drawable.x8_main_ev_unconnect);
        this.mIvISO.setBackgroundResource(R.drawable.x8_main_iso_unconnect);
        this.mIvShutter.setBackgroundResource(R.drawable.x8_main_shutter_unconnect);
        this.mIvSDK.setBackgroundResource(R.drawable.x8_main_sdk_unconnect);
        this.mImgColor.setBackgroundResource(R.drawable.x8_main_bottom_camera_color_unconnect);
        this.mIvRecord.setSelected(false);
        this.mTvCloud.setText(R.string.x8_na);
        this.mTvEv.setText(R.string.x8_na);
        this.mTvISO.setText(R.string.x8_na);
        this.mTvShutter.setText(R.string.x8_na);
        this.mTvColor.setText(R.string.x8_na);
        this.mTvSDK.setTextColor(-1);
        this.mTvSDK.setText(R.string.x8_na);
        this.mTvRecord.setText(R.string.x8_na);
    }

    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.main_bottom_parameter);
        this.root_layout = (PercentRelativeLayout) this.handleView;
        this.mIvCloud = (ImageView) rootView.findViewById(R.id.iv_bottom_cloud);
        this.mTvCloud = (TextView) rootView.findViewById(R.id.tv_bottom_cloud);
        this.mIvEv = (ImageView) rootView.findViewById(R.id.iv_bottom_ev);
        this.mTvEv = (TextView) rootView.findViewById(R.id.tv_bottom_ev);
        this.mIvISO = (ImageView) rootView.findViewById(R.id.iv_bottom_iso);
        this.mTvISO = (TextView) rootView.findViewById(R.id.tv_bottom_iso);
        this.mIvShutter = (ImageView) rootView.findViewById(R.id.iv_bottom_shutter);
        this.mTvShutter = (TextView) rootView.findViewById(R.id.tv_bottom_shutter);
        this.mImgColor = (ImageView) rootView.findViewById(R.id.iv_bottom_color);
        this.mTvColor = (TextView) rootView.findViewById(R.id.tv_bottom_color);
        this.mIvSDK = (ImageView) rootView.findViewById(R.id.iv_bottom_sdk);
        this.mTvSDK = (TextView) rootView.findViewById(R.id.tv_bottom_sdk);
        this.mIvRecord = (ImageView) rootView.findViewById(R.id.iv_bottom_record);
        this.mTvRecord = (TextView) rootView.findViewById(R.id.tv_bottom_record);
    }

    public void showCameraStatus(AutoCameraStateADV cameraStateADV) {
        if (cameraStateADV != null) {
            if (cameraStateADV.isCardInfo()) {
                updateTFCardStorage(cameraStateADV);
            } else {
                this.cameraStateADV = cameraStateADV;
            }
            if (this.mHandler != null && !this.mHandler.hasMessages(0)) {
                this.mHandler.sendEmptyMessageDelayed(0, 100);
            }
        }
    }

    private void updateTFCardStorage(AutoCameraStateADV cameraStateADV) {
        String freespace = NumberUtil.decimalPointStr((((double) cameraStateADV.getFreeSpace()) / 1024.0d) / 1024.0d, 1);
        String totalSpace = NumberUtil.decimalPointStr((((double) cameraStateADV.getTotalSpace()) / 1024.0d) / 1024.0d, 1);
        if (totalSpace.equals("0.0")) {
            this.tfCardCapt = getString(R.string.x8_na);
        } else {
            this.tfCardCapt = freespace + "/" + totalSpace + "G";
        }
    }

    public void initCameraParam(CameraCurParamsJson paramsJson) {
        if (paramsJson != null) {
            List<CurParamsJson> plist = paramsJson.getParam();
            if (plist != null && plist.size() > 0) {
                for (CurParamsJson curParams : plist) {
                    if (curParams.getAe_bias() != null) {
                        this.mTvEv.setText(replaceEv(curParams.getAe_bias()));
                        this.mIvEv.setBackgroundResource(R.drawable.x8_main_ev_connect);
                    } else if (curParams.getShutter_time() != null) {
                        updateShutter(curParams.getShutter_time());
                        this.mIvShutter.setBackgroundResource(R.drawable.x8_main_shutter_connect);
                    } else if (curParams.getIso() != null) {
                        this.mTvISO.setText(curParams.getIso());
                        this.mIvISO.setBackgroundResource(R.drawable.x8_main_iso_connect);
                    } else {
                        if (curParams.getDigital_effect() != null) {
                            setColor(curParams.getDigital_effect());
                        }
                        if (curParams.getVideo_resolution() != null) {
                            this.paramsValue.setVideo_resolution(curParams.getVideo_resolution());
                            StateManager.getInstance().setIs4KResolution(curParams.getVideo_resolution());
                        }
                        if (curParams.getPhoto_size() != null) {
                            this.paramsValue.setPhoto_size(curParams.getPhoto_size());
                        }
                    }
                }
            }
            updateCameraModelValue();
        }
    }

    private void setColor(String paramValue) {
        CharSequence paramValue2;
        if (paramValue2.equals("General")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_general);
        } else if (paramValue2.equals("Vivid")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_vivid);
        } else if (paramValue2.equals("art")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_art);
        } else if (paramValue2.equals("black/white")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_black_white);
        } else if (paramValue2.equals("film")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_film);
        } else if (paramValue2.equals("sepia")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_sepia);
        } else if (paramValue2.equals("F-LOG")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_flog);
        } else if (paramValue2.equals("punk")) {
            paramValue2 = this.context.getResources().getString(R.string.x8_colours_punk);
        }
        this.mImgColor.setBackgroundResource(R.drawable.x8_main_bottom_camera_color);
        this.mTvColor.setText(paramValue2);
    }

    public void updateEvTextValue(String ev) {
        this.mTvEv.setText(replaceEv(ev));
    }

    private String replaceEv(String ev) {
        if (ev.contains("EV")) {
            return ev.replace("EV", "");
        }
        return ev;
    }

    public void updateISOTextValue(String iso) {
        this.mTvISO.setText(iso);
    }

    public void updateColoreTextValue(String text) {
        this.mTvColor.setText(text);
    }

    public void updateShutter(String shutter) {
        if (shutter.equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
            this.mTvShutter.setText(shutter);
        } else {
            this.mTvShutter.setText(shutter.substring(0, shutter.length() - 1));
        }
    }

    public void updateCameraModelValue() {
        String modelValue = "";
        if (CameraParamStatus.modelStatus == CameraModelStatus.record || CameraParamStatus.modelStatus == CameraModelStatus.recording) {
            modelValue = X8CameraParamsValue.getInstance().getCurParamsJson().getVideo_resolution();
            if (modelValue != null && !"".equals(modelValue)) {
                String[] values = modelValue.split("\\s+");
                values[1] = values[1].replace("P", "FPS");
                if (values[0].equals("3840x2160")) {
                    modelValue = "4K/" + values[1];
                } else if (values[0].equals("2560x1440")) {
                    modelValue = "2.5K/" + values[1];
                } else if (values[0].equals("1920x1080")) {
                    modelValue = "1080P/" + values[1];
                } else if (values[0].equals("1280x720")) {
                    modelValue = "720P/" + values[1];
                }
                this.mIvRecord.setBackgroundResource(R.drawable.x8_bottom_record_btn_select);
            } else {
                return;
            }
        }
        modelValue = X8CameraParamsValue.getInstance().getCurParamsJson().getPhoto_size();
        if (modelValue != null && !"".equals(modelValue)) {
            modelValue = modelValue.split("\\s+")[2].split("\\u0029")[0];
            this.mIvRecord.setBackgroundResource(R.drawable.x8_bottom_photo_btn_select);
        } else {
            return;
        }
        this.mIvRecord.setSelected(true);
        this.mTvRecord.setText(modelValue);
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        updateViewEnable(b, this.root_layout);
    }

    private void updateViewEnable(boolean enable, ViewGroup... parent) {
        if (parent != null && parent.length > 0) {
            for (ViewGroup group : parent) {
                int len = group.getChildCount();
                for (int j = 0; j < len; j++) {
                    View subView = group.getChildAt(j);
                    if (subView instanceof ViewGroup) {
                        updateViewEnable(enable, (ViewGroup) subView);
                    } else {
                        subView.setEnabled(enable);
                        if (subView instanceof TextView) {
                            subView.setAlpha(enable ? 1.0f : 0.9f);
                        }
                    }
                }
            }
        }
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void openUi() {
        if (this.activity.getmMapVideoController().isFullVideo()) {
            super.openUi();
        }
    }

    public void openUiByMapChange() {
        if (!this.activity.getmMapVideoController().isFullVideo() && this.activity.getTaskManger().isTaskCanChangeBottom()) {
            super.openUi();
        }
    }

    public String getEvText() {
        return this.mTvEv.getText().toString();
    }

    public String getISOText() {
        return this.mTvISO.getText().toString();
    }
}
