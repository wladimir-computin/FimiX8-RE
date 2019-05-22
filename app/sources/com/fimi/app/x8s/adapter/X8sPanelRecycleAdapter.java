package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.INodataTip;
import com.fimi.album.iview.IRecycleAdapter;
import com.fimi.app.x8s.R;
import com.fimi.kernel.utils.DateFormater;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public class X8sPanelRecycleAdapter<T extends MediaModel> extends Adapter<ViewHolder> implements Callback {
    public static final int HEADTYPE = 48;
    public static final int ITEMBODYTYPE = 32;
    public static final int ITEMHEADTYPE = 16;
    public static final String TAG = X8sPanelRecycleAdapter.class.getName();
    private int bodySpanCount = 1;
    private Context context;
    private int headCount = 4;
    private int headSpanCount = 4;
    private int internalListBound = 2;
    private boolean isCamera;
    private INodataTip mINodataTip;
    private IRecycleAdapter mIRecycleAdapter;
    private Handler mainHandler;
    private DataManager mdataManager = DataManager.obtain();
    private CopyOnWriteArrayList<T> modelList;
    private CopyOnWriteArrayList<T> modelNoHeadList;
    private Handler otherHandler;
    private HashMap<String, CopyOnWriteArrayList<T>> stateHashMap;

    public X8sPanelRecycleAdapter(Context context, boolean isCamera, INodataTip mINodataTip) {
        this.isCamera = isCamera;
        initData();
        this.context = context;
        this.otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
        this.mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);
        this.mINodataTip = mINodataTip;
    }

    public void setmIRecycleAdapter(IRecycleAdapter mIRecycleAdapter) {
        this.mIRecycleAdapter = mIRecycleAdapter;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 48) {
            return new HeadRecyleViewHolder(LayoutInflater.from(this.context).inflate(R.layout.x8_recyleview_head, parent, false));
        }
        ViewHolder rvViewHolder;
        if (viewType == 16) {
            rvViewHolder = new PanelRecycleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_panel_view_holder, parent, false));
        } else {
            rvViewHolder = new BodyRecycleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_body_view_holder, parent, false));
        }
        return rvViewHolder;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int position) {
                    if (X8sPanelRecycleAdapter.this.isHeadView(position)) {
                        return X8sPanelRecycleAdapter.this.headCount;
                    }
                    if (X8sPanelRecycleAdapter.this.isCategoryType(position)) {
                        return X8sPanelRecycleAdapter.this.headSpanCount;
                    }
                    return X8sPanelRecycleAdapter.this.bodySpanCount;
                }
            });
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this.mIRecycleAdapter != null) {
            this.mIRecycleAdapter.onBindViewHolder(holder, position);
        }
    }

    public void remoteItem(int position) {
        MediaModel mediaModel = (MediaModel) this.modelList.get(position);
        String localPath = mediaModel.getFileLocalPath();
        String formateDate = mediaModel.getFormatDate().split(" ")[0];
        this.modelNoHeadList.remove(mediaModel);
        this.modelList.remove(mediaModel);
        notifyItemRemoved(mediaModel.getItemPosition());
        statisticalFileCount(mediaModel, false);
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
                    this.stateHashMap.remove(((MediaModel) internalList.get(0)).getFormatDate().split(" ")[0]);
                    this.modelList.remove(internalList.get(0));
                    notifyItemRemoved(((MediaModel) internalList.get(0)).getItemPosition());
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
            statisticalFileCount(mediaModel, false);
            notifyItemRemoved(position);
            if (!(this.stateHashMap == null || localPath == null)) {
                CopyOnWriteArrayList<T> internalList = (CopyOnWriteArrayList) this.stateHashMap.get(formateDate.split(" ")[0]);
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

    private void statisticalFileCount(MediaModel mediaModel, boolean isAdd) {
        if (!mediaModel.isCategory() && !mediaModel.isHeadView()) {
            int count;
            if (isAdd) {
                count = 1;
            } else {
                count = -1;
            }
            if (this.isCamera) {
                if (mediaModel.isVideo()) {
                    DataManager.obtain().setX9CameraVideoCount(DataManager.obtain().getX9CameraVideoCount() + ((long) count));
                } else {
                    DataManager.obtain().setX9CameraPhotoCount(DataManager.obtain().getX9CameraPhotoCount() + ((long) count));
                }
                if (DataManager.obtain().getX9CameraPhotoCount() == 0 && DataManager.obtain().getX9CameraVideoCount() == 0) {
                    notifyItemRangeRemoved(0, 2);
                    DataManager.obtain().reX9CameraDefaultVaribale();
                    judgeIsNoData();
                }
            } else {
                if (mediaModel.isVideo()) {
                    DataManager.obtain().setLocalVideoCount(DataManager.obtain().getLocalVideoCount() + ((long) count));
                } else {
                    DataManager.obtain().setLocalPhotoCount(DataManager.obtain().getLocalPhotoCount() + ((long) count));
                }
                if (DataManager.obtain().getLocalPhotoCount() == 0 && DataManager.obtain().getLocalVideoCount() == 0) {
                    DataManager.obtain().reLocalDefaultVaribale();
                    judgeIsNoData();
                }
            }
            Message message = Message.obtain();
            message.what = 11;
            message.arg1 = 0;
            this.mainHandler.sendMessage(message);
        }
    }

    public void addNewItem(T mediaModel) {
        if (mediaModel != null) {
            Message message = new Message();
            message.what = 6;
            message.obj = mediaModel;
            this.otherHandler.sendMessage(message);
        }
    }

    public void addItemReally(T mediaModel) {
        changeMediaModelState(mediaModel);
        int inserterPosition = 0;
        if (mediaModel != null) {
            int modelNoHeadListPosition = 0;
            Iterator it = this.modelNoHeadList.iterator();
            while (it.hasNext()) {
                if (mediaModel.getFormatDate().compareTo(((MediaModel) it.next()).getFormatDate()) > 0) {
                    break;
                }
                modelNoHeadListPosition++;
            }
            this.modelNoHeadList.add(modelNoHeadListPosition, mediaModel);
            statisticalFileCount(mediaModel, true);
            String currentFormatData = mediaModel.getFormatDate().split(" ")[0];
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
                    if (currentFormatData.compareTo((String) entry.getKey()) >= 0) {
                        break;
                    }
                    inserterPosition += ((CopyOnWriteArrayList) entry.getValue()).size();
                }
                if (this.modelList.size() == 0) {
                    MediaModel headviewModel = new MediaModel();
                    headviewModel.setHeadView(true);
                    this.modelList.add(0, headviewModel);
                    inserterPosition++;
                    if (this.isCamera) {
                        DataManager.obtain().setX9CameralLoad(true);
                    } else {
                        DataManager.obtain().setLocalLoad(true);
                    }
                } else {
                    inserterPosition++;
                }
                MediaModel mediaModel1 = new MediaModel();
                mediaModel1.setCategory(true);
                mediaModel1.setFormatDate(mediaModel.getFormatDate());
                newList.add(mediaModel1);
                newList.add(mediaModel);
                this.modelList.add(inserterPosition, mediaModel1);
                this.modelList.add(inserterPosition + 1, mediaModel);
                this.stateHashMap.put(currentFormatData, newList);
            }
        }
        Message message = Message.obtain();
        message.what = 7;
        message.arg1 = inserterPosition;
        message.obj = mediaModel;
        this.mainHandler.sendMessage(message);
    }

    private void changeMediaModelState(T mediaModel) {
        File file = new File(mediaModel.getFileLocalPath());
        if (file.exists()) {
            long createTime = file.lastModified();
            mediaModel.setCreateDate(createTime);
            mediaModel.setFormatDate(DateFormater.dateString(createTime, "yyyy.MM.dd HH:mm:ss"));
        }
        mediaModel.setSelect(false);
    }

    public int getItemViewType(int position) {
        if (isHeadView(position)) {
            return 48;
        }
        if (isCategoryType(position)) {
            return 16;
        }
        return 32;
    }

    private boolean isHeadView(int position) {
        if (this.modelList == null || this.modelList.size() <= 0 || position < 0 || !((MediaModel) this.modelList.get(position)).isHeadView()) {
            return false;
        }
        return true;
    }

    private boolean isCategoryType(int position) {
        if (this.modelList == null || this.modelList.size() <= 0 || position < 0 || !((MediaModel) this.modelList.get(position)).isCategory()) {
            return false;
        }
        return true;
    }

    public int getItemCount() {
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

    private void initData() {
        if (this.isCamera) {
            this.modelList = this.mdataManager.getX9CameraDataList();
            this.stateHashMap = this.mdataManager.getX9CameraDataHash();
            this.modelNoHeadList = this.mdataManager.getX9CameraDataNoHeadList();
            return;
        }
        this.modelList = this.mdataManager.getLocalDataList();
        this.stateHashMap = this.mdataManager.getDataHash();
        this.modelNoHeadList = this.mdataManager.getLocalDataNoHeadList();
    }

    public boolean handleMessage(Message message) {
        if (message.what == 5) {
            notifyDataSetChanged();
        } else if (message.what == 6) {
            addItemReally((MediaModel) message.obj);
        } else if (message.what == 7) {
            notifyAddNewItem(message.arg1);
            this.mINodataTip.notifyAddCallback((MediaModel) message.obj);
        } else if (message.what == 11) {
            notifyItemChanged(message.arg1);
        }
        return true;
    }

    private void notifyAddNewItem(int position) {
        notifyItemRangeInserted(position, this.modelList.size());
    }

    public void removeCallBack() {
    }
}
