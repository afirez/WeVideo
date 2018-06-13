package com.afirez.wevideo.Live;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afirez.wevideo.R;
import com.afirez.wevideo.common.BaseActivity;
import com.afirez.wevideo.player.VideoPlayerActivity;

public class LiveActivity extends BaseActivity {

    private RecyclerView rvRooms;
    private Adapter adapter;

    public static void start(Activity context) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected int layoutId() {
        return R.layout.wv_activity_live;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        setCommonSupportActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.live_title);
        rvRooms = (RecyclerView) findViewById(R.id.live_rv_rooms);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRooms.setFocusable(false);
        rvRooms.setLayoutManager(layoutManager);
        adapter = new Adapter();
        rvRooms.setAdapter(adapter);
        rvRooms.scrollToPosition(0);
    }

    @Override
    protected void initData() {

    }

    private View.OnClickListener itemOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvRooms.getChildAdapterPosition(v);
            VideoPlayerActivity.start(LiveActivity.this, mUrlList[position], mDataList[position]);
        }
    };

    private class Holder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final ImageView ivIcon;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(itemOnclickListener);
            tvTitle = (TextView) itemView.findViewById(R.id.live_tv_item_title);
            ivIcon = (ImageView) itemView.findViewById(R.id.live_iv_item_icon);
        }

        public void onBind(int position) {
            tvTitle.setText(mDataList[position]);
            ivIcon.setImageResource(mIconList[position]);
        }
    }


    private class Adapter extends RecyclerView.Adapter<Holder> {

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View inflate = inflater.inflate(R.layout.live_item_room, parent, false);
            return new Holder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mDataList.length;
        }
    }

    private String[] mDataList = new String[] {
            "CCTV-1 综合","CCTV-2 财经","CCTV-3 综艺",
            "CCTV-4 中文国际(亚)","CCTV-5 体育", "CCTV-6 电影",
            "CCTV-7 军事农业","CCTV-8 电视剧", "CCTV-9 纪录",
            "CCTV-10 科教", "CCTV-11 戏曲","CCTV-12 社会与法",
            "CCTV-13 新闻","CCTV-14 少儿","CCTV-15 音乐",
            "湖南卫视","北京卫视","天津卫视",
            "湖北卫视","东方卫视",
    };

    private int[] mIconList = new int[] {
            R.drawable.live_cctv_1, R.drawable.live_cctv_2, R.drawable.live_cctv_3,
            R.drawable.live_cctv_4, R.drawable.live_cctv_5, R.drawable.live_cctv_6,
            R.drawable.live_cctv_7, R.drawable.live_cctv_8, R.drawable.live_cctv_9,
            R.drawable.live_cctv_10, R.drawable.live_cctv_11, R.drawable.live_cctv_12,
            R.drawable.live_cctv_13, R.drawable.live_cctv_14, R.drawable.live_cctv_15,
            R.drawable.live_hunan_tv,R.drawable.live_beijing_tv,R.drawable.live_tianjing_tv,
            R.drawable.live_hubei_tv,R.drawable.live_dongfang_tv,
    };

    private String [] mUrlList = new String[]{
            "http://220.248.175.231:6610/001/2/ch00000090990000001022/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001014/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001023/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001015/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001016/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001017/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001018/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001019/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001020/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001021/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001027/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001028/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001029/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001030/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001031/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001053/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001077/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001069/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001047/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001081/index.m3u8?virtualDomain=001.live_hls.zte.com",
    };
}
