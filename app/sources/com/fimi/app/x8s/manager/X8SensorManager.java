package com.fimi.app.x8s.manager;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.fimi.app.x8s.entity.X8PressureGpsInfo;
import com.fimi.app.x8s.interfaces.IX8SensorListener;
import com.umeng.commonsdk.proguard.g;

public class X8SensorManager implements SensorEventListener {
    private IX8SensorListener listener;
    private Sensor mPressure = this.mSM.getDefaultSensor(6);
    private SensorManager mSM;
    private Sensor mSensor = this.mSM.getDefaultSensor(3);

    public X8SensorManager(Activity activity, IX8SensorListener listener) {
        this.listener = listener;
        this.mSM = (SensorManager) activity.getSystemService(g.aa);
        if (this.mPressure == null) {
            Log.i("istep", "您的手机不支持气压传感器，无法使用本软件功能");
        }
    }

    public void showAllSensor() {
        for (Sensor sensor : this.mSM.getSensorList(-1)) {
            Log.i("istep", "" + getTypeString(sensor));
        }
    }

    public void registerListener() {
        this.mSM.registerListener(this, this.mSensor, 3);
        if (this.mPressure != null) {
            this.mSM.registerListener(this, this.mPressure, 3);
        }
    }

    public void unRegisterListener() {
        this.mSM.unregisterListener(this, this.mSensor);
        if (this.mPressure != null) {
            this.mSM.unregisterListener(this, this.mPressure);
            X8PressureGpsInfo.getInstance().setHasPressure(false);
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 3) {
            this.listener.onSensorChanged(event.values[0]);
        } else if (event.sensor.getType() == 6) {
            X8PressureGpsInfo.getInstance().sethPa(event.values[0]);
            X8PressureGpsInfo.getInstance().setHasPressure(true);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private String getTypeString(Sensor s) {
        switch (s.getType()) {
            case 1:
                return "加速度传感器";
            case 2:
                return "磁场传感器";
            case 3:
                return "方向传感器";
            case 4:
                return "陀螺仪传感器";
            case 5:
                return "光线传感器";
            case 6:
                return "压力传感器";
            case 7:
                return "温度传感器";
            case 8:
                return "接近传感器";
            case 9:
                return "重力传感器";
            case 10:
                return "线性加速度传感器";
            case 11:
                return "旋转矢量传感器";
            case 12:
                return "相对湿度传感器";
            case 13:
                return "环境温度传感器";
            case 14:
                return "磁场传感器(未经校准)";
            case 15:
                return "游戏旋转矢量传感器";
            case 16:
                return "陀螺仪传感器(未经校准)";
            case 17:
                return "特殊动作触发传感器";
            case 18:
                return "步数探测传感器";
            case 19:
                return "步数计数传感器";
            case 20:
                return "地磁旋转矢量传感器";
            case 21:
                return "心率传感器";
            case 28:
                return "POSE_6DOF传感器";
            case 29:
                return "静止检测传感器";
            case 30:
                return "运动检测传感器";
            case 31:
                return "心跳传感器";
            case 34:
                return "低延迟身体检测传感器";
            case 35:
                return "加速度传感器(未经校准)";
            default:
                return "其它传感器" + s.getType();
        }
    }
}
