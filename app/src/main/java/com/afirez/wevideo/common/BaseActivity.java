package com.afirez.wevideo.common;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afirez.wevideo.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activities.getInstance().add(this);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    public  <V extends View> V findViewById(@IdRes int id) {
        return (V) super.findViewById(id);
    }


    public void setSupportActionBar() {
        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(resId);
        }
    }

    public void setNavigationIcon(Drawable icon) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(icon);
        }
    }

    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }

    @Override
    protected void onDestroy() {
        Activities.getInstance().remove(this);
        super.onDestroy();
    }
}
