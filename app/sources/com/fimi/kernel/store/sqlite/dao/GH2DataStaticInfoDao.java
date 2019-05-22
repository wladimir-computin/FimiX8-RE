package com.fimi.kernel.store.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fimi.kernel.store.sqlite.entity.GH2DataStaticInfo;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class GH2DataStaticInfoDao extends AbstractDao<GH2DataStaticInfo, Long> {
    public static final String TABLENAME = "GH2_DATA_STATIC_INFO";

    public static class Properties {
        public static final Property CreateTime = new Property(7, String.class, "createTime", false, "CREATE_TIME");
        public static final Property GimbalVersion = new Property(3, Integer.TYPE, "gimbalVersion", false, "GIMBAL_VERSION");
        public static final Property HandleVersion = new Property(2, Integer.TYPE, "handleVersion", false, "HANDLE_VERSION");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Latitude = new Property(6, Double.TYPE, "latitude", false, "LATITUDE");
        public static final Property Longitude = new Property(5, Double.TYPE, "longitude", false, "LONGITUDE");
        public static final Property ProductModel = new Property(1, String.class, "productModel", false, "PRODUCT_MODEL");
        public static final Property UseTime = new Property(4, Double.TYPE, "useTime", false, "USE_TIME");
    }

    public GH2DataStaticInfoDao(DaoConfig config) {
        super(config);
    }

    public GH2DataStaticInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"GH2_DATA_STATIC_INFO\" (" + "\"_id\" INTEGER PRIMARY KEY ," + "\"PRODUCT_MODEL\" TEXT," + "\"HANDLE_VERSION\" INTEGER NOT NULL ," + "\"GIMBAL_VERSION\" INTEGER NOT NULL ," + "\"USE_TIME\" REAL NOT NULL ," + "\"LONGITUDE\" REAL NOT NULL ," + "\"LATITUDE\" REAL NOT NULL ," + "\"CREATE_TIME\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GH2_DATA_STATIC_INFO\"");
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(DatabaseStatement stmt, GH2DataStaticInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String productModel = entity.getProductModel();
        if (productModel != null) {
            stmt.bindString(2, productModel);
        }
        stmt.bindLong(3, (long) entity.getHandleVersion());
        stmt.bindLong(4, (long) entity.getGimbalVersion());
        stmt.bindDouble(5, entity.getUseTime());
        stmt.bindDouble(6, entity.getLongitude());
        stmt.bindDouble(7, entity.getLatitude());
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(8, createTime);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(SQLiteStatement stmt, GH2DataStaticInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String productModel = entity.getProductModel();
        if (productModel != null) {
            stmt.bindString(2, productModel);
        }
        stmt.bindLong(3, (long) entity.getHandleVersion());
        stmt.bindLong(4, (long) entity.getGimbalVersion());
        stmt.bindDouble(5, entity.getUseTime());
        stmt.bindDouble(6, entity.getLongitude());
        stmt.bindDouble(7, entity.getLatitude());
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(8, createTime);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public GH2DataStaticInfo readEntity(Cursor cursor, int offset) {
        return new GH2DataStaticInfo(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), cursor.getInt(offset + 2), cursor.getInt(offset + 3), cursor.getDouble(offset + 4), cursor.getDouble(offset + 5), cursor.getDouble(offset + 6), cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
    }

    public void readEntity(Cursor cursor, GH2DataStaticInfo entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setProductModel(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHandleVersion(cursor.getInt(offset + 2));
        entity.setGimbalVersion(cursor.getInt(offset + 3));
        entity.setUseTime(cursor.getDouble(offset + 4));
        entity.setLongitude(cursor.getDouble(offset + 5));
        entity.setLatitude(cursor.getDouble(offset + 6));
        if (!cursor.isNull(offset + 7)) {
            str = cursor.getString(offset + 7);
        }
        entity.setCreateTime(str);
    }

    /* Access modifiers changed, original: protected|final */
    public final Long updateKeyAfterInsert(GH2DataStaticInfo entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(GH2DataStaticInfo entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(GH2DataStaticInfo entity) {
        return entity.getId() != null;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isEntityUpdateable() {
        return true;
    }
}
