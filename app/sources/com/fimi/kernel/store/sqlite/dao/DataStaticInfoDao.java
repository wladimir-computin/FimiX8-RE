package com.fimi.kernel.store.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fimi.kernel.store.sqlite.entity.DataStaticInfo;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class DataStaticInfoDao extends AbstractDao<DataStaticInfo, Long> {
    public static final String TABLENAME = "DATA_STATIC_INFO";

    public static class Properties {
        public static final Property CurrentTime = new Property(1, String.class, "currentTime", false, "CURRENT_TIME");
        public static final Property DeviceType = new Property(6, Byte.TYPE, "deviceType", false, "DEVICE_TYPE");
        public static final Property FlyDistance = new Property(9, String.class, "flyDistance", false, "FLY_DISTANCE");
        public static final Property FlyHeight = new Property(10, String.class, "flyHeight", false, "FLY_HEIGHT");
        public static final Property FlyTime = new Property(2, String.class, "flyTime", false, "FLY_TIME");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Latitude = new Property(12, String.class, "latitude", false, "LATITUDE");
        public static final Property Longitude = new Property(11, String.class, "longitude", false, "LONGITUDE");
        public static final Property McuVersion = new Property(8, String.class, "mcuVersion", false, "MCU_VERSION");
        public static final Property SysVersion = new Property(7, String.class, "sysVersion", false, "SYS_VERSION");
        public static final Property Type = new Property(5, Byte.TYPE, "type", false, "TYPE");
        public static final Property UseTime = new Property(4, String.class, "useTime", false, "USE_TIME");
        public static final Property UserId = new Property(3, String.class, "userId", false, "USER_ID");
    }

    public DataStaticInfoDao(DaoConfig config) {
        super(config);
    }

    public DataStaticInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"DATA_STATIC_INFO\" (" + "\"_id\" INTEGER PRIMARY KEY ," + "\"CURRENT_TIME\" TEXT," + "\"FLY_TIME\" TEXT," + "\"USER_ID\" TEXT," + "\"USE_TIME\" TEXT," + "\"TYPE\" INTEGER NOT NULL ," + "\"DEVICE_TYPE\" INTEGER NOT NULL ," + "\"SYS_VERSION\" TEXT," + "\"MCU_VERSION\" TEXT," + "\"FLY_DISTANCE\" TEXT," + "\"FLY_HEIGHT\" TEXT," + "\"LONGITUDE\" TEXT," + "\"LATITUDE\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DATA_STATIC_INFO\"");
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(DatabaseStatement stmt, DataStaticInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String currentTime = entity.getCurrentTime();
        if (currentTime != null) {
            stmt.bindString(2, currentTime);
        }
        String flyTime = entity.getFlyTime();
        if (flyTime != null) {
            stmt.bindString(3, flyTime);
        }
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(4, userId);
        }
        String useTime = entity.getUseTime();
        if (useTime != null) {
            stmt.bindString(5, useTime);
        }
        stmt.bindLong(6, (long) entity.getType());
        stmt.bindLong(7, (long) entity.getDeviceType());
        String sysVersion = entity.getSysVersion();
        if (sysVersion != null) {
            stmt.bindString(8, sysVersion);
        }
        String mcuVersion = entity.getMcuVersion();
        if (mcuVersion != null) {
            stmt.bindString(9, mcuVersion);
        }
        String flyDistance = entity.getFlyDistance();
        if (flyDistance != null) {
            stmt.bindString(10, flyDistance);
        }
        String flyHeight = entity.getFlyHeight();
        if (flyHeight != null) {
            stmt.bindString(11, flyHeight);
        }
        String longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindString(12, longitude);
        }
        String latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindString(13, latitude);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(SQLiteStatement stmt, DataStaticInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String currentTime = entity.getCurrentTime();
        if (currentTime != null) {
            stmt.bindString(2, currentTime);
        }
        String flyTime = entity.getFlyTime();
        if (flyTime != null) {
            stmt.bindString(3, flyTime);
        }
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(4, userId);
        }
        String useTime = entity.getUseTime();
        if (useTime != null) {
            stmt.bindString(5, useTime);
        }
        stmt.bindLong(6, (long) entity.getType());
        stmt.bindLong(7, (long) entity.getDeviceType());
        String sysVersion = entity.getSysVersion();
        if (sysVersion != null) {
            stmt.bindString(8, sysVersion);
        }
        String mcuVersion = entity.getMcuVersion();
        if (mcuVersion != null) {
            stmt.bindString(9, mcuVersion);
        }
        String flyDistance = entity.getFlyDistance();
        if (flyDistance != null) {
            stmt.bindString(10, flyDistance);
        }
        String flyHeight = entity.getFlyHeight();
        if (flyHeight != null) {
            stmt.bindString(11, flyHeight);
        }
        String longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindString(12, longitude);
        }
        String latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindString(13, latitude);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public DataStaticInfo readEntity(Cursor cursor, int offset) {
        return new DataStaticInfo(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), (byte) cursor.getShort(offset + 5), (byte) cursor.getShort(offset + 6), cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
    }

    public void readEntity(Cursor cursor, DataStaticInfo entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setCurrentTime(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFlyTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUseTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setType((byte) cursor.getShort(offset + 5));
        entity.setDeviceType((byte) cursor.getShort(offset + 6));
        entity.setSysVersion(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMcuVersion(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFlyDistance(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFlyHeight(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLongitude(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        if (!cursor.isNull(offset + 12)) {
            str = cursor.getString(offset + 12);
        }
        entity.setLatitude(str);
    }

    /* Access modifiers changed, original: protected|final */
    public final Long updateKeyAfterInsert(DataStaticInfo entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(DataStaticInfo entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(DataStaticInfo entity) {
        return entity.getId() != null;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isEntityUpdateable() {
        return true;
    }
}
