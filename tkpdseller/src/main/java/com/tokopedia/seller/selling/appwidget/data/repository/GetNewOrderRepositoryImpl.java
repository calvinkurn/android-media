package com.tokopedia.seller.selling.appwidget.data.repository;

import com.tokopedia.seller.selling.appwidget.data.source.GetNewOrderDataSource;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;
import com.tokopedia.seller.selling.appwidget.domain.GetNewOrderRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public class GetNewOrderRepositoryImpl implements GetNewOrderRepository {
    private final GetNewOrderDataSource getNewOrderDataSource;

    public GetNewOrderRepositoryImpl(GetNewOrderDataSource getNewOrderDataSource) {
        this.getNewOrderDataSource = getNewOrderDataSource;
    }

    @Override
    public Observable<List<DataOrder>> getNewOrderList() {
        return getNewOrderDataSource.getNewOrderList();
    }
}
