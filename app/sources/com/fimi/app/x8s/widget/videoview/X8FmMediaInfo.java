package com.fimi.app.x8s.widget.videoview;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;

public class X8FmMediaInfo implements Serializable {
    private String duration;
    private String name;
    private String path;

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String toString() {
        return "X8FmMediaInfo{name='" + this.name + CoreConstants.SINGLE_QUOTE_CHAR + ", duration='" + this.duration + CoreConstants.SINGLE_QUOTE_CHAR + ", path='" + this.path + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
    }
}
