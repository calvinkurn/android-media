package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

import rx.Subscriber;

/**
 * Created by kris on 11/13/17. Tokopedia
 */

public interface OrderDetailInteractor {

    void requestDetailData(Subscriber<OrderDetailData> subscriber,
                           TKPDMapParam<String, Object> params);

    void confirmFinishConfirm(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void confirmDeliveryConfirm(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void cancelOrder(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void cancelReplacement(Subscriber<String> subscriber, TKPDMapParam<String, Object> params);

    void onActivityClosed();
}
