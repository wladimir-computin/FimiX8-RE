package com.fimi.x8sdk.appsetting;

public class SensityJson {
    private ValueSensity newValue;

    public SensityJson(ValueSensity newValue) {
        setNewValue(newValue);
    }

    public ValueSensity getNewValue() {
        return this.newValue;
    }

    public void setNewValue(ValueSensity newValue) {
        this.newValue = newValue;
    }
}
