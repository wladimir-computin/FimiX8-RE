package com.fimi.app.x8s.ui.activity.update;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.manager.X8FpvManager;
import com.fimi.host.HostConstants;
import com.fimi.host.LocalFwEntity;
import com.fimi.kernel.Constants;
import com.fimi.kernel.animutils.IOUtils;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.base.EventMessage;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.widget.DownRoundProgress;
import com.fimi.x8sdk.controller.UpdateManager;
import com.fimi.x8sdk.ivew.IX8UpdateProgressView;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.process.RelayProcess;
import com.fimi.x8sdk.update.UpdateUtil;
import com.fimi.x8sdk.update.fwpack.FwInfo;
import com.twitter.sdk.android.core.internal.scribe.EventsFilesManager;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class X8UpdatingActivity extends BaseActivity {
    private Button btnReconnect;
    private DownRoundProgress downRoundProgress;
    private ImageView imgUpdateResult;
    private boolean isUpdating;
    private TextView tvUpdateWarnming;
    private TextView tvUpdating;
    private UpdateManager updateManager;
    private TextView x8TvUpdateFailureHint;
    private TextView x8sTvUpdatingProgress;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        StatusBarUtil.StatusBarLightMode(this);
    }

    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            this.isUpdating = intent.getBooleanExtra(Constants.UPDATING_KEY, false);
        }
        initView();
        this.updateManager = new UpdateManager(this);
        if (this.isUpdating) {
            this.updateManager.queryCurUpdateStatus(null);
        } else {
            this.updateManager.firmwareBuildPack(UpdateUtil.toFwInfo());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void initView() {
        this.downRoundProgress = (DownRoundProgress) findViewById(R.id.rpb_update_progress);
        this.imgUpdateResult = (ImageView) findViewById(R.id.img_update_result);
        this.tvUpdating = (TextView) findViewById(R.id.tv_updating);
        this.x8TvUpdateFailureHint = (TextView) findViewById(R.id.x8_tv_update_failure_hint);
        this.tvUpdateWarnming = (TextView) findViewById(R.id.tv_update_warnming);
        this.x8sTvUpdatingProgress = (TextView) findViewById(R.id.x8s_tv_updating_progress);
        this.btnReconnect = (Button) findViewById(R.id.btn_reconnect);
        X8FpvManager.isUpdateing = true;
        FontUtil.changeFontLanTing(getAssets(), this.tvUpdateWarnming, this.tvUpdating);
    }

    public void doTrans() {
        this.btnReconnect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8FpvManager.isUpdateing = false;
                X8UpdatingActivity.this.finish();
            }
        });
        this.updateManager.setOnUpdateProgress(new IX8UpdateProgressView() {
            public void showUpdateProgress(boolean isSuccess, int progress, final List<FwInfo> dtos, String name) {
                if (!isSuccess) {
                    X8UpdatingActivity.this.btnReconnect.setVisibility(0);
                    X8UpdatingActivity.this.btnReconnect.setText(R.string.x8_update_connect);
                    X8UpdatingActivity.this.imgUpdateResult.setVisibility(0);
                    X8UpdatingActivity.this.tvUpdateWarnming.setVisibility(4);
                    X8UpdatingActivity.this.tvUpdating.setText(name);
                    X8UpdatingActivity.this.x8sTvUpdatingProgress.setVisibility(8);
                } else if (100 != progress || dtos == null) {
                    X8UpdatingActivity.this.downRoundProgress.setProgress(progress);
                    String tempString = "";
                    if (name == null) {
                        tempString = X8UpdatingActivity.this.getString(R.string.x8_update_hint_one);
                    } else {
                        tempString = String.format(X8UpdatingActivity.this.getString(R.string.x8_updating), new Object[]{name});
                    }
                    X8UpdatingActivity.this.tvUpdating.setText(tempString);
                    X8UpdatingActivity.this.x8sTvUpdatingProgress.setText(progress + "%");
                    if (X8UpdatingActivity.this.btnReconnect.getVisibility() == 0) {
                        X8UpdatingActivity.this.btnReconnect.setVisibility(8);
                        X8UpdatingActivity.this.tvUpdateWarnming.setText(X8UpdatingActivity.this.getString(R.string.x8_update_warning));
                    }
                } else {
                    X8UpdatingActivity.this.btnReconnect.postDelayed(new Runnable() {
                        public void run() {
                            HostConstants.clearLocalFwEntitys();
                            RelayProcess.getRelayProcess().getAllVersion();
                            X8UpdatingActivity.this.btnReconnect.setVisibility(0);
                            X8UpdatingActivity.this.btnReconnect.setText(R.string.x8_update_success);
                            X8UpdatingActivity.this.tvUpdateWarnming.setVisibility(4);
                            X8UpdatingActivity.this.x8sTvUpdatingProgress.setVisibility(8);
                            X8UpdatingActivity.this.updateResult(dtos);
                            X8UpdatingActivity.this.imgUpdateResult.setVisibility(0);
                        }
                    }, 10000);
                }
            }
        });
    }

    private void updateResult(List<FwInfo> dtos) {
        StringBuffer sucessSb = new StringBuffer();
        StringBuffer failedSb = new StringBuffer();
        boolean hasFailed = false;
        for (FwInfo dto : dtos) {
            if ("0".equalsIgnoreCase(dto.getUpdateResult())) {
                sucessSb.append(dto.getSysName() + "、");
                HostConstants.saveLocalFirmware(new LocalFwEntity(dto.getTypeId(), dto.getModelId(), (long) dto.getSoftwareVer(), ""));
            }
            if ("1".equalsIgnoreCase(dto.getUpdateResult())) {
                failedSb.append(dto.getSysName() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + dto.getErrorCode() + "、");
            }
        }
        if (!TextUtils.isEmpty(sucessSb.toString())) {
            sucessSb.deleteCharAt(sucessSb.length() - 1);
            sucessSb.append(getString(R.string.x8_update_success1));
        }
        if (!TextUtils.isEmpty(failedSb.toString())) {
            failedSb.deleteCharAt(failedSb.length() - 1);
            failedSb.append(getString(R.string.x8_update_failed));
        }
        StringBuilder info = new StringBuilder();
        if (!TextUtils.isEmpty(failedSb.toString())) {
            info.append(failedSb.toString() + IOUtils.LINE_SEPARATOR_UNIX);
            hasFailed = true;
        }
        if (!TextUtils.isEmpty(sucessSb.toString())) {
            info.append(sucessSb.toString());
        }
        if (hasFailed) {
            this.imgUpdateResult.setImageResource(R.drawable.x8s_update_error_4);
            this.x8TvUpdateFailureHint.setVisibility(0);
        } else {
            this.imgUpdateResult.setImageResource(R.drawable.x8_img_updating_success);
            this.x8TvUpdateFailureHint.setVisibility(8);
        }
        if (hasFailed) {
            this.tvUpdating.setText(info.toString());
        } else {
            this.tvUpdating.setText(info.toString());
        }
        this.downRoundProgress.setProgress(100);
        EventBus.getDefault().post(new EventMessage(Constants.X8_UPDATE_EVENT_KEY, ""));
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        this.updateManager.removeNoticeList();
        X8FpvManager.isUpdateing = false;
        StateManager.getInstance().getVersionState().setModuleFcAckVersion(null);
    }

    public void onBackPressed() {
        X8FpvManager.isUpdateing = false;
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.x8_activity_updating;
    }
}
