package com.afirez.wevideo.common;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by afirez on 18-3-31.
 */

public class AppHelper {

    private static volatile AppHelper sInstance;

    private Application app;

    public static AppHelper getInstance() {
        if (sInstance == null) {
            synchronized (AppHelper.class) {
                if (sInstance == null) {
                    sInstance = new AppHelper();
                }
            }
        }
        return sInstance;
    }

    public void init(Application app) {
        this.app = app;
    }

    public void attachBaseContext(Context base) {
        if (callbacks == null) {
            return;
        }
        Iterator<Callback> iterator = callbacks.iterator();
        Callback callback;
        while (iterator.hasNext()) {
            callback = iterator.next();
            if (callback == null) {
                iterator.remove();
                continue;
            }
            callback.attachBaseContext(app, base);
        }
    }

    public void onCreate() {
        if (callbacks == null) {
            return;
        }
        Iterator<Callback> iterator = callbacks.iterator();
        Callback callback;
        while (iterator.hasNext()) {
            callback = iterator.next();
            if (callback == null) {
                iterator.remove();
                continue;
            }
            callback.onCreate(app);
        }
    }

    public void onTerminate() {
        if (callbacks == null) {
            return;
        }
        Iterator<Callback> iterator = callbacks.iterator();
        Callback callback;
        while (iterator.hasNext()) {
            callback = iterator.next();
            if (callback == null) {
                iterator.remove();
                continue;
            }
            callback.onTerminate(app);
        }
    }

    public void addCallback(Callback callback) {
        if (callbacks != null
                && callback != null
                && !callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    public void removeCallback(Callback callback) {
        if (callbacks != null) {
            callbacks.remove(callback);
        }
    }

    private ArrayList<Callback> callbacks = new ArrayList<>();

    public interface Callback {

        void attachBaseContext(Application app, Context base);

        void onCreate(Application app);

        void onTerminate(Application app);
    }
}
