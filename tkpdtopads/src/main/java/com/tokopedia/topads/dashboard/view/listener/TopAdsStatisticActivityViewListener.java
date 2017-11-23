package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.topads.dashboard.data.model.data.Cell;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public interface TopAdsStatisticActivityViewListener {
    void onError(Throwable throwable);

    void updateDataCell(List<Cell> cells);

    List<Cell> getDataCell();

    void showLoading();

    void dismissLoading();
}
