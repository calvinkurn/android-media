package com.tokopedia.seller.transaction.neworder.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDetailDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public interface GetNewOrderRepository {
    Observable<List<DataOrderDetailDomain>> getNewOrderList(RequestParams requestParams);
}
