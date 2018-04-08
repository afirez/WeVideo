package com.afirez.wevideo.common.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by afirez on 18-3-31.
 */

public class OkHttpUtils {

    public static final String REQUEST_TAG = "okhttp";

    public static Request buildRequest(String url) {
        if (AppUtils.isNetWorkAvailable()) {
            return new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .build();
        }
        return null;
    }

    public static Call execute(String url, Callback callback) {
        Request request = buildRequest(url);
        return enqueue(request, callback);
    }

    public static Call enqueue(Request request, Callback callback) {
        Call call = client().newCall(request);
        call.enqueue(callback);
        return call;
    }

    private static volatile OkHttpClient client;

    public static OkHttpClient client() {
        if (client == null) {
            synchronized (OkHttpUtils.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }
}
