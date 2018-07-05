package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import java.util.List;

public class OrderListInitContract {
     public interface Presenter {

         void getInitData(String orderCategory, int page, int perPage);
     }

    public interface View {
        Context getAppContext();

        void removeProgressBarView();

        void showErrorNetwork(String message);

        void renderTabs(List<String> orderLabelList);
    }
}
