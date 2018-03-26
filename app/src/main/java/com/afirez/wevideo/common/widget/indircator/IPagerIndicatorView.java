package com.afirez.wevideo.common.widget.indircator;

import java.util.List;

/**
 * Created by hejunlin on 17/4/8.
 */

public interface IPagerIndicatorView extends IPagerChangeListener {

    void setPostionDataList(List<PositionData> list);
}
