package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.CameraEVParamsAdatper;
import com.fimi.app.x8s.controls.camera.CameraParamStatus.CameraModelStatus;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraMainSetListener;
import com.fimi.app.x8s.viewHolder.CameraEVParamListener;
import com.fimi.app.x8s.widget.RecyclerDividerItemDecoration;
import com.fimi.app.x8s.widget.X8RulerView;
import com.fimi.app.x8s.widget.X8RulerView.RulerListener;
import com.fimi.app.x8s.widget.X8TabHost;
import com.fimi.app.x8s.widget.X8TabHost.OnSelectListener;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.dataparser.AckCamJsonInfo;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CameraCurParamsJson;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import com.fimi.x8sdk.jsonResult.CurParamsJson;
import com.fimi.x8sdk.listener.JsonCallBackListener;
import com.fimi.x8sdk.modulestate.StateManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class X8CameraEVShutterISOController extends AbsX8Controllers implements CameraEVParamListener, JsonCallBackListener, RulerListener, OnClickListener {
    private final int TIME_INTERVAL = 1000;
    private CameraManager cameraManager;
    private String curParam;
    private CurParamsJson curParamsJson;
    private final String defScale = "0.0";
    private final String defaultIso = CameraJsonCollection.KEY_DE_CONTROL_AUTO;
    private RelativeLayout evView;
    private TextView ev_add_btn;
    private TextView ev_reduct_btn;
    private boolean hasInit = false;
    private boolean isOkay = true;
    private CameraEVParamsAdatper isoAdatper;
    private List<String> isoOptions;
    private RecyclerView isoRecycler;
    private RelativeLayout isoView;
    private int iso_index;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private IX8CameraMainSetListener mainSetListener;
    private Map<String, String> paramMap = new HashMap();
    private X8RulerView rulerView;
    private String scaleValue = "0.0";
    private CameraEVParamsAdatper shutterAdapter;
    private LinearLayoutManager shutterLayout;
    private List<String> shutterOptions;
    private RecyclerView shutterRecycler;
    private RelativeLayout shutterView;
    private int shutter_index;
    private X8TabHost tabHost;
    private boolean tokenEnable = false;
    private TextView tvEv;
    private TextView tvIso;
    private TextView tvShutter;

    public void setCurModle() {
        String deControl = X8CameraParamsValue.getInstance().getCurParamsJson().getDe_control();
        if (deControl == null || !deControl.equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_MANUAL)) {
            this.tabHost.upSelect(0, false);
        } else {
            this.tabHost.upSelect(1, false);
        }
        this.cameraManager.getCameraIsoOptions();
        this.cameraManager.getCameraShutterOptions();
    }

    public void setCameraManager(CameraManager manager) {
        if (manager != null) {
            this.cameraManager = manager;
            this.cameraManager.registerJsonCallBackListener(this);
        }
    }

    public X8CameraEVShutterISOController(View rootView) {
        super(rootView);
        this.mContext = rootView.getContext();
        this.isoRecycler.addItemDecoration(new RecyclerDividerItemDecoration(this.mContext, 0, (int) this.mContext.getResources().getDimension(R.dimen.camera_iso_divider), 17170445));
        this.isoOptions = Arrays.asList(this.mContext.getResources().getStringArray(R.array.x8_iso_options));
        this.isoAdatper = new CameraEVParamsAdatper(this.mContext, this.isoOptions, this, CameraJsonCollection.KEY_AE_ISO);
        this.isoRecycler.setAdapter(this.isoAdatper);
        this.shutterRecycler.addItemDecoration(new RecyclerDividerItemDecoration(this.mContext, 0, (int) this.mContext.getResources().getDimension(R.dimen.camera_shutter_divider), 17170445));
        this.shutterOptions = Arrays.asList(this.mContext.getResources().getStringArray(R.array.x8_shutter_options));
        this.shutterAdapter = new CameraEVParamsAdatper(this.mContext, this.shutterOptions, this, CameraJsonCollection.KEY_SHUTTER_TIME);
        this.shutterRecycler.setAdapter(this.shutterAdapter);
    }

    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.camera_params_setting);
        this.tabHost = (X8TabHost) rootView.findViewById(R.id.camera_tab);
        this.isoRecycler = (RecyclerView) rootView.findViewById(R.id.iso_recycler);
        this.shutterRecycler = (RecyclerView) rootView.findViewById(R.id.shutter_recycler);
        this.rulerView = (X8RulerView) rootView.findViewById(R.id.rulerView);
        this.tvEv = (TextView) rootView.findViewById(R.id.ev_value);
        this.shutterView = (RelativeLayout) rootView.findViewById(R.id.shutter_layout);
        this.tvIso = (TextView) rootView.findViewById(R.id.iso_title);
        this.tvShutter = (TextView) rootView.findViewById(R.id.shutter_title);
        this.isoView = (RelativeLayout) rootView.findViewById(R.id.iso_layout);
        this.evView = (RelativeLayout) rootView.findViewById(R.id.ev_layout);
        this.ev_add_btn = (TextView) rootView.findViewById(R.id.ev_add_btn);
        this.ev_reduct_btn = (TextView) rootView.findViewById(R.id.ev_reduce_btn);
        this.layoutManager = new LinearLayoutManager(this.mContext);
        this.layoutManager.setOrientation(0);
        this.isoRecycler.setLayoutManager(this.layoutManager);
        this.isoRecycler.setHasFixedSize(true);
        this.isoRecycler.setAnimation(null);
        this.shutterLayout = new LinearLayoutManager(this.mContext);
        this.shutterLayout.setOrientation(0);
        this.shutterRecycler.setLayoutManager(this.shutterLayout);
        this.shutterRecycler.setHasFixedSize(true);
        this.shutterRecycler.setAnimation(null);
        this.ev_reduct_btn.setOnClickListener(this);
        this.ev_add_btn.setOnClickListener(this);
    }

    public void openUi() {
        super.openUi();
        if (!(this.cameraManager == null || this.hasInit)) {
            this.hasInit = true;
            this.cameraManager.getCameraIsoOptions();
            this.cameraManager.getCameraShutterOptions();
        }
        String deControl = X8CameraParamsValue.getInstance().getCurParamsJson().getDe_control();
        if (deControl == null || !deControl.equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_MANUAL)) {
            this.tabHost.upSelect(0, false);
        } else {
            this.tabHost.upSelect(1, false);
        }
        if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
            if (this.tabHost.getSelectIndex() == 0) {
                initPhotoModle(true);
            } else if (this.tabHost.getSelectIndex() == 1) {
                initPhotoModle(false);
            }
        } else if (CameraParamStatus.modelStatus != CameraModelStatus.record && CameraParamStatus.modelStatus != CameraModelStatus.recording) {
        } else {
            if (this.tabHost.getSelectIndex() == 0) {
                initRecordModle(true);
            } else if (this.tabHost.getSelectIndex() == 1) {
                initRecordModle(false);
            }
        }
    }

    public void defaultVal() {
    }

    public void initActions() {
        this.tabHost.setOnSelectListener(new OnSelectListener() {
            public void onSelect(int index, String text, int last) {
                X8CameraEVShutterISOController.this.tabHost.setSelect(last);
                if (!StateManager.getInstance().getCamera().isDelayedPhotography()) {
                    X8CameraEVShutterISOController.this.setCameraDeControl(index);
                }
            }
        });
        this.rulerView.setRulerListener(this);
    }

    public void setCameraDeControl(final int index) {
        if (index == 0) {
            this.curParam = CameraJsonCollection.KEY_DE_CONTROL_AUTO;
        } else if (index == 1) {
            this.curParam = CameraJsonCollection.KEY_DE_CONTROL_MANUAL;
        }
        this.cameraManager.setCameraDeControl(this.curParam, new JsonUiCallBackListener() {
            public void onComplete(JSONObject rt, Object o) {
                if (rt != null) {
                    CameraParamsJson paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class);
                    int rval = paramsJson.getRval();
                    if (paramsJson != null) {
                        switch (paramsJson.getMsg_id()) {
                            case 2:
                                if (paramsJson.getType() != null && paramsJson.getType().equals(CameraJsonCollection.KEY_DE_CONTROL_TYPE) && rval >= 0) {
                                    if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
                                        if (index == 0) {
                                            X8CameraEVShutterISOController.this.initPhotoModle(true);
                                        } else if (index == 1) {
                                            X8CameraEVShutterISOController.this.initPhotoModle(false);
                                        }
                                    } else if (CameraParamStatus.modelStatus == CameraModelStatus.record || CameraParamStatus.modelStatus == CameraModelStatus.recording) {
                                        if (index == 0) {
                                            X8CameraEVShutterISOController.this.initRecordModle(true);
                                        } else if (index == 1) {
                                            X8CameraEVShutterISOController.this.initRecordModle(false);
                                        }
                                    }
                                    X8CameraEVShutterISOController.this.tabHost.setSelect(index);
                                    X8CameraParamsValue.getInstance().getCurParamsJson().setDe_control(X8CameraEVShutterISOController.this.curParam);
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                }
            }
        });
    }

    public void updateParams(String key, String param) {
        if (this.cameraManager != null) {
            this.paramMap.clear();
            this.paramMap.put(key, param);
            if (key.equals(CameraJsonCollection.KEY_AE_ISO) && this.isoRecycler.isEnabled()) {
                this.cameraManager.setCameraIsoParams(param);
            } else if (key.equals(CameraJsonCollection.KEY_SHUTTER_TIME) && this.shutterRecycler.isEnabled()) {
                this.cameraManager.setCameraShutterParams(param);
            }
        }
    }

    public void onJSONSuccess(JSONObject json) {
    }

    public void onSuccess(AckCamJsonInfo rtJson) {
        this.curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        switch (rtJson.getMsg_id()) {
            case 1:
                HostLogBack.getInstance().writeLog("Alanqiu  ==========rtJson:" + rtJson.toString());
                if (!CameraJsonCollection.KEY_AE_ISO.equalsIgnoreCase(rtJson.getType())) {
                    if (!CameraJsonCollection.KEY_SHUTTER_TIME.equalsIgnoreCase(rtJson.getType())) {
                        if ("ae_bias".equalsIgnoreCase(rtJson.getType())) {
                            String ae_bias = rtJson.getParam();
                            String[] scaleArray = ae_bias.split("\\s+");
                            if (scaleArray != null) {
                                if (scaleArray.length == 3) {
                                    this.scaleValue = scaleArray[1];
                                } else {
                                    this.scaleValue = scaleArray[0];
                                }
                                this.tvEv.setText(this.scaleValue);
                            }
                            this.curParamsJson.setAe_bias(ae_bias);
                            if (!(this.rulerView == null || this.scaleValue == null || "".equals(this.scaleValue))) {
                                this.rulerView.setCurScaleValue(Float.valueOf(this.scaleValue).floatValue());
                                if (this.mainSetListener != null) {
                                    this.mainSetListener.evSetting(String.valueOf(this.scaleValue));
                                    break;
                                }
                            }
                        }
                    }
                    refreshShutterView(rtJson.getParam());
                    this.curParamsJson.setShutter_time(rtJson.getParam());
                    break;
                }
                refreshISOView(rtJson.getParam());
                this.curParamsJson.setIso(rtJson.getParam());
                break;
                break;
        }
        if (this.rootView.getVisibility() != 0 || !(rtJson instanceof CameraParamsJson)) {
            return;
        }
        if (rtJson.getMsg_id() == 9) {
            if (rtJson.getParam().equals(CameraJsonCollection.KEY_AE_ISO)) {
                this.isoOptions = ((CameraParamsJson) rtJson).getOptions();
                this.isoAdatper.updateDatas(this.isoOptions);
                this.isoAdatper.upSelectIndex(this.iso_index);
            } else if (rtJson.getParam().equals(CameraJsonCollection.KEY_SHUTTER_TIME)) {
                this.shutterOptions = ((CameraParamsJson) rtJson).getOptions();
                this.shutterAdapter.updateDatas(this.shutterOptions);
                this.shutterAdapter.upSelectIndex(this.shutter_index);
            }
        } else if (rtJson.getMsg_id() != 2) {
        } else {
            if (rtJson.getType().equals(CameraJsonCollection.KEY_AE_ISO)) {
                refreshISOView();
                if (this.paramMap.get(CameraJsonCollection.KEY_AE_ISO) != null) {
                    this.curParamsJson.setIso((String) this.paramMap.get(CameraJsonCollection.KEY_AE_ISO));
                }
                this.handleView.postDelayed(new Runnable() {
                    public void run() {
                        if (X8CameraEVShutterISOController.this.curParamsJson.getShutter_time().trim().equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
                            X8CameraEVShutterISOController.this.cameraManager.getCameraShutter();
                        }
                        if (!X8CameraEVShutterISOController.this.curParamsJson.getAe_bias().trim().contains("0.0")) {
                            X8CameraEVShutterISOController.this.cameraManager.getCameraEV();
                        }
                    }
                }, 1000);
            } else if (rtJson.getType().equals(CameraJsonCollection.KEY_SHUTTER_TIME)) {
                this.shutter_index = this.shutterOptions.indexOf(this.paramMap.get(CameraJsonCollection.KEY_SHUTTER_TIME));
                this.shutterAdapter.upSelectIndex(this.shutter_index);
                if (this.mainSetListener != null) {
                    this.mainSetListener.shutterSetting((String) this.paramMap.get(CameraJsonCollection.KEY_SHUTTER_TIME));
                }
                if (this.paramMap.get(CameraJsonCollection.KEY_SHUTTER_TIME) != null) {
                    this.curParamsJson.setShutter_time((String) this.paramMap.get(CameraJsonCollection.KEY_SHUTTER_TIME));
                }
                this.handleView.postDelayed(new Runnable() {
                    public void run() {
                        if (!X8CameraEVShutterISOController.this.curParamsJson.getIso().trim().equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
                            X8CameraEVShutterISOController.this.cameraManager.getCameraISO();
                        }
                        HostLogBack.getInstance().writeLog("Alanqiu  ========KEY_AE_BIAS==rtJson:" + X8CameraEVShutterISOController.this.curParamsJson.getAe_bias().trim().contains("0.0") + "curParamsJson:" + X8CameraEVShutterISOController.this.curParamsJson.toString());
                        if (!X8CameraEVShutterISOController.this.curParamsJson.getAe_bias().trim().contains("0.0")) {
                            X8CameraEVShutterISOController.this.cameraManager.getCameraEV();
                        }
                    }
                }, 1000);
            } else if (rtJson.getType().equals("ae_bias")) {
                this.tvEv.setText(String.valueOf(this.scaleValue));
                if (this.mainSetListener != null) {
                    this.mainSetListener.evSetting(String.valueOf(this.scaleValue));
                }
                this.curParamsJson.setAe_bias(this.scaleValue);
                this.handleView.postDelayed(new Runnable() {
                    public void run() {
                        if (!X8CameraEVShutterISOController.this.curParamsJson.getIso().trim().equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
                            X8CameraEVShutterISOController.this.cameraManager.getCameraISO();
                            if (!X8CameraEVShutterISOController.this.curParamsJson.getShutter_time().trim().equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
                                X8CameraEVShutterISOController.this.cameraManager.getCameraShutter();
                            }
                        }
                    }
                }, 1000);
            }
        }
    }

    public void onFail(int reval, int msgId, String type) {
    }

    public void outTime() {
    }

    private void initPhotoModle(boolean isAuto) {
        if (isAuto) {
            updateViewEnable(this.tokenEnable, this.evView);
            updateViewEnable(false, this.shutterView, this.isoView, this.shutterRecycler, this.isoRecycler);
        } else if (StateManager.getInstance().getCamera().isDelayedPhotography()) {
            updateViewEnable(false, this.shutterView, this.isoView, this.shutterRecycler, this.isoRecycler, this.evView);
        } else {
            updateViewEnable(this.tokenEnable, this.isoView, this.shutterView, this.isoRecycler, this.shutterRecycler);
            updateViewEnable(false, this.evView);
        }
        this.shutterView.setVisibility(0);
    }

    private void initRecordModle(boolean isAuto) {
        initPhotoModle(isAuto);
        this.shutterView.setVisibility(8);
    }

    private void updateViewEnable(boolean enable, ViewGroup... parent) {
        if (parent != null && parent.length > 0) {
            for (ViewGroup group : parent) {
                if (!(group instanceof RecyclerView)) {
                    int len = group.getChildCount();
                    for (int j = 0; j < len; j++) {
                        View subView = group.getChildAt(j);
                        if (subView instanceof X8RulerView) {
                            this.rulerView.setEnable(enable);
                        } else if (subView instanceof ViewGroup) {
                            updateViewEnable(enable, (ViewGroup) subView);
                        } else {
                            subView.setEnabled(enable);
                        }
                    }
                } else if (group == this.isoRecycler || group == this.shutterRecycler) {
                    this.isoAdatper.setEnable(enable);
                    this.shutterAdapter.setEnable(enable);
                }
            }
        }
    }

    public void initData(CameraCurParamsJson curParamsJson) {
        if (curParamsJson != null) {
            List<CurParamsJson> plist = curParamsJson.getParam();
            if (plist != null && plist.size() > 0) {
                for (CurParamsJson paramsJson : plist) {
                    if (paramsJson.getAe_bias() != null && !paramsJson.getAe_bias().equals("")) {
                        String[] scaleArray = paramsJson.getAe_bias().split("\\s+");
                        if (scaleArray != null) {
                            if (scaleArray.length == 3) {
                                this.scaleValue = scaleArray[1];
                            } else {
                                this.scaleValue = scaleArray[0];
                            }
                            this.tvEv.setText(this.scaleValue);
                        }
                        if (!(this.rulerView == null || this.scaleValue == null || "".equals(this.scaleValue))) {
                            this.rulerView.setCurScaleValue(Float.valueOf(this.scaleValue).floatValue());
                        }
                    } else if (this.shutterOptions.contains(paramsJson.getShutter_time())) {
                        this.shutter_index = this.shutterOptions.indexOf(paramsJson.getShutter_time());
                        this.shutterAdapter.upSelectIndex(this.shutter_index);
                    } else if (this.isoOptions.contains(paramsJson.getIso())) {
                        this.iso_index = this.isoOptions.indexOf(paramsJson.getIso());
                        this.isoAdatper.upSelectIndex(this.iso_index);
                    }
                }
            }
        }
        this.tokenEnable = true;
        if (CameraParamStatus.modelStatus == CameraModelStatus.takePhoto) {
            if (this.tabHost.getSelectIndex() == 0) {
                initPhotoModle(true);
            } else if (this.tabHost.getSelectIndex() == 1) {
                initPhotoModle(false);
            }
        } else if (CameraParamStatus.modelStatus != CameraModelStatus.record && CameraParamStatus.modelStatus != CameraModelStatus.recording) {
        } else {
            if (this.tabHost.getSelectIndex() == 0) {
                initRecordModle(true);
            } else if (this.tabHost.getSelectIndex() == 1) {
                initRecordModle(false);
            }
        }
    }

    public void updateRuler(float scaleValue) {
        if (this.cameraManager != null) {
            if (scaleValue < 0.0f) {
                this.scaleValue = String.valueOf(scaleValue);
            } else if (scaleValue > 0.0f) {
                this.scaleValue = this.mContext.getResources().getString(R.string.x8_camera_ev_add) + String.valueOf(scaleValue);
            } else {
                this.scaleValue = String.valueOf(" " + scaleValue);
            }
            this.cameraManager.setCameraEV(this.scaleValue);
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        int m;
        CameraManager cameraManager;
        String str;
        if (i == R.id.ev_add_btn) {
            if (this.scaleValue != null && !"".equals(this.scaleValue)) {
                m = 0;
                while (m < CameraJsonCollection.rulerValues.length) {
                    if (!CameraJsonCollection.rulerValues[m].equals(this.scaleValue.trim()) || m >= CameraJsonCollection.rulerValues.length - 1) {
                        m++;
                    } else {
                        this.rulerView.setCurScaleValue(Float.valueOf(CameraJsonCollection.rulerValues[m + 1]).floatValue());
                        cameraManager = this.cameraManager;
                        if (CameraJsonCollection.rulerValues[m + 1].equals("0.0")) {
                            str = " 0.0";
                        } else {
                            str = CameraJsonCollection.rulerValues[m + 1];
                        }
                        cameraManager.setCameraEV(str);
                        this.scaleValue = CameraJsonCollection.rulerValues[m + 1];
                        return;
                    }
                }
            }
        } else if (i == R.id.ev_reduce_btn && this.scaleValue != null && !"".equals(this.scaleValue)) {
            m = 0;
            while (m < CameraJsonCollection.rulerValues.length) {
                if (!CameraJsonCollection.rulerValues[m].equals(this.scaleValue.trim()) || m <= 0) {
                    m++;
                } else {
                    this.rulerView.setCurScaleValue(Float.valueOf(CameraJsonCollection.rulerValues[m - 1]).floatValue());
                    cameraManager = this.cameraManager;
                    if (CameraJsonCollection.rulerValues[m - 1].equals("0.0")) {
                        str = " 0.0";
                    } else {
                        str = CameraJsonCollection.rulerValues[m - 1];
                    }
                    cameraManager.setCameraEV(str);
                    this.scaleValue = CameraJsonCollection.rulerValues[m - 1];
                    return;
                }
            }
        }
    }

    public void setMainSetListener(IX8CameraMainSetListener mainSetListener) {
        this.mainSetListener = mainSetListener;
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.isOkay != b) {
            this.isOkay = b;
            this.tokenEnable = b;
            updateViewEnable(b, this.evView, this.isoView, this.shutterView, this.isoRecycler, this.shutterRecycler);
        }
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void setEvParamValue(String value) {
        this.scaleValue = value;
        this.rulerView.setCurScaleValue(Float.valueOf(this.scaleValue).floatValue());
    }

    public void setIOSParamValue(String value) {
        refreshISOView(value);
    }

    private void refreshISOView(String value) {
        this.paramMap.put(CameraJsonCollection.KEY_AE_ISO, value);
        refreshISOView();
    }

    private void refreshISOView() {
        String value = (String) this.paramMap.get(CameraJsonCollection.KEY_AE_ISO);
        this.iso_index = this.isoOptions.indexOf(value);
        this.isoAdatper.upSelectIndex(this.iso_index);
        if (this.mainSetListener != null) {
            this.mainSetListener.isoSetting(value);
        }
    }

    private void refreshShutterView(String value) {
        this.paramMap.put(CameraJsonCollection.KEY_SHUTTER_TIME, value);
        refreshShutterView();
    }

    private void refreshShutterView() {
        String value = (String) this.paramMap.get(CameraJsonCollection.KEY_SHUTTER_TIME);
        this.shutter_index = this.shutterOptions.indexOf(value);
        this.shutterAdapter.upSelectIndex(this.shutter_index);
        if (this.mainSetListener != null) {
            this.mainSetListener.shutterSetting(value);
        }
    }
}
