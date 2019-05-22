package com.fimi.app.x8s.map.interfaces;

import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.x8sdk.dataparser.AckGetAiLinePoint;
import com.fimi.x8sdk.dataparser.AckGetAiLinePointsAction;
import com.google.android.gms.maps.model.Polyline;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsAiLineManager extends AbsBaseManager {
    protected List<Polyline> polylineList = new ArrayList();

    public abstract void addInreterstMarker(int i, int i2);

    public abstract void addOrUpdateSmallMarkerForVideo(int i);

    public abstract void addSmallMakerByHistory();

    public abstract void addSmallMarkerByInterest();

    public abstract void addSmallMarkerByMap(int i);

    public abstract void changeLine();

    public abstract void clearAiLineMarker();

    public abstract void clearAllSmallMarker();

    public abstract void deleteMarker(boolean z);

    public abstract float getAiLineDistance();

    public abstract int getAiLinePointSize();

    public abstract float getDistance(MapPointLatLng mapPointLatLng, MapPointLatLng mapPointLatLng2);

    public abstract List<MapPointLatLng> getInterestPointList();

    public abstract MapPointLatLng getInterstMakerLatLng();

    public abstract MapPointLatLng getMapPointLatLng();

    public abstract List<MapPointLatLng> getMapPointList();

    public abstract List<MapPointLatLng> getMapPointList(MapPointLatLng mapPointLatLng);

    public abstract float getPointAngle(MapPointLatLng mapPointLatLng, MapPointLatLng mapPointLatLng2);

    public abstract boolean hasPointUnBind();

    public abstract boolean isFarToHome();

    public abstract boolean isInterestBeBind(MapPointLatLng mapPointLatLng);

    public abstract void notityChangeView(MapPointLatLng mapPointLatLng);

    public abstract void notityChangeView(MapPointLatLng mapPointLatLng, boolean z);

    public abstract void removeInterestUnBebind();

    public abstract void removeInterstPointByRunning();

    public abstract void setAiLineIndexPoint(int i);

    public abstract void setAiLineMark(double d, double d2, float f, float f2);

    public abstract void setAiLineMarkActionByDevice(List<AckGetAiLinePointsAction> list);

    public abstract void setAiLineMarkByDevice(List<AckGetAiLinePoint> list, List<AckGetAiLinePoint> list2);

    public abstract void setAiLineMarkByHistory(List<X8AiLinePointLatlngInfo> list, int i);

    public abstract void setAiLineMarkByHistory(List<MapPointLatLng> list, List<MapPointLatLng> list2);

    public abstract void setLineMarkerSelectListener(IX8MarkerListener iX8MarkerListener);

    public abstract void startAiLineProcess();

    public abstract void updataAngle(int i, float f);

    public abstract void updateInterestBindPoint(MapPointLatLng mapPointLatLng, int i);

    public abstract void updateInterestPoint();

    public abstract void updateSmallMarkerAngle(MapPointLatLng mapPointLatLng);
}
