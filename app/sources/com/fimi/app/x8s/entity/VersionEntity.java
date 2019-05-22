package com.fimi.app.x8s.entity;

import android.content.Context;
import com.fimi.app.x8s.R;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.twitter.sdk.android.core.internal.scribe.EventsFilesManager;

public class VersionEntity {
    private boolean hasNewVersion;
    private int tag;
    private AckVersion version;
    private String versionCode;
    private String versionName;

    public boolean isHasNewVersion() {
        return this.hasNewVersion;
    }

    public VersionEntity(int tag, String versionName, String versionCode, boolean hasNewVersion) {
        this.tag = tag;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.hasNewVersion = hasNewVersion;
    }

    public VersionEntity(Context context, String versionName, AckVersion version) {
        this.versionName = versionName;
        if (version != null) {
            String dt = version.getVersionDetails();
            if (dt != null) {
                String[] split = dt.split(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
                if (split.length > 0) {
                    dt = split[split.length - 1];
                }
            } else {
                dt = "";
            }
            this.versionCode = "" + version.getSoftVersion() + "" + dt;
        } else {
            this.versionCode = context.getString(R.string.x8_na);
        }
        this.versionCode = this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public boolean hasNewVersion() {
        return this.hasNewVersion;
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
