package com.afirez.wevideo.common.widget.recyclerview;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by afirez on 18-3-29.
 */

public class RecyclerHolder extends RecyclerView.ViewHolder {

    public RecyclerHolder(View itemView) {
        super(itemView);
    }

    public void onBind(int position){

    }

    public <V extends View> V findViewById(@IdRes int id) {
        if (itemView == null) {
            return null;
        }
        return (V) itemView.findViewById(id);
    }
}
