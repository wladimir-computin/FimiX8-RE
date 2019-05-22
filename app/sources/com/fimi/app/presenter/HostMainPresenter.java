package com.fimi.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import com.fimi.TcpClient;
import com.fimi.android.app.R;
import com.fimi.app.interfaces.IProductControllers;
import com.fimi.app.ui.main.HostNewMainActivity;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.host.HostConstants;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.AppBlockCanaryContext;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.permission.PermissionManager;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.ttsspeak.SpeakTTs;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.libdownfw.ivew.IFirmwareDownView;
import com.fimi.libdownfw.update.DownloadFwSelectActivity;
import com.fimi.network.DownFwService;
import com.fimi.network.DownFwService.DownState;
import com.fimi.network.DownNoticeMananger;
import com.fimi.network.IDownProgress;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.DialogManager;
import com.fimi.widget.DialogManager.OnDialogListener;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.common.GlobalConfig.Builder;
import com.fimi.x8sdk.map.MapType;
import com.github.moduth.blockcanary.BlockCanary;
import java.util.ArrayList;
import java.util.List;
import router.Router;

public class HostMainPresenter implements IDownProgress {
    static List<UpfirewareDto> list = new ArrayList();
    DialogManager dialogManager;
    IFirmwareDownView iFirmwareDownView;
    boolean isProviderEnabled;
    private LocationManager lm;
    private Context mContext;
    int position = 0;
    private IProductControllers productControler;

    public HostMainPresenter(Context mContext, IFirmwareDownView iFirmwareDownView) {
        this.mContext = mContext;
        this.iFirmwareDownView = iFirmwareDownView;
        initSessionAndNotice();
        initTTs();
        initDownListener();
    }

    private void initDownListener() {
        DownNoticeMananger.getDownNoticManger().addDownNoticeList(this);
    }

    public static void checkUpfireList() {
        list = HostConstants.getNeedDownFw();
    }

    public static List<UpfirewareDto> getUpfireList() {
        return list;
    }

    public boolean isForce() {
        return HostConstants.isForceUpdate(list);
    }

    public static boolean isDownFirmwareTip() {
        return (list != null && list.size() > 0 && DownFwService.getState().equals(DownState.UnStart)) || DownFwService.getState().equals(DownState.StopDown);
    }

