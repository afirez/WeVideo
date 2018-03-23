package com.afirez.wevideo.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by afirez on 18-3-23.
 */

public class Activities {

    private static volatile Activities instance;

    public static Activities getInstance() {
        if (instance == null) {
            synchronized (Activities.class) {
                if (instance == null) {
                    instance = new Activities();
                }
            }
        }
        return instance;
    }

    private ArrayList<Activity> activities = new ArrayList<>();

    public void add(Activity activity) {
        if (activity == null) {
            return;
        }
        activities.add(activity);
    }

    public void remove(Activity activity) {
        activities.remove(activity);
    }

    public void removeAll() {
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity == null) {
                continue;
            }
            if (activity.isFinishing() || activity.isDestroyed()) {
                iterator.remove();
                continue;
            }
            activity.finish();
            iterator.remove();
        }
    }
}
