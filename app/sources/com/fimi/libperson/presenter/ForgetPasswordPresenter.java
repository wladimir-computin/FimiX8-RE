package com.fimi.libperson.presenter;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.libperson.R;
import com.fimi.libperson.ivew.IForgetPasswordView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.RestPswDto;

public class ForgetPasswordPresenter {
    Context mContext;
    private IForgetPasswordView mIForgetPasswordView;

    public ForgetPasswordPresenter(IForgetPasswordView IForgetPasswordView, Context context) {
        this.mIForgetPasswordView = IForgetPasswordView;
        this.mContext = context;
    }

    public void sendEmail(String emailAddress) {
        UserManager.getIntance(this.mContext).getSecurityCode(emailAddress, "1", "1", new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                try {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        ForgetPasswordPresenter.this.mIForgetPasswordView.sendEmail(true, null);
                    } else {
                        ForgetPasswordPresenter.this.mIForgetPasswordView.sendEmail(false, ErrorMessage.getUserModeErrorMessage(ForgetPasswordPresenter.this.mContext, netModel.getErrCode()));
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Object reasonObj) {
                ForgetPasswordPresenter.this.mIForgetPasswordView.sendEmail(false, ForgetPasswordPresenter.this.mContext.getString(R.string.network_exception));
            }
        }));
    }

    public void inputVerficationCode(String emailAddress, String verficationCode) {
        RestPswDto restPswDto = new RestPswDto();
        restPswDto.setEmail(emailAddress);
        restPswDto.setCode(verficationCode);
        restPswDto.setCheckPsw("0");
        restPswDto.setPassword("");
        restPswDto.setConfirmPassword("");
        UserManager.getIntance(this.mContext).resetPassword(restPswDto, new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                if (netModel.isSuccess()) {
                    ForgetPasswordPresenter.this.mIForgetPasswordView.inputVerfication(true, null);
                } else {
                    ForgetPasswordPresenter.this.mIForgetPasswordView.inputVerfication(false, ErrorMessage.getUserModeErrorMessage(ForgetPasswordPresenter.this.mContext, netModel.getErrCode()));
                }
            }

            public void onFailure(Object reasonObj) {
                ForgetPasswordPresenter.this.mIForgetPasswordView.inputVerfication(false, ForgetPasswordPresenter.this.mContext.getString(R.string.network_exception));
            }
        }));
    }

    public void inputPassword(String emailAddress, String code, String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            RestPswDto restPswDto = new RestPswDto();
            restPswDto.setEmail(emailAddress);
            restPswDto.setCode(code);
            restPswDto.setPassword(password);
            restPswDto.setConfirmPassword(confirmPassword);
            UserManager.getIntance(this.mContext).resetPassword(restPswDto, new DisposeDataHandle(new DisposeDataListener() {
                public void onSuccess(Object responseObj) {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        ForgetPasswordPresenter.this.mIForgetPasswordView.resetPassword(true, null);
                    } else {
                        ForgetPasswordPresenter.this.mIForgetPasswordView.resetPassword(false, ErrorMessage.getUserModeErrorMessage(ForgetPasswordPresenter.this.mContext, netModel.getErrCode()));
                    }
                }

                public void onFailure(Object reasonObj) {
                    ForgetPasswordPresenter.this.mIForgetPasswordView.resetPassword(false, ForgetPasswordPresenter.this.mContext.getString(R.string.network_exception));
                }
            }));
            return;
        }
        this.mIForgetPasswordView.resetPassword(false, this.mContext.getString(R.string.login_input_password_different_hint));
    }
}
