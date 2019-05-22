package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.fimi.TcpClient;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController.LineModel;
import com.fimi.app.x8s.entity.X8AilinePrameter;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.manager.X8MapGetCityManager;
import com.fimi.app.x8s.map.interfaces.AbsAiLineManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8TabHost;
import com.fimi.app.x8s.widget.X8TabHost.OnSelectListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.widget.SwitchButton;
import com.fimi.widget.SwitchButton.OnSwitchListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction.Cmd;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.map.MapType;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.GpsCorrect;
import java.util.ArrayList;
import java.util.List;

public class X8AiLinesExcuteConfirmUi implements OnClickListener, OnSeekBarChangeListener, OnSwitchListener {
    private float DEFAULE_SPEED = 2.0f;
    private float MAX = 10.0f;
    private int MAX_PROGRESS = ((int) ((this.MAX - this.MIN) * 10.0f));
    private float MIN = 1.0f;
    private Button btnGo;
    private View contentView;
    private X8AiLinePointInfo dataByHistory;
    private float distance = 0.0f;
    private FcManager fcManager;
    int i = 4;
    private ImageView imgBack;
    boolean isInSky = StateManager.getInstance().getX8Drone().isInSky();
    private IX8NextViewListener listener;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (X8AiLinesExcuteConfirmUi.this.i < X8AiLinesExcuteConfirmUi.this.mapPointList.size()) {
                X8AiLinesExcuteConfirmUi.this.mHandler.sendEmptyMessageDelayed(0, 2000);
                AbsAiLineManager aiLineManager = X8AiLinesExcuteConfirmUi.this.mapVideoController.getFimiMap().getAiLineManager();
                X8AiLinesExcuteConfirmUi x8AiLinesExcuteConfirmUi = X8AiLinesExcuteConfirmUi.this;
                int i = x8AiLinesExcuteConfirmUi.i;
                x8AiLinesExcuteConfirmUi.i = i + 1;
                aiLineManager.setAiLineIndexPoint(i);
            }
        }
    };
    private SaveData mSaveData = new SaveData(this, null);
    private SwitchButton mSbCurve;
    private SwitchButton mSbRecord;
    private X8AiLineExcuteController mX8AiLineExcuteController;
    private X8AilinePrameter mX8AilinePrameter;
    private List<MapPointLatLng> mapPointList;
    private X8MapVideoController mapVideoController;
    private LineModel model;
    private SeekBar sbSeekBar;
    private int successCount = 0;
    private X8TabHost tbDisconnect;
    private X8TabHost tbEnd;
    private TextView tvDistance;
    private X8TabHost tvOrientation1;
    private X8TabHost tvOrientation2;
    private TextView tvPointSize;
    private TextView tvSpeed;
    private TextView tvTime;
    private View vMinus;
    private View vPlus;

    private class SaveData {
        public int disConnectType;
        public int endType;
        List<X8AiLinePointLatlngInfo> list;
        public int orientation;
        public int speed;

        private SaveData() {
        }

        /* synthetic */ SaveData(X8AiLinesExcuteConfirmUi x0, AnonymousClass1 x1) {
            this();
        }
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8MapVideoController mapVideoController, X8AilinePrameter mX8AilinePrameter, X8AiLineExcuteController mX8AiLineExcuteController) {
        this.listener = listener;
        this.fcManager = fcManager;
        this.mapVideoController = mapVideoController;
        this.mX8AilinePrameter = mX8AilinePrameter;
        this.mX8AiLineExcuteController = mX8AiLineExcuteController;
    }

    public X8AiLinesExcuteConfirmUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_excute_confirm_layout, (ViewGroup) parent, true);
        initView(this.contentView);
        initAction();
    }

    public void initView(View rootView) {
        this.imgBack = (ImageView) rootView.findViewById(R.id.img_ai_follow_return);
        this.tvDistance = (TextView) rootView.findViewById(R.id.tv_ai_follow_distance);
        this.tvTime = (TextView) rootView.findViewById(R.id.tv_ai_follow_time);
        this.tvSpeed = (TextView) rootView.findViewById(R.id.tv_ai_follow_speed);
        this.tvPointSize = (TextView) rootView.findViewById(R.id.tv_ai_follow_size);
        this.vMinus = rootView.findViewById(R.id.rl_minus);
        this.sbSeekBar = (SeekBar) rootView.findViewById(R.id.sb_value);
        this.vPlus = rootView.findViewById(R.id.rl_plus);
        this.btnGo = (Button) rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.tvOrientation1 = (X8TabHost) rootView.findViewById(R.id.x8_ai_line_rorate1);
        this.tvOrientation1.setOnSelectListener(new OnSelectListener() {
            public void onSelect(int index, String text, int last) {
                if (index == last) {
                    return;
                }
                if (index == 1) {
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setOrientation(2);
                    if (X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController != null && X8AiLinesExcuteConfirmUi.this.dataByHistory == null) {
                        X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController.getmX8AiLineInterestPointController().showView(false);
                    }
                    X8AiLinesExcuteConfirmUi.this.changeOrientation(2);
                    return;
                }
                X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setOrientation(0);
                if (X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController != null && X8AiLinesExcuteConfirmUi.this.dataByHistory == null) {
                    X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController.getmX8AiLineInterestPointController().showView(true);
                }
                X8AiLinesExcuteConfirmUi.this.changeOrientation(0);
            }
        });
        this.tvOrientation2 = (X8TabHost) rootView.findViewById(R.id.x8_ai_line_rorate2);
        this.tvOrientation2.setOnSelectListener(new OnSelectListener() {
            public void onSelect(int index, String text, int last) {
                if (index != last) {
                    if (index == 2) {
                        index = 1;
                    } else if (index == 1) {
                        index = 2;
                    }
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setOrientation(index);
                    X8AiLinesExcuteConfirmUi.this.changeOrientationForVideo(index);
                }
            }
        });
        this.tbDisconnect = (X8TabHost) rootView.findViewById(R.id.tb_disconnect);
        this.tbDisconnect.setOnSelectListener(new OnSelectListener() {
            public void onSelect(int index, String text, int last) {
                if (index != last) {
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setDisconnectActioin(index);
                }
            }
        });
        this.tbEnd = (X8TabHost) rootView.findViewById(R.id.tb_ai_excute_end);
        this.tbEnd.setOnSelectListener(new OnSelectListener() {
            public void onSelect(int index, String text, int last) {
                if (index != last) {
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setEndAction(index);
                }
            }
        });
        this.mSbRecord = (SwitchButton) rootView.findViewById(R.id.sb_ai_auto_record);
        this.mSbCurve = (SwitchButton) rootView.findViewById(R.id.swb_ai_curve);
        this.sbSeekBar.setMax(this.MAX_PROGRESS);
        this.mSbCurve.setEnabled(true);
        this.mSbRecord.setEnabled(true);
        if (!this.isInSky) {
            this.btnGo.setText(rootView.getContext().getString(R.string.x8_ai_fly_line_save));
        }
    }

    public void changeOrientation(int orientation) {
        int size = this.mapPointList.size();
        for (int i = 0; i < size; i++) {
            MapPointLatLng point = (MapPointLatLng) this.mapPointList.get(i);
            point.yawMode = orientation;
            if (orientation == 0) {
                point.setAngle(0.0f);
            } else {
                float angle;
                if (i == 0) {
                    angle = getAngle(this.mapVideoController.getFimiMap().getDeviceLatlng(), point);
                } else {
                    angle = getAngle((MapPointLatLng) this.mapPointList.get(i - 1), point);
                }
                point.setAngle(angle);
            }
        }
        this.mapVideoController.getFimiMap().getAiLineManager().clearAllSmallMarker();
        if (orientation != 0) {
            this.mapVideoController.getFimiMap().getAiLineManager().addSmallMarkerByMap(0);
        }
    }

    public void changeOrientationForVideo(int orientation) {
        int size = this.mapPointList.size();
        for (int i = 0; i < size; i++) {
            MapPointLatLng point = (MapPointLatLng) this.mapPointList.get(i);
            point.yawMode = orientation;
            if (orientation == 0) {
                point.showAngle = 0.0f;
            } else if (orientation == 2) {
                float angle;
                if (i == 0) {
                    angle = getAngle(this.mapVideoController.getFimiMap().getDeviceLatlng(), point);
                } else {
                    angle = getAngle((MapPointLatLng) this.mapPointList.get(i - 1), point);
                }
                point.showAngle = angle;
            } else {
                point.showAngle = point.angle;
            }
        }
        if (orientation == 0) {
            this.mapVideoController.getFimiMap().getAiLineManager().clearAllSmallMarker();
        } else if (orientation == 1) {
            this.mapVideoController.getFimiMap().getAiLineManager().addOrUpdateSmallMarkerForVideo(1);
        } else {
            this.mapVideoController.getFimiMap().getAiLineManager().addOrUpdateSmallMarkerForVideo(2);
        }
    }

    public void initAction() {
        this.imgBack.setOnClickListener(this);
        this.tvDistance.setOnClickListener(this);
        this.tvTime.setOnClickListener(this);
        this.tvSpeed.setOnClickListener(this);
        this.vMinus.setOnClickListener(this);
        this.sbSeekBar.setOnSeekBarChangeListener(this);
        this.vPlus.setOnClickListener(this);
        this.btnGo.setOnClickListener(this);
        this.mSbCurve.setOnSwitchListener(this);
        this.mSbRecord.setOnSwitchListener(new OnSwitchListener() {
            public void onSwitch(View view, boolean on) {
                if (on) {
                    X8AiLinesExcuteConfirmUi.this.mSbRecord.setSwitchState(false);
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setAutoRecorde(0);
                    return;
                }
                X8AiLinesExcuteConfirmUi.this.mSbRecord.setSwitchState(true);
                X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setAutoRecorde(1);
            }
        });
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_minus) {
            if (this.sbSeekBar.getProgress() != 0) {
                this.sbSeekBar.setProgress(this.sbSeekBar.getProgress() - 10);
            }
        } else if (id == R.id.rl_plus) {
            if (this.sbSeekBar.getProgress() != this.MAX_PROGRESS) {
                this.sbSeekBar.setProgress(this.sbSeekBar.getProgress() + 10);
            }
        } else if (id == R.id.img_ai_follow_return) {
            this.listener.onBackClick();
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            startAiLineSetPoint();
        }
    }

    private void startAiLineSetPoint() {
        int i;
        int orientation = 0;
        int compeletEvent = 0;
        int disconnectEvent = 0;
        if (this.tvOrientation1.getVisibility() == 0) {
            i = this.tvOrientation1.getSelectIndex();
            if (i == 0) {
                orientation = 0;
            } else if (i == 1) {
                orientation = 2;
            }
        }
        if (this.tvOrientation2.getVisibility() == 0) {
            i = this.tvOrientation2.getSelectIndex();
            if (i == 0) {
                orientation = 0;
            } else if (i == 1) {
                orientation = 2;
            } else if (i == 2) {
                orientation = 1;
            }
        }
        if (this.tbDisconnect.getSelectIndex() == 0) {
            disconnectEvent = 0;
        } else if (this.tbDisconnect.getSelectIndex() == 1) {
            disconnectEvent = 1;
        }
        if (this.tbEnd.getSelectIndex() == 0) {
            compeletEvent = 0;
        } else if (this.tbEnd.getSelectIndex() == 1) {
            compeletEvent = 4;
        }
        this.successCount = 0;
        int s = (int) (10.0f * (this.MIN + (((float) this.sbSeekBar.getProgress()) / 10.0f)));
        int size = this.mapPointList.size();
        List<CmdAiLinePoints> mCmdAiLinePoints = new ArrayList();
        List<X8AiLinePointLatlngInfo> list = new ArrayList();
        for (i = 0; i < size; i++) {
            MapPointLatLng point = (MapPointLatLng) this.mapPointList.get(i);
            CmdAiLinePoints cmdPoint = new CmdAiLinePoints();
            FLatLng mFlatlng = GpsCorrect.Mars_To_Earth0(point.latitude, point.longitude);
            if (point.mInrertestPoint != null) {
                cmdPoint.pioEnbale = 1;
            } else {
                cmdPoint.pioEnbale = 0;
            }
            float angle = point.showAngle;
            if (orientation == 1) {
                angle = point.angle;
            }
            point.yawMode = orientation;
            int altitude = ((int) point.altitude) * 10;
            X8AiLinePointLatlngInfo mLatlngInfo = new X8AiLinePointLatlngInfo();
            mLatlngInfo.setNumber(i);
            mLatlngInfo.setTotalnumber(size);
            mLatlngInfo.setLongitude(point.longitude);
            mLatlngInfo.setLatitude(point.latitude);
            mLatlngInfo.setAltitude((int) point.altitude);
            mLatlngInfo.setGimbalPitch(point.gimbalPitch);
            mLatlngInfo.setYaw(angle);
            mLatlngInfo.setSpeed(s);
            mLatlngInfo.setYawMode(point.yawMode);
            mLatlngInfo.setPointActionCmd(point.action);
            mLatlngInfo.setRoration(point.roration);
            if (point.mInrertestPoint != null) {
                mLatlngInfo.setLongitudePOI(point.mInrertestPoint.longitude);
                mLatlngInfo.setLatitudePOI(point.mInrertestPoint.latitude);
                mLatlngInfo.setAltitudePOI((int) point.mInrertestPoint.altitude);
            }
            list.add(mLatlngInfo);
            cmdPoint.speed = s;
            cmdPoint.angle = switchScreenAngle2DroneAngle(angle);
            cmdPoint.gimbalPitch = point.gimbalPitch;
            cmdPoint.orientation = point.yawMode;
            cmdPoint.rotation = point.roration;
            cmdPoint.latitude = mFlatlng.latitude;
            cmdPoint.longitude = mFlatlng.longitude;
            cmdPoint.altitude = altitude;
            cmdPoint.count = size;
            cmdPoint.nPos = i;
            cmdPoint.autoRecord = this.mSbRecord.getToggleOn() ? 1 : 0;
            MapPointLatLng pointInterest = point.mInrertestPoint;
            if (pointInterest != null) {
                FLatLng mFlatlngInterest = GpsCorrect.Mars_To_Earth0(pointInterest.latitude, pointInterest.longitude);
                cmdPoint.latitudePIO = mFlatlngInterest.latitude;
                cmdPoint.longitudePIO = mFlatlngInterest.longitude;
                cmdPoint.altitudePIO = ((int) pointInterest.altitude) * 10;
            }
            cmdPoint.disconnectEvent = disconnectEvent;
            cmdPoint.compeletEvent = compeletEvent;
            mCmdAiLinePoints.add(cmdPoint);
        }
        this.mSaveData.orientation = orientation;
        this.mSaveData.speed = s;
        this.mSaveData.disConnectType = disconnectEvent;
        this.mSaveData.endType = compeletEvent;
        this.mSaveData.list = list;
        if (this.isInSky) {
            sendPoint(mCmdAiLinePoints, size);
            return;
        }
        saveData(this.mSaveData.orientation, this.mSaveData.speed, this.mSaveData.disConnectType, this.mSaveData.endType, list);
        if (this.dataByHistory == null) {
            X8ToastUtil.showToast(this.contentView.getContext(), "" + this.contentView.getContext().getString(R.string.x8_ai_fly_line_save_tip), 1);
        }
        this.listener.onSaveClick();
    }

    public void sendPoint(List<CmdAiLinePoints> mCmdAiLinePoints, int size) {
        if (mCmdAiLinePoints.size() > 0) {
            sendPointCmdOneByOne(mCmdAiLinePoints, 0, size);
        }
    }

    public void sendPointCmdOneByOne(final List<CmdAiLinePoints> mCmdAiLinePoints, final int index, final int size) {
        this.fcManager.setAiLinePoints((CmdAiLinePoints) mCmdAiLinePoints.get(index), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    X8ToastUtil.showToast(X8AiLinesExcuteConfirmUi.this.contentView.getContext(), "sendCmdOneByOne ERROR", 0);
                } else if (index == size - 1) {
                    X8AiLinesExcuteConfirmUi.this.setPointsAction();
                } else {
                    X8AiLinesExcuteConfirmUi.this.sendPointCmdOneByOne(mCmdAiLinePoints, index + 1, size);
                }
            }
        });
    }

    public void sendActionCmdOneByOne() {
    }

    public void testUI() {
        this.mapVideoController.getFimiMap().getAiLineManager().startAiLineProcess();
        this.mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    public float getAngle(MapPointLatLng from, MapPointLatLng to) {
        return this.mapVideoController.getFimiMap().getAiLineManager().getPointAngle(from, to);
    }

    public float switchScreenAngle2DroneAngle(float angle) {
        if (angle < 0.0f) {
            return angle;
        }
        if (0.0f > angle || angle > 180.0f) {
            angle -= 360.0f;
        }
        return angle;
    }

    public void saveData(int orientation, int speed, int disconnectType, int endType, List<X8AiLinePointLatlngInfo> list) {
        int i = 1;
        if (this.dataByHistory == null) {
            int i2;
            X8AiLinePointInfo lineInfo = new X8AiLinePointInfo();
            lineInfo.setTime(System.currentTimeMillis());
            lineInfo.setType(orientation);
            lineInfo.setSpeed(speed);
            lineInfo.setDistance(this.distance);
            if (this.mSbCurve.getToggleOn()) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            lineInfo.setIsCurve(i2);
            if (GlobalConfig.getInstance().getMapType() == MapType.AMap) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            lineInfo.setMapType(i2);
            if (this.model == LineModel.VEDIO) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            lineInfo.setRunByMapOrVedio(i2);
            lineInfo.setDisconnectType(disconnectType);
            lineInfo.setExcuteEnd(endType);
            lineInfo.setLocality(X8MapGetCityManager.locality);
            if (!this.mSbRecord.getToggleOn()) {
                i = 0;
            }
            lineInfo.setAutoRecord(i);
            X8AiLinePointInfoHelper.getIntance().addLineDatas(lineInfo, list);
        }
    }

    public void startExcute() {
        this.fcManager.setAiLineExcute(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiLinesExcuteConfirmUi.this.saveData(X8AiLinesExcuteConfirmUi.this.mSaveData.orientation, X8AiLinesExcuteConfirmUi.this.mSaveData.speed, X8AiLinesExcuteConfirmUi.this.mSaveData.disConnectType, X8AiLinesExcuteConfirmUi.this.mSaveData.endType, X8AiLinesExcuteConfirmUi.this.mSaveData.list);
                    X8AiLinesExcuteConfirmUi.this.listener.onExcuteClick();
                }
            }
        });
    }

    public void setDistance(float distance) {
        this.distance = distance;
        this.tvDistance.setText(X8NumberUtil.getDistanceNumberString((float) Math.round(distance), 0, true));
    }

    private void setSpeed(float speed) {
        this.sbSeekBar.setProgress((int) (10.0f * speed));
    }

    public void setViewValue() {
        float speed = this.MIN + (((float) this.sbSeekBar.getProgress()) / 10.0f);
        int time = Math.round(getAllTime(speed));
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(speed, 1, true));
        this.tvTime.setText("" + time + "S");
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float speed = this.MIN + (((float) progress) / 10.0f);
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(speed, 1, true));
        this.tvTime.setText("" + Math.round(getAllTime(speed)) + "S");
        if (this.mX8AilinePrameter != null) {
            this.mX8AilinePrameter.setSpeed(speed - 1.0f);
        }
    }

    public float getAllTime(float speed) {
        if (this.mapPointList == null) {
            return 0.0f;
        }
        int size = this.mapPointList.size();
        float time = 0.0f;
        for (int i = 0; i < size; i++) {
            float runTime;
            MapPointLatLng point2;
            if (i == 0) {
                point2 = (MapPointLatLng) this.mapPointList.get(i);
                runTime = getRunTime(point2.altitude, StateManager.getInstance().getX8Drone().getHeight(), this.mapVideoController.getFimiMap().getDeviceLatlng(), point2, speed);
            } else {
                MapPointLatLng point1 = (MapPointLatLng) this.mapPointList.get(i - 1);
                point2 = (MapPointLatLng) this.mapPointList.get(i);
                runTime = getRunTime(point2.altitude, point1.altitude, point1, point2, speed);
            }
            time += runTime;
        }
        return time;
    }

    public float getRunTime(float height, float alt, MapPointLatLng point1, MapPointLatLng point2, float speed) {
        float temp;
        boolean isUp;
        float distance = this.mapVideoController.getFimiMap().getAiLineManager().getDistance(point1, point2);
        if (height - alt >= 0.0f) {
            temp = height - alt;
            isUp = true;
        } else {
            temp = alt - height;
            isUp = false;
        }
        double radians = Math.toRadians((double) ((float) Math.toDegrees(Math.atan((double) (temp / distance)))));
        float sh = ((float) Math.cos(radians)) * speed;
        float sv = ((float) Math.sin(radians)) * speed;
        if (isUp) {
            if (sv > 4.0f) {
                sv = 4.0f;
            }
        } else if (sv > 3.0f) {
            sv = 3.0f;
        }
        float tv = temp / sv;
        float th = distance / sh;
        float time = th;
        if (tv > th) {
            return tv;
        }
        return time;
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onSwitch(View view, boolean on) {
        if (on) {
            this.mSbCurve.setSwitchState(false);
        } else {
            this.mSbCurve.setSwitchState(true);
        }
    }

    public void setPointSizeAndDistance(int aiLinePointSize, float aiLineDistance, List<MapPointLatLng> mapPointList, LineModel model) {
        setDistance(aiLineDistance);
        this.mapPointList = mapPointList;
        this.tvPointSize.setText("" + aiLinePointSize);
        setViewValue();
        this.model = model;
        if (model == LineModel.VEDIO) {
            this.tvOrientation2.setVisibility(0);
        } else {
            this.tvOrientation1.setVisibility(0);
        }
        setDat();
    }

    public void setDat() {
        boolean z = true;
        TcpClient.getIntance().sendLog("initView ......." + this.mX8AilinePrameter.toString());
        int orientation = this.mX8AilinePrameter.getOrientation();
        int disconnectEvent = this.mX8AilinePrameter.getDisconnectActioin();
        int endEvent = this.mX8AilinePrameter.getEndAction();
        int autoRecord = this.mX8AilinePrameter.getAutoRecorde();
        if (this.model == LineModel.VEDIO) {
            this.tvOrientation2.setVisibility(0);
            if (orientation == 0) {
                this.tvOrientation2.setSelect(0);
            } else if (orientation == 1) {
                this.tvOrientation2.setSelect(2);
            } else if (orientation == 2) {
                this.tvOrientation2.setSelect(1);
            }
        } else {
            this.tvOrientation1.setVisibility(0);
            TcpClient.getIntance().sendLog("orientation ......." + orientation);
            if (orientation == 0) {
                this.tvOrientation1.setSelect(0);
            } else if (orientation == 2) {
                TcpClient.getIntance().sendLog("orientation .....1111111111111.." + orientation);
                this.tvOrientation1.setSelect(1);
            }
        }
        TcpClient.getIntance().sendLog("disconnectEvent ......." + disconnectEvent);
        if (disconnectEvent == 0) {
            this.tbDisconnect.setSelect(0);
        } else if (disconnectEvent == 1) {
            this.tbDisconnect.setSelect(1);
        }
        TcpClient.getIntance().sendLog("endEvent ......." + endEvent);
        if (endEvent == 0) {
            this.tbEnd.setSelect(0);
        } else if (endEvent == 1) {
            this.tbEnd.setSelect(1);
        }
        SwitchButton switchButton = this.mSbRecord;
        if (autoRecord != 1) {
            z = false;
        }
        switchButton.setSwitchState(z);
        setSpeed(this.mX8AilinePrameter.getSpeed());
        setViewValue();
    }

    public void setDataByHistory(long lineId) {
        X8AiLinePointInfo lineInfo = X8AiLinePointInfoHelper.getIntance().getLineInfoById(lineId);
        if (lineInfo != null) {
            setDataByHistory(lineInfo);
        }
    }

    public void setDataByHistory(X8AiLinePointInfo info) {
        this.dataByHistory = info;
    }

    public void setPointsAction() {
        int size = this.mapPointList.size();
        this.successCount = 0;
        List<CmdAiLinePointsAction> list = new ArrayList();
        for (int i = 0; i < size; i++) {
            MapPointLatLng point = (MapPointLatLng) this.mapPointList.get(i);
            CmdAiLinePointsAction cmd = new CmdAiLinePointsAction();
            cmd.pos = i;
            cmd.count = size;
            switch (point.action) {
                case 1:
                    cmd.cmd0 = Cmd.HOVER.ordinal();
                    cmd.time = 10;
                    cmd.para0 = 1;
                    break;
                case 2:
                    cmd.cmd0 = Cmd.VIDEO.ordinal();
                    cmd.time = 10;
                    cmd.para0 = 1;
                    break;
                case 4:
                    cmd.cmd0 = Cmd.PHOTO.ordinal();
                    cmd.time = 1;
                    cmd.para0 = 1;
                    break;
                case 5:
                    cmd.cmd0 = Cmd.HOVER.ordinal();
                    cmd.cmd1 = Cmd.PHOTO.ordinal();
                    cmd.time = 5;
                    cmd.para0 = 1;
                    cmd.para1 = 1;
                    break;
                case 6:
                    cmd.cmd0 = Cmd.PHOTO.ordinal();
                    cmd.time = 1;
                    cmd.para0 = 3;
                    break;
                default:
                    break;
            }
            list.add(cmd);
        }
        if (list.size() > 0) {
            sendActionCmdOneByOne(list, 0, list.size());
        }
    }

    public void sendActionCmdOneByOne(final List<CmdAiLinePointsAction> list, final int index, final int size) {
        this.fcManager.setAiLinePointsAction((CmdAiLinePointsAction) list.get(index), new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    X8ToastUtil.showToast(X8AiLinesExcuteConfirmUi.this.contentView.getContext(), "setAiLinePointsAction ERROR", 0);
                } else if (index == size - 1) {
                    X8AiLinesExcuteConfirmUi.this.startExcute();
                } else {
                    X8AiLinesExcuteConfirmUi.this.sendActionCmdOneByOne(list, index + 1, size);
                }
            }
        });
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnGo.setEnabled(true);
        } else {
            this.btnGo.setEnabled(false);
        }
    }
}
