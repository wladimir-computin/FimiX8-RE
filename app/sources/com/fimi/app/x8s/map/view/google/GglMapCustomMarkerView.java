package com.fimi.app.x8s.map.view.google;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.map.view.AbsMapCustomMarkerView;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8MapPointMarkerViewGroup;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class GglMapCustomMarkerView extends AbsMapCustomMarkerView {
    public BitmapDescriptor createCustomMarkerViewForP2P(Context context, int res, float heightVale, int npos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view_for_point, null);
        ((TextView) view.findViewById(R.id.point_heightValue)).setText(X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ImageView imageView = (ImageView) view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createCustomMarkerView(Context context, int res, float heightVale, int npos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view, null);
        TextView heightView = (TextView) view.findViewById(R.id.point_heightValue);
        ((TextView) view.findViewById(R.id.tv_pos)).setText("" + npos);
        heightView.setText(X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ImageView imageView = (ImageView) view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public Object createCustomMarkerView(Context context, int res, float heightVale) {
        return null;
    }

    public BitmapDescriptor createCustomMarkerView2(Context context, int res, int nPos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view2, null);
        ((TextView) view.findViewById(R.id.point_heightValue)).setText("" + nPos);
        ImageView imageView = (ImageView) view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createCustomMarkerView(Context context, int res) {
        return BitmapDescriptorFactory.fromResource(res);
    }

    public BitmapDescriptor createInreterstMarkerView0(Context context, int res) {
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view3, null), context));
    }

    public BitmapDescriptor createCurrentPointView(Context context, int res, int actionRes, int nPos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view4, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_point);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        ImageView imageView2 = (ImageView) view.findViewById(R.id.img_action);
        if (res != 0) {
            imageView2.setBackgroundResource(actionRes);
        }
        ((TextView) view.findViewById(R.id.tv_index)).setText("" + nPos);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public static Bitmap loadBitmapFromView(View view, Context context) {
        if (view == null) {
            return null;
        }
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static int dipToPx(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public BitmapDescriptor createMapPointNoAngleNoPioView(Context context, int res, float heightVale, int npos, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_no_pio_view, null);
        ((X8MapPointMarkerViewGroup) view.findViewById(R.id.myview)).setValueNoPio(res, heightVale, npos, 0.0f, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createMapPioView(Context context, int res, float heightVale, int npos, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_with_pio_view, null);
        ((X8MapPointMarkerViewGroup) view.findViewById(R.id.myview)).setPioValue(res, heightVale, npos, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createMapPointWithPioView(Context context, int res, float heightVale, int npos, int poi, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_with_pio_view, null);
        ((X8MapPointMarkerViewGroup) view.findViewById(R.id.myview)).setValueWithPio(res, heightVale, npos, poi, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createMapPointAngleNoPioView(Context context, int res, float heightVale, int npos, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_no_pio_view, null);
        ((X8MapPointMarkerViewGroup) view.findViewById(R.id.myview)).setValueNoPio(res, heightVale, npos, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createPointEventNoPioView(Context context, int actionRes, int angleRes, float heightVale, int npos, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_event_no_pio_view, null);
        ((X8MapPointMarkerViewGroup) view.findViewById(R.id.myview)).setPointEventNoPioValue(actionRes, angleRes, heightVale, npos, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createPointEventWithPioView(Context context, int actionRes, int angleRes, float heightVale, int npos, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_event_with_pio_view, null);
        ((X8MapPointMarkerViewGroup) view.findViewById(R.id.myview)).setPointEventValue(actionRes, angleRes, heightVale, npos, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createPointWithSmallArrow(Context context, int res, float angle, boolean isShow) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_arrow_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
            imageView.setRotation(angle);
        }
        if (!isShow) {
            imageView.setVisibility(4);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }
}
