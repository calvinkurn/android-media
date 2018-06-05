package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;

import java.util.List;

/**
 * Created by baghira on 07/05/18.
 */

public class OrderListContract {
    public interface View extends CustomerView {
        void showProcessGetData(OrderCategory orderCategory);

        void renderDataList(List<Order> orderDataList);

        void showFailedResetData(String message);

        void showNoConnectionResetData(String message);

        void showEmptyData(int typeRequest);

        void removeProgressBarView();

        void unregisterScrollListener();

        void showErrorNetwork(String errorMessage);

        void renderEmptyList(int typeRequest);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void getAllOrderData(Context context, OrderCategory orderCategory, int typeRequest, int page);
    }
}
