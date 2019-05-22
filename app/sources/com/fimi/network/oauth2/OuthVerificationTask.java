package com.fimi.network.oauth2;

import com.alibaba.fastjson.JSON;
import com.baidu.tts.loopj.AsyncHttpClient;
import com.facebook.internal.ServerProtocol;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.network.entity.AccessTokenEntity;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;
import okhttp3.HttpUrl;

public class OuthVerificationTask {
    public void getAuthorizationCode(final CallBackListner backListner) {
        OAuthClientRequest oAuthClientRequest = new OAuthClientRequest();
        oAuthClientRequest.setClientId(OauthConstant.CLIENT_ID);
        oAuthClientRequest.setResponseType("code");
        oAuthClientRequest.setRedirectURI("http://www.fimi.com");
        RequestParams requestParams = new RequestParams();
        requestParams.put("client_id", oAuthClientRequest.getClientId());
        requestParams.put(ServerProtocol.DIALOG_PARAM_RESPONSE_TYPE, oAuthClientRequest.getResponseType());
        requestParams.put("redirect_uri", oAuthClientRequest.getRedirectURI());
        CommonOkHttpClient.getUrl(CommonRequest.createGetOriginalRequest(HostConstants.HostURL + "v1/oauth/authorize", requestParams), new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                String code = ((HttpUrl) responseObj).queryParameter("code");
                if (code != null) {
                    backListner.onSuccess(code);
                }
            }

            public void onFailure(Object reasonObj) {
            }
        }));
    }

    public void getAccessToken(String code) {
        OAuthClientRequest oAuthClientRequest = new OAuthClientRequest();
        oAuthClientRequest.setClientId(OauthConstant.CLIENT_ID);
        oAuthClientRequest.setRedirectURI("http://www.fimi.com");
        oAuthClientRequest.setClientSecret(OauthConstant.CLIENT_SECRET);
        oAuthClientRequest.setGrantType(OauthConstant.AUTHORIZATION_CODE);
        oAuthClientRequest.setCode(code);
        RequestParams requestParams = new RequestParams();
        requestParams.put("client_id", oAuthClientRequest.getClientId());
        requestParams.put("redirect_uri", oAuthClientRequest.getRedirectURI());
        requestParams.put("client_secret", oAuthClientRequest.getClientSecret());
        requestParams.put(OAuthConstants.PARAM_GRANT_TYPE, oAuthClientRequest.getGrantType());
        requestParams.put("code", code);
        RequestParams headers = new RequestParams();
        headers.put(AsyncHttpClient.HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded");
        CommonOkHttpClient.postUrl(CommonRequest.createPostOriginalRequest(HostConstants.HostURL + "v1/oauth/accessToken", requestParams, headers), new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                if (responseObj != null) {
                    try {
                        SPStoreManager.getInstance().saveString(OauthConstant.ACCESS_TOKEN_SP, ((AccessTokenEntity) JSON.parseObject(responseObj.toString(), AccessTokenEntity.class)).getAccess_token());
                        HostLogBack.getInstance().writeLog("获取access_token 成功！");
                    } catch (Exception e) {
                        HostLogBack.getInstance().writeLog("获取access_token 失败 ==> " + e.getMessage().toString());
                        e.printStackTrace();
                    }
                }
            }

            public void onFailure(Object reasonObj) {
            }
        }));
    }
}
