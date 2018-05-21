package com.afirez.wevideo.common.widget.indircator;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;

/**
 * Created by hejunlin on 17/4/8.
 */

public abstract class ViewPagerIndicatorAdapter {

    public abstract int getCount();
    public abstract IPagerTitle getTitle(Context context, int index);
    public abstract IPagerIndicatorView getIndicator(Context context);

    public float getTitleWeight(){
        return 1;
    }

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public final void registerDataSetObservable(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public final void unregisterDataSetObservable(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public final void notifySetDataChanged() {
        mDataSetObservable.notifyChanged();
    }
}
