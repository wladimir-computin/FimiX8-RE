package com.fimi.kernel.store.shared;

import java.util.List;

public interface IKeyValueStoreManager extends IStoreManager {
    boolean contain(String str);

    boolean getBoolean(String str);

    int getInt(String str);

    int getInt(String str, int i);

    List getListObject(String str, Class<?> cls);

    long getLong(String str);

    <T> T getObject(String str, Class<?> cls);

    String getString(String str);

    void removeKey(String str);

    void saveBoolean(String str, boolean z);

    void saveInt(String str, int i);

    <T> void saveListObject(String str, List<T> list);

    void saveLong(String str, long j);

    void saveObject(String str, Object obj);

    void saveString(String str, String str2);
}
