package com.afirez.wevideo.common.widget.recyclerview;

/**
 * Created by afirez on 18-3-27.
 */

public interface LoadMoreHelper {

    boolean canLoadMore();

    void canLoadMore(boolean canLoadMore);

    boolean isLoading();

    void setLoading(boolean isLoading);

    void loadMore();

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    OnLoadMoreListener getOnLoadMoreListener();

    void onLoadComplete(int state);

    interface OnLoadMoreListener {
        void onLoadMore(LoadMoreHelper helper);
    }
}
