package com.fimi.libperson.ui.me.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.fimi.host.ComonStaticURL;
import com.fimi.host.HostConstants;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.Constants.UserType;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.language.RegionManage;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.DensityUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.R;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.ivew.IThirdLoginView;
import com.fimi.libperson.presenter.ThirdLoginPresenter;
import com.fimi.libperson.ui.me.register.RegisterActivity;
import com.fimi.libperson.ui.web.UserProtocolWebViewActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.libperson.widget.BitmapLoadTaskInstance.OnLoadListener;
import com.fimi.libperson.widget.LargeView;
import com.fimi.widget.DialogManager;
import com.fimi.widget.DialogManager.OnDialogListener;
import com.fimi.widget.NetworkLoadManage;
import router.Router;

public class LoginActivity extends BaseActivity implements IThirdLoginView, OnLoadListener {
    private static final String TAG = "LoginActivity";
    Button mBtnLogin;
    Button mBtnRegister;
    private DialogManager mDialogManager;
    ImageView mIvFacebook;
    ImageView mIvLogo;
    ImageView mIvMi;
    ImageView mIvTwitter;
    private LargeView mLargeView;
    private ThirdLoginPresenter mLoginPresenter;
    private RegionManage mRegionManage;
    RelativeLayout mRlLogin;
    TextView mTvNoLogin;
    TextView mTvRegion;
    RelativeLayout rlFimiLogin;
    RelativeLayout rlThirdLogin;

    public void initData() {
        getWindow().setFlags(1024, 1024);
        this.mIvLogo = (ImageView) findViewById(R.id.iv_logo);
        this.mBtnLogin = (Button) findViewById(R.id.btn_login);
        this.mBtnRegister = (Button) findViewById(R.id.btn_register);
        this.mIvTwitter = (ImageView) findViewById(R.id.iv_twitter);
        this.mIvFacebook = (ImageView) findViewById(R.id.iv_facebook);
        this.mIvMi = (ImageView) findViewById(R.id.iv_mi);
        this.mRlLogin = (RelativeLayout) findViewById(R.id.rl_login);
        this.mTvNoLogin = (TextView) findViewById(R.id.tv_no_login);
        this.mTvRegion = (TextView) findViewById(R.id.tv_region);
        this.mTvRegion.setText(getSpannableString());
        this.rlThirdLogin = (RelativeLayout) findViewById(R.id.rl_third_login);
        this.rlFimiLogin = (RelativeLayout) findViewById(R.id.rl_fimi_login);
        FontUtil.changeFontLanTing(getAssets(), this.mBtnLogin, this.mBtnRegister, this.mTvRegion, this.mTvNoLogin);
        if (Constants.productType == ProductEnum.X8S) {
            this.mTvNoLogin.setVisibility(0);
        } else {
            Constants.isRefreshMainView = true;
            ((LayoutParams) this.rlFimiLogin.getLayoutParams()).bottomMargin = DensityUtil.dip2px(this, 150.0f);
            this.mTvNoLogin.setVisibility(8);
        }
        this.mLoginPresenter = new ThirdLoginPresenter(this);
        this.mLargeView = (LargeView) findViewById(R.id.large_view);
        this.mLargeView.setImage(BitmapLoadTaskInstance.getInstance().getBitmap());
        BitmapLoadTaskInstance.getInstance().setOnLoadListener(this);
        if (BitmapLoadTaskInstance.getInstance().getBitmap() == null) {
            BitmapLoadTaskInstance.getInstance().setImage(ImageSource.asset("login_bg.jpg"), this.mContext);
        }
        this.mDialogManager = new DialogManager(this.mContext, getString(R.string.register_select_service_title), getSpannableStringSecurity(), getString(R.string.ensure), getString(R.string.cancel));
        this.mDialogManager.setClickOutIsCancle(true);
        this.mDialogManager.setSpan(true);
    }

