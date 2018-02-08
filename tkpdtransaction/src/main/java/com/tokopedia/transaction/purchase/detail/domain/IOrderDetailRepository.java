package com.tokopedia.transaction.purchase.detail.domain;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

import rx.Observable;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

public interface IOrderDetailRepository {

    Observable<OrderDetailData> requestOrderDetailData(TKPDMapParam<String, Object> params);

    Observable<String> requestCancelOrder(TKPDMapParam<String, String> params);

    Observable<String> cancelReplacement(TKPDMapParam<String, Object> params);

    Observable<String> confirmFinishDeliver(TKPDMapParam<String, String> params);

    Observable<String> confirmDelivery(TKPDMapParam<String, String> params);

}
