package com.fimi.app.x8s.manager;

import com.fimi.app.x8s.R;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.network.BaseManager;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8MapGetCityManager extends BaseManager {
    public static String locality = "";

    public static void onSetHomeEvent(final X8sMainActivity activity, final int type) {
        if (activity.getmMapVideoController().getFimiMap().hasHomeInfo()) {
            double lat;
            double lng;
            float h = StateManager.getInstance().getX8Drone().getHomeInfo().getHeight();
            float accuracy = activity.getmMapVideoController().getFimiMap().getAccuracy();
            if (type == 0) {
                lat = StateManager.getInstance().getX8Drone().getLatitude();
                lng = StateManager.getInstance().getX8Drone().getLongitude();
            } else {
                double[] latLng = activity.getmMapVideoController().getFimiMap().getManLatLng();
                if (latLng == null) {
                    X8ToastUtil.showToast(activity, activity.getString(R.string.x8_general_return_person_failed), 0);
                    return;
                } else {
                    lat = latLng[0];
                    lng = latLng[1];
                }
            }
            activity.getFcManager().setHomePoint(h, lat, lng, type, accuracy, new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        if (type != 0) {
                            X8ToastUtil.showToast(activity, activity.getString(R.string.x8_general_return_person), 0);
                        } else {
                            X8ToastUtil.showToast(activity, activity.getString(R.string.x8_general_return_drone), 0);
                        }
                    } else if (o == null) {
                        X8ToastUtil.showToast(activity, activity.getString(R.string.x8_general_return_failed), 0);
                    }
                }
            });
        }
    }
}
