package com.fimi.kernel.store.sqlite.helper;

import com.fimi.kernel.store.sqlite.dao.X8AiLinePointInfoDao;
import com.fimi.kernel.store.sqlite.dao.X8AiLinePointInfoDao.Properties;
import com.fimi.kernel.store.sqlite.dao.X8AiLinePointLatlngInfoDao;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.kernel.store.sqlite.helper.core.DbCore;
import java.util.List;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

public class X8AiLinePointInfoHelper {
    private static X8AiLinePointInfoHelper instance = new X8AiLinePointInfoHelper();
    private X8AiLinePointInfoDao lineDao = DbCore.getDaoSession().getX8AiLinePointInfoDao();
    private X8AiLinePointLatlngInfoDao pointDao = DbCore.getDaoSession().getX8AiLinePointLatlngInfoDao();

    public static X8AiLinePointInfoHelper getIntance() {
        return instance;
    }

    public boolean addLineDatas(X8AiLinePointInfo line, List<X8AiLinePointLatlngInfo> list) {
        boolean ret = false;
        Database db = this.lineDao.getSession().getDatabase();
        db.beginTransaction();
        try {
            long lineId = this.lineDao.insert(line);
            for (int i = 0; i < list.size(); i++) {
                X8AiLinePointLatlngInfo latlng = (X8AiLinePointLatlngInfo) list.get(i);
                latlng.setLineId(lineId);
                this.pointDao.insert(latlng);
            }
            db.setTransactionSuccessful();
            ret = true;
            return ret;
        } finally {
            db.endTransaction();
        }
    }

    public List<X8AiLinePointInfo> getLastItem(int count, int mapType, boolean isFavorites) {
        QueryBuilder<X8AiLinePointInfo> qb = this.lineDao.queryBuilder();
        if (isFavorites) {
            WhereCondition where = Properties.MapType.eq(Integer.valueOf(mapType));
            WhereCondition where1 = Properties.SaveFlag.eq(Integer.valueOf(1));
            qb.where(where, where1).build();
        } else {
            qb.where(Properties.MapType.eq(Integer.valueOf(mapType)), new WhereCondition[0]);
        }
        qb.orderDesc(Properties.Id);
        qb.limit(count);
        return qb.list();
    }

    public List<X8AiLinePointInfo> getLastItem(int mapType) {
        return getLastItem(5, mapType, false);
    }

    public List<X8AiLinePointInfo> getLastItem(int mapType, boolean isFavorites, int count) {
        return getLastItem(count, mapType, isFavorites);
    }

    public X8AiLinePointInfo getLineInfoById(long lineId) {
        QueryBuilder<X8AiLinePointInfo> qb = this.lineDao.queryBuilder();
        qb.where(Properties.Id.eq(Long.valueOf(lineId)), new WhereCondition[0]);
        qb.orderDesc(Properties.Id);
        return (X8AiLinePointInfo) qb.list().get(0);
    }

    public List<X8AiLinePointInfo> getAll() {
        return this.lineDao.queryBuilder().list();
    }

    public List<X8AiLinePointLatlngInfo> getLatlngByLineId(int mapType, long lineId) {
        QueryBuilder<X8AiLinePointLatlngInfo> qb = this.pointDao.queryBuilder();
        qb.where(X8AiLinePointLatlngInfoDao.Properties.LineId.eq(Long.valueOf(lineId)), new WhereCondition[0]);
        return qb.list();
    }

    public void updatelineSaveFlag(int saveFlag, long lineId) {
        X8AiLinePointInfo find = (X8AiLinePointInfo) this.lineDao.queryBuilder().where(Properties.Id.eq(Long.valueOf(lineId)), new WhereCondition[0]).build().unique();
        if (find != null) {
            find.setSaveFlag(saveFlag);
            this.lineDao.update(find);
        }
    }

    public void updateLineName(String name, long lineId) {
        X8AiLinePointInfo find = (X8AiLinePointInfo) this.lineDao.queryBuilder().where(Properties.Id.eq(Long.valueOf(lineId)), new WhereCondition[0]).build().unique();
        if (find != null) {
            find.setName(name);
            this.lineDao.update(find);
        }
    }
}
