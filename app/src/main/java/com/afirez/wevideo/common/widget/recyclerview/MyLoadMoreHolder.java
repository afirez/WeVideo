package com.afirez.wevideo.common.widget.recyclerview;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.afirez.wevideo.R;

/**
 * Created by afirez on 18-3-29.
 */

public class MyLoadMoreHolder extends LoadMoreHolder {
    private final ImageView ivLoading;
    private final TextView tvLoading;
    private AnimationDrawable animationDrawable;

    public MyLoadMoreHolder(View itemView) {
        super(itemView);
        ivLoading = (ImageView) findViewById(R.id.wv_channel_details_iv_loading);
        tvLoading = (TextView) findViewById(R.id.wv_channel_details_tv_loading);
        Drawable drawable = ivLoading.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            animationDrawable = ((AnimationDrawable) drawable);
        }
    }

    @Override
    public void onBind(int position) {
        itemView.setVisibility(isLoading() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setLoading(boolean isLoading) {
        super.setLoading(isLoading);
        itemView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            if (animationDrawable != null) {
                animationDrawable.start();
            }
        } else {
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            itemView.animate().translationX(itemView.getHeight())
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300)
                    .start();
        }
    }
}
