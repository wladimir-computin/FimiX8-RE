package com.fimi.album.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IViewpaper;
import java.util.concurrent.CopyOnWriteArrayList;

public class MediaDetailViewPaperAdapter extends PagerAdapter {
    private IViewpaper mIViewpaper;
    private DataManager mdataManager = DataManager.obtain();
    private CopyOnWriteArrayList<? extends MediaModel> modelList;

    public MediaDetailViewPaperAdapter(IViewpaper mIViewpaper) {
        this.mIViewpaper = mIViewpaper;
        initData();
    }

    private void initData() {
        if (X9HandleType.isCameraView()) {
            this.modelList = this.mdataManager.getX9CameraDataNoHeadList();
        } else {
            this.modelList = this.mdataManager.getLocalDataNoHeadList();
        }
    }

    public int getCount() {
        return this.modelList == null ? 0 : this.modelList.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        if (this.mIViewpaper != null) {
            return this.mIViewpaper.instantiateItem(container, position);
        }
        return super.instantiateItem(container, position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void deleteItem(int position) {
        this.modelList.remove(position);
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
