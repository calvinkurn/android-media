package com.tokopedia.transaction.purchase.detail.domain;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.List;

import rx.Observable;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

public interface IOrderDetailRepository {

    String SHOP_ID_KEY = "shop_id";

    String PRODUCT_ID_KEY = "product_id";

    String PRODUCT_PRICE_KEY = "product_price";

    String PRODUCT_WEIGHT_VALUE_KEY = "product_weight_value";

    String PRODUCT_PRICE_CURRENCY_KEY = "product_price_currency";

    String PRODUCT_WEIGHT_UNIT_KEY = "product_weight_unit";

    String PRODUCT_DESCRIPTION_KEY = "product_id_description_key";

    Observable<OrderDetailData> requestOrderDetailData(TKPDMapParam<String, Object> params);

    Observable<String> requestCancelOrder(TKPDMapParam<String, String> params);

    Observable<String> cancelReplacement(TKPDMapParam<String, Object> params);

    Observable<String> confirmFinishDeliver(TKPDMapParam<String, String> params);

    Observable<String> confirmDelivery(TKPDMapParam<String, String> params);

    Observable<String> processOrder(TKPDMapParam<String, String> param);

    Observable<String> processShipping(TKPDMapParam<String, String> param);

    Observable<String> retryPickup(TKPDMapParam<String, String> param);

    Observable<String> changeAwb(TKPDMapParam<String, String> param);

    Observable<String> rejectOrderChangeProductVarian(List<EmptyVarianProductEditable> emptyVarianProductEditables,
                                                      TKPDMapParam<String, String> productParam,
                                                      TKPDMapParam<String, String> rejectParam);

    Observable<String> rejectOrderWeightPrice(List<WrongProductPriceWeightEditable> editables,
                                                      TKPDMapParam<String, String> productParam,
                                                      TKPDMapParam<String, String> rejectParam);


}
