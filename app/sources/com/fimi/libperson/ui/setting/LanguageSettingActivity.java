package com.fimi.libperson.ui.setting;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.fimi.kernel.Constants;
import com.fimi.kernel.GlobalConfig;
import com.fimi.kernel.language.LanguageItem;
import com.fimi.kernel.language.LanguageModel;
import com.fimi.kernel.percent.PercentRelativeLayout.LayoutParams;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.LanguageUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.R;
import com.fimi.libperson.adapter.LanguageAdapter;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.NetworkDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageSettingActivity extends BasePersonActivity implements OnClickListener, OnItemClickListener {
    private LanguageAdapter adapter;
    int currPosition;
    private List<LanguageItem> languageItems;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0 && LanguageSettingActivity.this.mNetworkDialog != null) {
                LocalBroadcastManager.getInstance(LanguageSettingActivity.this.mContext).sendBroadcast(new Intent(Constants.CHANGELANGUGE));
                LanguageSettingActivity.this.restart();
            }
        }
    };
    private ListView mListView;
    NetworkDialog mNetworkDialog;
    private TitleView mTitleView;
    private LanguageModel selectedLanguageModel;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    private void initView() {
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mListView = (ListView) findViewById(R.id.lv_l_setting_setting);
        LayoutParams params = (LayoutParams) this.mTitleView.getLayoutParams();
        params.setMargins(0, this.statusBarHeight + this.marginStatus, 0, 0);
        this.mTitleView.setLayoutParams(params);
        this.mTitleView.setTvTitle(getString(R.string.language_setting_title));
        this.mNetworkDialog = new NetworkDialog(this.mContext, com.fimi.sdk.R.style.network_load_progress_dialog, true);
        this.mNetworkDialog.setCanceledOnTouchOutside(false);
        this.mNetworkDialog.setCancelable(false);
    }

    private void initListener() {
    }

    public void initData() {
        initView();
        initListener();
    }

    public void doTrans() {
        this.selectedLanguageModel = (LanguageModel) SPStoreManager.getInstance().getObject(Constants.LANGUAGETYPE, LanguageModel.class);
        if (this.selectedLanguageModel == null) {
            this.selectedLanguageModel = LanguageUtil.getLanguageModel(Locale.getDefault());
        }
        this.languageItems = new ArrayList();
        for (int i = 0; i < LanguageItem.languageModels.length; i++) {
            LanguageItem item = new LanguageItem();
            LanguageModel model = LanguageItem.languageModels[i];
            item.setInfo(model.getLanguageName());
            item.setCode(model.getLanguageCode());
            if (model.getLanguageCode().equals(this.selectedLanguageModel.getLanguageCode()) && model.getCountry().equals(this.selectedLanguageModel.getCountry())) {
                item.setSelect(true);
                this.currPosition = i;
            }
            this.languageItems.add(item);
        }
        this.adapter = new LanguageAdapter(this.languageItems, this);
        this.mListView.setAdapter(this.adapter);
        this.mListView.setOnItemClickListener(this);
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_user_language_setting;
    }

    @RequiresApi(api = 24)
    public void onSelectLanguage(int choseType) {
        if (choseType != this.currPosition) {
            this.currPosition = choseType;
            this.mNetworkDialog.show();
            this.selectedLanguageModel = LanguageItem.languageModels[choseType];
            LanguageUtil.changeAppLanguage(this.mContext, this.selectedLanguageModel.getLocale());
            GlobalConfig.getInstance().setLanguageModel(this.selectedLanguageModel);
            SPStoreManager.getInstance().saveObject(Constants.LANGUAGETYPE, this.selectedLanguageModel);
            this.mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    private void restart() {
        finish();
    }

    @RequiresApi(api = 24)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        onSelectLanguage(position);
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mNetworkDialog != null && this.mNetworkDialog.isShowing()) {
            this.mNetworkDialog.dismiss();
        }
        this.mHandler.removeCallbacksAndMessages(null);
    }
}
