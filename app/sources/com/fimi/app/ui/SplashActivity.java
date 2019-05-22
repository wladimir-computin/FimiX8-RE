package com.fimi.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.fimi.android.app.R;
import com.fimi.app.ui.main.HostNewMainActivity;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.libdownfw.service.AppInitService;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.ui.me.login.LoginActivity;
import com.fimi.libperson.ui.setting.ServiceSettingActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.network.ApkVersionManager;
import com.fimi.network.FwManager;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.LetterSpacingTextView;
import java.util.ArrayList;
import router.Router;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private final long Skip_Time = 1500;
    private final int Skip_What = 0;
    private ApkVersionManager mApkVersionManager;
    private BitmapLoadTaskInstance mBitmapLoadTaskInstance;
    TextView mTvBottom;
    LetterSpacingTextView mTvTitle;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
    }

    @RequiresApi(api = 21)
    public void initData() {
        getWindow().setFlags(1024, 1024);
        this.mTvTitle = (LetterSpacingTextView) findViewById(R.id.tv_title);
        if (Constants.productType == ProductEnum.X8S) {
            this.mTvTitle.setText(getResources().getText(R.string.app_fimi_slogn));
        }
        this.mTvBottom = (TextView) findViewById(R.id.tv_bottom);
        this.mTvBottom.setText(getSpannableString());
        FontUtil.changeFontLanTing(getAssets(), this.mTvTitle, this.mTvBottom);
        this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
        this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), this.mContext);
        ((ImageView) findViewById(R.id.img_splash)).setBackgroundResource(getMetaDataInt(this, getString(R.string.splash_icon)));
        this.mApkVersionManager = new ApkVersionManager();
    }

    public int getMetaDataInt(Context context, String name) {
        try {
            return context.getApplicationContext().getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(), 128).metaData.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void doTrans() {
        if ((getIntent().getFlags() & 4194304) != 0) {
            finish();
            return;
        }
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory("android.intent.category.LAUNCHER") && action != null && action.equals("android.intent.action.MAIN")) {
                finish();
                return;
            }
        }
        final String fimiId = HostConstants.getUserDetail().getFimiId();
        if (fimiId == null || "".equals(fimiId)) {
            this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
            this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), this.mContext);
        }
        SPStoreManager.getInstance().saveInt("x9_grahpic_hint", 0);
        SPStoreManager.getInstance().saveInt(HostConstants.SP_KEY_UPDATE_CHECK, 2);
        ThreadUtils.execute(new Runnable() {
            public void run() {
                SplashActivity.this.getFwDetail();
            }
        });
        new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    SplashActivity.this.startService(new Intent(SplashActivity.this, AppInitService.class));
                    if (Constants.productType == ProductEnum.X9) {
                        SplashActivity.this.mContext.startService((Intent) Router.invoke(SplashActivity.this.mContext, "service://x9.recordupload"));
                    } else if (Constants.productType == ProductEnum.GH2) {
                        SplashActivity.this.mContext.startService((Intent) Router.invoke(SplashActivity.this.mContext, "service://gh2.recordupload"));
                    }
                    if (SPStoreManager.getInstance().getInt(Constants.SP_PERSON_USER_TYPE) > 0 || !TextUtils.isEmpty(fimiId)) {
                        Constants.isRefreshMainView = true;
                        SplashActivity.this.readyGoThenKill(HostNewMainActivity.class);
                    } else if (SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class) == null) {
                        SplashActivity.this.readyGoThenKill(ServiceSettingActivity.class);
                    } else {
                        SplashActivity.this.readyGoThenKill(LoginActivity.class);
                    }
                }
                super.handleMessage(msg);
            }
        }.sendEmptyMessageDelayed(0, 1500);
    }

    private void getFwDetail() {
        new FwManager().getX9FwNetDetail(new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                try {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    LogUtil.d("moweiru", "responseObj:" + responseObj);
                    if (netModel.isSuccess() && netModel.getData() != null) {
                        HostConstants.saveFirmwareDetail(JSON.parseArray(netModel.getData().toString(), UpfirewareDto.class));
                    }
                } catch (Exception e) {
                    HostConstants.saveFirmwareDetail(new ArrayList());
                    HostLogBack.getInstance().writeLog("固件Json转换异常：" + e.getMessage());
                }
            }

            public void onFailure(Object reasonObj) {
            }
        }));
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    private SpannableString getSpannableString() {
        String str = this.mContext.getString(R.string.splash_copyright_2017_2018fimi_all_rights_reserved);
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.splash_bottom1)), 0, 8, 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.splash_bottom2)), 9, 21, 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.splash_bottom1)), 22, str.length(), 33);
        return spannableString;
    }
}
