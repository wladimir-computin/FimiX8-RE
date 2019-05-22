package com.fimi.x8sdk.modulestate;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.x8sdk.dataparser.AckPanoramaPhotographType;
import com.fimi.x8sdk.dataparser.AckRightRoller;
import com.fimi.x8sdk.dataparser.AutoVcTracking;
import com.fimi.x8sdk.entity.ConectState;
import com.fimi.x8sdk.listener.CameraStateListener;
import com.fimi.x8sdk.listener.ConnectListener;
import com.fimi.x8sdk.listener.FcBatteryListener;
import com.fimi.x8sdk.listener.FcHeartListener;
import com.fimi.x8sdk.listener.FcSingalListener;
import com.fimi.x8sdk.listener.FcSportStateListener;
import com.fimi.x8sdk.listener.HomeInfoListener;
import com.fimi.x8sdk.listener.IX8ErrorCodeListener;
import com.fimi.x8sdk.listener.IX8PanoramicInformationListener;
import com.fimi.x8sdk.listener.IX8PowerListener;
import com.fimi.x8sdk.listener.IX8VcTrackListener;
import com.fimi.x8sdk.listener.NavigationStateListener;
import com.fimi.x8sdk.listener.NoFlyLinstener;
import com.fimi.x8sdk.listener.RcMatchStateListener;
import com.fimi.x8sdk.listener.RelayHeartListener;
import com.fimi.x8sdk.listener.RightRollerLinstener;

