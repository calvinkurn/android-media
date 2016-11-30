package com.tokopedia.seller.topads.datasource;

import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;

import java.util.Date;
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