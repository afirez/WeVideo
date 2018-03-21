package com.afirez.wevideo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    public static final int WHAT_START_GUIDE_ACTIVITY = 1;
    public static final int WHAT_START_MAIN_ACTIVITY = 2;
    public static final int DURATION_DELAYED = 2000;

    private SharedPreferences preferences;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_START_GUIDE_ACTIVITY:
                    startGuideActivity();
                    return true;
                case WHAT_START_MAIN_ACTIVITY:
                    startMainActivity();
                    return true;
                default:
                    break;
            }
            return false;
        }
    });

    private void startMainActivity() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void startGuideActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wv_activity_splash);
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
        int what = isFirstIn ? WHAT_START_GUIDE_ACTIVITY : WHAT_START_MAIN_ACTIVITY;
        handler.sendEmptyMessageDelayed(what, DURATION_DELAYED);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
