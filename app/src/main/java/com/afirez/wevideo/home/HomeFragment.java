package com.afirez.wevideo.home;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afirez.wevideo.Live.LiveActivity;
import com.afirez.wevideo.R;
import com.afirez.wevideo.channel.ChannelDetailsActivity;
import com.afirez.wevideo.common.BaseFragment;
import com.afirez.wevideo.favorite.FavoriteActivity;
import com.afirez.wevideo.history.HistoryActivity;
import com.afirez.wevideo.home.model.Channel;
import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;

import java.util.ArrayList;

/**
 *
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";
    private LoopViewPager loopViewPager;
    private CircleIndicator indicatorBanner;
    private GridView gvChannels;

    public HomeFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wv_fragment_home;
    }

    @Override
    protected void initView() {
        Log.i(TAG, "initView: ");
        loopViewPager = (LoopViewPager) findViewById(R.id.wv_main_vp_banner);
        indicatorBanner = (CircleIndicator) findViewById(R.id.wv_main_indicator_banner);
        loopViewPager.setAdapter(new BannerAdapter());
        loopViewPager.setLooperPic(true);
        indicatorBanner.setViewPager(loopViewPager);
        gvChannels = (GridView) findViewById(R.id.wv_main_gv_channels);
        gvChannels.setAdapter(new ChannelAdapter());
        gvChannels.setOnItemClickListener(channelOnItemClickListener);
    }

    private AdapterView.OnItemClickListener channelOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                Log.d(TAG, "onItemClick: host activity == null");
                return;
            }
            Log.i(TAG, "onItemClick: " + position);
            switch (position) {
                case Channel.LIVE:
                    LiveActivity.start(activity);
                    break;
                case Channel.FAVORITE:
                    FavoriteActivity.start(activity);
                    break;
                case Channel.HISTORY:
                    HistoryActivity.start(activity);
                    break;
                default:
                    ChannelDetailsActivity.start(activity, channels.get(position).getId());
                    break;
            }
        }
    };

    @Override
    protected void initData() {

    }


    private int[] mBannerDescriptions = new int[]{
            R.string.wv_main_description_banner_a,
            R.string.wv_main_description_banner_b,
            R.string.wv_main_description_banner_c,
            R.string.wv_main_description_banner_d,
            R.string.wv_main_description_banner_e,
    };

    private int[] mBannerDrawableResources = new int[]{
            R.drawable.wv_main_ic_banner_a,
            R.drawable.wv_main_ic_banner_b,
            R.drawable.wv_main_ic_banner_c,
            R.drawable.wv_main_ic_banner_d,
            R.drawable.wv_main_ic_banner_e,
    };

    private class BannerHolder {
        public final View itemView;
        private final TextView tvBannerDescription;
        private ImageView ivBanner;

        public BannerHolder(View itemView) {
            this.itemView = itemView;
            ivBanner = (ImageView) findViewById(R.id.wv_main_iv_banner);
            tvBannerDescription = (TextView) findViewById(R.id.wv_main_tv_banner_description);
        }

        public void onBind(int position) {
            tvBannerDescription.setText(mBannerDescriptions[position % 5]);
            ivBanner.setImageResource(mBannerDrawableResources[position % 5]);
        }

        public <V extends View> V findViewById(@IdRes int id) {
            if (itemView == null) {
                return null;
            }
            return (V) itemView.findViewById(id);
        }
    }

    private class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.wv_main_item_home_banner, container, false);
            BannerHolder bannerHolder = new BannerHolder(view);
            view.setTag(bannerHolder);
            bannerHolder.onBind(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private ArrayList<Channel> channels = new ArrayList<>();

    {
        channels.add(new Channel(Channel.SHOW));
        channels.add(new Channel(Channel.MOVIE));
        channels.add(new Channel(Channel.COMIC));
        channels.add(new Channel(Channel.DOCUMENTARY));
        channels.add(new Channel(Channel.MUSIC));
        channels.add(new Channel(Channel.VARIETY));
        channels.add(new Channel(Channel.LIVE));
        channels.add(new Channel(Channel.FAVORITE));
        channels.add(new Channel(Channel.HISTORY));
    }


    private class ChannelHolder {
        public final View itemView;
        private final ImageView ivChannel;
        private final TextView tvChannel;

        public ChannelHolder(View itemView) {
            this.itemView = itemView;
            ivChannel = (ImageView) findViewById(R.id.wv_main_iv_channel);
            tvChannel = (TextView) findViewById(R.id.wv_main_tv_channel);
        }

        public void onBind(int position) {
            Channel channel = channels.get(position);
            ivChannel.setImageResource(channel.getImgResId());
            tvChannel.setText(channel.getNameRes());
        }

        public <V extends View> V findViewById(@IdRes int id) {
            if (itemView == null) {
                return null;
            }
            return (V) itemView.findViewById(id);
        }
    }

    private class ChannelAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return channels == null ? 0 : channels.size();
        }

        @Override
        public Channel getItem(int position) {
            return channels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ChannelHolder channelHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.wv_mian_item_channel, parent, false);
                channelHolder = new ChannelHolder(view);
                view.setTag(channelHolder);
            } else {
                view = convertView;
                channelHolder = (ChannelHolder) view.getTag();
            }
            if (channelHolder != null) {
                channelHolder.onBind(position);
            }
            return view;
        }
    }
}
