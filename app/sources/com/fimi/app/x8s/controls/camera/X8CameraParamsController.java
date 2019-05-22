package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.PhotoParamsAdapter;
import com.fimi.app.x8s.controls.camera.CameraParamStatus.CameraModelStatus;
import com.fimi.app.x8s.entity.PhotoParamItemEntity;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.viewHolder.CameraParamListener;
import com.fimi.app.x8s.widget.RecyclerDividerItemDecoration;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class X8CameraParamsController extends AbsX8Controllers implements JsonUiCallBackListener {
    private Context context;
    CameraModelStatus curStatus;
    private boolean isConnected = false;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private List<PhotoParamItemEntity> paramList = new ArrayList();
    private PhotoParamsAdapter paramsAdapter;
    private RecyclerView recyclerView;

    public void setParamListener(CameraParamListener paramListener) {
        this.paramsAdapter.setParamListener(paramListener);
    }

    public void updateMode(CameraModelStatus mode, Map<String, String> map) {
        this.paramList.clear();
        if (mode == CameraModelStatus.takePhoto) {
            initKeyArray(this.mContext.getResources().getStringArray(R.array.x8_photo_params_array));
        } else if (mode == CameraModelStatus.record || mode == CameraModelStatus.recording) {
            initKeyArray(this.mContext.getResources().getStringArray(R.array.x8_record_params_array));
        }
        if (this.paramList != null && this.paramList.size() > 0) {
            for (PhotoParamItemEntity item : this.paramList) {
                if (item != null) {
                    if (!(map == null || map.get(item.getParamKey()) == null)) {
                        item.setParamValue((String) map.get(item.getParamKey()));
                    }
                    matchNickKey(item);
                }
            }
        }
        this.paramsAdapter.updateData(this.paramList);
    }

    public X8CameraParamsController(View rootView) {
        super(rootView);
        this.context = rootView.getContext();
        initKeyArray(this.mContext.getResources().getStringArray(R.array.x8_photo_params_array));
        this.paramsAdapter = new PhotoParamsAdapter(this.mContext, this.paramList);
        this.layoutManager = new LinearLayoutManager(this.mContext);
        this.layoutManager.setOrientation(1);
        this.recyclerView.addItemDecoration(new RecyclerDividerItemDecoration(this.mContext, 1, 1, R.color.white_10));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAnimation(null);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.paramsAdapter);
    }

    private void initKeyArray(String[] keyArray) {
        if (keyArray != null && keyArray.length > 0) {
            for (String key : keyArray) {
                PhotoParamItemEntity entity = new PhotoParamItemEntity();
                if (key.equals(this.mContext.getResources().getString(R.string.x8_record_mode))) {
                    entity.setParamKey(CameraJsonCollection.KEY_RECORD_MODE);
                } else if (key.equals(this.mContext.getResources().getString(R.string.x8_video_type))) {
                    entity.setParamKey("system_type");
                } else if (key.equals(this.mContext.getResources().getString(R.string.x8_video_resolution))) {
                    entity.setParamKey("video_resolution");
                } else if (key.equals(this.mContext.getResources().getString(R.string.x8_record_quality))) {
                    entity.setParamKey("video_quality");
                } else if (!key.equals(this.mContext.getResources().getString(R.string.x8_record_auto))) {
                    if (key.equals(this.mContext.getResources().getString(R.string.x8_photo_mode))) {
                        entity.setParamKey("capture_mode");
                    } else if (key.equals(this.mContext.getResources().getString(R.string.x8_photo_size))) {
                        entity.setParamKey("photo_size");
                    } else if (key.equals(this.mContext.getResources().getString(R.string.x8_photo_format))) {
                        entity.setParamKey("photo_format");
                    } else if (key.equals(this.mContext.getResources().getString(R.string.x8_camera_awb))) {
                        entity.setParamKey("awb");
                    } else if (key.equals(this.mContext.getResources().getString(R.string.x8_camera_metering))) {
                        entity.setParamKey(CameraJsonCollection.KEY_METERMING_MODE);
                    } else if (key.equals(this.mContext.getResources().getString(R.string.x8_camera_digita))) {
                        entity.setParamKey(CameraJsonCollection.KEY_DIGITAL_EFFECT);
                    } else if (key.equals(this.mContext.getResources().getString(R.string.x8_camera_style))) {
                        entity.setParamKey(CameraJsonCollection.KEY_CAMERA_STYLE);
                    }
                }
                entity.setNickName(key);
                this.paramList.add(entity);
            }
        }
    }

    public void initViews(View rootView) {
        this.mContext = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.param_default_view_layout);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.param_recycler);
    }

    public void openUi() {
        super.openUi();
        this.paramsAdapter.updateData(this.paramList);
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void onComplete(JSONObject rt, Object o) {
        if (rt != null) {
            CameraParamsJson paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class);
            if (paramsJson != null) {
                paramsJson.getMsg_id();
            }
        }
    }

    private void matchNickKey(PhotoParamItemEntity itemEntity) {
        String paramKey = itemEntity.getParamKey();
        String paramValue = itemEntity.getParamValue();
        if (!paramKey.equals("awb") || paramValue == null) {
            if (!paramKey.equals(CameraJsonCollection.KEY_METERMING_MODE) || paramValue == null) {
                if (!paramKey.equals(CameraJsonCollection.KEY_DIGITAL_EFFECT) || paramValue == null) {
                    if (!paramKey.equals("video_quality") || paramValue == null) {
                        if (!paramKey.equals("capture_mode") || paramValue == null) {
                            if (paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE) && paramValue != null) {
                                if (paramValue.equals("normal_record")) {
                                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_record_normal));
                                } else if (paramValue.equals(CameraJsonCollection.KEY_TIMELAPSE_VIDEO)) {
                                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_record_cut_mode));
                                }
                            }
                        } else if (paramValue.equals("normal_capture")) {
                            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_photo_signal_mode));
                        } else if (paramValue.equals(CameraJsonCollection.KEY_TIMELAPSE_PHOTO)) {
                            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_photo_delay_mode));
                        } else if (paramValue.equals("panorama_capture")) {
                            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_photo_panorama_mode));
                        }
                    } else if (paramValue.equals("sfine")) {
                        itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_record_quality_sfine));
                    } else if (paramValue.equals("fine")) {
                        itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_record_quality_fine));
                    } else if (paramValue.equals("normal")) {
                        itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_record_quality_normal));
                    }
                } else if (paramValue.equals("General")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_general));
                } else if (paramValue.equals("Vivid")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_vivid));
                } else if (paramValue.equals("art")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_art));
                } else if (paramValue.equals("black/white")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_black_white));
                } else if (paramValue.equals("film")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_film));
                } else if (paramValue.equals("sepia")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_sepia));
                } else if (paramValue.equals("F-LOG")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_flog));
                } else if (paramValue.equals("punk")) {
                    itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_colours_punk));
                }
            } else if (paramValue.equals("spot")) {
                itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_meter_spot));
            } else if (paramValue.equals("center")) {
                itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_meter_center));
            } else if (paramValue.equals("average")) {
                itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_meter_average));
            }
        } else if (paramValue.equals(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_awb_auto));
        } else if (paramValue.equals("incandescent")) {
            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_awb_incandescent));
        } else if (paramValue.equals("d4000")) {
            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_awb_d4000));
        } else if (paramValue.equals("sunny")) {
            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_awb_sunny));
        } else if (paramValue.equals("cloudy")) {
            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_awb_cloudy));
        } else if (paramValue.equals("shadw")) {
            itemEntity.setNickParam(this.context.getResources().getString(R.string.x8_awb_shadow));
        }
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (b && this.curStatus != CameraParamStatus.modelStatus) {
            this.curStatus = CameraParamStatus.modelStatus;
            if (this.curStatus == CameraModelStatus.recording) {
                this.paramsAdapter.notifyDataSetChanged();
            }
        }
        if (this.isConnected != b) {
            this.isConnected = b;
            this.paramsAdapter.setEnable(b);
        }
    }

    public boolean onClickBackKey() {
        return false;
    }
}
