package com.fimi.apk.presenter;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.fimi.apk.iview.IApkVerisonView;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.SystemParamUtil;
import com.fimi.network.ApkVersionManager;
import com.fimi.network.entity.ApkVersionDto;
import com.fimi.network.entity.NetModel;
import java.net.URL;

public class ApkVersionPrenster {
    private Context context;
    private IApkVerisonView mApkVerisonView;
    private ApkVersionManager mApkVersionManager = new ApkVersionManager();
    private onApkUpdateListerner mOnApkUpdateListerner;
    private onShowDialogListerner mOnShowDialogListerner;

    public interface onApkUpdateListerner {
        void haveUpdate(boolean z);
    }

    public interface onShowDialogListerner {
        void showDialog(ApkVersionDto apkVersionDto, String str);
    }

    public ApkVersionPrenster(Context context, IApkVerisonView mApkVerisonView) {
        this.context = context;
        this.mApkVerisonView = mApkVerisonView;
    }

    public void getOnlineNewApkFileInfo() {
        String packageName = SystemParamUtil.getPackageName();
        final String savePath = DirectoryPath.getApkPath() + "/" + packageName.substring(packageName.lastIndexOf(".") + 1);
        this.mApkVersionManager.getOnlineNewApkFileInfo(packageName, new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                try {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        if (netModel.getData() != null) {
                            ApkVersionDto dto = (ApkVersionDto) JSON.parseObject(netModel.getData().toString(), ApkVersionDto.class);
                            URL urlObj = new URL(dto.getUrl());
                            ApkVersionPrenster.this.compareApkVersion(dto, savePath);
                        }
                    } else if (ApkVersionPrenster.this.mOnApkUpdateListerner != null) {
                        ApkVersionPrenster.this.mOnApkUpdateListerner.haveUpdate(false);
                    }
                } catch (Exception e) {
                    if (ApkVersionPrenster.this.mOnApkUpdateListerner != null) {
                        ApkVersionPrenster.this.mOnApkUpdateListerner.haveUpdate(false);
                    }
                }
            }

            public void onFailure(Object reasonObj) {
                if (ApkVersionPrenster.this.mOnApkUpdateListerner != null) {
                    ApkVersionPrenster.this.mOnApkUpdateListerner.haveUpdate(false);
                }
            }
        }));
    }

    public void compareApkVersion(ApkVersionDto dto, String savePath) {
        if (Integer.parseInt(dto.getNewVersion()) > SystemParamUtil.getVersionCode()) {
            if (this.mOnApkUpdateListerner != null) {
                this.mOnApkUpdateListerner.haveUpdate(true);
            }
            if (this.mOnShowDialogListerner != null) {
                this.mOnShowDialogListerner.showDialog(dto, savePath);
            }
        } else if (this.mOnApkUpdateListerner != null) {
            this.mOnApkUpdateListerner.haveUpdate(false);
        }
    }

    public void showDialog(ApkVersionDto dto, String savePath) {
        this.mApkVerisonView.showNewApkVersionDialog(dto, savePath);
    }

    public void setOnApkUpdateListerner(onApkUpdateListerner onApkUpdateListerner) {
        this.mOnApkUpdateListerner = onApkUpdateListerner;
    }

    public void setOnShowDialogListerner(onShowDialogListerner onShowDialogListerner) {
        this.mOnShowDialogListerner = onShowDialogListerner;
    }
}
