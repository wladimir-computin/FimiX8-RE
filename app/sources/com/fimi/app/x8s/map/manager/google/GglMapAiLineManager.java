package com.fimi.app.x8s.map.manager.google;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.interfaces.AbsAiLineManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.google.GglMapCustomMarkerView;
import com.fimi.app.x8s.tools.GeoTools;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.dataparser.AckGetAiLinePoint;
import com.fimi.x8sdk.dataparser.AckGetAiLinePointsAction;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.GpsCorrect;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

public class GglMapAiLineManager extends AbsAiLineManager implements OnMapClickListener, OnMarkerClickListener, OnMarkerDragListener {
    private final int MAX = 20;
    private List<Marker> arrowMarkerList = new ArrayList();
    private Context context;
    private GglMapCustomMarkerView gdCustemMarkerView;
    private GglMapLocationManager gglMapLocationManager;
    private GoogleMap googleMap;
    private List<Marker> interestMarkerList = new ArrayList();
    private boolean isClick = true;
    private Circle limitCircle;
    private IX8MarkerListener lineMarkerSelectListener;
    private List<MapPointLatLng> mMapPointList = new ArrayList();
    private List<Marker> mMarkerList = new ArrayList();
    private Marker mSelectMarker = null;
    private int nPos = -1;

    public GglMapAiLineManager(Context context, GoogleMap googleMap, GglMapLocationManager gglMapLocationManager) {
        this.context = context;
        this.googleMap = googleMap;
        this.gglMapLocationManager = gglMapLocationManager;
        this.gdCustemMarkerView = new GglMapCustomMarkerView();
    }

    public void setMarkerViewInfo(float height) {
        if (this.mSelectMarker != null) {
            MapPointLatLng mp = (MapPointLatLng) this.mSelectMarker.getTag();
            if (mp.isSelect) {
                mp.altitude = height;
                changePointMarker(this.mSelectMarker, mp, false);
            }
        }
    }

    public float getLineAngleByMapBealing(float angle) {
        return angle - this.googleMap.getCameraPosition().bearing;
    }

    public void setOnMapClickListener() {
        setOnMarkerListener();
    }

    public void removeMapClickListener() {
        this.googleMap.setOnMapClickListener(null);
        this.googleMap.setOnMarkerClickListener(null);
    }

