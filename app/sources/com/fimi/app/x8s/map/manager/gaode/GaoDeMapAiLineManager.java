package com.fimi.app.x8s.map.manager.gaode;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.interfaces.AbsAiLineManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.gaode.GaoDeMapCustomMarkerView;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.dataparser.AckGetAiLinePoint;
import com.fimi.x8sdk.dataparser.AckGetAiLinePointsAction;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.GpsCorrect;
import java.util.ArrayList;
import java.util.List;

public class GaoDeMapAiLineManager extends AbsAiLineManager implements OnMarkerClickListener, OnMarkerDragListener, OnMapClickListener {
    private final int MAX = 20;
    private AMap aMap;
    private Context context;
    private List<Marker> interestMarkerList = new ArrayList();
    private boolean isClick = true;
    private List<LatLng> latLngs = new ArrayList();
    private Circle limitCircle;
    private IX8MarkerListener lineMarkerSelectListener;
    private GaodeMapLocationManager mGaodeMapLocationManager;
    private List<MapPointLatLng> mMapPointList = new ArrayList();
    private List<Marker> mMarkerList = new ArrayList();
    private Marker mSelectMarker = null;
    private int nPos = -1;
    private Polyline polyline;

    public GaoDeMapAiLineManager(Context context, AMap aMap, GaodeMapLocationManager mGaodeMapLocationManager) {
        this.context = context;
        this.aMap = aMap;
        this.mGaodeMapLocationManager = mGaodeMapLocationManager;
    }

    private void setOnMarkerListener() {
        this.aMap.setOnMapClickListener(this);
        this.aMap.setOnMarkerClickListener(this);
        this.aMap.setOnMarkerDragListener(this);
    }

    public void removeMarkerListener() {
        this.aMap.setOnMarkerClickListener(null);
    }

