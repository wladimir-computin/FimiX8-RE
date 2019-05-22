package com.fimi.kernel.store.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, 1);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, 1);
        }

        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version 1");
            DaoMaster.createAllTables(db, false);
        }
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            DaoMaster.dropAllTables(db, true);
            onCreate(db);
        }
    }

    public static void createAllTables(Database db, boolean ifNotExists) {
        DataStaticInfoDao.createTable(db, ifNotExists);
        GH2DataStaticInfoDao.createTable(db, ifNotExists);
        MediaDownloadInfoDao.createTable(db, ifNotExists);
        StudentDao.createTable(db, ifNotExists);
        X8AiLinePointInfoDao.createTable(db, ifNotExists);
        X8AiLinePointLatlngInfoDao.createTable(db, ifNotExists);
    }

    public static void dropAllTables(Database db, boolean ifExists) {
        DataStaticInfoDao.dropTable(db, ifExists);
        GH2DataStaticInfoDao.dropTable(db, ifExists);
        MediaDownloadInfoDao.dropTable(db, ifExists);
        StudentDao.dropTable(db, ifExists);
        X8AiLinePointInfoDao.dropTable(db, ifExists);
        X8AiLinePointLatlngInfoDao.dropTable(db, ifExists);
    }

    public static DaoSession newDevSession(Context context, String name) {
        return new DaoMaster(new DevOpenHelper(context, name).getWritableDb()).newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, 1);
        registerDaoClass(DataStaticInfoDao.class);
        registerDaoClass(GH2DataStaticInfoDao.class);
        registerDaoClass(MediaDownloadInfoDao.class);
        registerDaoClass(StudentDao.class);
        registerDaoClass(X8AiLinePointInfoDao.class);
        registerDaoClass(X8AiLinePointLatlngInfoDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(this.db, type, this.daoConfigMap);
    }
}
