package com.fimi.kernel.store.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.github.moduth.blockcanary.internal.BlockInfo;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class X8AiLinePointInfoDao extends AbstractDao<X8AiLinePointInfo, Long> {
    public static final String TABLENAME = "X8_AI_LINE_POINT_INFO";

    public static class Properties {
        public static final Property AutoRecord = new Property(12, Integer.TYPE, "autoRecord", false, "AUTO_RECORD");
        public static final Property DisconnectType = new Property(10, Integer.TYPE, "disconnectType", false, "DISCONNECT_TYPE");
        public static final Property Distance = new Property(6, Float.TYPE, "distance", false, "DISTANCE");
        public static final Property ExcuteEnd = new Property(11, Integer.TYPE, "excuteEnd", false, "EXCUTE_END");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property IsCurve = new Property(7, Integer.TYPE, "isCurve", false, "IS_CURVE");
        public static final Property Locality = new Property(13, String.class, "locality", false, "LOCALITY");
        public static final Property MapType = new Property(8, Integer.TYPE, "mapType", false, "MAP_TYPE");
        public static final Property Name = new Property(2, String.class, "name", false, "NAME");
        public static final Property RunByMapOrVedio = new Property(9, Integer.TYPE, "runByMapOrVedio", false, "RUN_BY_MAP_OR_VEDIO");
        public static final Property SaveFlag = new Property(5, Integer.TYPE, "saveFlag", false, "SAVE_FLAG");
        public static final Property Speed = new Property(4, Integer.TYPE, "speed", false, "SPEED");
        public static final Property Time = new Property(1, Long.TYPE, BlockInfo.KEY_TIME_COST, false, "TIME");
        public static final Property Type = new Property(3, Integer.TYPE, "type", false, "TYPE");
    }

    public X8AiLinePointInfoDao(DaoConfig config) {
        super(config);
    }

    public X8AiLinePointInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"X8_AI_LINE_POINT_INFO\" (" + "\"_id\" INTEGER PRIMARY KEY ," + "\"TIME\" INTEGER NOT NULL ," + "\"NAME\" TEXT," + "\"TYPE\" INTEGER NOT NULL ," + "\"SPEED\" INTEGER NOT NULL ," + "\"SAVE_FLAG\" INTEGER NOT NULL ," + "\"DISTANCE\" REAL NOT NULL ," + "\"IS_CURVE\" INTEGER NOT NULL ," + "\"MAP_TYPE\" INTEGER NOT NULL ," + "\"RUN_BY_MAP_OR_VEDIO\" INTEGER NOT NULL ," + "\"DISCONNECT_TYPE\" INTEGER NOT NULL ," + "\"EXCUTE_END\" INTEGER NOT NULL ," + "\"AUTO_RECORD\" INTEGER NOT NULL ," + "\"LOCALITY\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"X8_AI_LINE_POINT_INFO\"");
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(DatabaseStatement stmt, X8AiLinePointInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, entity.getTime());
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
        stmt.bindLong(4, (long) entity.getType());
        stmt.bindLong(5, (long) entity.getSpeed());
        stmt.bindLong(6, (long) entity.getSaveFlag());
        stmt.bindDouble(7, (double) entity.getDistance());
        stmt.bindLong(8, (long) entity.getIsCurve());
        stmt.bindLong(9, (long) entity.getMapType());
        stmt.bindLong(10, (long) entity.getRunByMapOrVedio());
        stmt.bindLong(11, (long) entity.getDisconnectType());
        stmt.bindLong(12, (long) entity.getExcuteEnd());
        stmt.bindLong(13, (long) entity.getAutoRecord());
        String locality = entity.getLocality();
        if (locality != null) {
            stmt.bindString(14, locality);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(SQLiteStatement stmt, X8AiLinePointInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, entity.getTime());
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
        stmt.bindLong(4, (long) entity.getType());
        stmt.bindLong(5, (long) entity.getSpeed());
        stmt.bindLong(6, (long) entity.getSaveFlag());
        stmt.bindDouble(7, (double) entity.getDistance());
        stmt.bindLong(8, (long) entity.getIsCurve());
        stmt.bindLong(9, (long) entity.getMapType());
        stmt.bindLong(10, (long) entity.getRunByMapOrVedio());
        stmt.bindLong(11, (long) entity.getDisconnectType());
        stmt.bindLong(12, (long) entity.getExcuteEnd());
        stmt.bindLong(13, (long) entity.getAutoRecord());
        String locality = entity.getLocality();
        if (locality != null) {
            stmt.bindString(14, locality);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public X8AiLinePointInfo readEntity(Cursor cursor, int offset) {
        String str;
        Long valueOf = cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
        long j = cursor.getLong(offset + 1);
        if (cursor.isNull(offset + 2)) {
            str = null;
        } else {
            str = cursor.getString(offset + 2);
        }
        return new X8AiLinePointInfo(valueOf, j, str, cursor.getInt(offset + 3), cursor.getInt(offset + 4), cursor.getInt(offset + 5), cursor.getFloat(offset + 6), cursor.getInt(offset + 7), cursor.getInt(offset + 8), cursor.getInt(offset + 9), cursor.getInt(offset + 10), cursor.getInt(offset + 11), cursor.getInt(offset + 12), cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
    }

    public void readEntity(Cursor cursor, X8AiLinePointInfo entity, int offset) {
        String str;
        String str2 = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setTime(cursor.getLong(offset + 1));
        if (cursor.isNull(offset + 2)) {
            str = null;
        } else {
            str = cursor.getString(offset + 2);
        }
        entity.setName(str);
        entity.setType(cursor.getInt(offset + 3));
        entity.setSpeed(cursor.getInt(offset + 4));
        entity.setSaveFlag(cursor.getInt(offset + 5));
        entity.setDistance(cursor.getFloat(offset + 6));
        entity.setIsCurve(cursor.getInt(offset + 7));
        entity.setMapType(cursor.getInt(offset + 8));
        entity.setRunByMapOrVedio(cursor.getInt(offset + 9));
        entity.setDisconnectType(cursor.getInt(offset + 10));
        entity.setExcuteEnd(cursor.getInt(offset + 11));
        entity.setAutoRecord(cursor.getInt(offset + 12));
        if (!cursor.isNull(offset + 13)) {
            str2 = cursor.getString(offset + 13);
        }
        entity.setLocality(str2);
    }

    /* Access modifiers changed, original: protected|final */
    public final Long updateKeyAfterInsert(X8AiLinePointInfo entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(X8AiLinePointInfo entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(X8AiLinePointInfo entity) {
        return entity.getId() != null;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isEntityUpdateable() {
        return true;
    }
}