    public void doTrans() {
        this.mBtnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.readyGo(LoginMainActivity.class);
                    return;
                }
                LoginActivity.this.mDialogManager.setOnDiaLogListener(new OnDialogListener() {
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                        LoginActivity.this.readyGo(LoginMainActivity.class);
                    }

                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                LoginActivity.this.mDialogManager.showDialog();
            }
        });
        this.mBtnRegister.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.readyGo(RegisterActivity.class);
                    return;
                }
                LoginActivity.this.mDialogManager.setOnDiaLogListener(new OnDialogListener() {
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                        LoginActivity.this.readyGo(RegisterActivity.class);
                    }

                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                LoginActivity.this.mDialogManager.showDialog();
            }
        });
        this.mIvFacebook.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mLoginPresenter.loginFacebook();
                    return;
                }
                LoginActivity.this.mDialogManager.setOnDiaLogListener(new OnDialogListener() {
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                        LoginActivity.this.mLoginPresenter.loginFacebook();
                    }

                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                LoginActivity.this.mDialogManager.showDialog();
            }
        });
        this.mIvTwitter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mLoginPresenter.loginTwitter();
                    return;
                }
                LoginActivity.this.mDialogManager.setOnDiaLogListener(new OnDialogListener() {
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                        LoginActivity.this.mLoginPresenter.loginTwitter();
                    }

                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                LoginActivity.this.mDialogManager.showDialog();
            }
        });
        this.mIvMi.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mLoginPresenter.loginMi();
                    return;
                }
                LoginActivity.this.mDialogManager.setOnDiaLogListener(new OnDialogListener() {
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                        LoginActivity.this.mLoginPresenter.loginMi();
                    }

                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                LoginActivity.this.mDialogManager.showDialog();
            }
        });
        this.mTvNoLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SPStoreManager.getInstance().saveInt(Constants.SP_PERSON_USER_TYPE, UserType.Guest.ordinal());
                LoginActivity.this.readyGoThenKillAllActivity((Intent) Router.invoke(LoginActivity.this, "activity://app.main"));
            }
        });
        this.mTvRegion.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Constants.isShowUserProtocol = true;
                Intent intent = (Intent) Router.invoke(LoginActivity.this.mContext, "activity://person.service");
                intent.putExtra("is_setting", false);
                LoginActivity.this.startActivityForResult(intent, 2);
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        if (Constants.isShowUserProtocol && !SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
            this.mDialogManager.setOnDiaLogListener(new OnDialogListener() {
                public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                    SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                }

                public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                }
            });
            this.mDialogManager.showDialog();
            Constants.isShowUserProtocol = false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            this.mTvRegion.setText(getSpannableString());
        } else {
            this.mLoginPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void loginThirdListener(boolean isSuccess, String msg) {
        NetworkLoadManage.dismiss();
        if (!isSuccess && msg != null) {
            ToastUtil.showToast(this.mContext, msg, 1);
        }
    }

    public void updateProgress(boolean isShow) {
        if (isShow) {
            NetworkLoadManage.show(this);
        } else {
            NetworkLoadManage.dismiss();
        }
    }

    public void loginSuccess() {
        Constants.isRefreshMainView = true;
        readyGoThenKillAllActivity((Intent) Router.invoke(this, "activity://app.main"));
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mLargeView != null) {
            this.mLargeView.setRecyle();
        }
        BitmapLoadTaskInstance.getInstance().setRecyle();
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        NetworkLoadManage.dismiss();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        NetworkLoadManage.dismiss();
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private SpannableString getSpannableStringSecurity() {
        String str1 = this.mContext.getString(R.string.register_select_service_login);
        String str2 = this.mContext.getString(R.string.register_select_service2_login);
        String str3 = this.mContext.getString(R.string.register_select_service3);
        String str4 = this.mContext.getString(R.string.register_select_service4_login);
        String str6 = this.mContext.getString(R.string.register_select_service6);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3 + str4 + str6);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_ecurity_label)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_ecurity_label)), str1.length() + str2.length(), (str1.length() + str2.length()) + str3.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_ecurity_label)), ((str1.length() + str2.length()) + str3.length()) + str4.length(), (((str1.length() + str2.length()) + str3.length()) + str4.length()) + str6.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(LoginActivity.this.getResources().getColor(R.color.libperson_ecurity));
                ds.setUnderlineText(false);
            }

            public void onClick(View widget) {
                LoginActivity.this.goWebActivity(ComonStaticURL.getPolicyUrl(), LoginActivity.this.getString(R.string.person_setting_user_agreement));
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(LoginActivity.this.getResources().getColor(R.color.libperson_ecurity));
                ds.setUnderlineText(false);
            }

            public void onClick(View widget) {
                LoginActivity.this.goWebActivity(ComonStaticURL.getPrivacyUrl(), LoginActivity.this.getString(R.string.person_setting_user_privacy));
            }
        }, (str1.length() + str2.length()) + str3.length(), ((str1.length() + str2.length()) + str3.length()) + str4.length(), 33);
        return spannableString;
    }

    public void goWebActivity(String url, String title) {
        Intent it = new Intent(this.mContext, UserProtocolWebViewActivity.class);
        it.putExtra("web_url", url);
        it.putExtra("web_title", title);
        startActivity(it);
        overridePendingTransition(17432576, 17432577);
    }

    private SpannableString getSpannableString() {
        this.mRegionManage = new RegionManage();
        String string = getString(ServiceItem.getServicename()[this.mRegionManage.getCountryType()]);
        String str1 = this.mContext.getString(R.string.libperson_select);
        String str2 = string;
        SpannableString spannableString = new SpannableString(str1 + str2);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_region_label)), 0, str1.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(LoginActivity.this.mContext.getResources().getColor(R.color.libperson_region));
            }

            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }

    public void onComplete() {
        if (this.mLargeView != null && !isFinishing() && BitmapLoadTaskInstance.getInstance().getBitmap() != null) {
            this.mLargeView.setImage(BitmapLoadTaskInstance.getInstance().getBitmap());
        }
    }
}
