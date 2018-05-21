package com.afirez.wevideo.common;

import android.app.Application;
import android.content.Context;

import com.afirez.wevideo.common.utils.AppUtils;

/**
 * Created by afirez on 18-3-31.
 */

public class BaseApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        init();
        AppHelper.getInstance().init(this);
        AppHelper.getInstance().attachBaseContext(base);
    }

    public void init() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppHelper.getInstance().onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppHelper.getInstance().onTerminate();
    }
}