public class StateManager {
    private static final int ALL_CONNECTED_STATE = 0;
    private static final int BATTERYSTATE = 3;
    private static final int CHECK_CONNECT_MESSAGE = 1;
    private static final int PANORAMIC_INFORMATION = 5;
    private static final int RIGHT_ROLLER = 4;
    private static final int VC_TRACKING = 2;
    private static StateManager stateManager = new StateManager();
    private AckRightRoller ackRightRoller;
    private CameraState camera = new CameraState();
    CameraStateListener cameraStateListener = null;
    private ConectState conectState = new ConectState();
    ConnectListener connectListener = null;
    long cruTime = System.currentTimeMillis();
    long curTime = System.currentTimeMillis();
    private DroneState droneState = new DroneState();
    FcBatteryListener fcBatteryListeners = null;
    FcHeartListener fcHeartListener = null;
    FcSingalListener fcSingalListener = null;
    private GimbalState gimbalState = new GimbalState();
    HomeInfoListener homeInfoListeners = null;
    private boolean is4KResolution;
    IX8PanoramicInformationListener ix8PanoramicInformationListener;
    private ErrorCodeState mErrorCodeState = new ErrorCodeState();
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (StateManager.this.relayState.isConnect()) {
                        StateManager.this.onRelayProcess();
                        StateManager.this.onDroneProcess();
                        StateManager.this.onCameraProcess();
                        StateManager.this.onRcMatchProcess();
                    }
                    sendEmptyMessageDelayed(0, 500);
                    return;
                case 1:
                    StateManager.this.onConnectProcess((ConectState) msg.obj);
                    return;
                case 2:
                    if (StateManager.this.vcTrackListener != null) {
                        StateManager.this.vcTrackListener.onTracking((AutoVcTracking) msg.obj);
                        return;
                    }
                    return;
                case 3:
                    if (StateManager.this.powerListener != null && StateManager.this.droneState.getAutoFcHeart() != null) {
                        StateManager.this.powerListener.onPowerChange(StateManager.this.droneState.getAutoFcHeart().getPowerConRate());
                        return;
                    }
                    return;
                case 4:
                    if (StateManager.this.rightRollerLinstener != null) {
                        StateManager.this.rightRollerLinstener.changeDirection(StateManager.this.ackRightRoller);
                        return;
                    }
                    return;
                case 5:
                    if (StateManager.this.ix8PanoramicInformationListener != null) {
                        StateManager.this.ix8PanoramicInformationListener.onPanoramicInformationChange((AckPanoramaPhotographType) msg.obj);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    NavigationStateListener mNavigationStateListeners = null;
    private NfzState mNfzState = new NfzState();
    private VersionState mVersionState = new VersionState();
    IX8ErrorCodeListener mX8ErrorCodeListener;
    NoFlyLinstener noFlyLinstener = null;
    IX8PowerListener powerListener;
    private RCMatchState rcMatchState = new RCMatchState();
    RcMatchStateListener rcMatchStateListener;
    RelayHeartListener relayHeartListener = null;
    private RelayState relayState = new RelayState();
    RightRollerLinstener rightRollerLinstener;
    FcSportStateListener sportStateListeners = null;
    IX8VcTrackListener vcTrackListener;

    public GimbalState getGimbalState() {
        return this.gimbalState;
    }

    public NfzState getNfzState() {
        return this.mNfzState;
    }

    public VersionState getVersionState() {
        return this.mVersionState;
    }

    public ErrorCodeState getErrorCodeState() {
        return this.mErrorCodeState;
    }

    public RCMatchState getRcMatchState() {
        return this.rcMatchState;
    }

    private StateManager() {
    }

    public static StateManager getInstance() {
        return stateManager;
    }

    public DroneState getX8Drone() {
        if (this.droneState == null) {
            this.droneState = new DroneState();
        }
        return this.droneState;
    }

    public CameraState getCamera() {
        return this.camera;
    }

    public RelayState getRelayState() {
        return this.relayState;
    }

    public void onRelayProcess() {
        if (this.relayHeartListener != null && this.relayState.getRelayHeart() != null) {
            this.relayHeartListener.onRelayHeart(this.relayState.getRelayHeart());
        }
    }

    public void onCameraProcess() {
        if (this.cameraStateListener != null && this.camera.getAutoCameraStateADV() != null) {
            this.cameraStateListener.showCamState(this.camera.getAutoCameraStateADV());
        }
    }

    public void onConnectProcess(ConectState state) {
        this.conectState = state;
        if (this.connectListener != null) {
            this.connectListener.onConnectedState(state);
        }
    }

    public void setAckRightRoller(AckRightRoller ackRightRoller) {
        this.ackRightRoller = ackRightRoller;
        if (this.mHandler != null && System.currentTimeMillis() - this.cruTime > 500) {
            this.cruTime = System.currentTimeMillis();
            this.mHandler.sendEmptyMessage(4);
        }
    }

    public void onRcMatchProcess() {
        if (this.rcMatchStateListener != null) {
            this.rcMatchStateListener.showRcMatchState(this.rcMatchState);
        }
    }

    public void onDroneProcess() {
        if (this.droneState.isConnect()) {
            if (this.sportStateListeners != null) {
                this.sportStateListeners.showGimbalState(this.gimbalState);
                if (this.droneState.getFcSportState() != null) {
                    this.sportStateListeners.showSportState(this.droneState.getFcSportState());
                }
            }
            if (this.mNavigationStateListeners != null) {
                this.mNavigationStateListeners.onNavigationState(this.droneState);
            }
            if (!(this.noFlyLinstener == null || this.mNfzState.getAckNoFlyNormal() == null)) {
                this.noFlyLinstener.showNoFly(this.mNfzState.getAckNoFlyNormal());
            }
            if (!(this.fcHeartListener == null || this.droneState.getAutoFcHeart() == null || this.mErrorCodeState == null)) {
                this.fcHeartListener.onFcHeart(this.droneState.getAutoFcHeart(), this.mErrorCodeState.isLowPower());
            }
            if (!(this.fcBatteryListeners == null || this.droneState.getFcBatterState() == null)) {
                this.fcBatteryListeners.onBatteryListener(this.droneState.getFcBatterState());
            }
            if (!(this.homeInfoListeners == null || this.droneState.getHomeInfo() == null)) {
                this.homeInfoListeners.showHomeInfo(this.droneState.getHomeInfo());
            }
            if (!(this.fcSingalListener == null || this.droneState.getFcSingal() == null)) {
                this.fcSingalListener.showSingal(this.droneState.getFcSingal());
            }
            if (this.mX8ErrorCodeListener != null && this.mErrorCodeState != null) {
                this.mX8ErrorCodeListener.onErrorCode(this.mErrorCodeState.getErrooInfo());
                this.mX8ErrorCodeListener.cloudUnMountError(this.mErrorCodeState.unMountCloud());
            }
        }
    }

    public void setPowerListener(IX8PowerListener powerListener) {
        this.powerListener = powerListener;
    }

    public void setIx8PanoramicInformationListener(IX8PanoramicInformationListener ix8PanoramicInformationListener) {
        this.ix8PanoramicInformationListener = ix8PanoramicInformationListener;
    }

    public void removeAllListener() {
        this.sportStateListeners = null;
        this.mNavigationStateListeners = null;
        this.noFlyLinstener = null;
        this.fcHeartListener = null;
        this.fcBatteryListeners = null;
        this.homeInfoListeners = null;
        this.fcSingalListener = null;
        this.relayHeartListener = null;
        this.connectListener = null;
        this.cameraStateListener = null;
        this.vcTrackListener = null;
        this.rcMatchStateListener = null;
        this.powerListener = null;
        this.mX8ErrorCodeListener = this.mX8ErrorCodeListener;
        this.rightRollerLinstener = null;
        this.ix8PanoramicInformationListener = null;
    }

    public void registerRightRollerListener(RightRollerLinstener listener) {
        this.rightRollerLinstener = listener;
    }

    public void registerX8ErrorCodeListener(IX8ErrorCodeListener listener) {
        this.mX8ErrorCodeListener = listener;
    }

    public void registerVcTrackListener(IX8VcTrackListener listener) {
        this.vcTrackListener = listener;
    }

    public void registerSportState(FcSportStateListener sportStateListener) {
        this.sportStateListeners = sportStateListener;
    }

    public void registerNavigationStateListener(NavigationStateListener listener) {
        this.mNavigationStateListeners = listener;
    }

    public void registerNoFlyListener(NoFlyLinstener linstener) {
        this.noFlyLinstener = linstener;
    }

    public void registerListener(FcHeartListener listener) {
        this.fcHeartListener = listener;
    }

    public void registerFCBattery(FcBatteryListener listener) {
        this.fcBatteryListeners = listener;
    }

    public void registerRcMatchListener(RcMatchStateListener listener) {
        this.rcMatchStateListener = listener;
    }

    public void registerHomeListener(HomeInfoListener listener) {
        this.homeInfoListeners = listener;
    }

    public void registerFcSingalListener(FcSingalListener singalListener) {
        this.fcSingalListener = singalListener;
    }

    public void registerRelayHeartListener(RelayHeartListener listener) {
        this.relayHeartListener = listener;
    }

    public void registerConnectListener(ConnectListener listener) {
        this.connectListener = listener;
    }

    public void registerCameraStateListener(CameraStateListener listener) {
        this.cameraStateListener = listener;
    }

    public void startUpdateTimer() {
        if (!this.mHandler.hasMessages(0) && SessionManager.getInstance().hasSession()) {
            this.mHandler.sendEmptyMessage(0);
        }
    }

    public void stopUpdateTimer() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
        }
    }

    public void setCameraToken(int token) {
        if (this.relayState.isConnect() && this.relayHeartListener != null && this.relayState.getRelayHeart() != null) {
            this.relayHeartListener.cameraStatusListener(token >= 0);
        }
    }

    public void onConnectState(ConectState state) {
        this.mHandler.obtainMessage(1, state).sendToTarget();
    }

    public void onTracking(AutoVcTracking mAutoVcTracking) {
        this.mHandler.obtainMessage(2, mAutoVcTracking).sendToTarget();
    }

    public void onBatterProcess() {
        this.mHandler.obtainMessage(3).sendToTarget();
    }

    public void onPanoramicInformation(AckPanoramaPhotographType ackPanoramaPhotographType) {
        this.mHandler.obtainMessage(5, ackPanoramaPhotographType).sendToTarget();
    }

    public ConectState getConectState() {
        return this.conectState;
    }

    public boolean isIs4KResolution() {
        return this.is4KResolution;
    }

    public void setIs4KResolution(String is4KResolution) {
        if (is4KResolution.contains("4K")) {
            this.is4KResolution = true;
        } else {
            this.is4KResolution = false;
        }
    }
}
