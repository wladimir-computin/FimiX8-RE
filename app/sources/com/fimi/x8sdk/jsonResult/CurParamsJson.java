package com.fimi.x8sdk.jsonResult;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;

public class CurParamsJson implements Serializable {
    private String ae_bias;
    private String awb;
    private String capture_mode;
    private String contrast;
    private String de_control;
    private String default_setting;
    private String digital_effect;
    private String iso;
    private String metering_mode;
    private String photo_format;
    private String photo_size;
    private String photo_timelapse;
    private String record_mode;
    private String saturation;
    private String sharpness;
    private String shutter_time;
    private String system_type;
    private String video_quality;
    private String video_resolution;
    private String video_timelapse;

    public String getRecord_mode() {
        return this.record_mode;
    }

    public void setRecord_mode(String record_mode) {
        this.record_mode = record_mode;
    }

    public String getCapture_mode() {
        return this.capture_mode;
    }

    public void setCapture_mode(String capture_mode) {
        this.capture_mode = capture_mode;
    }

    public String getVideo_timelapse() {
        return this.video_timelapse;
    }

    public void setVideo_timelapse(String video_timelapse) {
        this.video_timelapse = video_timelapse;
    }

    public String getPhoto_timelapse() {
        return this.photo_timelapse;
    }

    public void setPhoto_timelapse(String photo_timelapse) {
        this.photo_timelapse = photo_timelapse;
    }

    public String getVideo_quality() {
        return this.video_quality;
    }

    public void setVideo_quality(String video_quality) {
        this.video_quality = video_quality;
    }

    public String getVideo_resolution() {
        return this.video_resolution;
    }

    public void setVideo_resolution(String video_resolution) {
        this.video_resolution = video_resolution;
    }

    public String getPhoto_format() {
        return this.photo_format;
    }

    public void setPhoto_format(String photo_format) {
        this.photo_format = photo_format;
    }

    public String getPhoto_size() {
        return this.photo_size;
    }

    public void setPhoto_size(String photo_size) {
        this.photo_size = photo_size;
    }

    public String getDe_control() {
        return this.de_control;
    }

    public void setDe_control(String de_control) {
        this.de_control = de_control;
    }

    public String getAe_bias() {
        return this.ae_bias;
    }

    public void setAe_bias(String ae_bias) {
        this.ae_bias = ae_bias;
    }

    public String getIso() {
        return this.iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getShutter_time() {
        return this.shutter_time;
    }

    public void setShutter_time(String shutter_time) {
        this.shutter_time = shutter_time;
    }

    public String getAwb() {
        return this.awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getMetering_mode() {
        return this.metering_mode;
    }

    public void setMetering_mode(String metering_mode) {
        this.metering_mode = metering_mode;
    }

    public String getDigital_effect() {
        return this.digital_effect;
    }

    public void setDigital_effect(String digital_effect) {
        this.digital_effect = digital_effect;
    }

    public String getSaturation() {
        return this.saturation;
    }

    public void setSaturation(String saturation) {
        this.saturation = saturation;
    }

    public String getContrast() {
        return this.contrast;
    }

    public void setContrast(String contrast) {
        this.contrast = contrast;
    }

    public String getSharpness() {
        return this.sharpness;
    }

    public void setSharpness(String sharpness) {
        this.sharpness = sharpness;
    }

    public String getSystem_type() {
        return this.system_type;
    }

    public void setSystem_type(String system_type) {
        this.system_type = system_type;
    }

    public String getDefault_setting() {
        return this.default_setting;
    }

    public void setDefault_setting(String default_setting) {
        this.default_setting = default_setting;
    }

    public String toString() {
        return "CurParamsJson{video_quality='" + this.video_quality + CoreConstants.SINGLE_QUOTE_CHAR + ", video_resolution='" + this.video_resolution + CoreConstants.SINGLE_QUOTE_CHAR + ", photo_format='" + this.photo_format + CoreConstants.SINGLE_QUOTE_CHAR + ", photo_size='" + this.photo_size + CoreConstants.SINGLE_QUOTE_CHAR + ", ae_bias='" + this.ae_bias + CoreConstants.SINGLE_QUOTE_CHAR + ", iso='" + this.iso + CoreConstants.SINGLE_QUOTE_CHAR + ", shutter_time='" + this.shutter_time + CoreConstants.SINGLE_QUOTE_CHAR + ", awb='" + this.awb + CoreConstants.SINGLE_QUOTE_CHAR + ", metering_mode='" + this.metering_mode + CoreConstants.SINGLE_QUOTE_CHAR + ", digital_effect='" + this.digital_effect + CoreConstants.SINGLE_QUOTE_CHAR + ", saturation='" + this.saturation + CoreConstants.SINGLE_QUOTE_CHAR + ", contrast='" + this.contrast + CoreConstants.SINGLE_QUOTE_CHAR + ", sharpness='" + this.sharpness + CoreConstants.SINGLE_QUOTE_CHAR + ", system_type='" + this.system_type + CoreConstants.SINGLE_QUOTE_CHAR + ", default_setting='" + this.default_setting + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
