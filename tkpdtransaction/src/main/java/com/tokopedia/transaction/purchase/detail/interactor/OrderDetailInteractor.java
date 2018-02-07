package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.List;

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

    void processOrder(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void rejectEmptyOrderVarian(Subscriber<String> subscriber,
                                List<EmptyVarianProductEditable> emptyVarianProductEditables,
                                TKPDMapParam<String, String> productParam,
                                TKPDMapParam<String, String> rejectParam);

    void rejectChangeWeightPrice(Subscriber<String> subscriber,
                                List<WrongProductPriceWeightEditable> emptyVarianProductEditables,
                                TKPDMapParam<String, String> productParam,
                                TKPDMapParam<String, String> rejectParam);

    void confirmAwb(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void confirmShipping(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void cancelShipping(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void retryPickup(Subscriber<String> subscriber, TKPDMapParam<String, String> params);

    void onActivityClosed();
}
