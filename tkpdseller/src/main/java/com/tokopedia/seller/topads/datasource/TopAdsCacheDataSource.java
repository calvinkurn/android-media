package com.tokopedia.seller.topads.datasource;

import com.tokopedia.seller.topads.model.data.Summary;

import java.util.Date;

import rx.Observable;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsCacheDataSource {

    Observable<Void> insertSummary(String shopId, int type, Date startDate, Date endDate, Summary summary);

    Observable<Summary> getSummary(String shopId, int type, Date startDate, Date endDate);
}
