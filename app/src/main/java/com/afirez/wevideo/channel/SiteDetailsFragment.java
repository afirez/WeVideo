package com.afirez.wevideo.channel;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afirez.wevideo.R;
import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.channel.model.Site;
import com.afirez.wevideo.common.BaseFragment;
import com.afirez.wevideo.common.widget.recyclerview.LoadMoreHelper;
import com.afirez.wevideo.common.widget.recyclerview.RecyclerAdapter;
import com.afirez.wevideo.common.widget.recyclerview.RecyclerHolder;
import com.afirez.wevideo.common.utils.ImageUtils;
import com.afirez.wevideo.home.model.Channel;

import java.util.ArrayList;

/**
 *
 */
public class SiteDetailsFragment extends BaseFragment {


    private RecyclerAdapter recyclerAdapter;

    public static SiteDetailsFragment newInstance(int siteId, int channelId) {
        Bundle args = new Bundle();
        args.putInt("siteId", siteId);
        args.putInt("channelId", channelId);
        SiteDetailsFragment fragment = new SiteDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SiteDetailsFragment() {

    }

    private int siteId;
    private int channelId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            siteId = arguments.getInt("siteId");
            channelId = arguments.getInt("channelId");
        }
        if (siteId == Site.LETV) {
            spanCount = 2;
        } else {
            spanCount = 3;
        }
    }

    private int spanCount;

    private SwipeRefreshLayout srlRefresh;
    private RecyclerView rvItems;

    @Override
    protected int getLayoutId() {
        return R.layout.wv_channel_details_fragment_site_details;
    }

    @Override
    protected void initView() {
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.wv_channel_details_srl_refresh);
        srlRefresh.setColorSchemeResources(
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark
        );
        srlRefresh.setOnRefreshListener(onRefreshListener);
        rvItems = (RecyclerView) findViewById(R.id.wv_channel_details_rv_items);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(layoutManager);
        AlbumAdapter adapter = new AlbumAdapter();
        recyclerAdapter = new RecyclerAdapter(adapter);
        recyclerAdapter.setOnLoadMoreListener(onLoadMoreListener);
        rvItems.setAdapter(recyclerAdapter);
    }

    private static final int DURATION_REFRESH = 1500;

    private static final int DURATION_LOAD_MORE = 3000;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            LoadMoreHelper loadMoreHelper = recyclerAdapter.getLoadMoreHelper();
            if (srlRefresh.isRefreshing()
                    || loadMoreHelper == null
                    || loadMoreHelper.isLoading()) {
                return;
            }
            srlRefresh.setRefreshing(true);
            refresh();
        }
    };

    private void refresh() {

    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            handler.removeCallbacks(refreshRunnable);
            handler.postDelayed(refreshRunnable, DURATION_REFRESH);
        }
    };

    private Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            LoadMoreHelper loadMoreHelper = recyclerAdapter.getLoadMoreHelper();
            if (srlRefresh.isRefreshing()
                    || loadMoreHelper == null
                    || loadMoreHelper.isLoading()) {
                return;
            }
            loadMoreHelper.setLoading(true);
            loadMore();
        }
    };

    private void loadMore() {
        
    }

    private LoadMoreHelper.OnLoadMoreListener onLoadMoreListener = new LoadMoreHelper.OnLoadMoreListener() {
        @Override
        public void onLoadMore(LoadMoreHelper helper) {
            handler.removeCallbacks(loadMoreRunnable);
            handler.postDelayed(loadMoreRunnable, DURATION_LOAD_MORE);
        }
    };

    @Override
    protected void initData() {

    }

    private ArrayList<Album> albums;

    {
        albums = new ArrayList<>();
    }

    private View.OnClickListener albumOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvItems.getChildAdapterPosition(v);
            Album album = albums.get(position);
            if (channelId == Channel.DOCUMENTARY
                    || channelId == Channel.MOVIE
                    || channelId == Channel.VARIETY
                    || channelId == Channel.MUSIC) {
                AlbumDetailActivity.launch(getActivity(), album, 0, true);
            } else {
                AlbumDetailActivity.launch(getActivity(), album);
            }
        }
    };

    private class AlbumHolder extends RecyclerHolder {

        private final ImageView ivPoster;
        private final TextView tvTip;
        private final TextView tvName;

        public AlbumHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) findViewById(R.id.wv_channel_details_iv_album_poster);
            tvTip = (TextView) findViewById(R.id.wv_channel_details_tv_album_tip);
            tvName = (TextView) findViewById(R.id.wv_channel_details_tv_album_name);
            itemView.setOnClickListener(albumOnClickListener);
        }

        public void onBind(int position) {
            Album album = albums.get(position);
            String tip = album.getTip();
            if (TextUtils.isEmpty(tip)) {
                tvTip.setVisibility(View.GONE);
            } else {
                tvTip.setText(album.getTip());
            }
            tvName.setText(album.getTitle());
            int hRatio = spanCount == 2 ? 2 : 4;
            String ratio = "3:" + hRatio;
            ConstraintLayout.LayoutParams layoutParams =
                    (ConstraintLayout.LayoutParams) ivPoster.getLayoutParams();
            if (!ratio.equals(layoutParams.dimensionRatio)) {
                layoutParams.dimensionRatio = "3:" + hRatio;
                ivPoster.setLayoutParams(layoutParams);
            }
            if (album.getVerImgUrl() != null) {
                ImageUtils.load(ivPoster, album.getVerImgUrl());
            } else if (album.getHorImgUrl() != null) {
                ImageUtils.load(ivPoster, album.getHorImgUrl());
            }
        }
    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

        @NonNull
        @Override
        public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.wv_channel_details_item_album, parent, false);
            return new AlbumHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return albums == null ? 0 : albums.size();
        }
    }
}
