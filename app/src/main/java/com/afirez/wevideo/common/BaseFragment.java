package com.afirez.wevideo.common;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by afirez on 18-3-22.
 */

public abstract class BaseFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layoutId(), container, false);
        initView();
        initData();
        return view;
    }

    protected abstract int layoutId();

    protected abstract void initView();

    protected abstract void initData();

    public <V extends View> V findViewById(@IdRes int id) {
        if (view == null) {
            return null;
        }
        return (V) view.findViewById(id);
    }
}
