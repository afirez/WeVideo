package com.afirez.wevideo;

import android.app.Application;
import android.content.Context;

import com.afirez.wevideo.common.AppHelper;
import com.afirez.wevideo.common.BaseApp;
import com.afirez.wevideo.common.utils.AppUtils;

/**
 * Created by afirez on 18-3-31.
 */

public class MainApp extends BaseApp {
    @Override
    public void init() {
        super.init();
        AppHelper.getInstance().addCallback(new MainApp.Callback());
    }

    public static class Callback implements AppHelper.Callback {

        @Override
        public void attachBaseContext(Application app, Context base) {

        }

        @Override
        public void onCreate(Application app) {
            AppUtils.init(app);
        }

        @Override
        public void onTerminate(Application app) {

        }
    }
}
