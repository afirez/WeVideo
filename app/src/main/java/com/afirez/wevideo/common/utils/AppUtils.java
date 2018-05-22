package com.afirez.wevideo.common.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *
 * Created by afirez on 18-3-31.
 */

public class AppUtils {
    private static Application app;

    public static void init(Application app) {
        AppUtils.app = app;
    }

    public static boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && connectivityManager.getNetworkInfo(1) != null) {
            NetworkInfo.State state = connectivityManager.getNetworkInfo(1).getState();
            return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
        }
        return false;
    }
}
