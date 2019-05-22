package com.fimi.album.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import ch.qos.logback.core.net.SyslogConstants;
import com.fimi.album.adapter.PanelRecycleAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IBroadcastPreform;
import com.fimi.album.iview.IRecycleAdapter;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.ui.MediaDetailActivity;
import com.fimi.kernel.utils.FileTool;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseFragmentPresenter<T extends MediaModel> implements IBroadcastPreform, IRecycleAdapter {
    public static final String TAG = BaseFragmentPresenter.class.getName();
    protected static boolean isScrollRecycle;
    protected Context context;
    protected int defaultPhtotHeight = SyslogConstants.LOG_CLOCK;
    protected int defaultPhtotWidth = SyslogConstants.LOG_CLOCK;
    protected boolean isEnterSelectMode;
    protected ISelectData mISelectData;
    protected PanelRecycleAdapter mPanelRecycleAdapter;
    protected RecyclerView mRecyclerView;
    protected CopyOnWriteArrayList<T> modelList;
    protected String perfix = "file://";
    protected List<T> selectList = new ArrayList();
    protected LinkedHashMap<String, CopyOnWriteArrayList<T>> stateHashMap;

    public BaseFragmentPresenter(RecyclerView mRecyclerView, PanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context) {
        this.mRecyclerView = mRecyclerView;
        this.mPanelRecycleAdapter = mPanelRecycleAdapter;
        this.mISelectData = mISelectData;
        this.context = context;
    }

    /* Access modifiers changed, original: protected */
    public void changeViewState(View view, int state, int resBg) {
        view.setVisibility(state);
        view.setBackgroundResource(resBg);
    }

    /* Access modifiers changed, original: protected */
    public void getOriginalData() {
        this.modelList = DataManager.obtain().getLocalDataList();
        this.stateHashMap = DataManager.obtain().getDataHash();
    }

    /* Access modifiers changed, original: protected */
    public T getModel(int position) {
        if (this.modelList == null) {
            getOriginalData();
        }
        if (position >= this.modelList.size()) {
            return null;
        }
        return (MediaModel) this.modelList.get(position);
    }

    /* Access modifiers changed, original: protected */
    public boolean isContainsModel(T model) {
        if (this.modelList == null) {
            getOriginalData();
        }
        return this.modelList.contains(model);
    }

    /* Access modifiers changed, original: protected */
    public int modelPosition(T model) {
        if (this.modelList == null) {
            getOriginalData();
        }
        return this.modelList.indexOf(model);
    }

    /* Access modifiers changed, original: protected */
    public void callBackSelectSize(int size) {
        if (this.mISelectData != null) {
            this.mISelectData.selectSize(size, 0);
        }
    }

    /* Access modifiers changed, original: protected */
    public void callBackEnterSelectMode() {
        if (this.mISelectData != null) {
            this.mISelectData.enterSelectMode();
        }
    }

    /* Access modifiers changed, original: protected */
    public void callBackQuitSelectMode() {
        if (this.mISelectData != null) {
            this.mISelectData.quitSelectMode();
        }
    }

    /* Access modifiers changed, original: protected */
    public void callBackDeleteFile() {
        if (this.mISelectData != null) {
            this.mISelectData.deleteFile();
        }
    }

    /* Access modifiers changed, original: protected */
    public void clearSelectData() {
        this.selectList.clear();
    }

    /* Access modifiers changed, original: protected */
    public void addSelectModel(T mode) {
        this.selectList.add(mode);
    }

    /* Access modifiers changed, original: protected */
    public void removeSelectModel(T mode) {
        this.selectList.remove(mode);
    }

    /* Access modifiers changed, original: protected */
    public void preformMode(T model, View view, int state, int selectBg, int unSelectBg) {
        if (model != null) {
            if (model.isSelect()) {
                removeSelectModel(model);
                model.setSelect(false);
                changeViewState(view, state, unSelectBg);
                return;
            }
            addSelectModel(model);
            model.setSelect(true);
            changeViewState(view, state, selectBg);
        }
    }

    public void enterSelectMode(boolean state) {
        this.isEnterSelectMode = state;
        if (!state) {
            preformSelectEvent(false);
        }
    }

    public int querySelectSize() {
        return this.selectList.size();
    }

    public void refreshData() {
        getOriginalData();
    }

    public void canCalAsyncTask() {
    }

    /* Access modifiers changed, original: protected */
    public void goMediaDetailActivity(int position) {
        Intent intent = new Intent(this.context, MediaDetailActivity.class);
        String formatDate = ((MediaModel) this.modelList.get(position)).getFormatDate();
        int dataPosition = 0;
        for (Entry key : this.stateHashMap.entrySet()) {
            dataPosition++;
            if (formatDate != null && formatDate.equals(key.getKey())) {
                break;
            }
        }
        intent.putExtra(AlbumConstant.SELECTPOSITION, position - dataPosition);
        this.context.startActivity(intent);
    }

    public void onReceive(Context context, Intent intent) {
        MediaModel mediaModel = (MediaModel) intent.getSerializableExtra(AlbumConstant.DELETEITEM);
        if (this.modelList.contains(mediaModel)) {
            this.mPanelRecycleAdapter.updateDeleteItem(this.modelList.indexOf(mediaModel));
        }
    }

    public void deleteSelectFile() {
        Integer startDeletePosition = null;
        for (int index = 0; index < this.selectList.size(); index++) {
            MediaModel mediaModel = (MediaModel) this.selectList.get(index);
            if (isContainsModel(mediaModel)) {
                int position = modelPosition(mediaModel);
                this.mPanelRecycleAdapter.remoteItem(position);
                if (startDeletePosition == null) {
                    startDeletePosition = Integer.valueOf(position);
                }
            }
            FileTool.deleteFile(mediaModel.getFileLocalPath());
        }
        this.mPanelRecycleAdapter.updateDeleteItems();
        this.selectList.clear();
        this.isEnterSelectMode = false;
        callBackDeleteFile();
    }

    public void enterSelectAllMode() {
        preformSelectEvent(true);
    }

    public void cancalSelectAllMode() {
        preformSelectEvent(false);
    }

    private void preformSelectEvent(boolean state) {
        if (this.modelList != null) {
            for (int index = 0; index < this.modelList.size(); index++) {
                MediaModel mediaModel = getModel(index);
                if (state) {
                    if (!mediaModel.isSelect()) {
                        addSelectModel(mediaModel);
                        mediaModel.setSelect(state);
                    }
                } else if (mediaModel.isSelect()) {
                    removeSelectModel(mediaModel);
                    mediaModel.setSelect(state);
                }
            }
            this.mPanelRecycleAdapter.notifyDataSetChanged();
            callBackSelectSize(this.selectList.size());
        }
    }
}
