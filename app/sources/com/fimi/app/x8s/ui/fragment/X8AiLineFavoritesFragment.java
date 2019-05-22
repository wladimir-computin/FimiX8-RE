package com.fimi.app.x8s.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.adapter.X8AiLineHistoryAdapter2;
import com.fimi.app.x8s.interfaces.IX8AiLineHistoryListener;
import com.fimi.app.x8s.widget.X8SingleCustomDialog;
import com.fimi.app.x8s.widget.X8SingleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.map.MapType;
import java.util.List;

public class X8AiLineFavoritesFragment extends Fragment {
    public static final String ARGS_PAGE = "X8AiLineFavoritesFragment";
    private X8SingleCustomDialog dialog;
    private boolean isShow;
    private List<X8AiLinePointInfo> list;
    private int mPage;
    private RecyclerView mRecyclerView;
    private X8AiLineHistoryAdapter2 mX8AiLineHistoryAdapter2;
    private IX8AiLineHistoryListener mX8AiLineSelectListener;
    private View rootView;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.x8_fragment_ai_line_history, container, false);
            this.mRecyclerView = (RecyclerView) this.rootView.findViewById(R.id.recycleview);
            this.list = X8AiLinePointInfoHelper.getIntance().getLastItem(GlobalConfig.getInstance().getMapType() == MapType.AMap ? 1 : 0, true, 20);
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ((SimpleItemAnimator) this.mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            this.mX8AiLineHistoryAdapter2 = new X8AiLineHistoryAdapter2(getContext(), this.list, 1);
            this.mX8AiLineHistoryAdapter2.setOnX8AiLineSelectListener(this.mX8AiLineSelectListener);
            this.mRecyclerView.setAdapter(this.mX8AiLineHistoryAdapter2);
        }
        return this.rootView;
    }

    public void setOnX8AiLineSelectListener(IX8AiLineHistoryListener listener) {
        this.mX8AiLineSelectListener = listener;
    }

    public void notityItemChange(long lineId, int saveFlag) {
        X8AiLinePointInfo info = null;
        int pos = 0;
        if (this.rootView != null) {
            int i = 0;
            while (i < this.list.size()) {
                if (lineId == ((X8AiLinePointInfo) this.list.get(i)).getId().longValue()) {
                    ((X8AiLinePointInfo) this.list.get(i)).setSaveFlag(saveFlag);
                    if (saveFlag == 1) {
                        this.mX8AiLineHistoryAdapter2.notifyItemChanged(i);
                    } else {
                        info = (X8AiLinePointInfo) this.list.get(i);
                    }
                    pos = i;
                } else {
                    i++;
                }
            }
        }
        if (info != null && saveFlag == 0) {
            this.mX8AiLineHistoryAdapter2.removeData(info, pos);
        }
    }

    public void addLineItem(X8AiLinePointInfo info) {
        if (this.rootView != null) {
            this.mX8AiLineHistoryAdapter2.addData(info);
        }
    }

    public void notityItemChange(long lineId, String name) {
        if (this.rootView != null) {
            for (int i = 0; i < this.list.size(); i++) {
                if (lineId == ((X8AiLinePointInfo) this.list.get(i)).getId().longValue()) {
                    ((X8AiLinePointInfo) this.list.get(i)).setName(name);
                    this.mX8AiLineHistoryAdapter2.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    public int favoritesCapacity() {
        return this.list.size();
    }

    public void showMaxSaveDialog() {
        if (!this.isShow) {
            if (this.list.size() > 15) {
                this.isShow = true;
            }
            if (this.isShow) {
                if (this.dialog == null) {
                    this.dialog = new X8SingleCustomDialog(getContext(), getContext().getString(R.string.x8_ai_line_not_enough_save_title), getContext().getString(R.string.x8_ai_line_not_enough_save_tip), new onDialogButtonClickListener() {
                        public void onSingleButtonClick() {
                            X8AiLineFavoritesFragment.this.mX8AiLineSelectListener.goFavorites();
                        }
                    });
                }
                this.dialog.show();
            }
        } else if (this.list.size() <= 15) {
            this.isShow = false;
        }
    }
}
