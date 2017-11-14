package com.tokopedia.transaction.purchase.detail.activity;

import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

/**
 * Created by kris on 11/13/17. Tokopedia
 */

public interface OrderDetailView {

    void onReceiveDetailData(OrderDetailData data);

    void onError(String errorMessage);

}
