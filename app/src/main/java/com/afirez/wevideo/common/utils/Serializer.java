package com.afirez.wevideo.common.utils;

/**
 * Created by afirez on 18-3-31.
 */

public interface Serializer {
    String serialize(Object obj);
    <T> T deserialize(String json, Class<T> clazz);
}
