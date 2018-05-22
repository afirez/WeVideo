package com.afirez.wevideo.channel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.afirez.wevideo.R;
import com.afirez.wevideo.api.ApiCallback;
import com.afirez.wevideo.api.SiteApis;
import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.channel.model.ErrorInfo;
import com.afirez.wevideo.channel.model.Video;
import com.afirez.wevideo.common.BaseFragment;

import java.util.ArrayList;

public class AlbumDetailGridFragment extends BaseFragment {

    private static final String TAG = AlbumDetailGridFragment.class.getSimpleName();
    private static final String ARGS_ALBUM = "album";
    private static final String ARGS_IS_SHOWDESC = "isShowDesc";
    private static final String ARGS_INIT_POSITION = "initVideoPosition";
    private Album mAlbum;
    private boolean mIsShowDesc;
    private int mInitPosition;
    private int mCurrentPosition;
    private int mPageNo;
    private int mPageSize;
    private int mPageTotal;

    private GridView gvVideoNumbers;
    private MyAdapter adapter;
    private TextView tvEmpty;
    private boolean isLoading;
    private boolean hasMore;
    private boolean isFristSelection;

    public static AlbumDetailGridFragment newInstance(Album album, boolean isShowDesc, int initVideoPosition) {
        AlbumDetailGridFragment fragment = new AlbumDetailGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ALBUM, album);
        bundle.putBoolean(ARGS_IS_SHOWDESC, isShowDesc);
        bundle.putInt(ARGS_INIT_POSITION, initVideoPosition);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ">> oncreate ");
        if (getArguments() != null) {
            mAlbum = getArguments().getParcelable(ARGS_ALBUM);
            mIsShowDesc = getArguments().getBoolean(ARGS_IS_SHOWDESC);
            mInitPosition = getArguments().getInt(ARGS_INIT_POSITION);
            mCurrentPosition = mInitPosition;
            mPageNo = 0;
            mPageSize = 50;
            adapter = new MyAdapter();
            mPageTotal = (mAlbum.getVideoTotal() + mPageSize - 1) / mPageSize;
            loadData();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private void loadData() {
        mPageNo++;
        SiteApis.getInstance().getVideos(mPageSize, mPageNo, mAlbum, new ApiCallback<ArrayList<Video>>() {
            @Override
            public void onSuccess(ArrayList<Video> videoList) {
                Log.d(TAG, ">> OnGetVideoSuccess " + videoList.size());
                videos.addAll(videoList);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvEmpty.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        if (adapter.getCount() > mInitPosition && isFristSelection) {
                            gvVideoNumbers.setSelection(mInitPosition);
                            gvVideoNumbers.setItemChecked(mInitPosition, true);
                            isFristSelection = false;
                            SystemClock.sleep(100);
                            gvVideoNumbers.smoothScrollToPosition(mInitPosition);
                        }
                    }
                });
            }

            @Override
            public void onError(ErrorInfo info) {

            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.wv_album_detail_fragment_grid;
    }

    @Override
    protected void initView() {
        tvEmpty = (TextView) findViewById(R.id.wv_album_detail_tv_empty);
        tvEmpty.setVisibility(View.VISIBLE);
        gvVideoNumbers = (GridView) findViewById(R.id.wv_album_detail_gv_video_numbers);
        gvVideoNumbers.setNumColumns(mIsShowDesc ? 1 : 6);
        gvVideoNumbers.setAdapter(adapter);
        hasMore = mAlbum.getVideoTotal() > 0 && mAlbum.getVideoTotal() > mPageSize;
        gvVideoNumbers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (!isLoading && hasMore && lastVisibleItem == totalItemCount) {
                        onLoadMore();
                    }
                }
            }
        });
    }

    private void onLoadMore() {
        loadData();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onVideoSelectedListener = (OnVideoSelectedListener) context;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onVideoSelectedListener = null;
    }

    private OnVideoSelectedListener onVideoSelectedListener;

    public interface OnVideoSelectedListener {
        void onVideoSelected(Video video, int position);
    }

    private ArrayList<Video> videos = new ArrayList<>();

    private boolean isFirst = true;

    private View.OnClickListener videoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof MyHolder) {
                int position = ((MyHolder) tag).position;
                Video video = videos.get(position);
                gvVideoNumbers.setSelection(position);
                gvVideoNumbers.setItemChecked(position, true);
                if (onVideoSelectedListener != null) {
                    onVideoSelectedListener.onVideoSelected(video, position);
                }
            }
        }
    };

    private class MyHolder {
        public View itemView;
        private final Button btnVideoNumber;
        private int position;

        public MyHolder(View itemView) {
            this.itemView = itemView;
            btnVideoNumber = (Button) itemView.findViewById(R.id.wv_album_detail_btn_item_video_number);
            itemView.setOnClickListener(videoOnClickListener);
        }

        public void onBind(int position, Video video) {
            this.position = position;
            if (mIsShowDesc && !TextUtils.isEmpty(video.getVideoName())) {
                btnVideoNumber.setText(video.getVideoName());
            } else {
                btnVideoNumber.setText(String.valueOf(position + 1));
            }
            if (position == 0 && isFirst) {
                itemView.performClick();
                isFirst = false;
            }
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return videos.size();
        }

        @Override
        public Video getItem(int position) {
            if (videos.size() > position) {
                return videos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.wv_album_detail_item_video_number, parent, false);
                myHolder = new MyHolder(convertView);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.onBind(position, getItem(position));
            return convertView;
        }
    }
}
