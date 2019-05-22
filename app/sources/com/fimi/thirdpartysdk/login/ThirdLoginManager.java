package com.fimi.thirdpartysdk.login;

import android.content.Context;
import android.content.Intent;
import com.fimi.thirdpartysdk.ThirdPartyConstants;
import com.fimi.thirdpartysdk.login.xiaomi.FaceBookManager;
import com.fimi.thirdpartysdk.login.xiaomi.TwitterManager;
import com.fimi.thirdpartysdk.login.xiaomi.XiaomiLoginManager;

public class ThirdLoginManager {
    private static ThirdLoginManager thirdLoginManager;
    ILogin login;

    public static ThirdLoginManager getInstance() {
        if (thirdLoginManager == null) {
            thirdLoginManager = new ThirdLoginManager();
        }
        return thirdLoginManager;
    }

    public void init() {
    }

    public void login(String chanelId, Context mContext, LoginCallback loginCallback) {
        if ("1".equalsIgnoreCase(chanelId)) {
            this.login = new XiaomiLoginManager();
        }
        if ("2".equalsIgnoreCase(chanelId)) {
            this.login = new FaceBookManager();
        } else if (ThirdPartyConstants.LOGIN_CHANNEL_TW.equals(chanelId)) {
            this.login = new TwitterManager();
        }
        this.login.login(mContext, loginCallback);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.login.onActivityResult(requestCode, resultCode, data);
    }
}
