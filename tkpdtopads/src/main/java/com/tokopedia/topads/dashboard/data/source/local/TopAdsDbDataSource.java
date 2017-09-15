package com.tokopedia.seller.topads.dashboard.data.source.local;

import com.tokopedia.seller.topads.dashboard.data.model.data.Cell;
import com.tokopedia.seller.topads.dashboard.data.model.data.Summary;
import com.tokopedia.seller.topads.dashboard.data.model.request.StatisticRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsDbDataSource {

    Observable<Summary> insertSummary(StatisticRequest statisticRequest, Summary summary);

    Observable<Summary> getSummary(StatisticRequest statisticRequest);

    Observable<List<Cell>> insertCellList(StatisticRequest statisticRequest, List<Cell> cellList);
}