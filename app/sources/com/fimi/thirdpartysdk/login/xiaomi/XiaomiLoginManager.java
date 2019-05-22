package com.fimi.thirdpartysdk.login.xiaomi;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.FimiAppContext;
import com.fimi.thirdpartysdk.R;
import com.fimi.thirdpartysdk.ThirdPartyConstants;
import com.fimi.thirdpartysdk.login.ILogin;
import com.fimi.thirdpartysdk.login.LoginCallback;
import com.fimi.thirdpartysdk.login.LoginResultCache;
import com.xiaomi.account.openauth.XMAuthericationException;
import com.xiaomi.account.openauth.XiaomiOAuthConstants;
import com.xiaomi.account.openauth.XiaomiOAuthFuture;
import com.xiaomi.account.openauth.XiaomiOAuthResults;
import com.xiaomi.account.openauth.XiaomiOAuthorize;
import java.io.IOException;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

public class XiaomiLoginManager implements ILogin {
    LoginCallback loginCallback;
    private Context mContext;
    private String mErrormessage;
    private XiaomiOAuthResults mResults;
    XiaoMiEntity xiaoMiEntity;

    class XiaoMiEntity {
        public String appId;
        public String redirectUri;

        XiaoMiEntity() {
        }
    }

    public XiaoMiEntity getXiaoMiEntity(ProductEnum product) {
        XiaoMiEntity xiaoMiEntity = new XiaoMiEntity();
        xiaoMiEntity.redirectUri = "http://www.fimi.com";
        if (product.equals(ProductEnum.X9)) {
            xiaoMiEntity.appId = ThirdPartyConstants.XIAOMI_APP_ID_X9;
        } else if (product.equals(ProductEnum.GH2)) {
            xiaoMiEntity.appId = ThirdPartyConstants.XIAOMI_APP_ID_GH2;
        } else {
            xiaoMiEntity.appId = ThirdPartyConstants.XIAOMI_APP_ID_FIMIAPP;
        }
        return xiaoMiEntity;
    }

    public void login(Context context, LoginCallback listener) {
        this.mContext = context;
        this.loginCallback = listener;
        scopes = new int[2];
        this.xiaoMiEntity = getXiaoMiEntity(Constants.productType);
        scopes[0] = 1;
        scopes[1] = 3;
        try {
            waitAndShowFutureResult(new XiaomiOAuthorize().setAppId(Long.parseLong(this.xiaoMiEntity.appId)).setRedirectUrl(this.xiaoMiEntity.redirectUri).setScope(Arrays.copyOf(scopes, 0)).setKeepCookies(true).setNoMiui(false).setSkipConfirm(false).startGetAccessToken((Activity) context));
        } catch (Exception e) {
            e.printStackTrace();
            if (this.loginCallback != null) {
                this.loginCallback.loginFail(this.mContext.getResources().getString(R.string.login_result));
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private <V> void waitAndShowFutureResult(final XiaomiOAuthFuture<V> future) {
        new AsyncTask<Void, Void, V>() {
            Exception e;

            /* Access modifiers changed, original: protected */
            public void onPreExecute() {
            }

            /* Access modifiers changed, original: protected|varargs */
            public V doInBackground(Void... params) {
                V v = null;
                try {
                    return future.getResult();
                } catch (IOException e1) {
                    this.e = e1;
                    return v;
                } catch (OperationCanceledException e12) {
                    this.e = e12;
                    return v;
                } catch (XMAuthericationException e13) {
                    this.e = e13;
                    return v;
                }
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(V v) {
                if (v != null) {
                    if (v instanceof XiaomiOAuthResults) {
                        XiaomiLoginManager.this.mResults = (XiaomiOAuthResults) v;
                        if (XiaomiLoginManager.this.mResults.getAccessToken() != null) {
                            LoginResultCache.setAccessToken(FimiAppContext.getContext(), 0, XiaomiLoginManager.this.mResults.getAccessToken());
                            LoginResultCache.setMacKey(FimiAppContext.getContext(), 0, XiaomiLoginManager.this.mResults.getMacKey());
                            LoginResultCache.setMacAlgorithm(FimiAppContext.getContext(), 0, XiaomiLoginManager.this.mResults.getMacAlgorithm());
                            XiaomiLoginManager.this.loginAuth();
                        } else if (XiaomiLoginManager.this.loginCallback != null) {
                            XiaomiLoginManager.this.loginCallback.loginFail(XiaomiLoginManager.this.mContext.getResources().getString(R.string.login_result));
                        }
                    }
                } else if (this.e != null) {
                    if (this.e.toString().equals("android.accounts.OperationCanceledException")) {
                        if (XiaomiLoginManager.this.loginCallback != null) {
                            XiaomiLoginManager.this.loginCallback.loginFail(null);
                        }
                    } else if (XiaomiLoginManager.this.loginCallback != null) {
                        XiaomiLoginManager.this.loginCallback.loginFail(this.e.getMessage());
                    }
                } else if (XiaomiLoginManager.this.loginCallback != null) {
                    XiaomiLoginManager.this.loginCallback.loginFail(XiaomiLoginManager.this.mContext.getResources().getString(R.string.login_result));
                }
            }
        }.execute(new Void[0]);
    }

    private void loginAuth() {
        final XiaomiOAuthFuture<String> futureProfile = new XiaomiOAuthorize().callOpenApi(this.mContext, Long.parseLong(this.xiaoMiEntity.appId), XiaomiOAuthConstants.OPEN_API_PATH_PROFILE, this.mResults.getAccessToken(), this.mResults.getMacKey(), this.mResults.getMacAlgorithm());
        new AsyncTask<Void, Void, String>() {
            Exception e;

            /* Access modifiers changed, original: protected */
            public void onPreExecute() {
            }

            /* Access modifiers changed, original: protected|varargs */
            public String doInBackground(Void... params) {
                String v = null;
                try {
                    return (String) futureProfile.getResult();
                } catch (IOException e1) {
                    this.e = e1;
                    return v;
                } catch (OperationCanceledException e12) {
                    this.e = e12;
                    return v;
                } catch (XMAuthericationException e13) {
                    this.e = e13;
                    return v;
                }
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(String result) {
                if (!TextUtils.isEmpty(result)) {
                    try {
                        XiaomiLoginManager.this.loginCallback.loginSuccess(new JSONObject(result).getJSONObject("data"));
                    } catch (JSONException e) {
                        XiaomiLoginManager.this.loginCallback.loginFail(e.getMessage());
                    }
                } else if (XiaomiLoginManager.this.loginCallback != null) {
                    XiaomiLoginManager.this.loginCallback.loginFail(XiaomiLoginManager.this.mContext.getResources().getString(R.string.login_result));
                }
            }
        }.execute(new Void[0]);
    }
}
