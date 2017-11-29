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

    void onAskSeller(OrderDetailData data);

    void onAskBuyer(OrderDetailData data);

    void onOrderFinished(String message);

    void onOrderCancelled(String message);

    void onRequestCancelOrder(OrderDetailData data);

    void onSellerConfirmShipping(OrderDetailData data);

    void onAcceptOrder(OrderDetailData data);

    void onRequestPickup(OrderDetailData data);

    void onChangeCourier(OrderDetailData data);

    void onRejectOrder(OrderDetailData data);

    void onCancelSearchPeluang(OrderDetailData data);

    void onChangeAwb(OrderDetailData data);

    void showMainViewLoadingPage();

    void hideMainViewLoadingPage();

    void onViewComplaint(String resoId);

    void showErrorSnackbar(String errorMessage);

}
