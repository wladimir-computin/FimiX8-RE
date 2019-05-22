package com.fimi.app.x8s;

import android.content.Context;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import router.Router;

public class X8sRouter {
    public static void register(Context context) {
        Router.router("activity://x8s.main", X8sMainActivity.class);
        new X8Application().onCreate(context);
    }
}
