package com.tokopedia.seller.transaction.neworder.view.appwidget;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderViewWidget;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public interface GetNewOrderView extends CustomerView {

    void onSuccessGetDataOrders(DataOrderViewWidget dataOrderViewWidget);

    void onErrorGetDataOrders();
}