    public void addPointLatLng(LatLng latLng, float distance, LatLng deviceLocation, boolean isMapPoint, float angle) {
        BitmapDescriptor mBitmapDescriptor;
        MapPointLatLng mp = new MapPointLatLng();
        mp.nPos = this.mMarkerList.size() + 1;
        int h = Math.round(StateManager.getInstance().getX8Drone().getHeight());
        if (h <= 5) {
            h = 5;
        }
        if (isMapPoint) {
            mBitmapDescriptor = new GaoDeMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_follow_point2, (float) h, mp.nPos);
            mp.altitude = (float) h;
            mp.isMapPoint = isMapPoint;
        } else {
            mBitmapDescriptor = new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, R.drawable.x8_ai_line_point_with_angle1, this.mMarkerList.size() + 1);
            mp.altitude = (float) h;
            mp.isMapPoint = isMapPoint;
        }
        Marker mMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
        mp.longitude = latLng.longitude;
        mp.latitude = latLng.latitude;
        mMarker.setObject(mp);
        this.mMarkerList.add(mMarker);
        mp.distance = distance;
        this.mMapPointList.add(mp);
        drawPointLine(deviceLocation);
        this.lineMarkerSelectListener.onMarkerSizeChange(this.mMarkerList.size());
        if (angle != -1.0f) {
            mp.angle = angle;
            setRotateAngle(mMarker, angle);
        }
        this.isClick = false;
        onMarkerClick(mMarker);
    }

    public void setRotateAngle(Marker marker, float angle) {
        marker.setRotateAngle((-angle) + this.aMap.getCameraPosition().bearing);
    }

    public boolean onMarkerClick(Marker marker) {
        MapPointLatLng mp = (MapPointLatLng) marker.getObject();
        GaoDeMapCustomMarkerView gdCustemMarkerView;
        int res;
        if (this.mSelectMarker == null) {
            this.mSelectMarker = marker;
            mp.isSelect = true;
            if (mp.isMapPoint) {
                gdCustemMarkerView = new GaoDeMapCustomMarkerView();
                res = R.drawable.x8_img_ai_follow_point;
                if (mp.isIntertestPoint) {
                    res = R.drawable.x8_img_ai_line_inreterst_max2;
                }
                marker.setIcon(gdCustemMarkerView.createCustomMarkerView(this.context, res, mp.altitude, mp.nPos));
            } else if (!mp.isMapPoint) {
                marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, R.drawable.x8_ai_line_point_with_angle2, mp.nPos));
            }
        } else if (this.mSelectMarker.getObject() == marker.getObject()) {
            mp.isSelect = false;
            this.mSelectMarker = null;
            if (mp.isMapPoint) {
                gdCustemMarkerView = new GaoDeMapCustomMarkerView();
                res = R.drawable.x8_img_ai_follow_point2;
                if (mp.isIntertestPoint) {
                    res = R.drawable.x8_img_ai_line_inreterst_max1;
                }
                marker.setIcon(gdCustemMarkerView.createCustomMarkerView(this.context, res, mp.altitude, mp.nPos));
            } else if (!mp.isMapPoint) {
                marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, R.drawable.x8_ai_line_point_with_angle2, mp.nPos));
            }
        } else {
            Marker lastMarker = this.mSelectMarker;
            MapPointLatLng lastMp = (MapPointLatLng) lastMarker.getObject();
            lastMp.isSelect = false;
            if (lastMp.isMapPoint) {
                gdCustemMarkerView = new GaoDeMapCustomMarkerView();
                res = R.drawable.x8_img_ai_follow_point2;
                if (lastMp.isIntertestPoint) {
                    res = R.drawable.x8_img_ai_line_inreterst_max1;
                }
                lastMarker.setIcon(gdCustemMarkerView.createCustomMarkerView(this.context, res, lastMp.altitude, lastMp.nPos));
            } else if (!lastMp.isMapPoint) {
                lastMarker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, R.drawable.x8_ai_line_point_with_angle1, lastMp.nPos));
            }
            this.mSelectMarker = marker;
            mp.isSelect = true;
            if (mp.isMapPoint) {
                gdCustemMarkerView = new GaoDeMapCustomMarkerView();
                res = R.drawable.x8_img_ai_follow_point;
                if (mp.isIntertestPoint) {
                    res = R.drawable.x8_img_ai_line_inreterst_max2;
                }
                marker.setIcon(gdCustemMarkerView.createCustomMarkerView(this.context, res, mp.altitude, mp.nPos));
            } else if (!mp.isMapPoint) {
                marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, R.drawable.x8_ai_line_point_with_angle2, mp.nPos));
            }
        }
        this.lineMarkerSelectListener.onMarkerSelect(mp.isSelect, mp.altitude, mp, this.isClick);
        this.isClick = true;
        return true;
    }

    private void clearPointMark() {
        for (Marker marker : this.mMarkerList) {
            marker.remove();
        }
        for (Marker marker2 : this.interestMarkerList) {
            marker2.remove();
        }
        clearMarker();
    }

    private void deleteMarker(boolean isMapPoint, LatLng homeLocation) {
        if (!isMapPoint) {
            if (this.mMarkerList.size() != 0) {
                this.mSelectMarker = (Marker) this.mMarkerList.get(this.mMarkerList.size() - 1);
            } else {
                return;
            }
        }
        if (this.mSelectMarker == null) {
            return;
        }
        if (((MapPointLatLng) this.mSelectMarker.getObject()).isIntertestPoint) {
            removeInterestPoint();
        } else {
            removeLinePoint(homeLocation);
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
            MapPointLatLng p = (MapPointLatLng) marker2.getObject();
            p.nPos = i;
            if (p.isMapPoint) {
                marker2.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_follow_point2, p.altitude, p.nPos));
                setRotateAngle(marker2, p.angle);
            } else {
                marker2.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, R.drawable.x8_ai_line_point_with_angle1, p.nPos));
                setRotateAngle(marker2, p.angle);
            }
        }
        drawPointLine(homeLocation);
        this.mSelectMarker = null;
        this.lineMarkerSelectListener.onMarkerSizeChange(this.mMarkerList.size());
        removeInterestUnBebind();
    }

    public void removeInterestPoint() {
        MapPointLatLng tmp = (MapPointLatLng) this.mSelectMarker.getObject();
        for (MapPointLatLng mpl : this.mMapPointList) {
            if (tmp == mpl.mInrertestPoint) {
                mpl.mInrertestPoint = null;
            }
        }
        this.interestMarkerList.remove(this.mSelectMarker);
        this.mSelectMarker.remove();
        int i = 0;
        for (Marker marker : this.interestMarkerList) {
            i++;
            MapPointLatLng p = (MapPointLatLng) marker.getObject();
            p.nPos = i;
            if (p.isMapPoint) {
                marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_line_inreterst_max1, p.altitude, p.nPos));
                setRotateAngle(marker, p.angle);
            }
        }
        this.mSelectMarker = null;
        if (this.lineMarkerSelectListener != null) {
            IX8MarkerListener iX8MarkerListener = this.lineMarkerSelectListener;
            boolean z = this.interestMarkerList.size() != this.mMarkerList.size() && hasPointUnBind();
            iX8MarkerListener.onInterestSizeEnable(z);
        }
    }

    public void drawPointLine(LatLng latLngDevice) {
        this.latLngs.clear();
        if (this.polyline != null) {
            this.polyline.remove();
        }
        this.latLngs.add(latLngDevice);
        for (Marker marker : this.mMarkerList) {
            this.latLngs.add(marker.getPosition());
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(this.latLngs);
        polylineOptions.setDottedLine(true);
        polylineOptions.color(this.context.getResources().getColor(R.color.x8_drone_inface_line)).zIndex(50.0f);
        polylineOptions.width(10.0f);
        this.polyline = this.aMap.addPolyline(polylineOptions);
    }

    public void clearMarker() {
        if (this.polyline != null) {
            this.polyline.remove();
            this.polyline = null;
        }
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        if (this.latLngs != null) {
            this.latLngs.clear();
        }
        this.mMarkerList.clear();
        this.mMapPointList.clear();
        this.interestMarkerList.clear();
        this.mSelectMarker = null;
    }

    public void setMarkerViewInfo(float height) {
        if (this.mSelectMarker != null) {
            MapPointLatLng mp = (MapPointLatLng) this.mSelectMarker.getObject();
            if (mp.isSelect) {
                mp.altitude = height;
                GaoDeMapCustomMarkerView gdCustemMarkerView = new GaoDeMapCustomMarkerView();
                int res;
                if (mp.isMapPoint) {
                    if (mp.isIntertestPoint) {
                        if (mp.isSelect) {
                            res = R.drawable.x8_img_ai_line_inreterst_max2;
                        } else {
                            res = R.drawable.x8_img_ai_line_inreterst_max1;
                        }
                    } else if (mp.isSelect) {
                        res = R.drawable.x8_img_ai_follow_point;
                    } else {
                        res = R.drawable.x8_img_ai_follow_point2;
                    }
                    this.mSelectMarker.setIcon(gdCustemMarkerView.createCustomMarkerView(this.context, res, mp.altitude, mp.nPos));
                } else if (!mp.isMapPoint) {
                    if (mp.isSelect) {
                        res = R.drawable.x8_ai_line_point_with_angle2;
                    } else {
                        res = R.drawable.x8_ai_line_point_with_angle1;
                    }
                    this.mSelectMarker.setIcon(gdCustemMarkerView.createCustomMarkerView2(this.context, res, mp.nPos));
                }
            }
        }
    }

    public float getLineAngleByMapBealing(float angle) {
        return this.aMap.getCameraPosition().bearing - angle;
    }

    public void setOnMapClickListener() {
        setOnMarkerListener();
    }

    public void removeMapClickListener() {
        this.aMap.setOnMapClickListener(null);
        this.aMap.setOnMarkerClickListener(null);
    }

    public void resetMapEvent() {
        this.aMap.setOnMapClickListener(null);
        removeMarkerListener();
        this.aMap.setOnMarkerDragListener(null);
    }

    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            this.limitCircle = this.aMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.strokeColor).fillColor(this.fillColor).strokeWidth((float) this.strokeWidth));
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
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
            if (AMapUtils.calculateLineDistance(latLng, new LatLng(mapPointLatLng.latitude, mapPointLatLng.longitude)) <= 10.0f) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public void setLineMarkerSelectListener(IX8MarkerListener lineMarkerSelectListener) {
        this.lineMarkerSelectListener = lineMarkerSelectListener;
    }

    public void clearAiLineMarker() {
        clearPointMark();
    }

    public synchronized void startAiLineProcess() {
        int i = 0;
        this.nPos = -1;
        for (Marker marker : this.mMarkerList) {
            int res;
            i++;
            MapPointLatLng p = (MapPointLatLng) marker.getObject();
            if (p.yawMode == 0) {
                res = R.drawable.x8_ai_line_point_no_angle1;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, res, i));
            marker.setFlat(true);
            setRotateAngle(marker, p.angle);
        }
    }

    public synchronized void setAiLineIndexPoint(int index) {
        if (this.nPos != index) {
            int res;
            Marker currentMarker = (Marker) this.mMarkerList.get(index);
            MapPointLatLng mapPointLatLng = (MapPointLatLng) currentMarker.getObject();
            if (mapPointLatLng.yawMode == 0) {
                res = R.drawable.x8_img_ai_follow_point_device4;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle2;
            }
            currentMarker.setIcon(new GaoDeMapCustomMarkerView().createCurrentPointView(this.context, res, getCurrentPointActionRes(mapPointLatLng.action), index + 1));
            if (this.nPos >= 0) {
                Marker lastMarker = (Marker) this.mMarkerList.get(this.nPos);
                if (((MapPointLatLng) currentMarker.getObject()).yawMode == 0) {
                    res = R.drawable.x8_ai_line_point_no_angle1;
                } else {
                    res = R.drawable.x8_ai_line_point_with_angle1;
                }
                lastMarker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, res, this.nPos + 1));
            }
            this.nPos = index;
        }
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

    public void setAiLineMarkByDevice(List<AckGetAiLinePoint> points, List<AckGetAiLinePoint> list) {
        for (AckGetAiLinePoint point : points) {
            int res;
            MapPointLatLng mp = new MapPointLatLng();
            mp.yawMode = point.getYawMode();
            if (mp.yawMode == 0) {
                res = R.drawable.x8_ai_line_point_no_angle1;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            BitmapDescriptor mBitmapDescriptor = new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, res, this.mMarkerList.size() + 1);
            mp.altitude = (float) point.getAltitude();
            mp.nPos = this.mMarkerList.size() + 1;
            FLatLng fLatLng = GpsCorrect.Earth_To_Mars(point.getLatitude(), point.getLongitude());
            LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
            Marker mMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
            mp.longitude = latLng.longitude;
            mp.latitude = latLng.latitude;
            mp.angle = point.getAngle();
            mMarker.setObject(mp);
            this.mMarkerList.add(mMarker);
            mp.distance = 0.0f;
            this.mMapPointList.add(mp);
            mMarker.setFlat(true);
            setRotateAngle(mMarker, point.getAngle());
        }
        if (points.size() > 0) {
            drawPointLine(this.mGaodeMapLocationManager.getHomeLocation());
        }
    }

    public void setAiLineMarkActionByDevice(List<AckGetAiLinePointsAction> points) {
        if (this.mMarkerList != null && this.mMarkerList.size() > 0) {
            for (Marker m : this.mMarkerList) {
                MapPointLatLng mpl = (MapPointLatLng) m.getObject();
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
                res = R.drawable.x8_ai_line_point_no_angle1;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            BitmapDescriptor mBitmapDescriptor = new GaoDeMapCustomMarkerView().createCustomMarkerView2(this.context, res, this.mMarkerList.size() + 1);
            mp.altitude = (float) point.getAltitude();
            mp.nPos = this.mMarkerList.size() + 1;
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
            Marker mMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
            mp.longitude = latLng.longitude;
            mp.latitude = latLng.latitude;
            mMarker.setObject(mp);
            this.mMarkerList.add(mMarker);
            mp.distance = 0.0f;
            this.mMapPointList.add(mp);
            setRotateAngle(mMarker, point.getYaw() / 100.0f);
        }
        if (points.size() > 0) {
            drawPointLine(this.mGaodeMapLocationManager.getHomeLocation());
        }
    }

    public void updataAngle(int i, float angle) {
        ((MapPointLatLng) ((Marker) this.mMarkerList.get(i)).getObject()).angle = angle;
    }

    public MapPointLatLng getMapPointLatLng() {
        if (this.mSelectMarker == null) {
            return null;
        }
        return (MapPointLatLng) this.mSelectMarker.getObject();
    }

    public int getAiLinePointSize() {
        return this.mMapPointList.size();
    }

    public float getAiLineDistance() {
        float distance = 0.0f;
        if (this.mMarkerList.size() == 2) {
            return AMapUtils.calculateLineDistance(((Marker) this.mMarkerList.get(0)).getPosition(), ((Marker) this.mMarkerList.get(1)).getPosition());
        }
        for (int i = 0; i < this.mMarkerList.size(); i++) {
            if (i != 0) {
                distance += AMapUtils.calculateLineDistance(((Marker) this.mMarkerList.get(i - 1)).getPosition(), ((Marker) this.mMarkerList.get(i)).getPosition());
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
        Marker tmp = null;
        for (Marker m : this.interestMarkerList) {
            MapPointLatLng mpl = (MapPointLatLng) m.getObject();
            boolean isFind = false;
            for (MapPointLatLng p2 : this.mMapPointList) {
                if (p2.mInrertestPoint == mpl) {
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
                p2 = (MapPointLatLng) marker.getObject();
                p2.nPos = i;
                if (p2.isMapPoint) {
                    marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_line_inreterst_max1, p2.altitude, p2.nPos));
                    setRotateAngle(marker, p2.angle);
                }
            }
        }
        if (this.lineMarkerSelectListener != null) {
            IX8MarkerListener iX8MarkerListener = this.lineMarkerSelectListener;
            boolean z = this.interestMarkerList.size() != this.mMarkerList.size() && hasPointUnBind();
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

    public void updateInterestBindPoint(MapPointLatLng mpl, int index) {
        for (int i = 0; i < this.mMapPointList.size(); i++) {
            if (i == index) {
                ((MapPointLatLng) this.mMapPointList.get(index)).mInrertestPoint = mpl;
                break;
            }
        }
        if (this.lineMarkerSelectListener != null) {
            IX8MarkerListener iX8MarkerListener = this.lineMarkerSelectListener;
            boolean z = this.interestMarkerList.size() != this.mMarkerList.size() && hasPointUnBind();
            iX8MarkerListener.onInterestSizeEnable(z);
        }
    }

    public List<MapPointLatLng> getInterestPointList() {
        List<MapPointLatLng> list = new ArrayList();
        for (int i = 0; i < this.interestMarkerList.size(); i++) {
            list.add((MapPointLatLng) ((Marker) this.interestMarkerList.get(i)).getObject());
        }
        return list;
    }

    public void setAiLineMark(double latitude, double logitude, float height, float angle) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        onAiLineAddPoint(new LatLng(fLatLng.latitude, fLatLng.longitude), height, angle);
    }

    public void onAiLineAddPoint(LatLng latLng, float height, float angle) {
        if (isFullSize()) {
            X8ToastUtil.showToast(this.context, this.context.getString(R.string.x8_ai_fly_lines_point_max), 0);
        } else if (this.mGaodeMapLocationManager.getHomeLocation() != null) {
            float distance = AMapUtils.calculateLineDistance(latLng, this.mGaodeMapLocationManager.getHomeLocation());
            if (10.0f > distance || distance > 1000.0f) {
                if (distance < 10.0f) {
                    X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far1), new Object[]{X8NumberUtil.getDistanceNumberString(10.0f, 0, true)}), 0);
                } else if (distance > 1000.0f) {
                    X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), new Object[]{X8NumberUtil.getDistanceNumberString(1000.0f, 0, true)}), 0);
                }
            } else if (isValid(latLng)) {
                addPointLatLng(latLng, distance, this.mGaodeMapLocationManager.getDevLocation(), false, angle);
            } else {
                X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_lines_point_magin), new Object[]{X8NumberUtil.getDistanceNumberString(10.0f, 0, true)}), 0);
            }
        }
    }

    public void deleteMarker(boolean isMapPoint) {
        deleteMarker(isMapPoint, this.mGaodeMapLocationManager.getDevLocation());
    }

    public void onMarkerDragStart(Marker marker) {
        MapPointLatLng mp = (MapPointLatLng) marker.getObject();
        marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_line_inreterst_max2, mp.altitude, mp.nPos));
    }

    public void onMarkerDrag(Marker marker) {
        if (this.lineMarkerSelectListener != null) {
            Rect rect = this.lineMarkerSelectListener.getDeletePosition();
            Point mPoint = this.aMap.getProjection().toScreenLocation(marker.getPosition());
            if (rect.left > mPoint.x || mPoint.x > rect.right || rect.top > mPoint.y || mPoint.y <= rect.bottom) {
            }
        }
    }

    public void onMarkerDragEnd(Marker marker) {
        MapPointLatLng mp = (MapPointLatLng) marker.getObject();
        marker.setIcon(new GaoDeMapCustomMarkerView().createCustomMarkerView(this.context, R.drawable.x8_img_ai_line_inreterst_max1, mp.altitude, mp.nPos));
        ((MapPointLatLng) marker.getObject()).isInrertestPointActive = true;
    }

    public void addInreterstMarker(int x, int y) {
        GaoDeMapCustomMarkerView gdCustemMarkerView = new GaoDeMapCustomMarkerView();
        MapPointLatLng mp = new MapPointLatLng();
        LatLng latLng = this.aMap.getProjection().fromScreenLocation(new Point(x, y));
        mp.isIntertestPoint = true;
        mp.isMapPoint = true;
        mp.nPos = this.interestMarkerList.size() + 1;
        int h = Math.round(StateManager.getInstance().getX8Drone().getHeight());
        if (h <= 5) {
            h = 5;
        }
        mp.altitude = (float) h;
        Marker interestMarker = this.aMap.addMarker(new MarkerOptions().position(latLng).icon(gdCustemMarkerView.createCustomMarkerView(this.context, R.drawable.x8_img_ai_line_inreterst_max1, (float) h, mp.nPos)).anchor(0.5f, 1.0f).draggable(true));
        interestMarker.setObject(mp);
        interestMarker.setDraggable(true);
        this.interestMarkerList.add(interestMarker);
        if (this.lineMarkerSelectListener != null) {
            IX8MarkerListener iX8MarkerListener = this.lineMarkerSelectListener;
            boolean z = this.interestMarkerList.size() != this.mMarkerList.size() && hasPointUnBind();
            iX8MarkerListener.onInterestSizeEnable(z);
        }
        this.isClick = false;
        onMarkerClick(interestMarker);
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
        return 0.0f;
    }

    public void notityChangeView(MapPointLatLng des, boolean isRelation) {
    }

    public void notityChangeView(MapPointLatLng des) {
    }

    public void changeLine() {
    }

    public void setAiLineMarkByHistory(List<MapPointLatLng> list, List<MapPointLatLng> list2) {
    }

    public float getDistance(MapPointLatLng points1, MapPointLatLng points2) {
        return 0.0f;
    }

    public boolean isFarToHome() {
        return false;
    }

    public void updateSmallMarkerAngle(MapPointLatLng mpl) {
    }

    public void addSmallMarkerByMap(int type) {
    }

    public void clearAllSmallMarker() {
    }

    public void addOrUpdateSmallMarkerForVideo(int type) {
    }

    public void addSmallMarkerByInterest() {
    }

    public void updateInterestPoint() {
    }

    public void addSmallMakerByHistory() {
    }

    public void onMapClick(LatLng latLng) {
        onMapClickForAiLine(latLng);
    }

    public void onMapClickForAiLine(LatLng latLng) {
        if (isOnMapClickValid()) {
            onAiLineAddPoint(latLng);
        }
    }

    public void onAiLineAddPoint(LatLng latLng) {
        if (isFullSize()) {
            X8ToastUtil.showToast(this.context, this.context.getString(R.string.x8_ai_fly_lines_point_max), 0);
        } else if (this.mGaodeMapLocationManager.getHomeLocation() != null) {
            float distance = AMapUtils.calculateLineDistance(latLng, this.mGaodeMapLocationManager.getHomeLocation());
            if (10.0f > distance || distance > 1000.0f) {
                if (distance < 10.0f) {
                    X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far1), new Object[]{X8NumberUtil.getDistanceNumberString(10.0f, 0, true)}), 0);
                } else if (distance > 1000.0f) {
                    X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), new Object[]{X8NumberUtil.getDistanceNumberString(1000.0f, 0, true)}), 0);
                }
            } else if (isValid(latLng)) {
                addPointLatLng(latLng, distance, this.mGaodeMapLocationManager.getDevLocation(), true, -1.0f);
            } else {
                X8ToastUtil.showToast(this.context, String.format(this.context.getString(R.string.x8_ai_fly_lines_point_magin), new Object[]{X8NumberUtil.getDistanceNumberString(10.0f, 0, true)}), 0);
            }
        }
    }
}
