package com.tokopedia.seller.orderstatus.presenter;

import android.content.Context;

import com.tokopedia.seller.orderstatus.model.InvoiceModel;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;

/**
 * Created by kris on 1/25/17. Tokopedia
 */

public interface StatusDetailPresenter {

    void setOrderDataToInvoiceModel(OrderShippingList orderData);

    void setOrderDataToView();

    InvoiceModel getInvoiceData();

    void setPermission(String permission);

    String generatedDestinationString();

    OrderShippingList getOrderData();

    void editRefNumber(Context context, String refNumber);

    void retryPickup(Context context);

    void onViewDestroyed();
}
