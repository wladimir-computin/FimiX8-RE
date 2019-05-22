package com.fimi.libperson.presenter;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.libperson.R;
import com.fimi.libperson.ivew.ILoginView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.FwManager;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;
import java.util.ArrayList;

public class LoginPresenter {
    private static final int TIMER = 1;
    private static final int sUPDATE_TIME = 1000;
    Context mContext;
    private int mSeconds = 60;
    ILoginView mView;

    public LoginPresenter(ILoginView mView) {
        this.mView = mView;
        this.mContext = (Context) mView;
    }

    public void getVerificationCode(String phone) {
        if (DataValidatorUtil.isMobile(phone)) {
            UserManager.getIntance(this.mContext).getSecurityCode(phone, "0", "0", new DisposeDataHandle(new DisposeDataListener() {
                public void onSuccess(Object responseObj) {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        LoginPresenter.this.mSeconds = 60;
                        LoginPresenter.this.mView.getCodeResult(true, null);
                        return;
                    }
                    LoginPresenter.this.mView.getCodeResult(false, ErrorMessage.getUserModeErrorMessage(LoginPresenter.this.mContext, netModel.getErrCode()));
                }

                public void onFailure(Object reasonObj) {
                    LoginPresenter.this.mView.getCodeResult(false, LoginPresenter.this.mContext.getString(R.string.network_exception));
                }
            }));
        } else {
            this.mView.getCodeResult(false, this.mContext.getString(R.string.register_input_right_phone));
        }
    }

    public void loginByPhone(final String phone, String code) {
        if (TextUtils.isEmpty(phone)) {
            this.mView.iphoneLoginResult(false, this.mContext.getString(R.string.login_hint_iphone));
        } else if (DataValidatorUtil.isMobile(phone)) {
            UserManager.getIntance(this.mContext).loginFmUser(phone, code, "0", new DisposeDataHandle(new DisposeDataListener() {
                public void onSuccess(Object responseObj) {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        HostConstants.saveUserDetail(netModel.getData());
                        HostConstants.saveUserInfo("0", phone);
                        LoginPresenter.this.getFwDetail();
                        return;
                    }
                    LoginPresenter.this.mView.iphoneLoginResult(false, ErrorMessage.getUserModeErrorMessage(LoginPresenter.this.mContext, netModel.getErrCode()));
                }

                public void onFailure(Object reasonObj) {
                    LoginPresenter.this.mView.iphoneLoginResult(false, LoginPresenter.this.mContext.getString(R.string.network_exception));
                }
            }));
        } else {
            this.mView.iphoneLoginResult(false, this.mContext.getString(R.string.register_input_right_phone));
        }
    }

    public void loginByEmail(final String email, String password) {
        if (TextUtils.isEmpty(email)) {
            this.mView.emailLoginResult(false, this.mContext.getString(R.string.register_email_not_null));
        } else if (DataValidatorUtil.isEmail(email)) {
            UserManager.getIntance(this.mContext).loginFmUser(email, password, "1", new DisposeDataHandle(new DisposeDataListener() {
                public void onSuccess(Object responseObj) {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        HostConstants.saveUserDetail(netModel.getData());
                        HostConstants.saveUserInfo("1", email);
                        LoginPresenter.this.getFwDetail();
                        return;
                    }
                    LoginPresenter.this.mView.emailLoginResult(false, ErrorMessage.getUserModeErrorMessage(LoginPresenter.this.mContext, netModel.getErrCode()));
                    if (ErrorMessage.VERIFICATION_CODE_LOGIN_OUTTIME.equals(netModel.getErrCode())) {
                        LoginPresenter.this.mView.freorgottenPasswords(true);
                    } else {
                        LoginPresenter.this.mView.freorgottenPasswords(false);
                    }
                }

                public void onFailure(Object reasonObj) {
                    LoginPresenter.this.mView.emailLoginResult(false, LoginPresenter.this.mContext.getString(R.string.network_exception));
                }
            }));
        } else {
            this.mView.emailLoginResult(false, this.mContext.getString(R.string.register_input_right_email));
        }
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
                r3 = com.fimi.libperson.presenter.LoginPresenter.this;
                r3 = r3.mView;
                if (r3 == 0) goto L_0x004e;
            L_0x0047:
                r3 = com.fimi.libperson.presenter.LoginPresenter.this;
                r3 = r3.mView;
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
                r3 = com.fimi.libperson.presenter.LoginPresenter.this;
                r3 = r3.mView;
                if (r3 == 0) goto L_0x004e;
            L_0x007c:
                r3 = com.fimi.libperson.presenter.LoginPresenter.this;
                r3 = r3.mView;
                r3.loginSuccess();
                goto L_0x004e;
            L_0x0084:
                r3 = move-exception;
                r4 = com.fimi.libperson.presenter.LoginPresenter.this;
                r4 = r4.mView;
                if (r4 == 0) goto L_0x0092;
            L_0x008b:
                r4 = com.fimi.libperson.presenter.LoginPresenter.this;
                r4 = r4.mView;
                r4.loginSuccess();
            L_0x0092:
                throw r3;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.fimi.libperson.presenter.LoginPresenter$AnonymousClass4.onSuccess(java.lang.Object):void");
            }

            public void onFailure(Object reasonObj) {
                if (LoginPresenter.this.mView != null) {
                    LoginPresenter.this.mView.loginSuccess();
                }
            }
        }));
    }
}
