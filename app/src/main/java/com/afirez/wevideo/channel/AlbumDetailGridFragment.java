package com.afirez.wevideo.channel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.common.BaseFragment;

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
//            mVideoItemAdatper = new VideoItemAdapter(getActivity(), mAlbum.getVideoTotal(), mVideoSelectedListner);
//            mVideoItemAdatper.setIsShowTitleContent(mIsShowDesc);
            mPageTotal = (mAlbum.getVideoTotal() + mPageSize -1)/ mPageSize;
            loadData();
        }
    }

    private void loadData() {

    }


    @Override
    protected int layoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
