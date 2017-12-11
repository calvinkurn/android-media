package com.tokopedia.flight.orderlist.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by alvarisi on 12/6/17.
 */

public interface FlightOrderListContract {

    interface View extends CustomerView {

        void showGetInitialOrderDataLoading();

        void hideGetInitialOrderDataLoading();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInitialOrderData();
    }
}
