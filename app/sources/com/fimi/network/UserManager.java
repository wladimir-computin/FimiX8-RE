package com.fimi.network;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.entity.RestPswDto;
import com.fimi.network.entity.ThirdAcountDto;
import com.umeng.commonsdk.proguard.g;

public class UserManager extends BaseManager {
    private static UserManager mUserManager;

    public UserManager(Context context) {
    }

    public static UserManager getIntance(Context context) {
        if (mUserManager == null) {
            mUserManager = new UserManager(context.getApplicationContext());
        }
        return mUserManager;
    }

    public void thirdUserLogin(ThirdAcountDto dto, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(dto).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.USER_LOGIN_URL + "loginByThirdAccount", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void getSecurityCode(String account, String codeFun, String codeType, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("codeFunc", codeFun);
        requestParams.put("codeType", codeType);
        CommonOkHttpClient.get(CommonRequest.createGetRequest(HostConstants.USER_LOGIN_URL_V2 + "getSecurityCode", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void loginFmUser(String account, String password, String loginType, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("password", password);
        requestParams.put("loginType", loginType);
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.USER_LOGIN_URL_V2 + "loginUser", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void registerFmUser(String account, String code, String password, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", account);
        requestParams.put("code", code);
        requestParams.put("password", password);
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.USER_LOGIN_URL + "registerFmAcountByPhone", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void registerByEmail(String email, String pwd, String confirmPwd, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);
        requestParams.put("password", pwd);
        requestParams.put("confirmPwd", confirmPwd);
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.USER_LOGIN_URL + "registerFmAcountByEmail", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void sendEmail(String email, String language, String whatApp, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);
        requestParams.put(g.M, language);
        requestParams.put("whatApp", whatApp);
        CommonOkHttpClient.get(CommonRequest.createGetRequest(HostConstants.RIGHT_APPLY_V1 + "reqPersonalData", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void sendRepealAccredit(String whatApp, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("whatApp", whatApp);
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.RIGHT_APPLY_V1 + "revokeAuthorization", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void resetPassword(RestPswDto restPswDto, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(restPswDto).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.USER_LOGIN_URL + "restAccountPwdByEmail", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void resetIphonePassword(RestPswDto restPswDto, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(restPswDto).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.USER_LOGIN_URL + "restPasswordByPhone", getRequestParams(requestParams)), disposeDataHandle);
    }
}
