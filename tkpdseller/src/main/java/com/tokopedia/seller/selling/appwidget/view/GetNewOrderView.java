package com.tokopedia.seller.selling.appwidget.view;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public interface GetNewOrderView extends CustomerView {

    void onSuccessGetDataOrders(List<DataOrder> dataOrders);

    void onErrorGetDataOrders();
}
