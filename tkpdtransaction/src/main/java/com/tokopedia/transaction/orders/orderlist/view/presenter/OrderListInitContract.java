package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;

import java.util.List;

public class OrderListInitContract {
     public interface Presenter {

         void getInitData();

         void destroyView();
     }

    public interface View {
        Context getAppContext();

        void removeProgressBarView();

        void showErrorNetwork(String message);

        void renderTabs(List<OrderLabelList> orderLabelList);
    }
}
