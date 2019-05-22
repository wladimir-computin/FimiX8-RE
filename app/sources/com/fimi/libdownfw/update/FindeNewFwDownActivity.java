package com.fimi.libdownfw.update;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fimi.host.Entity.DownloadFwSelectInfo;
import com.fimi.host.HostConstants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.DNSLookupThread;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libdownfw.R;
import com.fimi.libdownfw.ivew.IFirmwareDownView;
import com.fimi.libdownfw.presenter.DownFirmwarePresenter;
import com.fimi.network.DownFwService;
import com.fimi.network.DownFwService.DownState;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.RoundProgressBar;
import com.fimi.x9.upgrade.X9UpdateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindeNewFwDownActivity extends BaseActivity implements IFirmwareDownView {
    Button btnDownAgain;
    ImageView btnReturn;
    private String curFirmware = "";
    long currTime = System.currentTimeMillis();
    List<UpfirewareDto> downDtos = null;
    DownFirmwarePresenter downFirmwarePresenter;
    boolean hasTry;
    ImageView hostDownResult;
    Intent intent;
    private boolean isFirstDown = true;
    private List<DownloadFwSelectInfo> mSelectList;
    RoundProgressBar progressBar;
    private LinearLayout rt_layout;
    TextView tvDowning;
    TextView tvProgress;
    TextView tvTitle;
    TextView tv_downList;
    TextView tv_fail;
    TextView tv_succeuss;

    public void initData() {
        this.downFirmwarePresenter = new DownFirmwarePresenter(this);
        this.tvTitle = (TextView) findViewById(R.id.tv_setting_title);
        this.tvProgress = (TextView) findViewById(R.id.tv_progress);
        this.tvDowning = (TextView) findViewById(R.id.tv_downing);
        this.btnReturn = (ImageView) findViewById(R.id.ibtn_back);
        this.progressBar = (RoundProgressBar) findViewById(R.id.rpb_down_progress);
        this.hostDownResult = (ImageView) findViewById(R.id.host_down_result);
        this.btnDownAgain = (Button) findViewById(R.id.btn_down_again);
        this.rt_layout = (LinearLayout) findViewById(R.id.rt_layout);
        this.tv_fail = (TextView) findViewById(R.id.tv_down_fail);
        this.tv_succeuss = (TextView) findViewById(R.id.tv_down_success);
        this.tv_downList = (TextView) findViewById(R.id.tv_downing_list);
        FontUtil.changeFontLanTing(getAssets(), this.tvTitle, this.tvDowning, this.tvProgress, this.tvProgress, this.btnDownAgain);
        this.mSelectList = (ArrayList) getIntent().getSerializableExtra("listDownloadFwSelectInfo");
        this.currTime = System.currentTimeMillis();
        refreshTvDownFirmwareDetail();
        notifyView();
    }

    private void refreshTvDownFirmwareDetail() {
        if (this.mSelectList != null) {
            this.downDtos = HostConstants.getNeedDownFw(false, this.mSelectList);
        }
        if (this.downDtos != null && this.downDtos.size() > 0) {
            this.tvDowning.setText(String.format(this.mContext.getString(R.string.host_downing_firmware), new Object[]{((UpfirewareDto) this.downDtos.get(0)).getSysName()}));
        }
        this.tv_downList.setText(getUpdateContent());
    }

    private void notifyView() {
        this.rt_layout.setVisibility(8);
        String failString;
        String successString;
        if (DownFwService.getState().equals(DownState.Finish)) {
            this.progressBar.setVisibility(4);
            this.hostDownResult.setImageResource(R.drawable.host_down_update_sucess);
            this.hostDownResult.setVisibility(0);
            this.btnDownAgain.setText(R.string.host_down_fwname_finish);
            this.btnDownAgain.setVisibility(0);
            failString = getdownFail();
            if (!TextUtils.isEmpty(failString)) {
                this.tv_fail.setText(failString + getString(R.string.host_down_fwname_failed));
                this.tv_fail.setVisibility(0);
            }
            successString = getdownSuccess();
            if (!TextUtils.isEmpty(successString)) {
                this.tv_succeuss.setText(successString + getString(R.string.host_down_fwname_success));
                this.tv_succeuss.setVisibility(0);
            }
            this.tvDowning.setVisibility(4);
            this.tv_downList.setVisibility(8);
            this.rt_layout.setVisibility(0);
        } else if (DownFwService.getState().equals(DownState.DownFail)) {
            this.tvDowning.setText(String.format(getResources().getString(R.string.host_downed_firmware), new Object[0]));
            this.progressBar.setProgress(0);
            this.progressBar.setVisibility(4);
            this.hostDownResult.setImageResource(R.drawable.host_down_update_fail);
            this.hostDownResult.setVisibility(0);
            this.btnDownAgain.setVisibility(0);
            this.btnDownAgain.setText(R.string.host_try_down_fwname_again);
            failString = getdownFail();
            if (!TextUtils.isEmpty(failString)) {
                this.tv_fail.setText(failString + getString(R.string.host_down_fwname_failed));
                this.tv_fail.setVisibility(0);
            }
            successString = getdownSuccess();
            if (!TextUtils.isEmpty(successString)) {
                this.tv_succeuss.setText(successString + getString(R.string.host_down_fwname_success));
                this.tv_succeuss.setVisibility(0);
            }
            this.tvDowning.setVisibility(4);
            this.rt_layout.setVisibility(0);
            this.tv_downList.setVisibility(8);
        } else if (DownFwService.getState().equals(DownState.UnStart)) {
            this.progressBar.setVisibility(0);
            this.btnDownAgain.setVisibility(8);
        } else if (DownFwService.getState().equals(DownState.Downing)) {
            this.hostDownResult.setImageResource(R.drawable.icon_firmware_down);
            this.progressBar.setVisibility(0);
            this.hostDownResult.setVisibility(0);
            this.tvDowning.setVisibility(0);
            this.tv_downList.setVisibility(0);
            this.tvProgress.setVisibility(8);
        } else {
            this.tvDowning.setVisibility(4);
            this.progressBar.setVisibility(0);
            this.btnDownAgain.setVisibility(8);
        }
    }

    public String getdownFail() {
        StringBuffer failBuffer = new StringBuffer();
        if (X9UpdateUtil.getDownList() != null && X9UpdateUtil.getDownList().size() > 0) {
            for (UpfirewareDto dto : X9UpdateUtil.getDownList()) {
                if ("1" == dto.getDownResult()) {
                    failBuffer.append(dto.getSysName() + "、");
                }
            }
        }
        if (failBuffer.length() > 0) {
            failBuffer.deleteCharAt(failBuffer.length() - 1);
        }
        return failBuffer.toString();
    }

    public String getdownSuccess() {
        StringBuffer successBuffer = new StringBuffer();
        if (X9UpdateUtil.getDownList() != null && X9UpdateUtil.getDownList().size() > 0) {
            for (UpfirewareDto dto : X9UpdateUtil.getDownList()) {
                if ("0" == dto.getDownResult()) {
                    successBuffer.append(dto.getSysName() + "、");
                }
            }
        }
        if (successBuffer.length() > 0) {
            successBuffer.deleteCharAt(successBuffer.length() - 1);
        }
        return successBuffer.toString();
    }

    public void doTrans() {
        this.btnReturn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FindeNewFwDownActivity.this.finish();
            }
        });
        this.btnDownAgain.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DownFwService.getState().equals(DownState.Finish)) {
                    FindeNewFwDownActivity.this.finish();
                } else if (DownFwService.getState().equals(DownState.Downing)) {
                    DownFwService.setState(DownState.StopDown);
                    FindeNewFwDownActivity.this.finish();
                } else if (!FindeNewFwDownActivity.this.isDSNSuceess()) {
                    ToastUtil.showToast(FindeNewFwDownActivity.this, FindeNewFwDownActivity.this.getString(R.string.host_down_net_exception), 0);
                } else if (System.currentTimeMillis() - FindeNewFwDownActivity.this.currTime >= 1000 || FindeNewFwDownActivity.this.isFirstDown) {
                    FindeNewFwDownActivity.this.currTime = System.currentTimeMillis();
                    FindeNewFwDownActivity.this.isFirstDown = false;
                    FindeNewFwDownActivity.this.hasTry = true;
                    FindeNewFwDownActivity.this.tvDowning.setVisibility(0);
                    DownFwService.checkingTaskCount = 0;
                    FindeNewFwDownActivity.this.intent = new Intent(FindeNewFwDownActivity.this, DownFwService.class);
                    FindeNewFwDownActivity.this.intent.putExtra("listDownloadFwSelectInfo", (Serializable) FindeNewFwDownActivity.this.mSelectList);
                    FindeNewFwDownActivity.this.startService(FindeNewFwDownActivity.this.intent);
                    FindeNewFwDownActivity.this.refreshTvDownFirmwareDetail();
                    FindeNewFwDownActivity.this.tv_downList.setVisibility(0);
                    FindeNewFwDownActivity.this.tvProgress.setVisibility(8);
                    FindeNewFwDownActivity.this.progressBar.setVisibility(0);
                    FindeNewFwDownActivity.this.tv_fail.setText("");
                    FindeNewFwDownActivity.this.tv_fail.setVisibility(8);
                    FindeNewFwDownActivity.this.tv_succeuss.setVisibility(8);
                }
            }
        });
        if (DownFwService.getState().equals(DownState.UnStart)) {
            DownFwService.checkingTaskCount = 0;
            this.intent = new Intent(this, DownFwService.class);
            this.intent.putExtra("listDownloadFwSelectInfo", (Serializable) this.mSelectList);
            startService(this.intent);
        }
    }

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_find_new_fw;
    }

    public void showDownFwProgress(DownState isResult, int progress, String name) {
        this.curFirmware = name;
        if (isResult == DownState.Downing) {
            DownFwService.setState(DownState.Downing);
            if (this.hasTry) {
                this.hasTry = false;
            }
            this.progressBar.setProgress(progress);
            if (progress == 100) {
                stopService();
                DownFwService.setState(DownState.Finish);
            } else {
                this.tvDowning.setText(String.format(this.mContext.getString(R.string.host_downing_firmware), new Object[]{this.curFirmware}) + " " + progress + "%");
                this.tvDowning.setVisibility(0);
                if (HostConstants.isForceUpdate(X9UpdateUtil.getDownList())) {
                    this.btnDownAgain.setVisibility(4);
                } else {
                    this.btnDownAgain.setText(R.string.host_try_down_fwname_stop);
                    this.btnDownAgain.setVisibility(0);
                }
            }
        } else if (isResult == DownState.DownFail) {
            DownFwService.setState(DownState.DownFail);
            stopService();
        } else if (isResult == DownState.StopDown) {
            DownFwService.setState(DownState.StopDown);
            this.tvDowning.setText(String.format(this.mContext.getString(R.string.host_downing_firmware), new Object[]{this.curFirmware}) + String.valueOf(progress) + "%");
        }
        notifyView();
    }

    public String getUpdateContent() {
        StringBuffer sb = new StringBuffer();
        if (this.downDtos != null && this.downDtos.size() > 0) {
            for (UpfirewareDto dto : this.downDtos) {
                sb.append(dto.getSysName() + "、");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void stopService() {
        if (this.intent != null) {
            stopService(this.intent);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        this.downFirmwarePresenter.removerDownNoticeLisnter();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        finish();
        return true;
    }

    public boolean isDSNSuceess() {
        DNSLookupThread dnsTh = new DNSLookupThread("www.baidu.com");
        dnsTh.start();
        try {
            dnsTh.join(500);
        } catch (Exception e) {
        }
        if (dnsTh.getIP() != null) {
            return true;
        }
        return false;
    }
}
