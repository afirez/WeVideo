package com.afirez.wevideo.channel;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afirez.wevideo.R;
import com.afirez.wevideo.common.BaseFragment;

/**
 *
 */
public class SiteDetailsFragment extends BaseFragment {


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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wv_channel_details_fragment_site_details;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

}
