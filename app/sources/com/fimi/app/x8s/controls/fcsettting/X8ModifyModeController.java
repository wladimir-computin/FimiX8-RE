package com.fimi.app.x8s.controls.fcsettting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.fcsettting.maintain.X8BlackBoxController;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.network.ApkVersionManager;
import com.fimi.network.ApkVersionManager.AppSettingListener;
import com.fimi.x8sdk.controller.FcCtrlManager;

public class X8ModifyModeController extends AbsX8MenuBoxControllers {
    private X8AircraftCalibrationController aircraftCalibrationController;
    private ApkVersionManager apkVersionManager = new ApkVersionManager();
    private ImageView back_btn;
    private RelativeLayout blackBoxLayout;
    private RelativeLayout blackBoxLogLayout;
    private View blackBoxView;
    private View calibrationView;
    private ViewStub calibrationViewStub;
    private Button calibration_btn;
    private RelativeLayout checkLayout;
    private View checkView;
    private FcCtrlManager fcCtrlManager;
    private PercentRelativeLayout itemLayout;
    private X8BlackBoxController mX8BlackBoxController;
    private IX8GeneraModifyModeControllerListener modeControllerListener;
    private RelativeLayout nextContentLayout;
    private X8ModifySensorController sensorController;
    private RelativeLayout sensorLayout;
    private View sensorView;
    private ViewStub sensorViewStub;
    private PercentRelativeLayout topLayout;
    private RelativeLayout x8AircraftCalibrationLayout;

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setModeControllerListener(IX8GeneraModifyModeControllerListener modeControllerListener) {
        this.modeControllerListener = modeControllerListener;
    }

    public X8ModifyModeController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.handleView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_general_item_modify_layout, (ViewGroup) rootView, true);
        this.back_btn = (ImageView) this.handleView.findViewById(R.id.btn_return);
        this.sensorLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_sensor_layout);
        this.calibration_btn = (Button) this.handleView.findViewById(R.id.x8_btn_calibration);
        this.checkLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_check_layout);
        this.blackBoxLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_blackbox_layout);
        this.topLayout = (PercentRelativeLayout) this.handleView.findViewById(R.id.layout_top);
        this.itemLayout = (PercentRelativeLayout) this.handleView.findViewById(R.id.x8_modify_item_layout);
        this.blackBoxLogLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_blackbox_layout1);
        this.x8AircraftCalibrationLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_aircraft_calibration_layout);
        this.nextContentLayout = (RelativeLayout) this.handleView.findViewById(R.id.rl_next_content);
    }

    public void initActions() {
        this.calibration_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
                X8ModifyModeController.this.modeControllerListener.onOpen();
                if (X8ModifyModeController.this.calibrationViewStub == null) {
                    X8ModifyModeController.this.calibrationViewStub = (ViewStub) X8ModifyModeController.this.handleView.findViewById(R.id.x8_calibration_view);
                    X8ModifyModeController.this.calibrationView = X8ModifyModeController.this.calibrationViewStub.inflate();
                    X8ModifyModeController.this.aircraftCalibrationController = new X8AircraftCalibrationController(X8ModifyModeController.this.calibrationView);
                }
                X8ModifyModeController.this.aircraftCalibrationController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.aircraftCalibrationController.showItem();
                X8ModifyModeController.this.aircraftCalibrationController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() {
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(0);
                        X8ModifyModeController.this.itemLayout.setVisibility(0);
                    }

                    public void onOpen() {
                    }

                    public void onClose() {
                        X8ModifyModeController.this.modeControllerListener.onClose();
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
            }
        });
        this.back_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8ModifyModeController.this.handleView.setVisibility(8);
                X8ModifyModeController.this.modeControllerListener.returnBack();
            }
        });
        this.sensorLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (X8ModifyModeController.this.sensorViewStub == null) {
                    X8ModifyModeController.this.sensorViewStub = (ViewStub) X8ModifyModeController.this.handleView.findViewById(R.id.x8_sensor_view);
                    X8ModifyModeController.this.sensorView = X8ModifyModeController.this.sensorViewStub.inflate();
                    X8ModifyModeController.this.sensorController = new X8ModifySensorController(X8ModifyModeController.this.sensorView);
                }
                X8ModifyModeController.this.sensorController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.sensorController.showItem();
                X8ModifyModeController.this.sensorController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() {
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(0);
                        X8ModifyModeController.this.itemLayout.setVisibility(0);
                    }

                    public void onOpen() {
                    }

                    public void onClose() {
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
            }
        });
        this.checkLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.blackBoxLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.blackBoxLogLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
                X8ModifyModeController.this.mX8BlackBoxController = new X8BlackBoxController(X8ModifyModeController.this.nextContentLayout, X8ModifyModeController.this);
                X8ModifyModeController.this.modeControllerListener.onOpen();
            }
        });
    }

    public void defaultVal() {
    }

    public void showItem() {
        super.showItem();
        this.apkVersionManager.getAppSetting(new AppSettingListener() {
            public void onAppSettingListener() {
                ApkVersionManager access$1500 = X8ModifyModeController.this.apkVersionManager;
                X8ModifyModeController.this.apkVersionManager.getClass();
                if (access$1500.isOpen("open_sixpoint_calibrate")) {
                    X8ModifyModeController.this.x8AircraftCalibrationLayout.setVisibility(0);
                } else {
                    X8ModifyModeController.this.x8AircraftCalibrationLayout.setVisibility(8);
                }
            }
        });
        this.handleView.setVisibility(0);
        if (this.sensorView != null) {
            this.sensorView.setVisibility(8);
        }
        this.topLayout.setVisibility(0);
        this.itemLayout.setVisibility(0);
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
    }

    public void closeItem() {
        super.closeItem();
        if (this.sensorController != null) {
            this.sensorController.closeItem();
        }
    }

    public void onBlackBoxBack() {
        this.topLayout.setVisibility(0);
        this.itemLayout.setVisibility(0);
        this.modeControllerListener.onClose();
        this.mX8BlackBoxController = null;
        this.nextContentLayout.removeAllViews();
    }

    public boolean isRunningTask() {
        if (this.mX8BlackBoxController == null || !this.mX8BlackBoxController.isRunningTask()) {
            return true;
        }
        return false;
    }
}
