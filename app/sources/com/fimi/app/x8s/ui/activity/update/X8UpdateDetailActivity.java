package com.fimi.app.x8s.ui.activity.update;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.UpdateContentAdapter;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.x8sdk.controller.X8UpdateCheckManager;
import com.fimi.x8sdk.ivew.IUpdateCheckAction;
import com.fimi.x8sdk.update.UpdateUtil;
import java.util.List;

public class X8UpdateDetailActivity extends BaseActivity implements OnClickListener, IUpdateCheckAction {
    Button btnStartUpdate;
    UpdateContentAdapter contentAdapter;
    ImageView imgLogo;
    ImageView imgUpdateFirmware;
    private boolean isCanUpdate;
    ListView listviewUpdateContent;
    TextView tvReason;
    List<UpfirewareDto> upfirewareDtos;
    ImageView x8IvUpdateDetailReturn;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        StatusBarUtil.StatusBarLightMode(this);
    }

    public void initData() {
        this.imgLogo = (ImageView) findViewById(R.id.img_update_firmware);
        this.imgLogo.setImageResource(R.drawable.x8_update_wait);
        this.tvReason = (TextView) findViewById(R.id.tv_reason);
        this.imgUpdateFirmware = (ImageView) findViewById(R.id.img_update_firmware);
        this.x8IvUpdateDetailReturn = (ImageView) findViewById(R.id.x8_iv_update_detail_return);
        this.upfirewareDtos = UpdateUtil.getUpfireDtos();
        if (this.upfirewareDtos.size() > 0) {
            this.tvReason.setText(R.string.x8_update_ready);
        } else {
            this.tvReason.setText(R.string.x8_update_err_connect);
        }
        this.listviewUpdateContent = (ListView) findViewById(R.id.listview_update_content);
        this.contentAdapter = new UpdateContentAdapter(this.upfirewareDtos, this);
        this.listviewUpdateContent.setAdapter(this.contentAdapter);
        this.btnStartUpdate = (Button) findViewById(R.id.btn_start_update);
        this.btnStartUpdate.setOnClickListener(this);
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        X8UpdateCheckManager.getInstance().setOnIUpdateCheckAction(this, this);
        X8UpdateCheckManager.getInstance().queryCurSystemStatus();
    }

    public void doTrans() {
        this.x8IvUpdateDetailReturn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                X8UpdateDetailActivity.this.finish();
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.x8_activity_update_detail;
    }

    public void onClick(View v) {
        if (v == this.btnStartUpdate) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.UPDATING_KEY, false);
            readyGoThenKill(X8UpdatingActivity.class, bundle);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        X8UpdateCheckManager.getInstance().removeNoticeList();
    }

    public void showIsUpdate(boolean isUpdate, int reason) {
        if (isUpdate) {
            this.isCanUpdate = true;
            this.tvReason.setText(R.string.x8_update_ready);
            this.btnStartUpdate.setEnabled(true);
            this.imgUpdateFirmware.setImageResource(R.drawable.fimisdk_update_wait);
            return;
        }
        this.isCanUpdate = false;
        this.btnStartUpdate.setEnabled(false);
        if (reason == R.string.x8_update_err_insky) {
            this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_6);
        } else if (reason == R.string.x8_update_err_a12ununited) {
            this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_7);
        } else if (reason == R.string.x8_error_code_update_3 || reason == com.fimi.x8sdk.R.string.x8_error_code_update_3) {
            this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_1);
        } else if (reason == com.fimi.x8sdk.R.string.x8_error_code_update_5) {
            this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_2);
        } else if (reason == R.string.x8_update_err_lowpower || reason == R.string.x8_error_code_update_22) {
            this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_5);
        } else if (reason == R.string.x8_update_err_connect) {
            this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_7);
        } else if (reason == R.string.x8_update_err_updating) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.UPDATING_KEY, true);
            readyGoThenKill(X8UpdatingActivity.class, bundle);
        }
        this.tvReason.setText(reason);
    }

    public void checkUpdate() {
    }
}
