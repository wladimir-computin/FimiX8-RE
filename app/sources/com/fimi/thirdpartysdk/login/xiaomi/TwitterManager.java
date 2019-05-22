package com.fimi.thirdpartysdk.login.xiaomi;

import android.content.Context;
import android.content.Intent;
import com.fimi.thirdpartysdk.login.ILogin;
import com.fimi.thirdpartysdk.login.LoginCallback;
import com.fimi.thirdpartysdk.login.twitter.TwitterApiManager;
import java.util.Map;

public class TwitterManager implements ILogin {
    LoginCallback loginCallback;
    TwitterApiManager mTwitterApiClient;

    public void login(Context mContext, LoginCallback callback) {
        this.loginCallback = callback;
        this.mTwitterApiClient = new TwitterApiManager(mContext);
        this.mTwitterApiClient.login(new TwitterApiManager.LoginCallback() {
            public void onSuccess(Map<String, String> map) {
                TwitterManager.this.loginCallback.loginSuccess(map);
            }

            public void onFailure() {
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mTwitterApiClient.onActivityResult(requestCode, resultCode, data);
    }
}
