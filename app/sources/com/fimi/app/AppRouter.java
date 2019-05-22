package com.fimi.app;

import com.fimi.app.ui.main.HostNewMainActivity;
import router.Router;

public class AppRouter {
    public static void register() {
        Router.router("activity://app.main", HostNewMainActivity.class);
    }
}
