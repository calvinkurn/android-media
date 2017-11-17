package com.tokopedia.transaction.purchase.detail.activity;

import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

/**
 * Created by kris on 11/13/17. Tokopedia
 */

public interface OrderDetailView {

    void onReceiveDetailData(OrderDetailData data);

    void onError(String errorMessage);

    void goToProductInfo(ProductPass productPass);

    void trackShipment(String orderId);

    void showConfirmDialog(String orderId);

    void showComplaintDialog(String shopName, String orderId);

    void onOrderFinished(String message);

}
