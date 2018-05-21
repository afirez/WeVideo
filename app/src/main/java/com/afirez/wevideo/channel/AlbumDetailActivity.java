package com.afirez.wevideo.channel;

import android.app.Activity;
import android.content.Intent;

import com.afirez.wevideo.R;
import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.common.BaseActivity;

public class AlbumDetailActivity extends BaseActivity {

    private Album album;
    private int videoNo;
    private boolean isShowDesc;

    public static void launch(Activity activity, Album album) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.putExtra("album", album);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, Album album, int videoNo, boolean isShowDesc) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.putExtra("album", album);
        intent.putExtra("videoNo", videoNo);
        intent.putExtra("isShowDesc", isShowDesc);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wv_channel_activity_album_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            album = intent.getParcelableExtra("album");
            videoNo = intent.getIntExtra("videoNo", 0);
            isShowDesc = intent.getBooleanExtra("isShowDesc", false);
            setCommonSupportActionBar();
            setDisplayHomeAsUpEnabled(true);
            if (album != null) {
                setTitle(album.getTitle());
            }
        }
    }

    @Override
    protected void initData() {

    }

}
