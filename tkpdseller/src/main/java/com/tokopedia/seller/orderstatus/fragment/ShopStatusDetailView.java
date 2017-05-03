package com.tokopedia.seller.orderstatus.fragment;

import com.tokopedia.seller.selling.model.orderShipping.OrderCustomer;
import com.tokopedia.seller.selling.model.orderShipping.OrderDetail;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.seller.selling.model.orderShipping.OrderPayment;
import com.tokopedia.seller.selling.model.orderShipping.OrderShipment;

/**
 * Created by kris on 1/25/17. Tokopedia
 */

public interface ShopStatusDetailView {

    void setPaymentData(OrderPayment payment);

    void setCustomerDataToView(OrderCustomer customer);

    void setOrderDetailData(OrderDetail orderDetailData);

    void setDeliveryLocationDetail();

    void setShipmentDetailToView(OrderShipment shipping);

    void showSnackbarError(String errorMessage);

    void setPickUpAddressToView(String pickupAddress);

    void setRefNum(String refNum);

    void hideTrackButton();

    void showTrackButton();

    void showPickUpVisibility();

    void hidePickUpVisibility();

    void hideEditRefNum();

    void showEditRefNum();

    void showNoConnectionError();

    void hideRetryPickUpButton();

    void showRetryPickUpButton();

    String getRefNumber();

    void removesOrderStatusLayoutView();

    void addOrderStatusView(OrderHistory orderHistory);

    void showInfoSnackbar(String message);

    void showProgress();

    void hideProgress();
}
