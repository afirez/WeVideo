package com.afirez.wevideo.common.widget.recyclerview;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afirez.wevideo.R;

import java.lang.reflect.Constructor;

/**
 * Created by afirez on 18-3-27.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_UNKNOWN_ERROR = 2;
    public static final int STATE_NETWORK_ERROR = 3;
    public static final int STATE_EMPTY_DATA = 4;

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private RecyclerView.Adapter<? extends RecyclerHolder> adapter;

    public RecyclerView.Adapter<? extends RecyclerHolder> getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter<? extends RecyclerHolder> adapter) {
        this.adapter = adapter;
        state = STATE_NORMAL;
    }

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(RecyclerView.Adapter<? extends RecyclerHolder> adapter) {
        registerLoadMoreHolder(R.layout.wv_common_item_load_more, MyLoadMoreHolder.class);
        registerEmptyDataHolder(R.layout.wv_common_item_empty_data, MyEmptyDataHolder.class);
        registerNetworkErrorHolder(R.layout.wv_common_item_network_error, MyNetworkErrorHolder.class);
        registerUnknownErrorHolder(R.layout.wv_common_item_unknown_error, MyUnknownErrorHolder.class);
        setAdapter(adapter);
    }

    private int loadMoreLayoutId;
    private int unknownErrorLayoutId;
    private int networkErrorLayoutId;
    private int emptyDataLayoutId;

    private Class<? extends RecyclerHolder> class_LoadMoreHolder;
    private Class<? extends RecyclerHolder> class_UnknownErrorHolder;
    private Class<? extends RecyclerHolder> class_NetworkErrorHolder;
    private Class<? extends RecyclerHolder> class_EmptyDataHolder;


    public void registerLoadMoreHolder(@LayoutRes int id, Class<? extends RecyclerHolder> clazz) {
        loadMoreLayoutId = id;
        class_LoadMoreHolder = clazz;
    }

    public void registerUnknownErrorHolder(@LayoutRes int id, Class<? extends RecyclerHolder> clazz) {
        unknownErrorLayoutId = id;
        class_UnknownErrorHolder = clazz;
    }

    public void registerNetworkErrorHolder(@LayoutRes int id, Class<? extends RecyclerHolder> clazz) {
        networkErrorLayoutId = id;
        class_NetworkErrorHolder = clazz;
    }

    public void registerEmptyDataHolder(@LayoutRes int id, Class<? extends RecyclerHolder> clazz) {
        emptyDataLayoutId = id;
        class_EmptyDataHolder = clazz;
    }


    @Override
    public int getItemViewType(int position) {
        if (STATE_UNKNOWN_ERROR == state) {
            return unknownErrorLayoutId;
        }
        if (STATE_NETWORK_ERROR == state) {
            return networkErrorLayoutId;
        }
        if (STATE_EMPTY_DATA == state || adapter == null) {
            return emptyDataLayoutId;
        }
        if (position >= adapter.getItemCount()) {
            return loadMoreLayoutId;
        }
        return adapter.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerHolder holder;
        Class<? extends RecyclerHolder> clazz = null;
        if (viewType == loadMoreLayoutId) {
            clazz = class_LoadMoreHolder;
        } else if (viewType == emptyDataLayoutId) {
            clazz = class_EmptyDataHolder;
        } else if (viewType == networkErrorLayoutId) {
            clazz = class_NetworkErrorHolder;
        } else if (viewType == unknownErrorLayoutId) {
            clazz = class_UnknownErrorHolder;
        }
        if (clazz != null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(viewType, parent, false);
            try {
                Constructor<? extends RecyclerHolder> constructor =
                        clazz.getConstructor(View.class);
                holder = constructor.newInstance(view);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new IllegalStateException("Illegal Holder: " + clazz);
            }
        } else {
            holder = adapter.onCreateViewHolder(parent, viewType);
        }
        onViewHolderCreated(holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(RecyclerHolder holder, int viewType) {
        if (holder instanceof LoadMoreHelper) {
            loadMoreHelper = (LoadMoreHelper) holder;
            loadMoreHelper.setOnLoadMoreListener(onLoadMoreListener);
        }
        if (onViewHolderCreatedListener != null) {
            onViewHolderCreatedListener.onViewHolderCreated(holder, viewType);
        }
    }

    private OnViewHolderCreatedListener onViewHolderCreatedListener;

    public void setOnViewHolderCreatedListener(OnViewHolderCreatedListener onViewHolderCreatedListener) {
        this.onViewHolderCreatedListener = onViewHolderCreatedListener;
    }

    public interface OnViewHolderCreatedListener {
        void onViewHolderCreated(RecyclerHolder holder, int viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        if (onBindViewHolderListener != null) {
            onBindViewHolderListener.onBindViewHolder(holder, position);
        }
        holder.onBind(position);
    }

    private OnBindViewHolderListener onBindViewHolderListener;

    public void setOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener) {
        this.onBindViewHolderListener = onBindViewHolderListener;
    }

    public interface OnBindViewHolderListener {
        void onBindViewHolder(RecyclerHolder holder, int position);
    }

    @Override
    public int getItemCount() {
        if (state == STATE_UNKNOWN_ERROR
                || state == STATE_NETWORK_ERROR
                || state == STATE_EMPTY_DATA) {
            return 1;
        }
        if (adapter == null || adapter.getItemCount() == 0) {
            state = STATE_EMPTY_DATA;
            return 1;
        }
        return adapter.getItemCount() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(loadMoreOnScrollListener);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager glm = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = glm.getSpanSizeLookup();
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (state == STATE_NETWORK_ERROR
                            || state == STATE_UNKNOWN_ERROR
                            || state == STATE_EMPTY_DATA
                            || adapter == null
                            || adapter.getItemCount() == 0
                            || position == adapter.getItemCount()) {
                        return glm.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(loadMoreOnScrollListener);
    }

    private LoadMoreHelper loadMoreHelper;

    public LoadMoreHelper getLoadMoreHelper() {
        return loadMoreHelper;
    }

    public void loadMore() {
        if (state == STATE_NORMAL
                && onLoadMoreListener != null
                && loadMoreHelper != null) {
            loadMoreHelper.loadMore();
        }
    }

    private LoadMoreHelper.OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(LoadMoreHelper.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        if (loadMoreHelper != null) {
            loadMoreHelper.setOnLoadMoreListener(onLoadMoreListener);
        }
    }


    private RecyclerView.OnScrollListener loadMoreOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastCompletelyVisibleItemPosition;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
//            if (loadMoreHelper == null) {
//                return;
//                throw new IllegalStateException("MyLoadMoreHolder must implement LoadMoreHelper");
//            }
            if (state == STATE_NORMAL
                    && dy > 0
                    && loadMoreHelper != null
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastCompletelyVisibleItemPosition == adapter.getItemCount()) {
                loadMoreHelper.loadMore();
            }
        }

        private int dy;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            this.dy = dy;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
                lastCompletelyVisibleItemPosition = llm.findLastCompletelyVisibleItemPosition();
            }
        }
    };
}
