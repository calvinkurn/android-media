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

    String SHOP_ID_KEY = "shop_id";

    String PRODUCT_ID_KEY = "product_id";

    String PRODUCT_PRICE_KEY = "product_price";

    String PRODUCT_WEIGHT_VALUE_KEY = "product_weight_value";

    String PRODUCT_PRICE_CURRENCY_KEY = "product_price_currency";

    String PRODUCT_WEIGHT_UNIT_KEY = "product_weight_unit";

    String PRODUCT_DESCRIPTION_KEY = "product_id_description_key";

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
