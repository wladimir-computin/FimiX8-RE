package com.fimi.x8sdk.appsetting;

public class SensityJsonChange {
    private ValueSensity newValue;
    private ValueSensity oldValue;

    public SensityJsonChange(ValueSensity oldV, ValueSensity newV) {
        this.newValue = newV;
        this.oldValue = oldV;
    }

    public ValueSensity getNewValue() {
        return this.newValue;
    }

    public void setNewValue(ValueSensity newValue) {
        this.newValue = newValue;
    }

    public ValueSensity getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(ValueSensity oldValue) {
        this.oldValue = oldValue;
    }
}
