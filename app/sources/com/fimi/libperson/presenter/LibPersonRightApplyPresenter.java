package com.fimi.libperson.presenter;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.libperson.R;
import com.fimi.libperson.ivew.ILibpersonRightApplyView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;

public class LibPersonRightApplyPresenter {
    Context context;
    ILibpersonRightApplyView iLibpersonRightApplyView;

    public LibPersonRightApplyPresenter(ILibpersonRightApplyView iLibpersonRightApplyView, Context context) {
        this.iLibpersonRightApplyView = iLibpersonRightApplyView;
        this.context = context;
    }

    public void sendEmail(String email, String language, String whatApp) {
        if (TextUtils.isEmpty(email)) {
            this.iLibpersonRightApplyView.sendFailure(this.context.getString(R.string.register_email_not_null));
        } else if (DataValidatorUtil.isEmail(email)) {
            UserManager.getIntance(this.context).sendEmail(email, language, whatApp, new DisposeDataHandle(new DisposeDataListener() {
                public void onSuccess(Object responseObj) {
                    try {
                        NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                        if (netModel.isSuccess()) {
                            LibPersonRightApplyPresenter.this.iLibpersonRightApplyView.sendSuccess(LibPersonRightApplyPresenter.this.context.getString(R.string.libperson_send_hint_one));
                        } else {
                            LibPersonRightApplyPresenter.this.iLibpersonRightApplyView.sendFailure(ErrorMessage.getUserModeErrorMessage(LibPersonRightApplyPresenter.this.context, netModel.getErrCode()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(Object reasonObj) {
                    LibPersonRightApplyPresenter.this.iLibpersonRightApplyView.sendFailure(LibPersonRightApplyPresenter.this.context.getString(R.string.network_exception));
                }
            }));
        } else {
            this.iLibpersonRightApplyView.sendFailure(this.context.getString(R.string.register_input_right_email));
        }
    }
}
