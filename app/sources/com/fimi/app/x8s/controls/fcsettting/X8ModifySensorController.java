package com.fimi.app.x8s.controls.fcsettting;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetIMUInfo;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8ModifySensorController extends AbsX8MenuBoxControllers {
    private ImageView back_btn;
    private FcCtrlManager fcManager;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            X8ModifySensorController.this.updateView();
            X8ModifySensorController.this.mHandler.sendEmptyMessageDelayed(0, 500);
        }
    };
    private IX8GeneraModifyModeControllerListener modeControllerListener;
    private TextView tvAccelMode;
    private TextView tvAccelMode2;
    private TextView tvAccelX;
    private TextView tvAccelX2;
    private TextView tvAccelY;
    private TextView tvAccelY2;
    private TextView tvAccelZ;
    private TextView tvAccelZ2;
    private TextView tvGyroX;
    private TextView tvGyroX2;
    private TextView tvGyroY;
    private TextView tvGyroY2;
    private TextView tvGyroZ;
    private TextView tvGyroZ2;
    private TextView tvGyroxMode;
    private TextView tvGyroxMode2;
    private TextView tvMagMode;
    private TextView tvMagX;
    private TextView tvMagY;
    private TextView tvMagZ;

    public void setFcManager(FcCtrlManager fcManager) {
        this.fcManager = fcManager;
    }

    public void setModeControllerListener(IX8GeneraModifyModeControllerListener modeControllerListener) {
        this.modeControllerListener = modeControllerListener;
    }

    public X8ModifySensorController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.x8_rl_main_mdify_sensor_layout);
        this.back_btn = (ImageView) this.handleView.findViewById(R.id.btn_return);
        this.tvGyroX = (TextView) this.handleView.findViewById(R.id.gyro_x);
        this.tvGyroY = (TextView) this.handleView.findViewById(R.id.gyro_y);
        this.tvGyroZ = (TextView) this.handleView.findViewById(R.id.gyro_z);
        this.tvAccelX = (TextView) this.handleView.findViewById(R.id.accel_x);
        this.tvAccelY = (TextView) this.handleView.findViewById(R.id.accel_y);
        this.tvAccelZ = (TextView) this.handleView.findViewById(R.id.accel_z);
        this.tvMagX = (TextView) this.handleView.findViewById(R.id.magx_x);
        this.tvMagY = (TextView) this.handleView.findViewById(R.id.magx_y);
        this.tvMagZ = (TextView) this.handleView.findViewById(R.id.magx_z);
        this.tvGyroxMode = (TextView) this.handleView.findViewById(R.id.gyro_mode);
        this.tvAccelMode = (TextView) this.handleView.findViewById(R.id.accel_mode);
        this.tvMagMode = (TextView) this.handleView.findViewById(R.id.magx_mode);
        this.tvGyroX2 = (TextView) this.handleView.findViewById(R.id.gyro2_x);
        this.tvGyroY2 = (TextView) this.handleView.findViewById(R.id.gyro2_y);
        this.tvGyroZ2 = (TextView) this.handleView.findViewById(R.id.gyro2_z);
        this.tvAccelX2 = (TextView) this.handleView.findViewById(R.id.accel2_x);
        this.tvAccelY2 = (TextView) this.handleView.findViewById(R.id.accel2_y);
        this.tvAccelZ2 = (TextView) this.handleView.findViewById(R.id.accel2_z);
        this.tvGyroxMode2 = (TextView) this.handleView.findViewById(R.id.gyro2_mode);
        this.tvAccelMode2 = (TextView) this.handleView.findViewById(R.id.accel2_mode);
    }

    public void initActions() {
        this.back_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                X8ModifySensorController.this.handleView.setVisibility(8);
                X8ModifySensorController.this.modeControllerListener.returnBack();
                X8ModifySensorController.this.closeItem();
            }
        });
    }

    public void defaultVal() {
        String na = "N/A";
        this.tvGyroX.setText(na);
        this.tvGyroY.setText(na);
        this.tvGyroZ.setText(na);
        this.tvAccelX.setText(na);
        this.tvAccelY.setText(na);
        this.tvAccelZ.setText(na);
        this.tvMagX.setText(na);
        this.tvMagY.setText(na);
        this.tvMagZ.setText(na);
        this.tvGyroxMode.setText(na);
        this.tvAccelMode.setText(na);
        this.tvMagMode.setText(na);
        this.tvGyroX2.setText(na);
        this.tvGyroY2.setText(na);
        this.tvGyroZ2.setText(na);
        this.tvAccelX2.setText(na);
        this.tvAccelY2.setText(na);
        this.tvAccelZ2.setText(na);
        this.tvGyroxMode2.setText(na);
        this.tvAccelMode2.setText(na);
    }

    private void updateView() {
        if (StateManager.getInstance().getX8Drone().isInSky() || !StateManager.getInstance().getConectState().isConnectDrone()) {
            defaultVal();
        } else if (this.fcManager != null) {
            this.fcManager.getIUMInfo(1, new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    AckGetIMUInfo imuInfo = (AckGetIMUInfo) o;
                    if (imuInfo != null) {
                        X8ModifySensorController.this.tvGyroX.setText(String.valueOf(((float) imuInfo.getGyroX()) / 100.0f));
                        X8ModifySensorController.this.tvGyroY.setText(String.valueOf(((float) imuInfo.getGyroY()) / 100.0f));
                        X8ModifySensorController.this.tvGyroZ.setText(String.valueOf(((float) imuInfo.getGyroZ()) / 100.0f));
                        X8ModifySensorController.this.tvAccelX.setText(String.valueOf(((float) imuInfo.getAccelX()) / 100.0f));
                        X8ModifySensorController.this.tvAccelY.setText(String.valueOf(((float) imuInfo.getAccelY()) / 100.0f));
                        X8ModifySensorController.this.tvAccelZ.setText(String.valueOf(((float) imuInfo.getAccelZ()) / 100.0f));
                        X8ModifySensorController.this.tvMagX.setText(String.valueOf(((float) imuInfo.getMagX()) / 100.0f));
                        X8ModifySensorController.this.tvMagY.setText(String.valueOf(((float) imuInfo.getMagY()) / 100.0f));
                        X8ModifySensorController.this.tvMagZ.setText(String.valueOf(((float) imuInfo.getMagZ()) / 100.0f));
                        int accelMode = (int) Math.sqrt((Math.pow((double) imuInfo.getAccelX(), 2.0d) + Math.pow((double) imuInfo.getAccelY(), 2.0d)) + Math.pow((double) imuInfo.getAccelZ(), 2.0d));
                        int magMode = (int) Math.sqrt((Math.pow((double) imuInfo.getMagX(), 2.0d) + Math.pow((double) imuInfo.getMagY(), 2.0d)) + Math.pow((double) imuInfo.getMagZ(), 2.0d));
                        X8ModifySensorController.this.tvGyroxMode.setText(String.valueOf(((float) ((int) Math.sqrt((Math.pow((double) imuInfo.getGyroY(), 2.0d) + Math.pow((double) imuInfo.getGyroX(), 2.0d)) + Math.pow((double) imuInfo.getGyroZ(), 2.0d)))) / 100.0f));
                        X8ModifySensorController.this.tvAccelMode.setText(String.valueOf(((float) accelMode) / 100.0f));
                        X8ModifySensorController.this.tvMagMode.setText(String.valueOf(((float) magMode) / 100.0f));
                    }
                }
            });
            this.fcManager.getIUMInfo(2, new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    AckGetIMUInfo imuInfo = (AckGetIMUInfo) o;
                    if (imuInfo != null) {
                        X8ModifySensorController.this.tvGyroX2.setText(String.valueOf(((float) imuInfo.getGyroX()) / 100.0f));
                        X8ModifySensorController.this.tvGyroY2.setText(String.valueOf(((float) imuInfo.getGyroY()) / 100.0f));
                        X8ModifySensorController.this.tvGyroZ2.setText(String.valueOf(((float) imuInfo.getGyroZ()) / 100.0f));
                        X8ModifySensorController.this.tvAccelX2.setText(String.valueOf(((float) imuInfo.getAccelX()) / 100.0f));
                        X8ModifySensorController.this.tvAccelY2.setText(String.valueOf(((float) imuInfo.getAccelY()) / 100.0f));
                        X8ModifySensorController.this.tvAccelZ2.setText(String.valueOf(((float) imuInfo.getAccelZ()) / 100.0f));
                        int accelMode = (int) Math.sqrt((Math.pow((double) imuInfo.getAccelX(), 2.0d) + Math.pow((double) imuInfo.getAccelY(), 2.0d)) + Math.pow((double) imuInfo.getAccelZ(), 2.0d));
                        X8ModifySensorController.this.tvGyroxMode2.setText(String.valueOf(((float) ((int) Math.sqrt((Math.pow((double) imuInfo.getGyroY(), 2.0d) + Math.pow((double) imuInfo.getGyroX(), 2.0d)) + Math.pow((double) imuInfo.getGyroZ(), 2.0d)))) / 100.0f));
                        X8ModifySensorController.this.tvAccelMode2.setText(String.valueOf(((float) accelMode) / 100.0f));
                    }
                }
            });
        }
    }

    public void showItem() {
        super.showItem();
        this.handleView.setVisibility(0);
        this.mHandler.sendEmptyMessageDelayed(0, 10);
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
    }

    public void closeItem() {
        super.closeItem();
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
        }
    }
}
