package com.fimi.app.x8s.controls.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.camera.CameraParamStatus.CameraModelStatus;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraMainSetListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8PieView;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.percent.PercentLinearLayout;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8CameraOtherSettingController extends AbsX8Controllers implements OnClickListener {
    private CameraManager cameraManager;
    private TextView capacity_tv;
    private boolean clickAble = true;
    private Context context;
    private X8DoubleCustomDialog dialog;
    private RelativeLayout formatView;
    private RelativeLayout layoutReturn;
    private int linesSelectIdx;
    private IX8CameraMainSetListener listener;
    private RelativeLayout resetView;
    double sdcardFree;
    private TextView sdcard_free_tv;
    private TextView tvCurrLinesSetting;
    private TextView tvLinesSettingCenterPoint;
    private TextView tvLinesSettingDiagonalAndNine;
    private TextView tvLinesSettingNineLines;
    private TextView tvLinesSettingNone;
    private ImageView x8CameraOtherImgReturn;
    private PercentLinearLayout x8LinesSettingDetailView;
    private RelativeLayout x8LinesSettingLayout;
    private ViewStub x8LinesSettingStub;
    private RelativeLayout x8OtherSettingMainLayout;
    private X8PieView x8PieView;

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setCurModle() {
        upCapacityDes();
    }

    public X8CameraOtherSettingController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.rl_main_camera_otherSetting_layout);
        this.x8PieView = (X8PieView) rootView.findViewById(R.id.x8_pieView);
        this.x8PieView.setData(new int[]{45, 65, 78}, this.context.getResources().getStringArray(R.array.x8_sdcard_array));
        this.x8LinesSettingLayout = (RelativeLayout) rootView.findViewById(R.id.x8_lines_layout);
        this.x8LinesSettingStub = (ViewStub) rootView.findViewById(R.id.x8_lines_setting_stub);
        this.x8OtherSettingMainLayout = (RelativeLayout) rootView.findViewById(R.id.x8_other_setting_main_layout);
        this.tvCurrLinesSetting = (TextView) rootView.findViewById(R.id.tv_current_lines_setting);
        this.linesSelectIdx = GlobalConfig.getInstance().getGridLine();
        this.tvCurrLinesSetting.setText(getCurrLinesSettingStr());
        this.x8LinesSettingLayout.setOnClickListener(this);
        this.formatView = (RelativeLayout) rootView.findViewById(R.id.x8_sdcard_format_layout);
        this.resetView = (RelativeLayout) rootView.findViewById(R.id.x8_camera_reset_layout);
        this.formatView.setOnClickListener(this);
        this.resetView.setOnClickListener(this);
        this.sdcard_free_tv = (TextView) rootView.findViewById(R.id.sdcard_free_tv);
        this.capacity_tv = (TextView) rootView.findViewById(R.id.sdcard_capacity_tv);
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void openUi() {
        super.openUi();
    }

    public void closeUi() {
        super.closeUi();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.x8_lines_layout) {
            if (this.x8LinesSettingDetailView == null) {
                this.x8LinesSettingStub.inflate();
                this.x8LinesSettingDetailView = (PercentLinearLayout) this.rootView.findViewById(R.id.rl_main_camera_otherSetting_layout_grid);
                this.layoutReturn = (RelativeLayout) this.rootView.findViewById(R.id.x8_lines_layout_top);
                this.x8CameraOtherImgReturn = (ImageView) this.rootView.findViewById(R.id.img_return);
                this.tvLinesSettingNone = (TextView) this.rootView.findViewById(R.id.x8_lines_layout_none);
                this.tvLinesSettingCenterPoint = (TextView) this.rootView.findViewById(R.id.x8_lines_layout_center_point);
                this.tvLinesSettingNineLines = (TextView) this.rootView.findViewById(R.id.x8_lines_layout_nine_lines);
                this.tvLinesSettingDiagonalAndNine = (TextView) this.rootView.findViewById(R.id.x8_lines_layout_diagonal_and_nine);
                this.x8CameraOtherImgReturn.setOnClickListener(this);
                this.tvLinesSettingNone.setOnClickListener(this);
                this.tvLinesSettingCenterPoint.setOnClickListener(this);
                this.tvLinesSettingNineLines.setOnClickListener(this);
                this.tvLinesSettingDiagonalAndNine.setOnClickListener(this);
            }
            this.x8LinesSettingDetailView.setVisibility(0);
            this.x8OtherSettingMainLayout.setVisibility(8);
            setLinesSelect();
        } else if (i == R.id.img_return) {
            this.x8LinesSettingDetailView.setVisibility(8);
            this.x8OtherSettingMainLayout.setVisibility(0);
            this.tvCurrLinesSetting.setText(getCurrLinesSettingStr());
            SPStoreManager.getInstance().saveInt(Constants.X8_GLINE_LINE_OPTION, this.linesSelectIdx);
            GlobalConfig.getInstance().setGridLine(this.linesSelectIdx);
        } else if (i == R.id.x8_lines_layout_none) {
            this.linesSelectIdx = 0;
            setLinesSelect();
        } else if (i == R.id.x8_lines_layout_center_point) {
            this.linesSelectIdx = 1;
            setLinesSelect();
        } else if (i == R.id.x8_lines_layout_nine_lines) {
            this.linesSelectIdx = 2;
            setLinesSelect();
        } else if (i == R.id.x8_lines_layout_diagonal_and_nine) {
            this.linesSelectIdx = 3;
            setLinesSelect();
        } else if (i == R.id.x8_sdcard_format_layout) {
            if (CameraParamStatus.modelStatus != CameraModelStatus.recording) {
                this.dialog = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_sdcard_format_title), this.context.getString(R.string.x8_sdcard_format_tip), new onDialogButtonClickListener() {
                    public void onLeft() {
                        X8CameraOtherSettingController.this.dialog.dismiss();
                    }

                    public void onRight() {
                        X8CameraOtherSettingController.this.cameraManager.formatTFCard(new JsonUiCallBackListener() {
                            public void onComplete(JSONObject rt, Object o) {
                                if (rt != null && rt.getIntValue("rval") >= 0) {
                                    Toast.makeText(X8CameraOtherSettingController.this.context, X8CameraOtherSettingController.this.context.getResources().getString(R.string.x8_sdcard_format_rt), 0).show();
                                }
                            }
                        });
                        X8CameraOtherSettingController.this.dialog.dismiss();
                    }
                });
                this.dialog.show();
            }
        } else if (i == R.id.x8_camera_reset_layout && CameraParamStatus.modelStatus != CameraModelStatus.recording) {
            this.dialog = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_camera_reset_title), this.context.getString(R.string.x8_camera_reset_tip), new onDialogButtonClickListener() {
                public void onLeft() {
                    X8CameraOtherSettingController.this.dialog.dismiss();
                }

                public void onRight() {
                    X8CameraOtherSettingController.this.cameraManager.defaltSystem(new JsonUiCallBackListener() {
                        public void onComplete(JSONObject rt, Object o) {
                            if (rt != null && rt.getIntValue("rval") >= 0) {
                                Toast.makeText(X8CameraOtherSettingController.this.context, X8CameraOtherSettingController.this.context.getResources().getString(R.string.x8_camera_reset_rt), 0).show();
                                StateManager.getInstance().getCamera().setToken(-1);
                            }
                        }
                    });
                    X8CameraOtherSettingController.this.dialog.dismiss();
                }
            });
            this.dialog.show();
        }
    }

    private void setLinesSelect() {
        this.tvLinesSettingNone.setTextColor(this.linesSelectIdx == 0 ? this.context.getResources().getColor(R.color.x8_fc_all_setting_blue) : this.context.getResources().getColor(R.color.white_100));
        this.tvLinesSettingCenterPoint.setTextColor(this.linesSelectIdx == 1 ? this.context.getResources().getColor(R.color.x8_fc_all_setting_blue) : this.context.getResources().getColor(R.color.white_100));
        this.tvLinesSettingNineLines.setTextColor(this.linesSelectIdx == 2 ? this.context.getResources().getColor(R.color.x8_fc_all_setting_blue) : this.context.getResources().getColor(R.color.white_100));
        this.tvLinesSettingDiagonalAndNine.setTextColor(this.linesSelectIdx == 3 ? this.context.getResources().getColor(R.color.x8_fc_all_setting_blue) : this.context.getResources().getColor(R.color.white_100));
        if (this.listener != null) {
            this.listener.onGridLineSelect(this.linesSelectIdx);
            GlobalConfig.getInstance().setGridLine(this.linesSelectIdx);
        }
    }

    private String getCurrLinesSettingStr() {
        switch (this.linesSelectIdx) {
            case 0:
                return this.context.getString(R.string.x8_general_grid_line_nothing);
            case 1:
                return this.context.getString(R.string.x8_center_point);
            case 2:
                return this.context.getString(R.string.x8_grid_nine);
            case 3:
                return this.context.getString(R.string.x8_grid_nine_and_diagonal);
            default:
                return null;
        }
    }

    public void setListener(IX8CameraMainSetListener listener) {
        this.listener = listener;
    }

    public void upSdcardStatus(AutoCameraStateADV stateADV) {
        if (stateADV.getInfo() == 3) {
            this.capacity_tv.setText("");
        } else if (stateADV != null && stateADV.isCardInfo()) {
            String freeSpace = NumberUtil.decimalPointStr((((double) stateADV.getFreeSpace()) / 1024.0d) / 1024.0d, 1);
            double freeCapacity = Double.valueOf(freeSpace).doubleValue();
            if (this.sdcardFree != freeCapacity) {
                this.sdcardFree = freeCapacity;
                this.capacity_tv.setText(freeSpace + "G/" + NumberUtil.decimalPointStr((((double) stateADV.getTotalSpace()) / 1024.0d) / 1024.0d, 1) + "G");
                upCapacityDes();
            }
        }
    }

    @SuppressLint({"StringFormatMatches"})
    private void upCapacityDes() {
        if (this.sdcardFree > 0.0d) {
            String free_sum = "";
            if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
                free_sum = String.format(this.context.getResources().getString(R.string.x8_sdcard_photo_free), new Object[]{Integer.valueOf(3)});
            } else if (CameraParamStatus.modelStatus == CameraModelStatus.record) {
                free_sum = String.format(this.context.getResources().getString(R.string.x8_sdcard_record_free), new Object[]{Integer.valueOf(6)});
            }
            this.sdcard_free_tv.setText(free_sum);
        }
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.clickAble != b) {
            this.clickAble = b;
        }
        if (!b) {
            updateViewEnable(false, this.resetView, this.formatView);
            this.capacity_tv.setText("");
            this.sdcardFree = 0.0d;
        } else if (CameraParamStatus.modelStatus == CameraModelStatus.recording || StateManager.getInstance().getCamera().isDelayedPhotography()) {
            updateViewEnable(false, this.resetView, this.formatView);
        } else {
            AutoCameraStateADV stateADV = StateManager.getInstance().getCamera().getAutoCameraStateADV();
            if (!(stateADV == null || stateADV.isCardInfo())) {
                if (stateADV.isNoTFCard()) {
                    updateViewEnable(false, this.formatView);
                } else {
                    updateViewEnable(true, this.formatView);
                }
            }
            updateViewEnable(true, this.resetView);
        }
    }

    private void updateViewEnable(boolean enable, ViewGroup... parent) {
        if (parent != null && parent.length > 0) {
            for (ViewGroup group : parent) {
                group.setEnabled(enable);
                int len = group.getChildCount();
                for (int j = 0; j < len; j++) {
                    View subView = group.getChildAt(j);
                    if (subView instanceof ViewGroup) {
                        updateViewEnable(enable, (ViewGroup) subView);
                    } else {
                        subView.setEnabled(enable);
                    }
                }
            }
        }
    }

    public boolean onClickBackKey() {
        return false;
    }
}
