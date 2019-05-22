package com.fimi.x8sdk.jsonResult;

import com.fimi.x8sdk.dataparser.AckCamJsonInfo;
import java.util.List;

public class CameraParamsJson extends AckCamJsonInfo {
    private List<String> options;
    private String permission;

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
