package com.afirez.wevideo.common.utils;

import com.google.gson.Gson;

/**
 * Created by afirez on 18-3-31.
 */

public class GsonSerializer implements Serializer {

    private static volatile GsonSerializer sInstance;

    public static GsonSerializer getInstance() {
        if (sInstance == null) {
            synchronized (GsonSerializer.class) {
                if (sInstance == null) {
                    sInstance = new GsonSerializer();
                }
            }
        }
        return sInstance;
    }

    private Gson gson = new Gson();

    public Gson getGson() {
        return gson;
    }

    @Override
    public String serialize(Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
