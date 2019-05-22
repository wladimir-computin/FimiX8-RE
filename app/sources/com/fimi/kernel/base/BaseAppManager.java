package com.fimi.kernel.base;

import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

public class BaseAppManager {
    private static final String TAG = BaseAppManager.class.getSimpleName();
    private static BaseAppManager instance = null;
    private static List<Activity> mActivities = new LinkedList();

    public static BaseAppManager getInstance() {
        if (instance == null) {
            synchronized (BaseAppManager.class) {
                if (instance == null) {
                    instance = new BaseAppManager();
                }
            }
        }
        return instance;
    }

    public int size() {
        return mActivities.size();
    }

    public synchronized Activity getForwardActivity() {
        return size() > 0 ? (Activity) mActivities.get(size() - 1) : null;
    }

    public synchronized void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public synchronized void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    public synchronized void clear() {
        for (int i = mActivities.size() - 1; i > -1; i = mActivities.size() - 1) {
            Activity activity = (Activity) mActivities.get(i);
            removeActivity(activity);
            activity.finish();
        }
    }

    public synchronized void clearToTop() {
        for (int i = mActivities.size() - 2; i > -1; i = (mActivities.size() - 1) - 1) {
            Activity activity = (Activity) mActivities.get(i);
            removeActivity(activity);
            activity.finish();
        }
    }
}
