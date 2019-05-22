package com.fimi.thirdpartysdk.login;

public interface LoginCallback {
    void loginFail(String str);

    void loginSuccess(Object obj);
}