    private void initTTs() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                SpeakTTs.initContext(HostMainPresenter.this.mContext).initTTSAuth();
            }
        });
    }

    private void initSessionAndNotice() {
        SessionManager.initInstance();
        NoticeManager.initInstance();
    }

    public void requestPermissions() {
        PermissionManager.requestStoragePermissions();
    }

    public void requestX9Permissions(Activity activity) {
        if (VERSION.SDK_INT >= 23) {
            PermissionManager.checkRequiredPermission(activity);
        }
    }

    public void go2DownSelectActivty() {
        if (HostConstants.getNeedDownFw().size() > 0) {
            this.mContext.startActivity(new Intent(this.mContext, DownloadFwSelectActivity.class));
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 7) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                toGh2MainActivity(false);
            }
        } else if (requestCode == 2) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                PermissionManager.requestRecordAudioPermissions();
            } else {
                PermissionManager.requestRecordAudioPermissions();
            }
        } else if (requestCode == 8) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                PermissionManager.requestCameraPermissions();
            } else {
                PermissionManager.requestCameraPermissions();
            }
        } else if (requestCode == PermissionManager.ACTION_LOCATION_SOURCE_SETTINGS) {
            gh2PermissionDetection();
        } else if (requestCode == 9) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != 0) {
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) this.mContext, permissions[i]);
                }
            }
        }
    }

    private void initSDK() {
        BlockCanary.install(this.mContext, new AppBlockCanaryContext()).start();
        X8AiConfig.getInstance().init();
        Builder builder = new Builder();
        builder.setMapType(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_OPTION, false) ? MapType.AMap : MapType.GoogleMap).setMapStyle(SPStoreManager.getInstance().getInt(Constants.X8_MAP_STYLE)).setRectification(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_RECTIFYIN_OPTION, true)).setShowLog(SPStoreManager.getInstance().getBoolean(Constants.X8_SHOW_LOG_OPTION, true)).setShowmMtric(SPStoreManager.getInstance().getBoolean(Constants.X8_UNITY_OPTION, true)).setGridLine(SPStoreManager.getInstance().getInt(Constants.X8_GLINE_LINE_OPTION));
        GlobalConfig.getInstance().init(builder);
        X8NumberUtil.resetPrexString(this.mContext);
        TcpClient.createInit();
    }

    private void startX8s() {
        SPStoreManager.getInstance().saveBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE_SETTING, false);
        GlobalConfig.getInstance().setMapType(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_OPTION, false) ? MapType.AMap : MapType.GoogleMap);
        this.mContext.startActivity(new Intent(this.mContext, X8sMainActivity.class));
    }

    public void onConnectDevice() {
        boolean isStartBegnnerGuide;
        if (com.fimi.kernel.Constants.productType == ProductEnum.X8S) {
            startX8s();
        } else if (com.fimi.kernel.Constants.productType == ProductEnum.FIMIAPP) {
            if (this.position == 0) {
                startX8s();
            } else if (this.position != 1) {
                gh2PermissionDetection();
            } else if (!PermissionManager.isLocationEnabled(this.mContext)) {
                showGpsDialog();
            } else if (PermissionManager.hasLocaltionPermissions()) {
                isStartBegnnerGuide = SPStoreManager.getInstance().getBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE);
                this.productControler.stopAnimation();
                if (isStartBegnnerGuide) {
                    this.mContext.startActivity((Intent) Router.invoke(this.mContext, "activity://x9.main"));
                    ((Activity) this.mContext).overridePendingTransition(17432576, 17432577);
                    return;
                }
                SPStoreManager.getInstance().saveBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE_SETTING, false);
                this.mContext.startActivity((Intent) Router.invoke(this.mContext, "activity://x9.guide"));
            } else {
                showLocaltionPermissionDialog();
            }
        } else if (com.fimi.kernel.Constants.productType != ProductEnum.X9) {
            gh2PermissionDetection();
        } else if (!PermissionManager.isLocationEnabled(this.mContext)) {
            showGpsDialog();
        } else if (PermissionManager.hasLocaltionPermissions()) {
            isStartBegnnerGuide = SPStoreManager.getInstance().getBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE);
            this.productControler.stopAnimation();
            if (isStartBegnnerGuide) {
                this.mContext.startActivity((Intent) Router.invoke(this.mContext, "activity://x9.main"));
                ((Activity) this.mContext).overridePendingTransition(17432576, 17432577);
                return;
            }
            SPStoreManager.getInstance().saveBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE_SETTING, false);
            this.mContext.startActivity((Intent) Router.invoke(this.mContext, "activity://x9.guide"));
        } else {
            showLocaltionPermissionDialog();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void gh2PermissionDetection() {
    }

    private void toGh2MainActivity(boolean isProvider) {
        Context context = this.mContext;
        Context context2 = this.mContext;
        this.lm = (LocationManager) context.getSystemService("location");
        this.isProviderEnabled = this.lm.isProviderEnabled("gps");
        if (!this.isProviderEnabled) {
            this.isProviderEnabled = isProvider;
        }
        if (this.isProviderEnabled) {
            this.mContext.startActivity((Intent) Router.invoke(this.mContext, "activity://gh2.main"));
            return;
        }
        openCameraDialog(this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint));
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private void openCameraDialog(final String dialoghint) {
        this.dialogManager = new DialogManager(this.mContext, null, (CharSequence) dialoghint, this.mContext.getString(R.string.fimi_sdk_go_setting), this.mContext.getString(R.string.cancel));
        this.dialogManager.setOnDiaLogListener(new OnDialogListener() {
            public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                if (dialoghint.equals(HostMainPresenter.this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint))) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
                    ((Activity) HostMainPresenter.this.mContext).startActivityForResult(intent, PermissionManager.ACTION_LOCATION_SOURCE_SETTINGS);
                    return;
                }
                ((Activity) HostMainPresenter.this.mContext).startActivityForResult(new Intent("android.settings.SETTINGS"), 0);
            }

            public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                if (dialoghint.equals(HostMainPresenter.this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint))) {
                    HostMainPresenter.this.isProviderEnabled = true;
                    HostMainPresenter.this.toGh2MainActivity(true);
                }
            }
        });
        if (!((Activity) this.mContext).isFinishing()) {
            this.dialogManager.showDialog();
        }
    }

    public void onProgress(DownState downState, int progrss, String fileName) {
        this.iFirmwareDownView.showDownFwProgress(downState, progrss, fileName);
    }

    public void removeNoticDownListener() {
        DownNoticeMananger.getDownNoticManger().remioveDownNoticeList(this);
    }

    public void setProductControler(IProductControllers productControler) {
        this.productControler = productControler;
    }

    public void showGpsDialog() {
        DialogManager gpsDialogManager = new DialogManager(this.mContext, null, this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint), this.mContext.getString(R.string.fimi_sdk_go_setting), this.mContext.getString(R.string.fimi_sdk_update_ignore));
        gpsDialogManager.setOnDiaLogListener(new OnDialogListener() {
            public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                HostMainPresenter.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }

            public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
            }
        });
        gpsDialogManager.showDialog();
    }

    public void showLocaltionPermissionDialog() {
    }

    public void gotoTeacher(String pattern) {
        if (VERSION.SDK_INT != 23) {
            this.mContext.startActivity((Intent) Router.invoke(this.mContext, pattern));
        } else if (System.canWrite(this.mContext)) {
            this.mContext.startActivity((Intent) Router.invoke(this.mContext, pattern));
        } else {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS", Uri.parse("package:" + this.mContext.getPackageName()));
            HostNewMainActivity activity = this.mContext;
            activity.getClass();
            activity.startActivityForResult(intent, 1);
        }
    }
}
