package com.tokopedia.seller.transaction.neworder.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.transaction.neworder.data.mapper.NewOrderMapperData;
import com.tokopedia.seller.transaction.neworder.data.source.GetNewOrderDataSource;
import com.tokopedia.seller.transaction.neworder.domain.GetNewOrderRepository;
import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDetailDomain;

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
    public Observable<List<DataOrderDetailDomain>> getNewOrderList(RequestParams requestParams) {
        return getNewOrderDataSource.getNewOrderList(requestParams)
                .map(new NewOrderMapperData());
    }
}
