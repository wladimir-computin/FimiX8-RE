package com.fimi.app.x8s.ui.album.x8s;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import ch.qos.logback.core.net.SyslogConstants;
import com.alibaba.fastjson.JSONObject;
import com.example.album.R;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IBroadcastPreform;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.album.iview.IRecycleAdapter;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.widget.DownloadStateView.State;
import com.fimi.app.x8s.adapter.BodyRecycleViewHolder;
import com.fimi.app.x8s.adapter.PanelRecycleViewHolder;
import com.fimi.app.x8s.adapter.X8sPanelRecycleAdapter;
import com.fimi.app.x8s.ui.presenter.X8CameraFragmentPrensenter;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.utils.FileTool;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.widget.CustomLoadManage;
import com.fimi.x8sdk.controller.CameraManager;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class X8BaseMediaFragmentPresenter<T extends MediaModel> implements IRecycleAdapter, IHandlerCallback, IBroadcastPreform {
    private final int RESH_NOTIFY = 888;
    private final String TAG = "X9BaseMediaFragmentPren";
    CameraManager cameraManager = new CameraManager();
    protected Context context;
    protected int defaultPhtotHeight = SyslogConstants.LOG_CLOCK;
    protected int defaultPhtotWidth = SyslogConstants.LOG_CLOCK;
    protected List<T> deleteList = new ArrayList();
    protected boolean isCamera;
    protected boolean isEnterSelectMode;
    protected boolean isResh = true;
    protected boolean isScrollRecycle;
    protected GridLayoutManager mGridLayoutManager;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 888) {
                X8BaseMediaFragmentPresenter.this.isResh = true;
            }
        }
    };
    protected ISelectData mISelectData;
    protected X8sPanelRecycleAdapter mPanelRecycleAdapter;
    protected RecyclerView mRecyclerView;
    protected CopyOnWriteArrayList<T> modelList;
    protected String perfix = "file://";
    private IPersonalDataCallBack personalDataCallBack = new IPersonalDataCallBack() {
        public void onPersonalDataCallBack(int groupId, int cmdId, ILinkMessage message) {
        }

        public void onPersonalSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
        }
    };
    protected List<T> selectList = new ArrayList();
    protected LinkedHashMap<String, CopyOnWriteArrayList<T>> stateHashMap;

    public abstract void registerDownloadListerner();

    public abstract void registerReciver();

    public abstract void showCategorySelectView(boolean z);

    public abstract void unRegisterReciver();

    public X8BaseMediaFragmentPresenter(RecyclerView mRecyclerView, X8sPanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context, boolean isCamera) {
        this.mRecyclerView = mRecyclerView;
        this.mPanelRecycleAdapter = mPanelRecycleAdapter;
        this.mISelectData = mISelectData;
        this.context = context;
        this.isCamera = isCamera;
    }

    /* Access modifiers changed, original: protected */
    public void changeViewState(View view, int state, int resBg) {
        view.setVisibility(state);
        view.setBackgroundResource(resBg);
    }

    /* Access modifiers changed, original: protected */
    public void changeSelectViewState(MediaModel model, ViewHolder holder, int state) {
        if (model.isCategory()) {
            ((PanelRecycleViewHolder) holder).mBtnAllSelect.setVisibility(state);
            return;
        }
        BodyRecycleViewHolder bodyRecycleViewHolder = (BodyRecycleViewHolder) holder;
        if (state == 0 && bodyRecycleViewHolder.mDownloadStateView.getVisibility() == 0) {
            bodyRecycleViewHolder.ivSelect.setVisibility(0);
            bodyRecycleViewHolder.mIvDownloadMask.setVisibility(8);
            bodyRecycleViewHolder.mIvSelectMask.setVisibility(0);
        } else if (state == 0) {
            bodyRecycleViewHolder.ivSelect.setVisibility(0);
            bodyRecycleViewHolder.mIvSelectMask.setVisibility(0);
        } else {
            bodyRecycleViewHolder.ivSelect.setVisibility(8);
            bodyRecycleViewHolder.mIvSelectMask.setVisibility(8);
        }
    }

    /* Access modifiers changed, original: protected */
    public void changeDownloadState(BodyRecycleViewHolder holder, State state) {
        holder.mDownloadStateView.setState(state);
        if (state == State.DOWNLOAD_FAIL) {
            holder.mTvDownloadState.setText(this.context.getString(R.string.media_downlown_fail));
        } else if (state == State.PAUSE) {
            holder.mTvDownloadState.setText(this.context.getString(R.string.media_downlown_stop));
        }
    }

    /* Access modifiers changed, original: protected */
    public void changeDownloadProgress(BodyRecycleViewHolder holder, int progress) {
        holder.mDownloadStateView.setProgress(progress);
        holder.mTvDownloadState.setText(this.context.getString(R.string.media_downlown_download, new Object[]{progress + "%"}));
    }

    /* Access modifiers changed, original: protected */
    public void showDownloadImg(BodyRecycleViewHolder holder, boolean isShow) {
        int i;
        holder.mDownloadStateView.setVisibility(isShow ? 0 : 8);
        TextView textView = holder.mTvDownloadState;
        if (isShow) {
            i = 0;
        } else {
            i = 8;
        }
        textView.setVisibility(i);
        if (isShow && holder.ivSelect.getVisibility() == 0) {
            holder.mIvSelectMask.setVisibility(0);
            holder.mIvDownloadMask.setVisibility(8);
        } else if (isShow) {
            holder.mIvSelectMask.setVisibility(8);
            holder.mIvDownloadMask.setVisibility(0);
        } else {
            holder.mIvDownloadMask.setVisibility(8);
        }
    }

    /* Access modifiers changed, original: protected */
    public void getOriginalData() {
        if (X9HandleType.isCameraView()) {
            this.modelList = DataManager.obtain().getX9CameraDataList();
            this.stateHashMap = DataManager.obtain().getX9CameraDataHash();
            return;
        }
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
            this.mISelectData.selectSize(size, calculationSelectFileSize());
        }
    }

    /* Access modifiers changed, original: protected */
    public void callAddSingleFile() {
        if (this.mISelectData != null) {
            this.mISelectData.addSingleFile();
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
    public void callStartDownload() {
        if (this.mISelectData != null) {
            this.mISelectData.startDownload();
        }
    }

    public void callDeleteComplete() {
        if (this.mISelectData != null) {
            this.mISelectData.onDeleteComplete();
        }
    }

    /* Access modifiers changed, original: protected */
    public void callAllSelectMode(boolean isAll) {
        if (this.mISelectData != null) {
            this.mISelectData.allSelectMode(isAll);
        }
    }

    /* Access modifiers changed, original: protected */
    public void clearSelectData() {
        this.selectList.clear();
    }

    /* Access modifiers changed, original: protected */
    public void addSelectModel(T mode) {
        if (!mode.isCategory() && !mode.isHeadView()) {
            this.selectList.add(mode);
        }
    }

    /* Access modifiers changed, original: protected */
    public void removeSelectModel(T mode) {
        this.selectList.remove(mode);
    }

    /* Access modifiers changed, original: protected */
    public void preformMode(T model, ViewHolder holder) {
        BodyRecycleViewHolder bodyRecycleViewHolder = (BodyRecycleViewHolder) holder;
        if (model != null && bodyRecycleViewHolder.mDownloadStateView.getVisibility() != 0) {
            if (model.isSelect()) {
                removeSelectModel(model);
                model.setSelect(false);
                changeSelectViewState(model, holder, 8);
            } else {
                addSelectModel(model);
                model.setSelect(true);
                changeSelectViewState(model, holder, 0);
            }
            preformDataSelect(model);
            if (this.selectList.size() == (this.modelList.size() - this.stateHashMap.size()) - 1) {
                callAllSelectMode(true);
            } else {
                callAllSelectMode(false);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void preformDataSelect(T mode) {
        int firstIndex = 0;
        int count = 0;
        int selectCount = 0;
        boolean isEnter = false;
        for (int index = 0; index < this.modelList.size(); index++) {
            if (mode.getFormatDate().split(" ")[0].equals(((MediaModel) this.modelList.get(index)).getFormatDate() == null ? null : ((MediaModel) this.modelList.get(index)).getFormatDate().split(" ")[0])) {
                if (isEnter) {
                    count++;
                    if (((MediaModel) this.modelList.get(index)).isSelect()) {
                        selectCount++;
                    }
                } else {
                    firstIndex = index;
                }
                isEnter = true;
            } else if (isEnter) {
                break;
            }
        }
        if (count == selectCount) {
            if (!((MediaModel) this.modelList.get(firstIndex)).isSelect()) {
                ((MediaModel) this.modelList.get(firstIndex)).setSelect(true);
                this.mPanelRecycleAdapter.notifyItemChanged(firstIndex);
            }
        } else if (((MediaModel) this.modelList.get(firstIndex)).isSelect()) {
            ((MediaModel) this.modelList.get(firstIndex)).setSelect(false);
            this.mPanelRecycleAdapter.notifyItemChanged(firstIndex);
        }
    }

    public void enterSelectMode(boolean state) {
        this.isEnterSelectMode = state;
        if (!state) {
            preformSelectEvent(false);
        }
        showCategorySelectView(state);
    }

    public void setEnterSelectMode(boolean enterSelectMode) {
        this.isEnterSelectMode = enterSelectMode;
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
        Intent intent = new Intent(this.context, X8MediaDetailActivity.class);
        MediaModel mediaModel = (MediaModel) this.modelList.get(position);
        Log.i("moweiru", "mediaModel====" + mediaModel.toString());
        String formatDate = mediaModel.getFormatDate().split(" ")[0];
        int dataPosition = 0;
        for (Entry key : this.stateHashMap.entrySet()) {
            if (formatDate != null && formatDate.compareTo((String) key.getKey()) <= 0) {
                dataPosition++;
            }
        }
        intent.putExtra(AlbumConstant.SELECTPOSITION, (position - dataPosition) - 1);
        LogUtil.i("zhej", "goMediaDetailActivity: modelList:" + this.modelList.size() + ",position:" + ((position - dataPosition) - 1));
        this.context.startActivity(intent);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AlbumConstant.DELETEITEMACTION)) {
            MediaModel mediaModel = (MediaModel) intent.getSerializableExtra(AlbumConstant.DELETEITEM);
            if (this.modelList != null && this.modelList.contains(mediaModel)) {
                this.mPanelRecycleAdapter.updateDeleteItem(this.modelList.indexOf(mediaModel));
            }
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
            String filePath = mediaModel.getFileLocalPath();
            sendBroadcastMediaScannerScanFile(filePath);
            FileTool.deleteFile(filePath);
        }
        sendBroadcastUpdateDeleteItem();
        this.mPanelRecycleAdapter.updateDeleteItems();
        this.selectList.clear();
        this.isEnterSelectMode = false;
        callBackDeleteFile();
        callDeleteComplete();
    }

    public void sendBroadcastUpdateDeleteItem() {
        Intent intent = new Intent();
        List<T> list = new ArrayList();
        list.addAll(this.selectList);
        intent.setAction(X8CameraFragmentPrensenter.LOCALFILEDELETEEIVER);
        intent.putExtra(X8CameraFragmentPrensenter.LOCLAFILEDELETEITEM, (Serializable) list);
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
    }

    public void downLoadFile() {
        int index = 0;
        while (index < this.selectList.size()) {
            int j = 0;
            while (j < this.modelList.size()) {
                if (!((MediaModel) this.selectList.get(index)).getFileUrl().equals(((MediaModel) this.modelList.get(j)).getFileUrl()) || ((MediaModel) this.selectList.get(index)).isDownLoadOriginalFile()) {
                    if (((MediaModel) this.selectList.get(index)).getFileUrl().equals(((MediaModel) this.modelList.get(j)).getFileUrl()) && ((MediaModel) this.selectList.get(index)).isDownLoadOriginalFile()) {
                        ((MediaModel) this.modelList.get(j)).setSelect(false);
                        this.mPanelRecycleAdapter.notifyItemChanged(j);
                        break;
                    }
                    j++;
                } else {
                    ((MediaModel) this.modelList.get(j)).setSelect(false);
                    this.mPanelRecycleAdapter.notifyItemChanged(j);
                    break;
                }
            }
            index++;
        }
        X8MediaFileDownloadManager.getInstance().startDownload(this.selectList);
        this.selectList.clear();
        this.isEnterSelectMode = false;
        callStartDownload();
        this.isResh = false;
        for (index = 0; index < this.modelList.size(); index++) {
            if (((MediaModel) this.modelList.get(index)).isCategory()) {
                ((MediaModel) this.modelList.get(index)).setSelect(false);
                this.mPanelRecycleAdapter.notifyItemChanged(index);
            }
        }
        Message message = new Message();
        message.what = 888;
        this.mHandler.sendMessageDelayed(message, 500);
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
            notifyAllVisible();
            callBackSelectSize(this.selectList.size());
        }
    }

    public long calculationSelectFileSize() {
        long capacity = 0;
        for (MediaModel mediaModel : this.selectList) {
            capacity += mediaModel.getFileSize();
        }
        return capacity;
    }

    public void deleteCameraSelectFile() {
        int index;
        this.deleteList.clear();
        this.deleteList.addAll(this.selectList);
        for (index = 0; index < this.selectList.size(); index++) {
            for (int j = 0; j < this.modelList.size(); j++) {
                if (((MediaModel) this.selectList.get(index)).getFileUrl().equals(((MediaModel) this.modelList.get(j)).getFileUrl())) {
                    ((MediaModel) this.modelList.get(j)).setSelect(false);
                    this.mPanelRecycleAdapter.notifyItemChanged(j);
                    break;
                }
            }
        }
        this.selectList.clear();
        this.isEnterSelectMode = false;
        for (index = 0; index < this.modelList.size(); index++) {
            if (((MediaModel) this.modelList.get(index)).isCategory()) {
                this.mPanelRecycleAdapter.notifyItemChanged(index);
            }
        }
        callBackDeleteFile();
        if (this.deleteList.size() > 0) {
            CustomLoadManage.showNoClick(this.context);
        }
        for (int i = 0; i < this.deleteList.size(); i++) {
            final int finalI = i;
            this.cameraManager.deleteOnlineFile(((MediaModel) this.deleteList.get(i)).getFileUrl(), new JsonUiCallBackListener() {
                public void onComplete(JSONObject rt, Object o) {
                    if (X8BaseMediaFragmentPresenter.this.modelList != null && X8BaseMediaFragmentPresenter.this.modelList.size() > 0 && X8BaseMediaFragmentPresenter.this.modelList.contains(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI))) {
                        X8BaseMediaFragmentPresenter.this.mPanelRecycleAdapter.updateDeleteItem(X8BaseMediaFragmentPresenter.this.modelList.indexOf(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI)));
                    }
                    if (finalI >= X8BaseMediaFragmentPresenter.this.deleteList.size() - 1) {
                        CustomLoadManage.dismiss();
                    }
                }
            });
        }
    }

    public boolean isModelListEmpty() {
        if (this.modelList == null || this.modelList.size() <= 0) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void notifyAllVisible() {
        if (this.mGridLayoutManager == null) {
            return;
        }
        if (this.mGridLayoutManager != null) {
            int firstVisibleItem = this.mGridLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItem = this.mGridLayoutManager.findLastVisibleItemPosition();
            if (firstVisibleItem != -1 && lastVisibleItem != -1) {
                if (firstVisibleItem - 20 > 0) {
                    firstVisibleItem -= 20;
                } else {
                    firstVisibleItem = 0;
                }
                if (lastVisibleItem + 20 < this.modelList.size()) {
                    lastVisibleItem += 20;
                } else {
                    lastVisibleItem = this.modelList.size() - 1;
                }
                this.mPanelRecycleAdapter.notifyItemRangeChanged(firstVisibleItem, (lastVisibleItem - firstVisibleItem) + 1);
                return;
            }
            return;
        }
        this.mPanelRecycleAdapter.notifyDataSetChanged();
    }

    public void sendBroadcastMediaScannerScanFile(String path) {
        if (path != null) {
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(new File(path)));
            this.context.sendBroadcast(intent);
        }
    }

    protected static String englishConvertDigital(String englishData) {
        SimpleDateFormat digitalSdf = new SimpleDateFormat("yyyy.MM.dd");
        String digitalData = null;
        try {
            return new SimpleDateFormat("MM/dd/yyyy", Locale.CHINA).format(digitalSdf.parse(englishData));
        } catch (ParseException e) {
            e.printStackTrace();
            return digitalData;
        }
    }

    public void notifyAddCallback(MediaModel model) {
        callAddSingleFile();
        if (this.isEnterSelectMode) {
            preformDataSelect(model);
            if (this.selectList.size() == (this.modelList.size() - this.stateHashMap.size()) - 1) {
                callAllSelectMode(true);
            } else {
                callAllSelectMode(false);
            }
        }
    }
}
