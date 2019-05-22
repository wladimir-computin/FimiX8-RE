package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.PhotoSubParamsAdapter;
import com.fimi.app.x8s.entity.PhotoSubParamItemEntity;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraMainSetListener;
import com.fimi.app.x8s.viewHolder.CameraParamListener;
import com.fimi.app.x8s.viewHolder.SubParamItemListener;
import com.fimi.app.x8s.viewHolder.SubParamsViewHolder;
import com.fimi.app.x8s.widget.RecyclerDividerItemDecoration;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;

public class X8CameraSubParamsController extends AbsX8Controllers implements SubParamItemListener, JsonUiCallBackListener {
    private CameraManager cameraManager;
    private boolean canScroller = true;
    private String contrast;
    private String curParam;
    private SubParamsViewHolder holder;
    int index;
    private boolean isForbid = false;
    String key;
    private ScrollLinearLayoutManager layoutManager;
    private Context mContext;
    private IX8CameraMainSetListener mainSetListener;
    String optionName;
    private CameraParamListener paramListener;
    private PhotoSubParamsAdapter paramsAdapter = new PhotoSubParamsAdapter(this.mContext, this.subParam);
    private X8CameraParamsValue paramsValue;
    private RecyclerView recyclerView;
    private String saturation;
    private PhotoSubParamItemEntity subParam;
    String value;

    public class ScrollLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnable = true;

        public ScrollLinearLayoutManager(Context context) {
            super(context);
        }

        public ScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public ScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public boolean canScrollVertically() {
            return this.isScrollEnable && super.canScrollVertically();
        }

