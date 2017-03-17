package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.seller.topads.data.model.data.Cell;

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
