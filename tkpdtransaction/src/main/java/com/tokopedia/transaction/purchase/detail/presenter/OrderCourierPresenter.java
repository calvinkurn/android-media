package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.transaction.purchase.detail.activity.ConfirmShippingView;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.OrderDetailShipmentModel;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public interface OrderCourierPresenter {

    void setView(ConfirmShippingView view);

    void onGetCourierList(Context context);

    void onConfirmShipping(OrderDetailShipmentModel editableModel);

}
