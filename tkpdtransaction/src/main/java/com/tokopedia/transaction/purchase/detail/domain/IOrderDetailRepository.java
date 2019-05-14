package com.tokopedia.transaction.purchase.detail.domain;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

public interface IOrderDetailRepository {

    String SHOP_ID_KEY = "shop_id";

    String PRODUCT_ID_KEY = "product_id";

    String PRODUCT_DESCRIPTION_KEY = "product_id_description_key";

    Observable<OrderDetailData> requestOrderDetailData(TKPDMapParam<String, Object> params);

    Observable<String> requestCancelOrder(TKPDMapParam<String, String> params);

    Observable<String> cancelReplacement(TKPDMapParam<String, Object> params);

    Observable<String> confirmFinishDeliver(TKPDMapParam<String, String> params);

    Observable<String> confirmDelivery(TKPDMapParam<String, String> params);

    Observable<String> processOrder(TKPDMapParam<String, String> param);

    Observable<String> processShipping(TKPDMapParam<String, String> param);

    Observable<String> changeProduct(Map<String, String> productParam);

    Observable<String> retryPickup(TKPDMapParam<String, String> param);

    Observable<String> changeAwb(TKPDMapParam<String, String> param);

    Observable<String> rejectOrderChangeProductVarian(List<EmptyVarianProductEditable> emptyVarianProductEditables,
                                                      TKPDMapParam<String, String> productParam,
                                                      TKPDMapParam<String, String> rejectParam);

}
