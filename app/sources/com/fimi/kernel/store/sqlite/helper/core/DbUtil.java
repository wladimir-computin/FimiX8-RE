package com.fimi.kernel.store.sqlite.helper.core;

import com.fimi.kernel.store.sqlite.dao.StudentDao;
import com.fimi.kernel.store.sqlite.helper.StudentHelper;

public class DbUtil {
    private static StudentHelper sStudentHelper;

    private static StudentDao getDriverDao() {
        return DbCore.getDaoSession().getStudentDao();
    }

    public static StudentHelper getDriverHelper() {
        if (sStudentHelper == null) {
            sStudentHelper = new StudentHelper(getDriverDao());
        }
        return sStudentHelper;
    }
}
