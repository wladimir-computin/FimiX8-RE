package com.fimi.kernel.store.sqlite.helper;

import com.fimi.app.x8s.entity.X11CmdConstants;
import com.fimi.kernel.store.sqlite.dao.DataStaticInfoDao;
import com.fimi.kernel.store.sqlite.dao.DataStaticInfoDao.Properties;
import com.fimi.kernel.store.sqlite.entity.DataStaticInfo;
import com.fimi.kernel.store.sqlite.helper.core.DbCore;
import com.fimi.kernel.utils.LogUtil;
import java.util.List;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

public class DataStasicInfoHelper {
    private static DataStasicInfoHelper sDataStasicInfoHelper = new DataStasicInfoHelper();
    private DataStaticInfoDao dao = DbCore.getDaoSession().getDataStaticInfoDao();

    public static DataStasicInfoHelper getInstance() {
        return sDataStasicInfoHelper;
    }

    public void addRecord(DataStaticInfo dataStaticInfo) {
        DataStaticInfo unique = (DataStaticInfo) this.dao.queryBuilder().where(Properties.CurrentTime.eq(dataStaticInfo.getCurrentTime()), new WhereCondition[0]).where(Properties.Type.eq(Byte.valueOf(dataStaticInfo.getType())), new WhereCondition[0]).where(Properties.DeviceType.eq(Byte.valueOf(dataStaticInfo.getDeviceType())), new WhereCondition[0]).build().unique();
        if (unique != null) {
            LogUtil.i(X11CmdConstants.OPTION_APPSTATUS_RECORD, "addRecord: 1");
            unique.setUseTime(dataStaticInfo.getUseTime());
            unique.setFlyTime(dataStaticInfo.getFlyTime());
            this.dao.update(unique);
            return;
        }
        LogUtil.i(X11CmdConstants.OPTION_APPSTATUS_RECORD, "addRecord: 2");
        this.dao.insert(dataStaticInfo);
    }

    public List<DataStaticInfo> queryX9FlyTime() {
        QueryBuilder<DataStaticInfo> qb = this.dao.queryBuilder();
        qb.where(Properties.Type.eq(Integer.valueOf(1)), new WhereCondition[0]).where(Properties.DeviceType.eq(Integer.valueOf(0)), new WhereCondition[0]);
        return qb.list();
    }

    public List<DataStaticInfo> queryX9UseTime() {
        QueryBuilder<DataStaticInfo> qb = this.dao.queryBuilder();
        qb.where(Properties.Type.eq(Integer.valueOf(0)), new WhereCondition[0]).where(Properties.DeviceType.eq(Integer.valueOf(0)), new WhereCondition[0]);
        return qb.list();
    }

    public void deleteList(List<DataStaticInfo> notes) {
        this.dao.deleteInTx((Iterable) notes);
    }
}
