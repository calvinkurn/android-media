package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

import rx.Subscriber;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public interface OrderHistoryInteractor {

    void requestOrderHistoryData(Subscriber<OrderHistoryData> subscriber,
                                 TKPDMapParam<String, Object> params);

    void onViewDestroyed();
}
