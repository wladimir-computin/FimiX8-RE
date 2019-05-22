package com.fimi.album.ui.albumfragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.album.R;
import com.fimi.album.adapter.PanelRecycleAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.broadcast.DeleteItemReceiver;
import com.fimi.album.iview.INodataTip;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.presenter.BaseFragmentPresenter;
import com.fimi.album.presenter.LocalFragmentPresenter;
import com.fimi.album.ui.MediaActivity;
import com.fimi.album.widget.RBaseItemDecoration;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.SizeTool;
import java.lang.ref.WeakReference;

public abstract class BaseFragment extends Fragment implements INodataTip {
    protected WeakReference<Context> contextWeakReference;
    private DeleteItemReceiver deleteItemReceiver;
    private ImageButton lbBottomDelect;
    protected BaseFragmentPresenter mBaseFragmentPresenter;
    protected ISelectData mISelectData;
    protected RecyclerView mRecyclerView;
    protected PanelRecycleAdapter panelRecycleAdapter;
    private RelativeLayout rlMediaNoDataTip;
    private RelativeLayout rlMediaSelectBottom;

    public abstract int getContentID();

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deleteItemReceiver = new DeleteItemReceiver();
        LocalBroadcastManager.getInstance(((Context) this.contextWeakReference.get()).getApplicationContext()).registerReceiver(this.deleteItemReceiver, new IntentFilter(AlbumConstant.DELETEITEMACTION));
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentID(), null);
        initView(view);
        initData();
        initTrans();
        return view;
    }

    private void initTrans() {
        this.lbBottomDelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BaseFragment.this.mBaseFragmentPresenter == null) {
                    return;
                }
                if (BaseFragment.this.mBaseFragmentPresenter.querySelectSize() > 0) {
                    BaseFragment.this.mBaseFragmentPresenter.deleteSelectFile();
                } else {
                    Toast.makeText((Context) BaseFragment.this.contextWeakReference.get(), R.string.no_select_file, 0).show();
                }
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public void initView(View view) {
        this.rlMediaNoDataTip = (RelativeLayout) view.findViewById(R.id.media_no_data_tip);
        this.lbBottomDelect = (ImageButton) view.findViewById(R.id.bottom_delete_ibtn);
        this.rlMediaSelectBottom = (RelativeLayout) view.findViewById(R.id.media_select_bottom_rl);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        FontUtil.changeFontLanTing(((Context) this.contextWeakReference.get()).getAssets(), this.lbBottomDelect);
    }

    /* Access modifiers changed, original: protected */
    public void initData() {
        this.panelRecycleAdapter = new PanelRecycleAdapter((Context) this.contextWeakReference.get(), this);
        if (this.contextWeakReference.get() != null) {
            this.mRecyclerView.setLayoutManager(new GridLayoutManager((Context) this.contextWeakReference.get(), 4));
        }
        this.mRecyclerView.setAdapter(this.panelRecycleAdapter);
        this.mRecyclerView.setOverScrollMode(2);
        this.mRecyclerView.addItemDecoration(new RBaseItemDecoration((Context) this.contextWeakReference.get(), SizeTool.pixToDp(2.5f, (Context) this.contextWeakReference.get()), 17170445));
        this.mRecyclerView.getItemAnimator().setChangeDuration(0);
        initPresenter();
    }

    private void initPresenter() {
        this.mBaseFragmentPresenter = new LocalFragmentPresenter(this.mRecyclerView, this.panelRecycleAdapter, this.mISelectData, (Context) this.contextWeakReference.get());
        this.panelRecycleAdapter.setmIRecycleAdapter(this.mBaseFragmentPresenter);
        if (this.deleteItemReceiver != null) {
            this.deleteItemReceiver.setIReceiver(this.mBaseFragmentPresenter);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.contextWeakReference = new WeakReference(context);
        if (context instanceof MediaActivity) {
            this.mISelectData = (ISelectData) context;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mISelectData = null;
        if (this.mBaseFragmentPresenter != null) {
            this.mBaseFragmentPresenter.canCalAsyncTask();
        }
        if (!(this.contextWeakReference.get() == null || this.deleteItemReceiver == null)) {
            LocalBroadcastManager.getInstance(((Context) this.contextWeakReference.get()).getApplicationContext()).unregisterReceiver(this.deleteItemReceiver);
        }
        if (this.panelRecycleAdapter != null) {
            this.panelRecycleAdapter.removeCallBack();
        }
    }

    public void reshAdapter() {
        if (this.panelRecycleAdapter != null) {
            this.panelRecycleAdapter.refresh();
            if (this.mBaseFragmentPresenter != null) {
                this.mBaseFragmentPresenter.refreshData();
            }
        }
    }

    public void enterSelectAllMode() {
        this.mBaseFragmentPresenter.enterSelectAllMode();
    }

    public void cancalSelectAllMode() {
        this.mBaseFragmentPresenter.cancalSelectAllMode();
    }

    public void enterSelectMode(boolean state, boolean isNeedPreform) {
        showBottomDeleteView(state);
        if (isNeedPreform) {
            this.mBaseFragmentPresenter.enterSelectMode(state);
        }
    }

    public void showBottomDeleteView(boolean state) {
        if (state) {
            this.rlMediaSelectBottom.setVisibility(0);
        } else {
            this.rlMediaSelectBottom.setVisibility(8);
        }
    }

    public void noDataTipCallback(boolean isNodata) {
        if (isNodata) {
            this.rlMediaNoDataTip.setVisibility(0);
        } else {
            this.rlMediaNoDataTip.setVisibility(8);
        }
    }
}
