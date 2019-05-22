package com.fimi.kernel.network.okhttp.listener;

import java.util.ArrayList;

public interface DisposeHandleCookieListener extends DisposeDataListener {
    void onCookie(ArrayList<String> arrayList);
}
