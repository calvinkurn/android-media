package com.tokopedia.transaction.purchase.detail.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

import rx.Observable;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public interface IOrderHistoryRepository {

    Observable<OrderHistoryData> requestOrderHistoryData(TKPDMapParam<String, Object> params);

}
