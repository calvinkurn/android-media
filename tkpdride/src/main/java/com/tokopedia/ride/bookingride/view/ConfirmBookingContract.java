package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 3/22/17.
 */

public interface ConfirmBookingContract {
    interface View extends CustomerView{
        RequestParams getParam();

        void showErrorChangeSeat(String message);

        void renderFareEstimate(String fareId, String display);
    }

    interface Presenter extends CustomerPresenter<View>{

        void actionChangeSeatCount();
    }
}
