package com.fimi.app.x8s.interfaces;

import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;

public interface IX8AiLineHistoryListener {
    void addLineItem(X8AiLinePointInfo x8AiLinePointInfo);

    int favoritesCapacity();

    void goFavorites();

    void onItemChange(long j, int i, int i2);

    void onItemChange(long j, String str, int i);

    void onSelectId(long j, int i);
}
