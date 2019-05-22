package com.fimi.kernel.store.shared;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.alibaba.fastjson.JSON;
import com.fimi.kernel.security.SharePrefernceSec;
import java.util.List;

public class SPStoreManager implements IKeyValueStoreManager {
    private static SPStoreManager manager = null;
    private SharedPreferences settings;

    public static synchronized SPStoreManager getInstance(String key_storeName) {
        SPStoreManager sPStoreManager;
        synchronized (SPStoreManager.class) {
            if (manager == null) {
                manager = new SPStoreManager(key_storeName);
            }
            sPStoreManager = manager;
        }
        return sPStoreManager;
    }

    public static synchronized SPStoreManager getInstance() {
        SPStoreManager sPStoreManager;
        synchronized (SPStoreManager.class) {
            if (manager == null) {
                manager = new SPStoreManager();
            }
            sPStoreManager = manager;
        }
        return sPStoreManager;
    }

    private SPStoreManager(String key_storeName) {
        this.settings = SharePrefernceSec.getSharedPreferences(key_storeName);
    }

    private SPStoreManager() {
        this.settings = SharePrefernceSec.getSharedPreferences();
    }

    public void saveObject(String key, Object obj) {
        Editor edit = this.settings.edit();
        edit.putString(key, JSON.toJSONString(obj));
        edit.commit();
    }

    public <T> T getObject(String key, Class<?> classItem) {
        try {
            String str = this.settings.getString(key, null);
            if (str != null) {
                return JSON.parseObject(str, (Class) classItem);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getString(String key) {
        return this.settings.getString(key, null);
    }

    public String getString(String key, String defValue) {
        return this.settings.getString(key, defValue);
    }

    public void saveString(String key, String value) {
        Editor edit = this.settings.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public int getInt(String key) {
        return this.settings.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return this.settings.getInt(key, defaultValue);
    }

    public void saveInt(String key, int value) {
        Editor edit = this.settings.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void saveLong(String key, long value) {
        Editor edit = this.settings.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public long getLong(String key) {
        return this.settings.getLong(key, 0);
    }

    public boolean getBoolean(String key) {
        return this.settings.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return this.settings.getBoolean(key, def);
    }

    public void saveBoolean(String key, boolean value) {
        Editor edit = this.settings.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public List getListObject(String key, Class<?> classItem) {
        try {
            String str = this.settings.getString(key, null);
            if (str != null) {
                return JSON.parseArray(str, (Class) classItem);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void removeKey(String key) {
        this.settings.edit().remove(key).commit();
    }

    public boolean contain(String key) {
        if (this.settings.contains(key)) {
            return true;
        }
        return false;
    }

    public <T> void saveListObject(String key, List<T> list) {
        Editor edit = this.settings.edit();
        edit.putString(key, JSON.toJSONString(list));
        edit.commit();
    }
}
