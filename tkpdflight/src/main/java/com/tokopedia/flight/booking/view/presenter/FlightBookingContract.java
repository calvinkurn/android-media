package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by alvarisi on 11/8/17.
 */

public interface FlightBookingContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

        void onButtonSubmitClicked();
    }
}
