package com.fimi.app.x8s.controls.fcsettting;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8RcItemControllerListener;
import com.fimi.app.x8s.interfaces.IX8RcRockerListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8TabItem;
import com.fimi.app.x8s.widget.X8TabItem.OnSelectListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetRcMode;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8RcItemController extends AbsX8Controllers implements OnClickListener {
    public static final int FIVE_KEY_CENTER = 4;
    public static String[] FIVE_KEY_DATA_ARRAY = null;
    public static final int FIVE_KEY_DOWN = 1;
    public static final int FIVE_KEY_LEFT = 2;
    public static final int FIVE_KEY_RIGHT = 3;
    public static final int FIVE_KEY_UP = 0;
    private Button btnFiveKeyCenter;
    private Button btnFiveKeyDown;
    private Button btnFiveKeyLeft;
    private Button btnFiveKeyRight;
    private Button btnFiveKeyUp;
    private Button btnRcCalibration;
    private Button btnRcCode;
    private Button btnRockerMode;
    int currAPModel = 0;
    private FcCtrlManager fcCtrlManager;
    private IX8RcItemControllerListener listener;
    IX8RcRockerListener rcCtrlModelListener = new IX8RcRockerListener() {
        public void onRcCtrlModelListener(int result) {
            X8RcItemController.this.showRcCtrlModel(result);
        }
    };
    private View rlFcItem;
    private ViewStub stubFcItem;
    private X8TabItem thApModule;

    public X8RcItemController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.stubFcItem = (ViewStub) rootView.findViewById(R.id.stub_rc_item);
        FIVE_KEY_DATA_ARRAY = rootView.getContext().getResources().getStringArray(R.array.x8_five_key_define_option);
    }

    public void showApDialog(final int index) {
        new X8DoubleCustomDialog(this.rootView.getContext(), getString(R.string.x8_rc_setting_ap_dialog_title), getString(R.string.x8_rc_setting_ap_dialog_content), new onDialogButtonClickListener() {
            public void onLeft() {
                X8RcItemController.this.thApModule.setSelect(X8RcItemController.this.currAPModel);
            }

            public void onRight() {
                X8RcItemController.this.fcCtrlManager.setApMode((byte) index, new UiCallBackListener() {
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8RcItemController.this.thApModule.setSelect(index);
                            X8RcItemController.this.currAPModel = index;
                            X8RcItemController.this.fcCtrlManager.setApModeRestart(new UiCallBackListener() {
                                public void onComplete(CmdResult cmdResult, Object o) {
                                    if (cmdResult.isSuccess()) {
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).show();
    }

    public void initActions() {
        if (this.rlFcItem != null) {
            this.thApModule.setOnSelectListener(new OnSelectListener() {
                public void onSelect(int index, String text) {
                    if (X8RcItemController.this.currAPModel != index) {
                        if (index == 0) {
                            index = 1;
                        } else if (index == 1) {
                            index = 0;
                        }
                        X8RcItemController.this.showApDialog(index);
                    }
                }
            });
        }
    }

    public void defaultVal() {
    }

    private void requestRcCtrlModeValue() {
        if (this.fcCtrlManager != null) {
            this.fcCtrlManager.getRcCtrlMode(new UiCallBackListener<AckGetRcMode>() {
                public void onComplete(CmdResult cmdResult, AckGetRcMode obj) {
                    if (obj != null) {
                        X8RcItemController.this.showRcCtrlModel(obj.getMode());
                    }
                }
            });
        }
    }

    public void showRcCtrlModel(int result) {
        switch (result) {
            case 1:
                this.btnRockerMode.setText(R.string.x8_rc_setting_america_rocker);
                return;
            case 2:
                this.btnRockerMode.setText(R.string.x8_rc_setting_japanese_rocker);
                return;
            case 3:
                this.btnRockerMode.setText(R.string.x8_rc_setting_chinese_rocker);
                return;
            default:
                return;
        }
    }

    public void onRcConnected(boolean isConnect) {
    }

    public void showItem() {
        if (this.rlFcItem == null) {
            this.rlFcItem = this.stubFcItem.inflate().findViewById(R.id.x8_rl_main_rc_item);
            this.thApModule = (X8TabItem) this.rlFcItem.findViewById(R.id.th_ap_module);
            this.btnRcCalibration = (Button) this.rlFcItem.findViewById(R.id.btn_rc_calibration);
            this.btnRockerMode = (Button) this.rlFcItem.findViewById(R.id.btn_rocker_mode);
            this.btnRcCode = (Button) this.rlFcItem.findViewById(R.id.btn_rc_code);
            this.btnFiveKeyUp = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_up);
            this.btnFiveKeyDown = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_down);
            this.btnFiveKeyLeft = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_left);
            this.btnFiveKeyRight = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_right);
            this.btnFiveKeyCenter = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_center);
            this.btnRcCalibration.setOnClickListener(this);
            this.btnRockerMode.setOnClickListener(this);
            this.btnRcCode.setOnClickListener(this);
            this.btnFiveKeyUp.setOnClickListener(this);
            this.btnFiveKeyDown.setOnClickListener(this);
            this.btnFiveKeyLeft.setOnClickListener(this);
            this.btnFiveKeyRight.setOnClickListener(this);
            this.btnFiveKeyCenter.setOnClickListener(this);
            this.btnFiveKeyUp.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_UP_KEY)]);
            this.btnFiveKeyDown.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_DOWN_KEY, 1)]);
            this.btnFiveKeyLeft.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_LEFT_KEY, 2)]);
            this.btnFiveKeyRight.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_RIGHT_KEY, 3)]);
            this.btnFiveKeyCenter.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_CENTRE_KEY, 4)]);
            initActions();
        }
        this.rlFcItem.setVisibility(0);
        requestRcCtrlModeValue();
        this.isShow = true;
    }

    public void onDroneConnected(boolean b) {
        float f = 1.0f;
        if (this.isShow) {
            boolean isOpenRockerModer;
            if (this.thApModule != null) {
                boolean canSet;
                float f2;
                if (b && StateManager.getInstance().getX8Drone().isOnGround()) {
                    canSet = true;
                } else {
                    canSet = false;
                }
                X8TabItem x8TabItem = this.thApModule;
                if (canSet) {
                    f2 = 1.0f;
                } else {
                    f2 = 0.4f;
                }
                x8TabItem.setAlpha(f2);
                this.thApModule.setEnabled(canSet);
            }
            if (b && StateManager.getInstance().getX8Drone().isInSky()) {
                isOpenRockerModer = false;
            } else {
                isOpenRockerModer = true;
            }
            if (this.btnRockerMode != null) {
                Button button = this.btnRockerMode;
                if (!isOpenRockerModer) {
                    f = 0.4f;
                }
                button.setAlpha(f);
                this.btnRockerMode.setEnabled(isOpenRockerModer);
            }
            if (StateManager.getInstance().getRelayState().getApModel() == 0) {
                this.currAPModel = 1;
            } else {
                this.currAPModel = 0;
            }
            this.thApModule.setSelect(this.currAPModel);
        }
    }

    private void setViewEnabled(boolean isEnabled) {
        float f;
        float f2 = 1.0f;
        this.btnRockerMode.setEnabled(isEnabled);
        this.btnRcCode.setEnabled(isEnabled);
        this.btnFiveKeyUp.setEnabled(isEnabled);
        this.btnFiveKeyDown.setEnabled(isEnabled);
        this.btnFiveKeyLeft.setEnabled(isEnabled);
        this.btnFiveKeyRight.setEnabled(isEnabled);
        this.btnFiveKeyCenter.setEnabled(isEnabled);
        this.btnRockerMode.setAlpha(isEnabled ? 1.0f : 0.4f);
        Button button = this.btnRcCode;
        if (isEnabled) {
            f = 1.0f;
        } else {
            f = 0.4f;
        }
        button.setAlpha(f);
        button = this.btnFiveKeyUp;
        if (isEnabled) {
            f = 1.0f;
        } else {
            f = 0.4f;
        }
        button.setAlpha(f);
        button = this.btnFiveKeyDown;
        if (isEnabled) {
            f = 1.0f;
        } else {
            f = 0.4f;
        }
        button.setAlpha(f);
        button = this.btnFiveKeyLeft;
        if (isEnabled) {
            f = 1.0f;
        } else {
            f = 0.4f;
        }
        button.setAlpha(f);
        button = this.btnFiveKeyRight;
        if (isEnabled) {
            f = 1.0f;
        } else {
            f = 0.4f;
        }
        button.setAlpha(f);
        Button button2 = this.btnFiveKeyCenter;
        if (!isEnabled) {
            f2 = 0.4f;
        }
        button2.setAlpha(f2);
    }

    public void closeItem() {
        if (this.rlFcItem != null) {
            this.rlFcItem.setVisibility(8);
            this.isShow = false;
        }
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_rocker_mode) {
            if (this.listener != null) {
                this.listener.onRockerModeClicked(this.rcCtrlModelListener);
            }
        } else if (i == R.id.btn_five_key_up) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(0, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_UP_KEY));
            }
        } else if (i == R.id.btn_five_key_down) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(1, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_DOWN_KEY, 1));
            }
        } else if (i == R.id.btn_five_key_left) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(2, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_LEFT_KEY, 2));
            }
        } else if (i == R.id.btn_five_key_right) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(3, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_RIGHT_KEY, 3));
            }
        } else if (i == R.id.btn_five_key_center) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(4, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_CENTRE_KEY, 4));
            }
        } else if (i == R.id.btn_rc_code) {
            if (this.listener != null) {
                this.listener.onRcMatchCode();
                closeUi();
            }
        } else if (i == R.id.btn_rc_calibration && this.listener != null) {
            this.listener.onRcCalibration();
            closeUi();
        }
    }

    public void setListener(IX8RcItemControllerListener listener) {
        this.listener = listener;
    }

    public void setFiveKeyValue(int key, int position) {
        switch (key) {
            case 0:
                this.btnFiveKeyUp.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 1:
                this.btnFiveKeyDown.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 2:
                this.btnFiveKeyLeft.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 3:
                this.btnFiveKeyRight.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 4:
                this.btnFiveKeyCenter.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            default:
                return;
        }
    }

    public boolean onClickBackKey() {
        return false;
    }
}
