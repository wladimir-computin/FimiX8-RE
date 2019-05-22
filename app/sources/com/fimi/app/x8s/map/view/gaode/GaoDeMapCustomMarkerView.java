package com.fimi.app.x8s.map.view.gaode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.map.view.AbsMapCustomMarkerView;
import com.fimi.app.x8s.tools.X8NumberUtil;

public class GaoDeMapCustomMarkerView extends AbsMapCustomMarkerView {
    public BitmapDescriptor createCustomMarkerViewForP2P(Context context, int res, float heightVale, int npos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view_for_point, null);
        ((TextView) view.findViewById(R.id.point_heightValue)).setText(X8NumberUtil.getDistanceNumberString(heightVale, 0, true));
        ImageView imageView = (ImageView) view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        return BitmapDescriptorFactory.fromView(view);
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
        return BitmapDescriptorFactory.fromView(view);
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
        return BitmapDescriptorFactory.fromView(view);
    }

    public BitmapDescriptor createCustomMarkerView(Context context, int res) {
        return BitmapDescriptorFactory.fromResource(res);
    }

    public BitmapDescriptor createInreterstMarkerView0(Context context, int res) {
        return BitmapDescriptorFactory.fromView(LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view3, null));
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
        return BitmapDescriptorFactory.fromView(view);
    }
}
