package com.fimi.kernel.store.sqlite.helper.core;

import android.content.Context;
import com.fimi.kernel.store.sqlite.dao.DaoMaster.OpenHelper;
import com.fimi.kernel.store.sqlite.dao.StudentDao;
import org.greenrobot.greendao.database.Database;

public class UpgradeOpenHelper extends OpenHelper {
    public UpgradeOpenHelper(Context context, String name) {
        super(context, name);
    }

    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.getInstance().migrate(db, StudentDao.class);
    }
}
