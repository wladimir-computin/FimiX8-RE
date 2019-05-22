package com.fimi.app.x8s.map.interfaces;

import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;

public abstract class AbsFimiMap extends AbsMap {
    public abstract void snapshot(IFimiOnSnapshotReady iFimiOnSnapshotReady);
}
