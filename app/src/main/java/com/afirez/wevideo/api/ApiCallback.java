package com.afirez.wevideo.api;

import com.afirez.wevideo.channel.model.ErrorInfo;

/**
 * Created by afirez on 18-3-31.
 */

public interface ApiCallback<T> {
    void onSuccess(T data);
    void onError(ErrorInfo info);
}
