package com.fimi.app.x8s.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;

public interface SubParamItemListener {
    void checkDetailParam(String str, String str2, int i, ViewHolder viewHolder);

    void checkResolutionDetailParam(String str, String str2, String str3, int i, ViewHolder viewHolder);

    void gotoParentItem();

    void setRecyclerScroller(boolean z);

    void styleParam(String str, int i);

    void updateAddContent(String str, String str2);
}
