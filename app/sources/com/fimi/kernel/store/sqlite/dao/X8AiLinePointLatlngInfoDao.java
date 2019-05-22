package com.fimi.kernel.store.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class X8AiLinePointLatlngInfoDao extends AbstractDao<X8AiLinePointLatlngInfo, Long> {
    public static final String TABLENAME = "X8_AI_LINE_POINT_LATLNG_INFO";

    public static class Properties {
        public static final Property Altitude = new Property(5, Integer.TYPE, "altitude", false, "ALTITUDE");
        public static final Property AltitudePOI = new Property(16, Integer.TYPE, "altitudePOI", false, "ALTITUDE_POI");
        public static final Property GimbalMode = new Property(10, Integer.TYPE, "gimbalMode", false, "GIMBAL_MODE");
        public static final Property GimbalPitch = new Property(7, Integer.TYPE, "gimbalPitch", false, "GIMBAL_PITCH");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Latitude = new Property(4, Double.TYPE, "latitude", false, "LATITUDE");
        public static final Property LatitudePOI = new Property(15, Double.TYPE, "latitudePOI", false, "LATITUDE_POI");
        public static final Property LineId = new Property(17, Long.TYPE, "lineId", false, "LINE_ID");
        public static final Property Longitude = new Property(3, Double.TYPE, "longitude", false, "LONGITUDE");
        public static final Property LongitudePOI = new Property(14, Double.TYPE, "longitudePOI", false, "LONGITUDE_POI");
        public static final Property MissionFinishAction = new Property(12, Integer.TYPE, "missionFinishAction", false, "MISSION_FINISH_ACTION");
        public static final Property Number = new Property(1, Integer.TYPE, "number", false, "NUMBER");
        public static final Property PointActionCmd = new Property(18, Integer.TYPE, "pointActionCmd", false, "POINT_ACTION_CMD");
        public static final Property RCLostAction = new Property(13, Integer.TYPE, "rCLostAction", false, "R_CLOST_ACTION");
        public static final Property Roration = new Property(19, Integer.TYPE, "roration", false, "RORATION");
        public static final Property Speed = new Property(8, Integer.TYPE, "speed", false, "SPEED");
        public static final Property Totalnumber = new Property(2, Integer.TYPE, "totalnumber", false, "TOTALNUMBER");
        public static final Property TrajectoryMode = new Property(11, Integer.TYPE, "trajectoryMode", false, "TRAJECTORY_MODE");
        public static final Property Yaw = new Property(6, Float.TYPE, "yaw", false, "YAW");
        public static final Property YawMode = new Property(9, Integer.TYPE, "yawMode", false, "YAW_MODE");
    }

    public X8AiLinePointLatlngInfoDao(DaoConfig config) {
        super(config);
    }

    public X8AiLinePointLatlngInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"X8_AI_LINE_POINT_LATLNG_INFO\" (" + "\"_id\" INTEGER PRIMARY KEY ," + "\"NUMBER\" INTEGER NOT NULL ," + "\"TOTALNUMBER\" INTEGER NOT NULL ," + "\"LONGITUDE\" REAL NOT NULL ," + "\"LATITUDE\" REAL NOT NULL ," + "\"ALTITUDE\" INTEGER NOT NULL ," + "\"YAW\" REAL NOT NULL ," + "\"GIMBAL_PITCH\" INTEGER NOT NULL ," + "\"SPEED\" INTEGER NOT NULL ," + "\"YAW_MODE\" INTEGER NOT NULL ," + "\"GIMBAL_MODE\" INTEGER NOT NULL ," + "\"TRAJECTORY_MODE\" INTEGER NOT NULL ," + "\"MISSION_FINISH_ACTION\" INTEGER NOT NULL ," + "\"R_CLOST_ACTION\" INTEGER NOT NULL ," + "\"LONGITUDE_POI\" REAL NOT NULL ," + "\"LATITUDE_POI\" REAL NOT NULL ," + "\"ALTITUDE_POI\" INTEGER NOT NULL ," + "\"LINE_ID\" INTEGER NOT NULL ," + "\"POINT_ACTION_CMD\" INTEGER NOT NULL ," + "\"RORATION\" INTEGER NOT NULL );");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"X8_AI_LINE_POINT_LATLNG_INFO\"");
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(DatabaseStatement stmt, X8AiLinePointLatlngInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, (long) entity.getNumber());
        stmt.bindLong(3, (long) entity.getTotalnumber());
        stmt.bindDouble(4, entity.getLongitude());
        stmt.bindDouble(5, entity.getLatitude());
        stmt.bindLong(6, (long) entity.getAltitude());
        stmt.bindDouble(7, (double) entity.getYaw());
        stmt.bindLong(8, (long) entity.getGimbalPitch());
        stmt.bindLong(9, (long) entity.getSpeed());
        stmt.bindLong(10, (long) entity.getYawMode());
        stmt.bindLong(11, (long) entity.getGimbalMode());
        stmt.bindLong(12, (long) entity.getTrajectoryMode());
        stmt.bindLong(13, (long) entity.getMissionFinishAction());
        stmt.bindLong(14, (long) entity.getRCLostAction());
        stmt.bindDouble(15, entity.getLongitudePOI());
        stmt.bindDouble(16, entity.getLatitudePOI());
        stmt.bindLong(17, (long) entity.getAltitudePOI());
        stmt.bindLong(18, entity.getLineId());
        stmt.bindLong(19, (long) entity.getPointActionCmd());
        stmt.bindLong(20, (long) entity.getRoration());
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(SQLiteStatement stmt, X8AiLinePointLatlngInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, (long) entity.getNumber());
        stmt.bindLong(3, (long) entity.getTotalnumber());
        stmt.bindDouble(4, entity.getLongitude());
        stmt.bindDouble(5, entity.getLatitude());
        stmt.bindLong(6, (long) entity.getAltitude());
        stmt.bindDouble(7, (double) entity.getYaw());
        stmt.bindLong(8, (long) entity.getGimbalPitch());
        stmt.bindLong(9, (long) entity.getSpeed());
        stmt.bindLong(10, (long) entity.getYawMode());
        stmt.bindLong(11, (long) entity.getGimbalMode());
        stmt.bindLong(12, (long) entity.getTrajectoryMode());
        stmt.bindLong(13, (long) entity.getMissionFinishAction());
        stmt.bindLong(14, (long) entity.getRCLostAction());
        stmt.bindDouble(15, entity.getLongitudePOI());
        stmt.bindDouble(16, entity.getLatitudePOI());
        stmt.bindLong(17, (long) entity.getAltitudePOI());
        stmt.bindLong(18, entity.getLineId());
        stmt.bindLong(19, (long) entity.getPointActionCmd());
        stmt.bindLong(20, (long) entity.getRoration());
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public X8AiLinePointLatlngInfo readEntity(Cursor cursor, int offset) {
        Long l;
        if (cursor.isNull(offset + 0)) {
            l = null;
        } else {
            l = Long.valueOf(cursor.getLong(offset + 0));
        }
        return new X8AiLinePointLatlngInfo(l, cursor.getInt(offset + 1), cursor.getInt(offset + 2), cursor.getDouble(offset + 3), cursor.getDouble(offset + 4), cursor.getInt(offset + 5), cursor.getFloat(offset + 6), cursor.getInt(offset + 7), cursor.getInt(offset + 8), cursor.getInt(offset + 9), cursor.getInt(offset + 10), cursor.getInt(offset + 11), cursor.getInt(offset + 12), cursor.getInt(offset + 13), cursor.getDouble(offset + 14), cursor.getDouble(offset + 15), cursor.getInt(offset + 16), cursor.getLong(offset + 17), cursor.getInt(offset + 18), cursor.getInt(offset + 19));
    }

    public void readEntity(Cursor cursor, X8AiLinePointLatlngInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setNumber(cursor.getInt(offset + 1));
        entity.setTotalnumber(cursor.getInt(offset + 2));
        entity.setLongitude(cursor.getDouble(offset + 3));
        entity.setLatitude(cursor.getDouble(offset + 4));
        entity.setAltitude(cursor.getInt(offset + 5));
        entity.setYaw(cursor.getFloat(offset + 6));
        entity.setGimbalPitch(cursor.getInt(offset + 7));
        entity.setSpeed(cursor.getInt(offset + 8));
        entity.setYawMode(cursor.getInt(offset + 9));
        entity.setGimbalMode(cursor.getInt(offset + 10));
        entity.setTrajectoryMode(cursor.getInt(offset + 11));
        entity.setMissionFinishAction(cursor.getInt(offset + 12));
        entity.setRCLostAction(cursor.getInt(offset + 13));
        entity.setLongitudePOI(cursor.getDouble(offset + 14));
        entity.setLatitudePOI(cursor.getDouble(offset + 15));
        entity.setAltitudePOI(cursor.getInt(offset + 16));
        entity.setLineId(cursor.getLong(offset + 17));
        entity.setPointActionCmd(cursor.getInt(offset + 18));
        entity.setRoration(cursor.getInt(offset + 19));
    }

    /* Access modifiers changed, original: protected|final */
    public final Long updateKeyAfterInsert(X8AiLinePointLatlngInfo entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(X8AiLinePointLatlngInfo entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(X8AiLinePointLatlngInfo entity) {
        return entity.getId() != null;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isEntityUpdateable() {
        return true;
    }
}
