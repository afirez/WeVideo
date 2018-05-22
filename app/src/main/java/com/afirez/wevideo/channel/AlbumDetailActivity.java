package com.afirez.wevideo.channel;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afirez.wevideo.R;
import com.afirez.wevideo.api.ApiCallback;
import com.afirez.wevideo.api.SiteApis;
import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.channel.model.ErrorInfo;
import com.afirez.wevideo.channel.model.Video;
import com.afirez.wevideo.channel.model.VideoWithType;
import com.afirez.wevideo.common.BaseActivity;
import com.afirez.wevideo.common.utils.AppUtils;
import com.afirez.wevideo.common.utils.ImageUtils;
import com.afirez.wevideo.player.VideoPlayerActivity;

public class AlbumDetailActivity extends BaseActivity implements AlbumDetailGridFragment.OnVideoSelectedListener {

    private Album album;
    private int videoNo;
    private boolean isShowDesc;

    private ImageView ivAlbumImage;

    private TextView tvAlbumName;
    private TextView tvAlbumDirector;
    private TextView tvAlbumMainActor;
    private TextView tvAlbumDescription;

    private Button btnSuperBitStream;
    private Button btnHighBitStream;
    private Button btnNormalBitStream;

    private AlbumDetailGridFragment mFragment;

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
    protected int layoutId() {
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

        ivAlbumImage = (ImageView) findViewById(R.id.wv_album_detail_iv_image);
        tvAlbumName = (TextView) findViewById(R.id.wv_album_detail_tv_name);
        tvAlbumDirector = (TextView) findViewById(R.id.wv_album_detail_tv_director);
        tvAlbumMainActor = (TextView) findViewById(R.id.wv_album_detail_tv_main_actor);
        tvAlbumDescription = (TextView) findViewById(R.id.wv_album_detail_tv_description);

        btnSuperBitStream = (Button) findViewById(R.id.wv_album_detail_btn_super);
        btnSuperBitStream.setOnClickListener(superOnClickListener);
        btnHighBitStream = (Button) findViewById(R.id.wv_album_detail_btn_high);
        btnHighBitStream.setOnClickListener(highOnClickListener);
        btnNormalBitStream = (Button) findViewById(R.id.wv_album_detail_btn_normal);
        btnNormalBitStream.setOnClickListener(normalOnClickListener);

    }

    private View.OnClickListener superOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = (String) v.getTag(R.id.key_video_url);
            int type = (int) v.getTag(R.id.key_video_stream);
            Video video = (Video) v.getTag(R.id.key_video);
            int currentPosition = (int) v.getTag(R.id.key_current_video_number);
            if (AppUtils.isNetWorkAvailable()) {
                if (AppUtils.isWifiAvailable()) {
//                    mHistoryDBHelper.add(mAlbum);
                    Intent intent = new Intent(AlbumDetailActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("type", type);
                    intent.putExtra("currentPosition", currentPosition);
                    intent.putExtra("video", video);
                    startActivity(intent);
                }
            }
        }
    };

    private View.OnClickListener highOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener normalOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    private boolean mIsFavor;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.wv_album_detail_action_favor:
                if (mIsFavor) {
                    mIsFavor = false;
                    // 收藏状态更新
//                    mFavoriteDBHelper.delete(mAlbum.getAlbumId(), mAlbum.getSite().getSiteId());
                    invalidateOptionsMenu();
                    Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.wv_album_detail_action_unfavor:
                if (!mIsFavor) {
                    mIsFavor = true;
                    // 收藏状态更新
//                    mFavoriteDBHelper.add(mAlbum);
                    invalidateOptionsMenu();
                    Toast.makeText(this, "已添加收藏", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wv_album_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favitem = menu.findItem(R.id.wv_album_detail_action_favor);
        MenuItem unfavitem = menu.findItem(R.id.wv_album_detail_action_unfavor);
        favitem.setVisible(mIsFavor);
        unfavitem.setVisible(!mIsFavor);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void initData() {
        updateAlbumInfo();
        SiteApis.getInstance().getAlbumDetail(album, new ApiCallback<Album>() {
            @Override
            public void onSuccess(Album data) {
                album = data;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateAlbumInfo();
                        mFragment = AlbumDetailGridFragment.newInstance(album, isShowDesc, 0);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.wv_album_detail_fl_container, mFragment);
                        ft.commitAllowingStateLoss();
                    }
                });
            }

            @Override
            public void onError(ErrorInfo info) {

            }
        });
    }

    private void updateAlbumInfo() {
        tvAlbumName.setText(album.getTitle());
        //导演
        if (!TextUtils.isEmpty(album.getDirector())) {
            tvAlbumDirector.setText(getResources().getString(R.string.wv_album_detail_director) + album.getDirector());
            tvAlbumDirector.setVisibility(View.VISIBLE);
        } else {
            tvAlbumDirector.setVisibility(View.GONE);
        }
        //主演
        if (!TextUtils.isEmpty(album.getMainActor())) {
            tvAlbumMainActor.setText(getResources().getString(R.string.mainactor) + album.getMainActor());
            tvAlbumMainActor.setVisibility(View.VISIBLE);
        } else {
            tvAlbumMainActor.setVisibility(View.GONE);
        }
        //描述
        if (!TextUtils.isEmpty(album.getAlbumDesc())) {
            tvAlbumDescription.setText(album.getAlbumDesc());
            tvAlbumDescription.setVisibility(View.VISIBLE);
        } else {
            tvAlbumDescription.setVisibility(View.GONE);
        }
        //海报图
        if (!TextUtils.isEmpty(album.getVerImgUrl())) {
            ImageUtils.load(ivAlbumImage, album.getVerImgUrl());
        } else if (!TextUtils.isEmpty(album.getHorImgUrl())) {
            ImageUtils.load(ivAlbumImage, album.getHorImgUrl());
        }
    }

    private int curPosition;

    @Override
    public void onVideoSelected(Video video, int position) {
        curPosition = position;
        SiteApis.getInstance().getVideoUrl(video, videoWithTypeCallback);
    }

    private ApiCallback<VideoWithType> videoWithTypeCallback = new ApiCallback<VideoWithType>() {
        @Override
        public void onSuccess(final VideoWithType data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("video", "run: " + data);
                    View theView = null;
                    int type = data.getType();
                    switch (type) {
                        case VideoWithType.TYPE_NORMAL:
                            theView = btnNormalBitStream;
                            break;
                        case VideoWithType.TYPE_HIGH:
                            theView = btnHighBitStream;
                            break;
                        case VideoWithType.TYPE_SUPER:
                            theView = btnSuperBitStream;
                            break;
                    }
                    if (theView != null) {
                        theView.setVisibility(View.VISIBLE);
                        theView.setTag(R.id.key_video_url, data.getUrl()); //视频url
                        theView.setTag(R.id.key_video, data.getVideo());//视频info
                        theView.setTag(R.id.key_current_video_number, curPosition);//当前视频
                        theView.setTag(R.id.key_video_stream, type); //码流
                    }
                }
            });
        }

        @Override
        public void onError(ErrorInfo info) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            });
        }
    };

    private void hide() {
        btnHighBitStream.setVisibility(View.GONE);
        btnNormalBitStream.setVisibility(View.GONE);
        btnSuperBitStream.setVisibility(View.GONE);
    }
}
