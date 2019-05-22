package com.fimi.libperson.ui.setting;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LanguageUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.R;
import com.fimi.libperson.ivew.ILibpersonRightApplyView;
import com.fimi.libperson.presenter.LibPersonRightApplyPresenter;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.NetworkLoadManage;

public class LibPersonRightApplyActivity extends BasePersonActivity implements ILibpersonRightApplyView {
    private LibPersonRightApplyPresenter libPersonRightApplyPresenter;
    private Button libpersonBtnApplySend;
    private EditText libpersonEtEmail;
    private TextView libpersonTvApplyHint;
    private TitleView mTitleView;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.libperson_activity_right_apply;
    }

    public void initData() {
        initView();
    }

    private void initView() {
        this.libPersonRightApplyPresenter = new LibPersonRightApplyPresenter(this, this);
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(getResources().getString(R.string.libperson_user_right));
        this.libpersonTvApplyHint = (TextView) findViewById(R.id.libperson_tv_apply_hint);
        FontUtil.changeFontLanTing(getAssets(), this.libpersonTvApplyHint);
        this.libpersonEtEmail = (EditText) findViewById(R.id.libperson_et_email);
        this.libpersonBtnApplySend = (Button) findViewById(R.id.libperson_btn_apply_send);
        sendBtnIsClick(false);
    }

    public void doTrans() {
        this.libpersonEtEmail.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                if (DataValidatorUtil.isEmail(LibPersonRightApplyActivity.this.libpersonEtEmail.getText().toString().trim())) {
                    LibPersonRightApplyActivity.this.sendBtnIsClick(true);
                } else {
                    LibPersonRightApplyActivity.this.sendBtnIsClick(false);
                }
            }
        });
        this.libpersonBtnApplySend.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                NetworkLoadManage.show(LibPersonRightApplyActivity.this);
                LibPersonRightApplyActivity.this.libPersonRightApplyPresenter.sendEmail(LibPersonRightApplyActivity.this.libpersonEtEmail.getText().toString(), LanguageUtil.getCurrentLanguage().getInternalCoutry(), Constants.productType.name().toLowerCase());
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendBtnIsClick(boolean isClick) {
        if (isClick) {
            this.libpersonBtnApplySend.setEnabled(true);
        } else {
            this.libpersonBtnApplySend.setEnabled(false);
        }
    }

    public void sendSuccess(String hint) {
        ToastUtil.showToast((Context) this, hint, 1);
        NetworkLoadManage.dismiss();
    }

    public void sendFailure(String hint) {
        ToastUtil.showToast((Context) this, hint, 1);
        NetworkLoadManage.dismiss();
    }
}
