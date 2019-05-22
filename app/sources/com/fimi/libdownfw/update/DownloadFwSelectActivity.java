package com.fimi.libdownfw.update;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.fimi.host.Entity.DownloadFwSelectInfo;
import com.fimi.host.HostConstants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.DNSLookupThread;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.kernel.utils.SystemParamUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libdownfw.R;
import com.fimi.libdownfw.adapter.DownloadFwSelectAdapter;
import com.fimi.libdownfw.adapter.DownloadFwSelectAdapter.SelectListener;
import com.fimi.network.DownFwService;
import com.fimi.network.DownFwService.DownState;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.DialogManager;
import com.fimi.widget.DialogManager.OnDialogListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DownloadFwSelectActivity extends BaseActivity implements SelectListener {
    private Button btnDown;
    private ImageView btnReturn;
    long currTime = System.currentTimeMillis();
    private List<DownloadFwSelectInfo> infoList = new ArrayList();
    private boolean isFirstDown = true;
    private ListView lvSelectFw;
    private List<UpfirewareDto> mUpfirewareDtoList = new ArrayList();
    private DownloadFwSelectAdapter selectAdapter;
    private TextView tvHardWareSize;
    private View tv_title;
    private TextView tv_title2;

    public void initData() {
        this.btnReturn = (ImageView) findViewById(R.id.iv_return);
        this.btnDown = (Button) findViewById(R.id.btn_down);
        this.lvSelectFw = (ListView) findViewById(R.id.lv_select_fw);
        this.tvHardWareSize = (TextView) findViewById(R.id.tv_hardSize);
        this.tv_title2 = (TextView) findViewById(R.id.tv_title2);
        this.tv_title = findViewById(R.id.tv_title);
        initValue();
        this.selectAdapter = new DownloadFwSelectAdapter(this, this.infoList);
        this.selectAdapter.setSelectListener(this);
        this.lvSelectFw.setAdapter(this.selectAdapter);
        this.currTime = System.currentTimeMillis();
        FontUtil.changeFontLanTing(getAssets(), this.tvHardWareSize, this.tv_title2, this.tv_title);
    }

    public void doTrans() {
        this.btnReturn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DownloadFwSelectActivity.this.finish();
            }
        });
        this.btnDown.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (System.currentTimeMillis() - DownloadFwSelectActivity.this.currTime >= 1000 || DownloadFwSelectActivity.this.isFirstDown) {
                    DownloadFwSelectActivity.this.currTime = System.currentTimeMillis();
                    DownloadFwSelectActivity.this.isFirstDown = false;
                    DownFwService.setState(DownState.UnStart);
                    if (!DownloadFwSelectActivity.this.isDSNSuceess()) {
                        ToastUtil.showToast(DownloadFwSelectActivity.this, DownloadFwSelectActivity.this.getString(R.string.host_down_net_exception), 0);
                    } else if (SystemParamUtil.isWifiNetwork(DownloadFwSelectActivity.this)) {
                        DownloadFwSelectActivity.this.startDownLoad();
                    } else {
                        DialogManager dialogManager = new DialogManager(DownloadFwSelectActivity.this, DownloadFwSelectActivity.this.getString(R.string.host_down_tip), DownloadFwSelectActivity.this.getString(R.string.host_down_firmware_warning), DownloadFwSelectActivity.this.getString(R.string.host_down_continue), DownloadFwSelectActivity.this.getString(R.string.host_down_cancel));
                        dialogManager.setVerticalScreen(true);
                        dialogManager.setOnDiaLogListener(new OnDialogListener() {
                            public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                                DownloadFwSelectActivity.this.startDownLoad();
                            }

                            public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialogManager.showDialog();
                    }
                }
            }
        });
        this.lvSelectFw.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            }
        });
    }

    private void startDownLoad() {
        Intent intent = new Intent(this, FindeNewFwDownActivity.class);
        List<DownloadFwSelectInfo> mSelectList = new ArrayList();
        for (DownloadFwSelectInfo info : this.infoList) {
            if (info.isSelect()) {
                mSelectList.add(info);
            }
        }
        if (mSelectList.size() > 0) {
            HostConstants.getNeedDownFw(false, mSelectList);
            intent.putExtra("listDownloadFwSelectInfo", (Serializable) mSelectList);
            startActivity(intent);
            finish();
        }
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_download_fw_select;
    }

    public void initValue() {
        for (DownloadFwSelectInfo info : HostConstants.getDownloadFwSelectInfoList()) {
            if (info.hasData()) {
                switch (info.getProduct()) {
                    case GH2:
                        this.tv_title2.setText(getString(R.string.downfw_device_hand_gimbal));
                        break;
                    case X9:
                        this.tv_title2.setText(getString(R.string.downfw_device_x9));
                        break;
                    case X8S:
                        break;
                    case FIMIAPP:
                        this.tv_title2.setText(getString(R.string.downfw_device_fimiapp));
                        break;
                }
                this.tv_title2.setText(getString(R.string.downfw_device_fimiapp));
                this.infoList.add(info);
                continue;
            }
        }
    }

    public void onSelect(boolean b) {
        if (b) {
            this.btnDown.setEnabled(true);
            List<UpfirewareDto> mList = HostConstants.getNeedDownFw();
            if (mList != null && mList.size() > 0) {
                long totalSize = 0;
                for (UpfirewareDto info : mList) {
                    if (!HostConstants.iteratorProductSelectList(info, this.infoList)) {
                        mList.remove(info);
                    }
                }
                for (int m = 0; m < mList.size(); m++) {
                    totalSize += ((UpfirewareDto) mList.get(m)).getFileSize();
                    if (totalSize > 0) {
                        String tempString = String.valueOf(NumberUtil.decimalPointStr(((((double) totalSize) * 1.0d) / 1024.0d) / 1024.0d, 2)) + "M";
                        this.tvHardWareSize.setText(String.format(getString(R.string.downfw_update_firmware_detail), new Object[]{tempString}));
                        this.tvHardWareSize.setVisibility(0);
                    }
                }
                return;
            }
            return;
        }
        this.btnDown.setEnabled(false);
        this.tvHardWareSize.setVisibility(8);
    }

    public void onBackPressed() {
        super.onBackPressed();
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

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }
}
