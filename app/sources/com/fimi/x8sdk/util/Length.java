package com.fimi.x8sdk.util;

import java.util.Locale;

public class Length {
    private double lengthInMeters;

    public Length(double lengthInMeters) {
        set(lengthInMeters);
    }

    public double valueInMeters() {
        return this.lengthInMeters;
    }

    public void set(double lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }

    public String toString() {
        if (this.lengthInMeters >= 1000.0d) {
            return String.format(Locale.US, "%2.1f km", new Object[]{Double.valueOf(this.lengthInMeters / 1000.0d)});
        } else if (this.lengthInMeters >= 1.0d) {
            return String.format(Locale.US, "%2.1f m", new Object[]{Double.valueOf(this.lengthInMeters)});
        } else if (this.lengthInMeters < 0.001d) {
            return this.lengthInMeters + " m";
        } else {
            return String.format(Locale.US, "%2.1f mm", new Object[]{Double.valueOf(this.lengthInMeters * 1000.0d)});
        }
    }

    public boolean equals(Object o) {
        if ((o instanceof Length) && this.lengthInMeters == ((Length) o).lengthInMeters) {
            return true;
        }
        return false;
    }
}
