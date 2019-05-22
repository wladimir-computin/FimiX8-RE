package com.fimi.x8sdk.appsetting;

public class ValueFloatChange {
    private float newValue;
    private float oldValue;

    public ValueFloatChange(float oldValue, float newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public float getNewValue() {
        return this.newValue;
    }

    public void setNewValue(float newValue) {
        this.newValue = newValue;
    }

    public float getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(float oldValue) {
        this.oldValue = oldValue;
    }
}
