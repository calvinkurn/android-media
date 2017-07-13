package com.tokopedia.seller.selling.appwidget.domain;

import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public interface GetNewOrderRepository {
    Observable<List<DataOrder>> getNewOrderList();
}