    public void resetMapEvent() {
        this.googleMap.setOnMapClickListener(null);
        removeMarkerListener();
        this.googleMap.setOnMarkerDragListener(null);
    }

    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            this.limitCircle = this.googleMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.strokeColor).fillColor(this.fillColor).strokeWidth((float) this.strokeWidth));
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }

    public void removeMarkerListener() {
        this.googleMap.setOnMarkerClickListener(null);
    }

    public boolean isFullSize() {
        return this.mMapPointList.size() == 20;
    }

    public boolean isValid(LatLng latLng) {
        boolean ret = true;
        if (this.mMapPointList.size() < 1) {
            return 1;
        }
        for (MapPointLatLng mapPointLatLng : this.mMapPointList) {
            if (((float) GeoTools.getDistance(latLng, new LatLng(mapPointLatLng.latitude, mapPointLatLng.longitude)).valueInMeters()) <= 10.0f) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public void setAiLineMark(double latitude, double logitude, float height, float angle) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
        this.nPos = -1;
        onAiLineAddPoint(latLng, height, angle);
    }

    public void deleteMarker(boolean isMapPoint) {
        deleteMarker(isMapPoint, this.gglMapLocationManager.getDevLocation());
    }

    public int getAiLinePointSize() {
        return this.mMapPointList.size();
    }

    public float getAiLineDistance() {
        float distance = 0.0f;
        if (this.mMarkerList.size() == 2) {
            return (float) GeoTools.getDistance(((Marker) this.mMarkerList.get(0)).getPosition(), ((Marker) this.mMarkerList.get(1)).getPosition()).valueInMeters();
        }
        for (int i = 0; i < this.mMarkerList.size(); i++) {
            if (i != 0) {
                distance += (float) GeoTools.getDistance(((Marker) this.mMarkerList.get(i - 1)).getPosition(), ((Marker) this.mMarkerList.get(i)).getPosition()).valueInMeters();
            }
        }
        return distance;
    }

    public List<MapPointLatLng> getMapPointList() {
        return this.mMapPointList;
    }

    public List<MapPointLatLng> getMapPointList(MapPointLatLng mpl) {
        List<MapPointLatLng> list = new ArrayList();
        for (MapPointLatLng p : this.mMapPointList) {
            if (p.mInrertestPoint == null) {
                list.add(p);
            } else if (p.mInrertestPoint == mpl) {
                list.add(p);
            }
        }
        return list;
    }

    public boolean isInterestBeBind(MapPointLatLng mpl) {
        List<MapPointLatLng> list = new ArrayList();
        for (MapPointLatLng p : this.mMapPointList) {
            if (p.mInrertestPoint == mpl) {
                return true;
            }
        }
        return false;
    }

    public void removeInterestUnBebind() {
        MapPointLatLng p;
        boolean z = false;
        Marker tmp = null;
        for (Marker m : this.interestMarkerList) {
            MapPointLatLng mpl = (MapPointLatLng) m.getTag();
            boolean isFind = false;
            for (MapPointLatLng p2 : this.mMapPointList) {
                if (p2.mInrertestPoint != null && p2.mInrertestPoint == mpl) {
                    isFind = true;
                    continue;
                    break;
                }
            }
            if (!isFind) {
                tmp = m;
                break;
            }
        }
        if (tmp != null) {
            this.interestMarkerList.remove(tmp);
            tmp.remove();
            int i = 0;
            for (Marker marker : this.interestMarkerList) {
                i++;
                p2 = (MapPointLatLng) marker.getTag();
                p2.nPos = i;
                changePointMarker(marker, p2, false);
            }
        }
        if (this.lineMarkerSelectListener != null) {
            IX8MarkerListener iX8MarkerListener = this.lineMarkerSelectListener;
            if (this.interestMarkerList.size() < 20) {
                z = true;
            }
            iX8MarkerListener.onInterestSizeEnable(z);
        }
    }

    public boolean hasPointUnBind() {
        List<MapPointLatLng> list = new ArrayList();
        for (MapPointLatLng p : this.mMapPointList) {
            if (p.mInrertestPoint == null) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x002f  */
    public void updateInterestBindPoint(com.fimi.app.x8s.map.model.MapPointLatLng r7, int r8) {
        /*
        r6 = this;
        r0 = 0;
    L_0x0001:
        r3 = r6.mMapPointList;
        r3 = r3.size();
        if (r0 >= r3) goto L_0x002b;
    L_0x0009:
        if (r0 != r8) goto L_0x0042;
    L_0x000b:
        r3 = r6.mMapPointList;
        r2 = r3.get(r8);
        r2 = (com.fimi.app.x8s.map.model.MapPointLatLng) r2;
        r2.mInrertestPoint = r7;
        r1 = 1;
        if (r7 == 0) goto L_0x0040;
    L_0x0018:
        r3 = r6.getPointAngle(r2, r7);
        r2.setAngle(r3);
        r1 = 1;
    L_0x0020:
        r3 = r6.mMarkerList;
        r3 = r3.get(r0);
        r3 = (com.google.android.gms.maps.model.Marker) r3;
        r6.changePointMarker(r3, r2, r1);
    L_0x002b:
        r3 = r6.lineMarkerSelectListener;
        if (r3 == 0) goto L_0x003f;
    L_0x002f:
        r4 = r6.lineMarkerSelectListener;
        r3 = r6.interestMarkerList;
        r3 = r3.size();
        r5 = 20;
        if (r3 >= r5) goto L_0x0045;
    L_0x003b:
        r3 = 1;
    L_0x003c:
        r4.onInterestSizeEnable(r3);
    L_0x003f:
        return;
    L_0x0040:
        r1 = 0;
        goto L_0x0020;
    L_0x0042:
        r0 = r0 + 1;
        goto L_0x0001;
    L_0x0045:
        r3 = 0;
        goto L_0x003c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.app.x8s.map.manager.google.GglMapAiLineManager.updateInterestBindPoint(com.fimi.app.x8s.map.model.MapPointLatLng, int):void");
    }

    public List<MapPointLatLng> getInterestPointList() {
        List<MapPointLatLng> list = new ArrayList();
        for (int i = 0; i < this.interestMarkerList.size(); i++) {
            list.add((MapPointLatLng) ((Marker) this.interestMarkerList.get(i)).getTag());
        }
        return list;
    }

    public void setLineMarkerSelectListener(IX8MarkerListener listener) {
        this.lineMarkerSelectListener = listener;
    }

    public void clearAiLineMarker() {
        clearPointMark();
    }

    public void startAiLineProcess() {
        if (this.mSelectMarker != null) {
            onMarkerClick(this.mSelectMarker);
        }
        this.nPos = -1;
    }

    public void setAiLineIndexPoint(int index) {
        if (this.nPos != index) {
            int i;
            Marker lastMarker;
            MapPointLatLng lastMarkerPoint;
            int n;
            if (this.nPos == -1) {
                i = index;
                if (i >= 0) {
                    int j;
                    for (j = 0; j < i; j++) {
                        lastMarker = (Marker) this.mMarkerList.get(j);
                        lastMarkerPoint = (MapPointLatLng) lastMarker.getTag();
                        lastMarkerPoint.isSelect = false;
                        changeViewBySetPoints(lastMarker, lastMarkerPoint, false, true);
                        lightInterestPointByRunning(lastMarkerPoint, false);
                    }
                    for (j = 1; j < i; j++) {
                        lastMarkerPoint = (MapPointLatLng) ((Marker) this.mMarkerList.get(j)).getTag();
                        if (lastMarkerPoint.yawMode != 0) {
                            n = j * 2;
                            changeLineSmallMarkerByRunning((Marker) this.arrowMarkerList.get(n), (Marker) this.arrowMarkerList.get(n + 1), lastMarkerPoint.showAngle, 2);
                        } else if (this.arrowMarkerList.size() > 0 && lastMarkerPoint.mInrertestPoint != null) {
                            n = findIndexByInterest(lastMarkerPoint) * 2;
                            changeLineSmallMarkerByRunning((Marker) this.arrowMarkerList.get(n), (Marker) this.arrowMarkerList.get(n + 1), lastMarkerPoint.showAngle, 2);
                        }
                    }
                }
            }
            if (this.nPos >= 0) {
                lastMarker = (Marker) this.mMarkerList.get(this.nPos);
                lastMarkerPoint = (MapPointLatLng) lastMarker.getTag();
                lastMarkerPoint.isSelect = false;
                changeViewBySetPoints(lastMarker, lastMarkerPoint, false, true);
                lightInterestPointByRunning(lastMarkerPoint, false);
            }
            Marker currentMarker = (Marker) this.mMarkerList.get(index);
            MapPointLatLng mapPointLatLng = (MapPointLatLng) currentMarker.getTag();
            mapPointLatLng.isSelect = true;
            changeViewByRunning(currentMarker, mapPointLatLng);
            lightInterestPointByRunning(mapPointLatLng, true);
            this.nPos = index;
            if (this.lineMarkerSelectListener != null) {
                this.lineMarkerSelectListener.onRunIndex(index + 1, mapPointLatLng.action);
            }
            for (i = 0; i < this.polylineList.size(); i++) {
                if (i == this.nPos) {
                    ((Polyline) this.polylineList.get(i)).remove();
                    this.polylineList.set(i, getPolyline(this.nPos, (Polyline) this.polylineList.get(i), this.lineRunningColor));
                } else if (i < this.nPos) {
                    ((Polyline) this.polylineList.get(i)).remove();
                    this.polylineList.set(i, getPolyline(this.nPos, (Polyline) this.polylineList.get(i), this.lineRunColor));
                } else {
                    ((Polyline) this.polylineList.get(i)).remove();
                    this.polylineList.set(i, getPolyline(this.nPos, (Polyline) this.polylineList.get(i), this.lineDefaultColor));
                }
            }
            if (mapPointLatLng.yawMode == 0) {
                if (this.arrowMarkerList.size() > 0 && this.nPos > 0) {
                    n = 0;
                    if (mapPointLatLng.mInrertestPoint != null) {
                        index = findIndexByInterest(mapPointLatLng);
                        if (index >= 0) {
                            n = index * 2;
                            if (n < this.arrowMarkerList.size()) {
                                changeLineSmallMarkerByRunning((Marker) this.arrowMarkerList.get(n), (Marker) this.arrowMarkerList.get(n + 1), mapPointLatLng.showAngle, 1);
                            }
                        }
                    }
                    if (this.nPos - 1 > 0) {
                        lastMarkerPoint = (MapPointLatLng) ((Marker) this.mMarkerList.get(this.nPos - 1)).getTag();
                        if (lastMarkerPoint.mInrertestPoint != null) {
                            index = findIndexByInterest(lastMarkerPoint);
                            if (index >= 0 && n < this.arrowMarkerList.size()) {
                                n = index * 2;
                                changeLineSmallMarkerByRunning((Marker) this.arrowMarkerList.get(n), (Marker) this.arrowMarkerList.get(n + 1), lastMarkerPoint.showAngle, 2);
                            }
                        }
                    }
                }
            } else if (this.arrowMarkerList.size() > 0 && this.nPos > 0) {
                n = (this.nPos - 1) * 2;
                if (n < this.arrowMarkerList.size()) {
                    changeLineSmallMarkerByRunning((Marker) this.arrowMarkerList.get(n), (Marker) this.arrowMarkerList.get(n + 1), mapPointLatLng.showAngle, 1);
                    if (n > 0) {
                        n = (this.nPos - 2) * 2;
                        changeLineSmallMarkerByRunning((Marker) this.arrowMarkerList.get(n), (Marker) this.arrowMarkerList.get(n + 1), ((MapPointLatLng) ((Marker) this.mMarkerList.get(this.nPos - 1)).getTag()).showAngle, 2);
                    }
                }
            }
        }
    }

    public int findIndexByInterest(MapPointLatLng mpl) {
        int index = -1;
        for (int i = 1; i < this.mMarkerList.size(); i++) {
            MapPointLatLng current = (MapPointLatLng) ((Marker) this.mMarkerList.get(i)).getTag();
            if (current.mInrertestPoint != null) {
                index++;
                if (mpl == current) {
                    break;
                }
            }
        }
        return index;
    }

    public Polyline getPolyline(int index, Polyline pl, int color) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(pl.getPoints());
        polylineOptions.color(this.context.getResources().getColor(color)).zIndex(3.0f);
        polylineOptions.width(4.0f);
        Polyline line = this.googleMap.addPolyline(polylineOptions);
        line.setPattern(PATTERN_DASHED);
        return line;
    }

    public void setAiLineMarkActionByDevice(List<AckGetAiLinePointsAction> points) {
        if (this.mMarkerList != null && this.mMarkerList.size() > 0) {
            for (Marker m : this.mMarkerList) {
                MapPointLatLng mpl = (MapPointLatLng) m.getTag();
                for (AckGetAiLinePointsAction pointsAction : points) {
                    if (mpl.nPos - 1 == pointsAction.pos) {
                        mpl.action = pointsAction.getAction();
                        break;
                    }
                }
            }
        }
    }

    public void setAiLineMarkByHistory(List<X8AiLinePointLatlngInfo> points, int mapTpye) {
        for (X8AiLinePointLatlngInfo point : points) {
            int res;
            MapPointLatLng mp = new MapPointLatLng();
            if (point.getYawMode() == 0) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createCustomMarkerView2(this.context, res, this.mMarkerList.size() + 1);
            mp.altitude = (float) point.getAltitude();
            mp.nPos = this.mMarkerList.size() + 1;
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
            mp.longitude = latLng.longitude;
            mp.latitude = latLng.latitude;
            mMarker.setFlat(true);
            mMarker.setTag(mp);
            this.mMarkerList.add(mMarker);
            mp.distance = 0.0f;
            this.mMapPointList.add(mp);
        }
        if (points.size() > 0) {
            drawPointLine(this.gglMapLocationManager.getDevLocation());
        }
    }

    public void updataAngle(int i, float angle) {
        ((MapPointLatLng) ((Marker) this.mMarkerList.get(i)).getTag()).showAngle = angle;
    }

    public MapPointLatLng getMapPointLatLng() {
        if (this.mSelectMarker == null) {
            return null;
        }
        return (MapPointLatLng) this.mSelectMarker.getTag();
    }

    public void addInreterstMarker(int x, int y) {
        boolean z = true;
        LatLng latLng = this.googleMap.getProjection().fromScreenLocation(new Point(x, y));
        MapPointLatLng mp = new MapPointLatLng();
        mp.isIntertestPoint = true;
        mp.isMapPoint = true;
        mp.nPos = this.interestMarkerList.size() + 1;
        int h = Math.round(StateManager.getInstance().getX8Drone().getHeight());
        if (h <= 5) {
            h = 5;
        }
        mp.altitude = (float) h;
        mp.latitude = latLng.latitude;
        mp.longitude = latLng.longitude;
        Marker interestMarker = addInterestMarker(latLng, (float) h, mp);
        interestMarker.setTag(mp);
        interestMarker.setDraggable(false);
        this.interestMarkerList.add(interestMarker);
        if (this.lineMarkerSelectListener != null) {
            IX8MarkerListener iX8MarkerListener = this.lineMarkerSelectListener;
            if (this.interestMarkerList.size() >= 20) {
                z = false;
            }
            iX8MarkerListener.onInterestSizeEnable(z);
        }
        this.isClick = false;
        onMarkerClick(interestMarker);
    }

    public void addInterestByDevice(AckGetAiLinePoint point) {
        MapPointLatLng mp = new MapPointLatLng();
        mp.isIntertestPoint = true;
        mp.isMapPoint = true;
        mp.nPos = this.interestMarkerList.size() + 1;
        mp.altitude = (float) point.getAltitudePOI();
        mp.latitude = point.getLatitudePOI();
        mp.longitude = point.getLongitudePOI();
        Marker interestMarker = addInterestMarker(new LatLng(mp.latitude, mp.longitude), mp.altitude, mp);
        interestMarker.setTag(mp);
        interestMarker.setDraggable(true);
        this.interestMarkerList.add(interestMarker);
    }

    public int findInterestPoint(AckGetAiLinePoint point, List<AckGetAiLinePoint> interestPoints) {
        int i = -1;
        boolean isFind = false;
        for (AckGetAiLinePoint o : interestPoints) {
            i++;
            if (point.getLongitudePOI() == o.getLongitudePOI() && point.getLatitudePOI() == o.getLatitudePOI() && point.getAltitudePOI() == o.getAltitudePOI()) {
                isFind = true;
                break;
            }
        }
        if (isFind) {
            return i;
        }
        return -1;
    }

    public MapPointLatLng getInterstMakerLatLng() {
        return null;
    }

    public void removeInterstPointByRunning() {
        for (Marker m : this.interestMarkerList) {
            m.setDraggable(false);
        }
    }

    public float getPointAngle(MapPointLatLng from, MapPointLatLng to) {
        float angle = this.mapCalcAngle.getAngle(new LatLng(from.latitude, from.longitude), new LatLng(to.latitude, to.longitude)) % 360.0f;
        if (angle < 0.0f) {
            return angle + 360.0f;
        }
        return angle;
    }

    public void notityChangeView(MapPointLatLng des, boolean isRelation) {
        lightPoint(des, isRelation);
    }

    public void notityChangeView(MapPointLatLng des) {
        for (Marker marker : this.mMarkerList) {
            if (des == ((MapPointLatLng) marker.getTag())) {
                changePointMarker(marker, des, false);
                lightPoint(des, true);
                return;
            }
        }
    }

    private void setOnMarkerListener() {
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMarkerDragListener(this);
    }

    private void clearPointMark() {
        for (Marker marker : this.mMarkerList) {
            marker.remove();
        }
        for (Marker marker2 : this.interestMarkerList) {
            marker2.remove();
        }
        for (Marker marker22 : this.arrowMarkerList) {
            marker22.remove();
        }
        clearMarker();
    }

    public void onMapClick(LatLng latLng) {
        onMapClickForAiLine(latLng);
    }

    public boolean onMarkerClick(Marker marker) {
        if (isOnMarkerClickValid()) {
            MapPointLatLng mp = (MapPointLatLng) marker.getTag();
            if (mp != null) {
                if (this.mSelectMarker == null) {
                    this.mSelectMarker = marker;
                    mp.isSelect = true;
                    changePointMarker(this.mSelectMarker, mp, false);
                    lightPoint(mp, true);
                } else if (this.mSelectMarker.getTag() == marker.getTag()) {
                    mp.isSelect = false;
                    this.mSelectMarker = null;
                    changePointMarker(marker, mp, false);
                    lightPoint(mp, false);
                } else {
                    Marker lastMarker = this.mSelectMarker;
                    MapPointLatLng lastMp = (MapPointLatLng) lastMarker.getTag();
                    lastMp.isSelect = false;
                    changePointMarker(lastMarker, lastMp, false);
                    lightPoint(lastMp, false);
                    this.mSelectMarker = marker;
                    mp.isSelect = true;
                    changePointMarker(this.mSelectMarker, mp, false);
                    lightPoint(mp, true);
                }
                this.lineMarkerSelectListener.onMarkerSelect(mp.isSelect, mp.altitude, mp, this.isClick);
                this.isClick = true;
            }
        }
        return true;
    }

    public void lightPoint(MapPointLatLng mpl, boolean isRelation) {
        if (!mpl.isMapPoint) {
            return;
        }
        MapPointLatLng tmp;
        if (mpl.isIntertestPoint) {
            for (Marker marker : this.mMarkerList) {
                tmp = (MapPointLatLng) marker.getTag();
                if (tmp.mInrertestPoint == mpl) {
                    changePointMarker(marker, tmp, isRelation);
                }
            }
        } else if (mpl.mInrertestPoint != null) {
            for (Marker interest : this.interestMarkerList) {
                tmp = (MapPointLatLng) interest.getTag();
                if (mpl.mInrertestPoint == tmp) {
                    changePointMarker(interest, tmp, isRelation);
                    return;
                }
            }
        }
    }

    public void lightInterestPointByRunning(MapPointLatLng mpl, boolean isLight) {
        if (mpl.isMapPoint && mpl.mInrertestPoint != null) {
            for (Marker interest : this.interestMarkerList) {
                MapPointLatLng tmp = (MapPointLatLng) interest.getTag();
                if (mpl.mInrertestPoint == tmp) {
                    tmp.isSelect = isLight;
                    changePointMarker(interest, tmp, false);
                    return;
                }
            }
        }
    }

    public void onMarkerDragStart(Marker marker) {
        MapPointLatLng mp = (MapPointLatLng) marker.getTag();
        marker.setIcon(this.gdCustemMarkerView.createMapPioView(this.context, R.drawable.x8_img_ai_line_inreterst_max2, mp.altitude, mp.nPos, true, false));
    }

    public void onMarkerDrag(Marker marker) {
        if (this.lineMarkerSelectListener != null) {
            Rect rect = this.lineMarkerSelectListener.getDeletePosition();
            Point mPoint = this.googleMap.getProjection().toScreenLocation(marker.getPosition());
            if (rect.left > mPoint.x || mPoint.x > rect.right || rect.top > mPoint.y || mPoint.y <= rect.bottom) {
            }
        }
    }

    public void onMarkerDragEnd(Marker marker) {
        MapPointLatLng mp = (MapPointLatLng) marker.getTag();
        marker.setIcon(this.gdCustemMarkerView.createMapPioView(this.context, R.drawable.x8_img_ai_line_inreterst_max1, mp.altitude, mp.nPos, false, false));
        MapPointLatLng tmp = (MapPointLatLng) marker.getTag();
        tmp.isInrertestPointActive = true;
        tmp.latitude = marker.getPosition().latitude;
        tmp.longitude = marker.getPosition().longitude;
        int i = 0;
        for (MapPointLatLng mpl : this.mMapPointList) {
            if (mpl.mInrertestPoint != null && tmp == mpl.mInrertestPoint) {
                mpl.setAngle(getPointAngle(mpl, tmp));
                changePointMarker((Marker) this.mMarkerList.get(i), mpl, false);
            }
            i++;
        }
    }

    public void addPointLatLng(LatLng latLng, float distance, LatLng deviceLocation, boolean isMapPoint, float angle) {
        float height;
        MapPointLatLng mp = new MapPointLatLng();
        mp.nPos = this.mMarkerList.size() + 1;
        if (!isMapPoint) {
            height = StateManager.getInstance().getX8Drone().getHeight();
            if (height < 5.0f) {
                height = 5.0f;
            }
        } else if (this.mMarkerList.size() == 0) {
            height = StateManager.getInstance().getX8Drone().getHeight();
            if (height < 5.0f) {
                height = 5.0f;
            }
        } else {
            height = ((MapPointLatLng) ((Marker) this.mMarkerList.get(this.mMarkerList.size() - 1)).getTag()).altitude;
        }
        mp.yawMode = this.lineMarkerSelectListener.getOration();
        Marker mMarker = addPointMarker(isMapPoint, latLng, mp, (float) Math.round(height), angle);
        mp.longitude = latLng.longitude;
        mp.latitude = latLng.latitude;
        mMarker.setTag(mp);
        this.mMarkerList.add(mMarker);
        mp.distance = distance;
        this.mMapPointList.add(mp);
        drawPointLine(deviceLocation);
        this.lineMarkerSelectListener.onMarkerSizeChange(this.mMarkerList.size());
        this.isClick = false;
        int size;
        if (isMapPoint) {
            if (this.mMarkerList.size() > 1 && this.lineMarkerSelectListener.getOration() == 2) {
                size = this.mMarkerList.size();
                addSmallMakerByMap1((Marker) this.mMarkerList.get(size - 2), (Marker) this.mMarkerList.get(size - 1));
            }
        } else if (this.mMarkerList.size() > 1) {
            size = this.mMarkerList.size();
            addSmallMaker((Marker) this.mMarkerList.get(size - 2), (Marker) this.mMarkerList.get(size - 1));
        }
        onMarkerClick(mMarker);
    }

    public void addSmallMakerByMap1(Marker marker1, Marker marker2) {
        MapPointLatLng mpl1 = (MapPointLatLng) marker1.getTag();
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
        mpl2.setAngle(getPointAngle(mpl1, mpl2));
        mpl2.yawMode = this.lineMarkerSelectListener.getOration();
        float[] angleArray = new float[]{mpl2.showAngle, mpl2.showAngle};
        for (int i = 0; i < latLng.length; i++) {
            MapPointLatLng mpl = new MapPointLatLng();
            mpl.isSelect = true;
            mpl.setAngle(angleArray[i]);
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true)).anchor(0.5f, 0.5f).draggable(false));
            mMarker.setTag(mpl);
            mMarker.setFlat(true);
            this.arrowMarkerList.add(mMarker);
        }
    }

    public void addSmallMaker(Marker marker1, Marker marker2) {
        MapPointLatLng mpl1 = (MapPointLatLng) marker1.getTag();
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        if (mpl1.yawMode != 0) {
            LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
            float[] angleArray = new float[2];
            if (mpl1.yawMode == 2) {
                mpl2.showAngle = getPointAngle(mpl1, mpl2);
                angleArray[0] = mpl2.showAngle;
                angleArray[1] = mpl2.showAngle;
            } else if (mpl1.yawMode == 1) {
                angleArray = this.mapCalcAngle.getAnlgesByRoration(mpl1.showAngle, mpl2.showAngle, mpl2.roration);
            }
            for (int i = 0; i < latLng.length; i++) {
                MapPointLatLng mpl = new MapPointLatLng();
                mpl.isSelect = true;
                mpl.setAngle(angleArray[i]);
                Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true)).anchor(0.5f, 0.5f).draggable(false));
                mMarker.setTag(mpl);
                mMarker.setFlat(true);
                this.arrowMarkerList.add(mMarker);
            }
        }
    }

    public void deleteSmallMaker(Marker deleteMarker) {
        if (this.arrowMarkerList.size() > 0) {
            int index = ((MapPointLatLng) deleteMarker.getTag()).nPos - 1;
            if (index != 0) {
                int n = (index - 1) * 2;
                Marker m1 = (Marker) this.arrowMarkerList.get(n);
                m1.remove();
                Marker m2 = (Marker) this.arrowMarkerList.get(n + 1);
                m2.remove();
                this.arrowMarkerList.remove(m1);
                this.arrowMarkerList.remove(m2);
            }
        }
    }

    public void updateSmallMarkerAngle(MapPointLatLng target) {
        int index = target.nPos - 1;
        if (index != 0) {
            int n = (index - 1) * 2;
            MapPointLatLng mpl1 = (MapPointLatLng) ((Marker) this.arrowMarkerList.get(n)).getTag();
            MapPointLatLng mpl2 = (MapPointLatLng) ((Marker) this.arrowMarkerList.get(n + 1)).getTag();
            float[] angleArray = this.mapCalcAngle.getAnlgesByRoration(((MapPointLatLng) ((Marker) this.mMarkerList.get(index - 1)).getTag()).showAngle, target.showAngle, target.roration);
            mpl1.isSelect = true;
            mpl1.setAngle(angleArray[0]);
            ((Marker) this.arrowMarkerList.get(n)).setIcon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl1.showAngle, true));
            ((Marker) this.arrowMarkerList.get(n)).setAnchor(0.5f, 0.5f);
            mpl2.isSelect = true;
            mpl2.setAngle(angleArray[1]);
            ((Marker) this.arrowMarkerList.get(n + 1)).setIcon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl2.showAngle, true));
            ((Marker) this.arrowMarkerList.get(n + 1)).setAnchor(0.5f, 0.5f);
        }
    }

    public void addSmallMarkerByMap(int type) {
        if (type == 0) {
            int i = 0;
            clearAllInterestMarker();
            for (Marker marker : this.mMarkerList) {
                MapPointLatLng p = (MapPointLatLng) marker.getTag();
                clearAllInterestMarkerByMap(p);
                changeViewBySetPoints(marker, p, false, false);
                if (i != 0) {
                    addSmallMakerByMap((Marker) this.mMarkerList.get(i - 1), (Marker) this.mMarkerList.get(i));
                }
                i++;
            }
        }
    }

    public void clearAllInterestMarker() {
        if (this.mSelectMarker != null && ((MapPointLatLng) this.mSelectMarker.getTag()).isIntertestPoint) {
            this.mSelectMarker = null;
        }
        for (Marker marker : this.interestMarkerList) {
            marker.remove();
        }
        this.interestMarkerList.clear();
    }

    public void clearAllInterestMarkerByMap(MapPointLatLng tmp) {
        tmp.mInrertestPoint = null;
    }

    public void addSmallMakerByMap(Marker marker1, Marker marker2) {
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
        float[] angleArray = new float[]{mpl2.showAngle, mpl2.showAngle};
        for (int i = 0; i < latLng.length; i++) {
            MapPointLatLng mpl = new MapPointLatLng();
            mpl.isSelect = true;
            mpl.setAngle(angleArray[i]);
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true)).anchor(0.5f, 0.5f).draggable(false));
            mMarker.setTag(mpl);
            mMarker.setFlat(true);
            this.arrowMarkerList.add(mMarker);
        }
    }

    public void clearAllSmallMarker() {
        for (Marker marker : this.arrowMarkerList) {
            marker.remove();
        }
        this.arrowMarkerList.clear();
        int i = 0;
        for (Marker marker2 : this.mMarkerList) {
            MapPointLatLng p = (MapPointLatLng) marker2.getTag();
            p.isSelect = false;
            if (this.mSelectMarker != null && ((MapPointLatLng) this.mSelectMarker.getTag()) == p) {
                p.isSelect = true;
            }
            changeViewBySetPoints(marker2, p, false, false);
            i++;
        }
    }

    public void addOrUpdateSmallMarkerForVideo(int type) {
        clearAllSmallMarker();
        int j = 0;
        for (Marker marker : this.mMarkerList) {
            MapPointLatLng p = (MapPointLatLng) marker.getTag();
            if (j != 0) {
                MapPointLatLng mpl2 = (MapPointLatLng) ((Marker) this.mMarkerList.get(j)).getTag();
                LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(((Marker) this.mMarkerList.get(j - 1)).getPosition(), ((Marker) this.mMarkerList.get(j)).getPosition(), 3);
                float[] angleArray = new float[]{mpl2.showAngle, mpl2.showAngle};
                for (int i = 0; i < latLng.length; i++) {
                    MapPointLatLng mpl = new MapPointLatLng();
                    mpl.isSelect = true;
                    mpl.setAngle(angleArray[i]);
                    Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true)).anchor(0.5f, 0.5f).draggable(false));
                    mMarker.setTag(mpl);
                    mMarker.setFlat(true);
                    this.arrowMarkerList.add(mMarker);
                }
            }
            j++;
        }
    }

    public void addSmallMarkerByInterest() {
        clearAllSmallMarker();
        int j = 0;
        for (Marker marker : this.mMarkerList) {
            if (j > 0) {
                MapPointLatLng mpl1 = (MapPointLatLng) ((Marker) this.mMarkerList.get(j - 1)).getTag();
                MapPointLatLng mpl2 = (MapPointLatLng) ((Marker) this.mMarkerList.get(j)).getTag();
                if (mpl2.mInrertestPoint != null) {
                    LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(((Marker) this.mMarkerList.get(j - 1)).getPosition(), ((Marker) this.mMarkerList.get(j)).getPosition(), 3);
                    float[] angleArray = new float[2];
                    if (mpl1.mInrertestPoint == null) {
                        angleArray[0] = mpl2.showAngle;
                        angleArray[1] = mpl2.showAngle;
                    } else {
                        angleArray = this.mapCalcAngle.getAnlgesByRoration(mpl1.showAngle, mpl2.showAngle, 0);
                    }
                    for (int i = 0; i < latLng.length; i++) {
                        MapPointLatLng mpl = new MapPointLatLng();
                        mpl.isSelect = true;
                        mpl.setAngle(angleArray[i]);
                        Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true)).anchor(0.5f, 0.5f).draggable(false));
                        mMarker.setTag(mpl);
                        mMarker.setFlat(true);
                        this.arrowMarkerList.add(mMarker);
                    }
                }
            }
            j++;
        }
    }

    public void updateInterestPoint() {
        MapPointLatLng mp = (MapPointLatLng) this.mSelectMarker.getTag();
        if (mp.isIntertestPoint) {
            lightPoint(mp, true);
        }
    }

    public void drawPointLine(LatLng latLngDevice) {
        if (this.gglMapLocationManager != null && this.gglMapLocationManager.getDevLocation() != null) {
            Polyline polyline;
            if (this.polylineList != null) {
                for (Polyline polyline2 : this.polylineList) {
                    polyline2.remove();
                }
                this.polylineList.clear();
            }
            for (int i = 0; i < this.mMarkerList.size(); i++) {
                PolylineOptions polylineOptions = new PolylineOptions();
                if (i == 0) {
                    polylineOptions.add(latLngDevice);
                    polylineOptions.add(((Marker) this.mMarkerList.get(i)).getPosition());
                } else {
                    polylineOptions.add(((Marker) this.mMarkerList.get(i - 1)).getPosition());
                    polylineOptions.add(((Marker) this.mMarkerList.get(i)).getPosition());
                }
                polylineOptions.color(this.context.getResources().getColor(this.lineDefaultColor)).zIndex(3.0f);
                polylineOptions.width(4.0f);
                polyline2 = this.googleMap.addPolyline(polylineOptions);
                polyline2.setPattern(PATTERN_DASHED);
                try {
                    this.polylineList.add(polyline2);
                } catch (Exception e) {
                }
            }
        }
    }

    public void changeLine() {
        if (this.mMarkerList != null && this.nPos == -1 && this.polylineList != null && this.polylineList.size() > 0) {
            Polyline c = (Polyline) this.polylineList.get(0);
            List<LatLng> mLatLng = new ArrayList();
            mLatLng.add(this.gglMapLocationManager.getDevLocation());
            mLatLng.add(((Marker) this.mMarkerList.get(0)).getPosition());
            c.setPoints(mLatLng);
        }
    }

    public void setAiLineMarkByHistory(List<MapPointLatLng> points, List<MapPointLatLng> interests) {
        int i = 0;
        for (MapPointLatLng mpl : interests) {
            i++;
            mpl.nPos = i;
            Marker interestMarker = addInterestMarkerByHistory(mpl);
            interestMarker.setTag(mpl);
            interestMarker.setDraggable(true);
            this.interestMarkerList.add(interestMarker);
        }
        for (MapPointLatLng point : points) {
            Marker mMarker = addPointMarkerByHistory(point);
            if (point.mInrertestPoint != null) {
                findInterestPoint(point, (List) interests);
            }
            mMarker.setTag(point);
            this.mMarkerList.add(mMarker);
            point.distance = 0.0f;
            this.mMapPointList.add(point);
        }
        if (points.size() > 0) {
            drawPointLine(this.gglMapLocationManager.getDevLocation());
        }
    }

    public void findInterestPoint(MapPointLatLng point, List<MapPointLatLng> interests) {
        for (MapPointLatLng mpl : interests) {
            if (point.mInrertestPoint.latitude == mpl.latitude && point.mInrertestPoint.longitude == mpl.longitude) {
                point.mInrertestPoint.nPos = mpl.nPos;
                return;
            }
        }
    }

    public void setAiLineMarkByDevice(List<AckGetAiLinePoint> points, List<AckGetAiLinePoint> interestPoints) {
        for (AckGetAiLinePoint point : interestPoints) {
            addInterestByDevice(point);
        }
        for (AckGetAiLinePoint point2 : points) {
            int res;
            BitmapDescriptor mBitmapDescriptor;
            MapPointLatLng mp = new MapPointLatLng();
            mp.yawMode = point2.getYawMode();
            int index = findInterestPoint(point2, (List) interestPoints);
            if (index != -1) {
                mp.mInrertestPoint = (MapPointLatLng) ((Marker) this.interestMarkerList.get(index)).getTag();
            }
            mp.altitude = (float) point2.getAltitude();
            mp.nPos = this.mMarkerList.size() + 1;
            FLatLng fLatLng = GpsCorrect.Earth_To_Mars(point2.getLatitude(), point2.getLongitude());
            LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
            mp.longitude = latLng.longitude;
            mp.latitude = latLng.latitude;
            mp.setAngle(point2.getAngle());
            if (point2.getYawMode() == (byte) 0) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            if (mp.mInrertestPoint != null) {
                mBitmapDescriptor = this.gdCustemMarkerView.createMapPointWithPioView(this.context, res, mp.altitude, mp.nPos, mp.mInrertestPoint.nPos, mp.showAngle, mp.isSelect, false);
            } else {
                mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mp.altitude, mp.nPos, mp.showAngle, mp.isSelect, false);
            }
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.64285713f).draggable(false));
            mMarker.setTag(mp);
            mMarker.setFlat(true);
            this.mMarkerList.add(mMarker);
            mp.distance = 0.0f;
            this.mMapPointList.add(mp);
        }
        if (points.size() > 0) {
            drawPointLine(this.gglMapLocationManager.getDevLocation());
        }
    }

    public float getDistance(MapPointLatLng points1, MapPointLatLng points2) {
        return (float) GeoTools.getDistance(new LatLng(points1.latitude, points1.longitude), new LatLng(points2.latitude, points2.longitude)).valueInMeters();
    }

    public boolean isFarToHome() {
        for (Marker marker : this.mMarkerList) {
            this.gglMapLocationManager.getHomeLocation();
            if (((float) GeoTools.getDistance(marker.getPosition(), this.gglMapLocationManager.getHomeLocation()).valueInMeters()) > 1000.0f) {
                return true;
            }
        }
        return false;
    }

    public void clearMarker() {
        if (this.polylineList != null) {
            for (Polyline polyline : this.polylineList) {
                polyline.remove();
            }
        }
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        this.polylineList.clear();
        this.mMarkerList.clear();
        this.mMapPointList.clear();
        this.interestMarkerList.clear();
        this.arrowMarkerList.clear();
        this.mSelectMarker = null;
    }

    public int getCurrentPointActionRes(int action) {
        switch (action) {
            case 0:
                return R.drawable.x8_img_ai_line_action_na1;
            case 1:
                return R.drawable.x8_img_ai_line_action_hover1;
            case 2:
                return R.drawable.x8_img_ai_line_action_record1;
            case 3:
                return R.drawable.x8_img_ai_line_action_4x_slow1;
            case 4:
                return R.drawable.x8_img_ai_line_action_one_photo1;
            case 5:
                return R.drawable.x8_img_ai_line_action_5s_photo1;
            case 6:
                return R.drawable.x8_img_ai_line_action_three_photo1;
            default:
                return 0;
        }
    }

    public void onAiLineAddPoint(LatLng latLng, float height, float angle) {
        if (isFullSize()) {
            X8ToastUtil.showToast(this.context, this.context.getString(R.string.x8_ai_fly_lines_point_max), 0);
        } else if (this.gglMapLocationManager.getDevLocation() != null) {
            LatLng d = this.gglMapLocationManager.getDevLocation();
            if (((float) GeoTools.getDistance(latLng, this.gglMapLocationManager.getHomeLocation()).valueInMeters()) > 1000.0f) {
                X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), new Object[]{X8NumberUtil.getDistanceNumberString(1000.0f, 0, true)}), 0);
            } else if (isValid(latLng)) {
                addPointLatLng(latLng, (float) GeoTools.getDistance(latLng, d).valueInMeters(), this.gglMapLocationManager.getDevLocation(), false, angle);
            } else {
                X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_lines_point_magin), new Object[]{X8NumberUtil.getDistanceNumberString(10.0f, 0, true)}), 0);
            }
        }
    }

    private void deleteMarker(boolean isMapPoint, LatLng homeLocation) {
        if (isMapPoint) {
            if (this.mSelectMarker != null) {
                MapPointLatLng mapPointLatLng = (MapPointLatLng) this.mSelectMarker.getTag();
                if (mapPointLatLng.isIntertestPoint) {
                    removeInterestPoint();
                    return;
                }
                if (mapPointLatLng.mInrertestPoint != null) {
                    lightPoint(mapPointLatLng, false);
                }
                resetArrowList(mapPointLatLng);
                removeLinePoint(homeLocation);
                if (this.mMarkerList.size() > 0) {
                    onMarkerClick((Marker) this.mMarkerList.get(this.mMarkerList.size() - 1));
                }
                if (mapPointLatLng.yawMode == 0) {
                    addSmallMarkerByInterest();
                }
            }
        } else if (this.mMarkerList.size() != 0) {
            if (this.mSelectMarker != null) {
                ((MapPointLatLng) this.mSelectMarker.getTag()).isSelect = false;
            }
            this.mSelectMarker = (Marker) this.mMarkerList.get(this.mMarkerList.size() - 1);
            deleteSmallMaker(this.mSelectMarker);
            removeLinePoint(homeLocation);
            if (this.mMarkerList.size() > 0) {
                onMarkerClick((Marker) this.mMarkerList.get(this.mMarkerList.size() - 1));
            }
        }
    }

    public void resetArrowList(MapPointLatLng tmp) {
        if (this.arrowMarkerList != null && this.arrowMarkerList.size() > 0 && tmp.yawMode == 2) {
            int n;
            Marker m1;
            Marker m2;
            if (tmp.nPos == this.mMarkerList.size()) {
                n = ((tmp.nPos - 1) - 1) * 2;
                m1 = (Marker) this.arrowMarkerList.get(n);
                m2 = (Marker) this.arrowMarkerList.get(n + 1);
                m1.remove();
                m2.remove();
                this.arrowMarkerList.remove(m1);
                this.arrowMarkerList.remove(m2);
            } else if (tmp.nPos == 1) {
                m1 = (Marker) this.arrowMarkerList.get(0);
                m2 = (Marker) this.arrowMarkerList.get(1);
                m1.remove();
                m2.remove();
                this.arrowMarkerList.remove(m1);
                this.arrowMarkerList.remove(m2);
            } else {
                n = (tmp.nPos - 1) * 2;
                m1 = (Marker) this.arrowMarkerList.get(n);
                m2 = (Marker) this.arrowMarkerList.get(n + 1);
                m1.remove();
                m2.remove();
                this.arrowMarkerList.remove(m1);
                this.arrowMarkerList.remove(m2);
                n = ((tmp.nPos - 1) - 1) * 2;
                Marker m3 = (Marker) this.arrowMarkerList.get(n);
                Marker m4 = (Marker) this.arrowMarkerList.get(n + 1);
                m3.remove();
                m4.remove();
                this.arrowMarkerList.remove(m3);
                this.arrowMarkerList.remove(m4);
                resetSmallMakerByMap((Marker) this.mMarkerList.get(tmp.nPos - 2), (Marker) this.mMarkerList.get(tmp.nPos), n);
            }
        }
    }

    public void resetSmallMakerByMap(Marker marker1, Marker marker2, int n) {
        MapPointLatLng mpl1 = (MapPointLatLng) marker1.getTag();
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
        mpl2.setAngle(getPointAngle(mpl1, mpl2));
        float[] angleArray = new float[]{mpl2.showAngle, mpl2.showAngle};
        for (int i = 0; i < latLng.length; i++) {
            MapPointLatLng mpl = new MapPointLatLng();
            mpl.isSelect = true;
            mpl.setAngle(angleArray[i]);
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true)).anchor(0.5f, 0.5f).draggable(false));
            mMarker.setTag(mpl);
            mMarker.setFlat(true);
            this.arrowMarkerList.add(n + i, mMarker);
        }
    }

    public void removeLinePoint(LatLng homeLocation) {
        MapPointLatLng removeMapPointLatLng = null;
        for (MapPointLatLng mMapPointLatLng : this.mMapPointList) {
            LatLng mLatLng = this.mSelectMarker.getPosition();
            if (mLatLng.longitude == mMapPointLatLng.longitude && mLatLng.latitude == mMapPointLatLng.latitude) {
                removeMapPointLatLng = mMapPointLatLng;
                break;
            }
        }
        if (removeMapPointLatLng != null) {
            this.mMapPointList.remove(removeMapPointLatLng);
        }
        for (Marker marker : this.mMarkerList) {
            if (this.mSelectMarker == marker) {
                break;
            }
        }
        this.mMarkerList.remove(this.mSelectMarker);
        this.mSelectMarker.remove();
        int i = 0;
        for (Marker marker2 : this.mMarkerList) {
            i++;
            MapPointLatLng p = (MapPointLatLng) marker2.getTag();
            p.nPos = i;
            changePointMarker(marker2, p, false);
        }
        drawPointLine(homeLocation);
        this.mSelectMarker = null;
        this.lineMarkerSelectListener.onMarkerSizeChange(this.mMarkerList.size());
    }

    public void removeInterestPoint() {
        MapPointLatLng tmp = (MapPointLatLng) this.mSelectMarker.getTag();
        int i = 0;
        for (MapPointLatLng mpl : this.mMapPointList) {
            if (mpl.mInrertestPoint != null && tmp == mpl.mInrertestPoint) {
                mpl.mInrertestPoint = null;
                changePointMarker((Marker) this.mMarkerList.get(i), mpl, false);
            }
            i++;
        }
        this.interestMarkerList.remove(this.mSelectMarker);
        this.mSelectMarker.remove();
        i = 0;
        for (Marker marker : this.interestMarkerList) {
            i++;
            MapPointLatLng p = (MapPointLatLng) marker.getTag();
            p.nPos = i;
            changePointMarker(marker, p, false);
        }
        this.mSelectMarker = null;
        if (this.lineMarkerSelectListener != null) {
            boolean z;
            IX8MarkerListener iX8MarkerListener = this.lineMarkerSelectListener;
            if (this.interestMarkerList.size() < 20) {
                z = true;
            } else {
                z = false;
            }
            iX8MarkerListener.onInterestSizeEnable(z);
        }
    }

    public void onMapClickForAiLine(LatLng latLng) {
        if (isOnMapClickValid()) {
            onAiLineAddPoint(latLng);
        }
    }

    public void onAiLineAddPoint(LatLng latLng) {
        if (isFullSize()) {
            X8ToastUtil.showToast(this.context, this.context.getString(R.string.x8_ai_fly_lines_point_max), 0);
        } else if (this.gglMapLocationManager.getDevLocation() != null) {
            LatLng d = this.gglMapLocationManager.getDevLocation();
            if (((float) GeoTools.getDistance(latLng, this.gglMapLocationManager.getHomeLocation()).valueInMeters()) > 1000.0f) {
                X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), new Object[]{X8NumberUtil.getDistanceNumberString(1000.0f, 0, true)}), 0);
            } else if (isValid(latLng)) {
                addPointLatLng(latLng, (float) GeoTools.getDistance(latLng, d).valueInMeters(), this.gglMapLocationManager.getDevLocation(), true, -1.0f);
            } else {
                X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_lines_point_magin), new Object[]{X8NumberUtil.getDistanceNumberString(10.0f, 0, true)}), 0);
            }
        }
    }

    public void changePointMarker(Marker marker, MapPointLatLng mpl, boolean isRelation) {
        int res;
        if (!mpl.isMapPoint) {
            changeAngleOrOnAngle(marker, mpl, isRelation);
        } else if (mpl.isIntertestPoint) {
            if (mpl.isSelect) {
                res = R.drawable.x8_img_ai_line_inreterst_max2;
            } else {
                res = R.drawable.x8_img_ai_line_inreterst_max1;
            }
            marker.setIcon(this.gdCustemMarkerView.createMapPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation));
            marker.setAnchor(0.5f, 0.73802954f);
        } else if (mpl.mInrertestPoint != null) {
            if (mpl.isSelect) {
                res = R.drawable.x8_ai_line_point_with_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            marker.setIcon(this.gdCustemMarkerView.createMapPointWithPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.mInrertestPoint.nPos, mpl.showAngle, mpl.isSelect, isRelation));
            marker.setAnchor(0.5f, 0.5f);
        } else {
            changeAngleOrOnAngle(marker, mpl, isRelation);
        }
    }

    public void changeAngleOrOnAngle(Marker marker, MapPointLatLng mpl, boolean isRelation) {
        BitmapDescriptor mBitmapDescriptor;
        int res;
        if (mpl.yawMode == 1 || mpl.yawMode == 2) {
            if (mpl.isSelect) {
                res = R.drawable.x8_ai_line_point_with_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, isRelation);
        } else {
            if (mpl.isSelect) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_no_angle1;
            }
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation);
        }
        marker.setIcon(mBitmapDescriptor);
        marker.setAnchor(0.5f, 0.64285713f);
    }

    public void changeViewBySetPoints(Marker marker, MapPointLatLng mpl, boolean isRelation, boolean isRun) {
        int res;
        if (mpl.isMapPoint) {
            float anchorY;
            BitmapDescriptor mBitmapDescriptor;
            if (mpl.isIntertestPoint) {
                changePointMarker(marker, mpl, false);
            } else if (mpl.yawMode == 0) {
                if (mpl.mInrertestPoint != null) {
                    anchorY = 0.5f;
                    if (mpl.isSelect) {
                        res = R.drawable.x8_ai_line_point_with_angle2;
                    } else if (isRun) {
                        res = R.drawable.x8_ai_line_point_run_angle1;
                    } else {
                        res = R.drawable.x8_ai_line_point_with_angle1;
                    }
                    mBitmapDescriptor = this.gdCustemMarkerView.createMapPointWithPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.mInrertestPoint.nPos, mpl.showAngle, mpl.isSelect, isRelation);
                } else {
                    anchorY = 0.64285713f;
                    if (mpl.isSelect) {
                        res = R.drawable.x8_ai_line_point_no_angle2;
                    } else if (isRun) {
                        res = R.drawable.x8_ai_line_point_run_no_angle1;
                    } else {
                        res = R.drawable.x8_ai_line_point_no_angle1;
                    }
                    mBitmapDescriptor = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation);
                    marker.setIcon(mBitmapDescriptor);
                    marker.setAnchor(0.5f, 0.64285713f);
                }
                marker.setIcon(mBitmapDescriptor);
                marker.setAnchor(0.5f, anchorY);
            } else {
                if (mpl.mInrertestPoint != null) {
                    anchorY = 0.5f;
                    if (mpl.isSelect) {
                        res = R.drawable.x8_ai_line_point_with_angle2;
                    } else if (isRun) {
                        res = R.drawable.x8_ai_line_point_run_angle1;
                    } else {
                        res = R.drawable.x8_ai_line_point_with_angle1;
                    }
                    mBitmapDescriptor = this.gdCustemMarkerView.createMapPointWithPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.mInrertestPoint.nPos, mpl.showAngle, mpl.isSelect, isRelation);
                } else {
                    anchorY = 0.64285713f;
                    if (mpl.isSelect) {
                        res = R.drawable.x8_ai_line_point_with_angle2;
                    } else if (isRun) {
                        res = R.drawable.x8_ai_line_point_run_angle1;
                    } else {
                        res = R.drawable.x8_ai_line_point_with_angle1;
                    }
                    mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, isRelation);
                }
                marker.setIcon(mBitmapDescriptor);
                marker.setAnchor(0.5f, anchorY);
            }
        } else if (mpl.yawMode == 0) {
            if (mpl.isSelect) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else if (isRun) {
                res = R.drawable.x8_ai_line_point_run_no_angle1;
            } else {
                res = R.drawable.x8_ai_line_point_no_angle1;
            }
            marker.setIcon(this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation));
            marker.setAnchor(0.5f, 0.64285713f);
        } else {
            if (mpl.isSelect) {
                res = R.drawable.x8_ai_line_point_with_angle2;
            } else if (isRun) {
                res = R.drawable.x8_ai_line_point_run_angle1;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            marker.setIcon(this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, isRelation));
            marker.setAnchor(0.5f, 0.64285713f);
        }
    }

    public void changeViewByRunning(Marker marker, MapPointLatLng mpl) {
        if (!mpl.isIntertestPoint) {
            int res;
            if (mpl.mInrertestPoint != null) {
                if (mpl.yawMode == 0) {
                    res = R.drawable.x8_ai_line_point_with_angle2;
                } else {
                    res = R.drawable.x8_ai_line_point_with_angle2;
                }
                marker.setIcon(this.gdCustemMarkerView.createPointEventWithPioView(this.context, getCurrentPointActionRes(mpl.action), res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false));
                marker.setAnchor(0.5f, 0.6846361f);
                return;
            }
            if (mpl.yawMode == 0) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle2;
            }
            marker.setIcon(this.gdCustemMarkerView.createPointEventNoPioView(this.context, getCurrentPointActionRes(mpl.action), res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false));
            marker.setAnchor(0.5f, 0.7962384f);
        }
    }

    public void changeLineSmallMarkerByRunning(Marker marker1, Marker marker2, float angle, int type) {
        int res = 0;
        if (type == 0) {
            res = R.drawable.x8_ai_line_point_small1;
        } else if (type == 1) {
            res = R.drawable.x8_ai_line_point_small3;
        } else if (type == 2) {
            res = R.drawable.x8_ai_line_point_small2;
        }
        marker1.setIcon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, res, angle, ((MapPointLatLng) marker2.getTag()).isSelect));
        marker1.setAnchor(0.5f, 0.5f);
        marker2.setIcon(this.gdCustemMarkerView.createPointWithSmallArrow(this.context, res, angle, ((MapPointLatLng) marker2.getTag()).isSelect));
        marker2.setAnchor(0.5f, 0.5f);
    }

    public Marker addPointMarker(boolean isMapPoint, LatLng latLng, MapPointLatLng mpl, float h, float angle) {
        BitmapDescriptor mBitmapDescriptor;
        float anchorY;
        if (isMapPoint) {
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, R.drawable.x8_ai_line_point_no_angle1, h, mpl.nPos, mpl.isSelect, false);
            mpl.altitude = h;
            mpl.isMapPoint = isMapPoint;
            anchorY = 0.64285713f;
            int res = 0;
        } else {
            mpl.angle = angle;
            mpl.showAngle = angle;
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, R.drawable.x8_ai_line_point_with_angle1, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
            mpl.altitude = h;
            mpl.isMapPoint = isMapPoint;
            anchorY = 0.5f;
        }
        Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, anchorY).draggable(false));
        mMarker.setFlat(true);
        return mMarker;
    }

    public Marker addInterestMarker(LatLng latLng, float h, MapPointLatLng mpl) {
        Marker interestMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(this.gdCustemMarkerView.createCustomMarkerView(this.context, R.drawable.x8_img_ai_line_inreterst_max1, h, mpl.nPos)).anchor(0.5f, 1.0f).draggable(false));
        interestMarker.setFlat(true);
        return interestMarker;
    }

    public Marker addInterestMarkerByHistory(MapPointLatLng mpl) {
        int res;
        if (mpl.isSelect) {
            res = R.drawable.x8_img_ai_line_inreterst_max2;
        } else {
            res = R.drawable.x8_img_ai_line_inreterst_max1;
        }
        Marker interestMarker = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(mpl.latitude, mpl.longitude)).icon(this.gdCustemMarkerView.createMapPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.isSelect, false)).anchor(0.5f, 0.73802954f).draggable(false));
        interestMarker.setFlat(true);
        return interestMarker;
    }

    public Marker addPointMarkerByHistory(MapPointLatLng mpl) {
        BitmapDescriptor mBitmapDescriptor;
        float anchorY;
        if (this.lineMarkerSelectListener.getOration() != 0) {
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, R.drawable.x8_ai_line_point_with_angle1, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
            anchorY = 0.64285713f;
        } else if (mpl.mInrertestPoint != null) {
            mpl.setAngle(getPointAngle(mpl, mpl.mInrertestPoint));
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, R.drawable.x8_ai_line_point_with_angle1, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
            anchorY = 0.64285713f;
        } else {
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, R.drawable.x8_ai_line_point_no_angle1, mpl.altitude, mpl.nPos, mpl.isSelect, false);
            anchorY = 0.64285713f;
        }
        Marker marker = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(mpl.latitude, mpl.longitude)).icon(mBitmapDescriptor).anchor(0.5f, anchorY).draggable(false));
        marker.setFlat(true);
        return marker;
    }

    public void addSmallMakerByHistory() {
        int i = 0;
        for (Marker marker : this.mMarkerList) {
            MapPointLatLng p = (MapPointLatLng) marker.getTag();
            if (i != 0) {
                addSmallMakerByMap((Marker) this.mMarkerList.get(i - 1), (Marker) this.mMarkerList.get(i));
            }
            i++;
        }
    }
}
