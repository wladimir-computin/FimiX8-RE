package com.fimi.x8sdk.listener;

import com.fimi.x8sdk.entity.X8ErrorCodeInfo;
import java.util.List;

public interface IX8ErrorCodeListener {
    void cloudUnMountError(boolean z);

    void onErrorCode(List<X8ErrorCodeInfo> list);
}
