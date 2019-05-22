package com.fimi.app.base;

import com.fimi.app.AppRouter;
import com.fimi.app.x8s.X8sRouter;
import com.fimi.host.HostConstants;
import com.fimi.kernel.FimiAppContext;
import com.fimi.kernel.base.BaseApplication;
import com.fimi.kernel.exception.CrashHandler;
import com.fimi.kernel.store.sqlite.helper.core.DbCore;
import com.fimi.kernel.utils.MediaSDK;
import com.fimi.libperson.PersonRouter;
import com.fimi.thirdpartysdk.ThirdLoginManager;

public class HostApplication extends BaseApplication {
    public void onCreate() {
        super.onCreate();
        HostConstants.initUrl();
        ThirdLoginManager.initThirdLogin(this);
        FimiAppContext.initKernel(this);
        MediaSDK.getInstance().init(this);
        DbCore.init(this);
        CrashHandler.getInstance().init(getApplicationContext());
        registerRouter();
    }

    private void registerRouter() {
        AppRouter.register();
        PersonRouter.register();
        X8sRouter.register(this);
    }

    public void onTerminate() {
        super.onTerminate();
        MediaSDK.getInstance().shutdown();
    }
}
