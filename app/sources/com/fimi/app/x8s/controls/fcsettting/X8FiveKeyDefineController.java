package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.FiveKeyDefineAdapter;
import com.fimi.app.x8s.adapter.FiveKeyDefineAdapter.OnItemClickListener;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8FiveKeyDefineListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.controller.FcCtrlManager;

public class X8FiveKeyDefineController extends AbsX8MenuBoxControllers implements OnClickListener {
    private FiveKeyDefineAdapter adapter;
    private int curPosition;
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private int key;
    private IX8FiveKeyDefineListener listener;
    private Context mContext;
    private RecyclerView recyclerView;
    private TextView tvTitle;

    public X8FiveKeyDefineController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.contentView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.x8_main_rc_item_five_key_define, (ViewGroup) rootView, true);
        this.mContext = this.contentView.getContext();
        this.imgReturn = (ImageView) this.contentView.findViewById(R.id.img_return);
        this.tvTitle = (TextView) this.contentView.findViewById(R.id.tv_title);
        this.recyclerView = (RecyclerView) this.contentView.findViewById(R.id.recycle_five_key);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 3));
        this.adapter = new FiveKeyDefineAdapter(this.mContext, X8RcItemController.FIVE_KEY_DATA_ARRAY);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setItemAnimator(null);
        this.adapter.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClicked(int index) {
                X8FiveKeyDefineController.this.adapter.setItemSelect(index);
                if (X8FiveKeyDefineController.this.listener != null) {
                    if (X8FiveKeyDefineController.this.key == 0) {
                        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_UP_KEY, index);
                    } else if (X8FiveKeyDefineController.this.key == 1) {
                        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_DOWN_KEY, index);
                    } else if (X8FiveKeyDefineController.this.key == 2) {
                        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_LEFT_KEY, index);
                    } else if (X8FiveKeyDefineController.this.key == 3) {
                        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_RIGHT_KEY, index);
                    } else if (X8FiveKeyDefineController.this.key == 4) {
                        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_CENTRE_KEY, index);
                    }
                    X8FiveKeyDefineController.this.listener.onSelected(X8FiveKeyDefineController.this.key, index);
                }
            }
        });
        initActions();
    }

    public void initActions() {
        if (this.contentView != null) {
            this.imgReturn.setOnClickListener(this);
        }
    }

    public void onDroneConnected(boolean b) {
        this.adapter.notifyDataSetChanged();
    }

    public void defaultVal() {
    }

    public void onClick(View v) {
        if (v.getId() == R.id.img_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onBack();
            }
        }
    }

    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(0);
    }

    public void closeItem() {
        this.isShow = false;
        this.contentView.setVisibility(8);
        defaultVal();
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setCalibrationListener(IX8FiveKeyDefineListener listener) {
        this.listener = listener;
    }

    public void setCurIndex(int key, int curPosition) {
        this.key = key;
        this.curPosition = curPosition;
        initViewData();
    }

    private void initViewData() {
        switch (this.key) {
            case 0:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_up));
                break;
            case 1:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_down));
                break;
            case 2:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_left));
                break;
            case 3:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_right));
                break;
            case 4:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_center));
                break;
        }
        if (this.adapter != null) {
            this.adapter.setItemSelect(this.curPosition);
        }
    }
}
