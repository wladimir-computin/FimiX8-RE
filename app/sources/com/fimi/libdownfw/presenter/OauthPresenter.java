package com.fimi.libdownfw.presenter;

import com.fimi.network.oauth2.CallBackListner;
import com.fimi.network.oauth2.OuthVerificationTask;

public class OauthPresenter {
    OuthVerificationTask verificationTask = new OuthVerificationTask();

    public void getAccessToken() {
        this.verificationTask.getAuthorizationCode(new CallBackListner() {
            public void onSuccess(Object result) {
                OauthPresenter.this.verificationTask.getAccessToken((String) result);
            }
        });
    }
}
