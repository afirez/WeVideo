package com.afirez.wevideo.common.widget.recyclerview;

import android.view.View;

/**
 * Created by afirez on 18-3-29.
 */

public abstract class LoadMoreHolder extends RecyclerHolder implements LoadMoreHelper {

    public LoadMoreHolder(View itemView) {
        super(itemView);
    }

    private boolean canLoadMore;

    private boolean isLoading;

    private OnLoadMoreListener onLoadMoreListener;

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public void loadMore() {
//        setLoading(true);
//        OnLoadMoreListener onLoadMoreListener = getOnLoadMoreListener();
//        if (onLoadMoreListener != null) {
//            onLoadMoreListener.onLoadMore(this);
//        } else {
//            setLoading(false);
//        }
        OnLoadMoreListener onLoadMoreListener = getOnLoadMoreListener();
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore(this);
        }
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

}