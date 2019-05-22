package com.fimi.app.x8s.controls;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.view.KeyEvent;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.ui.activity.update.X8UpdateDetailActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.host.HostConstants;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.controller.X8UpdateCheckManager;
import com.fimi.x8sdk.ivew.IUpdateCheckAction;
import com.fimi.x8sdk.update.UpdateUtil;

public class X8UpdateHintController {
    private Activity context;
    private X8DoubleCustomDialog dialogManagerUpdate;
    private IUpdateCheckAction iUpdateCheckAction = new IUpdateCheckAction() {
        public void showIsUpdate(boolean isUpdate, int reason) {
        }

        public void checkUpdate() {
            X8UpdateHintController.this.showUpdateDialog();
        }
    };

    public X8UpdateHintController(Activity context) {
        this.context = context;
    }

    public void showUpdateDialog() {
        if (UpdateUtil.getUpfireDtos().size() > 0) {
            SPStoreManager.getInstance().saveInt(HostConstants.SP_KEY_UPDATE_CHECK, 1);
            if (UpdateUtil.isForceUpdate()) {
                this.dialogManagerUpdate = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_update_fw_title), this.context.getString(R.string.x8_update_tip), this.context.getString(R.string.fimi_sdk_update_now), this.context.getString(R.string.fimi_sdk_update_return), new onDialogButtonClickListener() {
                    public void onLeft() {
                        X8UpdateHintController.this.dialogManagerUpdate.dismiss();
                        X8UpdateHintController.this.context.startActivity(new Intent(X8UpdateHintController.this.context, X8UpdateDetailActivity.class));
                    }

                    public void onRight() {
                        X8UpdateHintController.this.context.finish();
                    }
                });
                this.dialogManagerUpdate.setCanceledOnTouchOutside(false);
                this.dialogManagerUpdate.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode != 4 || event.getRepeatCount() == 0) {
                        }
                        return true;
                    }
                });
                if (!this.context.isFinishing()) {
                    this.dialogManagerUpdate.show();
                    return;
                }
                return;
            }
            this.dialogManagerUpdate = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_update_fw_title), this.context.getString(R.string.x8_update_tip), this.context.getString(R.string.fimi_sdk_update_ignore), this.context.getString(R.string.fimi_sdk_update_now), new onDialogButtonClickListener() {
                public void onLeft() {
                    X8UpdateHintController.this.dialogManagerUpdate.dismiss();
                }

                public void onRight() {
                    X8UpdateHintController.this.dialogManagerUpdate.dismiss();
                    X8UpdateHintController.this.context.startActivity(new Intent(X8UpdateHintController.this.context, X8UpdateDetailActivity.class));
                }
            });
            this.dialogManagerUpdate.setCanceledOnTouchOutside(false);
            if (!this.context.isFinishing()) {
                this.dialogManagerUpdate.show();
            }
        }
    }

    public void queryCurSystemStatus() {
        X8UpdateCheckManager.getInstance().setOnIUpdateCheckAction(this.context, this.iUpdateCheckAction);
        X8UpdateCheckManager.getInstance().queryCurSystemStatus();
    }

    public void setPresenterLockMotor(int lock) {
        X8UpdateCheckManager.getInstance().setPresenterLockMotor(lock);
    }
}
