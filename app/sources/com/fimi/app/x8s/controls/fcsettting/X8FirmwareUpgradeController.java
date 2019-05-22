package com.fimi.app.x8s.controls.fcsettting;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.FirmwareUpgradeAdapter;
import com.fimi.app.x8s.entity.VersionEntity;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8FirmwareUpgradeControllerListener;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.EventMessage;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.VersionState;
import com.fimi.x8sdk.update.UpdateUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class X8FirmwareUpgradeController extends AbsX8MenuBoxControllers implements OnClickListener {
    private FirmwareUpgradeAdapter adapter;
    private boolean availableUpgrades;
    boolean currentConnectedState;
    private ImageView imgReturn;
    private ArrayList<VersionEntity> items = new ArrayList();
    private IX8FirmwareUpgradeControllerListener listener;
    private RecyclerView mRecyclerList;
    private TextView tvFirmwareUpgrade;

    public X8FirmwareUpgradeController(View rootView) {
        super(rootView);
        this.contentView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_general_item_firmware_upgrade, (ViewGroup) rootView, true);
        this.imgReturn = (ImageView) this.contentView.findViewById(R.id.btn_return);
        this.tvFirmwareUpgrade = (TextView) this.contentView.findViewById(R.id.tv_firmware_upgrade);
        this.mRecyclerList = (RecyclerView) this.contentView.findViewById(R.id.recycler_version_list);
        initData();
        this.mRecyclerList.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        this.adapter = new FirmwareUpgradeAdapter(this.items);
        this.mRecyclerList.setAdapter(this.adapter);
        this.imgReturn.setOnClickListener(this);
        this.tvFirmwareUpgrade.setOnClickListener(this);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onFirmwareUpgradeReturn();
            }
        } else if (i == R.id.tv_firmware_upgrade && this.listener != null) {
            this.listener.onFirmwareUpgradeClick();
        }
    }

    public void initViews(View rootView) {
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void showItem() {
        super.showItem();
        this.contentView.setVisibility(0);
        EventBus.getDefault().register(this);
    }

    public void closeItem() {
        super.closeItem();
        this.contentView.setVisibility(8);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusUI(EventMessage eventMessage) {
        if (eventMessage.getKey() == Constants.X8_UPDATE_EVENT_KEY) {
            onVersionChange();
            this.adapter.notifyDataSetChanged();
        }
    }

    public void setOnFirmwareClickListener(IX8FirmwareUpgradeControllerListener listener) {
        this.listener = listener;
    }

    private void initData() {
        this.items.clear();
        VersionState versionState = StateManager.getInstance().getVersionState();
        AckVersion moduleFcAckVersion = versionState.getModuleFcAckVersion();
        AckVersion moduleRcVersion = versionState.getModuleRcVersion();
        AckVersion moduleCvVersion = versionState.getModuleCvVersion();
        AckVersion moduleRepeaterRcVersion = versionState.getModuleRepeaterRcVersion();
        AckVersion moduleRepeaterVehicleVersion = versionState.getModuleRepeaterVehicleVersion();
        AckVersion moduleEscVersion = versionState.getModuleEscVersion();
        AckVersion moduleGimbalVersion = versionState.getModuleGimbalVersion();
        AckVersion moduleBatteryVersion = versionState.getModuleBatteryVersion();
        AckVersion moduleNfzVersion = versionState.getModuleNfzVersion();
        AckVersion moduleCameraVersion = versionState.getModuleCameraVersion();
        AckVersion moduleUltrasonic = versionState.getModuleUltrasonic();
        VersionEntity flyControllerVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_fc_name), versionState.getModuleFcAckVersion());
        VersionEntity cameraVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_camera_name), versionState.getModuleCameraVersion());
        VersionEntity cloudTerraceVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_gimbal_name), versionState.getModuleGimbalVersion());
        VersionEntity batteryVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_battery_name), versionState.getModuleBatteryVersion());
        VersionEntity versionEntity = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_rc_name), versionState.getModuleRcVersion());
        versionEntity = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_rc_rl_name), versionState.getModuleRepeaterRcVersion());
        VersionEntity cv = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_vc_name), versionState.getModuleCvVersion());
        VersionEntity fcRl = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_fc_rl_name), versionState.getModuleRepeaterVehicleVersion());
        versionEntity = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_esc_name), versionState.getModuleEscVersion());
        versionEntity = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_noflyzone_name), versionState.getModuleNfzVersion());
        versionEntity = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_ultrasonic_name), versionState.getModuleUltrasonic());
        List<UpfirewareDto> upfirewareDtos = UpdateUtil.getUpfireDtos();
        this.availableUpgrades = false;
        for (UpfirewareDto upfirewareDto : upfirewareDtos) {
            if (versionState == null) {
                this.availableUpgrades = false;
            } else if (moduleFcAckVersion != null && upfirewareDto.getModel() == moduleFcAckVersion.getModel() && upfirewareDto.getType() == moduleFcAckVersion.getType()) {
                flyControllerVersion.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleCameraVersion != null && upfirewareDto.getModel() == moduleCameraVersion.getModel() && upfirewareDto.getType() == moduleCameraVersion.getType()) {
                cameraVersion.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleGimbalVersion != null && upfirewareDto.getModel() == moduleGimbalVersion.getModel() && upfirewareDto.getType() == moduleGimbalVersion.getType()) {
                cloudTerraceVersion.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleBatteryVersion != null && upfirewareDto.getModel() == moduleBatteryVersion.getModel() && upfirewareDto.getType() == moduleBatteryVersion.getType()) {
                batteryVersion.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleRcVersion != null && upfirewareDto.getModel() == moduleRcVersion.getModel() && upfirewareDto.getType() == moduleRcVersion.getType()) {
                versionEntity.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleRepeaterRcVersion != null && upfirewareDto.getModel() == moduleRepeaterRcVersion.getModel() && upfirewareDto.getType() == moduleRepeaterRcVersion.getType()) {
                versionEntity.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleCvVersion != null && upfirewareDto.getModel() == moduleCvVersion.getModel() && upfirewareDto.getType() == moduleCvVersion.getType()) {
                cv.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleRepeaterVehicleVersion != null && upfirewareDto.getModel() == moduleRepeaterVehicleVersion.getModel() && upfirewareDto.getType() == moduleRepeaterVehicleVersion.getType()) {
                fcRl.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleEscVersion != null && upfirewareDto.getModel() == moduleEscVersion.getModel() && upfirewareDto.getType() == moduleEscVersion.getType()) {
                versionEntity.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleNfzVersion != null && upfirewareDto.getModel() == moduleNfzVersion.getModel() && upfirewareDto.getType() == moduleNfzVersion.getType()) {
                versionEntity.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else if (moduleUltrasonic != null && upfirewareDto.getModel() == versionState.getModuleUltrasonic().getModel() && upfirewareDto.getType() == versionState.getModuleUltrasonic().getType()) {
                versionEntity.setHasNewVersion(true);
                this.availableUpgrades = true;
            } else {
                this.availableUpgrades = false;
            }
        }
        this.items.add(flyControllerVersion);
        this.items.add(cameraVersion);
        this.items.add(cloudTerraceVersion);
        this.items.add(batteryVersion);
        this.items.add(versionEntity);
        this.items.add(versionEntity);
        this.items.add(cv);
        this.items.add(fcRl);
        this.items.add(versionEntity);
        this.items.add(versionEntity);
        this.items.add(versionEntity);
        showNewUpdate(this.availableUpgrades);
    }

    private void showNewUpdate(boolean isShow) {
        if (isShow) {
            this.tvFirmwareUpgrade.setAlpha(1.0f);
            this.tvFirmwareUpgrade.setEnabled(true);
            return;
        }
        this.tvFirmwareUpgrade.setAlpha(0.6f);
        this.tvFirmwareUpgrade.setEnabled(false);
    }

    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.currentConnectedState != b) {
            this.currentConnectedState = b;
            if (!b || StateManager.getInstance().getCamera().getToken() <= 0) {
                showNewUpdate(b);
                return;
            }
            initData();
            this.adapter.notifyDataSetChanged();
        }
    }

    public void onVersionChange() {
        if (this.contentView != null && this.adapter != null) {
            this.items.clear();
            initData();
            this.adapter.changeDatas(this.items);
        }
    }
}
