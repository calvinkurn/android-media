package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 4/21/17.
 */

public interface RideHomeContract {
    interface View extends CustomerView{

    }

    interface Presenter extends CustomerPresenter<View>{

    }
}
