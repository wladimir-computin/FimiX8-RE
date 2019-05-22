package com.fimi.x8sdk.appsetting;

public class ValueBoolean {
    private boolean newValue;

    public ValueBoolean(boolean newValue) {
        setNewValue(newValue);
    }

    public boolean isNewValue() {
        return this.newValue;
    }

    public void setNewValue(boolean newValue) {
        this.newValue = newValue;
    }
}
