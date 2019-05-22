package com.fimi.thirdpartysdk.login;

import android.content.Context;
import android.content.Intent;

public interface ILogin {
    void login(Context context, LoginCallback loginCallback);

    void onActivityResult(int i, int i2, Intent intent);
}
