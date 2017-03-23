package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 3/22/17.
 */

public interface ConfirmBookingContract {
    interface View extends CustomerView{

    }

    interface Presenter extends CustomerPresenter<View>{

    }
}
