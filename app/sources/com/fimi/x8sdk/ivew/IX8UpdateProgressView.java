package com.fimi.x8sdk.ivew;

import com.fimi.x8sdk.update.fwpack.FwInfo;
import java.util.List;

public interface IX8UpdateProgressView {
    void showUpdateProgress(boolean z, int i, List<FwInfo> list, String str);
}
