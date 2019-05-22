package com.fimi.x8sdk.common;

import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.connect.ConnectType;
import com.fimi.x8sdk.map.MapType;

public final class GlobalConfig {
    private static GlobalConfig globalConfig = new GlobalConfig();
    ConnectType connectType = ConnectType.Aoa;
    private boolean isLowLanding;
    private boolean isLowReturn;
    private boolean isMap;
    private boolean isNewHand;
    boolean isRectification;
    private boolean isShowLog;
    private boolean isShowmMtric = true;
    int lowPowerOperation;
    int lowPowerValue;
    private int mGridLine = 0;
    int mapStyle;
    MapType mapType = null;
    int seriousLowPowerOperation;
    int seriousLowPowerValue;

    public static class Builder {
        private boolean isLowLanding;
        private boolean isLowReturn;
        private boolean isRectification;
        private boolean isShowLog;
        private boolean isShowmMtric;
        int lowPowerOperation;
        int lowPowerValue;
        private int mGridLine;
        private int mapStyle;
        private MapType mapType;
        int seriousLowPowerOperation;
        int seriousLowPowerValue;

        public boolean isLowReturn() {
            return this.isLowReturn;
        }

        public void setLowReturn(boolean lowReturn) {
            this.isLowReturn = lowReturn;
        }

        public boolean isLowLanding() {
            return this.isLowLanding;
        }

        public void setLowLanding(boolean lowLanding) {
            this.isLowLanding = lowLanding;
        }

        public Builder setShowmMtric(boolean showmMtric) {
            this.isShowmMtric = showmMtric;
            return this;
        }

        public Builder setGridLine(int gridLine) {
            this.mGridLine = gridLine;
            return this;
        }

        public Builder setShowLog(boolean showLog) {
            this.isShowLog = showLog;
            return this;
        }

        public Builder setMapType(MapType mapType) {
            this.mapType = mapType;
            return this;
        }

        public Builder setMapStyle(int mapStyle) {
            this.mapStyle = mapStyle;
            return this;
        }

        public Builder setRectification(boolean isRectification) {
            this.isRectification = isRectification;
            return this;
        }

        public Builder setLowPowerValue(int lowPowerValue) {
            this.lowPowerValue = lowPowerValue;
            return this;
        }

        public Builder setSeriousLowPowerValue(int seriousLowPowerValue) {
            this.seriousLowPowerValue = seriousLowPowerValue;
            return this;
        }

        public Builder setLowPowerOpration(int lowPowerOperation) {
            this.lowPowerOperation = lowPowerOperation;
            return this;
        }

        public Builder setSeriousLowPowerOperation(int seriousLowPowerOperation) {
            this.seriousLowPowerOperation = seriousLowPowerOperation;
            return this;
        }
    }

    public boolean isAOAConnect() {
        return ConnectType.Aoa.equals(this.connectType);
    }

    public boolean isLowReturn() {
        return this.isLowReturn;
    }

    public void setLowReturn(boolean lowReturn) {
        this.isLowReturn = lowReturn;
        SPStoreManager.getInstance().saveBoolean(Constants.X8_LOW_POWER_RETURN, this.isLowReturn);
    }

    public boolean isLowLanding() {
        return this.isLowLanding;
    }

    public void setLowLanding(boolean lowLanding) {
        this.isLowLanding = lowLanding;
        SPStoreManager.getInstance().saveBoolean(Constants.X8_LOW_POWER_LANDING, this.isLowLanding);
    }

    public static GlobalConfig getInstance() {
        return globalConfig;
    }

    public void init(Builder builder) {
        this.mapType = builder.mapType;
        this.mapStyle = builder.mapStyle;
        this.lowPowerValue = builder.lowPowerValue;
        this.seriousLowPowerValue = builder.seriousLowPowerValue;
        this.lowPowerOperation = builder.lowPowerOperation;
        this.seriousLowPowerOperation = builder.seriousLowPowerOperation;
        this.isRectification = builder.isRectification;
        this.isShowLog = builder.isShowLog;
        this.mGridLine = builder.mGridLine;
        this.isShowmMtric = builder.isShowmMtric;
        this.isLowLanding = builder.isLowLanding;
        this.isLowReturn = builder.isLowReturn;
        SessionManager.initInstance();
        NoticeManager.initInstance();
    }

    public boolean isRectification() {
        return this.isRectification;
    }

    public MapType getMapType() {
        return this.mapType;
    }

    public boolean isShowLog() {
        return this.isShowLog;
    }

    public int getGridLine() {
        return this.mGridLine;
    }

    public boolean isShowmMtric() {
        return this.isShowmMtric;
    }

    public static void setGlobalConfig(GlobalConfig globalConfig) {
        globalConfig = globalConfig;
    }

    public int getMapStyle() {
        return this.mapStyle;
    }

    public void setMapStyle(int mapStyle) {
        this.mapStyle = mapStyle;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public void setRectification(boolean rectification) {
        this.isRectification = rectification;
    }

    public void setShowLog(boolean showLog) {
        this.isShowLog = showLog;
    }

    public void setMap(boolean map) {
        this.isMap = map;
    }

    public void setGridLine(int gridLine) {
        this.mGridLine = gridLine;
        SPStoreManager.getInstance().saveInt(Constants.X8_GLINE_LINE_OPTION, this.mGridLine);
    }

    public void setShowmMtric(boolean showmMtric) {
        this.isShowmMtric = showmMtric;
    }

    public int getLowPowerValue() {
        return this.lowPowerValue;
    }

    public void setLowPowerValue(int lowPowerValue) {
        this.lowPowerValue = lowPowerValue;
    }

    public int getSeriousLowPowerValue() {
        return this.seriousLowPowerValue;
    }

    public void setSeriousLowPowerValue(int seriousLowPowerValue) {
        this.seriousLowPowerValue = seriousLowPowerValue;
    }

    public int getLowPowerOperation() {
        return this.lowPowerOperation;
    }

    public void setLowPowerOperation(int lowPowerOperation) {
        this.lowPowerOperation = lowPowerOperation;
    }

    public int getSeriousLowPowerOperation() {
        return this.seriousLowPowerOperation;
    }

    public void setSeriousLowPowerOperation(int seriousLowPowerOperation) {
        this.seriousLowPowerOperation = seriousLowPowerOperation;
    }

    public boolean isNewHand() {
        return this.isNewHand;
    }

    public void setNewHand(boolean newHand) {
        this.isNewHand = newHand;
    }
}
