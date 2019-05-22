package com.fimi.x8sdk.appsetting;

public class ValueFloat {
    private float newValue;

    public ValueFloat(float newValue) {
        setNewValue(newValue);
    }

    public float getNewValue() {
        return this.newValue;
    }

    public void setNewValue(float newValue) {
        this.newValue = newValue;
    }
}
