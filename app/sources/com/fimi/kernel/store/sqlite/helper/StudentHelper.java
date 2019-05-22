package com.fimi.kernel.store.sqlite.helper;

import com.fimi.kernel.store.sqlite.entity.Student;
import com.fimi.kernel.store.sqlite.helper.core.BaseDbHelper;
import org.greenrobot.greendao.AbstractDao;

public class StudentHelper extends BaseDbHelper<Student, Long> {
    public StudentHelper(AbstractDao dao) {
        super(dao);
    }
}
