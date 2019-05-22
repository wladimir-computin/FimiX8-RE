package com.fimi.libperson.ui.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.fimi.host.ComonStaticURL;
import com.fimi.host.HostConstants;
import com.fimi.kernel.Constants;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.R;
import com.fimi.libperson.adapter.PersettingThirdAdapt;
import com.fimi.libperson.adapter.PersettingThirdAdapt.State;
import com.fimi.libperson.adapter.PersonSettingAdapt;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.entity.PersonSetting;
import com.fimi.libperson.ui.me.login.LoginActivity;
import com.fimi.libperson.ui.web.UserProtocolWebViewActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.libperson.widget.BitmapLoadTaskInstance.OnLoadListener;
import com.fimi.libperson.widget.TitleView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;
import com.fimi.widget.DialogManager;
import com.fimi.widget.DialogManager.OnDialogListener;
import java.util.ArrayList;
import java.util.List;

public class LibPersonAboutActivity extends BasePersonActivity implements OnLoadListener {
    boolean isLogin = false;
    private Button libpersonBtnRepeal;
    private TextView libpersonTvVersions;
    BitmapLoadTaskInstance mBitmapLoadTaskInstance;
    private ListView mLvMainSetting;
    private PersettingThirdAdapt mPersettingThirdAdapt;
    private OnItemClickListener mThirdListerner = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            State positionIndex = ((PersonSetting) LibPersonAboutActivity.this.mThirdPersonSettings.get(position)).getThirdAdapt();
            if (positionIndex == State.USER_PRIVACY) {
                LibPersonAboutActivity.this.goWebActivity(ComonStaticURL.getPrivacyUrl(), LibPersonAboutActivity.this.getString(R.string.person_setting_user_privacy));
            } else if (positionIndex == State.USER_AGREEMENT) {
                LibPersonAboutActivity.this.goWebActivity(ComonStaticURL.getPolicyUrl(), LibPersonAboutActivity.this.getString(R.string.person_setting_user_agreement));
            } else if (positionIndex == State.USER_RIGHT) {
                LibPersonAboutActivity.this.readyGo(LibPersonRightApplyActivity.class);
            }
        }
    };
    private List<PersonSetting> mThirdPersonSettings;
    private TitleView mTitleView;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.libperson_activity_about;
    }

    public void initData() {
        initView();
        initListView();
        this.mPersettingThirdAdapt = new PersettingThirdAdapt(this);
        this.mThirdPersonSettings = new ArrayList();
        for (State state : State.values()) {
            if (this.isLogin || !state.equals(State.USER_RIGHT)) {
                PersonSetting setting = new PersonSetting();
                setting.setIsOPen(Boolean.valueOf(true));
                setting.setThirdAdapt(state);
                this.mThirdPersonSettings.add(setting);
            }
        }
        this.mPersettingThirdAdapt.setData(this.mThirdPersonSettings);
        this.mLvMainSetting.setAdapter(this.mPersettingThirdAdapt);
        this.mLvMainSetting.setOnItemClickListener(this.mThirdListerner);
        this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
    }

    private void initView() {
        this.isLogin = !TextUtils.isEmpty(HostConstants.getUserDetail().getFimiId());
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(getResources().getString(R.string.libperson_about));
        this.mLvMainSetting = (ListView) findViewById(R.id.lv_main_setting);
        this.libpersonTvVersions = (TextView) findViewById(R.id.libperson_tv_versions);
        this.libpersonBtnRepeal = (Button) findViewById(R.id.libperson_btn_repeal);
        this.libpersonBtnRepeal.setVisibility(this.isLogin ? 0 : 8);
        FontUtil.changeFontLanTing(getAssets(), this.libpersonTvVersions, findViewById(R.id.libperson_tv_rights_reserved), this.libpersonBtnRepeal);
        this.libpersonTvVersions.setText(getResources().getString(R.string.app_product_name) + " " + getResources().getString(R.string.app_version) + "");
    }

    private void initListView() {
        for (PersonSettingAdapt.State state : PersonSettingAdapt.State.values()) {
            boolean isAdd = true;
            for (PersonSettingAdapt.State state2 : PersonSettingAdapt.mMainState) {
                if (state == state2) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                PersonSetting setting = new PersonSetting();
                setting.setIsOPen(Boolean.valueOf(true));
                setting.setSettingAdaptState(state);
            }
        }
    }

    public void doTrans() {
        this.libpersonBtnRepeal.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DialogManager dialogManager = new DialogManager(LibPersonAboutActivity.this.mContext, LibPersonAboutActivity.this.getString(R.string.libperson_repeal_accredit), LibPersonAboutActivity.this.getString(R.string.libperson_repeal_accredit_hint), LibPersonAboutActivity.this.getString(R.string.login_ensure), LibPersonAboutActivity.this.getString(R.string.person_setting_dialog_exit_left_text));
                dialogManager.setVerticalScreen(true);
                dialogManager.setOnDiaLogListener(new OnDialogListener() {
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        UserManager.getIntance(LibPersonAboutActivity.this).sendRepealAccredit(Constants.productType.name().toLowerCase(), new DisposeDataHandle(new DisposeDataListener() {
                            public void onSuccess(Object responseObj) {
                                try {
                                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                                    if (netModel.isSuccess()) {
                                        LibPersonAboutActivity.this.mBitmapLoadTaskInstance.setOnLoadListener(LibPersonAboutActivity.this);
                                        LibPersonAboutActivity.this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), LibPersonAboutActivity.this.mContext);
                                        return;
                                    }
                                    ToastUtil.showToast(LibPersonAboutActivity.this, ErrorMessage.getUserModeErrorMessage(LibPersonAboutActivity.this, netModel.getErrCode()), 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            public void onFailure(Object reasonObj) {
                                ToastUtil.showToast(LibPersonAboutActivity.this, LibPersonAboutActivity.this.mContext.getString(R.string.network_exception), 1);
                            }
                        }));
                    }

                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                dialogManager.showDialog();
            }
        });
    }

    public void goWebActivity(String url, String title) {
        Intent it = new Intent(this, UserProtocolWebViewActivity.class);
        it.putExtra("web_url", url);
        it.putExtra("web_title", title);
        startActivity(it);
        overridePendingTransition(17432576, 17432577);
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.fimi.x8sdk.common.Constants.A12_TCP_CMD_PORT) {
            recreate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mBitmapLoadTaskInstance != null) {
            this.mBitmapLoadTaskInstance.setOnLoadListener(null);
        }
    }

    public void onComplete() {
        SPStoreManager.getInstance().removeKey(HostConstants.SP_KEY_USER_DETAIL);
        SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, false);
        readyGoThenKillAllActivity(LoginActivity.class);
    }
}
