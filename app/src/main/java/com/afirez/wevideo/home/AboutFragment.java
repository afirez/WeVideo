package com.afirez.wevideo.home;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.afirez.wevideo.R;
import com.afirez.wevideo.common.BaseFragment;

/**
 *
 */
public class AboutFragment extends BaseFragment {


    public AboutFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wv_fragment_about;
    }

    @Override
    protected void initView() {
        TextView tvAppDescription = (TextView) findViewById(R.id.wv_main_tv_about_app_description);
        tvAppDescription.setAutoLinkMask(Linkify.ALL);
        tvAppDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void initData() {

    }
}
