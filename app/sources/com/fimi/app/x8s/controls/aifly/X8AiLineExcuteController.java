package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.TcpClient;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.X8AiTrackController.OnAiTrackControllerListener;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiLinesExcuteConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiLinesPointValueModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8BaseModule;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLineInterestPointController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLineInterestPointController.OnInterestTouchUp;
import com.fimi.app.x8s.entity.X8AilinePrameter;
import com.fimi.app.x8s.enums.X8AiLineState;
import com.fimi.app.x8s.enums.X8AiMapItem;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiItemMapListener;
import com.fimi.app.x8s.interfaces.IX8AiLineExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8AiLineHistoryActivity;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.app.x8s.widget.X8SingleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckGetAiLinePoint;
import com.fimi.x8sdk.dataparser.AckGetAiLinePointsAction;
import com.fimi.x8sdk.map.MapType;
import com.fimi.x8sdk.modulestate.StateManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class X8AiLineExcuteController extends AbsX8AiController implements OnClickListener, IX8MarkerListener, onDialogButtonClickListener, OnAiTrackControllerListener {
    protected int MAX_WIDTH;
    private X8sMainActivity activity;
    private View blank;
    private int count;
    private int countAction;
    private X8DoubleCustomDialog deleteDialoag;
    private X8DoubleCustomDialog dialog;
    private FcManager fcManager;
    private View flagSmall;
    int i = 0;
    private ImageView imgAdd;
    private ImageView imgBack;
    private ImageView imgDelete;
    private ImageView imgEdit;
    private ImageView imgHistory;
    private ImageView imgNext;
    private ImageView imgVcToggle;
    private boolean isDraw;
    protected boolean isNextShow;
    protected boolean isShow;
    private long lineId;
    private X8AiLinePointInfo lineInfo;
    private IX8AiLineExcuteControllerListener listener;
    private AiLineGetPointState mAiLineGetPointState = AiLineGetPointState.IDLE;
    private X8BaseModule mCurrentModule;
    private IX8NextViewListener mIX8NextViewListener = new IX8NextViewListener() {
        public void onBackClick() {
            X8AiLineExcuteController.this.closeNextUiFromNext(true);
        }

        public void onExcuteClick() {
            X8AiLineExcuteController.this.closeNextUi(false);
            X8AiLineExcuteController.this.imgEdit.setVisibility(8);
            X8AiLineExcuteController.this.imgDelete.setVisibility(8);
            X8AiLineExcuteController.this.rlAdd.setVisibility(8);
            X8AiLineExcuteController.this.mTipBgView.setVisibility(8);
            X8AiLineExcuteController.this.mX8LineState = X8AiLineState.RUNNING;
            X8AiLineExcuteController.this.imgHistory.setVisibility(8);
            if (X8AiLineExcuteController.this.mX8AilinePrameter.getOrientation() == 0) {
                X8AiLineExcuteController.this.openVcToggle();
            }
            X8AiLineExcuteController.this.mX8AiLineInterestPointController.showView(false);
            X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().startAiLineProcess();
            X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().removeInterstPointByRunning();
        }

        public void onSaveClick() {
            X8AiLineExcuteController.this.closeNextUiFromNext(true);
            X8AiLineExcuteController.this.closeAiLine();
        }
    };
    private List<AckGetAiLinePoint> mInterestList = new ArrayList();
    private List<AckGetAiLinePoint> mList = new ArrayList();
    private List<AckGetAiLinePointsAction> mListAtions = new ArrayList();
    private X8AiTipWithCloseView mTipBgView;
    private X8AiLineInterestPointController mX8AiLineInterestPointController;
    private X8AiLinesExcuteConfirmModule mX8AiLinesExcuteConfirmModule;
    private X8AiLinesPointValueModule mX8AiLinesPointValueModule;
    private X8AilinePrameter mX8AilinePrameter = new X8AilinePrameter();
    private X8AiLineState mX8LineState = X8AiLineState.IDLE;
    private int mode;
    private LineModel model = LineModel.VEDIO;
    private View nextRootView;
    private View rlAdd;
    private int timeSend = 0;
    private int totalPoint;
    private TextView tvActionTip;
    private TextView tvAdd;
    private TextView tvFlag;
    private TextView tvP2PTip;
    protected int width = X8Application.ANIMATION_WIDTH;

    private enum AiLineGetPointState {
        IDLE,
        FIRST,
        ALL,
        ALL_VALUE,
        END
    }

    public enum LineModel {
        MAP,
        VEDIO,
        HISTORY
    }

    public void onLeft() {
    }

    public void onRight() {
        if (this.mX8LineState == X8AiLineState.RUNNING || this.mX8LineState == X8AiLineState.RUNNING2) {
            lineTaskExite();
        }
    }

    public void onChangeMarkerAltitude(float altitude) {
        this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setMarkerViewInfo(altitude);
    }

    public void onMarkerSelect(boolean onSelect, float altitude, MapPointLatLng mpl, boolean isClick) {
        if (onSelect && !isClick) {
            openPointValue(mpl);
        }
    }

    public void onMarkerSizeChange(int size) {
        if (size == 1) {
            this.imgNext.setEnabled(false);
        } else if (size >= 2) {
            this.imgNext.setEnabled(true);
        }
        if (this.model == LineModel.VEDIO) {
            this.rlAdd.setVisibility(0);
            if (size == 0) {
                this.tvAdd.setText("");
                this.imgAdd.setBackgroundResource(R.drawable.x8_img_ai_line_add_selector);
            } else {
                this.tvAdd.setText("" + size);
                this.imgAdd.setBackgroundResource(R.drawable.x8_img_ai_line_add_size);
            }
        }
        if (this.mode != 1) {
            return;
        }
        if (size == 0) {
            this.mTipBgView.setTipText("" + getString(R.string.x8_ai_fly_lines_vedio_select_tip));
        } else if (size == 1) {
            this.mTipBgView.setTipText("" + getString(R.string.x8_ai_fly_lines_vedio_select_tip1));
        } else {
            this.mTipBgView.setTipText("" + getString(R.string.x8_ai_fly_lines_vedio_select_tip2));
        }
    }

    public X8AiLineInterestPointController getmX8AiLineInterestPointController() {
        return this.mX8AiLineInterestPointController;
    }

    public void onInterestSizeEnable(boolean b) {
        this.mX8AiLineInterestPointController.setInterestEnable(b);
    }

    public Rect getDeletePosition() {
        Rect viewRect = new Rect();
        int[] location = new int[2];
        this.imgDelete.getLocationOnScreen(location);
        viewRect.left = location[0];
        viewRect.top = location[1];
        viewRect.right = location[0] + this.imgDelete.getWidth();
        viewRect.bottom = location[1] + this.imgDelete.getHeight();
        return viewRect;
    }

    public void onRunIndex(int index, int action) {
        String msg = "";
        switch (action) {
            case 0:
                msg = getString(R.string.x8_ai_fly_lines_action_na);
                break;
            case 1:
                msg = getString(R.string.x8_ai_fly_lines_action_hover);
                break;
            case 2:
                msg = getString(R.string.x8_ai_fly_lines_action_record);
                break;
            case 4:
                msg = getString(R.string.x8_ai_fly_lines_action_one_photo);
                break;
            case 5:
                msg = getString(R.string.x8_ai_fly_lines_action_5s_photo);
                break;
            case 6:
                msg = getString(R.string.x8_ai_fly_lines_action_three_photo);
                break;
        }
        String s = String.format(getString(R.string.x8_ai_fly_point_to_point_action), new Object[]{Integer.valueOf(index)}) + msg;
        this.tvActionTip.setVisibility(0);
        this.tvActionTip.setText(s);
    }

    public int getOration() {
        return this.mX8AilinePrameter.getOrientation();
    }

    public X8AiLineExcuteController(X8sMainActivity activity, View rootView, X8AiLineState mX8LineState, int mode, long lineId) {
        super(rootView);
        this.activity = activity;
        this.mX8LineState = mX8LineState;
        this.mode = mode;
        this.lineId = lineId;
    }

    public void setListener(IX8AiLineExcuteControllerListener listener) {
        this.listener = listener;
    }

    public void setModel(LineModel model) {
        this.model = model;
    }

    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    public void addInreterstMarker() {
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public boolean onClickBackKey() {
        return false;
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fly_route), this.rootView.getContext().getString(R.string.x8_ai_fly_route_exit), this);
        }
        this.dialog.show();
    }

    public void showNoSaveExit() {
        new X8DoubleCustomDialog(this.rootView.getContext(), getString(R.string.x8_ai_fly_point_no_save_exit_title), getString(R.string.x8_ai_fly_point_no_save_exit), new onDialogButtonClickListener() {
            public void onLeft() {
            }

            public void onRight() {
                X8AiLineExcuteController.this.closeAiLine();
            }
        }).show();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.mX8LineState == X8AiLineState.RUNNING || this.mX8LineState == X8AiLineState.RUNNING2) {
                showExitDialog();
            } else if (this.activity.getmMapVideoController().getFimiMap().getAiLineManager().getMapPointList().size() > 0) {
                showNoSaveExit();
            } else {
                closeAiLine();
            }
        } else if (id == R.id.img_ai_line_history) {
            goHistoryActivity();
        } else if (id == R.id.x8_main_ai_line_next_blank) {
            closeNextUiFromNext(true);
        } else if (id == R.id.img_ai_line_edit) {
            openPointValue(this.activity.getmMapVideoController().getFimiMap().getAiLineManager().getMapPointLatLng());
        } else if (id == R.id.rl_ai_line_add) {
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setAiLineMark(StateManager.getInstance().getX8Drone().getLatitude(), StateManager.getInstance().getX8Drone().getLongitude(), StateManager.getInstance().getX8Drone().getHeight(), StateManager.getInstance().getX8Drone().getDeviceAngle());
            this.rlAdd.setVisibility(0);
        } else if (id == R.id.img_ai_line_delete) {
            if (this.model == LineModel.VEDIO) {
                if (this.activity.getmMapVideoController().getFimiMap().getAiLineManager().getMapPointList().size() > 0) {
                    showDelteDialog();
                }
            } else if (this.model == LineModel.MAP) {
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().deleteMarker(true);
            }
        } else if (id == R.id.img_ai_follow_next) {
            openNextUi();
            this.mX8AiLinesExcuteConfirmModule.setPointSizeAndDistance(this.activity.getmMapVideoController().getFimiMap().getAiLineManager().getAiLinePointSize(), this.activity.getmMapVideoController().getFimiMap().getAiLineManager().getAiLineDistance(), this.activity.getmMapVideoController().getFimiMap().getAiLineManager().getMapPointList(), this.model);
            this.mX8AiLinesExcuteConfirmModule.setDataByHistory(this.lineInfo);
        } else if (id == R.id.img_vc_targgle) {
            if (this.imgVcToggle.isSelected()) {
                setAiVcClose();
            } else {
                setAiVcOpen();
            }
        } else if (id != R.id.rl_flag_small) {
        } else {
            if (this.tvP2PTip.getVisibility() == 0) {
                this.tvP2PTip.setVisibility(8);
            } else {
                this.tvP2PTip.setVisibility(0);
            }
        }
    }

    public void showDelteDialog() {
        if (this.deleteDialoag == null) {
            this.deleteDialoag = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_ai_fly_lines_delete_title), this.rootView.getContext().getString(R.string.x8_ai_fly_lines_delete_content), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().deleteMarker(false);
                }
            });
        }
        this.deleteDialoag.show();
    }

    public void changeModelView() {
        if (this.model == LineModel.MAP) {
            if (this.lineId != 0) {
                this.mTipBgView.setVisibility(8);
                this.rlAdd.setVisibility(8);
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMapClickValid(false);
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMarkerClickValid(false);
                this.imgDelete.setVisibility(8);
                this.imgAdd.setVisibility(8);
                this.imgEdit.setVisibility(8);
                this.mX8AiLineInterestPointController.showView(false);
                this.imgNext.setEnabled(true);
                return;
            }
            this.rlAdd.setVisibility(8);
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMapClickValid(true);
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMarkerClickValid(true);
            this.mX8AiLineInterestPointController.showView(true);
        } else if (this.model == LineModel.VEDIO) {
            this.rlAdd.setVisibility(0);
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMapClickValid(false);
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMarkerClickValid(true);
            this.imgDelete.setVisibility(0);
            this.imgAdd.setVisibility(0);
            this.imgEdit.setVisibility(8);
            this.mX8AiLineInterestPointController.showView(false);
        } else {
            if (this.model == LineModel.HISTORY) {
            }
        }
    }

    public void openUi() {
        this.isShow = true;
        this.handleView = LayoutInflater.from(this.rootView.getContext()).inflate(R.layout.x8_ai_line_layout, (ViewGroup) this.rootView, true);
        initView2(this.handleView);
        initActions2();
        super.openUi();
    }

    public void initView2(View view) {
        this.imgBack = (ImageView) view.findViewById(R.id.img_ai_follow_back);
        this.tvP2PTip = (TextView) view.findViewById(R.id.img_ai_p2p_tip);
        this.mTipBgView = (X8AiTipWithCloseView) view.findViewById(R.id.v_content_tip);
        this.imgNext = (ImageView) view.findViewById(R.id.img_ai_follow_next);
        this.imgHistory = (ImageView) view.findViewById(R.id.img_ai_line_history);
        this.imgEdit = (ImageView) view.findViewById(R.id.img_ai_line_edit);
        this.imgAdd = (ImageView) view.findViewById(R.id.img_ai_line_add);
        this.rlAdd = view.findViewById(R.id.rl_ai_line_add);
        this.tvAdd = (TextView) view.findViewById(R.id.tv_ai_line_index);
        this.tvActionTip = (TextView) view.findViewById(R.id.rl_action_tip1);
        this.imgDelete = (ImageView) view.findViewById(R.id.img_ai_line_delete);
        this.imgVcToggle = (ImageView) view.findViewById(R.id.img_vc_targgle);
        this.imgVcToggle.setOnClickListener(this);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.tvFlag = (TextView) this.handleView.findViewById(R.id.tv_task_tip);
        this.flagSmall.setOnClickListener(this);
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_line_point_value_content);
        this.blank = this.rootView.findViewById(R.id.x8_main_ai_line_next_blank);
        this.mX8AiLinesExcuteConfirmModule = new X8AiLinesExcuteConfirmModule();
        this.mX8AiLinesPointValueModule = new X8AiLinesPointValueModule();
        this.mX8AiLineInterestPointController = new X8AiLineInterestPointController((RelativeLayout) view.findViewById(R.id.rl_x8_ai_surround), (ImageView) view.findViewById(R.id.img_interest_point), (TextView) view.findViewById(R.id.tv_tip));
        this.mX8AiLineInterestPointController.setListener(new OnInterestTouchUp() {
            public void onUp(int x, int y) {
                X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().addInreterstMarker(x, y);
            }
        });
        initViewVisiableByStateAndMode(view);
    }

    public void initViewVisiableByStateAndMode(View view) {
        this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setLineMarkerSelectListener(this);
        if (this.mX8LineState == X8AiLineState.IDLE) {
            if (this.mode == 0) {
                this.mX8AilinePrameter.setOrientation(0);
                this.imgNext.setVisibility(0);
                this.imgDelete.setVisibility(0);
                this.imgEdit.setVisibility(0);
                this.mTipBgView.setVisibility(0);
                this.mTipBgView.setTipText(getString(R.string.x8_ai_fly_lines_map_select_tip));
                this.mTipBgView.showTip();
                this.imgNext.setEnabled(false);
                this.rlAdd.setVisibility(8);
                view.findViewById(R.id.img_interest_point).setVisibility(0);
                view.findViewById(R.id.tv_tip).setVisibility(0);
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMapClickListener();
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMapClickValid(true);
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMarkerClickValid(true);
            } else if (this.mode == 1) {
                this.mX8AilinePrameter.setOrientation(1);
                this.imgNext.setVisibility(0);
                this.imgDelete.setVisibility(0);
                this.mTipBgView.setVisibility(0);
                this.rlAdd.setVisibility(0);
                this.mTipBgView.setTipText(getString(R.string.x8_ai_fly_lines_vedio_select_tip));
                this.mTipBgView.showTip();
                this.imgNext.setEnabled(false);
                this.imgEdit.setVisibility(8);
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMapClickListener();
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMapClickValid(false);
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setOnMarkerClickValid(true);
            } else if (this.mode == 2) {
                this.imgNext.setVisibility(0);
                this.rlAdd.setVisibility(8);
                this.imgDelete.setVisibility(8);
                this.imgEdit.setVisibility(8);
                this.mTipBgView.setVisibility(8);
                this.imgVcToggle.setVisibility(8);
                this.imgHistory.setVisibility(0);
                historyUirendering();
            }
            this.isDraw = true;
            setMode();
            return;
        }
        this.imgNext.setVisibility(8);
        this.rlAdd.setVisibility(8);
        this.imgDelete.setVisibility(8);
        this.imgEdit.setVisibility(8);
        this.mTipBgView.setVisibility(8);
        this.tvActionTip.setVisibility(8);
        this.isDraw = false;
    }

    public void initActions2() {
        this.imgBack.setOnClickListener(this);
        this.blank.setOnClickListener(this);
        this.imgEdit.setOnClickListener(this);
        this.rlAdd.setOnClickListener(this);
        this.imgDelete.setOnClickListener(this);
        this.imgNext.setOnClickListener(this);
        this.imgHistory.setOnClickListener(this);
        this.activity.getmMapVideoController().getFimiMap().setmX8AiItemMapListener(new IX8AiItemMapListener() {
            public X8AiMapItem getCurrentItem() {
                return X8AiMapItem.AI_LINE;
            }
        });
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
    }

    public void setMode() {
        if (this.mode == 0) {
            this.model = LineModel.MAP;
            this.activity.getmMapVideoController().switchByAiLineMap();
        } else if (this.mode == 1) {
            this.model = LineModel.VEDIO;
            this.activity.getmMapVideoController().switchByAiLineVideo();
        } else if (this.mode == 2) {
            this.activity.getmMapVideoController().switchByAiLineMap();
        }
    }

    public void switchMapVideo(boolean sightFlag) {
        if (this.handleView != null && this.isShow) {
            if (this.mX8LineState != X8AiLineState.IDLE && this.mX8AilinePrameter.getOrientation() == 0) {
                if (sightFlag) {
                    this.imgVcToggle.setVisibility(8);
                } else {
                    this.imgVcToggle.setVisibility(0);
                }
            }
            if (this.mX8LineState != X8AiLineState.IDLE) {
                return;
            }
            if (sightFlag) {
                if (this.mode == 1) {
                    this.imgDelete.setVisibility(8);
                    this.imgAdd.setVisibility(8);
                    this.tvAdd.setVisibility(8);
                    this.imgEdit.setVisibility(0);
                } else if (this.mode == 0) {
                    this.mX8AiLineInterestPointController.showView(true);
                }
            } else if (this.mode == 1) {
                this.imgDelete.setVisibility(0);
                this.imgAdd.setVisibility(0);
                this.tvAdd.setVisibility(0);
                this.imgEdit.setVisibility(8);
            } else if (this.mode == 0) {
                this.mX8AiLineInterestPointController.showView(false);
            }
        }
    }

    public void closeUi() {
        this.isShow = false;
        this.activity.getmMapVideoController().getFimiMap().setmX8AiItemMapListener(null);
        this.activity.getmMapVideoController().getFimiMap().getAiLineManager().clearAiLineMarker();
        this.activity.getmMapVideoController().getFimiMap().getAiLineManager().resetMapEvent();
        setAiVcClose();
        if (this.deleteDialoag != null) {
            this.deleteDialoag.dismiss();
        }
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        this.mX8AiLinesExcuteConfirmModule.init(this.activity, this.nextRootView);
        this.mX8AiLinesExcuteConfirmModule.setListener(this.mIX8NextViewListener, this.fcManager, this.activity.getmMapVideoController(), this.mX8AilinePrameter, this);
        this.mX8AiLinesExcuteConfirmModule.setParentLevel(0);
        closeIconByNextUi();
        this.mCurrentModule = this.mX8AiLinesExcuteConfirmModule;
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.start();
        }
    }

    public void closeNextUi(final boolean b) {
        this.blank.setVisibility(8);
        this.imgBack.setVisibility(0);
        if (this.isNextShow) {
            this.isNextShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{0.0f, (float) this.width});
            translationRight.setDuration(300);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8AiLineExcuteController.this.nextRootView.setVisibility(8);
                    ((ViewGroup) X8AiLineExcuteController.this.nextRootView).removeAllViews();
                    X8AiLineExcuteController.this.imgBack.setVisibility(0);
                    X8AiLineExcuteController.this.flagSmall.setVisibility(0);
                    if (b) {
                        X8AiLineExcuteController.this.imgNext.setVisibility(0);
                    }
                }
            });
        }
    }

    public void setFcManager(FcManager fcManager) {
        this.fcManager = fcManager;
    }

    private void closeNextUiFromNext(boolean b) {
        closeNextUi(b);
    }

    private void closeAiLine() {
        closeUi();
        if (this.listener != null) {
            this.listener.onLineBackClick();
        }
    }

    public boolean isShow() {
        return this.isShow;
    }

    public void showAiPointView() {
        if (this.mX8LineState == X8AiLineState.RUNNING) {
            this.mX8LineState = X8AiLineState.RUNNING2;
            TcpClient.getIntance().sendLog("showAiPointView");
            if (this.listener != null) {
                this.listener.onLineRunning();
            }
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().removeMapClickListener();
        }
        if (this.isDraw) {
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setAiLineIndexPoint(StateManager.getInstance().getX8Drone().getWpNUM());
        } else {
            getRunningPoint();
        }
    }

    public void lineTaskExite() {
        this.mX8LineState = X8AiLineState.STOP;
        this.fcManager.setAiLineExite(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiLineExcuteController.this.closeAiLine();
                    X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().clearAiLineMarker();
                    X8AiLineExcuteController.this.mX8LineState = X8AiLineState.IDLE;
                    X8AiLineExcuteController.this.listener.onLineComplete(false);
                    return;
                }
                X8AiLineExcuteController.this.mX8LineState = X8AiLineState.RUNNING;
            }
        });
    }

    public void getRunningPoint() {
        if (this.mAiLineGetPointState == AiLineGetPointState.IDLE) {
            this.mAiLineGetPointState = AiLineGetPointState.FIRST;
            getAiLinePoint();
        } else if (this.mAiLineGetPointState == AiLineGetPointState.ALL) {
            this.mAiLineGetPointState = AiLineGetPointState.ALL_VALUE;
            getAllPointValue();
        }
    }

    public void getAiLinePoint() {
        this.count = 0;
        this.countAction = 0;
        this.mList.clear();
        this.mListAtions.clear();
        this.mInterestList.clear();
        this.fcManager.getAiLinePoint(0, new UiCallBackListener<AckGetAiLinePoint>() {
            public void onComplete(CmdResult cmdResult, AckGetAiLinePoint o) {
                if (cmdResult.isSuccess()) {
                    X8AiLineExcuteController.this.count = X8AiLineExcuteController.this.count + 1;
                    X8AiLineExcuteController.this.mX8AilinePrameter.setOrientation(o.getYaw());
                    if (X8AiLineExcuteController.this.mX8AilinePrameter.getOrientation() == 0) {
                        X8AiLineExcuteController.this.openVcToggle();
                    }
                    X8AiLineExcuteController.this.mList.add(o);
                    X8AiLineExcuteController.this.getAiLinePoi(o);
                    X8AiLineExcuteController.this.getAllPoint(o.getTotalnumber());
                    X8AiLineExcuteController.this.totalPoint = o.getTotalnumber();
                    return;
                }
                X8AiLineExcuteController.this.mAiLineGetPointState = AiLineGetPointState.IDLE;
            }
        });
    }

    public void getAiLinePoi(AckGetAiLinePoint o) {
        if (o.hasInterrestPoint()) {
            boolean isAdd = false;
            for (AckGetAiLinePoint point : this.mInterestList) {
                if (point.getLongitudePOI() == o.getLongitudePOI() && point.getLatitudePOI() == o.getLatitudePOI() && point.getAltitudePOI() == o.getAltitudePOI()) {
                    isAdd = true;
                    break;
                }
            }
            if (!isAdd) {
                this.mInterestList.add(o);
            }
        }
    }

    public void getAllPoint(final int number) {
        for (int i = 1; i < number; i++) {
            this.fcManager.getAiLinePoint(i, new UiCallBackListener<AckGetAiLinePoint>() {
                public void onComplete(CmdResult cmdResult, AckGetAiLinePoint o) {
                    X8AiLineExcuteController.this.count = X8AiLineExcuteController.this.count + 1;
                    if (cmdResult.isSuccess()) {
                        X8AiLineExcuteController.this.mList.add(o);
                        X8AiLineExcuteController.this.getAiLinePoi(o);
                    }
                    if (X8AiLineExcuteController.this.count != number) {
                        return;
                    }
                    if (X8AiLineExcuteController.this.mList.size() == number) {
                        Collections.sort(X8AiLineExcuteController.this.mList);
                        X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setAiLineMarkByDevice(X8AiLineExcuteController.this.mList, X8AiLineExcuteController.this.mInterestList);
                        if (X8AiLineExcuteController.this.mX8AilinePrameter.getOrientation() != 0) {
                            X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().addSmallMakerByHistory();
                        } else {
                            X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().addSmallMarkerByInterest();
                        }
                        X8AiLineExcuteController.this.mAiLineGetPointState = AiLineGetPointState.ALL;
                        return;
                    }
                    X8AiLineExcuteController.this.mAiLineGetPointState = AiLineGetPointState.IDLE;
                }
            });
        }
    }

    public void getAllPointValue() {
        for (int i = 0; i < this.totalPoint; i++) {
            this.fcManager.getAiLinePointValue(i, new UiCallBackListener<AckGetAiLinePointsAction>() {
                public void onComplete(CmdResult cmdResult, AckGetAiLinePointsAction o) {
                    X8AiLineExcuteController.this.countAction = X8AiLineExcuteController.this.countAction + 1;
                    if (cmdResult.isSuccess()) {
                        X8AiLineExcuteController.this.mListAtions.add(o);
                    }
                    if (X8AiLineExcuteController.this.countAction != X8AiLineExcuteController.this.totalPoint) {
                        return;
                    }
                    if (X8AiLineExcuteController.this.mListAtions.size() == X8AiLineExcuteController.this.totalPoint) {
                        Collections.sort(X8AiLineExcuteController.this.mListAtions);
                        X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setAiLineMarkActionByDevice(X8AiLineExcuteController.this.mListAtions);
                        X8AiLineExcuteController.this.mAiLineGetPointState = AiLineGetPointState.END;
                        X8AiLineExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiLineManager().startAiLineProcess();
                        X8AiLineExcuteController.this.isDraw = true;
                        return;
                    }
                    X8AiLineExcuteController.this.mAiLineGetPointState = AiLineGetPointState.ALL;
                }
            });
        }
    }

    public void openPointValue(MapPointLatLng mpl) {
        if (mpl != null) {
            this.nextRootView.setVisibility(0);
            this.blank.setVisibility(0);
            closeIconByNextUi();
            this.mX8AiLinesPointValueModule.init(this.activity, this.nextRootView, this.mode, mpl, this.activity.getmMapVideoController(), this);
            this.mCurrentModule = this.mX8AiLinesPointValueModule;
            if (!this.isNextShow) {
                this.isNextShow = true;
                this.width = X8Application.ANIMATION_WIDTH;
                ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{(float) this.width, 0.0f});
                animatorY.setDuration(300);
                animatorY.start();
            }
        }
    }

    public void historyUi2NextUi(X8AiLinePointInfo lineInfo) {
        ((ViewGroup) this.nextRootView).removeAllViews();
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        this.imgBack.setVisibility(8);
        this.imgNext.setVisibility(8);
        this.mX8AiLinesExcuteConfirmModule.init(this.activity, this.nextRootView);
        this.mX8AiLinesExcuteConfirmModule.setListener(this.mIX8NextViewListener, this.fcManager, this.activity.getmMapVideoController(), this.mX8AilinePrameter, null);
        this.mX8AiLinesExcuteConfirmModule.setParentLevel(1);
        this.mCurrentModule = this.mX8AiLinesExcuteConfirmModule;
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", new float[]{(float) this.width, 0.0f});
            animatorY.setDuration(300);
            animatorY.start();
        }
        this.mX8AiLinesExcuteConfirmModule.setDataByHistory(lineInfo);
    }

    public void cancleByModeChange(int taskMode) {
        boolean z = true;
        if (taskMode != 1) {
            z = false;
        }
        onTaskComplete(z);
    }

    public void onDroneConnected(boolean b) {
        if (!this.isShow) {
            return;
        }
        if (b) {
            sysAiVcCtrlMode();
        } else {
            onDisconnectTaskComplete();
        }
    }

    private void onTaskComplete(boolean isShow) {
        closeUi();
        if (this.listener != null) {
            this.listener.onLineBackClick();
            this.listener.onLineComplete(isShow);
        }
    }

    private void onDisconnectTaskComplete() {
        closeUi();
        if (this.listener != null) {
            this.listener.onLineBackClick();
            this.listener.onLineComplete(false);
        }
    }

    public void setAiVcOpen() {
        this.fcManager.setAiVcOpen(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiLineExcuteController.this.imgVcToggle.setSelected(true);
                    X8AiLineExcuteController.this.activity.getmX8AiTrackController().openUi();
                }
            }
        });
    }

    public void setAiVcClose() {
        this.fcManager.setAiVcClose(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiLineExcuteController.this.imgVcToggle.setSelected(false);
                }
            }
        });
        this.activity.getmX8AiTrackController().closeUi();
    }

    public void setAiVcNotityFc() {
        this.fcManager.setAiVcNotityFc(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
    }

    public void setGoEnabled(boolean b) {
        if (b) {
            setAiVcNotityFc();
        }
    }

    public void onTouchActionDown() {
    }

    public void onTouchActionUp() {
    }

    public void onTracking() {
        this.imgVcToggle.setEnabled(true);
    }

    private void historyUirendering() {
        int mapType = GlobalConfig.getInstance().getMapType() == MapType.AMap ? 1 : 0;
        this.lineInfo = X8AiLinePointInfoHelper.getIntance().getLineInfoById(this.lineId);
        if (this.lineInfo != null) {
            List<X8AiLinePointLatlngInfo> list = X8AiLinePointInfoHelper.getIntance().getLatlngByLineId(mapType, this.lineId);
            this.mX8AilinePrameter.setOrientation(this.lineInfo.getType());
            this.mX8AilinePrameter.setSpeed((float) this.lineInfo.getSpeed());
            int type = this.lineInfo.getRunByMapOrVedio();
            if (type == 1) {
                this.model = LineModel.VEDIO;
            } else {
                this.model = LineModel.MAP;
            }
            List mapPointList = new ArrayList();
            List<MapPointLatLng> interestList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                MapPointLatLng point = new MapPointLatLng();
                X8AiLinePointLatlngInfo latlngInfo = (X8AiLinePointLatlngInfo) list.get(i);
                point.nPos = latlngInfo.getNumber() + 1;
                point.latitude = latlngInfo.getLatitude();
                point.longitude = latlngInfo.getLongitude();
                point.altitude = (float) latlngInfo.getAltitude();
                point.yawMode = this.lineInfo.getType();
                point.gimbalPitch = latlngInfo.getGimbalPitch();
                point.action = latlngInfo.getPointActionCmd();
                point.roration = latlngInfo.getRoration();
                point.isMapPoint = type == 0;
                if (!(latlngInfo.getAltitudePOI() == 0 && latlngInfo.getLatitudePOI() == 0.0d && latlngInfo.getLongitudePOI() == 0.0d)) {
                    MapPointLatLng interestPoint = new MapPointLatLng();
                    interestPoint.latitude = latlngInfo.getLatitudePOI();
                    interestPoint.longitude = latlngInfo.getLongitudePOI();
                    interestPoint.altitude = (float) latlngInfo.getAltitudePOI();
                    interestPoint.isIntertestPoint = true;
                    boolean isAdd = false;
                    for (MapPointLatLng tempPoint : interestList) {
                        if (interestPoint.latitude == tempPoint.latitude && interestPoint.longitude == tempPoint.longitude && interestPoint.altitude == tempPoint.altitude) {
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd) {
                        interestList.add(interestPoint);
                    }
                    point.mInrertestPoint = interestPoint;
                }
                if (this.model == LineModel.VEDIO) {
                    point.setAngle(latlngInfo.getYaw());
                }
                if (this.lineInfo.getType() == 0) {
                    point.showAngle = 0.0f;
                } else if (this.lineInfo.getType() != 1 && this.lineInfo.getType() == 2) {
                    float angle;
                    if (i == 0) {
                        angle = getAngle(this.activity.getmMapVideoController().getFimiMap().getDeviceLatlng(), point);
                    } else {
                        angle = getAngle((MapPointLatLng) mapPointList.get(i - 1), point);
                    }
                    point.showAngle = angle;
                }
                mapPointList.add(point);
            }
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().setAiLineMarkByHistory(mapPointList, (List) interestList);
            if (this.mX8AilinePrameter.getOrientation() != 0) {
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().addSmallMakerByHistory();
            } else {
                this.activity.getmMapVideoController().getFimiMap().getAiLineManager().addSmallMarkerByInterest();
            }
            if (this.activity.getmMapVideoController().getFimiMap().getAiLineManager().isFarToHome()) {
                this.imgNext.setEnabled(false);
                showMaxSaveDialog();
            }
        }
    }

    public float getAngle(MapPointLatLng from, MapPointLatLng to) {
        return this.activity.getmMapVideoController().getFimiMap().getAiLineManager().getPointAngle(from, to);
    }

    public void goHistoryActivity() {
        this.activity.startActivityForResult(new Intent(this.activity, X8AiLineHistoryActivity.class), X8sMainActivity.X8GETAILINEIDBYHISTORY);
    }

    public void switchLine(long lineId, int type) {
        if (lineId != this.lineId) {
            this.activity.getmMapVideoController().getFimiMap().getAiLineManager().clearAiLineMarker();
            this.lineId = lineId;
            historyUirendering();
        }
    }

    public void openVcToggle() {
        if (this.activity.getmMapVideoController().isFullVideo()) {
            this.imgVcToggle.setVisibility(0);
        } else {
            this.imgVcToggle.setVisibility(8);
        }
    }

    public void closeIconByNextUi() {
        this.imgNext.setVisibility(8);
        this.imgBack.setVisibility(8);
        this.flagSmall.setVisibility(8);
    }

    public void showMaxSaveDialog() {
        new X8SingleCustomDialog(this.activity, getString(R.string.x8_ai_line_history_disable_excute), String.format(getString(R.string.x8_ai_line_history_disable_excute_message), new Object[]{X8NumberUtil.getDistanceNumberString(1000.0f, 1, false)}), new X8SingleCustomDialog.onDialogButtonClickListener() {
            public void onSingleButtonClick() {
            }
        }).show();
    }

    public void sysAiVcCtrlMode() {
        if (this.mX8LineState != X8AiLineState.IDLE) {
            return;
        }
        if (this.timeSend == 0) {
            this.timeSend = 1;
            this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                public void onComplete(CmdResult cmdResult, Object o) {
                }
            }, X8Task.VCM_MISSION.ordinal());
            return;
        }
        this.timeSend = 0;
    }
}
