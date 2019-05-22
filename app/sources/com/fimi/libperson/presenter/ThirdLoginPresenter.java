package com.fimi.libperson.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.R;
import com.fimi.libperson.ivew.IThirdLoginView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.FwManager;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.ThirdAcountDto;
import com.fimi.thirdpartysdk.ThirdPartyConstants;
import com.fimi.thirdpartysdk.login.LoginCallback;
import com.fimi.thirdpartysdk.login.ThirdLoginManager;
import com.github.moduth.blockcanary.internal.BlockInfo;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ThirdLoginPresenter {
    private static final int DELAY = 50000;
    private static final int sUPDATE_PROGRESS = 1;
    IThirdLoginView loginView;
    Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ThirdLoginPresenter.this.loginView.updateProgress(false);
            }
        }
    };
    private ThirdLoginManager mThirdLoginManager;

    public ThirdLoginPresenter(IThirdLoginView loginView) {
        this.loginView = loginView;
        this.mContext = (Context) loginView;
        this.mThirdLoginManager = ThirdLoginManager.getInstance();
        this.mThirdLoginManager.init();
    }

    public void loginFacebook() {
        if (AbAppUtil.isNetworkAvailable(this.mContext)) {
            if (this.loginView != null) {
                this.loginView.updateProgress(true);
            }
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 50000);
            this.mThirdLoginManager.login("2", this.mContext, new LoginCallback() {
                public void loginSuccess(Object object) {
                    if (ThirdLoginPresenter.this.loginView != null) {
                        ThirdLoginPresenter.this.loginView.updateProgress(true);
                    }
                    Map<String, String> db = (Map) object;
                    ThirdAcountDto thirdAcountDto = new ThirdAcountDto();
                    StringBuilder sb = new StringBuilder();
                    thirdAcountDto.setName((String) db.get("name"));
                    thirdAcountDto.setThirdId((String) db.get(BlockInfo.KEY_UID));
                    thirdAcountDto.setUserImgUrl((String) db.get("iconurl"));
                    thirdAcountDto.setLoginChannel("2");
                    UserManager.getIntance(ThirdLoginPresenter.this.mContext).thirdUserLogin(thirdAcountDto, new DisposeDataHandle(new DisposeDataListener() {
                        public void onSuccess(Object responseObj) {
                            NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                            if (netModel.isSuccess()) {
                                HostConstants.saveUserDetail(netModel.getData());
                                HostConstants.saveUserInfo(null, null);
                                ThirdLoginPresenter.this.getFwDetail();
                                return;
                            }
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, ErrorMessage.getUserModeErrorMessage(ThirdLoginPresenter.this.mContext, netModel.getErrCode()));
                        }

                        public void onFailure(Object reasonObj) {
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, reasonObj.toString());
                        }
                    }));
                }

                public void loginFail(String error) {
                    if (error.contains("net::ERR_CONNECTION_RESET")) {
                        error = ThirdLoginPresenter.this.mContext.getResources().getString(R.string.libperson_facebook_connection);
                    }
                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, error);
                }
            });
        } else if (this.loginView != null) {
            this.loginView.loginThirdListener(false, this.mContext.getString(R.string.network_exception));
        }
    }

    public void loginTwitter() {
        if (!checkApkExist(this.mContext, "com.twitter.android")) {
            ToastUtil.showToast(this.mContext, R.string.login_install_twitter, 1);
        } else if (AbAppUtil.isNetworkAvailable(this.mContext)) {
            if (this.loginView != null) {
                this.loginView.updateProgress(true);
            }
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 50000);
            this.mThirdLoginManager.login(ThirdPartyConstants.LOGIN_CHANNEL_TW, this.mContext, new LoginCallback() {
                public void loginSuccess(Object object) {
                    if (ThirdLoginPresenter.this.loginView != null) {
                        ThirdLoginPresenter.this.loginView.updateProgress(true);
                    }
                    Map<String, String> db = (Map) object;
                    ThirdAcountDto thirdAcountDto = new ThirdAcountDto();
                    StringBuilder sb = new StringBuilder();
                    sb.append(((String) db.get("name")) + " ");
                    thirdAcountDto.setName(sb.toString());
                    thirdAcountDto.setThirdId((String) db.get("userId"));
                    thirdAcountDto.setUserImgUrl((String) db.get("iconurl"));
                    thirdAcountDto.setLoginChannel(ThirdPartyConstants.LOGIN_CHANNEL_TW);
                    UserManager.getIntance(ThirdLoginPresenter.this.mContext).thirdUserLogin(thirdAcountDto, new DisposeDataHandle(new DisposeDataListener() {
                        public void onSuccess(Object responseObj) {
                            NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                            if (netModel.isSuccess()) {
                                HostConstants.saveUserDetail(netModel.getData());
                                HostConstants.saveUserInfo(null, null);
                                ThirdLoginPresenter.this.getFwDetail();
                                return;
                            }
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, ErrorMessage.getUserModeErrorMessage(ThirdLoginPresenter.this.mContext, netModel.getErrCode()));
                        }

                        public void onFailure(Object reasonObj) {
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, reasonObj.toString());
                        }
                    }));
                }

                public void loginFail(String error) {
                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, error);
                }
            });
        } else if (this.loginView != null) {
            this.loginView.loginThirdListener(false, this.mContext.getString(R.string.network_exception));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mThirdLoginManager.onActivityResult(requestCode, resultCode, data);
    }

    public void loginMi() {
        if (AbAppUtil.isNetworkAvailable(this.mContext)) {
            if (this.loginView != null) {
                this.loginView.updateProgress(true);
            }
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 50000);
            this.mThirdLoginManager.login("1", this.mContext, new LoginCallback() {
                public void loginSuccess(Object object) {
                    if (ThirdLoginPresenter.this.loginView != null) {
                        ThirdLoginPresenter.this.loginView.updateProgress(true);
                    }
                    JSONObject dataJsonObject = (JSONObject) object;
                    ThirdAcountDto thirdAcountDto = new ThirdAcountDto();
                    if (dataJsonObject.has("miliaoIcon_orig")) {
                        try {
                            thirdAcountDto.setUserImgUrl(dataJsonObject.getString("miliaoIcon_orig"));
                            if (dataJsonObject.isNull("userId")) {
                                thirdAcountDto.setThirdId(dataJsonObject.getString("unionId"));
                            } else {
                                thirdAcountDto.setThirdId(dataJsonObject.getString("userId"));
                            }
                            thirdAcountDto.setName(dataJsonObject.getString("miliaoNick"));
                            thirdAcountDto.setNickName(dataJsonObject.getString("miliaoNick"));
                            thirdAcountDto.setLoginChannel("1");
                            UserManager.getIntance(ThirdLoginPresenter.this.mContext).thirdUserLogin(thirdAcountDto, new DisposeDataHandle(new DisposeDataListener() {
                                public void onSuccess(Object responseObj) {
                                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                                    if (netModel.isSuccess()) {
                                        HostConstants.saveUserDetail(netModel.getData());
                                        HostConstants.saveUserInfo(null, null);
                                        ThirdLoginPresenter.this.getFwDetail();
                                        return;
                                    }
                                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, ErrorMessage.getUserModeErrorMessage(ThirdLoginPresenter.this.mContext, netModel.getErrCode()));
                                }

                                public void onFailure(Object reasonObj) {
                                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, ThirdLoginPresenter.this.mContext.getString(R.string.network_exception));
                                }
                            }));
                        } catch (JSONException e) {
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, e.toString());
                        }
                    }
                }

                public void loginFail(String error) {
                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, error);
                }
            });
        } else if (this.loginView != null) {
            this.loginView.loginThirdListener(false, this.mContext.getString(R.string.network_exception));
        }
    }

    public void setPause() {
        this.mHandler.removeMessages(1);
    }

    private void getFwDetail() {
        FwManager x9FwManager = new FwManager();
        HostConstants.saveFirmwareDetail(new ArrayList());
        x9FwManager.getX9FwNetDetail(new DisposeDataHandle(new DisposeDataListener() {
            /* JADX WARNING: Failed to extract finally block: empty outs */
            public void onSuccess(java.lang.Object r7) {
                /*
                r6 = this;
                r3 = r7.toString();	 Catch:{ Exception -> 0x004f }
                r4 = com.fimi.network.entity.NetModel.class;
                r2 = com.alibaba.fastjson.JSON.parseObject(r3, r4);	 Catch:{ Exception -> 0x004f }
                r2 = (com.fimi.network.entity.NetModel) r2;	 Catch:{ Exception -> 0x004f }
                r3 = "moweiru";
                r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x004f }
                r4.<init>();	 Catch:{ Exception -> 0x004f }
                r5 = "responseObj:";
                r4 = r4.append(r5);	 Catch:{ Exception -> 0x004f }
                r4 = r4.append(r7);	 Catch:{ Exception -> 0x004f }
                r4 = r4.toString();	 Catch:{ Exception -> 0x004f }
                com.fimi.kernel.utils.LogUtil.d(r3, r4);	 Catch:{ Exception -> 0x004f }
                r3 = r2.isSuccess();	 Catch:{ Exception -> 0x004f }
                if (r3 == 0) goto L_0x0041;
            L_0x002a:
                r3 = r2.getData();	 Catch:{ Exception -> 0x004f }
                if (r3 == 0) goto L_0x0041;
            L_0x0030:
                r3 = r2.getData();	 Catch:{ Exception -> 0x004f }
                r3 = r3.toString();	 Catch:{ Exception -> 0x004f }
                r4 = com.fimi.network.entity.UpfirewareDto.class;
                r1 = com.alibaba.fastjson.JSON.parseArray(r3, r4);	 Catch:{ Exception -> 0x004f }
                com.fimi.host.HostConstants.saveFirmwareDetail(r1);	 Catch:{ Exception -> 0x004f }
            L_0x0041:
                r3 = com.fimi.libperson.presenter.ThirdLoginPresenter.this;
                r3 = r3.loginView;
                if (r3 == 0) goto L_0x004e;
            L_0x0047:
                r3 = com.fimi.libperson.presenter.ThirdLoginPresenter.this;
                r3 = r3.loginView;
                r3.loginSuccess();
            L_0x004e:
                return;
            L_0x004f:
                r0 = move-exception;
                r3 = new java.util.ArrayList;	 Catch:{ all -> 0x0084 }
                r3.<init>();	 Catch:{ all -> 0x0084 }
                com.fimi.host.HostConstants.saveFirmwareDetail(r3);	 Catch:{ all -> 0x0084 }
                r3 = com.fimi.host.HostLogBack.getInstance();	 Catch:{ all -> 0x0084 }
                r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0084 }
                r4.<init>();	 Catch:{ all -> 0x0084 }
                r5 = "固件Json转换异常：";
                r4 = r4.append(r5);	 Catch:{ all -> 0x0084 }
                r5 = r0.getMessage();	 Catch:{ all -> 0x0084 }
                r4 = r4.append(r5);	 Catch:{ all -> 0x0084 }
                r4 = r4.toString();	 Catch:{ all -> 0x0084 }
                r3.writeLog(r4);	 Catch:{ all -> 0x0084 }
                r3 = com.fimi.libperson.presenter.ThirdLoginPresenter.this;
                r3 = r3.loginView;
                if (r3 == 0) goto L_0x004e;
            L_0x007c:
                r3 = com.fimi.libperson.presenter.ThirdLoginPresenter.this;
                r3 = r3.loginView;
                r3.loginSuccess();
                goto L_0x004e;
            L_0x0084:
                r3 = move-exception;
                r4 = com.fimi.libperson.presenter.ThirdLoginPresenter.this;
                r4 = r4.loginView;
                if (r4 == 0) goto L_0x0092;
            L_0x008b:
                r4 = com.fimi.libperson.presenter.ThirdLoginPresenter.this;
                r4 = r4.loginView;
                r4.loginSuccess();
            L_0x0092:
                throw r3;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.fimi.libperson.presenter.ThirdLoginPresenter$AnonymousClass5.onSuccess(java.lang.Object):void");
            }

            public void onFailure(Object reasonObj) {
                if (ThirdLoginPresenter.this.loginView != null) {
                    ThirdLoginPresenter.this.loginView.loginSuccess();
                }
            }
        }));
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, 8192);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
