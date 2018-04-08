package com.afirez.wevideo.common.widget.recyclerview;

/**
 * Created by afirez on 18-3-27.
 */

public interface LoadMoreHelper {

    boolean isLoading();

    void setLoading(boolean isLoading);

    void loadMore();

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    OnLoadMoreListener getOnLoadMoreListener();

    interface OnLoadMoreListener {
        void onLoadMore(LoadMoreHelper helper);
    }
}
