package com.fimi.libperson.ui.me.login;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.fimi.kernel.base.BaseFragment;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.libperson.R;
import com.fimi.libperson.ivew.IForgetIphonePasswordView;
import com.fimi.libperson.presenter.ForgetIphonePasswordPresenter;

public class ForgetIphonePasswordFragment extends BaseFragment implements IForgetIphonePasswordView {
    private static final String TAG = "ForgetIphonePasswordFra";
    private boolean isShowPassword;
    private boolean isShowPasswordAgain;
    Button mBtnFiLoginIphone;
    EditText mEtFiAccount;
    EditText mEtFiVerification;
    EditText mEtNewPassword;
    EditText mEtNewPasswordAgain;
    private ForgetIphonePasswordPresenter mForgetIphonePasswordPresenter;
    ImageView mIvNewPasswordAgainUnified;
    ImageView mIvNewPasswordUnified;
    ImageView mIvShowPassword;
    ImageView mIvShowPasswordAgain;
    private OnResetIphonePasswordListerner mListerner;
    private OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 4 || actionId == 6 || (event != null && 66 == event.getKeyCode() && event.getAction() == 0)) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
            }
            return false;
        }
    };
    private State mState = State.IPHONE;
    TextView mTvFiAreaCode;
    TextView mTvFiErrorHint;
    TextView mTvFiGetValidationCode;
    TextView mTvFiPasswordErrorHint;
    TextView mTvFiSelectCountry;
    TextView mTvFiTitleSubName;
    TextView mTvTitleSubName;
    View mVNpDivider;
    View mVNpDividerAgain;
    View mView1;
    View mView2;
    View mViewDivide;

    class EditTextWatcher implements TextWatcher {
        private EditText mEditText;

        public EditTextWatcher(EditText editText) {
            this.mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                return;
            }
            if (R.id.et_fi_account == this.mEditText.getId()) {
                if (DataValidatorUtil.isMobile(s.toString().trim())) {
                    ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setEnabled(true);
                    ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
                    return;
                }
                ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.login_get_verfication_unclick));
                ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setEnabled(false);
            } else if (R.id.et_fi_verification == this.mEditText.getId()) {
                if (s.length() == 4 && DataValidatorUtil.isMobile(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString().trim())) {
                    ForgetIphonePasswordFragment.this.showClickState(true);
                } else {
                    ForgetIphonePasswordFragment.this.showClickState(false);
                }
                ForgetIphonePasswordFragment.this.mTvFiErrorHint.setText(null);
            } else if (R.id.et_new_password == this.mEditText.getId()) {
                if (!ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) || s.length() < 8) {
                    ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(8);
                    ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(8);
                    ForgetIphonePasswordFragment.this.setIvShowPassword(true);
                    ForgetIphonePasswordFragment.this.showClickState(false);
                } else {
                    ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(0);
                    ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(0);
                    ForgetIphonePasswordFragment.this.setIvShowPassword(false);
                    ForgetIphonePasswordFragment.this.showClickState(true);
                }
                ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
                ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
            } else if (R.id.et_new_password_again == this.mEditText.getId()) {
                if (!ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) || s.length() < 8) {
                    ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(8);
                    ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(8);
                    ForgetIphonePasswordFragment.this.setIvShowPassword(true);
                    ForgetIphonePasswordFragment.this.showClickState(false);
                } else {
                    ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(0);
                    ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(0);
                    ForgetIphonePasswordFragment.this.setIvShowPassword(false);
                    ForgetIphonePasswordFragment.this.showClickState(true);
                }
                ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
                ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
            }
        }
    }

    interface OnResetIphonePasswordListerner {
        void resetIphoneSuccess();
    }

    public enum State {
        IPHONE,
        NEW_PASSWORD
    }

    public void setIphone(String iphone) {
        if (this.mEtFiAccount != null) {
            this.mEtFiAccount.setText(iphone);
        }
        showState();
    }

    public void setListerner(OnResetIphonePasswordListerner listerner) {
        this.mListerner = listerner;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mListerner = (OnResetIphonePasswordListerner) context;
    }

    public int getLayoutId() {
        return R.layout.fragment_forget_iphone;
    }

    /* Access modifiers changed, original: protected */
    public void initData(View view) {
        this.mTvFiTitleSubName = (TextView) view.findViewById(R.id.tv_fi_title_sub_name);
        this.mTvFiSelectCountry = (TextView) view.findViewById(R.id.tv_fi_select_country);
        this.mEtFiAccount = (EditText) view.findViewById(R.id.et_fi_account);
        this.mEtFiVerification = (EditText) view.findViewById(R.id.et_fi_verification);
        this.mTvFiGetValidationCode = (TextView) view.findViewById(R.id.btn_fi_get_validation_code);
        this.mTvTitleSubName = (TextView) view.findViewById(R.id.tv_title_sub_name);
        this.mTvFiErrorHint = (TextView) view.findViewById(R.id.tv_fi_error_hint);
        this.mTvFiPasswordErrorHint = (TextView) view.findViewById(R.id.tv_fi_password_error_hint);
        this.mBtnFiLoginIphone = (Button) view.findViewById(R.id.btn_fi_login_phone);
        this.mTvFiAreaCode = (TextView) view.findViewById(R.id.tv_fi_area_code);
        this.mView1 = view.findViewById(R.id.view1);
        this.mView2 = view.findViewById(R.id.view2);
        this.mViewDivide = view.findViewById(R.id.v_divide);
        this.mEtNewPassword = (EditText) view.findViewById(R.id.et_new_password);
        this.mEtNewPasswordAgain = (EditText) view.findViewById(R.id.et_new_password_again);
        this.mIvNewPasswordUnified = (ImageView) view.findViewById(R.id.iv_new_password_unified);
        this.mIvNewPasswordAgainUnified = (ImageView) view.findViewById(R.id.iv_new_password_again_unified);
        this.mVNpDivider = view.findViewById(R.id.v_np_divider);
        this.mVNpDividerAgain = view.findViewById(R.id.v_np_again_divider);
        this.mIvShowPassword = (ImageView) view.findViewById(R.id.iv_show_password);
        this.mIvShowPasswordAgain = (ImageView) view.findViewById(R.id.iv_show_password_again);
        this.mTvFiSelectCountry.setText(getResources().getString(R.string.libperson_service_china));
        this.mTvFiGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvFiGetValidationCode.setEnabled(false);
        this.mIvNewPasswordUnified.setVisibility(8);
        this.mIvNewPasswordAgainUnified.setVisibility(8);
        this.mEtNewPassword.setVisibility(4);
        this.mEtNewPasswordAgain.setVisibility(4);
        this.mIvShowPassword.setVisibility(8);
        this.mIvShowPasswordAgain.setVisibility(8);
        this.mEtNewPassword.addTextChangedListener(new EditTextWatcher(this.mEtNewPassword));
        this.mEtNewPasswordAgain.addTextChangedListener(new EditTextWatcher(this.mEtNewPasswordAgain));
        this.mEtFiVerification.addTextChangedListener(new EditTextWatcher(this.mEtFiVerification));
        this.mEtFiAccount.addTextChangedListener(new EditTextWatcher(this.mEtFiAccount));
        this.mForgetIphonePasswordPresenter = new ForgetIphonePasswordPresenter(this, getActivity());
        showState();
    }

    /* Access modifiers changed, original: protected */
    public void doTrans() {
        OnClickListerner();
    }

    private void OnClickListerner() {
        this.mTvFiGetValidationCode.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
                ForgetIphonePasswordFragment.this.mForgetIphonePasswordPresenter.sendIphone(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString());
            }
        });
        this.mBtnFiLoginIphone.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
                ForgetIphonePasswordFragment.this.mBtnFiLoginIphone.setEnabled(true);
                if (ForgetIphonePasswordFragment.this.mState == State.IPHONE) {
                    ForgetIphonePasswordFragment.this.mForgetIphonePasswordPresenter.inputVerficationCode(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtFiVerification.getText().toString().trim());
                } else {
                    ForgetIphonePasswordFragment.this.mForgetIphonePasswordPresenter.inputPassword(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtFiVerification.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim());
                }
            }
        });
        this.mIvShowPassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ForgetIphonePasswordFragment.this.isShowPassword) {
                    ForgetIphonePasswordFragment.this.isShowPassword = false;
                    ForgetIphonePasswordFragment.this.mEtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetIphonePasswordFragment.this.isShowPassword = true;
                    ForgetIphonePasswordFragment.this.mEtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetIphonePasswordFragment.this.mEtNewPassword.requestFocus();
                ForgetIphonePasswordFragment.this.mEtNewPassword.setSelection(ForgetIphonePasswordFragment.this.mEtNewPassword.getText().length());
            }
        });
        this.mIvShowPasswordAgain.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ForgetIphonePasswordFragment.this.isShowPasswordAgain) {
                    ForgetIphonePasswordFragment.this.isShowPasswordAgain = false;
                    ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetIphonePasswordFragment.this.isShowPasswordAgain = true;
                    ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.requestFocus();
                ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.setSelection(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().length());
            }
        });
        this.mEtFiVerification.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtFiAccount.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtNewPassword.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtNewPasswordAgain.setOnEditorActionListener(this.mOnEditorActionListener);
    }

    /* Access modifiers changed, original: protected */
    public void initMVP() {
    }

    public void sendIphone(boolean isSuccess, String error) {
        this.mBtnFiLoginIphone.setEnabled(true);
        if (isSuccess) {
            this.mTvFiErrorHint.setText(null);
            showState();
        } else if (error != null) {
            this.mTvFiErrorHint.setText(error);
            this.mTvFiErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    public void sendVerfication(boolean isSuccess, String error) {
        if (isSuccess) {
            this.mTvFiErrorHint.setText(null);
            this.mTvFiGetValidationCode.setEnabled(true);
            this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mForgetIphonePasswordPresenter.setStopTime();
            this.mTvFiPasswordErrorHint.setText(null);
            showClickState(false);
            this.mState = State.NEW_PASSWORD;
            showState();
        } else if (error != null) {
            this.mTvFiErrorHint.setText(error);
            this.mTvFiErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    public void resetPassword(boolean isSuccess, String error) {
        if (isSuccess) {
            if (this.mListerner != null) {
                this.mEtFiAccount.setText(null);
                this.mEtFiVerification.setText(null);
                this.mEtNewPassword.setText(null);
                this.mEtNewPasswordAgain.setText(null);
                this.mState = State.IPHONE;
                showState();
                this.mListerner.resetIphoneSuccess();
            }
        } else if (error != null) {
            this.mTvFiPasswordErrorHint.setText(error);
            this.mTvFiPasswordErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    public void updateSeconds(boolean isComplete, int seconds) {
        if (isComplete) {
            this.mTvFiGetValidationCode.setEnabled(true);
            this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            return;
        }
        this.mTvFiGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvFiGetValidationCode.setEnabled(false);
        this.mTvFiGetValidationCode.setText(seconds + getString(R.string.login_second));
    }

    private void showState() {
        if (this.mState == State.IPHONE) {
            this.mEtFiAccount.setVisibility(0);
            this.mEtFiVerification.setVisibility(0);
            this.mTvFiSelectCountry.setVisibility(0);
            this.mView1.setVisibility(0);
            this.mView2.setVisibility(0);
            this.mTvFiAreaCode.setVisibility(0);
            this.mTvFiGetValidationCode.setVisibility(0);
            this.mBtnFiLoginIphone.setText(R.string.login_btn_next);
            this.mTvFiErrorHint.setVisibility(0);
            this.mTvFiErrorHint.setVisibility(0);
            this.mTvFiPasswordErrorHint.setVisibility(8);
            this.mIvShowPasswordAgain.setVisibility(4);
            this.mIvShowPassword.setVisibility(4);
            this.mIvNewPasswordUnified.setVisibility(4);
            this.mIvNewPasswordAgainUnified.setVisibility(4);
            this.mEtNewPassword.setVisibility(4);
            this.mEtNewPasswordAgain.setVisibility(4);
            this.mVNpDivider.setVisibility(4);
            this.mVNpDividerAgain.setVisibility(4);
            this.mViewDivide.setVisibility(4);
            if (DataValidatorUtil.isMobile(this.mEtFiAccount.getText().toString().trim()) && this.mEtFiVerification.getText().length() == 4) {
                showClickState(true);
            } else {
                showClickState(false);
            }
            if (DataValidatorUtil.isMobile(this.mEtFiAccount.getText().toString())) {
                Log.i(TAG, "showState: 1");
                this.mTvFiGetValidationCode.setEnabled(true);
                this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
                return;
            }
            Log.i(TAG, "showState: 2");
            this.mTvFiGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
            this.mTvFiGetValidationCode.setEnabled(false);
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mEtFiAccount.setVisibility(4);
            this.mEtFiVerification.setVisibility(4);
            this.mTvFiSelectCountry.setVisibility(4);
            this.mView1.setVisibility(4);
            this.mView2.setVisibility(4);
            this.mTvFiAreaCode.setVisibility(4);
            this.mTvFiGetValidationCode.setVisibility(4);
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mTvFiErrorHint.setVisibility(4);
            this.mBtnFiLoginIphone.setText(R.string.login_reset_password);
            this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
            this.mTvFiPasswordErrorHint.setTextColor(getResources().getColor(R.color.forget_password_hint));
            this.mTvFiPasswordErrorHint.setVisibility(0);
            this.mViewDivide.setVisibility(0);
            this.mIvShowPassword.setVisibility(0);
            this.mIvShowPasswordAgain.setVisibility(0);
            this.mEtNewPassword.setVisibility(0);
            this.mEtNewPasswordAgain.setVisibility(0);
            this.mVNpDivider.setVisibility(0);
            this.mVNpDividerAgain.setVisibility(0);
            if (!this.mEtNewPasswordAgain.getText().toString().trim().equals(this.mEtNewPassword.getText().toString().trim()) || this.mEtNewPasswordAgain.getText().toString().length() < 8) {
                setIvShowPassword(true);
            } else {
                setIvShowPassword(false);
            }
        }
    }

    public void setIvShowPassword(boolean isShowPassword) {
        int i;
        int i2 = 8;
        this.mIvShowPassword.setVisibility(isShowPassword ? 0 : 8);
        ImageView imageView = this.mIvShowPasswordAgain;
        if (isShowPassword) {
            i = 0;
        } else {
            i = 8;
        }
        imageView.setVisibility(i);
        View view = this.mVNpDivider;
        if (isShowPassword) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        view = this.mVNpDividerAgain;
        if (isShowPassword) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        imageView = this.mIvNewPasswordUnified;
        if (isShowPassword) {
            i = 8;
        } else {
            i = 0;
        }
        imageView.setVisibility(i);
        ImageView imageView2 = this.mIvNewPasswordAgainUnified;
        if (!isShowPassword) {
            i2 = 0;
        }
        imageView2.setVisibility(i2);
    }

    public void setState(State state) {
        this.mState = state;
    }

    public State getState() {
        return this.mState;
    }

    public void setBack() {
        if (this.mState == State.IPHONE) {
            this.mTvFiGetValidationCode.setEnabled(true);
            this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mForgetIphonePasswordPresenter.setStopTime();
            this.mEtFiAccount.setText(null);
            this.mEtFiVerification.setText(null);
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mEtNewPassword.setText(null);
            this.mEtNewPasswordAgain.setText(null);
            this.mState = State.IPHONE;
            this.mEtFiVerification.setText(null);
            showState();
        }
    }

    private void showClickState(boolean isClick) {
        if (isClick) {
            this.mBtnFiLoginIphone.setEnabled(true);
        } else {
            this.mBtnFiLoginIphone.setEnabled(false);
        }
    }

    private SpannableString getSpannableString() {
        String str1 = this.mContext.getString(R.string.login_send_email_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        String str3 = this.mContext.getString(R.string.login_send_email_hint3);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), (str1.length() + str2.length()) + str3.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }

    private SpannableString getEmailVerficationSpannableString() {
        String str1 = this.mContext.getString(R.string.login_email_send_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        SpannableString spannableString = new SpannableString(str1 + str2);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }
}
