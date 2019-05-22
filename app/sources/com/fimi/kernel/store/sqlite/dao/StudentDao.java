package com.fimi.kernel.store.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fimi.kernel.store.sqlite.entity.Student;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class StudentDao extends AbstractDao<Student, Long> {
    public static final String TABLENAME = "STUDENT";

    public static class Properties {
        public static final Property Age = new Property(2, String.class, "age", false, "AGE");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Name = new Property(1, String.class, "name", false, "NAME");
        public static final Property Number = new Property(3, String.class, "number", false, "NUMBER");
    }

    public StudentDao(DaoConfig config) {
        super(config);
    }

    public StudentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"STUDENT\" (" + "\"_id\" INTEGER PRIMARY KEY ," + "\"NAME\" TEXT," + "\"AGE\" TEXT," + "\"NUMBER\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STUDENT\"");
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(DatabaseStatement stmt, Student entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(3, age);
        }
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(SQLiteStatement stmt, Student entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(3, age);
        }
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public Student readEntity(Cursor cursor, int offset) {
        String str = null;
        Long valueOf = cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
        String string = cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
        String string2 = cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2);
        if (!cursor.isNull(offset + 3)) {
            str = cursor.getString(offset + 3);
        }
        return new Student(valueOf, string, string2, str);
    }

    public void readEntity(Cursor cursor, Student entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAge(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        if (!cursor.isNull(offset + 3)) {
            str = cursor.getString(offset + 3);
        }
        entity.setNumber(str);
    }

    /* Access modifiers changed, original: protected|final */
    public final Long updateKeyAfterInsert(Student entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(Student entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(Student entity) {
        return entity.getId() != null;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isEntityUpdateable() {
        return true;
    }
}
