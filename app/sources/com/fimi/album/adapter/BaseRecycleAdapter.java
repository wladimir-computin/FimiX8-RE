package com.fimi.album.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.album.iview.INodataTip;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseRecycleAdapter<T extends MediaModel> extends Adapter<ViewHolder> implements IHandlerCallback {
    public static final int ITEMBODYTYPE = 32;
    public static final int ITEMHEADTYPE = 16;
    public static final String TAG = BaseRecycleAdapter.class.getName();
    private int bodySpanCount = 1;
    protected Context context;
    private int headSpanCount = 4;
    private int internalListBound = 2;
    protected INodataTip mINodataTip;
    protected Handler mainHandler;
    private DataManager mdataManager = DataManager.obtain();
    private CopyOnWriteArrayList<T> modelList;
    private CopyOnWriteArrayList<T> modelNoHeadList;
    protected Handler otherHandler;
    private HashMap<String, CopyOnWriteArrayList<T>> stateHashMap;

    public BaseRecycleAdapter(Context context, INodataTip mINodataTip) {
        initData();
        this.context = context;
        this.otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
        this.mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);
        this.mINodataTip = mINodataTip;
    }

    private void initData() {
        this.modelList = this.mdataManager.getLocalDataList();
        this.stateHashMap = this.mdataManager.getDataHash();
        this.modelNoHeadList = this.mdataManager.getLocalDataNoHeadList();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int position) {
                    if (BaseRecycleAdapter.this.isCategoryType(position)) {
                        return BaseRecycleAdapter.this.headSpanCount;
                    }
                    return BaseRecycleAdapter.this.bodySpanCount;
                }
            });
        }
    }

    public void remoteItem(int position) {
        MediaModel mediaModel = (MediaModel) this.modelList.get(position);
        String localPath = mediaModel.getFileLocalPath();
        String formateDate = mediaModel.getFormatDate();
        this.modelNoHeadList.remove(mediaModel);
        this.modelList.remove(mediaModel);
        if (this.stateHashMap != null && localPath != null) {
            CopyOnWriteArrayList<T> internalList = (CopyOnWriteArrayList) this.stateHashMap.get(formateDate);
            if (internalList != null) {
                Iterator it = internalList.iterator();
                while (it.hasNext()) {
                    MediaModel cacheModel = (MediaModel) it.next();
                    if (cacheModel != null && localPath.equals(cacheModel.getFileLocalPath())) {
                        internalList.remove(cacheModel);
                    }
                }
                if (internalList.size() < this.internalListBound) {
                    this.modelList.remove(internalList.get(0));
                }
            }
        }
    }

    public void updateDeleteItems() {
        notifyDataSetChanged();
    }

    public void updateDeleteItem(int position) {
        MediaModel mediaModel = (MediaModel) this.modelList.get(position);
        if (mediaModel != null) {
            String formateDate = mediaModel.getFormatDate();
            String localPath = mediaModel.getFileLocalPath();
            this.modelList.remove(position);
            notifyItemRemoved(position);
            if (!(this.stateHashMap == null || localPath == null)) {
                CopyOnWriteArrayList<T> internalList = (CopyOnWriteArrayList) this.stateHashMap.get(formateDate);
                if (internalList != null) {
                    Iterator it = internalList.iterator();
                    while (it.hasNext()) {
                        MediaModel cacheModel = (MediaModel) it.next();
                        if (cacheModel != null && localPath.equals(cacheModel.getFileLocalPath())) {
                            internalList.remove(cacheModel);
                        }
                    }
                    if (internalList.size() < this.internalListBound) {
                        if (internalList.size() < this.internalListBound) {
                            if (position - 1 < this.modelList.size()) {
                                this.modelList.remove(position - 1);
                                notifyItemRemoved(position - 1);
                                notifyItemRangeRemoved(position, this.modelList.size() - position);
                            } else {
                                this.modelList.remove(this.modelList.size() - 1);
                                notifyItemRemoved(this.modelList.size() - 1);
                            }
                        }
                        judgeIsNoData();
                        return;
                    }
                }
            }
            judgeIsNoData();
            notifyItemRangeRemoved(position, this.modelList.size() - position);
        }
    }

    public void addNewItem(T mediaModel) {
        Message message = new Message();
        message.what = 6;
        message.obj = mediaModel;
        this.otherHandler.sendMessage(message);
    }

    public void addItemReally(T mediaModel) {
        int inserterPosition = 0;
        if (mediaModel != null) {
            this.modelNoHeadList.add(mediaModel);
            String currentFormatData = mediaModel.getFormatDate();
            if (this.stateHashMap.containsKey(currentFormatData)) {
                CopyOnWriteArrayList internalList = (CopyOnWriteArrayList) this.stateHashMap.get(currentFormatData);
                if (internalList.size() > 0) {
                    int position = this.modelList.indexOf(internalList.get(0));
                    internalList.add(mediaModel);
                    this.modelList.add(position + 1, mediaModel);
                }
            } else {
                CopyOnWriteArrayList newList = new CopyOnWriteArrayList();
                int forEachPosition = 0;
                for (Entry<String, CopyOnWriteArrayList<T>> entry : this.stateHashMap.entrySet()) {
                    forEachPosition++;
                    if (currentFormatData.compareTo((String) entry.getKey()) <= 0) {
                        break;
                    }
                    inserterPosition += ((CopyOnWriteArrayList) entry.getValue()).size();
                }
                MediaModel mediaModel1;
                if (forEachPosition == 1) {
                    mediaModel1 = new MediaModel();
                    mediaModel1.setCategory(true);
                    mediaModel1.setFormatDate(mediaModel.getFormatDate());
                    newList.add(mediaModel1);
                    newList.add(mediaModel);
                    this.modelList.add(mediaModel1);
                    this.modelList.add(mediaModel);
                } else {
                    mediaModel1 = new MediaModel();
                    mediaModel1.setCategory(true);
                    mediaModel1.setFormatDate(mediaModel.getFormatDate());
                    newList.add(mediaModel1);
                    newList.add(mediaModel);
                    this.modelList.add(inserterPosition, mediaModel1);
                    this.modelList.add(inserterPosition + 1, mediaModel);
                }
                this.stateHashMap.put(currentFormatData, newList);
            }
        }
        Message message = Message.obtain();
        message.what = 7;
        message.arg1 = inserterPosition;
        this.mainHandler.sendMessage(message);
    }

    public int getItemViewType(int position) {
        if (isCategoryType(position)) {
            return 16;
        }
        return 32;
    }

    /* Access modifiers changed, original: protected */
    public boolean isCategoryType(int position) {
        if (this.modelList == null || this.modelList.size() <= 0 || position < 0 || !((MediaModel) this.modelList.get(position)).isCategory()) {
            return false;
        }
        return true;
    }

    public int getItemCount() {
        judgeIsNoData();
        return this.modelList == null ? 0 : this.modelList.size();
    }

    private void judgeIsNoData() {
        if (this.modelList != null) {
            if (this.modelList.size() == 0) {
                if (this.mINodataTip != null) {
                    this.mINodataTip.noDataTipCallback(true);
                }
            } else if (this.mINodataTip != null) {
                this.mINodataTip.noDataTipCallback(false);
            }
        } else if (this.mINodataTip != null) {
            this.mINodataTip.noDataTipCallback(true);
        }
    }

    public void refresh() {
        initData();
        this.mainHandler.sendEmptyMessage(5);
    }

    public boolean handleMessage(Message message) {
        if (message.what == 5) {
            notifyDataSetChanged();
        } else if (message.what == 6) {
            addItemReally((MediaModel) message.obj);
        } else if (message.what == 7) {
            notifyAddNewItem(message.arg1);
        }
        return true;
    }

    private void notifyAddNewItem(int position) {
        notifyItemInserted(position);
    }

    public void removeCallBack() {
    }
}
