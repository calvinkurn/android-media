package com.tokopedia.flight.orderlist.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;

import java.util.List;

/**
 * Created by alvarisi on 12/6/17.
 */

public interface FlightOrderListContract {

    interface View extends CustomerView {

        void showGetInitialOrderDataLoading();

        void hideGetInitialOrderDataLoading();

        void renderOrderStatus(List<QuickFilterItem> filterItems);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInitialOrderData();
    }
}
