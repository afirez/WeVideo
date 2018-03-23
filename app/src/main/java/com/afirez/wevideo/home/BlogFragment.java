package com.afirez.wevideo.home;


import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.afirez.wevideo.R;
import com.afirez.wevideo.common.BaseFragment;

/**
 */
public class BlogFragment extends BaseFragment {

    private ProgressBar pbProgress;
    private WebView wvBlog;
    private WebSettings settings;
    private static final int MAX_PROGRESS = 100;
    private static final String URL = "http://m.blog.csdn.net/blog/index?username=hejjunlin";

    public BlogFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wv_fragment_blog;
    }

    @Override
    protected void initView() {
        pbProgress = (ProgressBar) findViewById(R.id.wv_main_pb_blog_progress);
        wvBlog = (WebView) findViewById(R.id.wv_main_wv_blog);
        pbProgress.setMax(MAX_PROGRESS);
        settings = wvBlog.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        wvBlog.setWebChromeClient(webChromeClient);
        wvBlog.loadUrl(URL);
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pbProgress.setProgress(newProgress);
            if (newProgress == MAX_PROGRESS) {
                pbProgress.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    protected void initData() {

    }
}
