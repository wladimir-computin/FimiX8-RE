package com.fimi.app.x8s.controls;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.maps.model.LatLng;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.gimbal.X8GimbalAdjustController;
import com.fimi.app.x8s.enums.NoFlyZoneEnum;
import com.fimi.app.x8s.interfaces.IControllers;
import com.fimi.app.x8s.interfaces.IFimiFpvShot;
import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;
import com.fimi.app.x8s.interfaces.IX8GestureListener;
import com.fimi.app.x8s.interfaces.IX8MainTopBarListener;
import com.fimi.app.x8s.interfaces.IX8MapVideoControllerListerner;
import com.fimi.app.x8s.map.GaoDeMap;
import com.fimi.app.x8s.map.GglMap;
import com.fimi.app.x8s.map.interfaces.AbsFimiMap;
import com.fimi.app.x8s.map.model.MapPoint;
import com.fimi.app.x8s.media.FimiH264Video;
import com.fimi.app.x8s.media.IFrameDataListener;
import com.fimi.app.x8s.widget.X8MapVideoCardView;
import com.fimi.kernel.Constants;
import com.fimi.kernel.animutils.IOUtils;
import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.connect.datatype.JsonDataChanel;
import com.fimi.x8sdk.controller.VideoManager;
import com.fimi.x8sdk.dataparser.AckNoFlyNormal;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.listener.NoFlyLinstener;
import com.fimi.x8sdk.map.MapType;
import com.fimi.x8sdk.modulestate.StateManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class X8MapVideoController implements OnClickListener, IControllers, VideodDataListener, NoFlyLinstener {
    public static FileOutputStream outputStream;
    X8GimbalAdjustController adjustController;
    private FimiH264Video fimiVideoView;
    private int h;
    private boolean isPushDataToPlayer;
    private IX8MapVideoControllerListerner listener;
    AbsFimiMap mFimiMap;
    private X8MapVideoCardView mapVideoCardView;
    private int oldh;
    private int oldw;
    private RelativeLayout rlFullScreen;
    private RelativeLayout rlShotScreen;
    private RelativeLayout rlSmallScreen;
    private RelativeLayout rlSwitchScreen;
    private TextView tvVedioFrame;
    private VideoManager videoManager;
    private int w;

    public void setListener(IX8MainTopBarListener listener) {
        if (this.adjustController != null) {
            this.adjustController.setListener(listener);
        }
    }

    public X8MapVideoController(View rootView, Bundle savedInstanceState) {
        if (GlobalConfig.getInstance().getMapType() == MapType.AMap || Constants.isFactoryApp()) {
            this.mFimiMap = new GaoDeMap();
        } else {
            this.mFimiMap = new GglMap();
        }
        this.mFimiMap.setContext(rootView.getContext());
        this.mFimiMap.onCreate(rootView, savedInstanceState);
        initViews(rootView);
        initActions();
        this.mFimiMap.setUpMap();
    }

    public void setUpMap() {
        if (this.mFimiMap != null) {
            this.mFimiMap.setUpMap();
        }
    }

    public void addHome(double latitude, double longitude) {
        if (this.mFimiMap != null) {
            this.mFimiMap.setHomeLocation(latitude, longitude);
        }
    }

    public void initActions() {
        this.rlSwitchScreen.setOnClickListener(this);
        StateManager.getInstance().registerNoFlyListener(this);
    }

    public void openUi() {
    }

    public void closeUi() {
    }

    public void defaultVal() {
        if (this.mFimiMap != null) {
            this.mFimiMap.defaultMapValue();
        }
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void initViews(View rootView) {
        this.adjustController = new X8GimbalAdjustController(rootView);
        this.rlSwitchScreen = (RelativeLayout) rootView.findViewById(R.id.rl_switchscreen);
        this.rlFullScreen = (RelativeLayout) rootView.findViewById(R.id.rl_fullscreen);
        this.rlSmallScreen = (RelativeLayout) rootView.findViewById(R.id.rl_smallscreen);
        this.rlShotScreen = (RelativeLayout) rootView.findViewById(R.id.rl_fullscreen_shot);
        this.rlSmallScreen.setVisibility(0);
        this.mapVideoCardView = (X8MapVideoCardView) rootView.findViewById(R.id.cv_map_video);
        this.fimiVideoView = new FimiH264Video(rootView.getContext());
        this.tvVedioFrame = (TextView) rootView.findViewById(R.id.tv_vedio_frame);
        this.fimiVideoView.setmIFrameDataListener(new IFrameDataListener() {
            public void onCountFrame(int count, int remainder, int fpvSize) {
                X8MapVideoController.this.tvVedioFrame.setText(count + " / " + remainder + " / " + fpvSize + "/" + JsonDataChanel.testString);
            }
        });
        this.rlSmallScreen.addView(this.mFimiMap.getMapView());
        this.rlFullScreen.addView(this.fimiVideoView);
        this.videoManager = new VideoManager(this);
        try {
            outputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/zdy1.h264");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setSmallViewListener();
    }

    public AbsFimiMap getFimiMap() {
        return this.mFimiMap;
    }

    public void changeCamera() {
        if (!(this.mFimiMap instanceof GglMap) && (this.mFimiMap instanceof GaoDeMap)) {
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.rl_switchscreen) {
            switchMapVideo();
        }
    }

    public void switchMapVideo() {
        this.listener.switchMapVideo(this.mapVideoCardView.isFullVideo());
        this.mapVideoCardView.switchDrawingOrder(this.mFimiMap.getMapView());
    }

    public void setListener(IX8MapVideoControllerListerner listener) {
        this.listener = listener;
    }

    public void drawNoFlightZone(MapPoint mMapPoint) {
        this.mFimiMap.clearNoFlightZone();
        switch (mMapPoint.getNfzType()) {
            case 0:
                mMapPoint.setType(NoFlyZoneEnum.CIRCLE);
                this.mFimiMap.drawNoFlightZone(mMapPoint);
                return;
            case 1:
                mMapPoint.setType(NoFlyZoneEnum.CIRCLE);
                this.mFimiMap.drawNoFlightZone(mMapPoint);
                return;
            case 2:
                mMapPoint.setType(NoFlyZoneEnum.CANDY);
                this.mFimiMap.drawNoFlightZone(mMapPoint);
                return;
            case 3:
                mMapPoint.setType(NoFlyZoneEnum.IRREGULAR);
                this.mFimiMap.drawNoFlightZone(mMapPoint);
                return;
            default:
                this.mFimiMap.clearNoFlightZone();
                return;
        }
    }

    public void switchDrawingOrderForAiFollow() {
        this.mapVideoCardView.switchDrawingOrderForAiFollow();
        this.rlSwitchScreen.setVisibility(8);
    }

    public void disShowSmall() {
        this.mapVideoCardView.disShowSmall();
        this.rlSwitchScreen.setVisibility(8);
    }

    public void resetShow() {
        this.mapVideoCardView.resetShow();
        this.rlSwitchScreen.setVisibility(0);
    }

    public void onRawdataCallBack(byte[] data) {
        if (isPushDataToPlayer()) {
            this.fimiVideoView.onRawdataCallBack(data);
        }
    }

    public void writeData(byte[] cmd) {
        try {
            int len = cmd.length;
            byte[] bytes = new byte[14];
            System.arraycopy(cmd, 0, bytes, 0, 14);
            int pts_cur = ((((bytes[4] & 255) << 24) | ((bytes[5] & 255) << 16)) | ((bytes[6] & 255) << 8)) | (bytes[7] & 255);
            outputStream.write((ByteHexHelper.bytesToHexString(bytes) + " seq=" + (((bytes[2] & 255) << 8) | (bytes[3] & 255)) + " pts_cur=" + pts_cur + IOUtils.LINE_SEPARATOR_UNIX).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showNoFly(AckNoFlyNormal flyNormal) {
        MapPoint mMapPoint = new MapPoint();
        mMapPoint.setCenter(new LatLng(flyNormal.getCenter().latitude, flyNormal.getCenter().longitude));
        if (flyNormal.getPolygonShape() == 2) {
            mMapPoint.setA1(new LatLng(flyNormal.getA1().latitude, flyNormal.getA1().longitude));
            mMapPoint.setA2(new LatLng(flyNormal.getA2().latitude, flyNormal.getA2().longitude));
            mMapPoint.setB1(new LatLng(flyNormal.getB1().latitude, flyNormal.getB1().longitude));
            mMapPoint.setB2(new LatLng(flyNormal.getB2().latitude, flyNormal.getB2().longitude));
            mMapPoint.setC1(new LatLng(flyNormal.getC1().latitude, flyNormal.getC1().longitude));
            mMapPoint.setC2(new LatLng(flyNormal.getC2().latitude, flyNormal.getC2().longitude));
            mMapPoint.setD1(new LatLng(flyNormal.getD1().latitude, flyNormal.getD1().longitude));
            mMapPoint.setD2(new LatLng(flyNormal.getD2().latitude, flyNormal.getD2().longitude));
        }
        mMapPoint.setNfzType(flyNormal.getPolygonShape());
        mMapPoint.setLimitHight(flyNormal.getHeightLimit());
        mMapPoint.setRadius(flyNormal.getNoflyRadius());
        mMapPoint.getLatLngs().clear();
        if (flyNormal.getNumEudges() > 0 && flyNormal.getPolygonShape() == 3) {
            for (int i = 0; i < flyNormal.getNumEudges(); i++) {
                FLatLng latLng = (FLatLng) flyNormal.getPoints().get(i);
                mMapPoint.getLatLngs().add(new LatLng(latLng.latitude, latLng.longitude));
            }
        }
        drawNoFlightZone(mMapPoint);
    }

    public void showGridLine(int type) {
        this.fimiVideoView.showGridLine(type);
    }

    public void switchByPoint2PointMap() {
        this.mapVideoCardView.switchDrawingOrderForPoint2Point();
    }

    public void switchByAiLineMap() {
        this.mapVideoCardView.switchDrawingOrderForPoint2Point();
    }

    public void switchByAiLineVideo() {
        this.mapVideoCardView.switchDrawingOrderForAiLineVideo();
    }

    public void switchBySurroundMap() {
        this.mapVideoCardView.switchDrawingOrderForAiLineVideo();
    }

    public boolean isFullVideo() {
        return this.mapVideoCardView.isFullVideo();
    }

    public void switchDrawingOrderForGimbal() {
        this.mapVideoCardView.switchDrawingOrderForGimbal();
    }

    public FimiH264Video getVideoView() {
        return this.fimiVideoView;
    }

    public void showVideoBg(boolean b) {
        this.fimiVideoView.showVideoBg(b);
    }

    public void setX8GestureListener(IX8GestureListener listener) {
        this.fimiVideoView.getX8AiTrackContainterView().setX8GestureListener(listener);
    }

    public void onResume() {
        this.isPushDataToPlayer = true;
    }

    public void onPause() {
        this.isPushDataToPlayer = false;
    }

    public boolean isPushDataToPlayer() {
        return true;
    }

    public void snapshot(IFimiOnSnapshotReady callback) {
        this.mFimiMap.snapshot(callback);
    }

    public void setMapShot(Bitmap mapShot) {
        if (isFullVideo()) {
            this.rlSwitchScreen.setBackground(new BitmapDrawable(mapShot));
        } else {
            this.rlShotScreen.setBackground(new BitmapDrawable(mapShot));
        }
    }

    public void clearShotBitmap() {
        this.rlSwitchScreen.setBackground(null);
        this.rlShotScreen.setBackground(null);
    }

    public void fpvShot(IFimiFpvShot callback) {
        this.fimiVideoView.fpvShot(callback);
    }

    public void setFpvShot(Bitmap fpvShot) {
        if (isFullVideo()) {
            this.rlShotScreen.setBackground(new BitmapDrawable(fpvShot));
        } else {
            this.rlSwitchScreen.setBackground(new BitmapDrawable(fpvShot));
        }
    }

    public void setSmallViewListener() {
        this.rlSwitchScreen.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                X8MapVideoController.this.mapVideoCardView.changeSmallSize();
            }
        });
    }
}
