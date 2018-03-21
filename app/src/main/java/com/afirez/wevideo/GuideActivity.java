package com.afirez.wevideo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideActivity extends AppCompatActivity {

    private int[] drawableResources;

    private ImageView[] ivIndicators;

    private int lastPosition;
    private ImageView ivStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wv_activity_guide);
        drawableResources = new int[]{
                R.drawable.wv_splash_ic_guide_0,
                R.drawable.wv_splash_ic_guide_1,
                R.drawable.wv_splash_ic_guide_2,
        };
        ivIndicators = new ImageView[]{
                (ImageView) findViewById(R.id.wv_guide_iv_indicator_0),
                (ImageView) findViewById(R.id.wv_guide_iv_indicator_1),
                (ImageView) findViewById(R.id.wv_guide_iv_indicator_2),
        };
        ivStart = (ImageView) findViewById(R.id.wv_guide_iv_start);
        ivStart.setOnClickListener(startOnClickListener);
        ViewPager vpGuide = (ViewPager) findViewById(R.id.wv_guide_vp_guide);
        vpGuide.setAdapter(adapter);
        vpGuide.addOnPageChangeListener(onPageChangeListener);
        ivIndicators[0].setSelected(true);
        ivStart.setVisibility(View.GONE);
    }

    private View.OnClickListener startOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startMainActivity();
            setIsFirstIn(false);
        }
    };

    private void setIsFirstIn(boolean isFirstIn) {
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        preferences.edit().putBoolean("isFirstIn", isFirstIn).apply();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private PagerAdapter adapter = new PagerAdapter() {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(drawableResources[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            params.width = ViewPager.LayoutParams.MATCH_PARENT;
            params.height = ViewPager.LayoutParams.MATCH_PARENT;
            container.addView(imageView, params);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return drawableResources == null ? 0 : drawableResources.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            position %= 3;
            updateIvIndicators(position);
            updateIvStart(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateIvStart(int position) {
        int visibility = position == 2 ? View.VISIBLE : View.GONE;
        ivStart.setVisibility(visibility);
    }

    private void updateIvIndicators(int position) {
        ivIndicators[position].setSelected(true);
        ivIndicators[lastPosition].setSelected(false);
        lastPosition = position;
    }
}