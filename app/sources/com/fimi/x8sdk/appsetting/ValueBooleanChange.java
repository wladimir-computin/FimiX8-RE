package com.fimi.x8sdk.appsetting;

public class ValueBooleanChange {
    private boolean newValue;
    private boolean oldValue;

    public ValueBooleanChange(boolean oldV, boolean newV) {
        this.oldValue = oldV;
        this.newValue = newV;
    }

    public boolean isNewValue() {
        return this.newValue;
    }

    public void setNewValue(boolean newValue) {
        this.newValue = newValue;
    }

    public boolean isOldValue() {
        return this.oldValue;
    }

    public void setOldValue(boolean oldValue) {
        this.oldValue = oldValue;
    }
}
