package com.fimi.x8sdk.modulestate;

import com.fimi.kernel.animutils.IOUtils;
import com.fimi.x8sdk.dataparser.AckVersion;

public class VersionState {
    private AckVersion moduleBatteryVersion;
    private AckVersion moduleCameraVersion;
    private AckVersion moduleCvVersion;
    private AckVersion moduleEscVersion;
    private AckVersion moduleFcAckVersion;
    private AckVersion moduleGimbalVersion;
    private AckVersion moduleNfzVersion;
    private AckVersion moduleRcVersion;
    private AckVersion moduleRepeaterRcVersion;
    private AckVersion moduleRepeaterVehicleVersion;
    private AckVersion moduleUltrasonic;

    public AckVersion getModuleUltrasonic() {
        return this.moduleUltrasonic;
    }

    public void setModuleUltrasonic(AckVersion moduleUltrasonic) {
        this.moduleUltrasonic = moduleUltrasonic;
    }

    public AckVersion getModuleCameraVersion() {
        return this.moduleCameraVersion;
    }

    public void setModuleCameraVersion(AckVersion moduleCameraVersion) {
        this.moduleCameraVersion = moduleCameraVersion;
    }

    public AckVersion getModuleNfzVersion() {
        return this.moduleNfzVersion;
    }

    public void setModuleNfzVersion(AckVersion moduleNfzVersion) {
        this.moduleNfzVersion = moduleNfzVersion;
    }

    public AckVersion getModuleBatteryVersion() {
        return this.moduleBatteryVersion;
    }

    public void setModuleBatteryVersion(AckVersion moduleBatteryVersion) {
        this.moduleBatteryVersion = moduleBatteryVersion;
    }

    public AckVersion getModuleGimbalVersion() {
        return this.moduleGimbalVersion;
    }

    public void setModuleGimbalVersion(AckVersion moduleGimbalVersion) {
        this.moduleGimbalVersion = moduleGimbalVersion;
    }

    public AckVersion getModuleEscVersion() {
        return this.moduleEscVersion;
    }

    public void setModuleEscVersion(AckVersion moduleEscVersion) {
        this.moduleEscVersion = moduleEscVersion;
    }

    public AckVersion getModuleRepeaterVehicleVersion() {
        return this.moduleRepeaterVehicleVersion;
    }

    public void setModuleRepeaterVehicleVersion(AckVersion moduleRepeaterVehicleVersion) {
        this.moduleRepeaterVehicleVersion = moduleRepeaterVehicleVersion;
    }

    public AckVersion getModuleRcVersion() {
        return this.moduleRcVersion;
    }

    public void setModuleRcVersion(AckVersion moduleRcVersion) {
        this.moduleRcVersion = moduleRcVersion;
    }

    public AckVersion getModuleCvVersion() {
        return this.moduleCvVersion;
    }

    public void setModuleCvVersion(AckVersion moduleCvVersion) {
        this.moduleCvVersion = moduleCvVersion;
    }

    public AckVersion getModuleRepeaterRcVersion() {
        return this.moduleRepeaterRcVersion;
    }

    public void setModuleRepeaterRcVersion(AckVersion moduleRepeaterRcVersion) {
        this.moduleRepeaterRcVersion = moduleRepeaterRcVersion;
    }

    public AckVersion getModuleFcAckVersion() {
        return this.moduleFcAckVersion;
    }

    public void setModuleFcAckVersion(AckVersion moduleFcAckVersion) {
        this.moduleFcAckVersion = moduleFcAckVersion;
    }

    public void clearVersion() {
        this.moduleFcAckVersion = null;
        this.moduleRcVersion = null;
        this.moduleCvVersion = null;
        this.moduleRepeaterRcVersion = null;
        this.moduleRepeaterVehicleVersion = null;
        this.moduleEscVersion = null;
        this.moduleGimbalVersion = null;
        this.moduleBatteryVersion = null;
        this.moduleNfzVersion = null;
        this.moduleCameraVersion = null;
        this.moduleUltrasonic = null;
    }

    public String showAllVersion() {
        StringBuffer b = new StringBuffer();
        b.append((this.moduleFcAckVersion != null ? "FC=" + this.moduleFcAckVersion.getSoftVersion() : "FC=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleRcVersion != null ? "RC=" + this.moduleRcVersion.getSoftVersion() : "RC=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleCvVersion != null ? "CV=" + this.moduleCvVersion.getSoftVersion() : "CV=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleRepeaterRcVersion != null ? "R_RC=" + this.moduleRepeaterRcVersion.getSoftVersion() : "R_RC=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleRepeaterVehicleVersion != null ? "RV=" + this.moduleRepeaterVehicleVersion.getSoftVersion() : "RV=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleEscVersion != null ? "ESC=" + this.moduleEscVersion.getSoftVersion() : "N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleGimbalVersion != null ? "GB=" + this.moduleGimbalVersion.getSoftVersion() : "GB=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleBatteryVersion != null ? "Battry=" + this.moduleBatteryVersion.getSoftVersion() : "Battry=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleNfzVersion != null ? "NFZ=" + this.moduleNfzVersion.getSoftVersion() : "NFZ=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        b.append((this.moduleCameraVersion != null ? "Camera=" + this.moduleCameraVersion.getSoftVersion() : "Camera=N/A") + IOUtils.LINE_SEPARATOR_UNIX);
        return b.toString();
    }
}
