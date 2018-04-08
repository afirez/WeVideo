package com.afirez.wevideo.channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.afirez.wevideo.R;
import com.afirez.wevideo.common.BaseActivity;
import com.afirez.wevideo.common.BaseFragment;
import com.afirez.wevideo.common.widget.indircator.CoolIndicatorLayout;
import com.afirez.wevideo.common.widget.indircator.IPagerIndicatorView;
import com.afirez.wevideo.common.widget.indircator.IPagerTitle;
import com.afirez.wevideo.common.widget.indircator.ViewPagerITitleView;
import com.afirez.wevideo.common.widget.indircator.ViewPagerIndicatorAdapter;
import com.afirez.wevideo.common.widget.indircator.ViewPagerIndicatorLayout;
import com.afirez.wevideo.common.widget.indircator.ViewPagerWrapper;
import com.afirez.wevideo.common.widget.indircator.ViewPaperIndicatorView;
import com.afirez.wevideo.home.model.Channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChannelDetailsActivity extends BaseActivity {

    private int channelId;

    public static void start(Activity context, int channelId) {
        Intent intent = new Intent(context, ChannelDetailsActivity.class);
        intent.putExtra("channelId", channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wv_activity_channel_details;
    }

    private ViewPager vpPager;
    private CoolIndicatorLayout ilTabs;

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            channelId = intent.getIntExtra("channelId", 0);
        }
        Channel channel = new Channel(channelId);
        setCommonSupportActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(channel.getNameRes());
        vpPager = (ViewPager) findViewById(R.id.wv_channel_details_vp_pager);
        ilTabs = (CoolIndicatorLayout) findViewById(R.id.wv_channel_details_indicator_tabs);
        ViewPagerIndicatorLayout pagerIndicatorLayout = new ViewPagerIndicatorLayout(this);
        pagerIndicatorLayout.setAdapter(new ViewPagerIndicatorAdapter() {
            @Override
            public int getCount() {
                return sites == null ? 0 : sites.size();
            }

            @Override
            public IPagerTitle getTitle(Context context, final int index) {
                ViewPagerITitleView titleView = new ViewPagerITitleView(context);
                titleView.setText(sites.get(index));
                titleView.setNormalColor(Color.parseColor("#333333"));
                titleView.setSelectedColor(Color.parseColor("#e94220"));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vpPager.setCurrentItem(index);
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicatorView getIndicator(Context context) {
                ViewPaperIndicatorView indicatorView = new ViewPaperIndicatorView(context);
                indicatorView.setFillColor(Color.parseColor("#ebe4e3"));
                return indicatorView;
            }
        });

        ilTabs.setPagerIndicatorLayout(pagerIndicatorLayout);
        ViewPagerWrapper.with(ilTabs, vpPager).compose();
        vpPager.setAdapter(new SitePagerAdapter(getSupportFragmentManager()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {

    }

    private String[] siteNames = new String[]{"搜狐视频", "乐视视频"};
    private List<String> sites = Arrays.asList(siteNames);

    private ArrayList<BaseFragment> fragments;

    {
        fragments = new ArrayList<>();
        fragments.add(SiteDetailsFragment.newInstance(1, channelId));
        fragments.add(SiteDetailsFragment.newInstance(2, channelId));
    }

    private class SitePagerAdapter extends FragmentPagerAdapter {

        public SitePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }
}
