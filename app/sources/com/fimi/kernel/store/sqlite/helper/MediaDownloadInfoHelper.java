package com.fimi.kernel.store.sqlite.helper;

import com.fimi.kernel.store.sqlite.dao.MediaDownloadInfoDao;
import com.fimi.kernel.store.sqlite.dao.MediaDownloadInfoDao.Properties;
import com.fimi.kernel.store.sqlite.entity.MediaDownloadInfo;
import com.fimi.kernel.store.sqlite.helper.core.DbCore;
import java.util.List;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

public class MediaDownloadInfoHelper {
    private static MediaDownloadInfoHelper mMediaDownloadInfoHelper = new MediaDownloadInfoHelper();
    private MediaDownloadInfoDao dao = DbCore.getDaoSession().getMediaDownloadInfoDao();

    public static MediaDownloadInfoHelper getIntance() {
        return mMediaDownloadInfoHelper;
    }

    public void addMediaDownloadInfo(MediaDownloadInfo info) {
        this.dao.insert(info);
    }

    public void updateMediaDownloadInfo(String url, MediaDownloadInfo info) {
        MediaDownloadInfo findUser = (MediaDownloadInfo) this.dao.queryBuilder().where(Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            findUser.setUrl(info.getUrl());
            findUser.setCompeleteZize(info.getCompeleteZize());
            findUser.setEndPos(info.getEndPos());
            findUser.setStartPos(info.getStartPos());
            this.dao.update(findUser);
        }
    }

    public void deleteMediaDownloadInfo(String url) {
        MediaDownloadInfo findUser = (MediaDownloadInfo) this.dao.queryBuilder().where(Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            this.dao.deleteByKey(findUser.getId());
        }
    }

    public MediaDownloadInfo queryMediaDownloadInfo(String url) {
        return (MediaDownloadInfo) this.dao.queryBuilder().where(Properties.Url.eq(url), new WhereCondition[0]).build().unique();
    }

    public void deleteByUrl(String url) {
        this.dao.getDatabase().execSQL("delete from MEDIA_DOWNLOAD_INFO where URL='" + url + "'");
    }

    public void deleteAll() {
        this.dao.deleteAll();
    }

    private void delete(String url) {
        MediaDownloadInfo findUser = (MediaDownloadInfo) this.dao.queryBuilder().where(Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            this.dao.deleteByKey(findUser.getId());
        }
    }

    private void update(String url, MediaDownloadInfo info) {
        MediaDownloadInfo findUser = (MediaDownloadInfo) this.dao.queryBuilder().where(Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            findUser.setUrl(info.getUrl());
            findUser.setCompeleteZize(info.getCompeleteZize());
            findUser.setEndPos(info.getEndPos());
            findUser.setStartPos(info.getStartPos());
            this.dao.update(findUser);
        }
    }

    private List<MediaDownloadInfo> queryAllList() {
        return this.dao.queryBuilder().list();
    }

    private List<MediaDownloadInfo> queryList(String url) {
        QueryBuilder<MediaDownloadInfo> qb = this.dao.queryBuilder();
        qb.where(Properties.Url.eq(url), new WhereCondition[0]);
        return qb.list();
    }

    public void test() {
        queryAllList();
    }

    private void add() {
        MediaDownloadInfo st = new MediaDownloadInfo();
        st.setUrl("ddd");
        st.setCompeleteZize(5);
        this.dao.insert(st);
    }
}