        public void setScrollEnable(boolean isEnable) {
            this.isScrollEnable = isEnable;
        }
    }

    public void setParamListener(CameraParamListener paramListener) {
        this.paramListener = paramListener;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setSubParam(PhotoSubParamItemEntity subParam) {
        this.subParam = subParam;
        this.paramsAdapter.updateData(subParam);
    }

    public X8CameraSubParamsController(View rootView) {
        super(rootView);
        this.paramsAdapter.setParamListener(this);
        this.layoutManager = new ScrollLinearLayoutManager(this.mContext);
        this.layoutManager.setOrientation(1);
        this.recyclerView.addItemDecoration(new RecyclerDividerItemDecoration(this.mContext, 1, 0, 17170445));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAnimation(null);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.paramsAdapter);
        this.paramsValue = X8CameraParamsValue.getInstance();
    }

    public void openUi() {
        super.openUi();
        this.paramsAdapter.updateData(this.subParam);
    }

    public void initViews(View rootView) {
        this.mContext = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.item_param_view_layout);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.item_param_Recycler);
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void checkDetailParam(String optionName, String key, int index, ViewHolder viewHolder) {
        this.holder = (SubParamsViewHolder) viewHolder;
        this.curParam = key;
        if (this.cameraManager != null && key != null) {
            if (optionName.equals(this.mContext.getResources().getString(R.string.x8_camera_awb))) {
                if (!key.equals(this.mContext.getResources().getString(R.string.x8_awb_hand))) {
                    this.cameraManager.setCameraKeyParams(key, "awb", this);
                }
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_camera_style))) {
                if (key.equals(this.mContext.getResources().getString(R.string.x8_camera_sharpness))) {
                }
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_photo_size))) {
                this.cameraManager.setCameraKeyParams(key, "photo_size", this);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_photo_format))) {
                this.cameraManager.setCameraKeyParams(key, "photo_format", this);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_camera_metering))) {
                this.cameraManager.setCameraKeyParams(key, CameraJsonCollection.KEY_METERMING_MODE, this);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_camera_digita))) {
                this.cameraManager.setCameraKeyParams(key, CameraJsonCollection.KEY_DIGITAL_EFFECT, this);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_video_type))) {
                this.cameraManager.setCameraKeyParams(key, "system_type", this);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_record_quality))) {
                this.cameraManager.setCameraKeyParams(key, "video_quality", this);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_photo_mode))) {
                this.index = index;
                this.cameraManager.setCameraKeyParams(key, "capture_mode", this);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_record_mode))) {
                this.index = index;
                this.cameraManager.setCameraKeyParams(key, CameraJsonCollection.KEY_RECORD_MODE, this);
            }
        }
    }

    public void checkResolutionDetailParam(String optionName, String key, String value, int index, ViewHolder viewHolder) {
        this.holder = (SubParamsViewHolder) viewHolder;
        this.curParam = key;
        if (!(this.cameraManager == null || key == null || !this.isForbid)) {
            if (optionName.equals(this.mContext.getResources().getString(R.string.x8_video_resolution))) {
                this.holder.addParamContent(this.mContext, this.cameraManager, R.layout.x8_camera_array_sub_layout, optionName, key, value);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_photo_mode)) && this.mContext.getResources().getString(R.string.x8_timelapse_capture_1).contains(key)) {
                this.cameraManager.setCameraKeyParams(key, "capture_mode", this);
                this.subParam.setParamValue(this.curParam);
                this.paramsAdapter.updateData(this.subParam);
                this.holder.addParamContent(this.mContext, this.cameraManager, R.layout.x8_camera_array_sub_layout, optionName, key, this.curParam);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_photo_mode)) && this.mContext.getResources().getString(R.string.x8_timelapse_capture_8).contains(key)) {
                this.cameraManager.setCameraKeyParams(key, "capture_mode", this);
                this.subParam.setParamValue(this.curParam);
                this.paramsAdapter.updateData(this.subParam);
                this.holder.addParamContent(this.mContext, this.cameraManager, R.layout.x8_camera_array_sub_layout, optionName, key, this.curParam);
            } else if (optionName.equals(this.mContext.getResources().getString(R.string.x8_record_mode))) {
                this.cameraManager.setCameraKeyParams(key, CameraJsonCollection.KEY_RECORD_MODE, this);
                this.subParam.setParamValue(this.curParam);
                this.paramsAdapter.updateData(this.subParam);
                this.holder.addParamContent(this.mContext, this.cameraManager, R.layout.x8_camera_array_sub_layout, optionName, key, this.curParam);
            }
        }
        this.paramsAdapter.notifyDataSetChanged();
    }

    public void gotoParentItem() {
        if (this.paramListener != null && this.subParam != null) {
            this.paramsAdapter.viewHolderRemoveAllViews();
            String paramKey = this.subParam.getParamKey();
            if (paramKey == null || !paramKey.equals(CameraJsonCollection.KEY_CAMERA_STYLE)) {
                this.paramListener.itemReturnBack(this.subParam.getParamKey(), this.subParam.getParamValue());
                return;
            }
            this.paramListener.itemReturnBack(this.subParam.getParamKey(), this.saturation, this.contrast);
        }
    }

    public void onComplete(JSONObject rt, Object o) {
        if (rt != null) {
            CameraParamsJson paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class);
            int rval = paramsJson.getRval();
            if (paramsJson != null) {
                switch (paramsJson.getMsg_id()) {
                    case 2:
                        if (paramsJson.getType() != null) {
                            if (paramsJson.getType().equals("photo_size") && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setPhoto_size(this.curParam);
                                if (this.mainSetListener != null) {
                                    this.mainSetListener.updateResOrSize();
                                }
                            } else if (paramsJson.getType().equals("photo_format") && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setPhoto_format(this.curParam);
                            } else if (paramsJson.getType().equals("contrast") && rval >= 0) {
                                this.subParam.setParamValue(this.mContext.getResources().getString(R.string.x8_camera_contrast));
                                this.subParam.getOptionMap().put("contrast", this.contrast);
                                this.paramsValue.getCurParamsJson().setContrast(this.curParam);
                                return;
                            } else if (paramsJson.getType().equals("saturation") && rval >= 0) {
                                this.subParam.setParamValue(this.mContext.getResources().getString(R.string.x8_camera_saturation));
                                this.subParam.getOptionMap().put("saturation", this.saturation);
                                this.paramsValue.getCurParamsJson().setSaturation(this.curParam);
                                return;
                            } else if (paramsJson.getType().equals("awb") && rval >= 0) {
                                if (this.mainSetListener != null) {
                                    this.mainSetListener.awbSetting(this.curParam);
                                }
                                this.paramsValue.getCurParamsJson().setAwb(this.curParam);
                            } else if (paramsJson.getType().equals("video_quality") && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setVideo_quality(this.curParam);
                            } else if (paramsJson.getType().equals("video_resolution") && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setVideo_resolution(this.curParam);
                                if (this.mainSetListener != null) {
                                    this.mainSetListener.updateResOrSize();
                                }
                            } else if (paramsJson.getType().equals("ae_bias") && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setAe_bias(this.curParam);
                            } else if (paramsJson.getType().equals(CameraJsonCollection.KEY_SHUTTER_TIME) && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setShutter_time(this.curParam);
                            } else if (paramsJson.getType().equals(CameraJsonCollection.KEY_METERMING_MODE) && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setMetering_mode(this.curParam);
                            } else if (paramsJson.getType().equals(CameraJsonCollection.KEY_DIGITAL_EFFECT) && rval >= 0) {
                                if (this.mainSetListener != null) {
                                    this.mainSetListener.colorSetting(this.curParam);
                                }
                                this.paramsValue.getCurParamsJson().setDigital_effect(this.curParam);
                            } else if (paramsJson.getType().equals("sharpness") && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setSharpness(this.curParam);
                            } else if (paramsJson.getType().equals("system_type") && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setSystem_type(this.curParam);
                                this.cameraManager.getCurCameraParams("video_resolution", new JsonUiCallBackListener() {
                                    public void onComplete(JSONObject rt, Object o) {
                                        if (rt != null && rt.containsKey("rval") && rt.getIntValue("rval") >= 0 && rt.containsKey("param")) {
                                            X8CameraSubParamsController.this.paramsValue.getCurParamsJson().setVideo_resolution(rt.getString("param"));
                                            if (X8CameraSubParamsController.this.mainSetListener != null) {
                                                X8CameraSubParamsController.this.mainSetListener.updateResOrSize();
                                                X8CameraSubParamsController.this.curParam = rt.getString("param");
                                            }
                                        }
                                    }
                                });
                            } else if (paramsJson.getType().equals(CameraJsonCollection.KEY_AE_ISO) && rval >= 0) {
                                this.paramsValue.getCurParamsJson().setIso(this.curParam);
                            }
                            this.subParam.setParamValue(this.curParam);
                            this.paramsAdapter.updateData(this.subParam);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void setRecyclerScroller(boolean scroller) {
        if (this.layoutManager != null) {
            this.layoutManager.setScrollEnable(scroller);
        }
    }

    public void styleParam(String paramKey, int param) {
        if (paramKey.equals("contrast")) {
            this.contrast = String.valueOf(param);
        } else if (paramKey.equals("saturation")) {
            this.saturation = String.valueOf(param);
        }
        this.cameraManager.setCameraKeyParams(String.valueOf(param), paramKey, this);
    }

    public void updateAddContent(String paramKey, String paramValue) {
        this.subParam.setParamKey(paramKey);
        this.subParam.setParamValue(paramValue);
        this.paramsAdapter.updateData(this.subParam);
        if (paramKey.equals("video_resolution") && this.mainSetListener != null) {
            this.mainSetListener.updateResOrSize();
        }
    }

    public void setMainSetListener(IX8CameraMainSetListener mainSetListener) {
        this.mainSetListener = mainSetListener;
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.paramsAdapter != null && this.isForbid != b) {
            this.isForbid = b;
            this.paramsAdapter.forbid(b);
        }
    }

    public boolean onClickBackKey() {
        return false;
    }
}
