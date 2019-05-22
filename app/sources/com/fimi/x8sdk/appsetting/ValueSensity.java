package com.fimi.x8sdk.appsetting;

public class ValueSensity {
    private float pitch;
    private float roll;
    private float thro;
    private float yaw;

    public ValueSensity(float pitch, float roll, float thro, float yaw) {
        setPitch(pitch);
        setRoll(roll);
        setThro(thro);
        setYaw(yaw);
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return this.roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getThro() {
        return this.thro;
    }

    public void setThro(float thro) {
        this.thro = thro;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
