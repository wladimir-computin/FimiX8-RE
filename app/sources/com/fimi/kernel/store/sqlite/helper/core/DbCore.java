package com.fimi.kernel.store.sqlite.helper.core;

import android.content.Context;
import com.fimi.kernel.store.sqlite.dao.DaoMaster;
import com.fimi.kernel.store.sqlite.dao.DaoSession;

public class DbCore {
    private static String DB_NAME = null;
    private static final String DEFAULT_DB_NAME = "_sql.db";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static Context mContext;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
        DB_NAME = dbName;
    }

    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            daoMaster = new DaoMaster(new UpgradeOpenHelper(mContext, DB_NAME).getWritableDb());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
