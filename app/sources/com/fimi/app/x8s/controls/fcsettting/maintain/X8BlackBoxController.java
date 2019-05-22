package com.fimi.app.x8s.controls.fcsettting.maintain;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.X8B2oxAdapter;
import com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController;
import com.fimi.app.x8s.entity.X8B2oxFile;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.host.HostConstants;
import com.fimi.kernel.fds.FdsCount;
import com.fimi.kernel.fds.FdsManager;
import com.fimi.kernel.fds.IFdsCountListener;
import com.fimi.kernel.fds.IFdsFileModel;
import com.fimi.kernel.fds.IFdsUiListener;
import com.fimi.kernel.utils.DNSLookupThread;
import com.fimi.widget.X8ToastUtil;

public class X8BlackBoxController implements IFdsUiListener, IFdsCountListener {
    private Button btnUploadToggle;
    private View contentView;
    private FdsCount fdsCount = new FdsCount();
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X8BlackBoxController.this.imgDelete.setEnabled(true);
                    X8BlackBoxController.this.btnUploadToggle.setEnabled(true);
                    X8BlackBoxController.this.mX8B2oxAdapter.notifyDataSetChanged();
                    X8BlackBoxController.this.fdsCount.setComplete(msg.arg1);
                    X8BlackBoxController.this.fdsCount.setTotal(msg.arg2);
                    return;
                case 1:
                    X8BlackBoxController.this.tvNoFiles.setVisibility(0);
                    X8BlackBoxController.this.imgDelete.setEnabled(false);
                    X8BlackBoxController.this.btnUploadToggle.setEnabled(false);
                    return;
                case 2:
                    X8BlackBoxController.this.tvNoFiles.setVisibility(0);
                    X8BlackBoxController.this.imgDelete.setEnabled(false);
                    X8BlackBoxController.this.btnUploadToggle.setEnabled(false);
                    X8BlackBoxController.this.mX8B2oxAdapter.clear();
                    return;
                default:
                    return;
            }
        }
    };
    private ImageView imgDelete;
    private ImageView imgReturn;
    private X8ModifyModeController listener;
    private X8B2oxAdapter mX8B2oxAdapter;
    private TextView tvNoFiles;

    public X8BlackBoxController(View rootView, X8ModifyModeController listener) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.listener = listener;
        this.contentView = inflater.inflate(R.layout.x8_black_box_log_layout, (ViewGroup) rootView, true);
        this.mX8B2oxAdapter = new X8B2oxAdapter(rootView.getContext());
        initView(this.contentView);
        initAction();
        seachFile();
    }

    private void initAction() {
        this.imgReturn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FdsManager.getInstance().hasUpload()) {
                    X8BlackBoxController.this.showUploadingEixtDialog();
                } else {
                    X8BlackBoxController.this.listener.onBlackBoxBack();
                }
            }
        });
        this.imgDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!FdsManager.getInstance().hasUpload()) {
                    X8BlackBoxController.this.showDeleteDialog();
                }
            }
        });
        this.btnUploadToggle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (DNSLookupThread.isDSNSuceess()) {
                    switch (X8BlackBoxController.this.fdsCount.getState()) {
                        case 0:
                            if (HostConstants.getUserDetail().getFimiId() == null || HostConstants.getUserDetail().getFimiId().equals("")) {
                                X8ToastUtil.showToast(X8BlackBoxController.this.contentView.getContext(), X8BlackBoxController.this.getString(R.string.x8_fds_login_tip), 0);
                                return;
                            } else {
                                X8BlackBoxController.this.mX8B2oxAdapter.uploadAll();
                                return;
                            }
                        case 1:
                            FdsManager.getInstance().stopAll();
                            X8BlackBoxController.this.mX8B2oxAdapter.notifyDataSetChanged();
                            return;
                        default:
                            return;
                    }
                }
                X8ToastUtil.showToast(X8BlackBoxController.this.contentView.getContext(), X8BlackBoxController.this.getString(R.string.x8_fds_connect_internet), 0);
            }
        });
        FdsManager.getInstance().setFdsCountListener(this);
        FdsManager.getInstance().setUiListener(this);
    }

    private void initView(View contentView) {
        this.imgReturn = (ImageView) contentView.findViewById(R.id.img_return);
        this.imgDelete = (ImageView) contentView.findViewById(R.id.img_delete);
        this.btnUploadToggle = (Button) contentView.findViewById(R.id.btn_upload_toggle);
        this.tvNoFiles = (TextView) contentView.findViewById(R.id.tv_no_files);
        this.imgDelete.setEnabled(false);
        this.btnUploadToggle.setEnabled(false);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.ryv_black_box);
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setAdapter(this.mX8B2oxAdapter);
    }

    public void seachFile() {
        new X8FileSeachDeleteThread(this.mX8B2oxAdapter, this.handler, true).start();
    }

    public void deleteFile() {
        new X8FileSeachDeleteThread(this.mX8B2oxAdapter, this.handler, false).start();
    }

    public void onProgress(IFdsFileModel model, int progrss) {
        this.mX8B2oxAdapter.notifyItemChanged(((X8B2oxFile) model).getItemPostion());
    }

    public void onSuccess(IFdsFileModel responseObj) {
        this.fdsCount.completeIncrease();
        FdsManager.getInstance().remove(responseObj);
        this.mX8B2oxAdapter.notifyItemChanged(((X8B2oxFile) responseObj).getItemPostion());
    }

    public void onFailure(IFdsFileModel responseObj) {
        FdsManager.getInstance().remove(responseObj);
        this.mX8B2oxAdapter.notifyItemChanged(((X8B2oxFile) responseObj).getItemPostion());
    }

    public void onStop(IFdsFileModel reasonObj) {
        FdsManager.getInstance().remove(reasonObj);
        this.mX8B2oxAdapter.notifyItemChanged(((X8B2oxFile) reasonObj).getItemPostion());
    }

    public void showDeleteDialog() {
        new X8DoubleCustomDialog(this.contentView.getContext(), getString(R.string.x8_modify_black_box_delete_title), getString(R.string.x8_modify_black_box_delete_content), getString(R.string.x8_setting_fc_loastaction_tips_content_cancel), getString(R.string.x8_setting_fc_loastaction_tips_content_confirm), new onDialogButtonClickListener() {
            public void onLeft() {
            }

            public void onRight() {
                X8BlackBoxController.this.deleteFile();
            }
        }).show();
    }

    public String getString(int id) {
        return this.contentView.getContext().getString(id);
    }

    public void onUploadingCountChange(int uploading) {
        if (this.fdsCount.getTotal() - this.fdsCount.getComplete() != uploading) {
            this.fdsCount.setState(0);
            this.btnUploadToggle.setText(getString(R.string.x8_modify_black_box_upload_start_all));
        } else if (uploading == 0) {
            this.fdsCount.setState(2);
            this.btnUploadToggle.setText(getString(R.string.x8_modify_black_box_upload_all));
        } else {
            this.fdsCount.setState(1);
            this.btnUploadToggle.setText(getString(R.string.x8_modify_black_box_upload_stop));
        }
        if (uploading != 0) {
            this.imgDelete.setEnabled(false);
        } else {
            this.imgDelete.setEnabled(true);
        }
    }

    public void showUploadingEixtDialog() {
        new X8DoubleCustomDialog(this.contentView.getContext(), getString(R.string.x8_modify_black_box_upload_exit_title), getString(R.string.x8_modify_black_box_upload_exit_content), getString(R.string.x8_setting_fc_loastaction_tips_content_cancel), getString(R.string.x8_setting_fc_loastaction_tips_content_confirm), new onDialogButtonClickListener() {
            public void onLeft() {
            }

            public void onRight() {
                FdsManager.getInstance().stopAll();
                X8BlackBoxController.this.listener.onBlackBoxBack();
            }
        }).show();
    }

    public boolean isRunningTask() {
        if (!FdsManager.getInstance().hasUpload()) {
            return true;
        }
        showUploadingEixtDialog();
        return false;
    }
}
