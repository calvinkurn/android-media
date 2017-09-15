package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.topads.dashboard.data.model.data.Cell;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public interface TopAdsStatisticViewListener {
    void updateDataCell(List<Cell> cells);
}
