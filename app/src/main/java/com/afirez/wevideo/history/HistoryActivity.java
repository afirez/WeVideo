package com.afirez.wevideo.history;

import android.app.Activity;
import android.content.Intent;

import com.afirez.wevideo.R;
import com.afirez.wevideo.common.BaseActivity;

public class HistoryActivity extends BaseActivity {

    public static void start(Activity context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected int layoutId() {
        return R.layout.wv_activity_history;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
