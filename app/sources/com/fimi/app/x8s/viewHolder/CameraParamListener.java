package com.fimi.app.x8s.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;

public interface CameraParamListener {
    void gotoSubItem(String str, String str2, ViewHolder viewHolder);

    void itemReturnBack(String str, String... strArr);
}
