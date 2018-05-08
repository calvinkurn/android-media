package com.tokopedia.transaction.orders.orderlist.view.presenter;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;

import java.util.List;

/**
 * @author Angga.Prasetiyo on 21/04/2016.
 */
public interface OrderListViewListener extends IBaseView {

    void showProcessGetData(OrderCategory orderCategory, int typeRequest);

    void renderDataList(List<Order> orderDataList);

    void showFailedResetData(String message);

    void showNoConnectionResetData(String message);

    void showEmptyData(int typeRequest);

    void removeProgressBarView();

    void unregisterScrollListener();

   /* void notifyAllDataLoaded();*/
}
